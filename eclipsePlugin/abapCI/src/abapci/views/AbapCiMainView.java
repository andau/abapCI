package abapci.views;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.part.ViewPart;

import com.sap.adt.project.AdtCoreProjectServiceFactory;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitAlertStackEntry;

import abapci.AbapCiPlugin;
import abapci.domain.AbapPackageTestState;
import abapci.feature.FeatureFacade;
import abapci.lang.UiTexts;
import abapci.presenter.ContinuousIntegrationPresenter;
import abapci.utils.EditorHandler;
import abapci.utils.InvalidItemUtil;
import abapci.views.actions.ci.AbapGitCiAction;
import abapci.views.actions.ci.AbapUnitCiAction;
import abapci.views.actions.ci.AbapUnitCiActionOpenFirstError;
import abapci.views.actions.ci.AtcCiAction;
import abapci.views.actions.ci.JenkinsCiAction;
import abapci.views.actions.ui.AddAction;
import abapci.views.actions.ui.DeleteAction;

public class AbapCiMainView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "abapci.views.AbapCiMainView";

	private TableViewer viewer;
	private Action jenkinsAction;
	private Action aUnitAction;
	private Action aUnitActionOpenFirstError;
	private Action atcAction;
	private Action addAction;
	private Action deleteAction;
	private Action abapGitAction;

	IPartListener partListener;

	public CLabel statusLabel;

	ContinuousIntegrationPresenter continuousIntegrationPresenter;
	FeatureFacade featureFacade;

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	public void createPartControl(Composite parent) {
		featureFacade = new FeatureFacade();

		Composite entireContainer = new Composite(parent, SWT.NONE);
		entireContainer.setLayout(new GridLayout(1, false));

		viewer = new TableViewer(entireContainer, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		createColumns(viewer);
		viewer.getTable().setHeaderVisible(true);

		statusLabel = new CLabel(entireContainer, SWT.BOTTOM);
		statusLabel.setBounds(0, 10, 500, 10);

		viewer.setContentProvider(ArrayContentProvider.getInstance());

		// TODO Viewer is needed because ViewModel is not yet implemented with
		// full functionality
		// ViewModel.INSTANCE.setMainViewer(viewer);

		// getSite().setSelectionProvider(viewer);

		continuousIntegrationPresenter = AbapCiPlugin.getDefault().continuousIntegrationPresenter;
		if (continuousIntegrationPresenter != null) {
			continuousIntegrationPresenter.setView(this);
			viewer.setInput(continuousIntegrationPresenter.getAbapPackageTestStatesForCurrentProject());
			statusLabel.setText("View inititialised, waiting for first ABAP object activation...");
		} else {
			statusLabel.setText(
					"View was not initialised, please check if the option 'Run Unit tests after an ABAP object is activated' is checked and restart");
		}

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "abapCI.viewer");
		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();
		contributeToActionBars();

	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		IMenuListener menuListener = manager -> this.fillContextMenu(manager);
		menuMgr.addMenuListener(menuListener);
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(jenkinsAction);
		manager.add(aUnitAction);
		manager.add(new Separator());
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(addAction);
		manager.add(deleteAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(aUnitAction);
		manager.add(atcAction);
		manager.add(jenkinsAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(aUnitActionOpenFirstError);
	}

	// TODO fill Action Methoden zusammenfÃƒÆ’Ã‚Â¼gen
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(addAction);
		manager.add(deleteAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(aUnitAction);
		manager.add(atcAction);
		manager.add(jenkinsAction);
		manager.add(abapGitAction);
	}

	private void makeActions() {

		// TODO set Images for actions

		jenkinsAction = new JenkinsCiAction(continuousIntegrationPresenter, "Run jenkins",
				"Run jenkins for selected packages");
		aUnitAction = new AbapUnitCiAction(continuousIntegrationPresenter, "Run ABAP Unittest",
				"Run ABAP Unittest for selected package");
		aUnitActionOpenFirstError = new AbapUnitCiActionOpenFirstError(continuousIntegrationPresenter,
				"Open first failed test(s)", "Open first failed test for the selected package(s)");
		atcAction = new AtcCiAction(continuousIntegrationPresenter, "Run ATC", "Run ATC for selected packages");
		addAction = new AddAction(continuousIntegrationPresenter, UiTexts.LABEL_ACTION_ADD_PACKAGE);
		deleteAction = new DeleteAction(continuousIntegrationPresenter, UiTexts.LABEL_ACTION_REMOVE_PACKAGE);
		abapGitAction = new AbapGitCiAction(continuousIntegrationPresenter, "Open abapGIT", "Open abapGIT in SAP GUI");
	}

	private void createColumns(final TableViewer viewer) {

		int colNumber = 0;

		TableViewerColumn col = createTableViewerColumn("Project name", 150, colNumber);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				AbapPackageTestState p = (AbapPackageTestState) element;
				return p.getProjectName();
			}
		});

		colNumber++;
		col = createTableViewerColumn("Package name", 150, colNumber);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				AbapPackageTestState p = (AbapPackageTestState) element;
				return p.getPackageName();
			}
		});

		colNumber++;
		col = createTableViewerColumn("Unit tests", 90, colNumber);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				AbapPackageTestState p = (AbapPackageTestState) element;
				return p.getUnitTestState().toString();
			}
		});

		colNumber++;
		col = createTableViewerColumn("OK", 40, colNumber);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				AbapPackageTestState p = (AbapPackageTestState) element;
				return Integer.toString(p.getAUnitNumOk());
			}
		});

		colNumber++;
		col = createTableViewerColumn("Err", 40, colNumber);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				AbapPackageTestState p = (AbapPackageTestState) element;
				return Integer.toString(p.getAUnitNumErr());
			}
		});

		colNumber++;
		col = createTableViewerColumn("Sup", 40, colNumber);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				AbapPackageTestState p = (AbapPackageTestState) element;
				return Integer.toString(p.getAUnitNumSuppressed());
			}
		});

		colNumber++;
		col = createTableViewerColumn("Last run", 70, colNumber);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				AbapPackageTestState p = (AbapPackageTestState) element;
				return p.getAUnitLastRun();
			}
		});

		if (featureFacade.getAtcFeature().isActive()) {
			colNumber++;
			col = createTableViewerColumn("ATC state", 90, colNumber);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					AbapPackageTestState p = (AbapPackageTestState) element;
					return p.getAtcInfo();
				}
			});

			colNumber++;
			col = createTableViewerColumn("Err", 40, colNumber);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					AbapPackageTestState p = (AbapPackageTestState) element;
					return p.getAtcNumErr();
				}
			});

			colNumber++;
			col = createTableViewerColumn("Warn", 40, colNumber);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					AbapPackageTestState p = (AbapPackageTestState) element;
					return p.getAtcNumWarn();
				}
			});

			colNumber++;
			col = createTableViewerColumn("Info", 40, colNumber);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					AbapPackageTestState p = (AbapPackageTestState) element;
					return p.getAtcNumInfo();
				}
			});

			colNumber++;
			col = createTableViewerColumn("Sup", 40, colNumber);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					AbapPackageTestState p = (AbapPackageTestState) element;
					return p.getAtcNumSuppressed();
				}
			});

			colNumber++;
			col = createTableViewerColumn("Last run", 70, colNumber);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					AbapPackageTestState p = (AbapPackageTestState) element;
					return p.getAtcLastRun();
				}
			});

			colNumber++;
			col = createTableViewerColumn("Jenkins state", 100, colNumber);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					AbapPackageTestState p = (AbapPackageTestState) element;
					return p.getJenkinsInfo();
				}
			});
		}

		try {
			colNumber++;
			col = createTableViewerColumn("First error", 300, colNumber);
			col.setLabelProvider(new MyHyperlinkLabelProvider(col.getColumn().getParent()));
		} catch (Exception ex) {
			continuousIntegrationPresenter.setStatusMessage("First error column could not be initialised",
					new Color(Display.getCurrent(), new RGB(0, 0, 0)));
		}
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void setViewerInput(List<AbapPackageTestState> allForProject) {
		viewer.setInput(allForProject);
	}

	private final class MyHyperlinkLabelProvider extends StyledCellLabelProvider {
		MyHyperlink m_control;
		IProject project;
		IAbapUnitAlertStackEntry firstStackEntry;

		private MyHyperlinkLabelProvider(Composite parent) {
			m_control = new MyHyperlink(parent, SWT.WRAP);
		}

		@Override
		protected void paint(Event event, Object element) {
			AbapPackageTestState p = (AbapPackageTestState) element;
			IProject[] availableProjects = AdtCoreProjectServiceFactory.createCoreProjectService()
					.getAvailableAdtCoreProjects();

			for (int position = 0; position < availableProjects.length; position++) {
				if (availableProjects[position].getName() == p.getProjectName()) {
					project = availableProjects[position];
				}
			}

			firstStackEntry = p.getFirstFailedUnitTest() != null ? p.getFirstFailedUnitTest().getFirstStackEntry()
					: null;
			m_control.setText(
					firstStackEntry != null ? InvalidItemUtil.getOutputForUnitTest(p.getFirstFailedUnitTest()) : "");

			m_control.addHyperlinkListener(new HyperlinkAdapter() {
				public void linkActivated(HyperlinkEvent e) {
					EditorHandler.open(project, firstStackEntry != null ? firstStackEntry.getUri() : null);
				}
			});

			GC gc = event.gc;
			Rectangle cellRect = new Rectangle(event.x, event.y, event.width, event.height);
			cellRect.width = 4000;

			m_control.paintText(gc, cellRect);
		}
	}

	private class MyHyperlink extends Hyperlink {
		public MyHyperlink(Composite parent, int style) {
			super(parent, style);
			this.setUnderlined(true);
		}

		public void paintText(GC gc, Rectangle bounds) {
			super.paintText(gc, bounds);
		}
	}

}

package abapci.ci.views;

import java.net.URI;
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

import abapci.AbapCiPluginHelper;
import abapci.ci.presenter.ContinuousIntegrationPresenter;
import abapci.domain.AbapPackageTestState;
import abapci.feature.FeatureFacade;
import abapci.feature.activeFeature.AtcFeature;
import abapci.feature.activeFeature.JenkinsFeature;
import abapci.feature.activeFeature.UnitFeature;
import abapci.lang.UiTexts;
import abapci.utils.EditorHandler;
import abapci.utils.InvalidItemUtil;
import abapci.utils.StringUtils;
import abapci.views.actions.ci.AbapGitCiAction;
import abapci.views.actions.ci.AbapUnitCiAction;
import abapci.views.actions.ci.AbapUnitCiActionOpenFirstError;
import abapci.views.actions.ci.AtcCiAction;
import abapci.views.actions.ci.JenkinsCiAction;
import abapci.views.actions.ui.AddAction;
import abapci.views.actions.ui.DeleteAction;
import abapci.views.actions.ui.UpdateAction;

public class AbapCiMainView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "abapci.ci.views.AbapCiMainView";

	Composite entireContainer;
	private TableViewer tableViewer;

	private Action jenkinsAction;
	private Action aUnitAction;
	private Action aUnitActionOpenFirstError;
	private Action atcAction;
	private Action addAction;
	private Action updateAction;
	private Action deleteAction;
	private Action abapGitAction;

	IPartListener partListener;

	public CLabel statusLabel;

	ContinuousIntegrationPresenter continuousIntegrationPresenter;

	private UnitFeature unitFeature;
	private AtcFeature atcFeature;
	private JenkinsFeature jenkinsFeature;

	public AbapCiMainView() {
		initFeatures();
		registerPreferencePropertyChangeListener();
	}

	public AbapCiMainView(boolean forTest) {

	}

	private void registerPreferencePropertyChangeListener() {
		final AbapCiPluginHelper abapCiPluginHelper = new AbapCiPluginHelper();
		abapCiPluginHelper.getPreferenceStore().addPropertyChangeListener(event -> {
			initFeatures();
			createTableColumns();
		});

	}

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		@Override
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	@Override
	public void createPartControl(Composite parent) {

		entireContainer = new Composite(parent, SWT.NONE);
		entireContainer.setLayout(new GridLayout(1, false));

		tableViewer = new TableViewer(entireContainer, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		createTableColumns();

		statusLabel = new CLabel(entireContainer, SWT.BOTTOM);
		statusLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		statusLabel.setBounds(0, 10, tableViewer.getTable().getBounds().width, 10);

		final AbapCiPluginHelper abapCiPluginHelper = new AbapCiPluginHelper();
		continuousIntegrationPresenter = abapCiPluginHelper.getContinousIntegrationPresenter();
		if (continuousIntegrationPresenter != null) {
			continuousIntegrationPresenter.setView(this);
			tableViewer.setInput(continuousIntegrationPresenter.getAbapPackageTestStatesForCurrentProject());
			statusLabel.setText("View inititialised, waiting for first ABAP object activation...");
		} else {
			statusLabel.setText(
					"View was not initialised, please check if the option 'Run Unit tests after an ABAP object is activated' is checked and restart");
		}

		final GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		tableViewer.getControl().setLayoutData(gridData);

		makeActions();
		hookContextMenu();
		contributeToActionBars();

		PlatformUI.getWorkbench().getHelpSystem().setHelp(tableViewer.getControl(), "abapCI.viewer");
		getSite().setSelectionProvider(tableViewer);

	}

	private void initFeatures() {
		final FeatureFacade featureFacade = new FeatureFacade();
		unitFeature = featureFacade.getUnitFeature();
		atcFeature = featureFacade.getAtcFeature();
		jenkinsFeature = featureFacade.getJenkinsFeature();

	}

	private void hookContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		final IMenuListener menuListener = manager -> this.fillContextMenu(manager);
		menuMgr.addMenuListener(menuListener);
		final Menu menu = menuMgr.createContextMenu(tableViewer.getControl());
		tableViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, tableViewer);
	}

	private void contributeToActionBars() {
		final IActionBars bars = getViewSite().getActionBars();
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
		manager.add(updateAction);
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
		manager.add(updateAction);
		manager.add(deleteAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(aUnitAction);
		manager.add(atcAction);
		manager.add(jenkinsAction);
		manager.add(abapGitAction);
	}

	private void makeActions() {

		jenkinsAction = new JenkinsCiAction(continuousIntegrationPresenter, "Run jenkins",
				"Run jenkins for selected packages");
		aUnitAction = new AbapUnitCiAction(continuousIntegrationPresenter, "Run ABAP Unittest",
				"Run ABAP Unittest for selected package");
		aUnitActionOpenFirstError = new AbapUnitCiActionOpenFirstError(continuousIntegrationPresenter,
				"Open first failed test(s)", "Open first failed test for the selected package(s)");
		atcAction = new AtcCiAction(continuousIntegrationPresenter, "Run ATC", "Run ATC for selected packages");
		addAction = new AddAction(continuousIntegrationPresenter, UiTexts.LABEL_ACTION_ADD_PACKAGE);
		updateAction = new UpdateAction(continuousIntegrationPresenter, UiTexts.LABEL_ACTION_UPDATE_PACKAGE);
		deleteAction = new DeleteAction(continuousIntegrationPresenter, UiTexts.LABEL_ACTION_REMOVE_PACKAGE);
		abapGitAction = new AbapGitCiAction(continuousIntegrationPresenter, "Open abapGIT", "Open abapGIT in SAP GUI");
	}

	private void createTableColumns() {

		for (final TableColumn column : tableViewer.getTable().getColumns()) {
			column.dispose();
		}

		int colNumber = 0;

		TableViewerColumn col = createTableViewerColumn("Project name", 150, colNumber);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				final AbapPackageTestState p = (AbapPackageTestState) element;
				return p.getProjectName();
			}
		});

		colNumber++;
		col = createTableViewerColumn("Package name", 150, colNumber);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				final AbapPackageTestState p = (AbapPackageTestState) element;
				return p.getPackageName();
			}
		});

		if (unitFeature.isActive()) {
			colNumber++;
			col = createTableViewerColumn("Unit tests", 90, colNumber);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					final AbapPackageTestState p = (AbapPackageTestState) element;
					return p.getUnitTestState().toString();
				}
			});

			colNumber++;
			col = createTableViewerColumn("#", 40, colNumber);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					final AbapPackageTestState p = (AbapPackageTestState) element;
					return StringUtils.IsNullOrEmpty(p.getAUnitLastRun()) ? StringUtils.EMPTY
							: Integer.toString(p.getNumTests());
				}
			});

			colNumber++;
			col = createTableViewerColumn("Err", 40, colNumber);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					final AbapPackageTestState p = (AbapPackageTestState) element;
					return StringUtils.IsNullOrEmpty(p.getAUnitLastRun()) ? StringUtils.EMPTY
							: Integer.toString(p.getAUnitNumErr());
				}
			});

			colNumber++;
			col = createTableViewerColumn("Sup", 40, colNumber);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					final AbapPackageTestState p = (AbapPackageTestState) element;
					return StringUtils.IsNullOrEmpty(p.getAUnitLastRun()) ? StringUtils.EMPTY
							: Integer.toString(p.getAUnitNumSuppressed());
				}
			});

			colNumber++;
			col = createTableViewerColumn("Last run", 70, colNumber);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					final AbapPackageTestState p = (AbapPackageTestState) element;
					return p.getAUnitLastRun();
				}
			});
		}

		if (atcFeature.isActive()) {
			colNumber++;
			col = createTableViewerColumn("ATC state", 90, colNumber);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					final AbapPackageTestState p = (AbapPackageTestState) element;
					return p.getAtcTestState().toString();
				}
			});

			colNumber++;
			col = createTableViewerColumn("#", 40, colNumber);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					final AbapPackageTestState p = (AbapPackageTestState) element;
					return StringUtils.IsNullOrEmpty(p.getAtcLastRun()) ? StringUtils.EMPTY
							: Integer.toString(p.getAtcNumFiles());
				}
			});

			colNumber++;
			col = createTableViewerColumn("Err", 40, colNumber);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					final AbapPackageTestState p = (AbapPackageTestState) element;
					return StringUtils.IsNullOrEmpty(p.getAtcLastRun()) ? StringUtils.EMPTY
							: Integer.toString(p.getAtcNumErr());
				}
			});

			colNumber++;
			col = createTableViewerColumn("Warn", 40, colNumber);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					final AbapPackageTestState p = (AbapPackageTestState) element;
					return StringUtils.IsNullOrEmpty(p.getAtcLastRun()) ? StringUtils.EMPTY
							: Integer.toString(p.getAtcNumWarn());
				}
			});

			colNumber++;
			col = createTableViewerColumn("Info", 40, colNumber);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					final AbapPackageTestState p = (AbapPackageTestState) element;
					return StringUtils.IsNullOrEmpty(p.getAtcLastRun()) ? StringUtils.EMPTY
							: Integer.toString(p.getAtcNumInfo());
				}
			});

			colNumber++;
			col = createTableViewerColumn("Sup", 40, colNumber);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					final AbapPackageTestState p = (AbapPackageTestState) element;
					return StringUtils.IsNullOrEmpty(p.getAtcLastRun()) ? StringUtils.EMPTY
							: Integer.toString(p.getAtcNumSuppressed());
				}
			});

			colNumber++;
			col = createTableViewerColumn("Last run", 70, colNumber);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					final AbapPackageTestState p = (AbapPackageTestState) element;
					return p.getAtcLastRun();
				}
			});
		}

		if (jenkinsFeature.isActive()) {
			colNumber++;
			col = createTableViewerColumn("Jenkins state", 100, colNumber);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					final AbapPackageTestState p = (AbapPackageTestState) element;
					return p.getJenkinsInfo();
				}
			});
		}

		try {
			colNumber++;
			col = createTableViewerColumn("First error", 300, colNumber);
			col.setLabelProvider(new MyHyperlinkLabelProvider(col.getColumn().getParent()));
		} catch (final Exception ex) {
			continuousIntegrationPresenter.setStatusMessage("First error column could not be initialised",
					new Color(Display.getCurrent(), new RGB(0, 0, 0)));
		}
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	@Override
	public void setFocus() {
		tableViewer.getControl().setFocus();
	}

	public void setViewerInput(List<AbapPackageTestState> allForProject) {
		tableViewer.setInput(allForProject);
	}

	private final class MyHyperlinkLabelProvider extends StyledCellLabelProvider {
		MyHyperlink m_control;
		IProject project;
		URI uriToFirstError;

		private MyHyperlinkLabelProvider(Composite parent) {
			m_control = new MyHyperlink(parent, SWT.WRAP);
		}

		@Override
		protected void paint(Event event, Object element) {
			final AbapPackageTestState p = (AbapPackageTestState) element;
			final IProject[] availableProjects = AdtCoreProjectServiceFactory.createCoreProjectService()
					.getAvailableAdtCoreProjects();

			for (final IProject availableProject : availableProjects) {
				if (availableProject.getName() == p.getProjectName()) {
					project = availableProject;
				}
			}

			uriToFirstError = p.getFirstFailedUnitTest() != null ? p.getFirstFailedUnitTest().getUriToError() : null;
			m_control.setText(
					uriToFirstError != null ? InvalidItemUtil.getOutputForUnitTest(p.getFirstFailedUnitTest()) : "");

			if (atcFeature.isActive() && uriToFirstError == null) {
				uriToFirstError = p.getFirstFailedAtc() != null ? p.getFirstFailedAtc().getUriToError() : null;
				m_control.setText(
						uriToFirstError != null ? InvalidItemUtil.getOutputForAtcTest(p.getFirstFailedAtc()) : "");
			}
			m_control.addHyperlinkListener(new HyperlinkAdapter() {
				@Override
				public void linkActivated(HyperlinkEvent e) {
					EditorHandler.open(project, uriToFirstError);
				}
			});

			final GC gc = event.gc;
			final Rectangle cellRect = new Rectangle(event.x, event.y, event.width, event.height);
			cellRect.width = 4000;

			m_control.paintText(gc, cellRect);
		}
	}

	private class MyHyperlink extends Hyperlink {
		public MyHyperlink(Composite parent, int style) {
			super(parent, style);
			this.setUnderlined(true);
		}

		@Override
		public void paintText(GC gc, Rectangle bounds) {
			super.paintText(gc, bounds);
		}
	}

}

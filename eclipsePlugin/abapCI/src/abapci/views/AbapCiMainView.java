package abapci.views;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.service.prefs.BackingStoreException;

import com.sap.adt.project.AdtCoreProjectServiceFactory;
import com.sap.adt.tools.core.internal.AbapProjectService;

import abapci.AbapCiPlugin;
import abapci.domain.AbapPackageTestState;
import abapci.lang.UiTexts;
import abapci.preferences.PreferenceConstants;
import abapci.views.actions.ci.AbapGitCiAction;
import abapci.views.actions.ci.AbapUnitCiAction;
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
	private Action atcAction;
	private Action addAction;
	private Action deleteAction;
	private Action abapGitAction;

	IPartListener partListener;

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

		Composite entireContainer = new Composite(parent, SWT.NONE);
		entireContainer.setLayout(new GridLayout(1, false));

		viewer = new TableViewer(entireContainer, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		createColumns(viewer);
		viewer.getTable().setHeaderVisible(true);

		Button abapGitButton = new Button(entireContainer, SWT.NONE);
		abapGitButton.setBounds(0, 10, 200, 200);
		abapGitButton.setText("Launch abapGit");
		Listener listener = e -> {
			if (e.type == SWT.Selection) {
				abapGitAction.run();
			}
		};
		abapGitButton.addListener(SWT.Selection, listener);
		viewer.setContentProvider(ArrayContentProvider.getInstance());

		viewer.setInput(ViewModel.INSTANCE.getPackageTestStates());

		// TODO Viewer is needed because ViewModel is not yet implemented with
		// full functionality
		ViewModel.INSTANCE.setMainViewer(viewer);

		getSite().setSelectionProvider(viewer);

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

		if (!checkActualAbapProject()) {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			ProjectSelectionDialog projectSelectionDialog = new ProjectSelectionDialog(shell, new LabelProvider());
			if (projectSelectionDialog.open() == Window.OK) {
				Object[] result = projectSelectionDialog.getResult();
				if (result.length == 1) {
					setProjectName((String) result[0]);
				}
			}
		}
	}

	private void setProjectName(String projectName) {
		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(AbapCiPlugin.PLUGIN_ID);

		prefs.put(PreferenceConstants.PREF_DEV_PROJECT, projectName);

		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean checkActualAbapProject() {
		IPreferenceStore prefs = AbapCiPlugin.getDefault().getPreferenceStore();
		String actualDevProject = prefs.getString(PreferenceConstants.PREF_DEV_PROJECT);

		IProject project = AdtCoreProjectServiceFactory.createCoreProjectService().findProject(actualDevProject);
		return AbapProjectService.getInstance().isAbapProject(project);
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

		jenkinsAction = new JenkinsCiAction("Run jenkins", "Run jenkins for selected packages");
		aUnitAction = new AbapUnitCiAction("Run ABAP Unittest", "Run ABAP Unittest for selected package");
		atcAction = new AtcCiAction("Run ATC", "Run ATC for selected packages");
		addAction = new AddAction(UiTexts.LABEL_ACTION_ADD_PACKAGE);
		deleteAction = new DeleteAction(UiTexts.LABEL_ACTION_REMOVE_PACKAGE);
		abapGitAction = new AbapGitCiAction("Open abapGIT", "Open abapGIT in SAP GUI");
	}

	private void createColumns(final TableViewer viewer) {
		String[] titles = { "ABAP package", "Unit test", "Sup", "Last run", "ATC state", "Sup", "Last run",
				"Jenkins state" };
		int[] bounds = { 150, 90, 40, 70, 90, 40, 70, 100 };

		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				AbapPackageTestState p = (AbapPackageTestState) element;
				return p.getPackageName();
			}
		});

		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				AbapPackageTestState p = (AbapPackageTestState) element;
				return p.getAUnitInfo();
			}
		});

		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				AbapPackageTestState p = (AbapPackageTestState) element;
				return p.getAUnitNumSuppressed();
			}
		});

		col = createTableViewerColumn(titles[3], bounds[3], 3);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				AbapPackageTestState p = (AbapPackageTestState) element;
				return p.getAUnitLastRun();
			}
		});

		col = createTableViewerColumn(titles[4], bounds[4], 4);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				AbapPackageTestState p = (AbapPackageTestState) element;
				return p.getAtcInfo();
			}
		});

		col = createTableViewerColumn(titles[5], bounds[5], 5);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				AbapPackageTestState p = (AbapPackageTestState) element;
				return p.getAtcNumSuppressed();
			}
		});

		col = createTableViewerColumn(titles[6], bounds[6], 6);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				AbapPackageTestState p = (AbapPackageTestState) element;
				return p.getAtcLastRun();
			}
		});

		col = createTableViewerColumn(titles[7], bounds[7], 7);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				AbapPackageTestState p = (AbapPackageTestState) element;
				return p.getJenkinsInfo();
			}
		});
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
}

package abapci.views;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
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
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import abapci.Activator;
import abapci.GeneralResourceChangeListener;
import abapci.Domain.AbapPackageTestState;
import abapci.jobs.RepeatingAUnitJob;
import abapci.lang.UiTexts;
import abapci.preferences.PreferenceConstants;
import abapci.views.actions.ci.AbapUnitCiAction;
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
	private Action addAction;
	private Action deleteAction;

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	public AbapCiMainView() {
	}

	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		createColumns(parent, viewer);
		final Table table = viewer.getTable();
        table.setHeaderVisible(true);
         
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		
		viewer.setInput(ViewModel.INSTANCE.getPackageTestStates());
		
		//TODO Viewer is needed because ViewModel is not yet implemented with full functionality 
		ViewModel.INSTANCE.setView(viewer);
		
		getSite().setSelectionProvider(viewer);
		
		
		GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        viewer.getControl().setLayoutData(gridData);
        

		//viewer.setLabelProvider(new ViewLabelProvider());

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "abapCI.viewer");
		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();
		contributeToActionBars();
		    
	    RepeatingAUnitJob job = new RepeatingAUnitJob(); 
	    job.schedule(6000); 			
		
		IResourceChangeListener listener = new GeneralResourceChangeListener();
		   ResourcesPlugin.getWorkspace().addResourceChangeListener(
		      listener, IResourceChangeEvent.POST_CHANGE);

	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				AbapCiMainView.this.fillContextMenu(manager);
			}
		});
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
		manager.add(jenkinsAction);
		manager.add(aUnitAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(addAction);
		manager.add(deleteAction);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(jenkinsAction);
		manager.add(aUnitAction);
	}

	private void makeActions() {

		//TODO set Images for actions 
		
		jenkinsAction = new JenkinsCiAction("Run jenkins", "Run jenkins for ABAP package"); 		
		aUnitAction = new AbapUnitCiAction("Run ABAP Unittest", "Run ABAP Unittest for given package");
		addAction = new AddAction(UiTexts.LABEL_ACTION_ADD_PACKAGE); 		
		deleteAction = new DeleteAction(UiTexts.LABEL_ACTION_REMOVE_PACKAGE); 
	}
	
	private void createColumns(final Composite parent, final TableViewer viewer) {
        String[] titles = { "ABAP package", "Unit test", "Last run", "Jenkins state"};
        int[] bounds = { 150, 70, 70, 200 };

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
                return p.getAbapState();
            }
        });

        col = createTableViewerColumn(titles[2], bounds[2], 2);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                AbapPackageTestState p = (AbapPackageTestState) element;
                return p.getLastRun();
            }
        });

        col = createTableViewerColumn(titles[3], bounds[3], 2);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                AbapPackageTestState p = (AbapPackageTestState) element;
                return p.getJenkinsState();
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

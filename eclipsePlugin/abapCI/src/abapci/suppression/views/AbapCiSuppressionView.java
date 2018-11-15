package abapci.suppression.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

import abapci.ci.views.ViewModel; 
import abapci.domain.Suppression;
import abapci.lang.UiTexts;
import abapci.views.actions.ui.AddSuppressionAction;
import abapci.views.actions.ui.DeleteSuppressionAction;

public class AbapCiSuppressionView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "abapci.views.AbapCiSuppressionView";

	private TableViewer viewer;
	private Action addSuppressionAction; 
	private Action deleteSuppressionAction; 
	
	public AbapCiSuppressionView() {
		ViewModel.INSTANCE.getOverallTestState();
	}

	public void createPartControl(Composite parent) {
		

		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.getTable().setHeaderVisible(true);
		createColumns(viewer);

		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setInput(ViewModel.INSTANCE.getSuppressions());
		getSite().setSelectionProvider(viewer);

		ViewModel.INSTANCE.setSuppressViewer(viewer);

		makeActions(); 
		hookContextMenu();
		contributeToActionBars();
	}

	private void createColumns(TableViewer viewer) {
		String[] titles = {"Class name"};
		int[] bounds = {250};

		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Suppression s = (Suppression) element;
				return s.getClassName();
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


	@Override
	public void setFocus() {
		// TODO Auto-generated method stub	
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

	
	private void fillContextMenu(IMenuManager manager) {
		manager.add(addSuppressionAction);
		manager.add(deleteSuppressionAction);
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(addSuppressionAction);
		manager.add(deleteSuppressionAction);
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(addSuppressionAction);
		manager.add(deleteSuppressionAction);
	}

	
	private void makeActions() {

		// TODO set Images for actions

		addSuppressionAction = new AddSuppressionAction(UiTexts.LABEL_ACTION_ADD_SUPPRESSION);
		deleteSuppressionAction = new DeleteSuppressionAction(UiTexts.LABEL_ACTION_REMOVE_SUPPRESSION);
	}


}

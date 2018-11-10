package abapci.coloredProject.view;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import abapci.AbapCiPluginHelper;
import abapci.coloredProject.model.ColoredProject;
import abapci.coloredProject.presenter.ColoredProjectsPresenter;
import abapci.lang.UiTexts;
import abapci.utils.ColorChooser;
import abapci.views.actions.ui.DeleteColoredProjectAction;

public class AbapCiColoredProjectView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "abapci.views.AbapCiColoredProjectView";

	private CLabel statusLabel;

	private TableViewer viewer;
	private Action addAction;
	private Action updateAction;
	private Action deleteAction;
	private final AbapCiPluginHelper abapCiPluginHelper;

	ColoredProjectsPresenter coloredProjectsPresenter;

	public AbapCiColoredProjectView() {
		abapCiPluginHelper = new AbapCiPluginHelper();
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

		coloredProjectsPresenter = abapCiPluginHelper.getColoredProjectsPresenter();
		coloredProjectsPresenter.setView(this);

		Composite entireContainer = new Composite(parent, SWT.NONE);
		entireContainer.setLayout(new GridLayout(1, false));

		viewer = new TableViewer(entireContainer, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		createColumns(viewer);
		viewer.getTable().setHeaderVisible(true);

		statusLabel = new CLabel(entireContainer, SWT.BOTTOM);
		statusLabel.setBounds(0, 10, 200, 10);
		statusLabel.setText(
				"By adding a project and assigning a color the development objects of this project are colored with the assigned color");

		viewer.setContentProvider(ArrayContentProvider.getInstance());
		getSite().setSelectionProvider(viewer);

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);

		makeActions();
		hookContextMenu();
		contributeToActionBars();

		coloredProjectsPresenter.setViewerInput();

	}

	private void createColumns(TableViewer viewer) {
		String[] titles = { "ABAP Project", "Color", "Suppressed" };
		int[] bounds = { 250, 150, 50 };

		TableViewerColumn col0 = createTableViewerColumn(titles[0], bounds[0], 0);
		col0.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ColoredProject c = (ColoredProject) element;
				return c.getName();
			}

			@Override
			public Color getBackground(Object element) {
				ColoredProject c = (ColoredProject) element;
				return c.getColor();
			}

			@Override
			public Color getForeground(Object element) {
				ColoredProject coloredProject = (ColoredProject) element;
				ColorChooser colorChooser = new ColorChooser();
				return colorChooser.getContrastColor(coloredProject.getColor());
			}

		});

		TableViewerColumn col1 = createTableViewerColumn(titles[1], bounds[1], 1);
		col1.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				ColoredProject c = (ColoredProject) element;
				return c.getColor().toString();
			}

			@Override
			public Color getBackground(Object element) {
				ColoredProject c = (ColoredProject) element;
				return c.getColor();
			}

			@Override
			public Color getForeground(Object element) {
				ColoredProject coloredProject = (ColoredProject) element;
				ColorChooser colorChooser = new ColorChooser();
				return colorChooser.getContrastColor(coloredProject.getColor());
			}

		});

		TableViewerColumn col2 = createTableViewerColumn(titles[2], bounds[2], 2);
		col2.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				ColoredProject c = (ColoredProject) element;
				return String.valueOf(c.isSuppressedColoring());
			}

			@Override
			public Color getBackground(Object element) {
				ColoredProject c = (ColoredProject) element;
				return c.getColor();
			}

			@Override
			public Color getForeground(Object element) {
				ColoredProject coloredProject = (ColoredProject) element;
				ColorChooser colorChooser = new ColorChooser();
				return colorChooser.getContrastColor(coloredProject.getColor());
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
		manager.add(addAction);
		manager.add(updateAction);
		manager.add(deleteAction);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(addAction);
		manager.add(updateAction);
		manager.add(deleteAction);
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(addAction);
		manager.add(updateAction);
		manager.add(deleteAction);
	}

	private void makeActions() {

		// TODO set Images for actions

		addAction = new AddColoredProjectAction(coloredProjectsPresenter, UiTexts.LABEL_ACTION_ADD_COLORED_PROJECT);
		updateAction = new UpdateColoredProjectAction(coloredProjectsPresenter,
				UiTexts.LABEL_ACTION_UPDATE_COLORED_PROJECT);
		deleteAction = new DeleteColoredProjectAction(coloredProjectsPresenter,
				UiTexts.LABEL_ACTION_REMOVE_COLORED_PROJECT);
	}

	public void setViewerInput(List<ColoredProject> coloredProjects) {
		viewer.setInput(coloredProjects);
	}

	public void setStatusLabelText(String statusLabelText) {
		statusLabel.setText(statusLabelText);
	}

	public void setStatusLabelForeground(Color color) {
		statusLabel.setForeground(color);
	}

}

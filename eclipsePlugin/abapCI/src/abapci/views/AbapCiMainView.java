package abapci.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.ViewPart;

import abapci.handlers.JenkinsHandler;

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

	/**
	 * The constructor.
	 */
	public AbapCiMainView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setInput(new String[] { "ZABAP_UI", "ZABAP_GIT", "ZCOMMONS" });

		viewer.setLabelProvider(new ViewLabelProvider());

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "abapCI.viewer");
		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();
		contributeToActionBars();
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
		jenkinsAction = new Action() {
			public void run() {

				// TODO errorhandling for wrong Url, username, password, ...

				try {
					Map<String, String> packageNames = getSelectedPackages();

					new JenkinsHandler().execute(new ExecutionEvent(null, packageNames, null, null));
					showMessage(String.format("Jenkins jobs for packages %s started",
							packageNames.entrySet().iterator().next().getValue()));

				} catch (Exception e) {
					// TODO Auto-generated catch block
					showMessage("Please select exactly one item of the package list");
				}

				// TODO Refactor duplicate code
			}

			private Map<String, String> getSelectedPackages() {

				ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.getSelection();
				String packageName = ((StructuredSelection) selection).getFirstElement().toString();

				Map<String, String> packageNames = new HashMap<String, String>();
				packageNames.put("1", packageName);

				return packageNames;
			}

		};
		jenkinsAction.setText("Run jenkins");
		jenkinsAction.setToolTipText("Run jenkins for SAP package");
		// TODO set image jenkinsAction.setImageDescriptor()));

		aUnitAction = new Action() {
			public void run() {

				try {
					Map<String, String> packageNames = getSelectedPackages();

					new JenkinsHandler().execute(new ExecutionEvent(null, packageNames, null, null));

					showMessage(String.format("ABAP Unittests for packages %s started",
							packageNames.entrySet().iterator().next().getValue()));

				} catch (Exception e) {
					// TODO Auto-generated catch block
					showMessage("Please select exactly one item of the package list");
				}

			}

			// TODO Refactor duplicate code
			private Map<String, String> getSelectedPackages() {
				ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.getSelection();
				String packageName = ((StructuredSelection) selection).getFirstElement().toString();

				Map<String, String> packageNames = new HashMap<String, String>();
				packageNames.put("1", packageName);

				return packageNames;
			}

		};

		aUnitAction.setText("Run ABAP Unittest");
		aUnitAction.setToolTipText("Run ABAP Unittest for given package");
		// TODO set image aUnitAction.setImageDescriptor()));

		// aUnitAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
		// getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		addAction = new Action() {
			public void run() {
				
				String[] currentPackages = (String[]) viewer.getInput();
				ArrayList<String> currentPackagesList = new ArrayList<String>(Arrays.asList(currentPackages));

				InputDialog packageNameDialog = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Add new package",
			            "Adding a new abap package", "", null); 
				
				if (packageNameDialog.open() == Window.OK) {
						currentPackagesList.add(packageNameDialog.getValue());
						viewer.setInput((String[]) currentPackagesList.toArray(currentPackages));
				} 
			}
		};
		addAction.setText(("Insert element"));

		deleteAction = new Action() {

			public void run() {

				ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.getSelection();
				String packageName = ((StructuredSelection) selection).getFirstElement().toString();

				String[] currentPackages = (String[]) viewer.getInput();
				ArrayList<String> currentPackagesList = new ArrayList<String>(Arrays.asList(currentPackages));

				Predicate<String> packageNamePredicate = p -> p.equals(packageName);
				currentPackagesList.removeIf(packageNamePredicate);
				viewer.setInput(currentPackagesList.toArray(new String[1]));

			}
		};
		deleteAction.setText(("Delete element"));

	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "ABAP CI View", message);
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}
}

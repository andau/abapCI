package abapci;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.Exception.ActiveEditorNotSetException;
import abapci.ci.model.ContinuousIntegrationModel;
import abapci.ci.presenter.ContinuousIntegrationPresenter;
import abapci.coloredProject.colorChanger.ColorChanger;
import abapci.coloredProject.colorChanger.ColorChangerFactory;
import abapci.coloredProject.colorChanger.ColorChangerType;
import abapci.coloredProject.exeption.ColorChangerNotImplementedException;
import abapci.coloredProject.exeption.ProjectColorNotSetException;
import abapci.coloredProject.general.DisplayColor;
import abapci.coloredProject.general.IStatusBarWidget;
import abapci.coloredProject.general.WorkspaceColorConfiguration;
import abapci.coloredProject.model.ColoredProjectModel;
import abapci.coloredProject.presenter.ColoredProjectsPresenter;
import abapci.feature.FeatureFacade;
import abapci.feature.activeFeature.ColoredProjectFeature;

/**
 * The activator class controls the plug-in life cycle
 */
public class AbapCiPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.abapci.core"; //$NON-NLS-1$
	// The shared instance
	private static AbapCiPlugin plugin;

	private static ContinuousIntegrationPresenter continuousIntegrationPresenter;
	private static ColoredProjectsPresenter coloredProjectsPresenter;

	private static IResourceChangeListener resourceChangeListener;
	private static IPartListener2 partListener;
	private static WorkspaceColorConfiguration workspaceColorConfiguration;

	private FeatureFacade featureFacade;

	private IStatusBarWidget statusBarWidget;

	/**
	 * The constructor
	 */
	public AbapCiPlugin() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		featureFacade = new FeatureFacade();

		if (featureFacade.getUnitFeature().isActive() || featureFacade.getAtcFeature().isActive()
				|| featureFacade.getAtcFeature().isRunActivatedObjects()) {
			continuousIntegrationPresenter = new ContinuousIntegrationPresenter(null, new ContinuousIntegrationModel(),
					null);

			initializeResourceChangeListener();

		}

		try {
			workspaceColorConfiguration = new WorkspaceColorConfiguration(true);

			coloredProjectsPresenter = new ColoredProjectsPresenter(null, new ColoredProjectModel());
			registerPreferencePropertyChangeListener();
			updateProjectColors();
		} catch (final Exception ex) {
			ex.printStackTrace();
			// if here is a problem we will go on as these are no critical feature
		}
		initializePartChangeListener();

		final ICommandService service = PlatformUI.getWorkbench().getService(ICommandService.class);
		service.addExecutionListener(new ActivationExecutionListener());

	}

	private void registerPreferencePropertyChangeListener() {

		getPreferenceStore().addPropertyChangeListener(event -> {
			try {
				workspaceColorConfiguration = new WorkspaceColorConfiguration(true);
			} catch (final AbapCiColoredProjectFileParseException e) {
				// if here is a problem we will go on as these are no critical feature
				e.printStackTrace();
			}
		});
	}

	private void updateProjectColors() throws ColorChangerNotImplementedException,
			AbapCiColoredProjectFileParseException, ActiveEditorNotSetException, ProjectColorNotSetException {

		final ColoredProjectFeature coloredProjectFeature = featureFacade.getColoredProjectFeature();
		if (coloredProjectFeature.isTitleIconActive()) {
			final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			activePage.getActiveEditor();

			final IEditorReference[] editorReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().getEditorReferences();

			final ColorChangerFactory colorChangerFactory = new ColorChangerFactory();

			for (final IEditorReference editorReference : editorReferences) {
				final DisplayColor displayColor = workspaceColorConfiguration
						.getColoring(GeneralProjectUtil.getProject(editorReference.getEditor(true)));
				final ColorChanger colorChanger = colorChangerFactory.create(ColorChangerType.TITLE_ICON,
						editorReference.getEditor(true), coloredProjectFeature, displayColor.getTitleIconColor());
				colorChanger.change();
			}
		}

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static AbapCiPlugin getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public void initializeResourceChangeListener() {
		if (resourceChangeListener == null) {
			resourceChangeListener = new GeneralResourceChangeListener(continuousIntegrationPresenter);
			ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener,
					IResourceChangeEvent.POST_CHANGE);
		}

	}

	public void initializePartChangeListener() {
		if (partListener == null) {
			partListener = new EditorActivationListener();
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(partListener);
		}

	}

	public void attachStatusBarWidget(IStatusBarWidget statusBarWidget) {
		this.statusBarWidget = statusBarWidget;
	}

	public IStatusBarWidget getStatusBarWidget() {
		return statusBarWidget;
	}

	public ColoredProjectsPresenter getColoredProjectsPresenter() {
		return coloredProjectsPresenter;
	}

	public static void resetWorkspaceColorConfiguration() throws AbapCiColoredProjectFileParseException {
		workspaceColorConfiguration = new WorkspaceColorConfiguration(true);
	}

	public static WorkspaceColorConfiguration getWorkspaceColorConfiguration() {
		return workspaceColorConfiguration;
	}

	public static ContinuousIntegrationPresenter getContinuousIntegrationPresenter() {
		return continuousIntegrationPresenter;
	}
}

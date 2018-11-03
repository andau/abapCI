package abapci;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import abapci.coloredProject.general.IStatusBarWidget;
import abapci.coloredProject.general.StatusBarWidget;
import abapci.coloredProject.model.ColoredProjectModel;
import abapci.coloredProject.presenter.ColoredProjectsPresenter;
import abapci.feature.FeatureFacade;
import abapci.model.ContinuousIntegrationModel;
import abapci.presenter.ContinuousIntegrationPresenter;
import abapci.presenter.GeneralThemePresenter;

/**
 * The activator class controls the plug-in life cycle
 */
public class AbapCiPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.abapci.core"; //$NON-NLS-1$
	// The shared instance
	private static AbapCiPlugin plugin;

	public ContinuousIntegrationPresenter continuousIntegrationPresenter;
	private ColoredProjectsPresenter coloredProjectsPresenter;


	private static IResourceChangeListener resourceChangeListener;
	private static IPartListener2 partListener;

	private FeatureFacade featureFacade;
	private GeneralThemePresenter generalThemePresenter;
	
	private IStatusBarWidget statusBarWidget; 

	/**
	 * The constructor
	 */
	public AbapCiPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		featureFacade = new FeatureFacade();
		if (featureFacade.getUnitFeature().isActive() || featureFacade.getAtcFeature().isActive()
				|| featureFacade.getAtcFeature().isRunActivatedObjects()) {
			generalThemePresenter = new GeneralThemePresenter(new ColoredProjectModel());
			continuousIntegrationPresenter = new ContinuousIntegrationPresenter(null, new ContinuousIntegrationModel(),
					null);

			initializeResourceChangeListener();
			
		}
		

		try {
			coloredProjectsPresenter = new ColoredProjectsPresenter(null, new ColoredProjectModel());
		} catch (Exception ex) {
			// if here is a problem we will go on as this is no critical feature
		}
		initializePartChangeListener();

		ICommandService service = PlatformUI.getWorkbench().getService(ICommandService.class);
		service.addExecutionListener(new ActivationExecutionListener());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
	 * BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static AbapCiPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative
	 * path
	 *
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
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
			partListener = new EditorActivationListener(generalThemePresenter);
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(partListener);
		}

	}
	
	public void attachStatusBarWidget(IStatusBarWidget statusBarWidget)
	{
		this.statusBarWidget = statusBarWidget; 
	}
	
	public IStatusBarWidget getStatusBarWidget() 
	{
		return statusBarWidget; 
	}

	public ColoredProjectsPresenter getColoredProjectsPresenter() {
		return coloredProjectsPresenter; 
	}
}

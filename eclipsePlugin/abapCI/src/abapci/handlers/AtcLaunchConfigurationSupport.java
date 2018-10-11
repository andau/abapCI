package abapci.handlers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;

import com.sap.adt.atc.AtcCheckVariantMode;
import com.sap.adt.atc.IAtcCheckableItem;
import com.sap.adt.atc.IAtcSetting;

public class AtcLaunchConfigurationSupport {
	private final ILaunchManager launchManager;

	public AtcLaunchConfigurationSupport(ILaunchManager launchManager) {
		this.launchManager = launchManager;
	}

	public ILaunchConfiguration tryToGetExistingConfig(String projectName, IAtcSetting setting,
			Set<IAtcCheckableItem> adtItems) throws CoreException {
		ILaunchConfiguration[] existingLaunchConfigurations;
		Set<String> urisAsStringSet = this.itemsToStringSet(adtItems);
		ILaunchConfigurationType type = this.getConfigurationType();
		ILaunchConfiguration[] arriLaunchConfiguration = existingLaunchConfigurations = this.launchManager
				.getLaunchConfigurations(type);
		int n = arriLaunchConfiguration.length;
		int n2 = 0;
		while (n2 < n) {
			ILaunchConfiguration existingLaunchConfiguration = arriLaunchConfiguration[n2];
			if (this.isTheSame(existingLaunchConfiguration, urisAsStringSet, projectName, setting)) {
				return existingLaunchConfiguration;
			}
			++n2;
		}
		return null;
	}

	public ILaunchConfiguration createConfiguration(String projectName, IAtcSetting setting,
			Set<IAtcCheckableItem> adtItems) throws CoreException {
		Set<String> urisAsStringSet = this.itemsToStringSet(adtItems);
		ILaunchConfigurationType type = this.getConfigurationType();
		String launchConfigName = this.createName(projectName, setting, adtItems);
		launchConfigName = this.launchManager.generateLaunchConfigurationName(launchConfigName);
		ILaunchConfigurationWorkingCopy wcopy = type.newInstance(null, launchConfigName);
		wcopy.setAttribute("uris", urisAsStringSet);
		wcopy.setAttribute("projectName", projectName);
		if (setting.getVariantMode() == AtcCheckVariantMode.USER_SPECIFIC) {
			wcopy.setAttribute("checkVariant", setting.getVariantName());
		}
		wcopy.setAttribute("reportFindingsWithRepositoryExemptions", setting.isReportFindingsWithCodeExemptions());
		wcopy.setAttribute("reportFindingsWithCodeExemptions", setting.isReportFindingsWithRepositoryExemptions());
		if (adtItems.size() == 1) {
			IAtcCheckableItem adtCheckableItem = adtItems.iterator().next();
			wcopy.setAttribute("atcObjectName", adtCheckableItem.getName());
			if (adtCheckableItem instanceof IAtcCheckableItemExtended) {
				IAtcCheckableItemExtended extended = (IAtcCheckableItemExtended) adtCheckableItem;
				wcopy.setAttribute("atcObjectType", extended.getType());
			}
		}
		return wcopy.doSave();
	}

	private boolean isTheSame(ILaunchConfiguration launchConfiguration, Set<String> urisAsStringSet, String projectName,
			IAtcSetting setting) {
		block13: {
			boolean storedBoolean = false;
			block12: {
				block11: {
					String storedName;
					block10: {
						block9: {
							try {
								storedName = launchConfiguration.getAttribute("projectName", "");
								if (storedName.equals(projectName))
									break block9;
								return false;
							} catch (CoreException coreException) {
								return false;
							}
						}
						if (setting.getVariantMode() != AtcCheckVariantMode.USER_SPECIFIC)
							break block10;
						try {
							storedName = launchConfiguration.getAttribute("checkVariant", "");
						} catch (CoreException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (!storedName.equals(setting.getVariantName())) {
							return false;
						}
						break block11;
					}
					try {
						storedName = launchConfiguration.getAttribute("checkVariant", "");
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (storedName.equals(""))
						break block11;
					return false;
				}
				try {
					storedBoolean = launchConfiguration.getAttribute("reportFindingsWithCodeExemptions", false);
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (setting.isReportFindingsWithRepositoryExemptions() == storedBoolean)
					break block12;
				return false;
			}
			try {
				storedBoolean = launchConfiguration.getAttribute("reportFindingsWithRepositoryExemptions", false);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (setting.isReportFindingsWithCodeExemptions() == storedBoolean)
				break block13;
			return false;
		}
		Set existingLaunchConfigurationUrisAsStringSet;
		try {
			existingLaunchConfigurationUrisAsStringSet = launchConfiguration.getAttribute("uris",
					Collections.emptySet());
			if (!existingLaunchConfigurationUrisAsStringSet.equals(urisAsStringSet)) {
				return false;
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	private Set<String> itemsToStringSet(Set<IAtcCheckableItem> adtItems) {
		HashSet<String> urisAsString = new HashSet<String>(adtItems.size());
		for (IAtcCheckableItem adtItem : adtItems) {
			urisAsString.add(adtItem.getUri().toString());
		}
		return urisAsString;
	}

	private String createName(String projectName, IAtcSetting setting, Set<IAtcCheckableItem> adtItems) {
		StringBuilder stringBuilder = new StringBuilder();
		Iterator<IAtcCheckableItem> iterator = adtItems.iterator();
		if (iterator.hasNext()) {
			IAtcCheckableItem adtItem = iterator.next();
			stringBuilder.append(adtItem.getName().toUpperCase(Locale.ENGLISH));
		}
		if (iterator.hasNext()) {
			stringBuilder.append("...");
		}
		if (setting.getVariantMode() == AtcCheckVariantMode.USER_SPECIFIC) {
			stringBuilder.append(" (");
			stringBuilder.append(setting.getVariantName());
			stringBuilder.append(")(");
			stringBuilder.append(projectName);
			stringBuilder.append(')');
			return stringBuilder.toString();
		}
		stringBuilder.append(" (");
		stringBuilder.append(projectName);
		stringBuilder.append(")");
		return stringBuilder.toString();
	}

	private ILaunchConfigurationType getConfigurationType() {
		return this.launchManager.getLaunchConfigurationType("com.sap.adt.atc.launchConfigurationType");
	}
}
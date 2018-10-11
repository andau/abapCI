package abapci.feature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;

import com.sap.adt.atc.AtcCheckVariantMode;
import com.sap.adt.atc.AtcCustomizingFlavor;
import com.sap.adt.atc.IAtcSetting;

public class AtcProjectSettingStore implements IAtcProjectSettingStore {
	private static final String FALSE = "FALSE";
	private static final String TRUE = "TRUE";
	private final IProject abapProject;
	private final AtcCustomizingFlavor flavor;
	private static int[] $SWITCH_TABLE$com$sap$adt$atc$AtcCheckVariantMode;
	private static int[] $SWITCH_TABLE$com$sap$adt$atc$AtcCustomizingFlavor;

	public AtcProjectSettingStore(IProject project, AtcCustomizingFlavor flavor) {
		this.abapProject = project;
		this.flavor = flavor;
	}

	public void saveSetting(IAtcSetting model) throws CoreException {
		switch (AtcProjectSettingStore.$SWITCH_TABLE$com$sap$adt$atc$AtcCustomizingFlavor()[this.flavor.ordinal()]) {
		case 1: {
			this.store("CHECK_VARIANT_NAME", model.getVariantName());
			switch (AtcProjectSettingStore.$SWITCH_TABLE$com$sap$adt$atc$AtcCheckVariantMode()[model.getVariantMode()
					.ordinal()]) {
			case 2: {
				this.store("CHECK_VARIANT_MODE", "USER_SPECIFIC");
				break;
			}
			default: {
				this.store("CHECK_VARIANT_MODE", "SYSTEM_DEFAULT");
			}
			}
			if (model.isReportFindingsWithRepositoryExemptions()) {
				this.store("REPORT_FINDINGS_EXEMPTED_IN_REPOSITORY", "TRUE");
				break;
			}
			this.store("REPORT_FINDINGS_EXEMPTED_IN_REPOSITORY", "FALSE");
			break;
		}
		case 2: {
			if (model.isReportFindingsWithRepositoryExemptions()) {
				this.store("REPORT_FINDINGS_EXEMPTED_IN_REPOSITORY", "TRUE");
			} else {
				this.store("REPORT_FINDINGS_EXEMPTED_IN_REPOSITORY", "FALSE");
			}
			if (model.isReportFindingsWithCodeExemptions()) {
				this.store("REPORT_FINDINGS_EXEMPTED_IN_CODE", "TRUE");
				break;
			}
			this.store("REPORT_FINDINGS_EXEMPTED_IN_CODE", "FALSE");
		}
		}
	}

	public AtcProjectSetting readSetting() {
		AtcProjectSettingCodeInspector model;
		switch (AtcProjectSettingStore.$SWITCH_TABLE$com$sap$adt$atc$AtcCustomizingFlavor()[this.flavor.ordinal()]) {
		case 1: {
			model = new AtcProjectSettingCodeInspector();
			break;
		}
		default: {
			return null;
		}
		}
		String propertyValue = this.read("CHECK_VARIANT_MODE");
		if ("USER_SPECIFIC".equals(propertyValue)) {
			model.setVariantMode(AtcCheckVariantMode.USER_SPECIFIC);
		} else if ("FALSE".equals(propertyValue) || "SYSTEM_DEFAULT".equals(propertyValue)) {
			model.setVariantMode(AtcCheckVariantMode.SYSTEM_DEFAULT);
		}
		propertyValue = this.read("CHECK_VARIANT_NAME");
		if (propertyValue != null) {
			model.setVariantName(propertyValue);
		}
		if ("TRUE".equals(propertyValue = this.read("REPORT_FINDINGS_EXEMPTED_IN_CODE"))) {
			model.doReportFindingsWithCodeExemptions(true);
		} else if ("FALSE".equals(propertyValue)) {
			model.doReportFindingsWithCodeExemptions(false);
		}
		propertyValue = this.read("REPORT_FINDINGS_EXEMPTED_IN_REPOSITORY");
		if ("TRUE".equals(propertyValue)) {
			model.doReportFindingsWithRepositoryExemptions(true);
		} else if ("FALSE".equals(propertyValue)) {
			model.doReportFindingsWithRepositoryExemptions(false);
		}
		return model;
	}

	private void store(String propertyName, String propertyValue) throws CoreException {
		QualifiedName qualifiedName = new QualifiedName("com.sap.adt.atc.ui", propertyName);
		this.abapProject.setPersistentProperty(qualifiedName, propertyValue);
	}

	private String read(String propertyName) {
		String value;
		QualifiedName qualifiedName = new QualifiedName("com.sap.adt.atc.ui", propertyName);
		try {
			value = this.abapProject.getPersistentProperty(qualifiedName);
		} catch (CoreException coreException) {
			value = "";
		}
		return value;
	}

	static int[] $SWITCH_TABLE$com$sap$adt$atc$AtcCheckVariantMode() {
		int[] arrn;
		int[] arrn2 = $SWITCH_TABLE$com$sap$adt$atc$AtcCheckVariantMode;
		if (arrn2 != null) {
			return arrn2;
		}
		arrn = new int[AtcCheckVariantMode.values().length];
		try {
			arrn[AtcCheckVariantMode.SYSTEM_DEFAULT.ordinal()] = 1;
		} catch (NoSuchFieldError noSuchFieldError) {
		}
		try {
			arrn[AtcCheckVariantMode.USER_SPECIFIC.ordinal()] = 2;
		} catch (NoSuchFieldError noSuchFieldError) {
		}
		$SWITCH_TABLE$com$sap$adt$atc$AtcCheckVariantMode = arrn;
		return $SWITCH_TABLE$com$sap$adt$atc$AtcCheckVariantMode;
	}

	static int[] $SWITCH_TABLE$com$sap$adt$atc$AtcCustomizingFlavor() {
		int[] arrn;
		int[] arrn2 = $SWITCH_TABLE$com$sap$adt$atc$AtcCustomizingFlavor;
		if (arrn2 != null) {
			return arrn2;
		}
		arrn = new int[AtcCustomizingFlavor.values().length];
		try {
			arrn[AtcCustomizingFlavor.CHECKMAN.ordinal()] = 2;
		} catch (NoSuchFieldError noSuchFieldError) {
		}
		try {
			arrn[AtcCustomizingFlavor.CODE_INSPECTOR.ordinal()] = 1;
		} catch (NoSuchFieldError noSuchFieldError) {
		}
		try {
			arrn[AtcCustomizingFlavor.UNKNOWN.ordinal()] = 3;
		} catch (NoSuchFieldError noSuchFieldError) {
		}
		$SWITCH_TABLE$com$sap$adt$atc$AtcCustomizingFlavor = arrn;
		return $SWITCH_TABLE$com$sap$adt$atc$AtcCustomizingFlavor;
	}
}
package abapci.feature;

import com.sap.adt.atc.AtcCheckVariantMode;

public class AtcProjectSettingCodeInspector extends AtcProjectSetting {
	private String variantName = "DEFAULT";
	private AtcCheckVariantMode variantMode = AtcCheckVariantMode.SYSTEM_DEFAULT;
	private boolean considerRepositoryBasedExemptions = false;

	public boolean isReportFindingsWithCodeExemptions() {
		return false;
	}

	public boolean isReportFindingsWithRepositoryExemptions() {
		return this.considerRepositoryBasedExemptions;
	}

	public String getVariantName() {
		return this.variantName;
	}

	public void doReportFindingsWithCodeExemptions(boolean withExemptedFindings) {
	}

	public void doReportFindingsWithRepositoryExemptions(boolean withExemptedFindings) {
		this.considerRepositoryBasedExemptions = withExemptedFindings;
	}

	public void setVariantName(String variantName) {
		this.variantName = variantName;
	}

	public AtcCheckVariantMode getVariantMode() {
		return this.variantMode;
	}

	public void setVariantMode(AtcCheckVariantMode variantMode) {
		this.variantMode = variantMode;
	}
}
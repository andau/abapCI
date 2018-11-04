package abapci.feature;
 
import com.sap.adt.atc.AtcCheckVariantMode;
import com.sap.adt.atc.IAtcSetting;

public abstract class AtcProjectSetting implements IAtcSetting {
	public abstract void doReportFindingsWithCodeExemptions(boolean var1);

	public abstract void doReportFindingsWithRepositoryExemptions(boolean var1);

	public abstract void setVariantName(String var1);

	public abstract void setVariantMode(AtcCheckVariantMode var1);
}
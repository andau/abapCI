package abapci.coloredProject.general;

import org.eclipse.swt.graphics.Color;

public interface IStatusBarWidget {

	void setText(String statusString);

	void setBackgroundColor(Color color);

	void setTextColor(Color color);

	void setToolTip(String tooltip);

}

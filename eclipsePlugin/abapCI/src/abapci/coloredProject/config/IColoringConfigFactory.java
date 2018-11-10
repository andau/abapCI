package abapci.coloredProject.config;

import abapci.coloredProject.model.ColoredProject;

public interface IColoringConfigFactory {
	IColoringConfig create(ColoredProject coloredProject);
}

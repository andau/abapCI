package abapci.activation;

import abapci.handlers.IUnitHandler;
import abapci.handlers.JavaUnitHandler;
import abapci.prettyPrinter.IPrettyPrinter;
import abapci.prettyPrinter.NoAutoformatPrettyPrinter;

public class JavaLanguageFactory implements ILanguageFactory {

	@Override
	public IPrettyPrinter createPrettyPrinter() {
		return new NoAutoformatPrettyPrinter(); 
	}

	@Override
	public IActivationExtractor createActivationExtractor() {
		return new JavaActivationExtractor(); 
	}

	@Override
	public IUnitHandler createUnitHandler() {
		return new JavaUnitHandler(); 
	}

}

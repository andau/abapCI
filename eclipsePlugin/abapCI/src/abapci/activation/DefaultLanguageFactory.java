package abapci.activation;

import abapci.handlers.DefaultUnitHandler;
import abapci.handlers.IUnitHandler;
import abapci.prettyPrinter.IPrettyPrinter;
import abapci.prettyPrinter.NoAutoformatPrettyPrinter;

public class DefaultLanguageFactory implements ILanguageFactory {

	@Override
	public IPrettyPrinter createPrettyPrinter() {
		return new NoAutoformatPrettyPrinter();
	}

	@Override
	public IActivationExtractor createActivationExtractor() {
		return new NoActivationExtractor();
	}

	@Override
	public IUnitHandler createUnitHandler() {
		return new DefaultUnitHandler();
	}

}

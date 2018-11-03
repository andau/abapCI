package abapci.activation;

import abapci.handlers.IUnitHandler;
import abapci.prettyPrinter.IPrettyPrinter;

public interface ILanguageFactory {
	public IPrettyPrinter createPrettyPrinter();
	public IActivationExtractor createActivationExtractor();
	public IUnitHandler createUnitHandler(); 
}

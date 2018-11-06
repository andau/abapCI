package abapci.activation;

import abapci.handlers.AbapUnitHandler;
import abapci.handlers.IUnitHandler;
import abapci.prettyPrinter.AbapPrettyPrinter;
import abapci.prettyPrinter.IPrettyPrinter;

public class AbapLanguageFactory implements ILanguageFactory {
	public IPrettyPrinter createPrettyPrinter() {
		return new AbapPrettyPrinter();
	}
	
	public IActivationExtractor createActivationExtractor() 
	{
		return new AbapActivationExtractor(); 
	}

	@Override
	public IUnitHandler createUnitHandler() {
		return new AbapUnitHandler(); 
	}

}

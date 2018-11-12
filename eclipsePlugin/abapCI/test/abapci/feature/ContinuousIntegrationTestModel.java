package abapci.feature;

import java.util.ArrayList;
import java.util.List;

import abapci.Exception.ContinuousIntegrationConfigFileParseException;
import abapci.ci.model.IContinuousIntegrationModel;
import abapci.domain.ContinuousIntegrationConfig;

public class ContinuousIntegrationTestModel implements IContinuousIntegrationModel {

	@Override
	public List<ContinuousIntegrationConfig> getAll() throws ContinuousIntegrationConfigFileParseException {
		// TODO Auto-generated method stub
		return new ArrayList<ContinuousIntegrationConfig>();
	}

	@Override
	public void remove(ContinuousIntegrationConfig ciConfig) throws ContinuousIntegrationConfigFileParseException {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(ContinuousIntegrationConfig ciConfig) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ContinuousIntegrationConfig> getAllForProjectAndGeneral(String project)
			throws ContinuousIntegrationConfigFileParseException {
		// TODO Auto-generated method stub
		return new ArrayList<ContinuousIntegrationConfig>();
	}

}

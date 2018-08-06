package abapci.model;

import java.util.List;
import java.util.stream.Collectors;

import abapci.Exception.ContinuousIntegrationConfigFileParseException;
import abapci.domain.ContinuousIntegrationConfig;
import abapci.xml.ContinuousIntegrationXmlModel;

public class ContinuousIntegrationModel implements IContinuousIntegrationModel {

	private ContinuousIntegrationXmlModel continuousIntegrationXmlModel;

	public ContinuousIntegrationModel() {

		continuousIntegrationXmlModel = new ContinuousIntegrationXmlModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see abapci.model.IContinuousIntegrationModel#getAll()
	 */
	@Override
	public List<ContinuousIntegrationConfig> getAll() throws ContinuousIntegrationConfigFileParseException {
		return continuousIntegrationXmlModel.getAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see abapci.model.IContinuousIntegrationModel#remove(abapci.domain.
	 * ContinuousIntegrationConfig)
	 */
	@Override
	public void remove(ContinuousIntegrationConfig ciConfig) throws ContinuousIntegrationConfigFileParseException {
		continuousIntegrationXmlModel.remove(ciConfig);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see abapci.model.IContinuousIntegrationModel#add(abapci.domain.
	 * ContinuousIntegrationConfig)
	 */
	@Override
	public void add(ContinuousIntegrationConfig ciConfig) {
		continuousIntegrationXmlModel.add(ciConfig);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * abapci.model.IContinuousIntegrationModel#getAllForProjectAndGeneral(java.lang
	 * .String)
	 */
	@Override
	public List<ContinuousIntegrationConfig> getAllForProjectAndGeneral(String project)
			throws ContinuousIntegrationConfigFileParseException {
		return getAll().stream()
				.filter(item -> item.getProjectName().equals("") || item.getProjectName().equals(project))
				.collect(Collectors.toList());
	}
}

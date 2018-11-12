package abapci.ci.model;

import java.util.List;

import abapci.Exception.ContinuousIntegrationConfigFileParseException;
import abapci.domain.ContinuousIntegrationConfig;

public interface IContinuousIntegrationModel {

	List<ContinuousIntegrationConfig> getAll() throws ContinuousIntegrationConfigFileParseException;

	void remove(ContinuousIntegrationConfig ciConfig) throws ContinuousIntegrationConfigFileParseException;

	void add(ContinuousIntegrationConfig ciConfig);

	List<ContinuousIntegrationConfig> getAllForProjectAndGeneral(String project)
			throws ContinuousIntegrationConfigFileParseException;

}
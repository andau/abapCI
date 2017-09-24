package abapci.domain;

import org.eclipse.swt.widgets.DateTime;

public class RunInfo {
    private DateTime executionDateTime; 
    private String  executionResult;
    private int NumErrors;

 	public DateTime getExecutionDateTime() {
		return executionDateTime;
	}
	public void setExecutionDateTime(DateTime executionDateTime) {
		this.executionDateTime = executionDateTime;
	}
	public String getExecutionResult() {
		return executionResult;
	}
	public void setExecutionResult(String executionResult) {
		this.executionResult = executionResult;
	} 

   public int getNumErrors() {
    	return NumErrors;
    }

    public void setNumErrors(int numErrors) {
    	NumErrors = numErrors;
    } 

}

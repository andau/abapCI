package abapci.connections;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import abapci.Domain.TestResultSummary;
import abapci.Domain.TestState;

@Deprecated 
public class SapConnection implements ISapConnection {
    private String sapUrl;
    private String username;
    private String password;
    public SapConnection(String sapUrl, String username, String password) {
        this.sapUrl = sapUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    public TestResultSummary executeTests(String packageName) {
        
        TestResultSummary testResultSummary = new TestResultSummary(packageName,  TestState.NOK);
        
        try {
            String fullSapUrl = String.format("http://%s('%s')", sapUrl, packageName);   
            URL url = new URL(fullSapUrl);
            //http://saphed.hella.int:8000/sap/opu/odata/sap/ZODATA_ABAP_CI_SRV/AbapCiSummarySet('ZHE_FINSTRAL')
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String userpass = username + ":" + password;
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
            
            connection.setRequestMethod("GET");
            connection.setRequestProperty ("Authorization", basicAuth);
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            //connection.setRequestProperty("Accept", "application/json");
            InputStream content = connection.getInputStream();
            BufferedReader in1 = new BufferedReader(new InputStreamReader(content));
            String line =  in1.readLine();
            
            if (line.contains("TESTRUN_OK"))
            {
                testResultSummary = new TestResultSummary(packageName, TestState.OK);
            }                        
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return testResultSummary;
    }
}

package abapci.connections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.xml.bind.DatatypeConverter;

public class JenkinsConnection {

	private final String baseUrl;
	private final String username;
	private final String password;
	private final String buildToken; 

	public JenkinsConnection(String baseUrl, String username, String password, String buildToken) {
		this.baseUrl = baseUrl;
		this.username = username;
		this.password = password;
		this.buildToken = buildToken; 
	}

	public void runJob(String packageName) {
		String urlstring = String.format("http://%s/job/%s/build?token=%s&cause=Build_launched_by_abapCI",
				baseUrl, packageName, buildToken);

		try {

			URL url = new URL(urlstring);
			String authStr = username + ":" + password;
			String encoding = DatatypeConverter.printBase64Binary(authStr.getBytes("utf-8"));

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setRequestProperty("Authorization", "Basic " + encoding);
			InputStream content = connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(content));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
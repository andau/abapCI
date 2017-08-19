package abapci.connections;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import abapci.Domain.TestResultSummary;


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
		try {

			URL url = new URL(sapUrl);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			connection.setRequestProperty("Accept", "application/json");
			String userpass = username + ":" + password;

			String request = "";
			OutputStream out = connection.getOutputStream();
			out.write(request.getBytes());
			out.close();

			if (connection.getResponseCode() == 200)
				;
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				String inputLine;
				while ((inputLine = in.readLine()) != null)
					System.out.println(inputLine);
				in.close();
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		return new TestResultSummary(packageName, 0);
	}

	public void call(String packageName) {
		// TODO Auto-generated method stub

	}

}
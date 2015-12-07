package sol.demo.seegeek;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import android.util.Log;

public class Utils {
	private final static String TAG = MainActivity.TAG;

	public static String getUrl(String pathName, String fileName) {
		String fileSD = pathName + "/" + fileName;
		InputStream data = readFromSD(fileSD);
		if (data == null) {
			Log.d(TAG, "No config file found.");
			return null;
		}
		String main = "";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(data);
			Element root = document.getDocumentElement();
			Log.d(TAG, "Get root = " + root.toString());
			Element e = (Element) root.getElementsByTagName("MainPage").item(0);
			if (e != null){
				main = e.getFirstChild().getNodeValue();
				Log.d(TAG, "Get main page = " + main);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} finally {
			try {
				data.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return main;
	}

	public static boolean getConfigFromServer(String remoteUrl, String pathName, String fileName) {
		Log.d(TAG, "getConfigFromServer " + remoteUrl);
		HttpGet httpRequest = new HttpGet(remoteUrl);
		String strResult = "";
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
				Log.d(TAG, "Get " + strResult);
				try {
					File dir = new File(pathName);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					File file = new File(pathName + "/" + fileName);
					if (!file.exists()) {
						file.createNewFile();
					}
					FileOutputStream fos = new FileOutputStream(file);
					Log.d(TAG, "Write " + file.getPath());
					fos.write(strResult.getBytes());
					fos.close();
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static InputStream readFromSD(String filePath) {
		Log.d(TAG, "readFrom " + filePath);
		InputStream in = null;
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return in;
	}

	public class SGConfig {
		private String mainPage;

		public SGConfig() {
			super();
		}

		public SGConfig(String name) {
			super();
			this.mainPage = name;
		}

		public String getName() {
			return mainPage;
		}

		public void setName(String name) {
			this.mainPage = name;
		}

		public String toString() {
			return "SGConfig: [ MainPage = " + mainPage + " ]";
		}
	}
}
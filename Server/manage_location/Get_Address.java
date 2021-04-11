import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class Get_Address {
	double latitude;
	double longitude;
	String regionAddress;

	Get_Address(double latitude, double longitude) throws Exception {
		this.latitude = latitude;
		this.longitude = longitude;
		this.regionAddress = getJSONData(getApiAddress());
	}

	String jsonPassing() {// 파싱
		String f_address = "";

		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(regionAddress);
		JsonArray array = (JsonArray) obj.get("results");

		JsonObject addr = (JsonObject) array.get(0);// region
		JsonObject roadaddr = (JsonObject) array.get(1);//

		JsonObject region = (JsonObject) addr.get("region");
		JsonObject land2 = (JsonObject) addr.get("land");
		JsonObject land = (JsonObject) roadaddr.get("land");

		JsonObject area[] = new JsonObject[3];
		JsonObject addition0 = (JsonObject) land.get("addition0");

		area[0] = (JsonObject) region.get("area1");
		area[1] = (JsonObject) region.get("area2");
		area[2] = (JsonObject) region.get("area3");

		JsonElement element;

		String space = "";
		for (int i = 0; i < area.length; i++) {
			if (i != 0)
				space = " ";
			element = area[i].get("name");
			f_address += space + element.getAsString();
		}

		f_address += " " + (element = land2.get("number1")).getAsString();
		f_address += "-" + (element = land2.get("number2")).getAsString();
		f_address += " " + (element = addition0.get("value")).getAsString();

		return f_address;

	}

	String getApiAddress() {
		String apiURL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?request=coordsToaddr&coords="
				+ longitude + "," + latitude + "&orders=addr,roadaddr&output=json";
		return apiURL;
	}

	String getJSONData(String apiURL) throws Exception {
		String jsonString = new String();
		String buf;
		URL url = new URL(apiURL);
		URLConnection conn = url.openConnection();
		// conn.setRequestProperty("Content-Type","application/json");
		conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "xgundembra");
		conn.setRequestProperty("X-NCP-APIGW-API-KEY", "GBV0ga1iRMmXljxEAjpwMq1hefwYZRorXoRPpMkW");
		conn.connect();
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		while ((buf = br.readLine()) != null) {
			jsonString += buf;
		}
		return jsonString;
	}

	public String getAddress() {
		return jsonPassing();
	}

}

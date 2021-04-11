import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.gson.JsonObject;

public class location_main {

	public static JsonObject main(JsonObject args) throws Exception {

		String result = "", result2 = "";
		String db_connect = "";
		String mysql_url = "jdbc:mysql://101.101.217.142:3306/main?useUnicode=true&characterEncoding=utf8";
		String db_id = "jin1004boy";
		String db_pw = "ckdwls31412";
		int time = 10, important_time = 20;
		

		Connection con;

		String id = "";
		double address_lat = 0.0, address_lng = 0.0;

		if (args.has("id"))
			id = args.getAsJsonPrimitive("id").getAsString();
		if (args.has("address_lat"))
			address_lat = args.getAsJsonPrimitive("address_lat").getAsDouble();
		if (args.has("address_lng"))
			address_lng = args.getAsJsonPrimitive("address_lng").getAsDouble();

		// db 연결
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(mysql_url, db_id, db_pw);
			save_location save_loc = new save_location(con, id, address_lat, address_lng,0);
			result = save_loc.result();
			if (save_loc.getCount() % time == 0) {// 10분마다 한번씩
				// 위치 머무름 지속별 우선순위 저장하는 class
				SelectLocation select_loc = new SelectLocation(con, id, save_loc.return_kAddress(), save_loc.getCount());
				result2 = select_loc.result();
			}
			if(save_loc.getCount() % important_time  == 0) {
				save_location save_loc_ = new save_location(con, id, address_lat, address_lng,1);
				result = save_loc_.result();
			}

		} catch (ClassNotFoundException e) {
			db_connect = "driver connect faild:" + e;
		} catch (SQLException e) {
			db_connect = "sql error:" + e;
		}

		JsonObject response = new JsonObject();
		response.addProperty("result:", result);
		response.addProperty("result2:", result2);
		response.addProperty("db_connect", db_connect);
		return response;
	}
}




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.gson.JsonObject;

public class send_main {
	public static JsonObject main(JsonObject args) throws Exception {

		String result = "";
		String db_connect = "";
		String mysql_url = "jdbc:mysql://101.101.217.142:3306/main?useUnicode=true&characterEncoding=utf8";
		String db_id = "jin1004boy";
		String db_pw = "ckdwls31412";

		Connection con;

		String id = "";
		int put_count = 4;
		JsonObject response = new JsonObject();

		if (args.has("id"))
			id = args.getAsJsonPrimitive("id").getAsString();
		// db 연결
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(mysql_url, db_id, db_pw);

			ChoseImportantloc loc = new ChoseImportantloc(con, id, put_count);
			result = loc.result();

			for (int i = 0; i < put_count; i++)
				response.addProperty("addr" +(i+1), loc.return_kAddress(i));

		} catch (ClassNotFoundException e) {
			db_connect = "driver connect faild:" + e;
		} catch (SQLException e) {
			db_connect = "sql error:" + e;
		}

		response.addProperty("result:", result);
		response.addProperty("db_connect", db_connect);
		return response;
	}
}

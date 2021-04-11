
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.JsonObject;

public class overview {
	Connection con;
	ResultSet rs;
	PreparedStatement pstmt = null;
	
	String content = ""; 
	String weather ="";
	String subject = "";
	String db_connect = "";

	overview(String url, String db_id, String db_pw, String id, String date,int num) {
		try {

			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, db_id, db_pw);
			db_connect = "connection success";

			String sql = "select * from diary where date = ? and id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, date);
			pstmt.setString(2, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {// 데이터가 있을때
				subject = rs.getString("subject");
				if(num == 1) {// 다이어리 내용 읽어
					content = rs.getString("content");
					weather = rs.getString("weather");
				}	
			} else
				subject = "No data";
		} catch (ClassNotFoundException e) {
			db_connect = "dirver faild :" + e;
		} catch (SQLException e) {
			db_connect = "sql faild :" + e;
		}
	}

	String get_subject() {
		return subject;
	}

	String get_db_conn() {
		return db_connect;
	}
	
	void read_diary(JsonObject response){
		response.addProperty("content", content);
		response.addProperty("weather", weather);
	}
}



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class fix_diary {

	Connection con;
	ResultSet rs;
	PreparedStatement pstmt = null;

	String db_connect = "";
	String result = "";

	fix_diary(String url, String db_id, String db_pw, String id, String subject, String content, String date) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, db_id, db_pw);
			db_connect = "connection success";

			String sql = "update diary set subject = ?, content = ? where id = ? and date =?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, subject);
			pstmt.setString(2, content);
			pstmt.setString(3, id);
			pstmt.setString(4, date);
			if (pstmt.executeUpdate() == 0)
				result = "faild fix data";
			else
				result = "success fix data";

		} catch (ClassNotFoundException e) {
			db_connect = "dirver faild :" + e;
		} catch (SQLException e) {
			db_connect = "sql faild :" + e;
		}
	}

	String get_result() {
		return result;
	}

	String get_db_conn() {
		return db_connect;
	}

}


import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class save_diary {

	Connection con;
	ResultSet rs;
	PreparedStatement pstmt = null;

	String db_connect = "";
	String result = "";

	save_diary (String url, String db_id, String db_pw, String id, String subject, String content, String weather,
			String date) throws UnsupportedEncodingException{

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, db_id, db_pw);
			db_connect = "connection success";

			String sql = "select content from diary where date = ? and id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, date);
			pstmt.setString(2, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {//이미 데이터가 있을때 
				result = "already have contnent";
			} 
			else {//업을때 신규생성 

				sql = "insert into diary(id,subject,content,weather,date) values(?,?,?,?,?)";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, id);
				pstmt.setString(2, new String(subject.getBytes("UTF-8"),"UTF-8"));
				pstmt.setString(3, new String(content.getBytes("UTF-8"),"UTF-8"));
				pstmt.setString(4, new String(weather.getBytes("UTF-8"),"UTF-8"));
				pstmt.setString(5, date);
				if(pstmt.executeUpdate() ==0) 
					result = "faild insert data";
				else 
					result = "success insert data";
			}
		} catch (ClassNotFoundException e) {
			db_connect = "dirver faild :" + e;
		} catch (SQLException e) {
			db_connect = "sql faild :" + e;
		} catch(UnsupportedEncodingException e){
			db_connect = "incoding faild :" + e;
		}		
	}
	
	String get_result() {
		return result;
	}

	String get_db_conn() {
		return db_connect;
	}
}


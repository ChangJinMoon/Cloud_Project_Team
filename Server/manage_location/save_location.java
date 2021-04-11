import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Date;

public class save_location {

	/*
	 * 좌표를 받아온 다음 db-> id , 좌표 , 좌표_한국 주소 변환 값, 시간,저장횟
	 * 좌표 -> 한국 지번으로 변환 저장 횟수가 10의 배수이면 주소 즐겨찾기 실행 저장 횟수 -> 리턴 좌표 ->decimal(),
	 * x(8,6) y(9,6) 시간 date_time
	 */
	ResultSet rs;
	PreparedStatement pstmt = null;
	String sql, err;
	String k_address;
	int count =0,next_count = 0;

	save_location(Connection con, String id, double lat, double lng,int case_)throws Exception {

		// 한국 표준시간 저장
		TimeZone time;
		Date realTime = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM--dd HH:mm:ss");
		time = TimeZone.getTimeZone("Asia/Seoul");
		df.setTimeZone(time);

		// 시간 저장 순서 찾기
		try {
			sql = "select count from location where id = ? order by count desc";// 제일 최근 데이터
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1,id);
			rs = pstmt.executeQuery();
			if (rs.next()) {// 데이터있을때카운 하나받고 종료
				if(next_count == 0) {
					count = rs.getInt("count");
					next_count++;
				}
			} 
			count++;
			// 좌표로 지번 찾기 
			Get_Address get_in_kAddress = new Get_Address(lat,lng);
			k_address = get_in_kAddress.getAddress();
			
			// 데이터 저장
			sql = "insert into location values(?,?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setDouble(2, lat);
			pstmt.setDouble(3, lng);
			pstmt.setString(4, k_address);
			pstmt.setString(5,df.format(realTime));
			pstmt.setInt(6, count);
			
			pstmt.executeUpdate();
			err = "update_save_loc";
			
			if(case_ == 1) {
				sql ="delete from location where id =?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, id);
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			err = "save_location_sql error" + e;
		}

	}
	
	String result() {
		return err;
	}
	
	int getCount() {
		return count;
	}
	String return_kAddress() {
		return k_address;
	}

}

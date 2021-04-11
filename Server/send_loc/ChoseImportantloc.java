import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChoseImportantloc {
	ResultSet rs;
	PreparedStatement pstmt = null;
	String sql, err;
	String k_address[];

	ChoseImportantloc(Connection con, String id, int count) {

		int get_count = count;
		// 우선 순위 장소 조희후전
		try {
			k_address = new String[get_count];
			sql = "select kAddress from importantloc where id = ? order by important desc";// 제일 최근 데이터
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			for(int i = 0 ; i<get_count ; i++) {
				if (rs.next()) {// 데이터있을때카운 하나받고 종료
						k_address[i] = rs.getString("kAddress");
					}
				else 
					k_address[i] ="faild";
			}
			// 좌표로 지번 찾기

		} catch (SQLException e) {
			err = "save_location_sql error" + e;
		}

	}

	String result() {
		return err;
	}

	String return_kAddress(int i) {
		return k_address[i];
	}
}

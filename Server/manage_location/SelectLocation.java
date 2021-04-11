import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectLocation {

	ResultSet rs;
	PreparedStatement pstmt = null;
	String sql, err;
	String before_add;
	int count_important = 0;
	boolean conn = false;

	/*
	 * 5분마다 호출 count - 이전(5분전 ) 장소를 지금 장소와 비교해서 같으면 중요도 +1
	 */
	SelectLocation() {
	}

	SelectLocation(Connection con, String id, String now_address, int now_count) {

		try {
			sql = "select kAddress from location where id = ? and count = ? ";// 제일 최근 데이터
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setInt(2, now_count - 4);
			rs = pstmt.executeQuery();

			if (rs.next()) {// 이전 장소 받아와 저장
				before_add = rs.getString("kAddress");
			}

			// 전 주소와 현주소 비교하기
			if (now_address.equals(before_add)) {// 같을 떄 해당 장소 저장
				// 해당 장소 찾기
				sql = "select KAddress from importantloc where id = ?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, id);
				rs = pstmt.executeQuery();
				// 데이터 수만큼 반
				for (int i = 0; i < rs.getRow(); i++) {
					if (rs.next()) { // 해당 장소가 있는지 확인
						if (now_address.equals(rs.getString("kAddress"))) {
							conn = true;
							count_important = rs.getInt("important");
						}
					}

					if (conn == true) {

						sql = "update importantloc set important = ? where kAddress = ?";// id, 지번 , 중요
						pstmt = con.prepareStatement(sql);
						count_important += 1;
						pstmt.setInt(1, count_important);
						pstmt.setString(2, now_address);

					} else {
						sql = "insert into importantloc values(?,?,?)";// id, kAddress , important
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, id);
						pstmt.setString(2, now_address);
						pstmt.setInt(3, 1);
					}
					pstmt.executeUpdate();
					err = "select_location_sql sucess";
				}
			}
		} catch (SQLException e) {
			err = "select_location_sql error" + e;
		}
	}

	String result() {
		return err;
	}

}

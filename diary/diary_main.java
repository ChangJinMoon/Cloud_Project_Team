import java.io.UnsupportedEncodingException;
import com.google.gson.JsonObject;

public class diary_main {

	public static JsonObject main(JsonObject args)throws UnsupportedEncodingException {

		JsonObject response = new JsonObject();

		String id = " ";
		String subject = " ";
		String content = " ";
		String db_connect = "";
		String date = "";// 년 월 일 형식으로 받음
		String weather = " ";// 날씨 -> 1.맑음 2.비 3.흐림 4.눈
		String category = "";//diary 필요기능 설정

		String mysql_url = "jdbc:mysql://101.101.217.142:3306/main?useUnicode=true&characterEncoding=utf8";
		String db_id = "jin1004boy";
		String db_pw = "ckdwls31412";
		String result = new String("");

		if (args.has("category"))
			category = args.getAsJsonPrimitive("category").getAsString();
		if (args.has("id"))
			id = args.getAsJsonPrimitive("id").getAsString();
		if (args.has("subject"))
			subject = args.getAsJsonPrimitive("subject").getAsString();
		if (args.has("content"))
			content = args.getAsJsonPrimitive("content").getAsString();
		if (args.has("weather"))
			weather = args.getAsJsonPrimitive("weather").getAsString();
		if (args.has("date"))
			date = args.getAsJsonPrimitive("date").getAsString();

		// 카테고리 별 기능
		switch (Integer.valueOf(category)) {
		case 1:
			// 미리보기,보기
			overview over_s = new overview(mysql_url, db_id, db_pw, id, date, 0);
			db_connect = over_s.get_db_conn();// 연결,작동 확인
			result = over_s.get_subject();//제목을 반환
			break;
		case 2:
			// 새롭게 저장
			save_diary save_d = new save_diary(mysql_url, db_id, db_pw, id, subject, content, weather, date);
			db_connect = save_d.get_db_conn();// 연결,작동 확인
			result = save_d.get_result();// 저장 유무 반환
			break;
		case 3:
			// 수정
			fix_diary fix_d = new fix_diary(mysql_url, db_id, db_pw, id, subject, content, date);
			db_connect = fix_d.get_db_conn();// 연결,작동 확인
			result = fix_d.get_result();// 수정 결과 반환 
			break;
		case 4:
			// 내용읽기
			overview over_c = new overview(mysql_url, db_id, db_pw, id, date, 1);
			db_connect = over_c.get_db_conn();// 연결,작동 확인
			result = over_c.get_subject();// 제목 반환
			over_c.read_diary(response);// response 에 content,weather 삽입 
			break;
		default:
			result = "incorrect category value";
			break;
		}

		response.addProperty("result:", result);
		response.addProperty("db_connect", db_connect);
		return response;
	}

}



	import java.net.InetAddress;
	import java.net.UnknownHostException;
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;

	import com.google.gson.JsonObject;

	/*trigger -> 유저가 회원가입 창으로 이동 후 정보 입력 후 회원가입 버튼을 누르면 발생
	action -> 이름과 비밀번호를 받아고 id는 100000~200000 범위로 랜덤수로 정한다  
	-> 이후 데이터 베이스에 저장 
	*/

	public class add_member {
	      public static JsonObject main(JsonObject args) {

	            String name = "name";
	            String password = "Password";
	            String sex = "sex";
	            String id = "id";
	            String db_connect ="";
	            String result = "";
	             
	             //이름과 장소를 트리거로 부터 전달 받음
	             if (args.has("name"))
	                 name = args.getAsJsonPrimitive("name").getAsString();
	            if (args.has("sex"))
	                 sex = args.getAsJsonPrimitive("sex").getAsString();
	           if (args.has("password"))
	                 password = args.getAsJsonPrimitive("password").getAsString();
	           if (args.has("id"))
	                 id = args.getAsJsonPrimitive("id").getAsString();
	           
	         // 회원 정보 저장을 위한 db연결
	        
	           Connection conn = null;
	           ResultSet rs = null;
	           PreparedStatement pstmt = null;
	            try {
	               //db연결 -> dns를 어떻게 할지 고민 -> dns - ip를 받아 connection에 전달 아니면 앱서버 생성후 host로 설정
	               Class.forName("com.mysql.jdbc.Driver");
	               conn = DriverManager.getConnection("jdbc:mysql://101.101.217.142:3306/main?useUnicode=true&characterEncoding=utf8","jin1004boy","ckdwls31412");
	               db_connect = "success";
	               /* db 연결후 아이디가 이미 있는지 확인 */
	               String sql = "select id from member where id = ?";
	               pstmt = conn.prepareStatement(sql);
	               pstmt.setString(1,id);
	               rs = pstmt.executeQuery();
	               
	               if(rs.next()) {
	            	  if(rs.getString("id") == id) 
	            		  result="this id is already exist";
	               }
	               else {//아이디가 중복이 되지 않으면 
	            		  /*회원 정보 db에 업데이*/
	            		 sql = "insert into member values(?,?,?,?)";
	            		 pstmt = conn.prepareStatement(sql);
	            		 pstmt.setString(1,name);
	            		 pstmt.setString(2,id);
	            		 pstmt.setString(3,password);
	            		 pstmt.setString(4,sex);
	            		 pstmt.executeUpdate();
	            		 result = "succees making account";
	            	  }
	               
	            }catch(ClassNotFoundException e) {
	    			db_connect = "connection faild"+e;
	    		}catch(SQLException e) {
	    			db_connect = "error" + e;
	    		}
	            
	             JsonObject response = new JsonObject();
	             //response.addProperty("payload", "Hello, " + name + " your gender is" + sex + ".");
	             response.addProperty("payload","Your id is"+id+". password is "+password);
	             response.addProperty("db",db_connect);
	             response.addProperty("result",result);//respone.put("result",result); -> 결과값 키값으로 전달
	           
	             return response;
	      }
	  }
	


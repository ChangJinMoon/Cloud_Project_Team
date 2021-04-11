

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.JsonObject;

public class login {

         public static JsonObject main(JsonObject args) {

                 Connection con;
                 ResultSet rs;
                 PreparedStatement pstmt = null;

         String password = "Password";
         String id = "id";
         String db_connect ="";

                 String url = "jdbc:mysql://101.101.217.142:3306/main?useUnicode=true&characterEncoding=utf8";
                 String dbid = "jin1004boy";
                 String dbpw = "ckdwls31412";
                 String sql ="select password from member where id = ?";
                 String result = new String("");


                 if (args.has("id"))
             id = args.getAsJsonPrimitive("id").getAsString();
         if (args.has("password"))
             password = args.getAsJsonPrimitive("password").getAsString();



         try {
                 Class.forName("com.mysql.jdbc.Driver");
                 con = DriverManager.getConnection(url, dbid, dbpw);
                 db_connect ="connection success";


                 pstmt = con.prepareStatement(sql);
                 pstmt.setString(1, id);
                 rs = pstmt.executeQuery();

                 if(rs.next()) {
                         if(rs.getString(1).equals(password)) {//비밀번호가 맞을
                        System.out.println("login good");
                        result = "login good";//1
                 } else {//비밀번호가 틀릴
                        System.out.println("wrong password");
                        result = "wrong password";//2
                 }
                 }

                 else {
                System.out.println("make account");
                result = "make account";//3
             }


         }catch(ClassNotFoundException e) {
             db_connect = "connection faild";
     }catch(SQLException e) {
             db_connect = "error" + e;
     }

      JsonObject response = new JsonObject();
      response.addProperty("login_process:",result);
      response.addProperty("db_connect",db_connect);
      response.addProperty("id:",id);
      response.addProperty("password:",password);
      return response;

}//가설 1.json main에서 받은 return을 자바 함수에서 이용가능
}





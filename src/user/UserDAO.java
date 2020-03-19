//jsp에서 데이터베이스의 회원정보에 접근하기 위해 사용하는 클래스
package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

	private Connection conn;
	private PreparedStatement pstmt;//select 기법의 해킹을 방어하기 위한 방식
	private ResultSet rs;
	
	public UserDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
			String dbID = "root";
			String dbPassword = "111111";
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}//실제로 mysql에 접속하게 하는 함수
	
	public int login(String userId, String userPassword) {
		String SQL = "SELECT userPassword FROM USER WHERE userId = ?";
		
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userId);//입력받은 값을 ?뒤에 넣어놨다가 그 ?에 해당하는 곳에 유저 아이디를 넣어주는것
			rs=pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getString(1).equals(userPassword)) {
					return 1;//로그인 성공
				}
				else
					return 0; //비밀번호 불일치
			}
			return -1;//아이디가 없음
		}catch(Exception e) {
			e.printStackTrace();
		}
		return-2; //데이터베이스 오류
	}
	
	public int join(User user) {
		String SQL="INSERT INTO USER VALUES(?,?,?,?,?)";
		try {
			pstmt=conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserId());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			return pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}return -1;
	}
}

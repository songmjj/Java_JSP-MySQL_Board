package auth.service;

import java.sql.Connection;
import java.sql.SQLException;

import jdbc.connection.ConnectionProvider;
import member.dao.MemberDao;
import member.model.Member;

public class LoginService {

	private MemberDao memberDao = new MemberDao();

	public User login(String id, String password) {
		//ConnectionProvider는  DriverManager.getConnection("jdbc:apache:commons:dbcp:~~"); 라는 뜻 
		try (Connection conn = ConnectionProvider.getConnection()) {//위 주소로 연결해라
			Member member = memberDao.selectById(conn, id);//서버에 연결됐으니 아이디를 검색하고 
			if (member == null || !(member.matchPassword(password))) {//아이디가 없거나 비밀번호가 없다면
				throw new RuntimeException();
			}
			return new User(member.getId(), member.getName());//User 클래스에 확인된 아이디와 비밀번호 값을 보냄
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}

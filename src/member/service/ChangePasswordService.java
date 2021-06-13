package member.service;

import java.sql.Connection;
import java.sql.SQLException;

import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;
import member.dao.MemberDao;
import member.model.Member;

public class ChangePasswordService {

	private MemberDao memberDao = new MemberDao();
	
	// 암호변경을 할 때 호출될 changePassword 메서드
	public void changePassword(String userId, String curPwd, String newPwd) {
		Connection conn = null;
		try {
			
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			// 데이터베이스와 연결 후 memberDao.selectById메서드를 이용해 
			// 매개변수로 받은 userId의 데이터를 구해 member객체에 저장한다.
			Member member = memberDao.selectById(conn, userId);
		
			// member 객체에 null값이 저장되어있으면 MemberNotFoundException 예외를 발생시킨다.
			if(member == null) { 
				throw new MemberNotFoundException();
			}
			// 현재암호와 회원의 실제 암호가 다르다면 InvalidPasswordException 예외를 발생시킨다.
			if(!member.matchPassword(curPwd)) {
				throw new InvalidPasswordException();
			}
			// 위 예외에 해당하지 않으면 객체의 암호 데이터를 변경한다.
			member.changePassword(newPwd);
			// 변경된 사항을 memberDao.update메서드로 데이터베이스에 반영한다.
			memberDao.update(conn, member);
			conn.commit();
			
		} catch (SQLException e) {
			JdbcUtil.close(conn);
			throw new RuntimeException(e);
		}finally {
			JdbcUtil.close(conn);
		}
	
	}
	
}

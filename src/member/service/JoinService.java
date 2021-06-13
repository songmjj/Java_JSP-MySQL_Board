package member.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;
import member.dao.MemberDao;
import member.model.Member;
/* JoinService클래스(모델)
 * : 회원가입 기능 구현 클래스 */
public class JoinService {

	private MemberDao memberDao = new MemberDao();
	
	/* join(JoinRequest joinReq) 메서드
	 * : JoinRequest객체에 저장한 정보를 이용해서 insert()메서드 실행 
	 * 	 즉, 입력받은 정보를 통해 DB에 저장하여 회원가입 기능 실행하는 메서드 */
	public void join(JoinRequest joinReq) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection(); //커넥션 연결
			conn.setAutoCommit(false); // 트랜잭션 시작(자동커밋 기능을 끔)
			
			// MemberDao의 selectById()메서드를 통해서 아이디 중복 여부 확인 및 중복시 롤백처리&DuplicateIdException처리함
			Member member = memberDao.selectById(conn, joinReq.getId());
			if (member != null) {
				JdbcUtil.rollback(conn);
				throw new DuplicateIdException();
			}
			
			// MemberDao의 insert()메서드를 통해서 회원정보를 board.member테이블에 삽입(저장)함
			memberDao.insert(conn, 
					new Member(
							joinReq.getId(), 
							joinReq.getName(), 
							joinReq.getPassword(), 
							new Date())
					);
			// 트랜잭션을 커밋(저장)함
			conn.commit();
		} catch (SQLException e) { // 예외발생시 롤백처리&RuntimeException처리함
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally { // try,catch문 끝나면 커넥션 닫음
			JdbcUtil.close(conn);
		}
	}
}

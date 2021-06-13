package member.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import jdbc.JdbcUtil;
import member.model.Member;
/* MemberDao클래스(모델 클래스)
 * : 퀴리문을 이용해서 DB의 member테이블에 CRUD기능을 실행하는 클래스 */
public class MemberDao {
	
	/* selectById()
	 * : DB의 member테이블 내에서 memberid를 통해 해당 멤버의 정보를 반환하는 메서드 */
	public Member selectById(Connection conn, String id) throws SQLException {
		PreparedStatement pstmt = null;// 쿼리를 실행시키는 메서드
		ResultSet rs = null; //쿼리문의 결과값을 저장할 수 있는 메서드
		try {
			pstmt = conn.prepareStatement(
					"select * from member where memberid = ?");// 멤버 아이디에 대응하는 값을 찾자
			pstmt.setString(1, id);//? 자리에 들어온 id 값이 들어간다.
			rs = pstmt.executeQuery();//이 메서드가 실행되면 위의 쿼리문이 실행되어 들어간거고 그 쿼리문의 결과가 rs에 들어감
			Member member = null;//멤버는 아이디 비밀번호 등의 변수를 가지고 있고 그것들이 null인 상태인것
			if (rs.next()) {//순차적으로
				member = new Member(//멤버의 변수에 각각의 값을 받아와서 넣자 멤버의 생성자 순서대로 값을 맞게 넣는거고 그 대응대는 받은 값은 ""안에 해당하는 쿼리문에서 얻어온 값이다.
					rs.getString("memberid"), 
					rs.getString("name"), 
					rs.getString("password"),
					toDate(rs.getTimestamp("regdate")));
			}
			return member;//멤버를 채워 넣었으면 반환
		} finally {
			JdbcUtil.close(rs);//sql을 끈다?
			JdbcUtil.close(pstmt);
		}
	}

	/* toDate()메서드
	 * : 날짜를 생성하는 함수  타임스탬프가 비어있으면 데이트를 생성
	 * 즉, DB의 member테이블 내에서 regdate변수에 현재 날짜 및 시간 생성 */
	private Date toDate(Timestamp date) {
		return date == null ? null : new Date(date.getTime());
	}
	
	/* insert()메서드
	 * : 퀴리문을 이용해서 DB의 member테이블에 C(Create)기능을 실행하는 메서드
	 * 즉, DB의 member테이블 내에서 쿼리문을 통해서 멤버의 정보(memberid,name,password,regdate) 생성 */
	public void insert(Connection conn, Member mem) throws SQLException {
		try (PreparedStatement pstmt = 
				conn.prepareStatement("insert into member values(?,?,?,?)")) {
			pstmt.setString(1, mem.getId());
			pstmt.setString(2, mem.getName());
			pstmt.setString(3, mem.getPassword());
			pstmt.setTimestamp(4, new Timestamp(mem.getRegDate().getTime()));
			pstmt.executeUpdate();
		}
	}
	
	public void update(Connection conn, Member member) throws SQLException {
		// SQL구문을 연결된 데이터베이스에 전달시키는 기능을 갖는 PreparedStatement의 객체 pstmt에
		// 암호를 변경하게 하는 쿼리문을  담는다.
		try (PreparedStatement pstmt = conn.prepareStatement(
				"update member set name = ?, password = ? where memberid = ?")) {
			// 순서대로 변수에 ?대입을 하여 Update를 시킨다.
			pstmt.setString(1, member.getName());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getId());
			pstmt.executeUpdate(); //SQL 문을 실행할때 쓰이는 executeUpdate메서드
		}
	}

}

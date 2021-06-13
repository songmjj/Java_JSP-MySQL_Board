package mvc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

		  /* NullHandler 클래스
		   * : 404 에러를 응답으로 전송하는 핸들러 클래스 */
public class NullHandler implements CommandHandler {
	/* NullHandler클래스는 process()메서드를 이용해서 404에러를 응답으로 전송함 */
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		res.sendError(HttpServletResponse.SC_NOT_FOUND);		
		return null;
	}
	
	

}

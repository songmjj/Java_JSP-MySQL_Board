package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginCheckFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;//요청 받은 정보에 HttpServletRequest로 자료형을 바꿔서 넣고
		HttpSession session = request.getSession(false);//세션은 더 만들지 말고
		if (session == null || session.getAttribute("authUser") == null) {//로그인인 안되어있다면 이라는 뜻
			HttpServletResponse response = (HttpServletResponse)res;//응답서블렛에 형변환해서 응답을 넣고
			response.sendRedirect(request.getContextPath() + "/login.do");//응답을 보낸다. 프로젝트 path를 따고 그 뒤에 "/login.do" 붙임
		} else {
			chain.doFilter(req, res);//세션에 auth속성이 있으면 로그인한 것으로 판단하고 기능 실행
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}

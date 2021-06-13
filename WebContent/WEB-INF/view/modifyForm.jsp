<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>게시글 수정</title>
</head>
<!-- 게시글 수정 폼을 보여주는 modifyForm.jsp 파일이며
     ModifyArticlehandler에서 전달 받은 ModifyRequest 객체를 이용해서
           폼에 데이터를 채운다. 실제 로그인을 하고 본인이 작성한 게시글 읽기 화면에서 [게시글수정] 링크를 클릭하면
           수정폼이 출력되도록 해줌 -->

<body>
<form action="modify.do" method="post">
<input type="hidden" name="no" value="${modReq.articleNumber}">
<p>
	번호:<br/>${modReq.articleNumber}
</p>
<p>
	제목:<br/><input type="text" name="title" value="${modReq.title}">
	<c:if test="${errors.title}">제목을 입력하세요.</c:if>
</p>
<p>
	내용:<br/>
	<textarea name="content" rows="5" cols="30">${modReq.content}</textarea>
</p>
<input type="submit" value="글 수정">
</form>
</body>
</html>
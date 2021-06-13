<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="refresh" content="10;url=list.do">
<meta charset="UTF-8">
<title>삭제 완료</title>
</head>
<body>

게시글을 삭제했습니다.
<br>
${ctxPath = pageContext.request.contextPath ; ''}
<a href="${ctxPath}/article/list.do">[게시글목록보기]</a>
</body>
</html>
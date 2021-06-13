<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="u" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 읽기</title>
</head>
<body>
<table border="1" width="100%">
<tr>
	<td>번호</td>
	<td>${articleData.article.article_no}</td>
</tr>
<tr>
	<td>작성자</td>
	<td>${articleData.article.writer.writer_name}</td>
</tr>	
<tr>
	<td>제목</td>
	<td><c:out value='${articleData.article.title}'/></td>
</tr>	
<tr>
	<td>내용</td>
	<!-- pre태그를 사용해 입력한 내용을 그대로 보여준다. -->
	<td><u:pre value='${articleData.content}'/></td>
</tr>
<tr>
	<td colspan="2">
	<!-- pageNo = JSTL이 지원하는 태그에서 사용할 수 있는 변수  -->
	<c:set var="pageNo" value="${empty param.pageNo ?'1':param.pageNo}" />
		<a href="list.do?pageNo=${pageNo}">[목록]</a>	
		<c:if test="${authUser.id == articleData.article.writer.wrtier_id}">
		<a href="modify.do?no=${articleData.article.article_no}">[게시글수정]</a>
		<a href="delete.do?no=${articleData.article.article_no}">[게시글삭제]</a>
		</c:if>
</table>
</body>
</html>
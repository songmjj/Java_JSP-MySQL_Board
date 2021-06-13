<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 목록</title>
</head>
<body>
<table border="1">
	<tr>
		<td colspan="4"><a href="write.do">[게시글 쓰기]</a>
	</tr>
	<tr>
		<td>번호</td>
		<td>제목</td>
		<td>작성자</td>
		<td>조회수</td>
	</tr>
	<c:if test="${articlePage.hasNoArticles() }"> <%-- total=0일 경우 true --%>
		<tr>
			<td colspan="4">게시글이 존재 하지 않습니다</td>
		</tr>
	</c:if>
	<c:forEach var="article" items="${articlePage.content}">
	<tr>
		<td>${article.article_no}</td>
		<td>
		<a href="read.do?no=${article.article_no }&pageNo=${articlePage.currentPage }"><c:out value="${article.title }"/></a>
		</td>
		<td>${article.writer.writer_name }</td>
		<td>${article.read_cnt }</td>
	</tr>
	</c:forEach>
		<c:if test="${articlePage.hasArticles()}"><%-- total=0일 경우 true --%>
		<tr>
			<td colspan="4">
				<c:if test="${articlePage.startPage > 5}">
				<a href="list.do?pageNo=${articlePage.startPage - 5}">[이전]</a>
		</c:if>
			<c:forEach var="pNo" begin="${articlePage.startPage}" end="${articlePage.endPage}">
			<a href="list.do?pageNo=${pNo}">[${pNo}]</a>
	</c:forEach>
			<c:if test="${articlePage.endPage < articlePage.totalPages}">
			<a href="list.do?pageNo=${articlePage.startPage + 5}">[다음]</a>
			</c:if>
		</td>
	</tr>
</c:if>
</table>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>게시글 등록</title>
</head>
<body>

<script type="text/javascript">
alert("게시글을 등록했습니다.");
</script>
<br>
${ctxPath = pageContext.request.contextPath ; ''}
<a href="${ctxPath}/article/list.do">[게시글목록보기]</a>
<a href="${ctxPath}/article/read.do?no=${newArticleNo}">[게시글내용보기]</a>
</body>
</html>
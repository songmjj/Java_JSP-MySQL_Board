<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>�Խñ� ���</title>
</head>
<body>

<script type="text/javascript">
alert("�Խñ��� ����߽��ϴ�.");
</script>
<br>
${ctxPath = pageContext.request.contextPath ; ''}
<a href="${ctxPath}/article/list.do">[�Խñ۸�Ϻ���]</a>
<a href="${ctxPath}/article/read.do?no=${newArticleNo}">[�Խñ۳��뺸��]</a>
</body>
</html>
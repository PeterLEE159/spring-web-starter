<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/resources/css/style.css" />
<title>Insert title here</title>
</head>
<body>
	<div class="container">
		<table class="table table-hover">
			<thead>
				<th>번호</th>
				<th>아이디</th>
				<th>생성날짜</th>
			</thead>
			<tbody>
				<c:forEach items="${users }" var="user">
					<tr>
						<td>${user.id }</td>
						<td>${user.username }</td>
						<td><fmt:formatDate value="${user.created_at }" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<a href="/user/create_form" class="btn btn-primary">등록</a>
		
	</div>
</body>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="/resources/js/main.js"></script>
</html>
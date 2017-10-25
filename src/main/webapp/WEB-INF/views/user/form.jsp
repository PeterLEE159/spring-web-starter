<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/resources/css/style.css" />
<title>Insert title here</title>
</head>
<body>
	<div class="container">
		<div class="jumbotron">
			<form action="/user/create" method="post">
				<div class="form-group">
					<label>username</label>
	    			<input type="text" class="form-control" name="username">
				</div>
				<div class="form-group">
					<label>password</label>
	    			<input type="password" class="form-control" name="password">
				</div>
				
				<button type="submit" class="btn btn-primary">등록</button>
				
			</form>
		</div>
	</div>
</body>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="/resources/js/main.js"></script>
</html>
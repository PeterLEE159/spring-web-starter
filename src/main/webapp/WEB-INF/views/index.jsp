<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="/resources/css/style.css" />
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" />
</head>
<body>
	<h1>여기여 요기</h1>
	<h3><span class="rating" value="3" onmouseover="starHover" onclick="starClick" name="rating" ></span></h3>
	<div id="dropzone">이미지를 여기에 드랍</div>
	<a id="a-ajax" class="btn btn-default">ajax 전송</a>
	<ul id="ul-image"></ul>
	<div>
		<input type="checkbox" class="btn-input" content="Java"/>
		<input type="checkbox" class="btn-input" content="C++"/>
		<input type="checkbox" class="btn-input" content="PHP"/>
	</div>
	<div>
		<input type="radio" class="btn-input" content="남" name="gender"/>
		<input type="radio" class="btn-input" content="여" name="gender"/>
	</div>
	
</body>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="/resources/js/main.js"></script>
<script>
	function starHover(a) {
		console.log('hover', a);
	}
	
	function starClick(a) {
		console.log('click', a);
	}
</script>
</html>
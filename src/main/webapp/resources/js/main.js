$(function() {
	/*
	 * 평점 
	 */
	(function() {
		
		// 레이팅 바
		var $rating = $('.rating'),
		hover = $rating.attr('onmouseover'),
		click = $rating.attr('onclick'),
		name = $rating.attr('name'),
		value = parseInt($rating.attr('value')),
		htmlContent = "",
		i = 0;

		$rating.attr({
			'onmouseover': '',
			'onclick': '',
			'value': ''
		});
		
		if(hover && click) { 	// 호버 레이팅바
			if(hover && click)
				for(;i<5;i++)
					htmlContent += "<span class='star star-hover"+(value >= 5-i ? " filled" : "")+"' onmouseover='innrStarHover(this, "+hover+")' onclick='innrStarClick(this, "+click+")'></span>";
			else if (hover)
				for(;i<5;i++)
					htmlContent += "<span class='star star-hover"+(value >= 5-i ? " filled" : "")+"' onmouseover='innrStarHover(this, "+hover+")'></span>";
			else if(click) 
				for(;i<5;i++)
					htmlContent += "<span class='star star-hover"+(value >= 5-i ? " filled" : "")+"' onclick='innrStarClick(this, "+click+")'></span>";

			htmlContent += "<input type='hidden' name='"+name+"' value='"+value+"'>";
			
		} else {				// 정적인 레이팅바
			for(;i<5;i++)
				htmlContent += "<span class='star"+(value >= 5-i ? " filled" : "")+"'></span>";
		}
		
		$rating.prepend(htmlContent);
		
		// 인풋 버튼
		var $btnInput = $('input.btn-input');
		
		var baseId = 'btn-input-', count = 0;
		$btnInput.each(function() {
			var $btn = $(this), id = baseId + (count++);
		
			var htmlContent = "<label for="+id+">"+$btn.attr('content')+"</label>";
			$btn.after(htmlContent).attr('id', id).attr('content', '');
		});
		
		
		// 로딩바
		var $loadingDiv = $('#div-loading'),
		$window = $(window);
		winHeight = $(window).height(),
		winWidth = $(window).width();
		
		$loadingDiv.height(winHeight);
		$loadingImg = $loadingDiv.find('img');
		$loadingImg.css({top: winHeight / 2 - 100, left: winWidth / 2 - 100});
		$('.btn-forward').click(function() {
			pageForwardAnim();
		});
	})();
});

var pageForwardAnim = function() {
	console.log('abc');
	$('div#div-loading').css({display: 'block'});
}

var innrStarHover = function(span, onmouseover) {
	var $hoverStar = $(span),
	value = $hoverStar.index();

	onmouseover(5 - value);
	
}

var innrStarClick = function(span, onclick) {
	var $hoverStar = $(span),
	$parent = $hoverStar.parent(),
	$spanStar = $parent.find(':first-child'),
	$input = $parent.find('input'),
	currValue = parseInt($input.attr('value')),
	value = $hoverStar.index(),
	i = 0;

	for(i; i< 5 ; i++) {
		if(i >= value) $spanStar.addClass('filled');
		else $spanStar.removeClass('filled');
		$spanStar = $spanStar.next();
	}

	onclick(5 - value);
	$input.attr('value', 5 - value);
	
}



/*
 * 이미지 드랍다운
 */
var Images = (function() {
	var map = {};
	var instance;
	var init = function() {
		return {
			getFiles: function() {
				return map;
			},
			setFiles: function(files) {
				map = files;
			}	
		}
	};
	
	return {
		getInstance: function() {
			if(!instance)
				instance = init();
			return instance;
		}
	}
	
})();

var divDropzone = $("#dropzone");


divDropzone.on('dragenter', function(e) {
	e.stopPropagation();
	e.preventDefault();

	$(this).css('border', '2px solid #5272A0');
});

divDropzone.on('dragleave', function(e) {
	e.stopPropagation();
	e.preventDefault();
	$(this).css('border', '2px dotted #8296C2');
});

divDropzone.on('dragover', function(e) {
	e.stopPropagation();
	e.preventDefault();
});

divDropzone.on('drop', function(e) {
	e.stopPropagation();
	e.preventDefault();
	
	$(this).css('border', '2px dotted #8296C2');
	var files = e.originalEvent.dataTransfer.files;
	
	if (files.length < 1)
		return;

	showFile(files);
});





function showFile(files) {
	
	
	var i=0, size = files.length;
	var $ul = $('#ul-image');
	for(; i< size; i ++) {
		var fileReader = new FileReader();
		fileReader.onload = function(file) {
			var html = "";
			html += "<li>";
			html +=	"	<div>";
			html += "		<img src='"+file.target.result+"'>";
			html += "	</div>";
			html += "</ul>";
			$ul.append(html);
		}
		
		fileReader.readAsDataURL(files[i]);
	}
	
	var images = Images.getInstance();
	images.setFiles(files);
}

$('#a-ajax').on('click', function() {
	var images = Images.getInstance().getFiles(),
	data = new FormData(),
	size = images.length, i =0;
	
	for(; i< size; i++) 
		data.append('file', images[i]);
	
	var url = "/upload.do";
    $.ajax({
       url: url,
       method: 'post',
       data: data,
       dataType: 'json',
       processData: false,
       contentType: false,
       success: function(res) {
           
       }
    });
});
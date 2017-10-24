$(window).scroll(function() {

	if($('.navbar').offset().top > 50) {
		$('.navbar').addClass('page-scroll');
	} else {
		$('.navbar').removeClass('page-scroll');
	}
});


$(function() {
	$('.navbar-menu a').click(function() {
		var $anchor = $(this);
		$('html, body').stop().animate({
			scrollTop: $($anchor.attr('href')).offset().top
		}, 500, 'easeInExpo');
		event.preventDefault();
	});
});
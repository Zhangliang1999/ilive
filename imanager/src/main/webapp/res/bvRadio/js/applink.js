// JavaScript Document

$(function(){
	$(".applink").append('<aside><img src="http://i.fm.hebrbtv.com/images/footerlogo.png"><p class="p-top no-margin">在即听FM发现更多精彩</p><p class="p-bottom no-margin">懂你的好声音,你懂的小世界</p><a class="a_link" href="http://fm.hebrbtv.com//download.shtml">下载</a></aside><div class="closeBox"><i class="closeL"></i><i class="closeR"></i></div>');
});
$(function () {
    $(".applink").click(function () {
    $(this).remove();
	});
});
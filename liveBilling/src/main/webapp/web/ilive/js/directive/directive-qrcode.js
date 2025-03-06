myapp.directive("qrcode", function($timeout) {
	return {
		restrict : 'A',
		scope : {
			"url" : "=url",
		},
		link : function(scope, element) {
			$timeout(function() {
				element.qrcode({
					render : "canvas", // 设置渲染方式，有table和canvas，使用canvas方式渲染性能相对来说比较好
					text : scope.url, // 扫描二维码后显示的内容,可以直接填一个网址，扫描二维码后自动跳向该链接
					width : "200", // //二维码的宽度
					height : "200", // 二维码的高度
					background : "#ffffff", // 二维码的后景色
					foreground : "#000000", // 二维码的前景色
					src : 'http://img.zcool.cn/community/0149c4554470fb0000019ae968e58c.jpg' // 二维码中间的图片
				});
			});
		}
	};
});

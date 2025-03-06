var $bg = $('.bg'),
    $innerContainer = $('.inner-container'),
    $container = $('.container'),
    $qrcode = $('#qrcode');

function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = decodeURI(window.location.search.substr(1)).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}

//判断是否是微信浏览器的函数
function isWeiXin() { 	//window.navigator.userAgent属性包含了浏览器类型、版本、操作系统类型、浏览器引擎类型等信息，这个属性可以用来判断浏览器类型
    var ua = window.navigator.userAgent.toLowerCase();
    //通过正则表达式匹配ua中是否含有MicroMessenger字符串
    if (ua.match(/MicroMessenger/i) == 'micromessenger') {
        return true;
    } else {
        return false;
    }
}

function getHeight() {
    var winWidth = $(window).width();
    var winHeight = $(window).height();
    var containeroffsetLeft = $('.container').width()+ $('.container').offset().left +200
    $bg.css({
        'width': winWidth,
        'height': winHeight
    });
    $qrcode.css({
        left:containeroffsetLeft
    })



}

function init() {
    getHeight()

}

$(window).resize(function () {
    getHeight();
});


init()
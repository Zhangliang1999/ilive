$(function() {
    //����rem��
    function calcRem() {
        var http = document.getElementsByTagName("html")[0];
        var w = document.documentElement.clientWidth;
            http.style.fontSize = w /26 + "px";
    }
    calcRem();
    window.onresize = function() {
        calcRem();
    };

});
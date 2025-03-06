var getInf;
var inf = GetQueryString('inf')

if (inf != null){
    sessionStorage.setItem('info', inf)
    getInf = inf
}else{
    getInf = sessionStorage.getItem('info');

    var newurl = updateQueryStringParameter(window.location.href, 'info', getInf);
    window.history.replaceState({
        path: newurl
    }, '', newurl);

}

if (getInf) {
    var script = document.createElement("script");
    script.type = "text/javascript";
    switch (getInf) {
        case 'dq': //欢go
            script.src = "http://image1.chinatelecom-ec.com/client/wap/common/js/s_code.js";
            break
        case  'dx': //电信学院
            script.src="https://hm.baidu.com/hm.js?e5bae42688641fe2fe03e67bcf4dbcb3"
    }

    document.body.appendChild(script);
}

function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = decodeURI(window.location.search.substr(1)).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}


//ios
function share(title, content, imgurl, weburl, zdsinfo) {
    window.location.href = "objc://share?title=" + encodeURIComponent(title) + "&content=" + encodeURIComponent(content) + "&imgurl=" + encodeURIComponent(imgurl) + "&weburl=" + encodeURIComponent(weburl) + "&zdsinfo=" + encodeURIComponent(zdsinfo);
}

function iOnShare() {
    share(_shareTitle, _shareDesc, _shareImg, url);
    return "successed";
}

//Android
function onShare() {
    CtclientJS.share(_shareTitle, _shareDesc, _shareImg, url)
}


function updateQueryStringParameter(uri, key, value) {
    if(!value) {
        return uri;
    }
    var re = new RegExp("([?&])" + key + "=.*?(&|$)", "i");
    var separator = uri.indexOf('?') !== -1 ? "&" : "?";
    if (uri.match(re)) {
        return uri.replace(re, '$1' + key + "=" + value + '$2');
    }
    else {
        return uri + separator + key + "=" + value;
    }
}

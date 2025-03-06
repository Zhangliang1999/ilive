const commonParams = {
    terminalType: "h5"
}

function createAPI(url,parmas){
    const data = Object.assign({}, commonParams, parmas)
    return new Promise(function (resolve,reject) {
        $.ajax({
            type: "GET",//请求方式 get/post
            url: url,
            dataType: "jsonp",
            jsonp: "callback",
            cache: false,
            data: parmas || '',
            success: function (res) {
                resolve(res)
            }
        });
    })
}

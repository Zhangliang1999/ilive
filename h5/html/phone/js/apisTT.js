var commonParams = {
    terminalType: "h5"
}

function createAPI(url, parmas) {
   
    var data = Object.assign({}, commonParams, parmas)
    return new Promise(function (resolve, reject) {
        console.log('--------')
        console.log(url)
        console.time();
        $.ajax({
            type: "GET",//请求方式 get/post
            url: url,
            dataType: "jsonp",
            jsonp: "callback",
            cache: false,
            data: parmas || '',
            success: function (res) {
                resolve(res)
            },
            error:function (res) {
                reject(res)
            }
        });
        console.timeEnd();
        console.log('--------')
    })
   
}


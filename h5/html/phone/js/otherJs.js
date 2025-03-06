function OtherJs_CbCount(enterpriseId) {
    var cbCount = 0;

    switch (enterpriseId) {
        case 1969: //李曰
            cbCount = 15000;
            break
        case 421: //少林5G嵩山
            cbCount = 56319
            break
    }

    // if (enterpriseId == 1969) { //李曰
    //     cbCount = 15000;
    // }
    // if (enterpriseId == 421) { //少林5G嵩山
    //     cbCount = 56319
    // }
    return cbCount
}

function ChongQingAdv(enterpriseId) {
    //2442
    if (enterpriseId == 2442) {
        var dom = '<div class="cell ChongQingAdv"><a href="http://cloud.189.cn/m.jsp?from=chongqing-01"><img src="http://pic.zb.tv189.com/img//2019_04/16/1555377295829.jpg" alt=""></a></div>';
        $('#zbjcompany').after(dom)
    }
}


// window.onload = function () {
//     myPlayer.ready(function () {
//         // 重庆广场舞
//         if (Params.fileId == 16363) {
//             this.poster('http://pic.zb.tv189.com/img/2019_04/16/1555378584815.jpg')
//         }

//         if (Params.fileId == 20118) {
//             this.poster('http://pic.zb.tv189.com/img//2019_05/21/1558400944988.jpg')

//         }
//         if (Params.fileId == 20117) {
//             this.poster('http://pic.zb.tv189.com/img//2019_05/21/1558400619658.jpg')

//         }
//     })
// }

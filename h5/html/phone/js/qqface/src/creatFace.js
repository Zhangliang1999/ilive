//传入arr 和图片路径 返回html字符串
module.exports = function(arr, imgPath) {
    return $.map(arr, function(item, index) {
        item = item.split(',');
        var src = imgPath + item[0] + '.gif';
        return '<i data-code="' + item[1] + '"><img src="' + src + '" width="24" height="24" title="' + item[1] + '" /></i>';
    }).join('');
}
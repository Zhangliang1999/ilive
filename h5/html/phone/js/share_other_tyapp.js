var options = {
    shareUrl: window.location.href,
    cover: _shareImg,
    title: _shareTitle,
    content: _shareTitle
};
TysxJsSdk.invokeJsApi("set_shareInfo", options, function (data) {
    //回调的逻辑处理
    console.log(data);
});

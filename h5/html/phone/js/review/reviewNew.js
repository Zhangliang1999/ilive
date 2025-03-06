if (!/Android|webOS|iPhone|iPod|iPad|BlackBerry/i.test(navigator.userAgent)) {
    window.location.href = "http://" + h5Base + "/pc/review.html?fileId=" + Params.fileId + "&roomId=" + Params.roomId; //PC
}
if (Params.fileId == '') {
    errorfunction()

} else {
    if (judgeDeviceType.isWeiXin) {
        // 执行操作
        createAPI("http://" + tomcatBase + "/ilive/app/wx/app.jspx", {
                fileId: Params.fileId,
                roomId: Params.roomId
            })
            .then(function (res) {
                var status = res.loginWx;
                if (status == 1) {
                    window.stop();
                    window.location.href = res.loginUrl;
                    return false;
                }
            }).then(function () {
                init()
            })
    } else {
        init()
    }
}

function init() {

    checklogin(function () {
        //引导鉴权
        guide(function (liveEvent) {
            var viewAuthorized = liveEvent.viewAuthorized;
            /*
            2.密码观看 3.付费
            */
            switch (viewAuthorized) {
                case 2:
                    setlogin('password')
                    //window.location.href = "http://" + h5Base + "/phone/password.html?roomId=" + Params.roomId + "&fileId=" + Params.fileId;
                    break;
                case 3:
                    console.log(3)
                    break
                case 6:
                    setlogin('FCode')
                    //window.location.href = "http://" + h5Base + "/phone/FCode.html?roomId=" + Params.roomId + "&fileId=" + Params.fileId;
                    break
                default:
                    $('#loding').hide()
                    _template.review_page() //回看
                    myPlayer = videojs('video');
                    _height = $(window).height() - $('.menuBox').offset().top
                    createAPI("http://" + tomcatBase + "/ilive/app/room/vod/fileinfo.jspx", {
                        fileId: Params.fileId
                    }).then(function (getMediaFile) {
                        if (getMediaFile.code == 1) {
                        
                            setInit(getMediaFile) //设置初始
                        } else {
                            layer.open({
                                content: getMediaFile.message,
                                btn: '确定',
                                yes: function () {
                                    errorfunction()
                                }
                            });
                        }

                    }, function (res) {
                        layerOpen('获取数据失败')
                    })
                    break
            }
        })
    })
}

function checklogin(callback) {
    createAPI("http://" + tomcatBase + "/ilive/app/room/vod/checklogin.jspx", {
        fileId: Params.fileId,
        roomId: Params.roomId
    }).then(function (res) {
        if (res.code == 1) {

            var roomNeedLogin = res.data.roomNeedLogin;
            if (roomNeedLogin === 1) {
                setlogin()
                //window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + Params.roomId + "&fileId=" + Params.fileId + "&userId=" + userId;
            } else {
                var data = JSON.parse(res.data);
                var userInfo = data.userInfo;
                if (userInfo != undefined || userInfo != null) {
                    userId = userInfo.userId;
                    $("input[name=userId]").val(userInfo.userId);
                }

                callback()

            }

        } else {
            window.location.href = "404.html";
        }
    })
}

function guide(callback) {
    createAPI("http://" + tomcatBase + "/ilive/app/room/vod/guide.jspx", {
        fileId: Params.fileId,
        roomId: Params.roomId
    }).then(function (res) {
        switch (res.code) {
            case 1:
                var liveEvent = res.data.liveEvent;
                callback(liveEvent)
                break
            case 402:
                setlogin()
                //window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + Params.roomId + "&fileId=" + Params.fileId + "&userId=" + userId;
                break
            case 404:
                window.location.href = "http://" + h5Base + "/phone/end.html";
                break
            default:
                layer.open({
                    content: '引导鉴权失败',
                    btn: '确定',
                    yes: function (index) {
                        errorfunction()
                    }
                });
                break

        }
    })


}


//设置初始
function setInit(data) {
    try {

        if (data.data != null && data.data != undefined) {
            var mediaFile = {
                mediaFileName: data.data.mediaFileName,
                mediaFileDesc: data.data.mediaFileDesc,
                filePath: data.data.filePath,
                viewNum: data.data.viewNum,
                fileCover: data.data.fileCover,
                fileCoverImg: data.data.fileCoverImg,
                allowComment: data.data.allowComment, //评论开关
                isFileDoc: data.data.isFileDoc //文档开关
            };

            //sessionStorage.setItem('review', JSON.stringify(options))
            document.title = mediaFile.mediaFileName;
            $(".liveTitle").html(mediaFile.mediaFileName);
            $('.viewNum').text(setNum(mediaFile.viewNum));

            setVideo(mediaFile); //视频

            /*
            相关设置
            **/
            // 企业信息
            createAPI("http://" + tomcatBase + "/ilive/file/enterprise.jspx", {
                userId: userId,
                fileId: Params.fileId
            }).then(function (getEnterprise) {
                setEnterprise(getEnterprise) //设置企业
            })

            //相关视频
            createAPI("http://" + tomcatBase + "/ilive/app/room/getrelatedlist.jspx", {
                fileId: Params.fileId
            }).then(function (getFileList) {
                setFileList(getFileList)
            })

            //自定义分享
            createAPI("http://" + tomcatBase + "/ilive/app/wx/sharefileinfo.jspx", {
                fileId: Params.fileId
            }).then(function (shareinfo) {
                //发送给朋友
                _shareTitle = shareinfo.data.friendCircle.mediaFileName;
                _shareImg = shareinfo.data.friendCircle.shareImg;
                _shareDesc = shareinfo.data.friendCircle.mediaFileDesc;

                _shareTitle2 = shareinfo.data.friendSingle.mediaFileName;
                _shareImg2 = shareinfo.data.friendSingle.shareImg;
                _shareDesc2 = shareinfo.data.friendSingle.mediaFileDesc;


                options = {
                    shareUrl: window.location.href,
                    cover: _shareImg,
                    title: _shareTitle,
                    content: _shareDesc,
                    cover2: _shareImg2,
                    title2: _shareTitle2,
                    content2: _shareDesc2
                };
                //分享
                reviewshareInfo(options);
            })

            //判断文档
            if (mediaFile.isFileDoc == 1) {
                showFileDoc();
                $('#wendang').css('display', 'block');
            }

            //判断评论
            if (mediaFile.allowComment == 0) { //不允许评论
                $('.chatipt').find('span').text('目前还不能讨论哦~');
                $('#talkLi').css('display', 'none');
                $('.menuBox').find('li').last().css('display', 'none');
            } else {
                //加载评论
                showcomments()
            }


            //评论弹层
            window.chatipt.onclick = function () {
                if (mediaFile.allowComment == 0) return //未开启评论
                chatiptOpen('review')
                // //没有登陆
                // checkUserID()
                //
                // layer.open({
                //     type: 1,
                //     className: 'CB',
                //     content: talkCon(),
                //     anim: 'up'
                // });
                // setTimeout(function () {
                //     A_Emoji($('.box'));
                // }, 0)
                //
                // $('.textarea').on('input propertychange', function () {
                //     if ($('.textarea').val().length != 0) {
                //         $(".sendMessage").removeAttr("disabled")
                //         $(".sendMessage").css("background", "#0084ff");
                //     } else {
                //         $(".sendMessage").css("background", "#b2daff");
                //     }
                // });
            }
            //简介弹层
            window.showDesc.onclick = function () {
                popup({
                    title: '简介',
                    content: desc(mediaFile.mediaFileDesc)
                })
            }

        }
    } catch (error) {
        layerOpen(error)
    }


}
//
//设置企业
function setEnterprise(data) {
    if (data.code == 1) {
        var enterprise = {
            enterpriseId: data.data.enterpriseId,
            enterpriseImg: data.data.enterpriseImg,
            enterpriseName: data.data.enterpriseName,
            enterpriseDesc: data.data.enterpriseDesc,
            concernTotal: data.data.concernTotal,
            certStatus: data.data.certStatus,
            concernStatus: data.data.concernStatus,
            isReg: data.isReg

        }
        /**caibin**/
        // var cbCount = 0
        // if (enterprise.enterpriseId == 1969) { //李曰
        //     cbCount = 15000
        // }
        var cbCount = OtherJs_CbCount(enterprise.enterpriseId) //李曰
        ChongQingAdv(enterprise.enterpriseId); //重庆广告位置
        /**caibin**/
        var concernTotal = cbCount + enterprise.concernTotal; //粉丝数
        concernTotalTemp = concernTotal;
        concernTotal = setNum(concernTotal); //关注人数

        /**
         * 企业认证状态 0未提交认证 1认证中 4认证通过 5认证失败
         */

        if (enterprise.certStatus == 4) { //4认证通过
            var guanzhu = {
                type: 1,
                className: 'guanzhu icon-jia',
            }
            if (enterprise.concernStatus != 0) {
                guanzhu = {
                    type: 0,
                    className: 'yiguanzhu',
                }
            }
            //关注设置
            $('#concernStatus').css('display', 'block')
                .addClass(guanzhu.className)
                .attr('onclick', 'updateConcernStatus(' + enterprise.enterpriseId + ',' + guanzhu.type + ',event)')

        }

        //是否点赞过
        if (enterprise.isReg == 1) {
            $('.dianzan').find('i').addClass('checked')
        }
        //查询多少个点赞
        createAPI("http://" + tomcatBase + "/ilive/register/MediaRegisterCount.jspx", {
            fileId: Params.fileId
        }).then(function (res) {
            if (res.code == 0) {
                layer.open(res.message)
                return false
            }
            $('#dianzan-tag').text(res.data.count)


        })
        //赋值
        $('#companyName').text(enterprise.enterpriseName);
        $('#concernTotal').find('span').text(setNum(concernTotal)); //粉丝数量
        getEnterprise(enterprise.enterpriseId); //企业logo

    } else {
        $('#zbjcompany').hide()
    }

}

//设置相关视频
function setFileList(data) {
    if (data.code === 1 && data.data.length > 0) {
        var dom = createReviewFile(data.data);
        $('#reviewsAll').css('display', 'block')
            .find('.swiper-wrapper').html(dom);

        new Swiper('#reviews', {
            slidesPerView: 2.5,
            spaceBetween: 5
        });

        window.showAllReview.onclick = function () {
            var dom = _template.show_all_review(data.data);
            $('.review-List').append(dom)
            popup({
                id: 'review',
                title: '相关视频',
                content: $('#review').html()
            });

        }
    }

}

function changeVideo(fileId) {
    window.location.href = "http://" + h5Base + "/phone/review.html?roomId=" + Params.roomId + "&fileId=" + fileId;
}

function createReviewFile(data) {
    var arr = [];
    data.forEach(function (value) {
        var str =
            '<div class="swiper-slide">\n' +
            '  <div class="review-group" onclick="changeVideo(' + value.fileId + ')">\n' +
            '    <div class="review-img">\n' +
            '      <img src="' + value.fileImg + '" alt="">\n' +
            '      <div class="reviewMask"><span>' + value.fileDuration + '</span></div>\n' +
            '    </div>\n' +
            '    <div class="review-title">' + value.fileName + '</div>\n' +
            '  </div>\n' +
            '</div>\n';
        arr.push(str);
    })
    return arr.join('')

}

// function show_all_review(data) {
//     var arr = [];
//     var authType = []
//     var authTxt = ['公开观看', '密码观看', '付费观看', '白名单观看', '登录观看']
//     data.forEach(function (value) {
//         var recallingDiv = "<div class=\"reviewList\" id=\"fileId_" + value.fileId + "\" onclick=\"changeVideo(" + value.fileId + ")\" >";
//         recallingDiv += "   <div class=\"reviewLeft\">";
//         recallingDiv += "       <img src=\"" + value.fileImg + "\" alt=\"\" />";
//         recallingDiv += "       <div class=\"reviewMask\">";
//         recallingDiv += "           <span>" + value.fileDuration + "</span>";
//         recallingDiv += "       </div>";
//         recallingDiv += "   </div>";
//         recallingDiv += "   <div class=\"reviewRight\">";
//         recallingDiv += "       <div class=\"reviewRightTop\">" + value.fileName + "</div>";
//         recallingDiv += "       <div class=\"reviewRightBottom\">" + value.createTime + "</div>";
//         recallingDiv += "       <div class=\"reviewIcon\">";
//         recallingDiv += "           <div class=\"reviewIcondiv\"><i class=\"reviewplayIcon iconfont icon-video reviewplay\"><span>" + value.viewNum + "次</span></i></div>";
//
//         switch (value.authType) {
//             case 1:
//                 authType = 'icon-yan'
//                 break
//             case 3:
//                 authType = 'icon-qian'
//                 break
//             default:
//                 authType = 'icon-mima'
//                 break
//
//         }
//         recallingDiv += "           <div class=\"reviewIcondiv\"><i class=\"reviewplayIcon iconfont " + authType + " gongkai\"><span>" + authTxt[value.authType - 1] + "</span></i></div>";
//         recallingDiv += "       </div>";
//         recallingDiv += "   </div>";
//         recallingDiv += "</div>";
//
//         arr.push(recallingDiv)
//     })
//     return '<div id="review">' + arr.join('') + '</div>'
// }

//设置视频路径
function setVideo(mediaFile) {
    var filePath = mediaFile.filePath;
    var poster = mediaFile.fileCoverImg || 'http://pic.zb.tv189.com/img/livebg_default.png';
    myPlayer.ready(function () {
        this.src({
            src: filePath
        })
        this.poster(poster)
        this.on('error', function () {
            var mediaError = this.error();
            console.log(mediaError);
            if (mediaError.code == 1) { //MEDIA_ERR_ABORTED 视频播放被终止
                layer.alert("视频播放被终止");
            } else if (mediaError.code == 2) { //MEDIA_ERR_NETWORK 网络错误导致视频下载中途失败
                layer.alert("网络错误导致视频下载中途失败");
            } else if (mediaError.code == 3) { //MEDIA_ERR_DECODE 由于视频文件损坏或是该视频使用了你的浏览器不支持的功能，播放终止。
                layer.alert("由于视频文件损坏或是该视频使用了你的浏览器不支持的功能，播放终止。");
            } else if (mediaError.code == 4) { //MEDIA_ERR_SRC_NOT_SUPPORTED 视频因格式不支持或者服务器或网络的问题无法加载。
                layer.alert("视频因格式不支持或者服务器或网络的问题无法加载。");
            } else if (mediaError.code == 5) { //MEDIA_ERR_ENCRYPTED 视频已加密，无法解密。
                layer.alert("视频已加密，无法解密。");
            }
        })
        // this.on('loadedmetadata', function () {
        //     console.log(this.buffered().start(0));
        //
        // })
    })


}

// function setNum(num) {
//     if (num > 9999) {
//         num = (num / 10000).toFixed(1) / 1 + 'w'
//     }
//     return num
//
// }

//加载文档
var fileDoc = null;
var popupDisable = false; //取消多次弹层
var docArr = [];

function showFileDoc() {
    createAPI("http://" + tomcatBase + "/ilive/document/getByfileId.jspx", {
            fileId: Params.fileId
        })
        .then(function (res) {
            if (res.code == 1) {
                $('#fileDoc').css('display', 'block');
                createFileDoc(res.data);
            }
        })
}

function createFileDoc(res) {
    var arr = [];
    res.forEach(function (value) {
        var str = '<dl class="document document_' + value.document.id + '" data-docid="' + value.document.id + '">\n' +
            '     <dd>\n' +
            '      <img src="css/review/images/icon-p.png" class="ppt" alt="">\n' +
            '      <div class="document-name ellipsis" onclick="fileDocDetail(' + value.document.id + ')">' + value.document.name + '</div>\n' +
            '      <div class="links"><i class="iconfont icon-youjiantou"></i></div>\n' +
            '      <div class="fileImgs"></div>\n' +
            '     </dd>\n' +
            '</dl>\n';
        arr.push(str)
    });
    fileDoc = '<div class="document" style="height: 100vh">' + arr.join('') + '</div>';
    $('#document').html(arr.join(''))
}


function fileDocDetail(docId) {
    if (!popupDisable) {
        popup({
            id: 'document',
            title: '文档预览',
            content: fileDoc
        });
        popupDisable = true;
    }
    //获取详细文档
    var curDoc = $('.showPopupContent').find('.document_' + docId);
    if (curDoc.hasClass('open')) {
        curDoc.removeClass('open');
        return
    } else {
        curDoc.addClass('open').siblings().removeClass('open');
    }
    var mescroll2 = new MeScroll("mescroll_document", {
        down: {
            use: false,
        },
        up: {
            use: false,
        }
    });
    mescroll2.scrollTo(curDoc[0].offsetTop - 50);
    if (docArr[docId] == undefined) { //如果第一次没有数据 那数据
        createAPI("http://" + tomcatBase + "/ilive/document/getPicByDocId.jspx", {
            docId: docId
        }).then(function (res) {
            if (res.code == 1) {
                docArr[docId] = res.data
                wendangJS(curDoc, res.data)
            }
        })
    } else {
        wendangJS(curDoc, docArr[docId])
    }


}

function wendangJS(dom, documentPictures) {
    var str = '<div class="swiper-container " id="gallery_' + documentPictures[0].docId + '">';
    str += '<div class="swiper-wrapper">\n'
    for (var i = 0; i < documentPictures.length; i++) {
        var url = documentPictures[i].url;
        str += '<div class="swiper-slide"><img src="' + url + '"></div>\n'
    }
    str += '</div>\n'
    str += '</div>\n'
    str += '<div class="swiper-container thumbs" id="thumbs_' + documentPictures[0].docId + '">\n'
    str += ' <div class="swiper-wrapper">\n'
    for (var i = 0; i < documentPictures.length; i++) {
        var url = documentPictures[i].url;
        str += '<div class="swiper-slide"><img src="' + url + '"><span>' + (i + 1) + '</span></div>\n'
    }
    str += '</div>';
    dom.find('.fileImgs').html(str);

    var thumbsSwiper = new Swiper('#thumbs_' + documentPictures[0].docId, {
        spaceBetween: 10,
        slidesPerView: 3,
        watchSlidesVisibility: true,
        watchSlidesProgress: true,
        freeMode: true,
        freeModeMomentumRatio: 5
    });

    new Swiper('#gallery_' + documentPictures[0].docId, {
        thumbs: {
            swiper: thumbsSwiper,
        },
        lazy: {
            loadPrevNext: true,
            loadPrevNextAmount: 2,
        }
    })


}


//加载评论
var mescroll;

function showcomments() {
    mescroll = new MeScroll("mescroll", {
        down: {
            use: false,
        },
        up: {
            auto: true,
            isBounce: false,
            offset: 150,
            noMoreSize: 8,
            page: {
                size: 10,
            },
            empty: {
                warpId: no_comment,
                icon: "css/review/images/nocomment.png", //图标,默认null
                tip: "还没有讨论，快来聊几句吧~~", //提示
            },
            htmlNodata: '<p class="upwarp-nodata">-- 我是有底线的 --</p>',
            callback: upCallback,
        }
    });
    setTimeout(function () {

        $(".menuBox").find('li').on('click', function () {
            if ($(this).hasClass('liactive')) return
            if (this.className == 'talkLi') {
                var talkli = document.querySelectorAll('.talkLi')[1];
                var offset = talkli.offsetTop + 1;
                $(talkli).css("min-height", _height);
                mescroll.scrollTo(offset);
                return
            }
            mescroll.scrollTo(0);
        })

        $(".mescroll").scroll(function () {
            nScrollTop = $(this)[0].scrollTop;
            var talkli = document.querySelectorAll('.talkLi')[1];
            var offset = talkli.offsetTop;
            $('.menuBox').find('li.zbjj').addClass('liactive').siblings().removeClass('liactive')
            if (nScrollTop > offset) {
                $('.menuBox').find('li.talkLi').addClass('liactive').siblings().removeClass('liactive')
            }
        })
    }, 0)

    // $(".mescroll").scroll(function () {
    //     nScrollHight = $(this)[0].scrollHeight;
    //     nScrollTop = $(this)[0].scrollTop;
    //     console.log(talkOffsetTop)
    // })

}

//下拉刷新
function upCallback(page) {
    getListDataFromNet(page.num, page.size, function (curPageData) {
        mescroll.endSuccess(curPageData.length);
        setListData(curPageData);
    }, function () {
        mescroll.endErr();
    });
}

function getListDataFromNet(pageNum, pageSize, successCallback, errorCallback) {
    setTimeout(function () {
        try {
            createAPI("http://" + tomcatBase + "/ilive/app/room/vod/getcomments.jspx", {
                fileId: Params.fileId,
                pageNo: pageNum,
                pageSize: pageSize
            }).then(function (res) {
                successCallback && successCallback(res.data);
            })
        } catch (e) {
            errorCallback && errorCallback();
        }
    }, 1000)
}

function setListData(res) {
    var dom = [];
    var str = '';
    res.forEach(function (value) {
        str = commentDom(value)
        dom.push(str)
    });
    $('#comment').append(dom.join(''))
}

//显示评论dom
function commentDom(value) {
    var cusotm = {
        className: '',
        css: ''
    };
    if (value.topFlagNum == 1) { //置顶
        cusotm.className = ' topBox';
        cusotm.css = ' style="display: inline-block"'
    };
    return str = '<div class="talkline' + cusotm.className + '" id="' + value.commentsId + '">\n' +
        '          <div class="userImg"><img src="' + value.userImg + '"></div>\n' +
        '          <div class="commentText">\n' +
        '               <div class="inlineName">\n' +
        '                   <span class="senderName">' + value.commentsUser + '<i class="questionIcon stickIcon" ' + cusotm.css + '></i></span>\n' +
        '                   <span class="createTime">' + value.createTime + '</span>\n' +
        '               </div>\n' +
        '             <div class="comment">' + value.comments + '</div>\n' +
        '           </div>' +
        '      </div>';
}

//发送评论
// function saveComments() {
//     var msgContent = $.trim($("input[name=msgContent]").val());//发送内容
//     if (msgContent.replace("/^s+|s+$/g+", "") == "" || msgContent == undefined) {
//         layerOpen('请输入消息内容')
//         return false
//     } else {
//         createAPI("http://" + tomcatBase + "/ilive/app/room/vod/pushcomments.jspx",
//             {
//                 userId: Params.userId,
//                 fileId: Params.fileId,
//                 comments: encodeURI(msgContent)
//             }).then(function (res) {
//             if (res.code == -1 || res.code == 0) {
//                 layerOpen(res.message)
//             } else if (res.code == 1) {
//                 var str = commentDom(res.data)
//                 if ($('.mescroll-empty').length > 0) $('.mescroll-empty').hide()
//                 //如果有置顶数据追加置顶下面
//                 var topLen = $('#comment').find('.topBox').length
//                 if (topLen > 0) {
//                     $('#comment').find('.topBox').eq(topLen - 1).after(str)
//                 } else {
//                     $('#comment').prepend(str)
//                 }
//
//                 $("textarea[name=msgContent]").val("")
//                 layer.closeAll()
//
//             } else {
//                 layerConfim(res.message, function () {
//                     window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + Params.roomId + "&fileId=" + Params.fileId + "&userId=" + Params.userId
//                 })
//             }
//         });
//     }
//
// }

//发送点赞
function saveMediaFile() {
    createAPI("http://" + tomcatBase + "/ilive/register/registerMedia.jspx", {
        fileId: Params.fileId,
        userId: userId
    }).then(function (res) {
        $('.dianzan').find('i').addClass('checked')
        var dianzanTag = $('#dianzan-tag');
        var tagNum = dianzanTag.text();
        dianzanTag.text(++tagNum)
    })
}


// //弹层
// function popup(obj) {
//     var index = layer.open({
//         type: 1,
//         className: 'CB',
//         content: showPopupContent(obj),
//         anim: 'up',
//         shadeClose: false,
//         style: ' height:' + _height + 'px; '
//     });
//
//     $('.showPopupContent .close').on('click', function () {
//         layer.close(index)
//         popupDisable = false
//     })
// }
//
// function showPopupContent(obj) {
//     return '<div class="showPopupContent">' +
//         '  <div class="header"><span class="ellipsis">' + obj.title + '</span><i class="close iconfont icon-close"></i></div>' +
//         '  <div class="body mescroll" id="mescroll2">' + obj.content + '</div>' +
//         '</div>'
// }

//简介弹层
function desc(content) {
    var title = $('.liveTitle').html();
    var view = $('.play-num').html();
    return '<div class="desc-content">\n' +
        '       <div class="title liveTitle">' + title + '</div>\n' +
        '       <div class="play-num">' + view + '</div>\n' +
        '  <div class="content">' + content + '</div>\n'
    '   </div>\n';
}

//评论输入
function talkCon() {
    return '<div id="talkCon">\n' +
        '    <div class="talkCon box">\n' +
        '        <input type="text"  class="textarea"  placeholder="聊几句吧" id="saytext" onblur="wchatHackInput()" name="msgContent">\n' +
        '        <div class="talkBottom">\n' +
        '            <div class="hiddenDiv" style="display: none;"></div>\n' +
        '            <div class="newhiddenDiv" style="display: none;"></div>\n' +
        '            <div class="biaoqing emotion" data="emoji"></div>\n' +
        '            <button class="sendMessage" disabled onclick="saveComments()">发送</button>\n' +
        '            <div class="emojiBox" style="display: none;">\n' +
        '                <section class="emoji_container"></section>\n' +
        '            </div>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '</div>'
}

//是否有userid
// function checkUserID() {
//
//     if (userId == 0) {
//         window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + Params.roomId + "&fileId=" + Params.fileId + "&userId=" + userId;
//         return false
//     }
// }
// $(document.body).on('click','.videoBox',function(){
//     alert()
// })
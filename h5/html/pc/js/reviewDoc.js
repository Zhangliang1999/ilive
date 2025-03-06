// 回看视频文档

function documentShow(num) {
    $.ajax({
        type: "GET",
        url: "http://" + tomcatBase + "/ilive/document/getByfileId.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            fileId: fileId,
        },
        success: function (data) {
            if (data.code == 1) {

                var documentList = data.data;
                var HTML = "";
                for (var i = 0; i < documentList.length; i++) {
                    var document = documentList[i].document;
                    // console.log('**********', document)
                    var url = document.url;
                    var name = document.name;
                    var size = document.size;
                    var type = document.type;
                    HTML += "<tr>"
                    HTML += "	<td class=\"col-sm-1\"><input type=\"checkbox\" style=\"vertical-align: middle;\" title=\"" + i + "\"  url=\"" + url + "\"/></td>";
                    HTML += "	<td>";
                    if (type == "doc" || type == "docx") {
                        HTML += "		<img class=\"pptPic\" src=\"images/word.png\" alt=\"\" />" + name;
                    } else {
                        HTML += "		<img class=\"pptPic\" src=\"images/ppt.png\" alt=\"\" />" + name;
                    }
                    HTML += "	</td>";
                    // HTML += "	<td>" + size + "b</td>";
                    // HTML += "	<td onclick=\"downloadFile('" + url + "')\"><i class=\"fa fa-download\" style=\"font-size:14px;cursor: pointer;\"></i></td>";
                    HTML += "</tr>";
                }
                $("#fileTbody").html(HTML);

                var documentId = documentList[num].document.id;
                var documentDownload = documentList[num].document.isAllow;
                if (documentDownload == 0) {
                    documentDownload = 1
                } else {
                    documentDownload = 0
                }
                $.ajax({
                    type: "GET",
                    url: "http://" + tomcatBase + "/ilive/document/getPicByDocId.jspx",
                    dataType: "jsonp",
                    jsonp: "callback",
                    cache: false,
                    data: {
                        docId: documentId
                    },
                    success: function (data) {
                        var HTML = "";

                        HTML += '<div class="swiper-container" id="gallery">';
                        HTML += '  <div class="swiper-wrapper">';
                        HTML += '  </div></div>';
                        HTML += '<div class="pagintatioin">' +
                            '<div class="prevAll"><i class="iconfont icon-prevall"></i></div>' +
                            '<div class="prev"><i class="iconfont icon-prev"></i></div>' +
                            '<div class="swiper-pagination document-pagination"></div>' +
                            '<div class="next"><i class="iconfont icon-next"></i></div>' +
                            '<div class="nextAll"><i class="iconfont icon-nextall"></i></div>' +
                            '<div class="inputText"><input type="text" value="1"></div>' +
                            '<div class="documentBtn"><button>确定</button></div>' +
                            '<div class="download reviewDown"><span></span></div>' +
                            '</div>';

                        HTML += ''
                        $(".document").html(HTML);

                        var documentPictures = data.data
                        var documentPageNO = 1;
                        var documentManual = 1;
                        wendangJS(documentPictures, documentPageNO - 1);

                        if (documentDownload == 0) {
                            $(".download span").css("display", "none");
                        } else {
                            $(".download span").css("display", "inline-block");
                        }
                        $(".pagintatioin >div.download span").click(function () {
                            var url = documentList[num].document.url
                            window.open(url)
                        })

                        $('#fileTbody tr').click(function () {
                            var index = $(this).index()
                            $(this).addClass('active').siblings().removeClass('active');
                            $("#fileTbody tr").each(function () {
                                $(this).find('input').prop('checked', false)
                                if ($(this).hasClass('active')) {
                                    $(this).find('input').prop('checked', true)
                                }
                            });
                            $('.modal-footer .btn').click(function () {
                                $('#wdModal').hide()
                                documentShow(index)
                            })
                        })

                        $('#fileTbody tr').eq(num).trigger('click')
                    }
                })

            } else {
                layerAlert(data.message)
            }
        }
    });
}

function checkslide(documentPageNO) {
    $('.pagintatioin .prevAll').click(function () {
        gallerySwiper.slideTo(0, 1000, false);
    })

    $('.pagintatioin .prev').click(function () {
        gallerySwiper.slidePrev();
    })

    $('.pagintatioin .next').click(function () {
        gallerySwiper.slideNext();
    })
    $('.pagintatioin .nextAll').click(function () {
        var num = $('.swiper-pagination-total').text()
        gallerySwiper.slideTo(num - 1, 1000, false);
    })
    $('.pagintatioin .documentBtn').click(function () {
        var num = $('.inputText').find('input').val()
        gallerySwiper.slideTo(num - 1, 1000, false);
    })
}
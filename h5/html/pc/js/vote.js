/**
 * Created by Administrator on 2018/6/12.
 */

var vote_num = 0;
var problemAnswers = "";
var voteId = 0;//投票活动ID
var isOpen = 0;//是否对用户公开结果    0不公开   1公开
var evTimeStamp = 0; //label 执行2次问题
var Arr_Answer = []
var flat
function checklg(obj) {

    var _this = $(obj)
    var $parent = _this.parents('.MainUl')
    var MaxVotenum = $parent.data('maxvotenum')
    var now = new Date();
    if (now.getTime() - evTimeStamp < 100) {
        return;
    }
    evTimeStamp = now;
    var ChooseVotenum = $parent.find($("input[type='checkbox']:checked")).length
    if (ChooseVotenum > MaxVotenum) {
        _this.prop('checked', false)
        $.DialogBySHF.Alert({
            Width: 350,
            Height: 200,
            Title: "警告",
            Content: '选择项大于最大限制数'
        });
    }

    var problem_Answer = _this.attr("problem_Answer");
    if (_this.prop('checked')) {
        Arr_Answer.push(problem_Answer)
    } else {
        var index = Arr_Answer.indexOf(problem_Answer);
        if (index > -1) {
            Arr_Answer.splice(index, 1);
        }
    }

    console.log(Arr_Answer)
}

function checkVote() {

    $("#QuestonBox").find('.MainUl').each(function (index, item) {
        flat = true
        var i = 0
        $(item).find($("input[type='checkbox']")).each(function () {
            if ($(this).prop('checked')) {
                i++;
            }
        })
        if (i <= 0) {
            var content = "Q" + (index + 1) + "问题未选择"
            $.DialogBySHF.Alert({
                Width: 350,
                Height: 200,
                Title: "警告",
                Content: content
            });
            flat = false
            return false;
        }

    });
}

function next() {
    checkVote ()
    if (flat){
        problemAnswers = Arr_Answer.join(',')
        var userId = $("input[name=userId]").val();//用户ID
        $.ajax({
            type: "GET",//请求方式 get/post
            url: "http://" + tomcatBase + "/ilive/vote/addVote.jspx",
            dataType: "jsonp",
            jsonp: "callback",
            cache: false,
            data: {
                userId: userId,
                problemAnswers: problemAnswers,
                voteId: voteId
            },
            success: function (data) {
                if (data.code == 1) {
                    successVode('投票提交已完成', '感谢您的参与');
                } else {
                    layerOpen(data.message)
                    return false
                    // $.DialogBySHF.Alert({
                    //   Width: 350,
                    //   Height: 200,
                    //   Title: "警告",
                    //   Content: data.message
                    // });
                }
            }
        });

    }

    return


    // if (voteId == 0) {
    //     $.DialogBySHF.Alert({
    //         Width: 350,
    //         Height: 200,
    //         Title: "提示",
    //         Content: "投票活动标示未获得"
    //     });
    // } else {
    //     //获取展示内容元素对象
    //     var main = document.getElementById("QuestonBox");
    //     //判断是否为最后一题
    //     if (num == 0) {
    //         var ret = true;
    //         var content = "你没有选择答案，无法继续下一题";
    //         var i = 0;
    //         var contentAn = problemAnswers;
    //         $("#QuestonBox li").each(function (index, item) {
    //             if ($(item).find("input").attr("checked") == "checked") {
    //                 i = i + 1;
    //                 var problem_Answer = $(item).find("input").attr("problem_Answer");
    //                 problemAnswers = problem_Answer + "," + problemAnswers;
    //                 ret = false;
    //             }
    //         });
    //         var maxVoteNum = $("#QuestonBox li").attr("maxVoteNum");
    //         if (i > maxVoteNum) {
    //             content = "选择项大于最大限制数";
    //             ret = true;
    //         }
    //         if (ret) {
    //             problemAnswers = contentAn;
    //             layerOpen(content)
    //             return false
    //             // $.DialogBySHF.Alert({
    //             //       Width: 350,
    //             //       Height: 200,
    //             //       Title: "警告",
    //             //       Content: content
    //             // });
    //         } else {
    //             console.log(problemAnswers);
    //             var userId = $("input[name=userId]").val();//用户ID
    //             $.ajax({
    //                 type: "GET",//请求方式 get/post
    //                 url: "http://" + tomcatBase + "/ilive/vote/addVote.jspx",
    //                 dataType: "jsonp",
    //                 jsonp: "callback",
    //                 cache: false,
    //                 data: {
    //                     userId: userId,
    //                     problemAnswers: problemAnswers,
    //                     voteId: voteId
    //                 },
    //                 success: function (data) {
    //                     if (data.code == 1) {
    //                         successVode('投票提交已完成', '感谢您的参与');
    //                     } else {
    //                         layerOpen(data.message)
    //                         return false
    //                         // $.DialogBySHF.Alert({
    //                         //   Width: 350,
    //                         //   Height: 200,
    //                         //   Title: "警告",
    //                         //   Content: data.message
    //                         // });
    //                     }
    //                 }
    //             });
    //         }
    //     } else {
    //         var ret = true;
    //         var content = "你没有选择答案，无法继续下一题";
    //         var i = 0;
    //         $("#QuestonBox li").each(function (index, item) {
    //             if ($(item).find("input").attr("checked") == "checked") {
    //                 i = i + 1;
    //                 var problem_Answer = $(item).find("input").attr("problem_Answer");
    //                 problemAnswers = problemAnswers + problem_Answer + ",";
    //                 ret = false;
    //             }
    //         })
    //         var maxVoteNum = $("#QuestonBox li").attr("maxVoteNum");
    //         if (i > maxVoteNum) {
    //             content = "选择项大于最大限制数";
    //             ret = true;
    //         }
    //         if (ret) {
    //             problemAnswers = "";
    //             layerOpen(content)
    //             return false
    //             // $.DialogBySHF.Alert({
    //             //       Width: 350,
    //             //       Height: 200,
    //             //       Title: "警告",
    //             //       Content: content
    //             // });
    //         } else {
    //             //显示下一道题目
    //             var content = document.getElementById("question_" + num).innerHTML;
    //             main.innerHTML = content;
    //             num--;
    //         }
    //     }
    // }
}

//投票内容
function selectTouPiao() {
    vote_num = 0;
    problemAnswers = "";
    var userId = $("input[name=userId]").val();//用户ID
    var roomId = $("input[name=roomId]").val();//直播间ID
    if (userId == 0) {
        window.location.href = "http://" + h5Base + "/pc/login.html?roomId=" + roomId;
    }
    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/vote/getVote.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            roomId: roomId,
            userId: userId
        },
        success: function (data) {
            console.log(data);
            //401 手机登录
            //402     绑定手机号
            //403    微信打开
            if (data.code == 401) {
                window.location.href = "http://" + h5Base + "/pc/login.html?roomId=" + roomId;
            } else if (data.code == 402) {
                saveMessagerPhoneNumber();
            } else if (data.code == 403) {

            } else if (data.code == 1) {
                var vote = data.data;
                voteId = vote.id
                isOpen = vote.isOpen;
                if (data.voteStatus == 0) {
                    //进行
                    var HTML = "	<div class=\"voteMain\">";
                    HTML += "		<div id=\"QuestonBox\">";
                    var problemList = vote.list;
                    vote_num = problemList.length - 1
                    problemAnswers = "";
                    for (var i = 0; i < vote_num + 1; i++) {
                        var problem = problemList[i]
                        var maxVoteNum = problem.maxVoteNum

                        HTML += "<div class=\"voteList\" >";
                        HTML += "   <div class=\"voteQuestion\">Q" + [i + 1] + ": " + problem.problemName + "(多选题 ， 最大" + maxVoteNum + "项)</div>";
                        HTML += "   <div class=\"MainUl\"  data-maxVoteNum = " + maxVoteNum + " data-question=" + i + " >";
                        var listOption = problem.listOption;
                        for (var j = 0; j < listOption.length; j++) {
                            var answerOption = listOption[j];
                            HTML += "   <div class='checkbox'>";
                            HTML += "       <label>";
                            HTML += "       <input  onclick='checklg(this)' type=\"checkbox\" problem_Answer=\"" + problem.id + "_" + answerOption.id + "\"  name=\"problem_" + problem.id + "\" class=\"agreement-checkbox choose-way\">";
                            HTML += "       <div class='choseText'>";
                            HTML += "       <span class='Text'>" + answerOption.content + "</span>";
                            if (problem.style == 2) {
                                HTML += "       <span class='Img'><img class=\"wayImg\" src=\"" + answerOption.contentImg + "\" alt=\"\"/></span>";
                            }
                            HTML += "       </div>";
                            HTML += "       </label>";
                            HTML += "</div>";
                        }

                        HTML += "					</div>";
                        HTML += "				</div>";
                    }
                    HTML += "</div></div>";
                    HTML += '	<div class="modal-footer">';
                    HTML += '		<button type="button" class="btn tyBtn" onclick="next()">提交</button>';
                    HTML += '	</div>';
                    $("#actionContent").html(HTML);
                    $(".voteMain").slimScroll({
                        height: '350px'
                    });
                } else if (data.voteStatus == 1) {
                    //未开始
                    var startTime = data.data.startTime;
                    endVode(startTime);
                } else if (data.voteStatus == 2) {
                    //已结束
                    endVode();
                } else if (data.voteStatus == 3) {
                    //已投票
                    //participationVode();
                    successVode('你已参与过投票', '感谢您的参与')
                }
            } else {
                $.DialogBySHF.Alert({
                    Width: 350,
                    Height: 200,
                    Title: "警告",
                    Content: data.message
                });
            }
        }
    });
}

// 投票结束
function endVode(startTime) {
    var HTML = "<div class=\"voteMain\">";
    HTML += "	<div class=\"NostartPic\">";
    if (startTime) {
        HTML += "	<div class=\"voteText\"> <span>投票将于<time>" + startTime + "</time>开始</span></div>";
    } else {
        HTML += "	<div class=\"voteText\">投票活动已结束</div>";
    }
    HTML += '	</div>';
    HTML += '	<div class="modal-footer">';
    HTML += '		<button type="button" class="btn tyBtn" data-dismiss="modal">关闭</button>';
    HTML += '	</div>';
    HTML += '</div>';
    $("#actionContent").html(HTML);
}

//投票未开始
// function noStartVode(startTime){
// 	var HTML  = "<div class=\"voteNostart\">";
// 		HTML += "	<div class=\"voteNostartPic\"></div>";
//
// 		HTML += "</div>";
// 		$("#actionContent").html(HTML);
// }
//投票完成
function successVode(msg1, msg2) {
    var HTML = "<div class=\"voteMain\">";
    HTML += "	<div class=\"endPic\">";
    HTML += "		<div class=\"voteText\"><span>" + msg1 + "</span><span>" + msg2 + "</span></div>";
    HTML += "	</div>";
    HTML += '	<div class="modal-footer">';
    //是否对用户公开结果    0不公开   1公开
    if (isOpen == 1) {
        HTML += '		<button type="button" class="btn tyBtn" onclick="vodeContent()">投票结果</button>';
    } else {
        HTML += '		<button type="button" class="btn tyBtn" data-dismiss="modal">关闭</button>';
    }
    HTML += "</div>";
    HTML += '</div>';
    $("#actionContent").html(HTML);
}

// //已完成
// function participationVode(){
// 	var HTML  = "<div class=\"voteEnd\">";
// 		HTML += "	<div class=\"voteendPic\"></div>";
// 		HTML += "	<div class=\"voteText\">你已参与过投票</div>";
// 		HTML += "	<div class=\"voteText\">感谢您的参与</div>";
// 		//是否对用户公开结果    0不公开   1公开
// 		if(isOpen == 1){
// 			HTML += "	<div class=\"voteendButton\" onclick=\"vodeContent()\">投&nbsp;票&nbsp;结&nbsp;果</div>";
// 		}else{
// 			HTML += "	<div class=\"voteendButton\" onclick=\"closeVode()\">关&nbsp;闭</div>";
// 		}
// 		HTML += "</div>";
// 		$("#actionContent").html(HTML);
// }

//投票结果
function vodeContent() {
    if (voteId == 0) {
        $.DialogBySHF.Alert({
            Width: 350,
            Height: 200,
            Title: "提示",
            Content: "投票活动标示未获得"
        });
    } else {
        $.ajax({
            type: "GET",//请求方式 get/post
            url: "http://" + tomcatBase + "/ilive/vote/getResult.jspx",
            dataType: "jsonp",
            jsonp: "callback",
            cache: false,
            data: {
                voteId: voteId
            },
            success: function (data) {
                console.log(data);
                if (data.code == 1) {
                    console.log("投票结果");
                    var vote = data.data;
                    var problemList = vote.list;
                    var HTML = "<div class=\"voteMain\">";
                    HTML += "		<div id=\"QuestonBox\">";
                    for (var i = 0; i < problemList.length; i++) {
                        var problem = problemList[i];
                        HTML += "	<div class=\"voteList\">";
                        HTML += "   <div class=\"voteQuestion\">Q" + [i + 1] + ": " + problem.problemName + "</div>";
                        var listOption = problem.listOption;
                        for (var j = 0; j < listOption.length; j++) {
                            var option = listOption[j]
                            HTML += "			<div class=\"voteListCon\">";
                            HTML += "				<div class=\"voteListName\">" + option.content + "</div>";
                            HTML += "				<div class='bar'>";
                            HTML += "				    <div class=\"progress\">";
                            HTML += "					    <div class=\"progress-bar\" role=\"progressbar\" aria-valuenow=\" "+ option.percen +" \" aria-valuemin=\"0\" aria-valuemax=\""+ option.percen +"\" style=\"width:" + option.percen + "%;\"></div>";
                            HTML += "				    </div>";
                            HTML += "				    <div class=\"progressNum\">" + option.percen + "%</div>";
                            HTML += "				</div>";
                            HTML += "			</div>";
                        }
                        HTML += "	</div>";
                    }
                    HTML += "	</div>";
                    HTML += "</div>";
                    $("#actionContent").html(HTML);
                    $(".voteMain").slimScroll({
                        height: '350px'
                    });
                } else {
                    $.DialogBySHF.Alert({
                        Width: 350,
                        Height: 200,
                        Title: "警告",
                        Content: data.message
                    });
                }
            }
        });
    }
}
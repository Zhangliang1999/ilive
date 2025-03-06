var vote_num = 0;
var show_num = 0;
var problemAnswers ="";
var voteId = 0;//投票活动ID
var isOpen = 0;//是否对用户公开结果    0不公开   1公开

//执行答题结束操作
$("body").delegate(".voteconButton","click",function(){
    next();
})


function next() {
    if (voteId == 0) {
        layerOpen('投票活动标示未获得')
        return false
        // $.DialogBySHF.Alert({
        //       Width: 350,
        //       Height: 200,
        //       Title: "提示",
        //       Content: "投票活动标示未获得"
        // });
    } else {
        //获取展示内容元素对象
        var main = document.getElementById("QuestonBox");
        //判断是否为最后一题
        if (vote_num == 0) {
            var ret = true;
            var content = "你没有选择答案，无法继续下一题";
            var i = 0;
            var contentAn = problemAnswers;
            $("#QuestonBox li").each(function (index, item) {
                if ($(item).find("input").prop("checked") == true) {
                    i = i + 1;
                    var problem_Answer = $(item).find("input").attr("problem_Answer");
                    problemAnswers = problem_Answer + "," + problemAnswers;
                    ret = false;
                }
            });
            var maxVoteNum = $("#QuestonBox li").attr("maxVoteNum");
            if (i > maxVoteNum) {
                content = "选择项大于最大限制数";
                ret = true;
            }
            if (ret) {
                problemAnswers = contentAn;
                layerOpen(content)
                return false
                // $.DialogBySHF.Alert({
                //       Width: 350,
                //       Height: 200,
                //       Title: "警告",
                //       Content: content
                // });
            } else {
                console.log(problemAnswers);
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
                            successVode();
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
        } else {
            var ret = true;
            var content = "你没有选择答案，无法继续下一题";
            var i = 0;
            $("#QuestonBox li").each(function (index, item) {
                if ($(item).find("input").prop("checked") == true) {
                    i = i + 1;
                    var problem_Answer = $(item).find("input").attr("problem_Answer");
                    problemAnswers = problemAnswers + problem_Answer + ",";
                    ret = false;
                }
            })
            var maxVoteNum = $("#QuestonBox li").attr("maxVoteNum");
            if (i > maxVoteNum) {
                content = "选择项大于最大限制数";
                ret = true;
            }
            if (ret) {
                problemAnswers = "";
                layerOpen(content)
                return false
                // $.DialogBySHF.Alert({
                //       Width: 350,
                //       Height: 200,
                //       Title: "警告",
                //       Content: content
                // });
            } else {
                //显示下一道题目
                vote_num--;
                show_num++
                var content = document.getElementById("question_" + show_num).innerHTML;
                main.innerHTML = content;

            }
        }
    }
}
//关闭
function closeVode(){
    layer.closeAll()
}
//投票内容
function selectTouPiao() {

    var userId = $("input[name=userId]").val();//用户ID
    var roomId = $("input[name=roomId]").val();//直播间ID
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
                setlogin()
                //window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
            } else if (data.code == 402) {
                layer.closeAll()
                setlogin('bindPhone')
            } else if (data.code == 403) {

            } else if (data.code == 1) {
                var vote = data.data;
                voteId = vote.id
                isOpen = vote.isOpen;
                if (data.voteStatus == 0) {
                    //进行
                    var HTML = "<div class=\"voteResult mescroll\">";
                    HTML += "	<div class=\"voteMain\">";
                    var problemList = vote.list;
                    vote_num = problemList.length - 1
                    problemAnswers = "";
                    for (var i = 0; i < vote_num + 1; i++) {
                        var problem = problemList[i]
                        var maxVoteNum = problem.maxVoteNum
                        if (i == 0) {
                            HTML += "		<div id=\"QuestonBox\">";
                            HTML += "			<div class=\"voteList\">";
                            HTML += "				<div class=\"voteQuestion\">" + problem.problemName + "(多选题 ， 最大" + maxVoteNum + "项)</div>";
                            HTML += "				<div class=\"voteListCon\">";
                            HTML += "					<ul class=\"MainUl\">";
                            var listOption = problem.listOption;
                            for (var j = 0; j < listOption.length; j++) {
                                var answerOption = listOption[j];
                                HTML += "						<li maxVoteNum = \"" + maxVoteNum + "\">";
                                HTML += "							<label>";
                                HTML += "								<div class=\"way-choose\">";
                                HTML += "									<input type=\"checkbox\" problem_Answer=\"" + problem.id + "_" + answerOption.id + "\"  name=\"problem_" + problem.id + "\" class=\"agreement-checkbox choose-way\">";
                                HTML += "								</div>";
                                HTML += "<div class='content'>" + answerOption.content + "</div>";
                                if (problem.style == 2) {
                                    HTML += "							<img class=\"wayImg\" src=\"" + answerOption.contentImg + "\" alt=\"\"/>";
                                }
                                HTML += "							</label>";
                                HTML += "						</li>";
                            }

                            HTML += "					</ul>";
                            HTML += "				</div>";
                            HTML += "			</div>";
                            HTML += "		</div>";
                        } else {
                            HTML += "		<div id=\"question_" + i + "\" style=\"display: none;\" class=\"voteList\">";
                            HTML += "			<div class=\"voteQuestion\">" + problem.problemName + "(多选题 ， 最大" + maxVoteNum + "项)</div>";
                            HTML += "			<div class=\"voteListCon\">";
                            HTML += "				<ul class=\"MainUl\">";
                            var listOption = problem.listOption;
                            for (var j = 0; j < listOption.length; j++) {
                                var answerOption = listOption[j];
                                HTML += "					<li maxVoteNum = \"" + maxVoteNum + "\">";
                                HTML += "						<label>";
                                HTML += "							<div class=\"way-choose\">";
                                HTML += "								<input type=\"checkbox\" problem_Answer=\"" + problem.id + "_" + answerOption.id + "\" name=\"problem_" + problem.id + "\" class=\"agreement-checkbox choose-way\">";
                                HTML += "							</div>";
                                HTML += "<div class='content'>" + answerOption.content + "</div>";
                                if (problem.style == 2) {
                                    HTML += "							<img class=\"wayImg\" src=\"" + answerOption.contentImg + "\" alt=\"\"/>";
                                }
                                HTML += "						</label>";
                                HTML += "					</li>";
                            }
                            HTML += "				</ul>";
                            HTML += "			</div>";
                            HTML += "		</div>";
                        }
                    }
                    HTML += "	</div>";
                   
                    HTML += "</div>";
                    HTML += "	<div class=\"voteconButton\">继&nbsp;续</div>";
                    $("#vodeContentDiv").html(HTML);
                } else if (data.voteStatus == 1) {
                    //未开始
                    var startTime = data.data.startTime;
                    noStartVode(startTime);
                } else if (data.voteStatus == 2) {
                    //已结束
                    endVode();
                } else if (data.voteStatus == 3) {
                    //已投票
                    participationVode();
                }
            } else {
                layerOpen(data.message)
                return false
            }
        }
    });
}

// 投票结束
function endVode() {
    var HTML = "<div class=\"voteNostart\">";
    HTML += "	<div class=\"voteNostartPic\"></div>";
    HTML += "	<div class=\"voteText\">投票活动已结束</div>";
    HTML += "</div>";
    $("#vodeContentDiv").html(HTML);
}

//投票未开始
function noStartVode(startTime) {
    var HTML = "<div class=\"voteNostart\">";
    HTML += "	<div class=\"voteNostartPic\"></div>";
    HTML += "	<div class=\"voteText\">投票将于" + startTime + "开始</div>";
    HTML += "</div>";
    $("#vodeContentDiv").html(HTML);
}

//投票完成
function successVode() {
    var HTML = "<div class=\"voteEnd\">";
    HTML += "	<div class=\"voteendPic\"></div>";
    HTML += "	<div class=\"voteText\">投票提交已完成</div>";
    HTML += "	<div class=\"voteText\">感谢您的参与</div>";
    //是否对用户公开结果    0不公开   1公开
    if (isOpen == 1) {
        HTML += "	<div class=\"voteendButton\" onclick=\"vodeContent()\">投&nbsp;票&nbsp;结&nbsp;果</div>";
    } else {
        HTML += "	<div class=\"voteendButton\" onclick=\"closeVode()\">关&nbsp;闭</div>";
    }
    HTML += "</div>";
    $("#vodeContentDiv").html(HTML);
}

//已完成
function participationVode() {
    var HTML = "<div class=\"voteEnd\">";
    HTML += "	<div class=\"voteendPic\"></div>";
    HTML += "	<div class=\"voteText\">你已参与过投票</div>";
    HTML += "	<div class=\"voteText\">感谢您的参与</div>";
    //是否对用户公开结果    0不公开   1公开
    if (isOpen == 1) {
        HTML += "	<div class=\"voteendButton\" onclick=\"vodeContent()\">投&nbsp;票&nbsp;结&nbsp;果</div>";
    } else {
        HTML += "	<div class=\"voteendButton\" onclick=\"closeVode()\">关&nbsp;闭</div>";
    }
    HTML += "</div>";
    $("#vodeContentDiv").html(HTML);
}


//投票结果
function vodeContent() {
    if (voteId == 0) {
        layerOpen('投票活动标示未获得')
        return false
        // $.DialogBySHF.Alert({
        //       Width: 350,
        //       Height: 200,
        //       Title: "提示",
        //       Content: ""
        // });
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
                    var HTML = "<div class=\"voteResult mescroll\">";
                    HTML += "	<div class=\"voteMain\">";
                    for (var i = 0; i < problemList.length; i++) {
                        var problem = problemList[i];
                        HTML += "	<div class=\"voteList\">";
                        HTML += "		<div class=\"voteQuestion\">Q"+(i+1)+": " + problem.problemName + "</div>";
                        var listOption = problem.listOption;
                        for (var j = 0; j < listOption.length; j++) {
                            var option = listOption[j]
                            HTML += "			<div class=\"voteListCon\">";
                            HTML += "				<div class=\"voteListName\">" + option.content + "</div>";
                            HTML += "				<div class=\"Bar\">";
                            HTML += "					<div class=\"progress\" style=\"width:" + option.percen + "%;\"></div>";
                            HTML += "				</div>";
                            HTML += "				<div class=\"progressNum\">" + option.percen + "%</div>";
                            HTML += "			</div>";
                        }
                        HTML += "	</div>";
                    }
                    HTML += "</div>";
                    HTML += "</div>";
                    $("#vodeContentDiv").html(HTML);
                } else {
                    layerOpen(data.message)
                    return false
                    // $.DialogBySHF.Alert({
                    //       Width: 350,
                    //       Height: 200,
                    //       Title: "警告",
                    //       Content: data.message
                    //  });
                }
            }
        });
    }
}
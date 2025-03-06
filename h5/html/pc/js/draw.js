/**
 * Created by Deng on 2018/6/8.
 */
//抽奖活动未开启
var indexs = 0;
var id = 0;
var click = false;
var disable;
//抽奖进行
function inPrize() {
    var userId = $("input[name=userId]").val();//用户ID
    var roomId = $("input[name=roomId]").val();//直播间ID
    if(userId==0){
        window.location.href="http://"+h5Base+"/pc/login.html?roomId="+roomId;
    }
    disable = false;
    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/prize/getPrize.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            terminalType: "h5",
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
                return false
                // if(confirm("是否进入登录？")){
                // 	window.location.href="http://"+h5Base+"/phone/login.html?roomId="+roomId;
                // }else{
                // 	layerOpen('不登录无法进行抽奖')
                // 	return false
                // 	// $.DialogBySHF.Alert({
                // 	//       Width: 350,
                // 	//       Height: 200,
                // 	//       Title: "警告",
                // 	//       Content: "不登录无法进行抽奖"
                // 	//  });
                // }
            } else if (data.code == 1) {
                var prize = data.data;
                if (prize == undefined || prize == null || prize == "") {
                    errorPrize('获取抽奖活动失败')
                    // $.DialogBySHF.Alert({
                    //       Width: 350,
                    //       Height: 200,
                    //       Title: "警告",
                    //       Content: "获取抽奖活动失败"
                    //  });
                } else {
                    var prizeStatus = data.prizeStatus;
                    if (prizeStatus == 2) {
                        //closePrize();
                        errorPrize('抱歉！来晚了！抽奖已经结束...');
                    } else if (prizeStatus == 1) {
                        //抽奖未开始
                        var startTime = prize.startTime;
                        openPrize(startTime);
                    } else if (prizeStatus == 0) {
                        var HTML = "";
                        var lotteryType = prize.lotteryType;
                        var userNum = prize.userNum;
                        if (userNum == "" || userNum == undefined) {
                            userNum = 0;
                        }
                        var nodrawText = ['砸金蛋', '摇一摇', '九宫格抽奖']
                        HTML += '<div class="drawList">';
                        HTML += '	<div class="nodrawText">' + nodrawText[lotteryType - 1] + '</div>';
                        HTML += "<div class=\"drawMain\">";
                        var list = prize.list;
                        var prizeLength = list.length;
                        if (prizeLength < 8) {
                            errorPrize('没有奖品')
                        } else {
                            switch (lotteryType) {
                                case 1:
                                    //砸金蛋
                                    HTML += "	<div class=\"egg\" id=\"egg\">";
                                    HTML += "	    <div class=\"hammer\" id=\"hammer\"><img src=\"draw/images/chuizi.png\" class=\"imgChuiZi\"></div>";
                                    HTML += "		<div id=\"jindan\" data-id=\"egg\" onclick='drawBtn(this)'></div>";
                                    HTML += "	</div>";
                                    break
                                case 2:
                                    //摇一摇
                                    HTML += "	<div class=\"red_bg\">";
                                    HTML += "		<div class=\"red-ss-bg\" data-id=\"shake\" onclick='drawBtn(this)'>";
                                    HTML += "			<div class=\"red-ss\" id='red-ss'></div>";
                                    HTML += "		</div>";
                                    HTML += "	</div>";
                                    break
                                case 3: //九宫格抽奖
                                    indexs = 0;
                                    //抽奖进行中

                                    HTML += "	<div class=\"draw\" id=\"lottery\">";
                                    HTML += "		<table>";
                                    HTML += "			<tr>";
                                    if (list[0].id != 0) {
                                        HTML += "				<td id=\"prizeId_" + list[0].id + "\" class=\"item lottery-unit lottery-unit-0\" prizeId=\"0\">";
                                        HTML += "					<div class=\"img\">";
                                        HTML += "						<img src=\"" + list[0].prizeImg + "\" >";
                                        HTML += "					</div>";
                                    } else {
                                        HTML += "				<td id=\"prizeId_" + indexs + "\" class=\"item lottery-unit lottery-unit-0\" prizeId=\"0\">";
                                        indexs = indexs - 1;
                                    }
                                    HTML += "					<span class=\"name\">" + list[0].prizeName + "</span>";
                                    HTML += "				</td>";
                                    HTML += "				<td class=\"gap\"></td>";
                                    if (list[1].id != 0) {
                                        HTML += "				<td id=\"prizeId_" + list[1].id + "\" class=\"item lottery-unit lottery-unit-1\" prizeId=\"1\">";
                                        HTML += "					<div class=\"img\">";
                                        HTML += "						<img src=\"" + list[1].prizeImg + "\" >";
                                        HTML += "					</div>";
                                    } else {
                                        HTML += "				<td id=\"prizeId_" + indexs + "\" class=\"item lottery-unit lottery-unit-1\" prizeId=\"1\">";
                                        indexs = indexs - 1;
                                    }
                                    HTML += "					<span class=\"name\">" + list[1].prizeName + "</span>";
                                    HTML += "				</td>";
                                    HTML += "				<td class=\"gap\"></td>";
                                    if (list[2].id != 0) {
                                        HTML += "				<td id=\"prizeId_" + list[2].id + "\" class=\"item lottery-unit lottery-unit-2\"  prizeId=\"2\">";
                                        HTML += "					<div class=\"img\">";
                                        HTML += "						<img src=\"" + list[2].prizeImg + "\" alt=\"\">";
                                        HTML += "					</div>";
                                    } else {
                                        HTML += "				<td id=\"prizeId_" + indexs + "\" class=\"item lottery-unit lottery-unit-2\"  prizeId=\"2\">";
                                        indexs = indexs - 1;
                                    }

                                    HTML += "					<span class=\"name\">" + list[2].prizeName + "</span>";
                                    HTML += "				</td>";
                                    HTML += "			</tr>";
                                    HTML += "			<tr>";
                                    HTML += "				<td class=\"gap-2\" colspan=\"5\"></td>";
                                    HTML += "			</tr>";
                                    HTML += "			<tr>";
                                    if (list[7].id != 0) {
                                        HTML += "				<td id=\"prizeId_" + list[7].id + "\" class=\"item lottery-unit lottery-unit-7\" prizeId=\"7\">";
                                        HTML += "					<div class=\"img\">";
                                        HTML += "						<img src=\"" + list[7].prizeImg + "\" alt=\"\">";
                                        HTML += "					</div>";
                                    } else {
                                        HTML += "				<td id=\"prizeId_" + indexs + "\" class=\"item lottery-unit lottery-unit-7\" prizeId=\"7\">";
                                        indexs = indexs - 1;
                                    }

                                    HTML += "					<span class=\"name\">" + list[7].prizeName + "</span>";
                                    HTML += "				</td>";
                                    HTML += "				<td class=\"gap\"></td>";
                                    HTML += "				<td class=\"draw-btn\" data-id=\"lottery\" onclick='drawBtn(this)'>GO</td>";
                                    HTML += "				<td class=\"gap\"></td>";
                                    if (list[3].id != 0) {
                                        HTML += "				<td id=\"prizeId_" + list[3].id + "\" class=\"item lottery-unit lottery-unit-3\"  prizeId=\"3\">";
                                        HTML += "					<div class=\"img\">";
                                        HTML += "						<img src=\"" + list[3].prizeImg + "\" alt=\"\">";
                                        HTML += "					</div>";
                                    } else {
                                        HTML += "				<td id=\"prizeId_" + indexs + "\" class=\"item lottery-unit lottery-unit-3\"  prizeId=\"3\">";
                                        indexs = indexs - 1;
                                    }

                                    HTML += "					<span class=\"name\">" + list[3].prizeName + "</span>";
                                    HTML += "				</td>";
                                    HTML += "			</tr>";
                                    HTML += "			<tr>";
                                    HTML += "				<td class=\"gap-2\" colspan=\"5\"></td>";
                                    HTML += "			</tr>";
                                    HTML += "			<tr>";
                                    if (list[6].id != 0) {
                                        HTML += "				<td id=\"prizeId_" + list[6].id + "\" class=\"item lottery-unit lottery-unit-6\"  prizeId=\"6\">";
                                        HTML += "					<div class=\"img\">";
                                        HTML += "						<img src=\"" + list[6].prizeImg + "\" alt=\"\">";
                                        HTML += "					</div>";
                                    } else {
                                        HTML += "				<td id=\"prizeId_" + indexs + "\" class=\"item lottery-unit lottery-unit-6\"  prizeId=\"6\">";
                                        indexs = indexs - 1;
                                    }

                                    HTML += "					<span class=\"name\">" + list[6].prizeName + "</span>";
                                    HTML += "				</td>";
                                    HTML += "				<td class=\"gap\"></td>";
                                    if (list[5].id != 0) {
                                        HTML += "				<td id=\"prizeId_" + list[5].id + "\" class=\"item lottery-unit lottery-unit-5\"  prizeId=\"5\">";
                                        HTML += "					<div class=\"img\">";
                                        HTML += "						<img src=\"" + list[5].prizeImg + "\" alt=\"\">";
                                        HTML += "					</div>";
                                    } else {
                                        HTML += "				<td id=\"prizeId_" + indexs + "\" class=\"item lottery-unit lottery-unit-5\"  prizeId=\"5\">";
                                        indexs = indexs - 1;
                                    }

                                    HTML += "					<span class=\"name\">" + list[5].prizeName + "</span>";
                                    HTML += "				</td>";
                                    HTML += "				<td class=\"gap\"></td>";
                                    if (list[4].id != 0) {
                                        HTML += "				<td id=\"prizeId_" + list[4].id + "\" class=\"item lottery-unit lottery-unit-4\"  prizeId=\"4\">";
                                        HTML += "					<div class=\"img\">";
                                        HTML += "						<img src=\"" + list[4].prizeImg + "\" alt=\"\">";
                                        HTML += "					</div>";
                                    } else {
                                        HTML += "				<td id=\"prizeId_" + indexs + "\" class=\"item lottery-unit lottery-unit-4\"  prizeId=\"4\">";
                                        indexs = indexs - 1;
                                    }

                                    HTML += "					<span class=\"name\">" + list[4].prizeName + "</span>";
                                    HTML += "				</td>";
                                    HTML += "			</tr>";
                                    HTML += "		</table>";
                                    HTML += "	</div>";


                                    setTimeout(function () {
                                        lottery.init('lottery');
                                    }, 0)
                                    break
                            }
                        }
                        HTML += "</div>";
                        HTML += "<div class=\"guize\">剩余<span id=\"choujiangNumber\">" + userNum + "</span>次机会 &nbsp;<a href='javascript:;' onclick=\"prizeList(" + prize.id + ")\" >查看奖品</a></div>";
                        HTML += "</div>";


                        $("#actionContent").html(HTML);
                    }
                }
            } else {
                errorPrize(data.message)
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

function openPrize(startTime) {
    //未开启
    var HTML = '<div class="drawList">';
    HTML += '	<div class="drawnostartImg"><div class="endText">抽奖还未开始</div>';
    HTML += '	<div class="startText">将于 <span>' + startTime + '</span> 开启</div></div>';
    HTML += '<div class="modal-footer"><button type="button" class="btn tyBtn" data-dismiss="modal">确定</button></div>';
    HTML += '</div></div></div>'
    $("#actionContent").html(HTML);
}

//抽奖活动已结束
// function closePrize() {
//     var HTML = '<div class="drawList">';
//     HTML += '	<div class="drawEndImg"></div>';
//     HTML += '	<div class="endText"></div>';
//     HTML += '</div>' +
//         '<div class="modal-footer"><button type="button" class="btn tyBtn" data-dismiss="modal">确定</button></div>';
//     $("#actionContent").html(HTML);
// }

//错误信息
function errorPrize(msg, clickBtn) {
    var HTML = '<div class="drawList">';
    HTML += '	<div class="drawEndImg"><div class="endText">' + msg + '</div></div>';

    HTML += '<div class="modal-footer">';
    if (clickBtn) {
        HTML += '<button type="button" class="btn tyBtn" onclick="inPrize()">确定</button>';
    } else {
        HTML += '<button type="button" class="btn tyBtn" data-dismiss="modal">确定</button>';
    }
    HTML += '</div></div>'
    $("#actionContent").html(HTML);
}

//未中奖
// function notWinningPrize() {
//
//     var HTML = '<div class="drawList">';
//     HTML += '	<div class="drawEndImg"></div>';
//     HTML += '	<div class="endText">大奖擦肩而过...</div>';
//     HTML += '</div>' +
//         '<div class="modal-footer"><button type="button" class="btn tyBtn" onclick="inPrize()">确定</button></div>';
//     $("#actionContent").html(HTML);
// }

//获得奖品
function winningPrize(id) {
    $(".drawMask").attr("drawMask", "0");
    if (id == 0) {
        //notWinningPrize();
        errorPrize('大奖擦肩而过...', true)
    } else {
        $.ajax({
            type: "GET",//请求方式 get/post
            url: "http://" + tomcatBase + "/ilive/prize/getLotteryPrize.jspx",
            dataType: "jsonp",
            jsonp: "callback",
            cache: false,
            data: {
                terminalType: "h5",
                id: id
            },
            success: function (data) {
                console.log(data);
                if (data.code == 1) {
                    var prize = data.data;
                    var HTML = "<div class=\"drawList\">";
                    HTML += "	<div class=\"nodrawText\">恭喜您，中奖啦！<span>工作人员稍后会和您联系</span></div>";
                    HTML += "	<div class=\"drawImg\"><img src=\"" + prize.prizeImg + "\" alt=\"\"/>";
                    HTML += "	<div class=\"startText\">"+ prize.prizeName +"</div></div>";
                    HTML += '<div class="modal-footer">';
                    HTML += '<button type="button" class="btn tyBtn" onclick="inPrize()">确定</button></div>';
                    HTML += '</div>'
                    $("#actionContent").html(HTML);
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


//奖品列表
function prizeList(lotteryId) {
    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/prize/getPrizeList.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            terminalType: "h5",
            lotteryId: lotteryId
        },
        success: function (data) {
            console.log(data);
            if (data.code == 1) {
                var list = data.data;
                var HTML = "<div class=\"drawList\">";
                HTML += "		<div class=\"nodrawText\">本期奖品</div>";
                HTML += "		<div class=\"drawMain\">";
                HTML += "		<div class=\"drawListAll\">";
                for (var i = 0; i < list.length; i++) {
                    HTML += "		<div class=\"media\">";
                    HTML += "			<div class=\"media-left\"><img class='media-object' src=\"" + list[i].prizeImg + "\" alt=\"\"/></div>";
                    HTML += "			<div class=\"media-body\">";
                    HTML += "				<div class=\"media-heading\"><p>" + list[i].prizeName + "</p><p class='prizeNum'>x" + list[i].num + "</p></div>";
                    HTML += "			</div>";
                    HTML += "		</div>";
                }
                HTML += "</div>";
                HTML += "<div class=\"modal-footer\"><button type=\"button\" onclick=\"inPrize()\" class=\"btn tyBtn\">一键参与</button></div>" +
                    "</div>";
                $("#actionContent").html(HTML);
                $(".drawListAll").slimScroll({
                    height: '350px'
                }).css('padding-bottom', '10px');
            } else {
                errorPrize(data.message)
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

var lottery = {
    index: -1,    //当前转动到哪个位置，起点位置
    count: 0,     //总共有多少个位置
    timer: 0,     //setTimeout的ID，用clearTimeout清除
    speed: 20,    //初始转动速度
    times: 0,     //转动次数
    cycle: 50,    //转动基本次数：即至少需要转动多少次再进入抽奖环节
    prize: -1,    //中奖位置
    init: function (id) {
        if ($('#' + id).find('.lottery-unit').length > 0) {
            $lottery = $('#' + id);
            $units = $lottery.find('.lottery-unit');
            this.obj = $lottery;
            this.count = $units.length;
            $lottery.find('.lottery-unit.lottery-unit-' + this.index).addClass('drawactive');
        }
        ;
    },
    roll: function () {
        var index = this.index;
        var count = this.count;
        var lottery = this.obj;
        $(lottery).find('.lottery-unit.lottery-unit-' + index).removeClass('drawactive');
        index += 1;
        if (index > count - 1) {
            index = 0;
        }
        ;
        $(lottery).find('.lottery-unit.lottery-unit-' + index).addClass('drawactive');
        this.index = index;
        return false;
    },
    stop: function (index) {
        this.prize = index;
        return false;
    }
};

function roll() {
    lottery.times += 1;
    lottery.roll(); //转动过程调用的是lottery的roll方法，这里是第一次调用初始化

    if (lottery.times > lottery.cycle + 10 && lottery.prize == lottery.index) {
        clearTimeout(lottery.timer);
        lottery.prize = -1;
        lottery.times = 0;
    } else {
        if (lottery.times < lottery.cycle) {
            lottery.speed -= 10;
        } else if (lottery.times == lottery.cycle) {
            var index = Math.random() * (lottery.count) | 0; //静态演示，随机产生一个奖品序号，实际需请求接口产生
            //奖品位置
            var userId = $("input[name=userId]").val();//用户ID
            var roomId = $("input[name=roomId]").val();//直播间ID
            $.ajax({
                type: "GET",//请求方式 get/post
                url: "http://" + tomcatBase + "/ilive/prize/userLottery.jspx",
                dataType: "jsonp",
                jsonp: "callback",
                cache: false,
                data: {
                    terminalType: "h5",
                    userId: userId,
                    roomId: roomId
                },
                success: function (data) {
                    console.log(data);
                    if (data.code == 1) {
                        id = data.data.prize;
                        var prizeId = $("#prizeId_" + id).attr("prizeId");
                        lottery.prize = prizeId;
                    } else {
                        console.log(data.message);
                        id = 0;
                    }
                }
            });
        } else {
            if (lottery.times > lottery.cycle + 10 && ((lottery.prize == 0 && lottery.index == 7) || lottery.prize == lottery.index + 1)) {
                lottery.speed += 110;

                //停止转动
                setTimeout(function () {
                    winningPrize(id);
                }, 1000);
            } else {
                lottery.speed += 20;
            }
        }
        if (lottery.speed < 40) {
            lottery.speed = 40;
        }
        ;
        lottery.timer = setTimeout(roll, lottery.speed); //循环调用
    }
    return false;
}

function drawBtn(obj) {
    //判断抽奖次数
    console.log($(obj))
    var switchType = obj.getAttribute('data-id')

    var choujiangNumber = $("#choujiangNumber").text();
    if (parseInt(choujiangNumber) > parseInt(0)) {
        switch (switchType) {
            case 'lottery':
                StartLottery(obj)
                break;
            case 'egg':
                StartEgg(obj)
                break
            case 'shake':
                StartShake(obj)
        }
    } else {
        errorPrize('抽奖次数已使用完毕')
        // $.DialogBySHF.Alert({
        //       Width: 350,
        //       Height: 200,
        //       Title: "提示",
        //       Content: "抽奖次数已使用完毕"
        //  });
    }

}
function StartLottery() {
    if (disable) { //click控制一次抽奖过程中不能重复点击抽奖按钮，后面的点击不响应
        return;
    } else {
        lottery.speed = 100;
        roll(); //转圈过程不响应click事件，会将click置为false
        disable = true; //一次抽奖完成后，设置click为true，可继续抽奖
    }
}

function  StartEgg(obj){
    $('#hammer').addClass('active')
    if(disable){
        alert("蛋都碎了，请勿再敲.");
        return;
    }
    var _this = $(obj);
    //$("#hammer").stop(true);

    //clearInterval(dingshi);

    //$(".hammer").css({"top":"150px","left":"420px"});
    setTimeout(function(){
        _this.addClass("curr").parent(); //蛋碎效果
        disable = true;

    },750)
    setTimeout(function(){
        // _this.addClass("curr").parent().hide(); //蛋碎效果
        // _this.find("sup").show(); //金花四溅
        // $(".hammer").hide();//隐藏锤子


        //砸金蛋奖项处理
        var userId = $("input[name=userId]").val();//用户ID
        var roomId = $("input[name=roomId]").val();//直播间ID
        $.ajax({
            type : "GET",//请求方式 get/post
            url : "http://" + tomcatBase + "/ilive/prize/userLottery.jspx",
            dataType : "jsonp",
            jsonp : "callback",
            cache : false,
            data : {
                terminalType:"h5",
                userId : userId,
                roomId : roomId
            },
            success : function(data) {
                console.log(data);
                if(data.code==1){
                    var id = data.data.prize;
                    winningPrize(id);
                }else{
                    console.log(data.message);
                    errorPrize(data.message);
                }
            }
        });

    },1500)

}

function StartShake(obj) {
    if(disable){
        return;
    }
    $('#red-ss').addClass('active')
    disable = true;
    setTimeout(function(){
        var userId = $("input[name=userId]").val();//用户ID
        var roomId = $("input[name=roomId]").val();//直播间ID
        $.ajax({
            type : "GET",//请求方式 get/post
            url : "http://" + tomcatBase + "/ilive/prize/userLottery.jspx",
            dataType : "jsonp",
            jsonp : "callback",
            cache : false,
            data : {
                terminalType:"h5",
                userId : userId,
                roomId : roomId
            },
            success : function(data) {
                console.log(data);
                if(data.code==1){
                    var id = data.data.prize;
                    winningPrize(id);
                }else{
                    console.log(data.message);
                    errorPrize(data.message);
                }
            }
        });

    },1500)
}
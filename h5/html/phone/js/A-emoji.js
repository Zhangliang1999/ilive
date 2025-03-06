function A_Emoji($box,fn){
	var _box = $box;
	defaults = {
		emojiconfig: {
			qq: {
				name: "qq表情",
				path: "img/qq/",
				maxNum: 100,
				file: ".gif",
				placeholder: ":{alias}:",
				alias: {
					1: "微笑",
					2: "撇嘴",
					3: "色",
					4: "发呆",
					5: "得意",
					6: "流泪",
					7: "害羞",
					8: "闭嘴",
					9: "睡",
					10: "大哭",
					11: "尴尬",
					12: "发怒",
					13: "调皮",
					14: "龇牙",
					15: "惊讶",
					16: "难过",
					17: "酷",
					18: "冷汗",
					19: "抓狂",
					20: "吐",
					21: "偷笑",
					22: "可爱",
					23: "白眼",
					24: "傲慢",
					25: "饥饿",
					26: "困",
					27: "惊恐",
					28: "流汗",
					29: "憨笑",
					30: "大兵",
					31: "奋斗",
					32: "咒骂",
					33: "疑问",
					34: "嘘",
					35: "晕",
					36: "折磨",
					37: "衰",
					38: "骷髅",
					39: "敲打",
					40: "再见",
					41: "擦汗",
					42: "抠鼻",
					43: "鼓掌",
					44: "糗大了",
					45: "坏笑",
					46: "左哼哼",
					47: "右哼哼",
					48: "哈欠",
					49: "鄙视",
					50: "委屈",
					51: "快哭了",
					52: "阴险",
					53: "亲亲",
					54: "吓",
					55: "可怜",
					56: "菜刀",
					57: "西瓜",
					58: "啤酒",
					59: "篮球",
					60: "乒乓",
					61: "咖啡",
					62: "饭",
					63: "猪头",
					64: "玫瑰",
					65: "凋谢",
					66: "示爱",
					67: "爱心",
					68: "心碎",
					69: "蛋糕",
					70: "闪电",
					71: "炸弹",
					72: "刀",
					73: "足球",
					74: "瓢虫",
					75: "便便",
					76: "月亮",
					77: "太阳",
					78: "礼物",
					79: "拥抱",
					80: "强",
					81: "弱",
					82: "握手",
					83: "胜利",
					84: "抱拳",
					85: "勾引",
					86: "拳头",
					87: "差劲",
					88: "爱你",
					89: "NO",
					90: "OK",
					91: "爱情",
					92: "飞吻",
					93: "跳跳",
					94: "发抖",
					95: "怄火",
					96: "转圈",
					97: "磕头",
					98: "回头",
					99: "跳绳",
					100: "挥手"
				},
				title: {
					1: "微笑",
					2: "撇嘴",
					3: "色",
					4: "发呆",
					5: "得意",
					6: "流泪",
					7: "害羞",
					8: "闭嘴",
					9: "睡",
					10: "大哭",
					11: "尴尬",
					12: "发怒",
					13: "调皮",
					14: "龇牙",
					15: "惊讶",
					16: "难过",
					17: "酷",
					18: "冷汗",
					19: "抓狂",
					20: "吐",
					21: "偷笑",
					22: "可爱",
					23: "白眼",
					24: "傲慢",
					25: "饥饿",
					26: "困",
					27: "惊恐",
					28: "流汗",
					29: "憨笑",
					30: "大兵",
					31: "奋斗",
					32: "咒骂",
					33: "疑问",
					34: "嘘",
					35: "晕",
					36: "折磨",
					37: "衰",
					38: "骷髅",
					39: "敲打",
					40: "再见",
					41: "擦汗",
					42: "抠鼻",
					43: "鼓掌",
					44: "糗大了",
					45: "坏笑",
					46: "左哼哼",
					47: "右哼哼",
					48: "哈欠",
					49: "鄙视",
					50: "委屈",
					51: "快哭了",
					52: "阴险",
					53: "亲亲",
					54: "吓",
					55: "可怜",
					56: "菜刀",
					57: "西瓜",
					58: "啤酒",
					59: "篮球",
					60: "乒乓",
					61: "咖啡",
					62: "饭",
					63: "猪头",
					64: "玫瑰",
					65: "凋谢",
					66: "示爱",
					67: "爱心",
					68: "心碎",
					69: "蛋糕",
					70: "闪电",
					71: "炸弹",
					72: "刀",
					73: "足球",
					74: "瓢虫",
					75: "便便",
					76: "月亮",
					77: "太阳",
					78: "礼物",
					79: "拥抱",
					80: "强",
					81: "弱",
					82: "握手",
					83: "胜利",
					84: "抱拳",
					85: "勾引",
					86: "拳头",
					87: "差劲",
					88: "爱你",
					89: "NO",
					90: "OK",
					91: "爱情",
					92: "飞吻",
					93: "跳跳",
					94: "发抖",
					95: "怄火",
					96: "转圈",
					97: "磕头",
					98: "回头",
					99: "跳绳",
					100: "挥手"
				}
			}
			//, AcFun: {
			// 	name: "AcFun表情",
			// 	path: "img/AcFun/",
			// 	maxNum: 54,
			// 	file: ".png"
			// }
		},
//		postFunction: function() {
//			alert(InputText.html());
//			console.log(InputText.html());
//		}
};
	var _emojiBox = _box.find('.emojiBox');
	emojiconfig = defaults.emojiconfig;
	var InputText = _box.find('#saytext');
	var emojiBox = _box.find('.emojiBox');
	var imgBtn = _box.find('[data="emoji"]');
	var newArr = [];
	imgBtn.click(
		function() {

			//$(".talkCon #saytext").focus();
			$('.hiddenDiv').empty();
			var emojiContainer = _emojiBox.find('.emoji_container');
			if (emojiContainer.children().length <= 0) {
				_emojiBox.css({
					display: 'block'
				});
				for (var emojilist in emojiconfig) {
					emojiContainer.append('<section class="for_' + emojilist + '"></section>');
					for (var i = 1; i <= emojiconfig[emojilist].maxNum; i++) {
						var num = parseInt(99+i);
						var titNum = parseInt(i);
						if (emojiContainer.find('.for_' + emojilist) !== undefined) {
							emojiContainer.find('.for_' + emojilist).append('<a href="#!" class="_img"><img src="' + emojiconfig[emojilist].path + num + emojiconfig[emojilist].file + '" alt="" data-alias="'+(emojiconfig[emojilist].alias == undefined ? (i+emojiconfig[emojilist].file) : emojiconfig[emojilist].alias[i])+'" title="' + (emojiconfig[emojilist].title == undefined ? '' : emojiconfig[emojilist].title[titNum]) + '" /></a>');
						}
					}
				}
				_emojiBox.find('.emoji_container section')[0].style.display = 'block';
				_emojiBox.find('.emoji_container ._img').on('click', function() {

					if (InputText) {
							$(".hiddenDiv").append(this.innerHTML);
							setTimeout(function(){
							var imgHtml = $('.hiddenDiv').find("img");
							var tit = imgHtml.attr("title")
							console.log(tit)
							var valHtml = $("#saytext").val()+"["+tit+"]";
							console.log(valHtml);
							$("#saytext").val(valHtml);
							if($('.textarea').val().length!=0){
								$(".sendMessage").removeAttr("disabled")
									.css("background", "#0084ff");
					        }else{
					            $(".sendMessage").css("background","#b2daff");
					        }
						},100)
						_emojiBox.css({
							display: 'none'
						});

					} else {
						alert(2)
						InputText.append('[' + $(this).find('img').attr('data-alias')+']');
					}
				});
			} else {
				_emojiBox.toggle();
			}
		}
	);
	_box.find('[data="confirm"]').on('click', function(){
		addRow({detail:InputText.html()});
		if(fn){fn();}

		$(".emojiBox").css("display","none")
	});

	$(document).on('click', function(e) {
		if ((_emojiBox.find($(e.target)).length) <= 0 && (_box.find($(e.target)).length <= 0)) {
			_emojiBox.hide();
		}
	});

}




	function addRow(aa){
		arr = emojiconfig.qq.title;
	}


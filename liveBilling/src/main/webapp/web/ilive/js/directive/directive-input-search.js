myapp.directive("enterpriseInputSearch", function($timeout, enterpriseService) {
	return {
		restrict : 'A',
		require : 'ngModel',
		scope : {
			datavalue : "=",
			ngModel : '='
		},
		link : function(scope, element, attrs, ngModel) {
			$timeout(function() {
				// 关闭浏览器提供给输入框的自动完成
				element.attr('autocomplete', 'off');
				// 创建自动完成的下拉列表，用于显示服务器返回的数据,插入在搜索按钮的后面，等显示的时候再调整位置
				var $autocomplete = $('<div class="autocomplete"></div>').hide().appendTo($('body'));
				// 清空下拉列表的内容并且隐藏下拉列表区
				var clear = function() {
					$autocomplete.empty().hide();
				};
				// 注册事件，当输入框失去焦点的时候清空下拉列表并隐藏
				element.blur(function() {
					if ($autocomplete.find('li').eq(selectedItem).attr("data-id")>0) {
						scope.datavalue = $autocomplete.find('li').eq(selectedItem).attr("data-id");
						ngModel.$setViewValue($autocomplete.find('li').eq(selectedItem).text());
						ngModel.$render();
					}
					setTimeout(clear, 500);
				});
				// 下拉列表中高亮的项目的索引，当显示下拉列表项的时候，移动鼠标或者键盘的上下键就会移动高亮的项目，想百度搜索那样
				var selectedItem = null;
				// timeout的ID
				var timeoutid = null;
				// 设置下拉项的高亮背景
				var setSelectedItem = function(item) {
					// 更新索引变量
					selectedItem = item;
					// 按上下键是循环显示的，小于0就置成最大的值，大于最大值就置成0
					if (selectedItem < 0) {
						selectedItem = $autocomplete.find('li').length - 1;
					} else if (selectedItem > $autocomplete.find('li').length - 1) {
						selectedItem = 0;
					}
					// 首先移除其他列表项的高亮背景，然后再高亮当前索引的背景
					$autocomplete.find('li').removeClass('highlight').eq(selectedItem).addClass('highlight');
				};
				var ajax_request = function() {
					enterpriseService.list(scope.ngModel).then(
							function(response) {
								if (response.code == 1) {
									var result = response.data;
									// 遍历data，添加到自动完成区
									$.each(result, function(index, bean) {
										var enterpriseId = bean.enterpriseId;
										var enterpriseName = bean.enterpriseName;
										// 创建li标签,添加到下拉列表中
										$('<li data-id="' + enterpriseId + '"></li>').text(enterpriseName)
												.appendTo($autocomplete).addClass('clickable').hover(function() {
													// 下拉列表每一项的事件，鼠标移进去的操作
													$(this).siblings().removeClass('highlight');
													$(this).addClass('highlight');
													selectedItem = index;
												}, function() {
													// 下拉列表每一项的事件，鼠标离开的操作
													$(this).removeClass('highlight');
													// 当鼠标离开时索引置-1，当作标记
													selectedItem = -1;
												}).click(function() {
													// 鼠标单击下拉列表的这一项的话，就将这一项的值添加到输入框中
													// element.val(term);
													// scope.ngModel =
													// enterpriseName;
													scope.datavalue = enterpriseId;
													ngModel.$setViewValue(enterpriseName);
													ngModel.$render();
													// 清空并隐藏下拉列表
													$autocomplete.empty().hide();
												});
									});// 事件注册完毕
									// 设置下拉列表的位置，然后显示下拉列表
									var ypos = element.offset().top;
									var xpos = element.offset().left;
									$autocomplete.css('width', element.css('width'));
									$autocomplete.css({
										'position' : 'absolute',
										'left' : xpos + "px",
										'top' : ypos + 30 + "px",
										'z-index' : 9999
									});
									setSelectedItem(0);
									// 显示下拉列表
									$autocomplete.show();
								} else if (response.code == 501) {
									window.location.href = loginUrl;
								} else {
									scope.message = response.message;
								}
							}, function(error) {
								scope.error = error;
							});
				};
				// 对输入框进行事件注册
				element.keyup(function(event) {
					// 字母数字，退格，空格
					if (event.keyCode > 40 || event.keyCode == 8 || event.keyCode == 32) {
						// 首先删除下拉列表中的信息
						$autocomplete.empty().hide();
						clearTimeout(timeoutid);
						timeoutid = setTimeout(ajax_request, 100);
					} else if (event.keyCode == 38) {
						// 上
						// selectedItem = -1 代表鼠标离开
						if (selectedItem == -1) {
							setSelectedItem($autocomplete.find('li').length - 1);
						} else {
							// 索引减1
							setSelectedItem(selectedItem - 1);
						}
						event.preventDefault();
					} else if (event.keyCode == 40) {
						// 下
						// selectedItem = -1 代表鼠标离开
						if (selectedItem == -1) {
							setSelectedItem(0);
						} else {
							// 索引加1
							setSelectedItem(selectedItem + 1);
						}
						event.preventDefault();
					}
				}).keypress(function(event) {
					// enter键
					if (event.keyCode == 13) {
						// 列表为空或者鼠标离开导致当前没有索引值
						if ($autocomplete.find('li').length == 0 || selectedItem == -1) {
							return;
						}
						// element.val($autocomplete.find('li').eq(selectedItem).text());
						// scope.ngModel =
						// $autocomplete.find('li').eq(selectedItem).text();
						scope.datavalue = $autocomplete.find('li').eq(selectedItem).attr("data-id");
						ngModel.$setViewValue($autocomplete.find('li').eq(selectedItem).text());
						ngModel.$render();
						$autocomplete.empty().hide();
						event.preventDefault();
					}
				}).keydown(function(event) {
					// esc键
					if (event.keyCode == 27) {
						$autocomplete.empty().hide();
						event.preventDefault();
					}
				});
			});
		}
	};
});
myapp.directive("donutChart", function($timeout) {
	return {
		restrict : 'A',
		scope : {
			"data" : "=data",
		},
		link : function(scope, element) {
			$timeout(function() {
				scope.$watch("data", function(newValue, oldValue, ctrl) {
					if (newValue) {
						$(element).html("");
						var _DataSet = DataSet;
						DataView = _DataSet.DataView;
						var dv = new DataView();
						dv.source(newValue).transform({
							type : 'percent',
							field : 'count',
							dimension : 'item',
							as : 'percent'
						});
						var chart = new G2.Chart({
							container : element[0],
							width : element.width(),
							height : element.height(),
							animate : false
						});
						chart.source(dv, {
							percent : {
								formatter : function formatter(val) {
									val = (val * 100).toFixed(0) + '%';
									return val;
								}
							}
						});
						chart.coord('theta', {
							radius : 0.75,
							innerRadius : 0.6
						});
						chart.tooltip({
							showTitle : false,
							itemTpl : '<li><span style="background-color:{color};"'
									+ ' class="g2-tooltip-marker"></span>{name}:{value}</li>'
						});
						var interval = chart.intervalStack().position('percent').color('item').label('percent', {
							formatter : function formatter(val, item) {
								return item.point.item + ': ' + val;
							}
						}).tooltip('item*percent', function(item, percent) {
							percent = (percent * 100).toFixed(0) + '%';
							return {
								name : item,
								value : percent
							};
						}).style({
							lineWidth : 1,
							stroke : '#fff'
						});
						chart.render();
					}
				})
			});
		}
	};
});
myapp.directive("columnChart", function($timeout) {
	return {
		restrict : 'A',
		scope : {
			"data" : "=data",
		},
		link : function(scope, element) {
			$timeout(function() {
				scope.$watch("data", function(newValue, oldValue, ctrl) {
					if (newValue) {
						$(element).html("");
						var chart = new G2.Chart({
							container : element[0],
							width : element.width(),
							height : element.height()
						});
						chart.source(newValue);
						chart.scale('value', {
							type : 'linear'
						});
						chart.interval().position('field*value');
						chart.render();
					}
				})
			});
		}
	};
});
myapp.directive("multiLineChart", function($timeout) {
	return {
		restrict : 'A',
		scope : {
			"data" : "=data",
			"fields" : "=fields",
		},
		link : function(scope, element) {
			$timeout(function() {
				scope.$watch("data", function(newValue, oldValue, ctrl) {
					if (newValue && newValue.length > 0) {
						$(element).html("");
						var ds = new DataSet();
						var dv = ds.createView().source(newValue);
						dv.transform({
							type : 'fold',
							fields : scope.fields, // 展开字段集
							key : 'key', // key字段
							value : 'value' // value字段
						});
						var chart = new G2.Chart({
							container : element[0],
							width : element.width(),
							height : element.height()
						});
						chart.source(dv, {
							date : {
								type : 'linear',
								// tickInterval : 1,
								range : [ 0.05, 0.95 ]
							}
						});
						chart.tooltip({
							crosshairs : {
								type : 'line'
							}
						});
						chart.axis('value', {
							label : {
								formatter : function formatter(val) {
									return val;
								}
							},
							line : {
								lineWidth : 1, // 设置线的宽度
								lineDash : [ 3, 3 ]
							// 设置虚线样式
							}
						});
						chart.line().position('date*value').color('key');
						chart.point().position('date*value').color('key').size(1).shape('circle').style({
							stroke : '#fff',
							lineWidth : 1
						});
						chart.render();
					}
				})
			});
		}
	};
});
myapp.directive("chinaMapChart", function($timeout) {
	return {
		restrict : 'A',
		scope : {
			"data" : "=data",
			"field" : "=field",
		},
		link : function(scope, element) {
			$timeout(function() {
				scope.$watch("data", function(newValue, oldValue, ctrl) {
					$.getJSON('../common/js/plugins/g2/china.json', function(mapData) {
						$(element).html("");
						const chart = new G2.Chart({
							container : element[0],
							width : element.width(),
							height : element.height(),
							padding : {
								top : 20,
								right : 10,
								bottom : 20,
								left : 100
							},
							background : {
								fill : "#fff", // 图表背景色
								fillOpacity : 0, // 图表背景透明度
								stroke : "#fff", // 图表边框颜色
								strokeOpacity : 0, // 图表边框透明度
								opacity : 0, // 图表整体透明度
								lineWidth : 0, // 图表边框粗度
								radius : 0
							}
						});
						chart.tooltip({
							showTitle : false
						});
						// 同步度量
						chart.scale({
							longitude : {
								sync : true
							},
							latitude : {
								sync : true
							},
						});
						chart.axis(false);
						chart.legend('number', {
							position : 'left'
						});
						// 绘制世界地图背景
						const ds = new DataSet();
						const worldMap = ds.createView('back').source(mapData, {
							type : 'GeoJSON'
						});
						const worldMapView = chart.view();
						worldMapView.source(worldMap);
						worldMapView.tooltip(false);
						worldMapView.polygon().position('longitude*latitude').style({
							fill : '#eeeeee',
							stroke : '#fff',
							lineWidth : 1
						});

						const userData = [];
						if (newValue) {
							console.log(newValue);
							const locationList = newValue;
							var provinceNum = 0;
							var hasProvinceName = [];
							for (var i = 0; i < locationList.length; i++) {
								const location = locationList[i];
								var provinceName = location.provinceName;
								const number = location[scope.field];
								// 去重
								var isExistedName = false;
								for (var j = 0; j < hasProvinceName.length; j++) {
									if (provinceName === hasProvinceName[j]) {
										isExistedName = true;
										break;
									}
								}
								if (!isExistedName) {
									provinceNum++;
									userData.push({
										provinceName : provinceName,
										number : number
									});
								}
								hasProvinceName.push(provinceName);
							}
							console.log(userData);
						}
						const userDv = ds.createView().source(userData).transform({
							geoDataView : worldMap,
							field : 'provinceName',
							type : 'geo.region',
							as : [ 'longitude', 'latitude' ]
						}).transform({
							type : 'map',
							callback : function(obj) {
								return obj;
							}
						});
						const userView = chart.view();
						userView.polygon().position('longitude*latitude').color('number', [ "#FF9F9F", "#ff0000" ]).opacity(
								'number').tooltip('provinceName*number').animate({
							leave : {
								animation : 'fadeOut'
							}
						});
						userView.source(userDv, {
							'provinceName' : {
								alias : '省份'
							},
							'number' : {
								alias : '数量'
							}
						});
						chart.render();
					});
				})
			});
		}
	};
});
myapp.directive("donutStaticChart", function($timeout) {
	return {
		restrict : 'A',
		scope : {
			"data" : "=data",
			"label" : "=label",
			"colors" : "=colors",
		},
		link : function(scope, element) {
			$timeout(function() {
				scope.$watch("data", function(newValue, oldValue, ctrl) {
					if (newValue) {
						$(element).html("");
						var _DataSet = DataSet;
						DataView = _DataSet.DataView;
						var dv = new DataView();
						dv.source(newValue).transform({
							type : 'percent',
							field : 'count',
							dimension : 'item',
							as : 'percent'
						});
						var chart = new G2.Chart({
							container : element[0],
							width : element.width(),
							height : element.height(),
							padding : 5,
							animate : false
						});
						chart.guide().html(
								{
									position : [ '50%', '50%' ],
									html : '<div style="color:#000000;font-size: 20px;text-align: center;width: 10em;">'
											+ scope.label + '<br>'
											+ '<span style="color:#8c8c8c;font-size:14px">已使用</span></div>',
									alignX : 'middle',
									alignY : 'middle'
								});
						chart.legend(false);
						chart.tooltip(false);
						chart.source(dv, {
							percent : {
								formatter : function formatter(val) {
									val = (val * 100).toFixed(0) + '%';
									return val;
								}
							}
						});
						chart.coord('theta', {
							radius : 1,
							innerRadius : 0.8
						});
						var interval = chart.intervalStack().position('percent').color('item', scope.colors).style({
							lineWidth : 1,
							stroke : '#fff'
						});
						chart.render();
						chart.off('click');
					}
				})
			});
		}
	};
});
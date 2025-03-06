String.prototype.endWith = function(s) {
	if (s == null || s == "" || this.length == 0 || s.length > this.length)
		return false;
	if (this.substring(this.length - s.length) == s)
		return true;
	else
		return false;
	return true;
}
String.prototype.startWith = function(s) {
	if (s == null || s == "" || this.length == 0 || s.length > this.length)
		return false;
	if (this.substr(0, s.length) == s)
		return true;
	else
		return false;
	return true;
}

var setCookie = function(name, value) {
	var Days = 365;
	var exp = new Date();
	exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
	document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}

var contains = function(arr, obj) {
	var i = arr.length;
	while (i--) {
		if (arr[i] == obj) {
			return true;
		}
	}
	return false;
}

var vaildPageSizes = [ 50, 100, 150, 200 ];

var containsForString = function(str, st) {
	var bool = str.indexOf(st);
	if (bool > -1) {
		return true;
	}
	return false;
}
var postTransformRequest = function(obj) {
	var str = [];
	if (Object.prototype.toString.call(obj) == '[object Array]') {
		addArray(str, obj);
	} else if (Object.prototype.toString.call(obj) == '[object Object]') {
		addObject(str, obj);
	}
	var data = str.join("&");
	return data;
}

var addObject = function(map, obj, prefix) {
	for ( var paramName in obj) {
		if (paramName.indexOf("$") != -1) {
			continue;
		}
		var paramValue = obj[paramName];
		if (!isBlank(paramValue)) {
			if (Object.prototype.toString.call(paramValue) == '[object Array]') {
				if (isBlank(prefix)) {
					addArray(map, paramValue, encodeURIComponent(paramName));
				} else {
					addArray(map, paramValue, prefix + "." + encodeURIComponent(paramName));
				}
			} else if (Object.prototype.toString.call(paramValue) == '[object Object]') {
				if (isBlank(prefix)) {
					addObject(map, paramValue, encodeURIComponent(paramName));
				} else {
					addObject(map, paramValue, prefix + "." + encodeURIComponent(paramName));
				}
			} else {
				if (!isBlank(paramValue)) {
					if (isBlank(prefix)) {
						map.push(encodeURIComponent(paramName) + "=" + encodeURIComponent(paramValue));
					} else {
						map.push(encodeURIComponent(prefix + "." + paramName) + "=" + encodeURIComponent(paramValue));
					}
				}
			}
		}
	}
}

var addArray = function(map, arrays, prefix) {
	for (var i = 0; i < arrays.length; i++) {
		var arrayValue = arrays[i];
		if (!isBlank(arrayValue)) {
			if (Object.prototype.toString.call(arrayValue) == '[object Array]') {
				addArray(map, arrayValue, prefix + "[" + i + "]");
			} else if (Object.prototype.toString.call(arrayValue) == '[object Object]') {
				addObject(map, arrayValue, prefix + "[" + i + "]");
			} else {
				if (!isBlank(arrayValue)) {
					map.push(encodeURIComponent(prefix ) + "=" + encodeURIComponent(arrayValue));
				}
			}
		}
	}
}

var resolveBean = function(response) {
	if (response.code == 1) {
		return response.data;
	} else if (response.code == 501) {
		window.location.href = loginUrl;
	} else {
		return response.data;
	}
}

var isIp = function(ip) {
	var exp = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	if (isBlank(ip)) {
		return false;
	}
	if (ip.trim().match(exp) != null) {
		if (ip == "0.0.0.0") {
			return false;
		}
		return true;
	} else {
		return false;
	}
}

var isBlank = function(str) {
	if (str == undefined) {
		return true;
	}
	if (str == null) {
		return true;
	}
	if (str === "") {
		return true;
	}
	var regu = "^[ ]+$";
	var re = new RegExp(regu);
	return re.test(str);
}

var isEmail = function(str) {
	var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	if (reg.test(str)) {
		return true;
	} else {
		return false;
	}
}

var isPort = function(str) {
	if (isNaN(str)) {
		return false;
	}
	if (str >= 0 && str <= 65535) {
		return true;
	} else {
		return false;
	}
}

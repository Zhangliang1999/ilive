myapp.filter("blank", function() {
	return function(input, showStr) {
		if (input) {
			return input;
		}
		return showStr;
	}
});
myapp.filter("duration", function() {
	return function(input, showStr) {
		try {
			if (isNaN(input)) {
				return "00:00:00";
			} else {
				var hours = parseInt(input / 3600);
				var minutes = parseInt(input % 3600 / 60);
				var secounds = input % 60;
				var duration = "";
				if (hours < 10) {
					duration += "0" + hours;
				} else {
					duration += hours;
				}
				duration += ":";
				if (minutes < 10) {
					duration += "0" + minutes;
				} else {
					duration += minutes;
				}
				duration += ":";
				if (secounds < 10) {
					duration += "0" + secounds;
				} else {
					duration += secounds;
				}
				return duration;
			}
		} catch (e) {
			return showStr;
		}

	}
});
myapp.filter("hour", function() {
	return function(input, showStr) {
		try {
			if (isNaN(input)) {
				return "0";
			} else {
				var hours = (input / 3600).toFixed(2);
				return hours;
			}
		} catch (e) {
			return showStr;
		}
		
	}
});
myapp.filter("fileSize", function() {
	return function(input, showStr) {
		if (input) {
			if (input > 1048576) {
				return (input / 1048576).toFixed(2) + " GB";
			} else if (input > 1024) {
				return (input / 1024).toFixed(2) + " MB";
			} else {
				return input + " KB";
			}
		}
		return showStr;
	}
});
myapp.filter("boolean", function() {
	return function(input, showStr) {
		if (!input) {
			return "否";
		} else {
			return "是";
		}
		return showStr;
	}
});

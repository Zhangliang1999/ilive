(function() {

    var css = require('./css.css'); //创建css
    var arr = require('./facemap.js'); //返回表情数组
    var insertText = require('./insertText.js'); //负责向textarea插入字符串
    var position = require('./position.js'); //根据按钮显示div的位置
    var creatFace = require('./creatFace.js'); //传入arr 和图片路径 返回html字符串

    $.extend({
        qqface: function(opt) {

            var defaults = {
                before: function() {},
                after: function() {}
            };

            opt = $.extend({}, defaults, opt);

            var div = $('<div class="jquery-qqface">');
            var layer = $('<div class="jquery-qqface-layer">');

            layer.html(creatFace(arr, opt.imgPath));
            div.append(layer).appendTo('body');

            //表情层事件
            div
                .on('click', 'i', function() {
                    var code = '[:' + $(this).data('code') + ']';
                    opt.before.call(null, opt.textarea, code);
                    insertText(opt.textarea[0], code);
                    opt.after.call(null, opt.textarea, code);
                    div.hide();
                });

            //点击打开表情层
            opt.handle.on('click', function(e) {
                div.show();
                position(opt.handle, div);
                e.stopPropagation();
            });

            //空白处点击，关闭表情层
            $(document).on('click', function() {
                div.hide();
            });
        }
    });

}());
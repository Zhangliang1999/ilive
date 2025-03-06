# jquery.qqface
[![Packagist](https://img.shields.io/packagist/l/doctrine/orm.svg?style=plastic)](https://github.com/kyo4311/jquery.qqface)

jQuery QQ 表情插件。     
点击一个按钮，弹出一个选择QQ表情的组件，点击即可选择表情，插入到textara里面。

查看：示例：http://kyo4311.github.io/jquery.qqface/   
<img src="http://kyo4311.github.io/jquery.qqface/gif/bg.png" width="406" height="163" />

# 开始使用
1. 引入jQuery
2. 引入jquery.qqface.js

# html
```
<div>
    <textarea id="liveChatContent" spellcheck="false"></textarea>
</div>
<a href="javascript:;" id="face">表情</a>
```

# js
```js
//看起来很简单！
$.qqface({
    imgPath : 'gif/',
    textarea : $('#liveChatContent'),
    handle : $('#face')
});
```

# License
Copyright (c) 2016 [guosheng](http://www.f00sun.com/) Licensed under the MIT License.


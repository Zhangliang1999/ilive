//-----------------------------------------------------------------------------------  
//  “虚”函数，可以由调用方改写  
//-----------------------------------------------------------------------------------  
//初始化  
var slide_func_Init = function(){  
    //高度自适应..宽度为显示器宽,设置容器的大小和位置  
    window.winWidth = _slide_width;  
    var width = _slide_width;  
    var height = 130;  
      
    //占位，由于滑动块父容器是position:absolute,用这个DIV将它包容起来，利于上下的排版  
    var placeholder = $("#divPlaceholder");  
    placeholder.css("width", width);  
    placeholder.css("height", height);      
      
    //观看滑动块的视图  
    var view = $("#slideView");  
    view.css("width", width);  
    view.css("height", height);  
      
    //滑动块父容器  
    slide_container = $(".slideContainer");  
  
    //滑动块  
    var slideList = $(".block");//待滑动的块  
    slideList.css("width", width);  
    slideList.css("height", height);  
    var slideLen = slideList.length;  
    var block = new Array();  
    for( var i = 0; i < slideLen;i++) {  
        block[i] = slideList.eq(i);  
    }  
    slide_blockShow = block;  
      
    //待加载数据  
    slide_blockData = new Array("#999","#f00","#0f0","#00f","#ff0","#0ff","#000");  
      
    var tbcolor ="<table><tr>";  
    for(var i = 0;i < slide_blockData.length;i++){  
        tbcolor += "<td style='background-color:" + slide_blockData[i] + "'> </td>";  
    }  
    tbcolor += "</tr></table>";  
    $("#divColor").html(tbcolor);  
      
   
}  
//滑动块数据加载  
var slide_func_ShowBlock = function(p,ari){  
    var i = ari[0];  
    var il = ari[1];  
    var ir = ari[2];  
          
    var arj = _slide_func_GetPointer(p,slide_blockData.length);  
    var j = arj[0];  
    var jl = arj[1];  
    var jr = arj[2];  
      
    slide_blockShow[i].css("background-color", slide_blockData[j]);  
    slide_blockShow[il].css("background-color", slide_blockData[jl]);  
    slide_blockShow[ir].css("background-color", slide_blockData[jr]);  
}  
var slide_func_debug = function(text){  
    $(".debug").text(text);  
}  
  
//-----------------------------------------------------------------------------------  
//  全局变量  
//-----------------------------------------------------------------------------------  
var slide_container;    //滑动块容器  
var slide_blockShow;    //滑动块，应该有3个  
var slide_blockData;    //用于在滑动块上显示的数据，数量不限，但多于3个那是肯定的  
var _slide_width = window.screen.width;  
var _slide_height = (window.screen.height > 500) ? 500 : window.screen.height;  
  
//-----------------------------------------------------------------------------------  
//  私有函数  
//-----------------------------------------------------------------------------------  
$(document).ready(function(){  
    _slide_func_disRightMouse();  
      
    var startX;  
    var basePoint = 0;   
    var stargDis;  
    var distance;  
    var maxDistance = 50;  
    var doc = $(document);  
    var animateSpeed = 800;  
    var recoverSpeed = 100;  
    var width = _slide_width;  
    var yT,yB;  
    var p;  
      
    slide_func_Init();  
      
    var container = slide_container;  
    if(container == null){  
        alert("还没有设置滑动块容器！");  
    }  
      
    var blockShow = slide_blockShow;  
    if(blockShow == null || blockShow.length < 3){  
        alert("还没有设置滑动块或滑动块数量少于3个！");  
    }  
    yT = blockShow[0].offset().top;  
    yB = yT + blockShow[0].height();  
  
    //鼠标大人，乃过气之物？妇人之屁鬼如鼠，壮士之屁猛若牛  
    doc.mousedown(function(event){  
        startX = event.clientX;  
        doc.bind("mousemove", moveHandler);   
        startX = event.clientX;  
    });  
    doc.mouseup(function(){  
        if(distance < maxDistance){  
            recoverPosition(distance);  
        }  
        doc.unbind("mousemove");  
    });  
    function moveHandler(event){  
        var y = event.clientY;  
        if( y < yT || y > yB) return;  
        distance = event.clientX - startX;  
        movePanel(distance);  
        if(event.clientX - startX > maxDistance){  
            doc.unbind("mousemove");  
            slideRight();  
            return;  
        }  
        if(event.clientX - startX < -maxDistance){  
            doc.unbind("mousemove");      
            slideLeft();  
            return;  
        }  
    }  
    //touch...别摸我  
    try{  
        document.addEventListener('touchstart',function(ev){  
                    startX = ev.touches[0].pageX;  
                    touchBind(ev);  
                },false);  
        document.addEventListener('touchend',function(ev){  
                    if(distance < maxDistance){  
                        recoverPosition(distance);  
                    }  
                    touchUnBind();  
                },false);  
    }catch(ex){}  
    function touchBind(ev){  
        document.addEventListener('touchmove',touchHandler,false);  
    }  
    function touchUnBind(){  
        document.removeEventListener('touchmove',touchHandler,false);  
    }  
    function touchHandler(ev){  
        var y = ev.touches[0].pageY;  
        if( y < yT || y > yB) return;  
        distance = ev.touches[0].pageX - startX;  
        movePanel(distance);  
        if(ev.touches[0].pageX - startX > maxDistance){  
            slideRight();  
            touchUnBind();  
            return;  
        }  
        if(ev.touches[0].pageX - startX < -maxDistance){  
            slideLeft();  
            touchUnBind();  
            return;  
        }  
    }  
      
    function recoverPosition(dis){  
        container.animate({   
            left : basePoint  
        }, recoverSpeed );  
        dis = 0;  
    }  
    function movePanel(distance){  
        container.css("left", basePoint + distance);  
    }  
    function slideRight(){  
        container.animate({   
            left : basePoint += width  
        }, animateSpeed);  
        p--;  
        ShowBlock(p);  
    }  
    function slideLeft(){                   
        container.animate({   
            left : basePoint -= width  
        }, animateSpeed);  
        p++;  
        ShowBlock(p);  
    }  
    function ShowBlock(p){  
        var ari = _slide_func_GetPointer(p,blockShow.length);  
        var i = ari[0];  
        var il = ari[1];  
        var ir = ari[2];  
  
        var sleft = blockShow[i].css("left");  
        var left = sleft.substring(0,sleft.indexOf("px")) * 1;  
        blockShow[il].css("left", (left - width) + "px");  
        blockShow[ir].css("left", (left + width) + "px");      
          
        slide_func_ShowBlock(p,ari);  
    }  
      
    p = 0;  
    ShowBlock(p);  
})  
function _slide_func_GetPointer(p,len){  
    var min = 0;  
    var max = len - 1;  
    var i = p;  
    if(i < 0){  
        i = len - (i * -1) % len;  
    }  
    i = i % len;  
    var il = (i > min) ? i - 1 : max;  
    var ir = (i < max) ? i + 1 : min;  
    return [i,il,ir];  
}  
function _slide_func_disRightMouse(){  
    $(document).bind("contextmenu",function(e){  
         return false;  
    });  
} 
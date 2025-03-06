//左侧开始

$(document).ready(function(){
    $(".SortL").click(function(){
        if ($('.Sort-eject-L').hasClass('grade-w-roll')) {
            $('.Sort-eject-L').removeClass('grade-w-roll');
        } else {
            $('.Sort-eject-L').addClass('grade-w-roll');
        }
    });
});

//中间开始

$(document).ready(function(){
    $(".SortC").click(function(){
        if ($('.Sort-eject-C').hasClass('grade-w-roll')) {
            $('.Sort-eject-C').removeClass('grade-w-roll');
        } else {
            $('.Sort-eject-C').addClass('grade-w-roll');
        }
    });
});

//右侧开始

$(document).ready(function(){
    $(".SortR").click(function(){
        if ($('.Sort-eject-R').hasClass('grade-w-roll')) {
            $('.Sort-eject-R').removeClass('grade-w-roll');
        } else {
            $('.Sort-eject-R').addClass('grade-w-roll');
        }
    });
});


//判断页面是否有弹出

$(document).ready(function(){
    $(".SortL").click(function(){
        if ($('.Sort-eject-C').hasClass('grade-w-roll')){
            $('.Sort-eject-C').removeClass('grade-w-roll');
        };
    });
});
$(document).ready(function(){
    $(".SortL").click(function(){
        if ($('.Sort-eject-R').hasClass('grade-w-roll')){
            $('.Sort-eject-R').removeClass('grade-w-roll');
        };
    });
});



$(document).ready(function(){
    $(".SortC").click(function(){
        if ($('.Sort-eject-L').hasClass('grade-w-roll')){
            $('.Sort-eject-L').removeClass('grade-w-roll');
        };
    });
});
$(document).ready(function(){
    $(".SortC").click(function(){
        if ($('.Sort-eject-R').hasClass('grade-w-roll')){
            $('.Sort-eject-R').removeClass('grade-w-roll');
        };
    });
});


$(document).ready(function(){
    $(".SortR").click(function(){
        if ($('.Sort-eject-L').hasClass('grade-w-roll')){
            $('.Sort-eject-L').removeClass('grade-w-roll');
        };
    });
});
$(document).ready(function(){
    $(".SortR").click(function(){
        if ($('.Sort-eject-C').hasClass('grade-w-roll')){
            $('.Sort-eject-C').removeClass('grade-w-roll');
        };
    });
});
//js点击事件监听开始

function Sorts(sbj){
    var arr = document.getElementById("Sort-Sort").getElementsByTagName("li");
    for (var i = 0; i < arr.length; i++){
        var a = arr[i];
        a.style.borderBottom = "";
    };
    sbj.style.borderBottom = "solid 1px #ff7c08"
}

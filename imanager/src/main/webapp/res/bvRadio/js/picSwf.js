$(document).ready(function(){
	$('.dragZoneP').click(function(){
		$('#contentdiv').html('');
		initSwf(1);
		$("#imageUpl").dialog("open");
	})		
	
	$( "#imageUpl" ).dialog({
		autoOpen: false,
		height: 400,
		width: 600,
		buttons: {
			"确定": function() {
				sDialog();
				$( this ).dialog( "close" );
			},
			"取消": function() {
				$( this ).dialog( "close" );
			}
		},
		close: function() {
		}
	});
})

function sDialog(){
	var s = $('#contentdiv').find('.cpimg').attr('src');
	$('.AddQImgCon.picU').append('<img src='+s+' style=display:block;width:155px;height:210px;>');
}

function delUpImage(obj){
	$(obj).parent().remove();
}
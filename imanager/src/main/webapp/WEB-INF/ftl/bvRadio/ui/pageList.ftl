<#--
表格标签：用于显示列表数据。
	value：列表数据，可以是Pagination也可以是List。
	class：table的class样式。默认"pn-ltable"。
	sytle：table的style样式。默认""。
	width：表格的宽度。默认100%。
-->
<#macro pageList value id="" listAction="v_list.do" >
<script>
function autoHeight() {
		var h = $(window).height();
		var h_old = 400;
		if (h > h_old) {
			$("#tableBody").css("height", h - 280);
		} else {
			return false;
		}
	}
	$(document).ready(function() {
		autoHeight();
		$(window).resize(autoHeight);
	});
</script>
<#if value?is_sequence><#local pageList=value/><#else><#local pageList=value.list/></#if>
<form action="${listAction}" id="_page_form" class="no-margin" method="post">
<div class="row m-t">
                <div class="col-lg-12 p-w-lg">
                    <table class="table table-striped table-hover ScrollViewer table-responsive">
                        <thead class="gray-bg">
                        <tr>
                            <#assign i=-1/>
							<#nested row,i,true/>
                        </tr>
                        </thead>
                        <tbody style="height: 363px;">
							<#if pageList??>
								<#list pageList as row>
								<#assign i=row_index has_next=row_has_next/>
								<tr onclick="PL.onclickLi(this)" ondblclick="PL.ondblclickLi(this)">
								<#nested row,row_index,row_has_next/>
								</tr>
								</#list>
								<#else>
							</#if>
                        </tbody>
                    </table>
                </div>
                <div class="col-lg-12 m-t-sm pageBox">
                    <div class="pull-right btn-group m-r">
						<#if !value?is_sequence>
							<#assign pageNow=value.pageNo totalPage=value.totalPage >
							<input type="hidden" name="pageNo" value="${value.pageNo}">
							<#if (pageNow>0) && (pageNow<=totalPage) >
								<#if (pageNow<=1)>
								<button type="button" class="btn btn-sm btn-white disabled">
									<a><i class="fa fa-chevron-left"></i></a>
								</button>
								<#else>
								<button type="button" onclick="PL.gotoPage(${pageNow - 1})" class="btn btn-sm btn-white">
									<a><i class="fa fa-chevron-left"></i></a>
								</button>
								</#if>
								<#if (pageNow-4 >1)>
								<#if (pageNow<totalPage-2)>
								<button type="button" onclick="PL.gotoPage(1)" class="btn btn-sm btn-white">
									<a>1</a>
								</button>
								<button type="button" class="btn btn-sm btn-white">
									<a>...</a>
								</button>
								<#list pageNow-2..pageNow-1 as i>
								<button type="button" onclick="PL.gotoPage(${i})" class="btn btn-sm btn-white">
									<a>${i}</a>
								</button>
								</#list>
								<#elseif (pageNow-6 > 1)>
								<button onclick="PL.gotoPage(1)" type="button" class="btn btn-sm btn-white">
									<a>1</a>
								</button>
								<button type="button" class="btn btn-sm btn-white">
									<a>...</a>
								</button>
								<#list pageNow-4..pageNow-1 as i>
								<button type="button" onclick="PL.gotoPage(${i})" class="btn btn-sm btn-white">
									<a>${i}</a>
								</button>
								</#list>
								<#else>
								<#list 1..pageNow-1 as i>
								<button type="button" onclick="PL.gotoPage(${i})" class="btn btn-sm btn-white">
									<a>${i}</a>
								</button>
								</#list>
								</#if>
								<#elseif (pageNow=1)>
								<#else>
								<#list 1..pageNow-1 as i>
								<button type="button" onclick="PL.gotoPage(${i})" class="btn btn-sm btn-white">
									<a>${i}</a>
								</button>
								</#list>
								</#if>
								<button type="button" onclick="PL.gotoPage(${pageNow})" class="active btn btn-sm btn-white">
									<a>${pageNow}</a>
								</button>
								<#if (pageNow+4 < totalPage)>
								<#if (pageNow>2)>
								<#list pageNow+1..pageNow+2 as i>
								<button type="button" onclick="PL.gotoPage(${i})" class="btn btn-sm btn-white">
									<a>${i}</a>
								</button>
								</#list>
								<button type="button" class="btn btn-sm btn-white">
									<a>...</a>
								</button>
								<button type="button" onclick="PL.gotoPage(${totalPage})" class="btn btn-sm btn-white">
									<a>${totalPage}</a>
								</button>
								<#elseif (pageNow+6 < totalPage)>
								<#list pageNow+1..pageNow+4 as i>
								<button type="button" onclick="PL.gotoPage(${i})" class="btn btn-sm btn-white">
									<a>${i}</a>
								</button>
								</#list>
								<button type="button" class="btn btn-sm btn-white">
									<a>...</a>
								</button>
								<button type="button" onclick="PL.gotoPage(${totalPage})" class="btn btn-sm btn-white">
									<a>${totalPage}</a>
								</button>
								<#else>
								<#list pageNow+1..totalPage as i>
								<button onclick="PL.gotoPage(${i})" type="button" class="btn btn-sm btn-white">
									<a>${i}</a>
								</button>
								</#list>
								</#if>
								<#elseif (pageNow=totalPage)>
								<#else>
								<#list pageNow+1..totalPage as i>
								<button type="button" onclick="PL.gotoPage(${i})" class="btn btn-sm btn-white">
									<a>${i}</a>
								</button>
								</#list>
								</#if>
								<#if (pageNow>=totalPage)>
								<button type="button" class="btn btn-sm btn-white disabled">
									<a><i class="fa fa-chevron-right"></i></a>
								</button>
								<#else>
								<button type="button" onclick="PL.gotoPage(${pageNow + 1})" class="btn btn-sm btn-white">
									<a><i class="fa fa-chevron-right"></i></a>
								</button>
								</#if>
								<#else>
								<button type="button" class="btn btn-sm btn-white">
									<a><i class="fa fa-chevron-left"></i></a>
								</button>
								<button type="button" onclick="PL.gotoPage(1)" class="btn btn-sm btn-white">
									<a>1</a>
								</button>
								<button type="button" class="btn btn-sm btn-white">
									<a><i class="fa fa-chevron-right"></i></a>
								</button>
								</#if>
						</#if>
      
                    </div>
                </div>
            </div>
	</form>
</#macro>
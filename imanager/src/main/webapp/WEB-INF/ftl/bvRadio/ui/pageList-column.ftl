<#--
表格列标签：展示数据列。
	title：标题（列头）。直接显示字符串。默认""。
	class：css样式class
	style：css样式style
-->
<#macro column title="" class="" style="">
<#if i==-1>
	<th <#if class!=""> class="${class}"</#if><#if style!=""> style="${style}"</#if>><#if title!="">${title}</#if></th><#rt/>
<#elseif i==-2||i==-3>
<#else>
	<td <#if class!=""> class="${class}"</#if><#if style!=""> style="${style}"</#if>><#nested/></td><#rt/>
</#if>
</#macro>

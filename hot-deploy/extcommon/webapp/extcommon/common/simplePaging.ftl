<#if paging?has_content && paging.getTotalPage() gt 1>
    <#assign paramMap = Static["org.ofbiz.base.util.UtilHttp"].getParameterMap(request)!""/>
    <#if paramMap?has_content>
        <#assign previousParams = Static["org.ofbiz.base.util.UtilHttp"].urlEncodeArgs(paramMap)/>
    <#else>
        <#assign previousParams = ""/>
    </#if>
    <#if previousParams?has_content>
        <#assign previousParams = Static["org.ofbiz.base.util.UtilHttp"].stripNamedParamsFromQueryString(previousParams,Static["org.ofbiz.base.util.UtilMisc"].toSet("pageIndex"))/>
    </#if>
    <#assign requestUri = request.getRequestURI()!""/>
    <#if previousParams?has_content>
        <#assign pageIndexUrl = requestUri +"?"+previousParams+"&pageIndex="/>
    <#else>
        <#assign pageIndexUrl = requestUri +"?pageIndex="/>
    </#if>
	
	<#--
    <div class="pagination">
    <span class="page-info">每页${paging.pageSize}条</span>
    <span class="page-info">共${paging.totalSize}条</span>
        <#if paging.isFirstPage()>
    <a href="javascript:;">第一页</a>
        <#else>
    <a href="${pageIndexUrl+"1"}">第一页</a>
        </#if>
        
        <#if paging.hasPreviousPage()>
    <a href="${pageIndexUrl+(paging.pageIndex-1)}">上一页</a>
        <#else>
    <a href="javascript:;">上一页</a>
        </#if>

        <#if paging.hasNextPage()>
            <a href="${pageIndexUrl+(paging.pageIndex+1)}">下一页</a>
        <#else>
            <a href="javascript:;">下一页</a>
        </#if>
        
        <#if paging.isLastPage()>
        <a href="javascript:;">最后一页</a>
        <#else>
        <a href="${pageIndexUrl+paging.totalPage}">最后一页</a>
        </#if>
    </div>
    -->

    <#macro renderPagingPager pageFrom pageTo pageIndex>
			<#list pageFrom..pageTo as index>
	        	<#if pageIndex == index>
	        	<li class="active"><a href="javascript:void(0);">${pageIndex+1}</a></li>
	        	<#else>
	        	<li><a href="${pageIndexUrl+index}">${index+1}</a></li>
	        	</#if>
        	</#list>
	</#macro>
	
	<#assign total = paging.getTotalPage()> 
    <#assign pageIndex = paging.pageIndex> 
    
    <div>
		<div class="pagination pull-right">
		    <ul>
		    
		      <#if paging.hasPreviousPage()>
		      <li><a href="${pageIndexUrl+(paging.pageIndex-1)}">« 上一页</a></li>
		      <#else>
		      <li class="disabled"><a href="javascript:void(0);">« 上一页</a></li>
		      </#if>
		      
		      <#--
		      <#list 1..paging.getTotalPage() as pageNumber>
		      	<li ${(paging.pageIndex == pageNumber)?string("class="active"", "")}><a href="${(paging.pageIndex == pageNumber)?string("javascript:void(0);", (pageIndexUrl+pageNumber))}">${pageNumber}</a></li>
		      </#list>
		      -->
		      
			 <#if (total > 0) >
				<#if total lte 6>
			    	<@renderPagingPager pageFrom=0 pageTo=total-1 pageIndex=pageIndex/>
			    <#else>
			    	<#if pageIndex lte 3>
			        	<@renderPagingPager pageFrom=0 pageTo=5 pageIndex=pageIndex/>
			        <#else>
			            <#assign tmpTotalPage = (pageIndex+3)>
			            <#if tmpTotalPage gt total>
			            	<#assign tmpTotalPage = total>
			            </#if>
			        	<@renderPagingPager pageFrom=(tmpTotalPage-6) pageTo=tmpTotalPage-1 pageIndex=pageIndex/>
			    	</#if>
				</#if>
			</#if>
		      
		      <#--
		      <li class="active"><a href="#">1</a></li>
		      <li><a href="#">2</a></li>
		      <li><a href="#">3</a></li>
		      <li><a href="#">4</a></li>
		      <li class="bui-bar-item disabled bui-inline-block" aria-disabled="true"><a href="javascript:void(0);">...</a></li>
		      <li><a href="#">11</a></li>
		      -->
		      
		      <#if paging.hasNextPage()>
	            <li><a href="${pageIndexUrl+(paging.pageIndex+1)}">下一页 »</a></li>
	          <#else>
	            <li class="disabled"><a href="javascript:void(0);">下一页 »</a></li>
	          </#if>
		      
		    </ul>
		</div>
	</div>

</#if>
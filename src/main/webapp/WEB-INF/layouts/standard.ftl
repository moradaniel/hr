<#import "/WEB-INF/layouts/NavAdmin.ftl" as nav />
<#import "/WEB-INF/views/spring.ftl" as spring />
<#--
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> -->
<!DOCTYPE html>

<html ng-app="hrangularspring">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>Sistema de Creditos</title>
	<link rel="stylesheet" href="${requestContext.contextPath}/resources/styles/screen.css" type="text/css" media="screen" title="default" />
	
	<!--[if IE]>
	<link rel="stylesheet" media="all" type="text/css" href="${requestContext.contextPath}/resources/styles/pro_dropline_ie.css" />
	<![endif]-->
	
	<link rel="stylesheet" href="${requestContext.contextPath}/resources/styles/bootstrap-3.1.1/css/bootstrap.min.css">
	
	<!-- Optional theme -->
	<link rel="stylesheet" href="${requestContext.contextPath}/resources/styles/bootstrap-3.1.1/css/bootstrap-theme.min.css">
	
	<!-- Override bootstrap (loaded after bootstrap css files) -->
	<link rel="stylesheet" href="${requestContext.contextPath}/resources/styles/custom_bootstrap_3.css">
	
	<link rel="stylesheet" href="${requestContext.contextPath}/resources/javascript/angularjs/vendors/angular-growl-2/build/angular-growl.css">
	
	
		
	<link rel="stylesheet" type="text/css" media="screen" href='${requestContext.contextPath}/resources/javascript/jquery/jquery-ui/1.8.18/smoothness/jquery-ui-1.8.18.custom.css'/>
	<link rel="stylesheet" type="text/css" media="screen" href='${requestContext.contextPath}/resources/javascript/jquery/jqgrid/4.3.1/ui.jqgrid-4.3.1.css'/>

	
	<script type='text/javascript' src='${requestContext.contextPath}/resources/javascript/jquery/1.10/jquery-1.10.2.min.js'/></script>
	
	
	<script type='text/javascript' src='${requestContext.contextPath}/resources/javascript/jquery/jquery-ui/1.8.18/smoothness/jquery-ui-1.8.18.custom.min.js'/></script>
	
	
	<script type='text/javascript' src='${requestContext.contextPath}/resources/javascript/jquery/blockUI/2.64/jquery.blockUI.js'/></script>
	
	<script type='text/javascript' src='${requestContext.contextPath}/resources/javascript/jquery/utils/utils.js'/></script>
	

		
</head>
<body>
<!-- Start: page-top-outer -->
<div id="page-top-outer">    

<!-- Start: page-top -->
<div id="page-top">

	<!-- start logo -->
	<div id="logo">

		<a href=""><img src="${requestContext.contextPath}/resources/images/logo gobierno SJ 2.png" width="100" height="80" alt="" /></a>
	</div>
	<!-- end logo -->
	
	<div id="logo2">

		<a href=""><img src="${requestContext.contextPath}/resources/images/headerTituloSistemaCreditos.png" width="250" height="80" alt="" /></a>
	</div>
	
			<#if account?exists>
				<div style="float:right;">
					<div id="loggedInAs">
						<strong>Usuario: </strong>${account.name} <strong>Rol: </strong>
						<#assign isFirstRole=true>
						<#list roles as role >
						<#if isFirstRole>
							<#assign isFirstRole=false>
						<#else>
							,
						</#if>
							<@spring.message ("msg.roles."+role) />
						</#list>
					</div>
					
					<div id="div_departmentSearch" >
						<span class="searchDropDown">Reparticion: </span>
						<select style="color:black;" id="departmentId" name="departmentId" <#-- onchange="onSelect(this,'${currentURL?default('')}')" --> >
	
							<#if myDepartments?exists >
								<option value="" >-- Seleccione Reparticion --</option>
								<#list myDepartments?sort_by("code") as aDepartment>
									<#assign selected="">
									<#if currentDepartment?exists && (currentDepartment.id == aDepartment.id)>
										<#assign selected="selected">
									</#if>
									
									<#assign departmentName = aDepartment.name >
									<#if (departmentName?length >70) >
										<#assign departmentName = departmentName?substring(0,70) >
									</#if>
									<option value="${aDepartment.id}" ${selected?default("")} >${departmentName?html}</option>
								</#list>
							</#if>
						</select>
						
					    <!-- select id="makeListBox" 
  						    ng-model="make.selected" 
      						ng-options="make.id as make.name for make in makes"
      						ng-change="makeChanged(make.selected)"
      						class="form-control  input-sm"
      						>
  						</select -->
  						
  						<!-- span>{{SessionService.getCurrentDepartment().id}} - {{SessionService.getCurrentDepartment().name}}</span -->
  						
					</div>
				</div>
			</#if>
	

	
 	<!--  end top-search -->
 	<div class="clear"></div>

</div>
<!-- End: page-top -->

</div>
<!-- End: page-top-outer -->
	
<div class="clear">&nbsp;</div>
 
<!--  start nav-outer-repeat................................................................................................. START -->
<div class="nav-outer-repeat"> 
<!--  start nav-outer -->
<div class="nav-outer"> 

		<!-- start nav-right -->
		<div id="nav-right">
		
			<div class="nav-divider">&nbsp;</div>
			

			
			<#-- div class="showhide-account"><img src="${requestContext.contextPath}/resources/images/admin/shared/nav/nav_myaccount.gif" width="93" height="14" alt="" /></div>
			<div class="nav-divider">&nbsp;</div -->
			<a href="${requestContext.contextPath}/j_spring_security_logout" id="logout"><img src="${requestContext.contextPath}/resources/images/admin/shared/nav/nav_logout.gif" width="64" height="14" alt="" /></a>
			


			<div class="clear">&nbsp;</div>
		
			<!--  start account-content -->	
			<div class="account-content">
			<div class="account-drop-inner">
				<a href="" id="acc-settings">Settings</a>
				<div class="clear">&nbsp;</div>
				<div class="acc-line">&nbsp;</div>
				<a href="" id="acc-details">Personal details </a>
				<div class="clear">&nbsp;</div>
				<div class="acc-line">&nbsp;</div>
				<a href="" id="acc-project">Project details</a>
				<div class="clear">&nbsp;</div>
				<div class="acc-line">&nbsp;</div>
				<a href="" id="acc-inbox">Inbox</a>
				<div class="clear">&nbsp;</div>
				<div class="acc-line">&nbsp;</div>
				<a href="" id="acc-stats">Statistics</a> 
			</div>
			</div>
			<!--  end account-content -->
		
		</div>
		<!-- end nav-right -->


		<!--  start nav -->
		<div class="nav">
		<div class="table">
		
		
		<#list nav.navLinks as navLink>
				<#if navLink.doDisplay>
					<#assign ulFirstLevelNavigationStyleClass="select">
					<#assign divSecondLevelNavigationStyleClass="">
					<#if navLink_index == nav.tabNum>
						<#assign ulFirstLevelNavigationStyleClass="current">
						<#assign divSecondLevelNavigationStyleClass="show">
					</#if>
					
					<#assign tabURL = nav.firstDisplayableSubNav(navLink.subNav).URL />
						<ul class=${ulFirstLevelNavigationStyleClass}>
							<li><a href="${requestContext.contextPath}/${tabURL}"><b>${navLink.label}</b><!--[if IE 7]><!--></a><!--<![endif]-->
								<!--[if lte IE 6]><table><tr><td><![endif]-->
								<div class="select_sub ${divSecondLevelNavigationStyleClass}">
									<ul class="sub">
										<#list navLink.subNav as subLink>
											<#if subLink.doDisplay>
												<#assign subNavStyleId="">
												<#if subLink_index == nav.subNum>
													<#assign subNavStyleId="sub_show" />
													<#assign currentURL=subLink.URL />
												</#if>
												<li class="${subNavStyleId}"><a href="${requestContext.contextPath}/${subLink.URL}">${subLink.label}</a></li>
												<#if subLink_has_next>
												</#if>
											</#if>
										</#list>
																
									</ul>
								</div>
								<!--[if lte IE 6]></td></tr></table></a><![endif]-->
							</li>
						</ul>
					
					
				</#if>
		</#list>
				
		
		
		
		<#-- ul class="select"><li><a href="#nogo"><b>Dashboard</b><!--[if IE 7]><!--><#--/a --><!--<![endif]-->
		<!--[if lte IE 6]><table><tr><td><![endif]-->
		<#-- div class="select_sub">
			<ul class="sub">
				<li><a href="#nogo">Dashboard Details 1</a></li>
				<li><a href="#nogo">Dashboard Details 2</a></li>
				<li><a href="#nogo">Dashboard Details 3</a></li>
			</ul>
		</div -->
		<!--[if lte IE 6]></td></tr></table></a><![endif]-->
		<#--/li>
		</ul -->
		
		<#-- div class="nav-divider">&nbsp;</div -->
		                    
		<#-- ul class="current"><li><a href="#nogo"><b>Products</b><!--[if IE 7]></a>
		
		<div class="select_sub show">
			<ul class="sub">
				<li><a href="#nogo">View all products</a></li>
				<li class="sub_show"><a href="#nogo">Add product</a></li>
				<li><a href="#nogo">Delete products</a></li>
			</ul>
		</div>
		
		</li>
		</ul>
		
		<div class="nav-divider">&nbsp;</div -->
		
		<#-- ul class="select"><li><a href="#nogo"><b>Categories</b><!--[if IE 7]><!--><#-- /a --><!--<![endif]-->
		<!--[if lte IE 6]><table><tr><td><![endif]-->
		<#-- div class="select_sub">
			<ul class="sub">
				<li><a href="#nogo">Categories Details 1</a></li>
				<li><a href="#nogo">Categories Details 2</a></li>
				<li><a href="#nogo">Categories Details 3</a></li>
			</ul>
		</div>
		<!--[if lte IE 6]></td></tr></table></a><![endif]-->
		<#-- /li>
		</ul -->
		
		<#-- div class="nav-divider">&nbsp;</div>
		
		<ul class="select"><li><a href="#nogo"><b>Clients</b><!--[if IE 7]><!--><#-- /a --><!--<![endif]-->
		<!--[if lte IE 6]><table><tr><td><![endif]-->
		<#-- div class="select_sub">
			<ul class="sub">
				<li><a href="#nogo">Clients Details 1</a></li>
				<li><a href="#nogo">Clients Details 2</a></li>
				<li><a href="#nogo">Clients Details 3</a></li>
			 
			</ul>
		</div>
		<!--[if lte IE 6]></td></tr></table></a><![endif]-->
		<#-- /li>
		</ul -->
		
		<#-- div class="nav-divider">&nbsp;</div>
		
		<ul class="select"><li><a href="#nogo"><b>News</b><!--[if IE 7]><!--><#-- /a --><!--<![endif]-->
		<!--[if lte IE 6]><table><tr><td><![endif]-->
		<#-- div class="select_sub">
			<ul class="sub">
				<li><a href="#nogo">News details 1</a></li>
				<li><a href="#nogo">News details 2</a></li>
				<li><a href="#nogo">News details 3</a></li>
			</ul>
		</div>
		<!--[if lte IE 6]></td></tr></table></a><![endif]-->
		<#-- /li>
		</ul -->
		
		<div class="clear"></div>
		</div>
		<div class="clear"></div>
		</div>
		<!--  start nav -->

</div>
<div class="clear"></div>
<!--  start nav-outer -->
</div>
<!--  start nav-outer-repeat................................................... END -->

 <div class="clear"></div>
 
<!-- start content-outer ........................................................................................................................START -->
<div id="content-outer">

<!-- ----------------------------------------------           BEGIN           Notifications ----------------------------------------------------------------- -->
<div growl></div>

<div class="panel panel-default">
  <!-- Default panel contents -->
  <!--div class="panel-heading">Estado</div -->
  <div class="panel-body alert-panel-body">
    <div mc-messages></div>
  </div>
</div>
<!-- ----------------------------------------------           END           Notifications ----------------------------------------------------------------- -->

<!-- div style="overflow: scroll; height: 80px;" >
	<div mc-messages></div>
</div -->

<!-- start content -->
<div id="content">



	<!-- here is where the main content goes. -->
		<#if currentDepartment?exists || !nav.currPageRequiresDepartment >
			<@tiles.insertAttribute name="body" />
		<#else>
			Por favor seleccione Reparticion
		</#if>
		

</div>
<!--  end content -->
<div class="clear">&nbsp;</div>

</div>
<!--  end content-outer........................................................END -->

<div class="clear">&nbsp;</div>

<!-- start footer -->         
<div id="footer">
	<!--  start footer-left -->
	<div id="footer-left">
	
	Direccion Provincial de Informatica
	<!--  end footer-left -->
	<div class="clear">&nbsp;</div>
</div>
<!-- end footer -->

	

	
	

	<script type="text/javascript">

		
		function onSelect(selectBox,currPath)
		{
			var selectedCode = selectBox.value;
			
			if(selectedCode!==currentSelectedDepartmentId){
				var url = "${requestContext.contextPath}/departments/select?departmentId=" + selectedCode + "&currPath=" + currPath;

				window.location = url;
			}
		}

	</script>

<script type="text/javascript">
		
		function browserLessThanIE9(){
			   return (/MSIE ((5\\.5)|6|7|8)/.test(navigator.userAgent) );
		}
		
		function browserLessThanGecko1_9_2(){
			if(typeof(jQuery.browser.mozilla) === 'undefined' ){
				return false;
			}
			return jQuery.browser.version < '1.9.2';
		}
		
		
		$(function() {
			
			//no soportamos ie<9
			if(browserLessThanIE9()==true){
				window.location="notSupportedBrowser.html"; //URL to redirect to.
			}
		});
		
		
		var currentSelectedDepartmentId;
		
		var prefixContextPath = '${requestContext.contextPath}';
		
		$(function() {
			
			//if the user has access to only one department and it is not yet selected, then auto-select it
			if( $("#departmentId option").length == 2 && ($("#departmentId option:eq(1)").prop('selected')!==true)){
				$("#departmentId option:eq(1)").prop("selected","selected");
				onSelect($("#departmentId option:selected").get(0),'${currentURL?default('')}');
			}
			
			//AddIncSearch does not work for ie<8 or firefox < 3.6
			/*if(browserLessThanIE8()==false && browserLessThanGecko1_9_2()==false){
				//add search capability to department combo
				$("#departmentId").AddIncSearch(
						{
					        maxListSize: 200,
					        maxMultiMatch: 150,
					        warnMultiMatch: 'top {0} matches ...',
					        warnNoMatch: 'no matches ...'
						}
						);
			}*/
			
			//we store the current selected department for comparing in the onSelect function
			currentSelectedDepartmentId=$("#departmentId").val();
			
					
			
			$('#departmentId').bind('change', function() {
				onSelect(this,'${currentURL?default('')}');
			});
			
			
		});

	</script>
	

	<script type="text/javascript" src="${requestContext.contextPath}/resources/styles/bootstrap-3.1.1/js/bootstrap.min.js"></script>
	
	<script type="text/javascript" src='${requestContext.contextPath}/resources/javascript/lodash.js'/></script>
	
	<script type="text/javascript" src='${requestContext.contextPath}/resources/javascript/angularjs/angular-1.2.16/angular.js'/></script>
	<!-- script type="text/javascript" src='${requestContext.contextPath}/resources/javascript/angularjs/angular-1.2.16/i18n/angular-locale_es-ar.js'/></script -->
	
	<script type="text/javascript" src='${requestContext.contextPath}/resources/javascript/angularjs/vendors/ui-bootstrap/ui-bootstrap-tpls-0.11.0.min.js'/></script>
	
	
	
	<script type="text/javascript" src='${requestContext.contextPath}/resources/javascript/angularjs/angular-1.2.16/angular-resource.js'/></script>
	<script type="text/javascript" src='${requestContext.contextPath}/resources/javascript/angularjs/angular-1.2.16/angular-route.js'/></script>
		
	<!-- script type="text/javascript" src='${requestContext.contextPath}/resources/javascript/angularjs/vendors/ng-table-0.3.1/ng-table.js'/></script -->
	
	<script type="text/javascript" src='${requestContext.contextPath}/resources/javascript/angularjs/vendors/restangular/restangular.js'/></script>
		
	<script type="text/javascript" src='${requestContext.contextPath}/resources/javascript/angularjs/vendors/angular-growl-2/build/angular-growl.js'/></script>		
	<script type="text/javascript" src='${requestContext.contextPath}/resources/javascript/angularjs/vendors/message-center/message-center.js'/></script>
	
	<script type="text/javascript" src='${requestContext.contextPath}/resources/javascript/app/common/services/notifications.js'/></script>
	<script type="text/javascript" src='${requestContext.contextPath}/resources/javascript/app/common/services/session.js'/></script>
	<script type="text/javascript" src='${requestContext.contextPath}/resources/javascript/app/common/services/rest.api.js'/></script>
	
	<script type="text/javascript" src='${requestContext.contextPath}/resources/javascript/app/admin/employments/admin-employment-editor.js'/></script>
	
	<script type="text/javascript" src='${requestContext.contextPath}/resources/javascript/app/admin/employments/admin-employments.js'/></script>
	<!-- script type="text/javascript" src='${requestContext.contextPath}/resources/javascript/app/admin/employments/admin-employments2.js'/></script -->
	
	<script type="text/javascript" src='${requestContext.contextPath}/resources/javascript/app/admin/creditsEntries/admin-creditsEntries.js'/></script>
		
			
	<script type="text/javascript" src="${requestContext.contextPath}/resources/javascript/app/app.js"/></script>
		
	<script type="text/javascript" src="${requestContext.contextPath}/resources/javascript/app/common/directives/directives.js"/></script>

		
</body>
</html>
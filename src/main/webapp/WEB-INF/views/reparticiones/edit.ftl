
<#import "/WEB-INF/views/spring.ftl" as spring />
<#-- import "/WEB-INF/views/creditosUtils.ftl" as creditosUtils / -->


<div id="employmentResults">

	<#-- div id="table-actions" -->
		<#-- p class="buttoniseUs" -->

		
			<#-- if creditosUtils.canIngresarAscenderBorrarMovimientoPorUsuario(account) -->
				<#if canAccountProposeNewEmployment>
					<a href="${requestContext.contextPath}/employments/proposeNewEmploymentForm" class="btn btn-default btn-xs" role="button">Ingresar Agente</a>
				</#if>
			<#-- /#if >
		</p>
	</div -->
			
<#-- if !activeEmployments?has_content>
	<p>No se encontraron Agentes. Por favor cambie el criterio de busqueda.</p>
<#else -->

<#--     --------------------- Angularjs                        -------------------             -->

	<#-- div ng-controller="employmentsTableCtrl">
	  <button ng-click="tableParams.reload()" >Reload</button>
	    <button ng-click="tableParams.sorting({})" >Clear sorting</button>
	    <div class="clearfix"></div>
	    <div loading-container="tableParams.settings().$loading">
	      <table ng-table="tableParams" show-filter="true" >
	        <tbody>
	          <tr ng-repeat="employment in $data">
	            <td data-title="'Name'" filter="{ 'name': 'text' }" sortable="name">
	                    {{employment.person.apellidoNombre}}
	                    {{$loading}}
	                </td>
	            <td data-title="'Age'" sortable="age">
	                    {{employment.person.cuil}}
	                </td>
	                
	            <td data-title="'Status'" sortable="status">
	                    {{employment.status}}
	                </td>
	          </tr>
	        </tbody>
	      </table>
	    </div>
   </div -->

<div ng-controller="GridCtrl">
<h1>Reparticion:  ${reparticion.nombre}</h1>
<!-- h1>Reparticion:  {{SessionService.getCurrentDepartment().name}}</h1 -->
<!-- input type="text" name="input" ng-model="SessionService.getCurrentDepartment().name" size="100" -->

<h3>Se encontraron ({{employmentsCount}}) empleo/s</h3>
<table class="table table-hover table-condensed">
  <thead>
    <tr>
      <th ng-repeat="header in headers" class="{{header.klass}}">
        <sort-by onsort="onSort" sortdir="filterCriteria.sortDir" sortedby="filterCriteria.sortedBy" sortvalue="{{ header.value }}">{{ header.title }}</sort-by>
      </th>
      <th>Accion</th>
      
    </tr>
  </thead>
  <tbody>
  <tr>
    <td><input on-enter-blur on-blur-change="filterResult()" ng-model="filterCriteria.apellidoNombre" type="text" class="form-control"  /></td>
    <td><input on-enter-blur on-blur-change="filterResult()" ng-model="filterCriteria.cuil" type="text" class="form-control"  /></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <!-- td><input on-enter-blur on-blur-change="filterResult()" ng-model="filterCriteria.email" type="text" /></td -->
    <!-- td><input on-enter-blur on-blur-change="filterResult()" ng-model="filterCriteria.city" type="text" /></td -->
    <td>
      <select ng-change="filterResult()" ng-model="filterCriteria.employmentstatus" ng-options="status for status in employmentstatuses" class="form-control  input-sm" >
        <!-- option value=""> </option -->
      </select>
    </td>
    
    <td></td>
  </tr>
  <tr ng-repeat="employmentVO in employmentsVOs">
    <!-- td><a href="/#/customer{{customer.id}}">{{employment.id}}</a></td -->
    <td>{{employmentVO.employment.person.apellidoNombre}}</td>
    <td>{{employmentVO.employment.person.cuil}}</td>
    <td>{{employmentVO.employment.category.code}}</td>
    <td>
    	<span ng-if="employmentVO.employment.occupationalGroup" class="small">
				{{employmentVO.employment.occupationalGroup.name}} - {{employmentVO.employment.occupationalGroup.code}}
		</span>
		<span ng-if="!employmentVO.employment.occupationalGroup">
			<span class="error-text">Error Falta asignar Tramo</span>
		</span>
	</td>				
	
	<td>
    	<span ng-if="employmentVO.employment.occupationalGroup && employmentVO.employment.occupationalGroup.parentOccupationalGroup">
				{{employmentVO.employment.occupationalGroup.parentOccupationalGroup.name}}
		</span>
	</td>		

    <td>{{employmentVO.employment.centroSector.codigoCentro}}</td>
    <td>{{employmentVO.employment.centroSector.nombreCentro}}</td>
    <td>{{employmentVO.employment.centroSector.codigoSector}}</td>
    <td>{{employmentVO.employment.centroSector.nombreSector}}</td>
							
	<td>						
		<span ng-repeat="note in employmentVO.notes">
          {{ note }}
         </span>
         <br>
	</td>
							
    <td>{{employmentVO.employment.status}}</td>

	<td>
		<span ng-if="employmentVO.employment.occupationalGroup">

			<a ng-if="employmentVO.canBePromoted" ng-href="/creditos/employments/{{employmentVO.employment.id}}/promotePerson" class="btn btn-default btn-xs" role="button">Ascender</a>
		
		</span>
		
		<button ng-if="employmentVO.canPersonBeModified" role='button' class="btn btn-default btn-xs" ng-click="editEmployment(employmentVO.employment)" data-toggle="modal" href="#editModal">Modificar</button>
							
		<a ng-if="employmentVO.canBeDeactivated" ng-href="/creditos/employments/{{employmentVO.employment.id}}/deactivatePerson" class="btn btn-default btn-xs" role="button">Dar de Baja</a>
		<a ng-if="employmentVO.canUndoDeactivation" ng-href="/creditos/employments/{{employmentVO.employment.id}}/undoDeactivatePerson" class="btn btn-default btn-xs" role="button">Deshacer Baja</a>
	</td>

  </tr>
  </tbody>
</table>
<div ng-show="employmentsCount == 0">
  <h3>No se encontraron empleos con este criterio de busqueda</h3>
</div>
<div ng-show="totalPages > 1" class="align-center">
  <pagination num-pages="totalPages" current-page="filterCriteria.pageNumber" max-size="10" class="" boundary-links="true"
            on-select-page="selectPage(page)"></pagination>
</div>


<#--     --------------------- Angularjs                        -------------------             -->

<!-- div class="modal hide fade"  tabindex="-1" ng-include="'/creditos/resources/templates/employment-detail.html'" id="editModal" role='dialog'></div -->

<!-- ng-include="'/creditos/resources/templates/employment-detail.html'" -->
  
  <div class="modal fade" ng-include="'/creditos/resources/javascript/app/admin/employments/employment-detail.html'" id="editModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" >
 	 <!-- template will be includede here -->
  </div><!-- /.modal -->
  	 
  	 

<!-- Button trigger modal 
  <a data-toggle="modal" href="#myModal" class="btn btn-primary btn-lg">Launch demo modal</a>
  

  <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
          <h4 class="modal-title">Modal title</h4>
        </div>
        <div class="modal-body">
          ...
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
          <button type="button" class="btn btn-primary">Save changes</button>
        </div>
      </div>
    </div>
  </div>

</div -->


<#--

	<#assign empleosUrl= requestContext.contextPath+"/employments">

		<!--  start page-heading ->
	<div id="page-heading">
		<h1>Reparticion:  ${reparticion.nombre}</h1>
	</div>
	<!-- end page-heading ->
	
		<table border="0" width="100%" cellpadding="0" cellspacing="0" id="content-table">
	<tr>
		<th rowspan="3" class="sized"><img src="${requestContext.contextPath}/resources/images/admin/shared/side_shadowleft.jpg" width="20" height="300" alt="" /></th>
		<th class="topleft"></th>
		<td id="tbl-border-top">&nbsp;</td>
		<th class="topright"></th>
		<th rowspan="3" class="sized"><img src="${requestContext.contextPath}/resources/images/admin/shared/side_shadowright.jpg" width="20" height="300" alt="" /></th>
	</tr>
	<tr>
		<td id="tbl-border-left"></td>
		<td>
		<!--  start content-table-inner ...................................................................... START ->
		<div id="content-table-inner">
	
			<!--  start table-content  ->
			<div id="table-content">
			
				<table border="0" width="100%" cellpadding="0" cellspacing="0" class="product-table">
					<tr>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Apellido y Nombre</a>	</th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">CUIL</a>	</th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Condicion Agente</a>	</th>
						<th class="table-header-repeat line-left "><a href="">Categoria</a></th>
						<th class="table-header-repeat line-left "><a href="">Tramo</a></th>
						<th class="table-header-repeat line-left "><a href="">Agrupamiento</a></th>
						<th class="table-header-repeat line-left "><a href="">Codigo Centro</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Nombre Centro</a></th>
						<th class="table-header-repeat line-left "><a href="">Codigo Sector</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Nombre Sector</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Accion</a></th>
	
					</tr>
									

				<#list activeEmployments as activeEmployment>
					<#assign trStyle= "" >
					<#if (activeEmployment_index % 2) == 0>
				    	<#assign trStyle= "alternate-row" >
				    </#if>	
				    
				    	<tr class="${trStyle}">
							<td>${activeEmployment.employment.person.apellidoNombre}</td>
							<td>${activeEmployment.employment.person.cuil?default("")}</td>
							<td>${activeEmployment.employment.person.condicion?default("")}</td>
							<td>${activeEmployment.employment.category.code}</td>
							
							
							<td>
								<#if activeEmployment.employment.occupationalGroup?exists >
									${activeEmployment.employment.occupationalGroup.name} - ${activeEmployment.employment.occupationalGroup.code}
								<#else>
									<span class="error-text">Error Falta asignar Tramo</span>								
								</#if>
								
							</td>
							
							<td>
								<#if activeEmployment.employment.occupationalGroup?exists && activeEmployment.employment.occupationalGroup.parentOccupationalGroup?exists>
									${activeEmployment.employment.occupationalGroup.parentOccupationalGroup.name}
								</#if>
							</td>
							
							<td>${activeEmployment.employment.centroSector.codigoCentro}</td>
							<td>${activeEmployment.employment.centroSector.nombreCentro}</td>
							<td>${activeEmployment.employment.centroSector.codigoSector}</td>
							<td>${activeEmployment.employment.centroSector.nombreSector}</td>
							<td>
							
							<#if activeEmployment.employment.occupationalGroup?exists >
								<#if creditosUtils.canIngresarAscenderBorrarMovimientoPorUsuario(account)>
									<#if activeEmployment.canBePromoted >
										<a href="${empleosUrl}/${activeEmployment.employment.id}/promotePerson" class="ajaxLink">Ascender Agente</a>
									</#if>
								</#if>
							</#if>
								<#if activeEmployment.canBeDeactivated >
									<a href="${empleosUrl}/${activeEmployment.employment.id}/deactivatePerson" class="ajaxLink">Dar de Baja</a>
								</#if>
							
							</td>
						</tr>
				    
				
			</#list>
			<#if !activeEmployments?has_content>
				<tr>
					<td colspan="5">No se encontraron agentes</td>
				</tr>
			</#if>
			
				</table>
				<!--  end product-table................................... -> 
			
			
			
			</div>
			<!--  end content-table  ->
			
				<div class="clear"></div>
		 
		</div>
		<!--  end content-table-inner ............................................END  ->
		</td>
		<td id="tbl-border-right"></td>
	</tr>
	<tr>
		<th class="sized bottomleft"></th>
		<td id="tbl-border-bottom">&nbsp;</td>
		<th class="sized bottomright"></th>
	</tr>
	</table>
	<div class="clear">&nbsp;</div -->
	
<#-- /#if -->





</div>	

<#-- script>
	$(function() {
		$("input:submit, a, button", ".buttoniseUs").button();
	});
</script -->

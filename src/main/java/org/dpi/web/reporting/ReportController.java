package org.dpi.web.reporting;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;

import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriodQueryFilter;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.empleo.EmpleoQueryFilter;
import org.dpi.empleo.EstadoEmpleo;
import org.dpi.movimientoCreditos.MovimientoCreditos;
import org.dpi.movimientoCreditos.MovimientoCreditosAscensoVO;
import org.dpi.movimientoCreditos.MovimientoCreditosQueryFilter;
import org.dpi.movimientoCreditos.MovimientoCreditosService;
import org.dpi.movimientoCreditos.TipoMovimientoCreditos;
import org.dpi.reparticion.Reparticion;
import org.dpi.reparticion.ReparticionController;
import org.dpi.web.reporting.dto.GenericReportRecord;
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Handles and retrieves download request
 */
@Controller
@RequestMapping("/reports")
public class ReportController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="downloadService")
	private DownloadService downloadService;
	
	@Resource(name = "movimientoCreditosService")
	private MovimientoCreditosService movimientoCreditosService;
	
	@Resource(name = "administradorCreditosService")
	private AdministradorCreditosService administradorCreditosService;
	
	@Resource(name = "creditsPeriodService")
	private CreditsPeriodService creditsPeriodService;
	
	/**
	 * Handles and retrieves the download page
	 * 
	 * @return the name of the JSP page
	 */
    @RequestMapping(value = "/buildSetupPage", method = RequestMethod.GET)
    public String getDownloadPage() {
    	logger.debug("Received request to show Report Setup page");
    
    	// Do your work here. Whatever you like
    	// i.e call a custom service to do your business
    	// Prepare a model to be used by the JSP page
    	
    	// This will resolve to /WEB-INF/jsp/downloadpage.jsp
    	return "reports/setup";
	}
 
    /**
     * Retrieves the download file
     * 
     * @return
     */
    @RequestMapping(value = "/creditos", method = RequestMethod.POST)
    public void doCreditosReport(HttpServletRequest request, 
    							HttpServletResponse response
    						/*, ReportParameters parameters*/
    	) throws ServletException, IOException,
		ClassNotFoundException, SQLException, JRException {
    	
		logger.debug("Received request to download creditos report");
		

		// Call DownloadService to do the actual report processing
		//downloadService.downloadPdf(response);
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		// get the current reparticion in the session
		final Reparticion reparticion = ReparticionController.getCurrentReparticion(request);

		//get movimientos de ascenso
		EmpleoQueryFilter empleoQueryFilter = new EmpleoQueryFilter();
		empleoQueryFilter.setReparticionId(reparticion.getId().toString());
		//todos los estados
		empleoQueryFilter.setEstadosEmpleo(CollectionUtils.arrayToList(EstadoEmpleo.values()));
		MovimientoCreditosQueryFilter movimientoCreditosQueryFilter = new MovimientoCreditosQueryFilter();
		movimientoCreditosQueryFilter.setEmpleoQueryFilter(empleoQueryFilter);
		movimientoCreditosQueryFilter.addTipoMovimientoCreditos(TipoMovimientoCreditos.AscensoAgente);
			

		List<MovimientoCreditos> movimientoCreditosReparticion = movimientoCreditosService.find(movimientoCreditosQueryFilter);

		Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Account currenUser = (Account)accountObj;
		List<MovimientoCreditosAscensoVO> movimientosCreditosAscensosReparticionVO = movimientoCreditosService.buildMovimientoCreditosVO(movimientoCreditosReparticion,currenUser);
		
			
		
		params.put("MOVIMIENTOS_ASCENSO",  new MovimientosAscensoReportDataSource(getReportDataMovimientosAscenso(movimientosCreditosAscensosReparticionVO)));
		
		//get movimientos de Ingreso
		empleoQueryFilter = new EmpleoQueryFilter();
		empleoQueryFilter.setReparticionId(reparticion.getId().toString());
		//todos los estados
		empleoQueryFilter.setEstadosEmpleo(CollectionUtils.arrayToList(EstadoEmpleo.values()));
		movimientoCreditosQueryFilter = new MovimientoCreditosQueryFilter();
		movimientoCreditosQueryFilter.setEmpleoQueryFilter(empleoQueryFilter);
		movimientoCreditosQueryFilter.addTipoMovimientoCreditos(TipoMovimientoCreditos.IngresoAgente);
		
		List<MovimientoCreditos> movimientoCreditosIngresosReparticion = movimientoCreditosService.find(movimientoCreditosQueryFilter);

		int cantidadMovimientosIngreso = 0;
		if(!CollectionUtils.isEmpty(movimientoCreditosIngresosReparticion)){
			cantidadMovimientosIngreso=movimientoCreditosIngresosReparticion.size();
		}
		
		params.put("CANTIDAD_MOVIMIENTOS_INGRESO",  cantidadMovimientosIngreso);
		
		params.put("MOVIMIENTOS_INGRESO",  new MovimientosIngresoReportDataSource(getReportDataMovimientosIngreso(movimientoCreditosIngresosReparticion)));
		
		
		//obtener periodo actual
		CreditsPeriodQueryFilter creditsPeriodQueryFilter = new CreditsPeriodQueryFilter();
		creditsPeriodQueryFilter.setName("2013");//TODO get year from current date
		
		List<CreditsPeriod> currentCreditsPeriods = creditsPeriodService.find(creditsPeriodQueryFilter);
		CreditsPeriod currentCreditsPeriod = currentCreditsPeriods.get(0);
		Long creditosDisponiblesAlInicioDelPeriodo =this.administradorCreditosService.getCreditosDisponiblesAlInicioDelPeriodo(currentCreditsPeriod,reparticion.getId());

		Long creditosPorIngresosOAscensosSolicitados = this.administradorCreditosService.getCreditosPorIngresosOAscensosSolicitados(currentCreditsPeriod, reparticion.getId());

		Long creditosDisponibles = this.administradorCreditosService.getCreditosDisponiblesSegunSolicitado(currentCreditsPeriod,reparticion.getId());
	
		
		params.put("CANTIDAD_CREDITOS_DISPONIBLES_INICIO_PROCESO",creditosDisponiblesAlInicioDelPeriodo);
		params.put("CANTIDAD_CREDITOS_UTILIZADOS",creditosPorIngresosOAscensosSolicitados);
		params.put("CANTIDAD_CREDITOS_DISPONIBLES_AL_FINAL_DELS_PERIODO",creditosDisponibles);
		
		
		downloadService.download("pdf", params, response);
	}
    
    
	private List<GenericReportRecord> getReportDataMovimientosAscenso(List<MovimientoCreditosAscensoVO> movimientosCreditosAscensosReparticionVO) {

		final List<GenericReportRecord> records = new ArrayList<GenericReportRecord>();
		for(MovimientoCreditosAscensoVO movimientoCreditosAscensosReparticionVO:movimientosCreditosAscensosReparticionVO){
			GenericReportRecord record = new GenericReportRecord();
			record.setValue(MovimientosAscensoReportDataSource.ReportFieldID.AGENTE_APELLIDO_NOMBRE.name(),movimientoCreditosAscensosReparticionVO.getMovimientoCreditos().getEmpleo().getAgente().getApellidoNombre());
			record.setValue(MovimientosAscensoReportDataSource.ReportFieldID.AGENTE_CUIL.name(),movimientoCreditosAscensosReparticionVO.getMovimientoCreditos().getEmpleo().getAgente().getCuil());
			record.setValue(MovimientosAscensoReportDataSource.ReportFieldID.AGENTE_CATEGORIA_ACTUAL.name(),movimientoCreditosAscensosReparticionVO.getCategoriaActual());
			record.setValue(MovimientosAscensoReportDataSource.ReportFieldID.AGENTE_CATEGORIA_PROPUESTA.name(),movimientoCreditosAscensosReparticionVO.getCategoriaPropuesta());
			records.add(record);
		}
		
		return records;
	}
	
	
	private List<GenericReportRecord> getReportDataMovimientosIngreso(List<MovimientoCreditos> movimientoCreditosIngresosReparticion) {

		final List<GenericReportRecord> records = new ArrayList<GenericReportRecord>();
		for(MovimientoCreditos movimientoCreditosIngreso:movimientoCreditosIngresosReparticion){
			GenericReportRecord record = new GenericReportRecord();
			record.setValue(MovimientosIngresoReportDataSource.ReportFieldID.AGENTE_NUEVO_PERFIL.name(),"");
			record.setValue(MovimientosIngresoReportDataSource.ReportFieldID.AGENTE_NUEVO_AGRUPAMIENTO.name(),"");
			record.setValue(MovimientosIngresoReportDataSource.ReportFieldID.AGENTE_NUEVO_CATEGORIA_PROPUESTA.name(),movimientoCreditosIngreso.getEmpleo().getCategoria().getCodigo());
			
			records.add(record);
		}
		
		return records;
	}


}

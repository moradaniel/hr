package org.dpi.creditsPeriod;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.dpi.agente.AgenteService;
import org.dpi.categoria.CategoriaService;
import org.dpi.centroSector.CentroSectorService;
import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import biz.janux.calendar.DateRange;



public class CreditsPeriodServiceImpl implements CreditsPeriodService
{
	//private static Log log = LogFactory.getLog(EmpleoServiceImpl.class);
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	final DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	
	private final CreditsPeriodDao creditsPeriodDao;
	
	@Resource(name = "administradorCreditosService")
	private AdministradorCreditosService administradorCreditosService;
	
	@Resource(name = "categoriaService")
	private CategoriaService categoriaService;
	
	@Resource(name = "agenteService")
	private AgenteService agenteService;
	
	@Resource(name = "centroSectorService")
	private CentroSectorService centroSectorService;
	

	private ApplicationContext applicationContext;
	
	public CreditsPeriodServiceImpl(final CreditsPeriodDao creditsPeriodDao) {
		this.creditsPeriodDao = creditsPeriodDao;
	}

	
	public List<CreditsPeriod> find(CreditsPeriodQueryFilter creditsPeriodQueryFilter){
		
		return creditsPeriodDao.find(creditsPeriodQueryFilter);
	}

	
	public void save(final CreditsPeriod creditsPeriod) 
	{
		creditsPeriodDao.save(creditsPeriod);

	}
	
	public void saveOrUpdate(final CreditsPeriod creditsPeriod) 
	{
		
		if (creditsPeriod.getId() != null) { // it is an update
			creditsPeriodDao.merge(creditsPeriod);
			} else { // you are saving a new one
				creditsPeriodDao.saveOrUpdate(creditsPeriod);
			}
		
	//	hotelDao.removeOrphanPolicies(aHotel);
	}
	
	public void delete(CreditsPeriod creditsPeriod){
		creditsPeriodDao.delete(creditsPeriod);
	}


	public CreditsPeriodDao getCreditsPeriodDao()
	{
		return creditsPeriodDao;
	}

	
	
	public List<CreditsPeriod> findAll(){
		return this.creditsPeriodDao.findAll();
	}
	
	



	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}




	
	public AdministradorCreditosService getAdministradorCreditosService() {
		return administradorCreditosService;
	}


	public void setAdministradorCreditosService(
			AdministradorCreditosService administradorCreditosService) {
		this.administradorCreditosService = administradorCreditosService;
	}

	public CategoriaService getCategoriaService() {
		return categoriaService;
	}


	public void setCategoriaService(CategoriaService categoriaService) {
		this.categoriaService = categoriaService;
	}



	
	public AgenteService getAgenteService() {
		return agenteService;
	}


	public void setAgenteService(AgenteService agenteService) {
		this.agenteService = agenteService;
	}

	public CentroSectorService getCentroSectorService() {
		return centroSectorService;
	}


	public void setCentroSectorService(CentroSectorService centroSectorService) {
		this.centroSectorService = centroSectorService;
	}


	@Override
	public DateRange getDateRangeForEjercicioAnual(int year) {
		Date startDate	 = null;
		Date endDate	 = null;

		try{
			startDate	 = format.parse("01/07/"+ year + " 00:00:00.000");
			endDate	 = format.parse("30/06/"+ year + " 23:59:59.999");
		}catch(ParseException ex){
			log.error(ex.getMessage());
			throw new RuntimeException(ex);
		}
		DateRange dateRange = new DateRange(startDate, endDate);
		return dateRange;
	}


	@Override
	public boolean isDateWithinEjercicioAnual(Date date, int year) {
		DateRange dateRange =  getDateRangeForEjercicioAnual(year);
		return dateRange.contains(date);
	}


	@Override
	public int getCurrentCreditsPeriodYear() {
		final Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.YEAR);
	}
	
	@Override
	public CreditsPeriod getCurrentCreditsPeriod(){
		CreditsPeriodQueryFilter creditsPeriodQueryFilter = new CreditsPeriodQueryFilter();
		creditsPeriodQueryFilter.setName("2013");//TODO get year from current date or better the ACTIVE period
		
		//obtener periodo actual
		List<CreditsPeriod> currentCreditsPeriods = find(creditsPeriodQueryFilter);
		CreditsPeriod currentCreditsPeriod = currentCreditsPeriods.get(0);
		return currentCreditsPeriod;
	}
	
}

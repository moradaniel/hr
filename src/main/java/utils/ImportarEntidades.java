package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dpi.agente.Agente;
import org.dpi.agente.AgenteImpl;
import org.dpi.agente.AgenteService;
import org.dpi.categoria.Categoria;
import org.dpi.categoria.CategoriaImpl;
import org.dpi.categoria.CategoriaService;
import org.dpi.centroSector.CentroSector;
import org.dpi.centroSector.CentroSectorImpl;
import org.dpi.centroSector.CentroSectorService;
import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
import org.dpi.empleo.Empleo;
import org.dpi.empleo.EmpleoImpl;
import org.dpi.empleo.EmpleoQueryFilter;
import org.dpi.empleo.EmpleoService;
import org.dpi.empleo.EstadoEmpleo;
import org.dpi.movimientoCreditos.MovimientoCreditosImpl;
import org.dpi.movimientoCreditos.TipoMovimientoCreditos;
import org.dpi.reparticion.Reparticion;
import org.dpi.reparticion.ReparticionImpl;
import org.dpi.reparticion.ReparticionService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class ImportarEntidades {
	
	ReparticionService reparticionService;
	
	CentroSectorService centroSectorService;
	
	AgenteService agenteService;
	
	CategoriaService categoriaService;
	
	EmpleoService empleoService;

	AdministradorCreditosService administradorCreditosService;
	
	NamedParameterJdbcTemplate dpiJdbcTemplate;
	
	TransactionTemplate transactionTemplate;
	
	public static void main( String[] args )
    {
		String[] contextFiles = new String[] {
				"classpath:spring/root-context.xml",
				"classpath:GenericDaoContext.xml",
				"classpath:SecurityContextGeneric.xml"
		};
		
		ApplicationContext context = new ClassPathXmlApplicationContext(contextFiles);

		final ImportarEntidades importadorEntidades = new ImportarEntidades();
		importadorEntidades.setDpiJdbcTemplate((NamedParameterJdbcTemplate)context.getBean("dpiJdbcTemplate"));
		importadorEntidades.setReparticionService((ReparticionService)context.getBean("reparticionService"));
		importadorEntidades.setCentroSectorService((CentroSectorService)context.getBean("centroSectorService"));
		importadorEntidades.setAgenteService((AgenteService)context.getBean("agenteService"));
		importadorEntidades.setCategoriaService((CategoriaService)context.getBean("categoriaService"));
		importadorEntidades.setEmpleoService((EmpleoService)context.getBean("empleoService"));
		
		importadorEntidades.setAdministradorCreditosService((AdministradorCreditosService)context.getBean("administradorCreditosService"));
		
		importadorEntidades.setTransactionTemplate((TransactionTemplate)context.getBean("transactionTemplate"));
		
		
	    importadorEntidades.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status){
				importadorEntidades.procesarImportacion();
				
			}
		});
		
		//passwordsTool.setAccountService((AccountDao)context.getBean("accountDao"));
		//passwordsTool.setMycrsJdbcTemplate((SimpleJdbcTemplate)context.getBean("mycrsJdbcTemplate"));
		
		
		
		/*SortedSet<Account> accounts = passwordsTool.getAccountService().loadAllAccounts(false);
		
		for(Account account:accounts){
			
			String unencryptedPassword = account.get
			accounts
		}*/



    }

    public void setAdministradorCreditosService(
			AdministradorCreditosService administradorCreditosService) {
		this.administradorCreditosService=administradorCreditosService;
		
	}

	
	public AdministradorCreditosService getAdministradorCreditosService() {
		return this.administradorCreditosService;
	}

	
	/**
     * 
     */
	private void procesarImportacion() throws RuntimeException{
		
		String query = "SELECT	* " +
					" FROM	CREDITOS.TODO " +
					" WHERE	(CREDITOS.TODO.ESCALAFON = '6' or CREDITOS.TODO.ESCALAFON = '2')" +
					"		AND REPARTICION<>'#N/A'" ; //solo escalafon general 02 y 06

		MapSqlParameterSource parameters = new MapSqlParameterSource();

		List<Map<String, Object>> rows = getDpiJdbcTemplate().queryForList(query, parameters);
		
		for (Map<String, Object> row: rows) {
			importarCategorias(row);
			importarAgentes(row);
			importarReparticion(row);
			//importarCentroSectores(row);
			importarRelacionEntreReparticionyCentroSector(row);
			importarEmpleos(row);
			

		}
		
	}

	private void importarRelacionEntreReparticionyCentroSector(
			Map<String, Object> row) {
		String nombreReparticion = (String)row.get("REPARTICION");
		if(nombreReparticion!=null){
			Reparticion reparticion = reparticionService.findByNombre(nombreReparticion);
			if(reparticion!=null){
				String codigoCentro = (String)row.get("CENTRO");
				String codigoSector = (String)row.get("SECTOR");
				if(codigoCentro!=null && codigoSector!=null){
					
					CentroSector centroSector = centroSectorService.findByCodigoCentroCodigoSector(codigoCentro,codigoSector );
					if(centroSector==null){
						//si no existe el centroSector todavia, crearlo
						if(codigoCentro!=null && codigoSector!=null){
								centroSector = new CentroSectorImpl();
								String nombreCentro = (String)row.get("NOMBRECENTRO");
								String nombreSector = (String)row.get("NOMBRESECTOR");
								centroSector.setCodigoCentro(codigoCentro);
								centroSector.setCodigoSector(codigoSector);
								centroSector.setNombreCentro(nombreCentro);
								centroSector.setNombreSector(nombreSector);
						}
						
					}
					
					reparticion.addCentroSector(centroSector);
					centroSector.setReparticion(reparticion);
					centroSectorService.saveOrUpdate(centroSector);
					reparticionService.saveOrUpdate(reparticion);

				}
			}
		}

	}

	private void importarReparticion(Map<String, Object> row) {
		String nombreReparticion = (String)row.get("REPARTICION");
		if(nombreReparticion!=null){
			Reparticion reparticion = reparticionService.findByNombre(nombreReparticion);
			//si no existe todavia, crearlo
			if(reparticion==null){
				ReparticionImpl newReparticion = new ReparticionImpl();
				newReparticion.setNombre(nombreReparticion);
				reparticionService.saveOrUpdate(newReparticion);
				
			}
		}
	}
	
	
	private void importarCentroSectores(Map<String, Object> row) {
		String codigoCentro = (String)row.get("CENTRO");
		String codigoSector = (String)row.get("SECTOR");
		String nombreCentro = (String)row.get("NOMBRECENTRO");
		String nombreSector = (String)row.get("NOMBRESECTOR");
		
		if(codigoCentro!=null && codigoSector!=null){
			CentroSector centroSector = centroSectorService.findByCodigoCentroCodigoSector(codigoCentro,codigoSector );
			//si no existe todavia, crearlo
			if(centroSector==null){
				CentroSectorImpl newCentroSector = new CentroSectorImpl();
				newCentroSector.setCodigoCentro(codigoCentro);
				newCentroSector.setCodigoSector(codigoSector);
				newCentroSector.setNombreCentro(nombreCentro);
				newCentroSector.setNombreSector(nombreSector);
				centroSectorService.saveOrUpdate(newCentroSector);
			}
		}
	}

	private void importarAgentes(Map<String, Object> row) {
		String cuil = (String)row.get("CUIL");
		if(cuil!=null){
			Agente agente = agenteService.findByCuil(cuil);
			//si no existe todavia, crearlo
			if(agente==null){
				AgenteImpl newAgente = new AgenteImpl();
				newAgente.setCuil(cuil);
				String apellidoNombre = (String)row.get("APELLIDONOMBRE");
				newAgente.setApellidoNombre(apellidoNombre);
				agenteService.saveOrUpdate(newAgente);
			}
		}
	}
	
	private void importarCategorias(Map<String, Object> row) {
		String codigoCategoria = (String)row.get("CATEGORIA");
		if(codigoCategoria!=null){
			Categoria categoria = categoriaService.findByCodigo(codigoCategoria);
			//si no existe todavia, crearlo
			if(categoria==null){
				CategoriaImpl newCategoria = new CategoriaImpl();
				newCategoria.setCodigo(codigoCategoria);
				categoriaService.saveOrUpdate(newCategoria);
			}
		}
	}
	
	private void importarEmpleos(Map<String, Object> row) throws RuntimeException{
		String codigoCentro = (String)row.get("CENTRO");
		String codigoSector = (String)row.get("SECTOR");
		
		if(codigoCentro!=null && codigoSector!=null){
			
			CentroSector centroSector = centroSectorService.findByCodigoCentroCodigoSector(codigoCentro,codigoSector );
			
			String cuil = (String)row.get("CUIL");
			
			System.out.println(cuil);
			
			if(cuil!=null){
				Agente agente = agenteService.findByCuil(cuil);
				String codigoCategoria = (String)row.get("CATEGORIA");
				if(codigoCategoria!=null){

					Categoria categoria = categoriaService.findByCodigo(codigoCategoria);
				
					EmpleoQueryFilter empleoQueryFilter = new EmpleoQueryFilter();
					empleoQueryFilter.setCuil(cuil);
					empleoQueryFilter.setCodigoCentro(codigoCentro);
					empleoQueryFilter.setCodigoSector(codigoSector);
					empleoQueryFilter.setCodigoCategoria(codigoCategoria);
					
					
					
					List<Empleo> empleos = empleoService.find(empleoQueryFilter);
					
					if(CollectionUtils.isEmpty(empleos)){
						//crear una entrada en empleo
						//crear un movimiento de creditos de tipo CargaInicialAgenteExistente
						
						EmpleoImpl empleo = new EmpleoImpl();
						empleo.setAgente(agente);
						empleo.setCentroSector(centroSector);
						empleo.setCategoria(categoria);
						
						String esBaja = (String)row.get("BAJA");
						
						if(!StringUtils.hasText(esBaja)){ //es carga inicial
							MovimientoCreditosImpl movimientoCargaInicialAgenteExistente = new MovimientoCreditosImpl();
							movimientoCargaInicialAgenteExistente.setTipoMovimientoCreditos(TipoMovimientoCreditos.CargaInicialAgenteExistente);
							
							int cantidadCreditosPorCargaInicial = this.administradorCreditosService.getCreditosPorCargaInicial(codigoCategoria);
							
							movimientoCargaInicialAgenteExistente.setCantidadCreditos(cantidadCreditosPorCargaInicial);
							movimientoCargaInicialAgenteExistente.setEmpleo(empleo);
							
							empleo.setEstado(EstadoEmpleo.ACTIVO);
							empleo.addMovimientoCreditos(movimientoCargaInicialAgenteExistente);
							
						}else{//es baja
							MovimientoCreditosImpl movimientoBajaAgente = new MovimientoCreditosImpl();
							movimientoBajaAgente.setTipoMovimientoCreditos(TipoMovimientoCreditos.BajaAgente);
							
							int cantidadCreditosPorBaja = this.administradorCreditosService.getCreditosPorBaja(codigoCategoria);
							
							movimientoBajaAgente.setCantidadCreditos(cantidadCreditosPorBaja);
							movimientoBajaAgente.setEmpleo(empleo);
							
							DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							//dfm.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
							Date fechaFin = null;
							
							try{
								fechaFin=dfm.parse("2012-03-01 0:0:00");
							}catch(ParseException ex){
								throw new RuntimeException(ex);
							}
							
							empleo.setFechaFin(fechaFin);
							empleo.setEstado(EstadoEmpleo.BAJA);
							
							empleo.addMovimientoCreditos(movimientoBajaAgente);
							
						}
						
						
						empleoService.saveOrUpdate(empleo);
						
					}
				
				}			
			}
		}
		
	}
	
	/*
	public AccountDao getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountDao accountService) {
		this.accountService = accountService;
	}
*/
	public NamedParameterJdbcTemplate getDpiJdbcTemplate() {
		return dpiJdbcTemplate;
	}

	public void setDpiJdbcTemplate(NamedParameterJdbcTemplate dpiJdbcTemplate) {
		this.dpiJdbcTemplate = dpiJdbcTemplate;
	}

	
	public ReparticionService getReparticionService() {
		return reparticionService;
	}

	public void setReparticionService(ReparticionService reparticionService) {
		this.reparticionService = reparticionService;
	}
	
	public CentroSectorService getCentroSectorService() {
		return centroSectorService;
	}

	public void setCentroSectorService(CentroSectorService centroSectorService) {
		this.centroSectorService = centroSectorService;
	}
	
	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	
	public AgenteService getAgenteService() {
		return agenteService;
	}

	public void setAgenteService(AgenteService agenteService) {
		this.agenteService = agenteService;
	}
	
	public CategoriaService getCategoriaService() {
		return categoriaService;
	}

	public void setCategoriaService(CategoriaService categoriaService) {
		this.categoriaService = categoriaService;
	}
	
	public EmpleoService getEmpleoService() {
		return empleoService;
	}

	public void setEmpleoService(EmpleoService empleoService) {
		this.empleoService = empleoService;
	}


}

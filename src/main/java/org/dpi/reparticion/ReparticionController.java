package org.dpi.reparticion;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dpi.agente.Agente;
import org.dpi.agente.AgenteQueryFilter;
import org.dpi.agente.AgenteService;
import org.dpi.agente.EstadoAgente;
import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
import org.dpi.creditsPeriod.CreditsPeriodImpl;
import org.dpi.empleo.EmpleoQueryFilter;
import org.dpi.empleo.EmpleoQueryFilter.estado;
import org.dpi.empleo.EmpleoService;
import org.dpi.movimientoCreditos.MovimientoCreditos;
import org.dpi.movimientoCreditos.MovimientoCreditosAscensoVO;
import org.dpi.movimientoCreditos.MovimientoCreditosQueryFilter;
import org.dpi.movimientoCreditos.MovimientoCreditosService;
import org.dpi.movimientoCreditos.TipoMovimientoCreditos;
import org.dpi.security.AccountSettings;
import org.dpi.security.UserAccessService;
import org.dpi.security.UserSettingsFactory;
import org.dpi.security.UserSettingsFactoryImpl;
import org.janux.bus.persistence.EntityNotFoundException;
import org.janux.bus.security.Account;
import org.janux.bus.security.AccountImpl;
import org.janux.bus.security.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.travel.AjaxUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ReparticionController {


	static Logger log = LoggerFactory.getLogger(ReparticionController.class);

	/**  name of the http Request parameter which we use to pass an Reparticion's code: 'hotelCode' */
	public static final String PARAM_REPARTICION_ID = "reparticionId";

	public static final String PARAM_CURR_PATH = "currPath";

	public static final String KEY_CURRENT_REPARTICION_ID = "currentReparticionId";


	/** 
	 * key that we use when placing an Reparticion instance in a model: 'hotel'
	 * TODO: make private; have clients use session getters/putters
	 */
	public static final String KEY_REPARTICION = "reparticion";

	/** 
	 * The key by which we store in the Session the list of Reparticions that the
	 * Principal is authorized to view: "myReparticions"
	 */
	private static final String KEY_REPARTICION_LIST = "myReparticiones";

	/** name of a generic view used to display messages: 'MessagePage' */
	public static final String VIEW_MESSAGE = "MessagePage";

	/** key that we use when placing a Message string in a model: 'message' */
	public static final String KEY_MESSAGE = "message";

	private ReparticionService reparticionService;

	@Resource(name = "accountServiceGeneric")
	private AccountService accountService;

	@Resource(name = "empleoService")
	private EmpleoService empleoService;

	@Resource(name = "agenteService")
	private AgenteService agenteService;


	@Resource(name = "administradorCreditosService")
	private AdministradorCreditosService administradorCreditosService;

	@Resource(name = "movimientoCreditosService")
	private MovimientoCreditosService movimientoCreditosService;

	@Resource(name = "accountSettingsFactory")
	private UserSettingsFactory settingsFactory;	

	@Inject
	public ReparticionController(ReparticionService reparticionService) {
		this.reparticionService = reparticionService;
	}


	public AdministradorCreditosService getAdministradorCreditosService() {
		return administradorCreditosService;
	}

	public void setAdministradorCreditosService(
			AdministradorCreditosService administradorCreditosService) {
		this.administradorCreditosService = administradorCreditosService;
	}

	public EmpleoService getEmpleoService() {
		return empleoService;
	}


	public void setEmpleoService(EmpleoService empleoService) {
		this.empleoService = empleoService;
	}

	/*@ModelAttribute
	public Reparticion addReparticion(@PathVariable Long reparticionId) {
		return (reparticionId != null) ? reparticionService.findById(reparticionId) : null;
	}*/

	public static Reparticion getCurrentReparticion(HttpServletRequest request)
	{
		return (Reparticion) request.getAttribute(ReparticionController.KEY_REPARTICION);
	}

	@RequestMapping(value = "/reparticiones/reparticion/showCreditos", method = RequestMethod.GET)
	public String showCreditos(HttpServletRequest request, HttpServletResponse response,/*@PathVariable Long reparticionId,*/ Model model) {

		// get the current reparticion in the session
		final Reparticion reparticion = ReparticionController.getCurrentReparticion(request);

		if (reparticion != null){

			if (reparticion instanceof Reparticion) 
			{
				model.addAttribute(PARAM_REPARTICION_ID, reparticion.getId());
			}

			Long creditosPorCargaInicialDeReparticion = this.administradorCreditosService.getCreditosPorCargaInicialDeReparticion(reparticion.getId());
			model.addAttribute("creditosPorCargaInicialDeReparticion", creditosPorCargaInicialDeReparticion);


			Long creditosPorBaja = this.administradorCreditosService.getCreditosPorBajasDeReparticion(reparticion.getId());
			model.addAttribute("creditosPorBaja", creditosPorBaja);

			Long creditosDisponiblesAlInicioPeriodo = this.administradorCreditosService.getCreditosDisponiblesAlInicioPeriodo(new CreditsPeriodImpl(),reparticion.getId());
			model.addAttribute("creditosDisponiblesAlInicioPeriodo", creditosDisponiblesAlInicioPeriodo);

			

			
			Long creditosPorIngresosOAscensosSolicitados = this.administradorCreditosService.getCreditosPorIngresosOAscensosSolicitados(new CreditsPeriodImpl(), reparticion.getId());
			
			model.addAttribute("creditosPorIngresosOAscensosSolicitados", creditosPorIngresosOAscensosSolicitados);
			

			Long creditosPorIngresosOAscensosOtorgados = this.administradorCreditosService.getCreditosPorIngresosOAscensosOtorgados(new CreditsPeriodImpl(), reparticion.getId());
			
			model.addAttribute("creditosPorIngresosOAscensosOtorgados", creditosPorIngresosOAscensosOtorgados);

			
			

			Long creditosDisponiblesSegunSolicitado = this.administradorCreditosService.getCreditosDisponiblesSegunSolicitado(reparticion.getId());
			model.addAttribute("creditosDisponiblesSegunSolicitado", creditosDisponiblesSegunSolicitado);
			
			Long creditosDisponiblesSegunOtorgado = this.administradorCreditosService.getCreditosDisponiblesSegunOtorgado(reparticion.getId());
			model.addAttribute("creditosDisponiblesSegunOtorgado", creditosDisponiblesSegunOtorgado);

		}
		return "reparticiones/show";
	}


	@RequestMapping(value = "/reparticiones/reparticion/showEmpleos", method = RequestMethod.GET)
	public String showEmpleos(	HttpServletRequest request, 
			HttpServletResponse response,
			/*@PathVariable Long reparticionId,*/ Model model) {
		// get the current reparticion in the session
		final Reparticion reparticion = ReparticionController.getCurrentReparticion(request);

		if (reparticion != null){

			AgenteQueryFilter agenteQueryFilter = new AgenteQueryFilter();
			agenteQueryFilter.setReparticionId(reparticion.getId());
			agenteQueryFilter.setEstadoAgente(EstadoAgente.ACTIVO);
			

			List<Agente> agentes = agenteService.find(agenteQueryFilter);
			model.addAttribute("agentes", agentes);

		}
		return "reparticiones/edit";
	}

	@RequestMapping(value = "/reparticiones/{reparticionId}", method = RequestMethod.POST)
	public String edit(ReparticionImpl reparticion, BindingResult result,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		if (result.hasErrors()) {
			return "reparticiones/edit";
		}
		reparticionService.saveOrUpdate(reparticion);
		return (AjaxUtils.isAjaxRequest(requestedWith)) ? "reparticiones/show" : "redirect:/reparticiones/" + reparticion.getId();
	}

	@RequestMapping(value = "/reparticiones/reparticion/showMovimientos", method = RequestMethod.GET)
	public String showMovimientos(
			HttpServletRequest request, 
			HttpServletResponse response,
			/*@PathVariable Long reparticionId,*/ Model model) {

		// get the current reparticion in the session
		final Reparticion reparticion = ReparticionController.getCurrentReparticion(request);

		if (reparticion != null){


			EmpleoQueryFilter empleoQueryFilter = new EmpleoQueryFilter();
			empleoQueryFilter.setReparticionId(reparticion.getId().toString());
			empleoQueryFilter.setEstadoEmpleo(estado.TODOS);

			MovimientoCreditosQueryFilter movimientoCreditosQueryFilter = new MovimientoCreditosQueryFilter();
			movimientoCreditosQueryFilter.setEmpleoQueryFilter(empleoQueryFilter);
			movimientoCreditosQueryFilter.addTipoMovimientoCreditos(TipoMovimientoCreditos.CargaInicialAgenteExistente);
			movimientoCreditosQueryFilter.addTipoMovimientoCreditos(TipoMovimientoCreditos.AscensoAgente);
			movimientoCreditosQueryFilter.addTipoMovimientoCreditos(TipoMovimientoCreditos.BajaAgente);
			movimientoCreditosQueryFilter.addTipoMovimientoCreditos(TipoMovimientoCreditos.IngresoAgente);

			List<MovimientoCreditos> movimientoCreditosReparticion = movimientoCreditosService.find(movimientoCreditosQueryFilter);
			
			Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Account currenUser = (Account)accountObj;
					
			List<MovimientoCreditosAscensoVO> movimientoCreditosVOReparticion = movimientoCreditosService.buildMovimientoCreditosVO(movimientoCreditosReparticion,currenUser);
					
			model.addAttribute("movimientos", movimientoCreditosVOReparticion);
			
			//creditos disponibles
			long creditosDisponibles = administradorCreditosService.getCreditosDisponiblesSegunSolicitado(reparticion.getId());
			
			model.addAttribute("creditosDisponibles", creditosDisponibles);
		}
		return "reparticiones/movimientos";
	}
	
	
	




	@RequestMapping(value = "/reparticiones/reparticion/movimientos/{movimientoId}/borrar", method = RequestMethod.GET)
	public String borrarMovimiento(
			HttpServletRequest request, 
			HttpServletResponse response,
			@PathVariable Long movimientoId,
			Model model) {

		// get the current reparticion in the session
		final Reparticion reparticion = ReparticionController.getCurrentReparticion(request);

		if (reparticion != null){
			MovimientoCreditosQueryFilter movimientoCreditosQueryFilter = new MovimientoCreditosQueryFilter();
			movimientoCreditosQueryFilter.setId(movimientoId);
			EmpleoQueryFilter empleoFilter = new EmpleoQueryFilter();
			empleoFilter.setReparticionId(reparticion.getId().toString());
			movimientoCreditosQueryFilter.setEmpleoQueryFilter(empleoFilter);

			MovimientoCreditos movimiento = movimientoCreditosService.find(movimientoCreditosQueryFilter).get(0);

			if(movimiento!=null){
				movimientoCreditosService.delete(movimiento);	
			}
		}

		return "redirect:/reparticiones/reparticion/showMovimientos";

	}

	/*@ExceptionHandler(MissingServletRequestParameterException.class)
	public String handleMyException(Exception  exception) {
		return "yourErrorViewName";
	}*/


	public MovimientoCreditosService getMovimientoCreditosService() {
		return movimientoCreditosService;
	}


	public void setMovimientoCreditosService(
			MovimientoCreditosService movimientoCreditosService) {
		this.movimientoCreditosService = movimientoCreditosService;
	}



	/**
	 * Reloads the list of hotels that a user is allowed to edit via the AccessService.
	 * If the hotel list for a user changes, the changes will be seen immediately
	 * @param request
	 * @param accessService
	 */
	public static void refreshRequestReparticionList(HttpServletRequest request,UserAccessService accessService)
	{
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String accountName = ((Account) principal).getName();
		Set<ReparticionAdminInfo> reparticiones = accessService.getReparticionListForAccount(accountName, null);

		request.setAttribute(ReparticionController.KEY_REPARTICION_LIST,reparticiones);
	}

	/** 
	 * Loads the Reparticion in the Request, and checks that the current account has the proper
	 * permissions to view that hotel
	 */
	public static void refreshRequestReparticion(HttpServletRequest request,ReparticionService reparticionService,
			UserAccessService accessService,boolean checkAccess)
	{
		boolean createSessionIfNeeded = true;
		HttpSession session = request.getSession(createSessionIfNeeded);
		Long reparticionId = (Long) session.getAttribute(ReparticionController.KEY_CURRENT_REPARTICION_ID);

		AccountSettings settings = UserSettingsFactoryImpl.getSettingsForPrincipal();

		checkAccess = checkAccess || (reparticionId == null); // first time in

		if(reparticionId == null){
			if(StringUtils.hasText(settings.getLastHotel())){
				reparticionId = Long.parseLong(settings.getLastHotel());	
			}
		}

		Reparticion reparticion = null;

		if (reparticionId == null)
		{
			log.warn("There is currently no Reparticion active in the session");
		}
		else 
		{
			try
			{ 
				reparticion = reparticionService.findById(reparticionId);
			}
			catch (EntityNotFoundException e)
			{
				log.error(e.toString());
				reparticion = null;
			}

			if (checkAccess)
			{
				Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				Account account = (Account) principal;
				boolean hasAccess = accessService.hasAccessToReparticion(account, reparticionId); 

				if (!hasAccess)
				{
					reparticion = null;
					settings.setLastHotel(null);
					log.warn("Account: '" + account.getName() + "' attempting to access page without authorization");
				}
			}
		}

		request.setAttribute(ReparticionController.KEY_REPARTICION,reparticion);
	}


	/** 
	 * deep loads an Reparticion based on a code passed in the query string parameter denoted by
	 * ReparticionInfoController.PARAM_HOTEL_CODE and forwards to the path indicated by  
	 */

	//http://localhost:58080/creditos/reparticiones/select?hotelCode=68&currPath=/
	//@RequestMapping(value = "/reparticiones/{reparticionId}/movimientos/{movimientoId}/borrar", method = RequestMethod.GET)

	//	@RequestMapping(value = "/reparticiones/select?hotelCode={reparticionId}&currPath={currPath}/", method = RequestMethod.GET)
	@RequestMapping(value = "/reparticiones/select", method = RequestMethod.GET)
	//public ModelAndView selectReparticion(HttpServletRequest request, HttpServletResponse response, @PathVariable Long reparticionId,@PathVariable String currPath)
	public ModelAndView selectReparticion(HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam("reparticionId") Long reparticionId,
			@RequestParam("currPath") String currPath)
					throws Exception 
					{
		ModelAndView returnView = null;
		//String id = ServletRequestUtils.getStringParameter(request, PARAM_REPARTICION_ID, "");

		//MDC.put(LoggingConstants.KEY_DETAILS, PARAM_HOTEL_CODE + "='" + code + "'");

		if (this.doSelectReparticion(request,reparticionId))
		{
			//String currPath = ServletRequestUtils.getStringParameter(request, PARAM_CURR_PATH,"");

			if (log.isDebugEnabled()) {log.debug("Doing redirect to: '" + currPath + "'");}
			returnView = new ModelAndView(new RedirectView("/home",true));

			//String selectedReportCode = ServletRequestUtils.getStringParameter(request, "reportCode", "");
			//request.getSession().setAttribute("selectedReportCode", selectedReportCode);

			// clear these session variables
			//request.getSession().setAttribute(DailyUpdateController.INV_MANAGE_RATE_FILTER_PARAM, null);
			//request.getSession().setAttribute(DailyUpdateController.INV_MANAGE_ROOM_FILTER_PARAM, null);
		}	
		else
		{
			returnView = new ModelAndView(VIEW_MESSAGE, KEY_MESSAGE, "Unable to load Reparticion with code '" + reparticionId + "'");
		}

		return returnView;
					}

	/** 
	 * deep loads an Reparticion based on a code passed in the query string parameter denoted by
	 * ReparticionInfoController.PARAM_HOTEL_CODE and forwards to a summary display page 
	 * @returns whether selection was successful (empty code allowed; for non-empty code, hotel must
	 * be able to be loaded.) 
	 */
	public boolean doSelectReparticion(HttpServletRequest request, Long id) 
	{
		boolean ok = true;
		Reparticion reparticion = null;

		if (id!=null)
		{
			reparticion = reparticionService.findById(id);

			if (reparticion != null)
			{

				Account januxUserDetailsAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				//AbstractUserSettings januxUserDetailsAccountSettings = settingsFactory.getSettingsForAccount(januxUserDetailsAccount);

				Account account = accountService.loadAccountByName(januxUserDetailsAccount.getName());


				AccountSettings accountSettings = (AccountSettings)settingsFactory.getSettingsForAccount(account);

				accountSettings.setLastHotel(String.valueOf(id));

				accountService.saveOrUpdateAccount((AccountImpl)account);
				request.getSession().setAttribute(KEY_CURRENT_REPARTICION_ID, reparticion.getId());
				request.setAttribute(KEY_REPARTICION, reparticion);
				if (log.isDebugEnabled()) {log.debug("Completed doing doSelectReparticion with ok status");}
			}
			else {
				ok = false;
			}
		}

		return ok;
	}


	/*
	public ModelAndView doReparticionInfoView(HttpServletRequest request,
			HttpServletResponse response, ReparticionPropertyInfo propertyInfo, Map<String, Object> model) throws Exception {

		model = (model != null) ? model : new HashMap<String,Object>();
		Reparticion hotel = ReparticionInfoController.getCurrentReparticion(request);


		this.addGeoToModel(model);
		this.addCurrenciesToModel(model);
		this.addLanguagesToModel(model);

		this.addPmsTypesToModel(model);

		boolean isBookingEngineEnabled = false;
		boolean allowsStayCredits = false;

		ReparticionAdminInfo hotelInfo = new ReparticionAdminInfo();

		String isSaveReparticionAttribute = ServletRequestUtils.getStringParameter(request,
				IS_SAVE_HOTEL_ATTRIBUTE, Boolean.FALSE.toString());

		if (hotel != null) 
		{
			boolean getPropertyMap = "true".equals(request.getParameter("dump"));

			if (getPropertyMap) 
			{
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				String hotelPropsString = os.toString();
				model.put("textDump", hotelPropsString);
			}

			if (new Boolean(isSaveReparticionAttribute)) 
			{
				boolean success = true;
				successMessage = null;
				failureMessage = null;

				if(isPostedReparticionDifferentFromCurrentReparticion(hotel,propertyInfo)){
					Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					String accountName = ((Account) principal).getName();
					log.warn("User "+accountName+" has changed the current active hotel to "+hotel.getCode()+"in another browser window/tab." +
							"Saving the posted data of hotel "+propertyInfo.getPropertyCode());
					success=true;
					successMessage=this.getApplicationContext().getMessage("msg.currentReparticionDifferentFromPostedReparticion", 
							new Object[] { propertyInfo.getPropertyCode(), hotel.getCode(), hotel.getCode()},null);
					hotel=hotelService.loadByCode(propertyInfo.getPropertyCode());
				}else{
					successMessage = "Updated hotel: " + hotel.getCode();
				}

				saveReparticionAttribute(request, hotel, propertyInfo);

				model.put(messageParam,success ? successMessage : failureMessage);
				model.put(successParam,new Boolean(success));
				//restore current hotel
				hotel = ReparticionInfoController.getCurrentReparticion(request);
			}

			hotelInfo = new ReparticionAdminInfo(hotel);
			isBookingEngineEnabled = Boolean.valueOf(hotel
					.getReparticionAttribute(ReparticionAttributeConstants.Attributes.IP_LINK_SETUP.name()));

			allowsStayCredits = Boolean.valueOf(hotel
					.getReparticionAttribute(ReparticionAttributeConstants.Attributes.ALLOWS_STAY_CREDITS.name()));

			propertyInfo = new ReparticionPropertyInfo(hotel);

			// add amenity info
			final List<Amenity> amenityList = this.hotelService.getReparticionDao().findPropertyAmenities();
			model.put("amenityList", amenityList);

			final Map<String, String> amenityTypes = new HashMap<String, String>();
			for (Amenity amenity : amenityList)
			{
				amenityTypes.put(amenity.getAmenityType(), amenity.getAmenityType());
			}

			model.put("amenityTypes", amenityTypes);

			final Set<ReparticionAmenity> assignedAmenities = hotel.getReparticionAmenities();
			model.put("assignedAmenities", assignedAmenities);
		}

		model.put(KEY_ACCOUNT, this.getAccountFromContext());
		model.put(BOOKING_ENGINE_ENABLED, new Boolean(isBookingEngineEnabled));
		model.put(ALLOWS_STAY_CREDITS, new Boolean(allowsStayCredits));
		model.put(HOTEL_INFO, hotelInfo);
		model.put(PROPERTY_INFO, propertyInfo);
		model.put(WHICH_TAB_KEY, ServletRequestUtils.getIntParameter(request,WHICH_TAB_KEY, 1));

		return new ModelAndView(VIEW_PROPERTY_INFO, model);
	}

	 */

}
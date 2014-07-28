package org.dpi.creditsManagement;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.dpi.creditsEntry.CreditsEntry.GrantedStatus;
import org.dpi.creditsEntry.CreditsEntryDaoHibImpl;
import org.dpi.creditsEntry.CreditsEntryQueryFilter;
import org.dpi.creditsEntry.CreditsEntryType;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.employment.EmploymentQueryFilter;
import org.dpi.person.PersonCondition;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.janux.bus.persistence.DataAccessHibImplAbstract;
import org.janux.util.Chronometer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class CreditsManagerServiceImpl extends DataAccessHibImplAbstract implements CreditsManagerService{

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	Map<String,Integer> creditosPorCargaInicial = new HashMap<String,Integer>();
	
	Map<String,Integer> creditosARestarPorIngreso = new HashMap<String,Integer>();
	
	Map<String,Integer> creditosASumarPorBaja = new HashMap<String,Integer>();
	
	Map<String, Map<String, Integer>> creditosPorAscenso = new HashMap<String,Map<String, Integer>>();
	
	//String MAX_CATEGORIA_PARA_PROFESIONALES_SIN_CONSUMO_DE_CREDITOS = "21";
	
	@Resource(name = "creditsPeriodService")
	private CreditsPeriodService creditsPeriodService;
	

	public CreditsManagerServiceImpl() {
		creditosPorCargaInicial.put("12", 60);
		creditosPorCargaInicial.put("13", 60);
		creditosPorCargaInicial.put("14", 56);
		creditosPorCargaInicial.put("15", 52);
		creditosPorCargaInicial.put("16", 48);
		creditosPorCargaInicial.put("17", 42);
		creditosPorCargaInicial.put("18", 36);
		creditosPorCargaInicial.put("19", 28);
		creditosPorCargaInicial.put("20", 20);
		creditosPorCargaInicial.put("21", 12);
		creditosPorCargaInicial.put("22", 6);
		creditosPorCargaInicial.put("23", 4);
		creditosPorCargaInicial.put("24", 2);
		
		
		//------------------------------------
		creditosARestarPorIngreso.put("12", 1000);
		creditosARestarPorIngreso.put("13", 1010);
		creditosARestarPorIngreso.put("14", 1030);
		creditosARestarPorIngreso.put("15", 1061);
		creditosARestarPorIngreso.put("16", 1103);
		creditosARestarPorIngreso.put("17", 1158);
		creditosARestarPorIngreso.put("18", 1227);
		creditosARestarPorIngreso.put("19", 1314);
		creditosARestarPorIngreso.put("20", 1420);
		creditosARestarPorIngreso.put("21", 1548);
		creditosARestarPorIngreso.put("22", 1710);
		creditosARestarPorIngreso.put("23", 1916);
		creditosARestarPorIngreso.put("24", 2182);
		
		
		//-------------------------------------
		
		creditosASumarPorBaja.put("12", 750);
		creditosASumarPorBaja.put("13", 758);
		creditosASumarPorBaja.put("14", 773);
		creditosASumarPorBaja.put("15", 796);
		creditosASumarPorBaja.put("16", 827);
		creditosASumarPorBaja.put("17", 869);
		creditosASumarPorBaja.put("18", 920);
		creditosASumarPorBaja.put("19", 986);
		creditosASumarPorBaja.put("20", 1065);
		creditosASumarPorBaja.put("21", 1161);
		creditosASumarPorBaja.put("22", 1283);
		creditosASumarPorBaja.put("23", 1437);
		creditosASumarPorBaja.put("24", 1637);
		
		//-------------------------------------


		
		creditosPorAscenso.put("12", new HashMap<String,Integer>());
		creditosPorAscenso.get("12").put("13", 10);
		creditosPorAscenso.get("12").put("14", 30);
		creditosPorAscenso.get("12").put("15", 61);
		creditosPorAscenso.get("12").put("16", 103);
		creditosPorAscenso.get("12").put("17", 158);
		creditosPorAscenso.get("12").put("18", 227);
		creditosPorAscenso.get("12").put("19", 314);
		creditosPorAscenso.get("12").put("20", 420);
		creditosPorAscenso.get("12").put("21", 548);
		creditosPorAscenso.get("12").put("22", 710);
		creditosPorAscenso.get("12").put("23", 916);
		creditosPorAscenso.get("12").put("24", 1182);

		creditosPorAscenso.put("13", new HashMap<String,Integer>());
		creditosPorAscenso.get("13").put("14", 20);
		creditosPorAscenso.get("13").put("15", 51);//
		creditosPorAscenso.get("13").put("16", 93);//
		creditosPorAscenso.get("13").put("17", 148);
		creditosPorAscenso.get("13").put("18", 217);
		creditosPorAscenso.get("13").put("19", 304);//
		creditosPorAscenso.get("13").put("20", 410);
		creditosPorAscenso.get("13").put("21", 538);
		creditosPorAscenso.get("13").put("22", 700);//
		creditosPorAscenso.get("13").put("23", 906);
		creditosPorAscenso.get("13").put("24", 1172);

		creditosPorAscenso.put("14", new HashMap<String,Integer>());
		creditosPorAscenso.get("14").put("15", 31);
		creditosPorAscenso.get("14").put("16", 73);
		creditosPorAscenso.get("14").put("17", 128);
		creditosPorAscenso.get("14").put("18", 197);
		creditosPorAscenso.get("14").put("19", 284);
		creditosPorAscenso.get("14").put("20", 390);
		creditosPorAscenso.get("14").put("21", 518);
		creditosPorAscenso.get("14").put("22", 680);
		creditosPorAscenso.get("14").put("23", 886);
		creditosPorAscenso.get("14").put("24", 1152);

		creditosPorAscenso.put("15", new HashMap<String,Integer>());
		creditosPorAscenso.get("15").put("16", 42 );
		creditosPorAscenso.get("15").put("17", 97);
		creditosPorAscenso.get("15").put("18", 166);
		creditosPorAscenso.get("15").put("19", 253);
		creditosPorAscenso.get("15").put("20", 359);//
		creditosPorAscenso.get("15").put("21", 487);
		creditosPorAscenso.get("15").put("22", 649);
		creditosPorAscenso.get("15").put("23", 855);
		creditosPorAscenso.get("15").put("24", 1121);
		
		creditosPorAscenso.put("16", new HashMap<String,Integer>());
		creditosPorAscenso.get("16").put("17", 55);
		creditosPorAscenso.get("16").put("18", 124);
		creditosPorAscenso.get("16").put("19", 211);
		creditosPorAscenso.get("16").put("20", 317);//
		creditosPorAscenso.get("16").put("21", 445);
		creditosPorAscenso.get("16").put("22", 607);
		creditosPorAscenso.get("16").put("23", 813);
		creditosPorAscenso.get("16").put("24", 1079);

		creditosPorAscenso.put("17", new HashMap<String,Integer>());
		creditosPorAscenso.get("17").put("18", 69);
		creditosPorAscenso.get("17").put("19", 156);
		creditosPorAscenso.get("17").put("20", 262);
		creditosPorAscenso.get("17").put("21", 390);
		creditosPorAscenso.get("17").put("22", 552);
		creditosPorAscenso.get("17").put("23", 758);
		creditosPorAscenso.get("17").put("24", 1024);
		
		creditosPorAscenso.put("18", new HashMap<String,Integer>());
		creditosPorAscenso.get("18").put("19", 87);
		creditosPorAscenso.get("18").put("20", 193);
		creditosPorAscenso.get("18").put("21", 321);
		creditosPorAscenso.get("18").put("22", 483);
		creditosPorAscenso.get("18").put("23", 689);
		creditosPorAscenso.get("18").put("24", 955);
		
		creditosPorAscenso.put("19", new HashMap<String,Integer>());
		creditosPorAscenso.get("19").put("20", 106);//
		creditosPorAscenso.get("19").put("21", 234);
		creditosPorAscenso.get("19").put("22", 396);
		creditosPorAscenso.get("19").put("23", 602);
		creditosPorAscenso.get("19").put("24", 868);

		creditosPorAscenso.put("20", new HashMap<String,Integer>());
		creditosPorAscenso.get("20").put("21", 128);
		creditosPorAscenso.get("20").put("22", 290);//
		creditosPorAscenso.get("20").put("23", 496);
		creditosPorAscenso.get("20").put("24", 762);

		creditosPorAscenso.put("21", new HashMap<String,Integer>());
		creditosPorAscenso.get("21").put("22", 162);//
		creditosPorAscenso.get("21").put("23", 368);
		creditosPorAscenso.get("21").put("24", 634);
		
		creditosPorAscenso.put("22", new HashMap<String,Integer>());
		creditosPorAscenso.get("22").put("23", 206);
		creditosPorAscenso.get("22").put("24", 472);//

		creditosPorAscenso.put("23", new HashMap<String,Integer>());
		creditosPorAscenso.get("23").put("24", 266);

		
	}

	
	public int getCreditosPorCargaInicial(String categoryCode){
		Integer creditos = creditosPorCargaInicial.get(categoryCode);
		if(creditos==null){
			creditos=0;
		}
		return creditos;
		
	}
	

	public int getCreditosPorBaja(String categoryCode){
		Integer creditos = creditosASumarPorBaja.get(categoryCode);
		if(creditos==null){
			creditos=0;
		}
		return creditos;
		
	}
	
	
	public int getCreditosPorAscenso(PersonCondition personCondition, String currentCategoryCode, String newCategoryCode){
		Integer creditos = 0;
		creditos = this.creditosPorAscenso.get(currentCategoryCode).get(newCategoryCode);	

		if(creditos==null){
			creditos=0;
		}
		return creditos;
	}
	
	public int getCreditosPorIngreso(String categoryCode){
		Integer creditos = this.creditosARestarPorIngreso.get(categoryCode);
		if(creditos==null){
			creditos=0;
		}
		return creditos;
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public 	Long getCreditosPorCargaInicialDeReparticion(final Long creditsPeriodId,final long reparticionId){
		CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
		EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
		creditsEntryQueryFilter.setEmploymentQueryFilter(employmentQueryFilter);
		employmentQueryFilter.setReparticionId(String.valueOf(reparticionId));
		creditsEntryQueryFilter.setIdCreditsPeriod(creditsPeriodId);
		creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.CargaInicialAgenteExistente);
		creditsEntryQueryFilter.addGrantedStatus(GrantedStatus.Otorgado);
		return 	getCreditsEntriesSum(creditsEntryQueryFilter);
		
		/*
		return (Long) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session sess)
				throws HibernateException, SQLException  {
				
				Chronometer timer = new Chronometer();

				if (log.isDebugEnabled()) log.debug("attempting to find Reparticion with id: '" + reparticionId + "'");

				String queryStr = "select sum(entry.cantidadCreditos) " +
						" from CreditsEntryImpl entry " +
						" INNER JOIN entry.employment employment " +
						" INNER JOIN employment.centroSector centroSector " +
						" INNER JOIN centroSector.reparticion reparticion "+
						" INNER JOIN employment.person person "+
						" where reparticion.id='"+reparticionId+"'" +
						" AND entry.creditsEntryType = '"+CreditsEntryType.CargaInicialAgenteExistente.name()+"'"+
						" AND entry.creditsPeriod.id = '"+creditsPeriodId+"'";
				
				Query query = sess.createQuery(queryStr);
			
				Long totalAmount = (Long) query.uniqueResult();
				
				
				if (log.isDebugEnabled()) log.debug("successfully retrieved reparticion with id: '" + reparticionId + "' in " + timer.printElapsedTime());
				if(totalAmount==null){
					totalAmount=new Long(0);
				}
				return totalAmount;
			}
		});*/
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long getCreditosPorBajasDeReparticion(final Long creditsPeriodId,final long reparticionId) {

		CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
		EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
		creditsEntryQueryFilter.setEmploymentQueryFilter(employmentQueryFilter);
		employmentQueryFilter.setReparticionId(String.valueOf(reparticionId));
		creditsEntryQueryFilter.setIdCreditsPeriod(creditsPeriodId);
		creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.BajaAgente);
		creditsEntryQueryFilter.addGrantedStatus(GrantedStatus.Otorgado);
		return 	getCreditsEntriesSum(creditsEntryQueryFilter);
	}	

	@SuppressWarnings("unchecked")
	@Override
	public 	Long getCreditsEntriesSum(final CreditsEntryQueryFilter creditsEntryQueryFilter){
		return (Long) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session sess)
				throws HibernateException, SQLException  {
				
				Chronometer timer = new Chronometer();

				if (log.isDebugEnabled()) log.debug("attempting to calculate credits for Reparticion with id: '" + creditsEntryQueryFilter.getEmploymentQueryFilter().getReparticionId() + "'");

				StringBuffer sb = new StringBuffer();
				sb.append(" select sum(entry.cantidadCreditos) ")
				.append(" from CreditsEntryImpl entry ")
				.append(" INNER JOIN entry.employment employment " )
				.append(" INNER JOIN employment.centroSector centroSector " )
				.append(" INNER JOIN centroSector.reparticion reparticion ");
				
				String where = " WHERE 1=1 "+buildWhereClause(creditsEntryQueryFilter);
				
				sb.append(where);
				
						/*" where reparticion.id = :reparticionId " +
						" AND entry.creditsEntryType IN (:creditsTypes) "+
						" AND entry.grantedStatus IN (:grantedStatuses) "+
						" AND entry.creditsPeriod.id = :creditsPeriod ";*/
				Query query = sess.createQuery(sb.toString());
				
				setNamedParameters(query,creditsEntryQueryFilter);
			
				Long totalAmount = (Long) query.uniqueResult();
				

				
				if (log.isDebugEnabled()) log.debug("successfully calculated credits for Reparticion with id: '" + creditsEntryQueryFilter.getEmploymentQueryFilter().getReparticionId() + "' in " + timer.printElapsedTime());
				if(totalAmount==null){
					totalAmount=new Long(0);
				}
				return totalAmount;
			}
		});
	}
	
	
	private String buildWhereClause(CreditsEntryQueryFilter creditsEntryQueryFilter) {
		StringBuffer sb = new StringBuffer();
		
		if(creditsEntryQueryFilter!=null) {
			EmploymentQueryFilter employmentQueryFilter = creditsEntryQueryFilter.getEmploymentQueryFilter();
			if(employmentQueryFilter!=null) {
				
				String idReparticion = employmentQueryFilter.getReparticionId();
				if(StringUtils.hasText(idReparticion)) {
					sb.append(" AND reparticion.id = :idReparticion ");
				}
			}
			
			if(!CollectionUtils.isEmpty(creditsEntryQueryFilter.getCreditsEntryTypes())) {
				sb.append(" AND entry.creditsEntryType IN ( :creditsEntryTypes ) ");
			}
			
			if(!CollectionUtils.isEmpty(creditsEntryQueryFilter.getGrantedStatuses())) {
				sb.append(" AND entry.grantedStatus IN (:grantedStatuses) ");
			}
			
			
			if(creditsEntryQueryFilter.getIdCreditsPeriod()!=null && creditsEntryQueryFilter.getIdCreditsPeriod()>0) {
				sb.append(" AND entry.creditsPeriod.id = :creditsPeriod ");
			}
						
			

		}
		return sb.toString();
	}
	
	
	public void setNamedParameters(Query query,		CreditsEntryQueryFilter creditsEntryQueryFilter) {
		
		if(creditsEntryQueryFilter!=null) {
			EmploymentQueryFilter employmentQueryFilter = creditsEntryQueryFilter.getEmploymentQueryFilter();
			if(employmentQueryFilter!=null) {
				
				String idReparticion = employmentQueryFilter.getReparticionId();
				if(StringUtils.hasText(idReparticion)) {
					query.setString("idReparticion", idReparticion);
				}
			}
			
			if(!CollectionUtils.isEmpty(creditsEntryQueryFilter.getCreditsEntryTypes())) {
				query.setParameterList("creditsEntryTypes", creditsEntryQueryFilter.getCreditsEntryTypes());
			}
			
			if(!CollectionUtils.isEmpty(creditsEntryQueryFilter.getGrantedStatuses())) {
				query.setParameterList("grantedStatuses", creditsEntryQueryFilter.getGrantedStatuses());
			}
			
			
			if(creditsEntryQueryFilter.getIdCreditsPeriod()!=null && creditsEntryQueryFilter.getIdCreditsPeriod()>0) {
				query.setLong("creditsPeriod", creditsEntryQueryFilter.getIdCreditsPeriod());
			}
						
			

		}
	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public 	Long getTotalCreditos(final CreditsEntryQueryFilter creditsEntryQueryFilter){
		return (Long) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session sess)
				throws HibernateException, SQLException  {
				
				Chronometer timer = new Chronometer();
				
				String reparticionId = creditsEntryQueryFilter.getEmploymentQueryFilter().getReparticionId();

				if (log.isDebugEnabled()) log.debug("attempting to find Movimientos with id: '" + reparticionId + "'");
				
				String where = " WHERE 1=1 " + CreditsEntryDaoHibImpl.buildWhereClause(creditsEntryQueryFilter);

				StringBuffer sb = new StringBuffer();
				sb.append("select sum(entry.cantidadCreditos) ");
				sb.append(" from CreditsEntryImpl entry ");
				sb.append(" INNER JOIN entry.employment employment ");
				sb.append(" INNER JOIN employment.centroSector centroSector ");
				sb.append(" INNER JOIN centroSector.reparticion reparticion ");
				sb.append(" INNER JOIN entry.creditsPeriod creditsPeriod ");
				
				
				sb.append(where);
				
				Query query = sess.createQuery(sb.toString());
			
				Long totalAmount = (Long) query.uniqueResult();
				
				
				if (log.isDebugEnabled()) log.debug("successfully retrieved reparticion with id: '" + reparticionId + "' in " + timer.printElapsedTime());
				if(totalAmount==null){
					totalAmount=new Long(0);
				}
				return totalAmount;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long getCreditosDisponiblesSegunSolicitado(Long creditsPeriodId,final long reparticionId){

		Long creditosDisponiblesAlInicioPeriodo = getCreditosDisponiblesAlInicioPeriodo(creditsPeriodId,reparticionId);
		
		Long creditosAcreditadosPorBajasDelPeriodo = getCreditosPorBajasDeReparticion(creditsPeriodId,reparticionId);
		
		Long totalPorIngresosOAscensosSegunSolicitado = this.getCreditosPorIngresosOAscensosSolicitados(creditsPeriodId, reparticionId);
		
		return creditosDisponiblesAlInicioPeriodo + creditosAcreditadosPorBajasDelPeriodo -totalPorIngresosOAscensosSegunSolicitado;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long getCreditosDisponiblesSegunOtorgado(Long creditsPeriodId, final long reparticionId){
		
		Long creditosDisponiblesAlInicioPeriodo = getCreditosDisponiblesAlInicioPeriodo(creditsPeriodId,reparticionId);
		
		Long creditosAcreditadosPorBajasDelPeriodo = getCreditosPorBajasDeReparticion(creditsPeriodId,reparticionId);
		
		Long totalPorIngresosOAscensosSegunOtorgado = this.getCreditosPorIngresosOAscensosOtorgados(creditsPeriodId, reparticionId);
		
		return creditosDisponiblesAlInicioPeriodo + creditosAcreditadosPorBajasDelPeriodo - totalPorIngresosOAscensosSegunOtorgado;
		
	}


	@Override
	public Long getCreditosPorIngresosOAscensosSolicitados(Long creditsPeriodId, Long reparticionId) {
		EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
		employmentQueryFilter.setReparticionId(String.valueOf(reparticionId));
		
		CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
		creditsEntryQueryFilter.setEmploymentQueryFilter(employmentQueryFilter);
		creditsEntryQueryFilter.setIdCreditsPeriod(creditsPeriodId);
		
		creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.AscensoAgente);
		creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.IngresoAgente);
		creditsEntryQueryFilter.addGrantedStatus(GrantedStatus.Solicitado);
		creditsEntryQueryFilter.addGrantedStatus(GrantedStatus.Otorgado); //Un entry Otorgado tambien es solicitado(fue solicitado en algun momento)
		return getTotalCreditos(creditsEntryQueryFilter);
	}
	
	@Override
	public Long getCreditosPorIngresosOAscensosOtorgados(
			 Long creditsPeriodId, Long reparticionId) {
		EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
		employmentQueryFilter.setReparticionId(String.valueOf(reparticionId));
	
		CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
		creditsEntryQueryFilter.setEmploymentQueryFilter(employmentQueryFilter);
		creditsEntryQueryFilter.setIdCreditsPeriod(creditsPeriodId);
		creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.AscensoAgente);
		creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.IngresoAgente);
		creditsEntryQueryFilter.addGrantedStatus(GrantedStatus.Otorgado);
		return getTotalCreditos(creditsEntryQueryFilter);
	}


	@Override
	public Long getCreditosDisponiblesAlInicioPeriodo(Long creditsPeriodId, Long reparticionId) {
		
		CreditsPeriod creditsPeriod = creditsPeriodService.findById(creditsPeriodId);
		
		//If period does not have previous period return 0
		if(creditsPeriod.getPreviousCreditsPeriod()==null){
			return 0l;
		}
		
		//get previous period
		CreditsPeriod previousPeriod = creditsPeriod.getPreviousCreditsPeriod();

		Long totalPorCargaInicial = this.getCreditosPorCargaInicialDeReparticion(previousPeriod.getId(),reparticionId);
		
		Long totalPorBajas = this.getCreditosPorBajasDeReparticion(previousPeriod.getId(),reparticionId);
		
		Long totalcreditosDisponiblesSegunOtorgadoPeriodoActual = this.getCreditosPorIngresosOAscensosOtorgados(previousPeriod.getId(),reparticionId);
		
		long creditosDisponiblesAlInicioPeriodoActual = totalPorCargaInicial+totalPorBajas-totalcreditosDisponiblesSegunOtorgadoPeriodoActual;
		
		long creditosDisponiblesAlInicioPeriodoAnterior = getCreditosDisponiblesAlInicioPeriodo(previousPeriod.getId(), reparticionId);
		
		return creditosDisponiblesAlInicioPeriodoAnterior + creditosDisponiblesAlInicioPeriodoActual;
		
	}

	
	public CreditsPeriodService getCreditsPeriodService() {
		return creditsPeriodService;
	}


	public void setCreditsPeriodService(CreditsPeriodService creditsPeriodService) {
		this.creditsPeriodService = creditsPeriodService;
	}

}

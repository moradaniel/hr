package org.dpi.empleo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.dpi.agente.Agente;
import org.dpi.category.Category;
import org.dpi.centroSector.CentroSector;
import org.dpi.domain.Persistent;
import org.dpi.movimientoCreditos.MovimientoCreditos;
import org.dpi.occupationalGroup.OccupationalGroup;


public interface Empleo extends Persistent,Serializable{
	

	Agente getAgente();

	void setAgente(Agente agente);

	CentroSector getCentroSector();

	void setCentroSector(CentroSector centroSector);
	
	Category getCategory();

	void setCategory(Category category);
	
	EmploymentStatus getEstado();

	void setEstado(EmploymentStatus estado);

	public Date getFechaInicio();

	public void setFechaInicio(Date fechaInicio);

	public Date getFechaFin();

	public void setFechaFin(Date fechaFin);
	
	public Set<MovimientoCreditos> getMovimientosCreditos();
	
	public void setMovimientosCreditos(Set<MovimientoCreditos> movimientosCreditos);

	public void addMovimientoCreditos(MovimientoCreditos movimientoCreditos);

	public boolean isClosed();

	Empleo getEmpleoAnterior();

	void setEmpleoAnterior(Empleo empleoAnterior);
	
	public OccupationalGroup getOccupationalGroup();

	public void setOccupationalGroup(OccupationalGroup occupationalGroup);
	

}
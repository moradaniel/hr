package org.dpi.employment;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.dpi.category.Category;
import org.dpi.centroSector.CentroSector;
import org.dpi.creditsEntry.CreditsEntry;
import org.dpi.domain.Persistent;
import org.dpi.occupationalGroup.OccupationalGroup;
import org.dpi.person.Person;


public interface Employment extends Persistent,Serializable{
	

	Person getPerson();

	void setPerson(Person person);

	CentroSector getCentroSector();

	void setCentroSector(CentroSector centroSector);
	
	Category getCategory();

	void setCategory(Category category);
	
	EmploymentStatus getStatus();

	void setStatus(EmploymentStatus status);

	public Date getFechaInicio();

	public void setFechaInicio(Date fechaInicio);

	public Date getFechaFin();

	public void setFechaFin(Date fechaFin);
	
	public Set<CreditsEntry> getCreditsEntries();
	
	public void setCreditsEntries(Set<CreditsEntry> creditsEntries);

	public void addCreditsEntry(CreditsEntry creditsEntry);

	public boolean isClosed();

	Employment getPreviousEmployment();

	void setPreviousEmployment(Employment previousEmployment);
	
	public OccupationalGroup getOccupationalGroup();

	public void setOccupationalGroup(OccupationalGroup occupationalGroup);
	

}
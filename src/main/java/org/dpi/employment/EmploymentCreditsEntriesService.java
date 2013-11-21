package org.dpi.employment;

import java.util.List;

import org.janux.bus.security.Account;
import org.springframework.context.ApplicationContextAware;


/**
 * Used to create, save, retrieve, update and delete Employment objects from
 * persistent storage
 *
 */
public interface EmploymentCreditsEntriesService extends ApplicationContextAware
{
	public void promotePerson(Employment empleo, String newCategoryCode);
	
	public void darDeBaja(Employment empleo);
	
	public void proposeNewEmployment(String proposedCategoryCode,Long centroSectorId);

	public List<EmploymentVO> buildEmploymentsVO(List<Employment> activeEmployments, Long reparticionId, Account currenUser);
}

package org.dpi.creditsEntry;

import java.util.List;
import java.util.Set;

import org.dpi.creditsEntry.CreditsEntry.GrantedStatus;
import org.dpi.util.PageList;
import org.janux.bus.security.Account;
import org.springframework.context.ApplicationContextAware;


/**
 * Used to create, save, retrieve, update and delete CreditsEntry objects from
 * persistent storage
 *
 */
public interface CreditsEntryService extends ApplicationContextAware
{

	public List<CreditsEntry> find(CreditsEntryQueryFilter creditsEntryQueryFilter);
	
	public PageList<CreditsEntry> findCreditsEntries(final CreditsEntryQueryFilter creditsEntryQueryFilter);
	   
	public void delete(CreditsEntry creditsEntry);
	
	public List<CreditsEntryVO> buildCreditsEntryVO(List<CreditsEntry> creditsEntryReparticion, Account account);
	public void saveOrUpdate(final CreditsEntry creditsEntry);
	public void actualizarCreditosPorAscenso();
	
	public void changeGrantedStatus(CreditsEntry creditsEntry, GrantedStatus newEstado);
	
	public Set<Long> havePendingEntries(List<Long> personsIds, Long idReparticion,Long idCreditsPeriod);
		
}

package org.dpi.creditsEntry;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.domain.PersistentAbstract;
import org.dpi.employment.Employment;
import org.janux.util.JanuxToStringStyle;

public class CreditsEntryImpl  extends PersistentAbstract implements CreditsEntry{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	CreditsEntryType creditsEntryType;

	Employment employment;
	int cantidadCreditos;

	private String observaciones;

	private CreditsPeriod creditsPeriod;
	
	private GrantedStatus grantedStatus;
	
	public CreditsEntryImpl() {
		super();
	}
	
	
	@Override
	public Employment getEmployment() {
		return this.employment;
	}

	@Override
	public void setEmployment(Employment employment) {
		this.employment=employment;
		
	}
	@Override
	public int getCantidadCreditos() {
		return this.cantidadCreditos;
	}

	@Override
	public void setCantidadCreditos(int cantidadCreditos) {
		this.cantidadCreditos=cantidadCreditos;
		
	}
	
	
	public CreditsEntryType getCreditsEntryType() {
		return creditsEntryType;
	}

	public void setCreditsEntryType(
			CreditsEntryType creditsEntryType) {
		this.creditsEntryType = creditsEntryType;
	}

	@Override
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
		
	}

	@Override
	public String getObservaciones() {
		return this.observaciones;
	}

	@Override
	public CreditsPeriod getCreditsPeriod() {
		return this.creditsPeriod;
	}

	@Override
	public void setCreditsPeriod(CreditsPeriod creditsPeriod) {
		this.creditsPeriod = creditsPeriod;
	}

	@Override
	public GrantedStatus getGrantedStatus() {
		return grantedStatus;
	}

	@Override
	public void setGrantedStatus(GrantedStatus grantedStatus) {
		this.grantedStatus = grantedStatus;
	}

	@Override
	public String toString() 
	{
		ToStringBuilder sb = new ToStringBuilder(this, JanuxToStringStyle.COMPACT);
		
		sb.append(super.toString());
		
		sb.append("creditsEntryType", getCreditsEntryType().name());
		sb.append("grantedStatus", getGrantedStatus().name());
		sb.append("credits", getCantidadCreditos());

		
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cantidadCreditos;
		result = prime
				* result
				+ ((creditsEntryType == null) ? 0 : creditsEntryType.hashCode());
		result = prime * result
				+ ((creditsPeriod == null) ? 0 : creditsPeriod.hashCode());
		result = prime * result
				+ ((employment == null) ? 0 : employment.hashCode());
		result = prime * result
				+ ((grantedStatus == null) ? 0 : grantedStatus.hashCode());
		result = prime * result
				+ ((observaciones == null) ? 0 : observaciones.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreditsEntryImpl other = (CreditsEntryImpl) obj;
		if (cantidadCreditos != other.cantidadCreditos)
			return false;
		if (creditsEntryType != other.creditsEntryType)
			return false;
		if (creditsPeriod == null) {
			if (other.creditsPeriod != null)
				return false;
		} else if (!creditsPeriod.equals(other.creditsPeriod))
			return false;
		if (employment == null) {
			if (other.employment != null)
				return false;
		} else if (!employment.equals(other.employment))
			return false;
		if (grantedStatus != other.grantedStatus)
			return false;
		if (observaciones == null) {
			if (other.observaciones != null)
				return false;
		} else if (!observaciones.equals(other.observaciones))
			return false;
		return true;
	}
}

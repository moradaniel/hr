package org.dpi.person;

import java.io.Serializable;


/**
 * DAO implementation agnostic class that can hold commons constraints to be used
 * by query DAO query methods. The class allows to keep informations useful to do 
 * common things on object retrieval methods like multiple field results ordering, 
 * paging, and very common by date filtering.
 * 
 *
 */
public class PersonQueryFilter implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String apellidoNombre;
	private String cuil;
	private Long personId;
	private PersonCondition personCondition;


	private Long reparticionId;
	

	public String getApellidoNombre() {
		return apellidoNombre;
	}
	public void setApellidoNombre(String apellidoNombre) {
		this.apellidoNombre = apellidoNombre;
	}
	public String getCuil() {
		return cuil;
	}
	public void setCuil(String cuil) {
		this.cuil = cuil;
	}

	public Long getReparticionId() {
		return reparticionId;
	}
	public void setReparticionId(Long reparticionId) {
		this.reparticionId = reparticionId;
	}
	
	public Long getPersonId() {
		return personId;
	}
	public void setPersonId(Long personId) {
		this.personId = personId;
	}
	
	public PersonCondition getPersonCondition() {
		return personCondition;
	}
	
	public void setPersonCondition(PersonCondition personCondition) {
		this.personCondition = personCondition;
	}

}

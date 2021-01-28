package org.dpi.department;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.domain.PersistentAbstract;
import org.dpi.subDepartment.SubDepartment;

public class DepartmentImpl extends PersistentAbstract implements Department {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String name;
	
	private Set<SubDepartment> subDepartments = new HashSet<SubDepartment>();
	
	private Department parent;
	private Set<Department> children = new HashSet<Department>();
	
	private CreditsPeriod validFromPeriod;
	private CreditsPeriod validToPeriod;

    public DepartmentImpl(){
	}
	
    @Override
	public String getCode(){
		return this.code;
	}
	
    @Override
	public void setCode(String code){
		this.code = code;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Set<SubDepartment> getSubDepartments() {
		return subDepartments;
	}

	@Override
	public void setSubDepartments(Set<SubDepartment> subDepartments) {
		this.subDepartments = subDepartments;
		
	}

	@Override
	public void addSubDepartment(SubDepartment subDepartment) {
		this.subDepartments.add(subDepartment);
	}

    @Override
    public Department getParent() {
        return parent;
    }

    @Override
    public void setParent(Department parent) {
        this.parent = parent;
    }

    @Override
    public Set<Department> getChildren() {
        return children;
    }

    @Override
    public void setChildren(Set<Department> children) {
        this.children = children;
    }
    
    @Override
    public CreditsPeriod getValidFromPeriod() {
        return validFromPeriod;
    }

    @Override
    public void setValidFromPeriod(CreditsPeriod validFromPeriod) {
        this.validFromPeriod = validFromPeriod;
    }

    @Override
    public CreditsPeriod getValidToPeriod() {
        return validToPeriod;
    }

    @Override
    public void setValidToPeriod(CreditsPeriod validToPeriod) {
        this.validToPeriod = validToPeriod;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        DepartmentImpl other = (DepartmentImpl) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    /*
    @Override
    public String toString() {
        return "Department [code=" + code + ", name=" + name + "]";
    }*/
    
        @Override
        public String toString() {
            ToStringBuilder toStringBuilder = new ToStringBuilder(this,
                    ToStringStyle.SHORT_PREFIX_STYLE);
            toStringBuilder.append("code", code);
            toStringBuilder.append("name", name);
            return toStringBuilder.toString();
        }

}

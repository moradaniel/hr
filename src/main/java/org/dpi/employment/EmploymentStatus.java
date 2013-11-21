package org.dpi.employment;

public enum EmploymentStatus {
	ACTIVO,
	INACTIVO, //el empleo fue creado en un periodo ya cerrado, el ascenso o ingreso fue solicitado pero NO fue otorgado
	BAJA, 
	PENDIENTE /*El empleo es generado por un movimiento de ingreso, ascenso o baja en el periodo actual abierto*/
}
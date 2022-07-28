package eParticipation.backend.dto;

import java.util.List;

import lombok.Data;

@Data
public class DtOrganismo {
	
	private Long id;
	private String nombre;
	private String departamento;	
	private List<String> empleados;
	
	public DtOrganismo() {	}

	public DtOrganismo(Long id, String nombre, String departamento) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.departamento = departamento;
	} 	

	public DtOrganismo(Long id, String nombre, String departamento, List<String> e) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.departamento = departamento;
		this.empleados = e;
	} 


}

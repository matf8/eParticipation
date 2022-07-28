package eParticipation.backend.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import eParticipation.backend.dto.DtOrganismo;
import lombok.Data;

@Data
@Entity
@NamedQuery(name="Organismo.findAll", query="select i from Organismo i")
public class Organismo {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long idOrganismo;
	
	@Column(nullable=false)
	private String nombre;
	
	private String departamento;	
	
	@OneToMany(mappedBy = "oficina", cascade = CascadeType.ALL)
	private List<Funcionario> empleados = new ArrayList<>();
	
	public Organismo() {	}

	public Organismo(String nombre, String departamento) {
		super();
		this.nombre = nombre;
		this.departamento = departamento;
	}
	
	public void agregarEmpleado(Funcionario f) {
		empleados.add(f);		
	}
	
	public void quitarEmpleado(Funcionario f) {
		empleados.remove(f);		
	}	
	
	public DtOrganismo getDt() { 
		List<String> e = new ArrayList<>();
		if (empleados.size() > 0) {			
			for (Funcionario f: empleados)
				e.add(f.getCedula());
		}
		return new DtOrganismo(this.idOrganismo.toString(), this.nombre, this.departamento, e);
	}
}

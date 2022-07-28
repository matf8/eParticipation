package eParticipation.externo.data;

import lombok.Data;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class User {
	
	private String cedula;
	private String nombre;
	private String birth;
	private String nacionalidad;
	private String domicilio;
		
	public User() {	}

	public User(String cedula, String nacionalidad, String nombre, String birth, String d) {
		super();
		this.cedula = cedula;
		this.nacionalidad = nacionalidad;
		this.nombre = nombre;
		this.birth = birth;
		this.domicilio = d;
	}

}

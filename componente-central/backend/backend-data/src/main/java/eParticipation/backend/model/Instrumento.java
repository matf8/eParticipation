package eParticipation.backend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Instrumento {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long idInstrumento;
	
	@ManyToOne
	private ProcesoParticipativo proceso;
	
	public Instrumento () {	}	

}

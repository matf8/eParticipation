package eParticipation.backend.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class TipoCertificado {
	
	private static TipoCertificado instancia = null;	

	public static TipoCertificado getInstancia() {
		if (instancia == null)
			instancia = new TipoCertificado();
		return instancia;
	}
		
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idTipoCertificado;	
	
	@ElementCollection(targetClass = String.class)
	private List<String> tipos = new ArrayList<>();
	//Comentarios, Adhesiones, Seguidores, Participaciones;
			
	public TipoCertificado() {
		this.tipos.add("Comentarios");
		this.tipos.add("Adhesiones");
		this.tipos.add("Seguidores");
		this.tipos.add("Creadores");
		this.tipos.add("Crear");		

	}
	
	public String getTipo(String t) throws Exception {		
		if (tipos.contains(t))			
			return t;
		else throw new Exception("Tipo no está");
		
	}
	
	public boolean agregarTipoCertificado(String tipo) {
		if (!tipos.contains(tipo))  {
			this.tipos.add(tipo);
			return true;
		} else return false;
	}
	
	public void quitarTipoCertificado(String tipo) {
		if (tipos.contains(tipo))
			this.tipos.remove(tipo);
	}

}

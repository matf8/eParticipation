package eParticipation.backend.dto;

import org.apache.commons.lang3.ArrayUtils;

import lombok.Data;

@Data
public class DtParticipa {
	
	private String proceso;		
	private String user;		
	private String[] respuesta = new String[3]; // (p1, u1, [votacion,pregunta,respuesta]), [encuesta,pregunta,respuesta]))
												// (p1, u2, [votacion,pregunta,respuesta]))
	public DtParticipa() {	}	
	
	public DtParticipa(String proceso, String user, String[] respuesta) {
		super();
		this.proceso = proceso;
		this.user = user;
		this.respuesta = respuesta;
	}
	
	public void addElement(String e) {
		ArrayUtils.add(respuesta, e);		
	}
	
	public void removeElement(int e) {
		ArrayUtils.remove(respuesta, e);	
	}

	// ¿cuantos votos obtuvo "Q" la votacion "X" del p1?
/*	public void calcularVotos() {
		 int cantQ = 0;
		 for (DtParticipa k: participantes) {
		 	String[] res = k.getRespuesta();
		 	if (res[0].equals("votacion"))
		 		if (res[1].equals("X"))
		 			if (res[2].equals("Q"))
		 				cantQ++;
		 }
	}	
	 
	 	*
	 	*/
	 

}

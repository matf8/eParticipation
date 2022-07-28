package eParticipation.backend.service;

import eParticipation.backend.controller.ControladorIniciativa;
import eParticipation.backend.controller.ControladorInsignia;
import eParticipation.backend.controller.ControladorProceso;
import eParticipation.backend.controller.ControladorUsuario;
import eParticipation.backend.data.Persistencia;

public class Fabrica {
	private static Fabrica instancia = null;
	
	private Fabrica(){}
	
	public static Fabrica getInstancia() {
		if (instancia == null)
			instancia = new Fabrica();
		return instancia;
	}
	
	public UsuarioService getUsuarioService() {
		return new ControladorUsuario();
	}
	
	public IniciativaService getIniciativaService() {
		return new ControladorIniciativa();
	}
	
	public ProcesoService getProcesoService() {
		return new ControladorProceso();
	}
	
	public PersistenciaLocal getPersistenciaService() {
		return Persistencia.getInstancia();
	}
	
	public InsigniaService getInsigniaService() {
		return new ControladorInsignia();
	}	
		
}



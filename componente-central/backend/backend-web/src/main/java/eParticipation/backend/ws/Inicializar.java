package eParticipation.backend.ws;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import eParticipation.backend.service.IniciativaService;
import eParticipation.backend.service.PersistenciaLocal;
import eParticipation.backend.service.ProcesoService;

@RequestScoped
@Path("/inicializar")
public class Inicializar {
	
	@EJB private PersistenciaLocal bd;	
	@EJB private ProcesoService procesos;	
	@EJB private IniciativaService iniciativas;	
	
	@GET
	@Path("v1")
	public void init() {		
		try {
			bd.datosIniciales();			
			procesos.datosIniciales();
			iniciativas.comentariosIniciales();
		} catch (Exception e) {
			System.out.println("exception: " + e.getMessage());
		}		
	}

}

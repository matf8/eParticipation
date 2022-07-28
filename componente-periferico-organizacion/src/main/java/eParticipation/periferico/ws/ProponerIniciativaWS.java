package eParticipation.periferico.ws;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import eParticipation.periferico.controller.ControladorOrganizacion;
import eParticipation.periferico.model.Iniciativa;
import eParticipation.periferico.service.OrganizacionService;

@Path("/proponerIniciativa")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ProponerIniciativaWS {	
		
	private OrganizacionService organizacion = new ControladorOrganizacion();
	
	public ProponerIniciativaWS() throws NamingException {	}
	
	@POST
	@Path("/ingresarDatos")
	public String altaIniciativa(Iniciativa i) throws Exception {				
		if (i != null) 		
			return organizacion.altaIniciativa(i);		
		else throw new Exception ("Datos no ingresados");
	}	
	
	@GET
	@Path("/ping")
	public String ping() {		
		return "pong";		
	}

	
	
}

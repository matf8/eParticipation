package eParticipation.periferico.ws;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import eParticipation.periferico.controller.ControladorEvaluador;
import eParticipation.periferico.model.Iniciativa;
import eParticipation.periferico.model.DtIniciativaEv;
import eParticipation.periferico.service.EvaluadorService;

@Path("/evaluarIniciativa")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class EvaluarIniciativaWS {	
		
	private EvaluadorService evaluador = new ControladorEvaluador();
	
	public EvaluarIniciativaWS() throws NamingException {	}
	
	@POST
	@Path("/evaluador")
	public Response evaluarIniciativa(Iniciativa i) {		
		DtIniciativaEv iev = evaluador.evaluarIniciativa(i);		
		return Response.ok(iev).build();
	}
		
	@GET
	@Path("/ping")
	public String ping() {		
		return "pong";		
	}

	
	
}

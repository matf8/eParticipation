package eParticipation.backend.ws;

import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import eParticipation.backend.service.ConverterService;
import eParticipation.backend.dto.DtParticipa;
import eParticipation.backend.dto.DtProceso;
import eParticipation.backend.service.ProcesoService;

@RequestScoped
@Path("/proceso")
@Produces("application/json")
@Consumes("application/json")
public class ProcesoWS {
	
	@EJB private ProcesoService p;		
	@EJB private ConverterService cc;	
		
	public ProcesoWS() throws NamingException {	}	
	
	@POST
	@Path("/alta") // solo funcionarios
	public Response altaProceso(DtProceso d) throws Exception {
		if (d.getNombre() != null)  {		
			try {
				p.altaProceso(d);				
				return Response.ok("Proceso participativo " + d.getNombre() + " agregado correctamente para el dia " + d.getFecha() +
						"\nEl alcance de este proceso es: " + d.getDescripcionAlcance()).build();
			} catch (Exception e) {
				return Response.ok(e.getMessage()).build();	
			}
		} else return Response.ok("Iniciativa sin nombre no permitido").build();
	}
	
	@GET
	@Path("/buscar/{nombre}")
	public Response buscarProceso(@PathParam("nombre") String nombre) {
		try {
			if (nombre != null) {
				DtProceso i = p.buscarProceso(nombre);
				if (i != null) {
					JSONObject ji = cc.jsonProceso(i);
					return Response.ok(ji).build();
				} else return Response.ok("Proceso no existe").build();
			} else return Response.ok("Proceso null").build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();		
		}		
	}
	
	@GET
	@Path("/listar")
	public Response listarProcesos() {	// manda un jsonarray
		try {
			List<DtProceso> li = p.listarProcesos();				
			JSONArray jsLi = new JSONArray();	
			for (DtProceso i: li)
				jsLi.add(i);
			return Response.ok(jsLi).build();
		} catch (Exception e) {
			return Response.status(404, "Error").build();			
		}			
	}
	
	@PUT
	@Path("/modificar")
	public Response modificarProceso(DtProceso d) {
		try {
			if (d != null) {				
				p.modificarProceso(d);					
				return Response.ok("proceso modificado " + d).build();
			}
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}
		return Response.status(401, "Error").build();		
	}
	
	@DELETE
	@Path("/baja")
	public Response bajaProceso(@QueryParam("nombre") String d) {
		try {
			if (d != null) {				
				p.bajaProceso(d);			
				return Response.ok("Proceso " + d + " dado de baja").build();
			}
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}
		return Response.status(401, "Error").build();		
	}
	
	@POST
	@Path("/participar")	
	//public Response agregarParticipante(@QueryParam("proceso") String proceso, @QueryParam("user") String correo) throws Exception {
	public Response agregarParticipante(DtParticipa a) throws Exception {
		if (a.getProceso() != null)
			try {
				p.agregarParticipanteProceso(a);
				return Response.ok("Usuario agregado a los participantes del proceso " + a.getProceso() + " correctamente, " + "respuesta: " + Arrays.asList(a.getRespuesta()).toString()).build();
			} catch (Exception e) {
				return Response.ok(e.getMessage()).build();
			}
		return Response.status(403, "error").build();	
	}
	
	@POST
	@Path("/quitarParticipar")	
	public Response removerParticipante(@QueryParam("proceso") String proceso, @QueryParam("user") String correo) throws Exception {
		if (proceso != null)
			try {
				p.quitarParticipanteProceso(proceso, correo);
				return Response.ok("Usuario quitado de los participantes del proceso " + proceso + " correctamente").build();
			} catch (Exception e) {
				return Response.ok(e.getMessage()).build();
			}
		return Response.status(403, "error").build();	
	}	
	
	@GET
	@Path("/listarRango")
	public Response listarProcesosaFechas(@QueryParam("fechaI") String d1, @QueryParam("fechaF") String d2) {	
		try {
			List<DtProceso> li = p.listarProcesosFechas(d1, d2);			
			JSONArray jsLi = new JSONArray();	
			for (DtProceso i: li)
				jsLi.add(i);
			return Response.ok(jsLi).build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}			
	}
	
	@POST
	@Path("/seguir")	
	public Response agregarSeguidor(@QueryParam("proceso") String proceso, @QueryParam("user") String correo) throws Exception {
		if (proceso != null)
			try {
				p.agregarSeguidor(proceso, correo);
				return Response.ok("Proceso seguido correctamente por el usuario " + correo).build();
			} catch (Exception e) {
				return Response.ok(e.getMessage()).build();
			}
		return Response.status(403, "error").build();	
	}
	
	@POST
	@Path("/dejarSeguir")	
	public Response quitarSeguidor(@QueryParam("proceso") String proceso, @QueryParam("user") String correo) throws Exception {
		if (proceso != null)
			try {
				p.quitarSeguidor(proceso, correo);
				return Response.ok("Proceso dejado de seguir correctamente por el usuario " + correo).build();
			} catch (Exception e) {
				return Response.ok(e.getMessage()).build();
			}
		return Response.status(403, "error").build();	
	}
	
	@POST
	@Path("/comentar")
	public Response comentarProceso(@QueryParam("comentario") String comentario, @QueryParam("user") String user, @QueryParam("proceso") String proceso) throws Exception {	
		if (proceso != null) {
			if (user != null) {
				if (comentario != null) {
					try {		
						p.comentarProceso(proceso, user, comentario);		
						return Response.ok("Comentario agregado").build();
					} catch (Exception e) {
						return Response.ok(e.getMessage()).build();	
					}
				} else return Response.ok("Comentario vacio").build();
			} else return Response.ok("Dato user null").build();
		} else return Response.ok("Dato iniciativa null").build();		
	}
	
	@POST
	@Path("/borrarComentario")
	public Response borrarComentarioProceso(@QueryParam("comentario") String comentario, @QueryParam("proceso") String proceso) throws Exception {	
		if (comentario != null) {
			try {		
				p.borrarComentarioProceso(proceso, comentario);		
				return Response.ok("Comentario borrado").build();
			} catch (Exception e) {
				return Response.ok(e.getMessage()).build();	
			}
		} else return Response.ok("Comentario vacio").build();			
	}
		
	@GET
	@Path("/listarSeguidores")
	public Response listarSeguidores(@QueryParam("proceso") String pr) {
		try {
			List<String> li = p.listarSeguidores(pr);		
			JSONArray jsLi = new JSONArray();	
			for (String i: li)
				jsLi.add(i);
			return Response.ok(jsLi).build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}				
	}
	
	@GET
	@Path("/resultadoVotacion")
	public Response resultadoVotacion(@QueryParam("proceso") String pr) {
		try {
			if (pr != null) {
				String r = p.resultVotacion(pr);			
				return Response.ok(r).build();
			} else return Response.ok("proceso null").build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}				
	}
	
	@GET
	@Path("/resultadoEncuesta")
	public Response resultadoEncuesta(@QueryParam("proceso") String pr) {
		try {
			if (pr != null) {
				String r = p.resultEncuesta(pr);			
				return Response.ok(r).build();
			} else return Response.ok("proceso null").build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}				
	}	
}

package eParticipation.backend.ws;

import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import eParticipation.backend.service.ConverterService;
import eParticipation.backend.dto.DtIniciativa;
import eParticipation.backend.service.IniciativaService;
import eParticipation.backend.helpers.TokenSecurity;

@RequestScoped
@Path("/iniciativa")
@Consumes(MediaType.APPLICATION_JSON)
@Produces({MediaType.APPLICATION_JSON})
@SuppressWarnings("unchecked")
public class IniciativaWS {
	
	@EJB private IniciativaService s;	
	@EJB private ConverterService cc;
	
	@POST	 
	@Path("/alta")
	public Response altaIniciativa(DtIniciativa i, @HeaderParam("authorization") String header) throws Exception {	
		try {
			String user = null;
			if (i.getNombre() != null) {
				if (!i.getCreador().contains("ORG")) {
					if (header != null) {
						String token = header.substring(7);		
						System.out.println("token substring: " + token);
						user = TokenSecurity.getIdClaim(TokenSecurity.validarToken(token));
						if(user == null) 
							throw new NotAuthorizedException("No se encuentra correo en token - Unauthorized!");
						System.out.println("Correo obtenido en REST: " + user);
					} else throw new NotAuthorizedException("Header Bearer null");
				}			
				s.altaIniciativa(i.getNombre(), i.getDescripcion(), i.getFecha(), i.getCreador(), i.getRecurso());		
				return Response.ok("Iniciativa " + i.getNombre() + " agregada correctamente para el dia " + i.getFecha() + " con estado EN ESPERA.\nSe le notificará los próximos cambios de estado").build();
			} else return Response.ok("Dato null").build();					
		} catch (Exception e) {			
			return Response.ok(e.getMessage()).build();	
		}			
	}		
		
	@GET
	@Path("/buscar/{nombre}")
	public Response buscarIniciativa(@PathParam("nombre") String nombre, @HeaderParam("authorization") String header) throws Exception {	
		try {
			String user = null;		
			if (nombre != null) {				
				if (header != null) {
					String token = header.substring(7);		
					System.out.println("token substring: " + token);
					user = TokenSecurity.getIdClaim(TokenSecurity.validarToken(token));
					if(user == null) 
						throw new NotAuthorizedException("No se encuentra correo en token - Unauthorized!");
					System.out.println("Correo obtenido en REST: " + user);
				} else throw new NotAuthorizedException("Header Bearer null");
				DtIniciativa i = s.buscarIniciativa(nombre);
				if (i != null) {
					JSONObject ji = cc.jsonIniciativa(i);
					return Response.ok(ji).build();
				} else 
					return Response.ok("Iniciativa no encontrada").build();		
			} else return Response.ok("Dato null").build();				
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();	
		}		
	}	
	
	@GET
	@Path("/listar")
	public Response listarIniciativa() {	// manda un jsonarray
		try {
			List<DtIniciativa> li = s.listarIniciativas();				
			JSONArray jsLi = new JSONArray();	
			for (DtIniciativa i: li)
				jsLi.add(i);
			return Response.ok(jsLi).build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();	
		}			
	}
	
	@GET
	@Path("/listarPublicadas")
	public Response listarIniciativaPublicadas(@HeaderParam("authorization") String header) {	// manda un jsonarray
		try {
			String user = null;							
			if (header != null) {
				String token = header.substring(7);		
				System.out.println("token substring: " + token);
				user = TokenSecurity.getIdClaim(TokenSecurity.validarToken(token));
				if(user == null) 
					throw new NotAuthorizedException("No se encuentra correo en token - Unauthorized!");
				System.out.println("Correo obtenido en REST: " + user);
			} else throw new NotAuthorizedException("Header Bearer null");
			List<DtIniciativa> li = s.listarIniciativasPublicadas();				
			JSONArray jsLi = new JSONArray();	
			for (DtIniciativa i: li)
				jsLi.add(i);
			return Response.ok(jsLi).build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();	
		}			
	}	
	
	@GET
	@Path("/listarRango") // dd/MM/yyyy formato fecha
	public Response listarIniciativaFechas(@QueryParam("fechaI") String d1, @QueryParam("fechaF") String d2, @HeaderParam("authorization") String header) {	
		try {
			String user = null;							
			if (header != null) {
				String token = header.substring(7);		
				System.out.println("token substring: " + token);
				user = TokenSecurity.getIdClaim(TokenSecurity.validarToken(token));
				if(user == null) 
					throw new NotAuthorizedException("No se encuentra correo en token - Unauthorized!");
				System.out.println("Correo obtenido en REST: " + user);
			} else throw new NotAuthorizedException("Header Bearer null");
			List<DtIniciativa> li = s.listarIniciativasFechas(d1, d2);			
			JSONArray jsLi = new JSONArray();	
			for (DtIniciativa i: li)
				jsLi.add(i);
			return Response.ok(jsLi).build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}			
	}
	
	@POST
	@Path("/adherir")	
	public Response adherirUsuario(@QueryParam("iniciativa") String iniciativa, @QueryParam("user") String correo, @HeaderParam("authorization") String header) throws Exception {
		try {
			if (iniciativa != null) {
				String user = null;							
				if (header != null) {
					String token = header.substring(7);		
					System.out.println("token substring: " + token);
					user = TokenSecurity.getIdClaim(TokenSecurity.validarToken(token));
					if(user == null) 
						throw new NotAuthorizedException("No se encuentra correo en token - Unauthorized!");
					System.out.println("Correo obtenido en REST: " + user);
				} else throw new NotAuthorizedException("Header Bearer null");
				s.adherirUsuario(iniciativa, correo);
				return Response.ok("Usuario adherido correctamente a la iniciativa " + iniciativa).build();			
			} return Response.ok("iniciativa null").build();	
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/desadherir")	
	public Response desadherirUsuario(@QueryParam("iniciativa") String iniciativa, @QueryParam("user") String correo, @HeaderParam("authorization") String header) throws Exception {
		try {
			if (iniciativa != null) {
				String user = null;							
				if (header != null) {
					String token = header.substring(7);		
					System.out.println("token substring: " + token);
					user = TokenSecurity.getIdClaim(TokenSecurity.validarToken(token));
					if(user == null) 
						throw new NotAuthorizedException("No se encuentra correo en token - Unauthorized!");
					System.out.println("Correo obtenido en REST: " + user);
				} else throw new NotAuthorizedException("Header Bearer null");
				s.desadherirUsuario(iniciativa, correo);
				return Response.ok("Usuario desadherido correctamente de la iniciativa " + iniciativa).build();			
			} return Response.status(403, "error").build();	// el mensaje de error nunca lo muestra asi q es al pedo			
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();	
		}
	}
	
	@POST
	@Path("/seguir")	
	public Response agregarSeguidor(@QueryParam("iniciativa") String iniciativa, @QueryParam("user") String correo, @HeaderParam("authorization") String header) throws Exception {
		try {
			if (iniciativa != null) {
				String user = null;		
				if (correo != null) {
					if (header != null) {
						String token = header.substring(7);		
						System.out.println("token substring: " + token);
						user = TokenSecurity.getIdClaim(TokenSecurity.validarToken(token));
						if(user == null) 
							throw new NotAuthorizedException("No se encuentra correo en token - Unauthorized!");
						System.out.println("Correo obtenido en REST: " + user);
					} else throw new NotAuthorizedException("Header Bearer null");
					s.agregarSeguidor(iniciativa, correo);
					return Response.ok("Iniciativa seguida correctamente por el usuario " + correo).build();
				} else return Response.ok("Dato usuario null").build(); 
			} else return Response.ok("Dato iniciativa null").build();	
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/dejarSeguir")	
	public Response quitarSeguidor(@QueryParam("iniciativa") String iniciativa, @QueryParam("user") String correo, @HeaderParam("authorization") String header) throws Exception {
		try {
			if (iniciativa != null) {
				if (correo != null) {
					String user = null;							
					if (header != null) {
						String token = header.substring(7);		
						System.out.println("token substring: " + token);
						user = TokenSecurity.getIdClaim(TokenSecurity.validarToken(token));
						if(user == null) 
							throw new NotAuthorizedException("No se encuentra correo en token - Unauthorized!");
						System.out.println("Correo obtenido en REST: " + user);
					} else throw new NotAuthorizedException("Header Bearer null");
					s.quitarSeguidor(iniciativa, correo);
					return Response.ok("Iniciativa dejada de seguir correctamente por el usuario " + correo).build();		
				} else return Response.ok("Dato usuario null").build(); 
			} else return Response.ok("Dato iniciativa null").build();	
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/listarAdheridos")
	public Response listarAdheridosAini(@QueryParam("iniciativa") String iniciativa) {
		try {
			List<String> li = s.listarAdheridos(iniciativa);				
			JSONArray jsLi = new JSONArray();	
			for (String i: li)
				jsLi.add(i);
			return Response.ok(jsLi).build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}			
	}
	
	@GET
	@Path("/listarSeguidores")
	public Response listarSeguidoresAini(@QueryParam("iniciativa") String iniciativa) {
		try {
			List<String> li = s.listarSeguidores(iniciativa);				
			JSONArray jsLi = new JSONArray();	
			for (String i: li)
				jsLi.add(i);
			return Response.ok(jsLi).build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}				
	}
	
	@GET
	@Path("/listarRelacionados") // todas las iniciativas con sus adheridos y seguidores
	public Response listarRelacionadosAini() {
		try {
			List<DtIniciativa> li = s.listarRelacionados();				
			JSONArray jsLi = new JSONArray();	
			for (DtIniciativa i: li)
				jsLi.add(i);
			return Response.ok(jsLi).build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}			
	}	
	
	@PUT
	@Path("/modificar")
	public Response modificarIniciativa(DtIniciativa d, @HeaderParam("authorization") String header) {
		try {
			if (d != null) {	
				String user = null;							
				if (header != null) {
					String token = header.substring(7);		
					System.out.println("token substring: " + token);
					user = TokenSecurity.getIdClaim(TokenSecurity.validarToken(token));
					if(user == null) 
						throw new NotAuthorizedException("No se encuentra correo en token - Unauthorized!");
					System.out.println("Correo obtenido en REST: " + user);
				} else throw new NotAuthorizedException("Header Bearer null");
				s.modificarIniciativa(d);			
				return Response.ok("iniciativa modificada: " + d).build();
			} else return Response.ok("Dato null").build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}
	}
		
	@DELETE
	@Path("/baja")
	public Response bajaIniciativa(@QueryParam("nombre") String d, @HeaderParam("authorization") String header) {
		try {
			if (d != null) {	
				String user = null;							
				if (header != null) {
					String token = header.substring(7);		
					System.out.println("token substring: " + token);
					user = TokenSecurity.getIdClaim(TokenSecurity.validarToken(token));
					if(user == null) 
						throw new NotAuthorizedException("No se encuentra correo en token - Unauthorized!");
					System.out.println("Correo obtenido en REST: " + user);
				} else throw new NotAuthorizedException("Header Bearer null");
				s.bajaIniciativa(d);			
				return Response.ok("Iniciativa " + d + " dada de baja").build();
			} else return Response.ok("Dato null").build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}
	}
	
	@POST
	@Path("/comentar")
	public Response comentarInciativa(@QueryParam("comentario") String comentario, @QueryParam("user") String user, @QueryParam("iniciativa") String nombreIniciativa, @HeaderParam("authorization") String header) throws Exception {	
		try {	
			if (user != null) {
				if (comentario != null) {					
					if (nombreIniciativa != null) {
						String u = null;							
						if (header != null) {
							String token = header.substring(7);		
							System.out.println("token substring: " + token);
							u = TokenSecurity.getIdClaim(TokenSecurity.validarToken(token));
							if(u == null) 
								throw new NotAuthorizedException("No se encuentra correo en token - Unauthorized!");
							System.out.println("Correo obtenido en REST: " + u);
						} else throw new NotAuthorizedException("Header Bearer null");
						s.comentarIniciativa(nombreIniciativa, user, comentario);		
						return Response.ok("Comentario agregado").build();
					} else return Response.ok("Dato iniciativa null").build();					
				} else return Response.ok("Comentario vacio").build();
			} else return Response.ok("Dato user null").build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();	
		}			
	}	
	
	@POST
	@Path("/borrarComentario")
	public Response borrarComentarioIniciativa(@QueryParam("comentario") String comentario, @QueryParam("iniciativa") String ini) throws Exception {	
		if (comentario != null) {
			if (ini != null) {
				try {				
					s.borrarComentarioIniciativa(ini, comentario);		
					return Response.ok("Comentario borrado").build();
				} catch (Exception e) {
					return Response.ok(e.getMessage()).build();	
				}					
			} else return Response.ok("Iniciativa vacia").build();			

		} else return Response.ok("Comentario vacio").build();			
	}
	
}

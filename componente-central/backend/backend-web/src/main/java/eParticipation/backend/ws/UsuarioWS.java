package eParticipation.backend.ws;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import eParticipation.backend.service.ConverterService;
import eParticipation.backend.dto.DtCertificado;
import eParticipation.backend.dto.DtCiudadano;
import eParticipation.backend.dto.DtFuncionario;
import eParticipation.backend.dto.DtProceso;
import eParticipation.backend.service.UsuarioService;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;

@RequestScoped
@Path("/usuario")
@Produces("application/json")
@Consumes("application/json")
@SuppressWarnings("unchecked")
public class UsuarioWS {
	
	@EJB private UsuarioService u;	
	@EJB private ConverterService cc;
		
	public UsuarioWS() throws NamingException {	}	
	
	@POST
	@Path("/registro")
	public Response registroUsuario(@QueryParam("correo") String correo, @QueryParam("cedula") String cedula) {
		if (correo != null) {
			try {
				if (u.buscarUsuario(correo) != null && u.buscarUsuario(cedula) != null) {					
					u.altaCiudadano(cedula);
					return Response.ok("Registro con exito").build();							
				}							
			} catch (Exception e) {
				return Response.ok(e.getMessage()).build();
			}			
		} else return Response.ok("Dato null").build();		
		return Response.status(405).build();	
	} 

	@GET
	@Path("/buscar/{correo}")
	public Response buscarUsuario(@PathParam("correo") String correo) {
		JSONObject j = null;
		if (correo != null) {
			try {
				Object usuario = u.buscarUsuario(correo);				
				if (usuario instanceof DtCiudadano) {
					j = cc.jsonCiudadano((DtCiudadano) usuario);
				
					return Response.ok(j).build();		
				}
			    else if	(usuario instanceof DtFuncionario) {
					j = cc.jsonFuncionario((DtFuncionario) usuario);
					return Response.ok(j).build();
			    }
				return Response.ok(j).build();			
			} catch (Exception e) {			
				return Response.ok(e.getMessage()).build();			
			}	
		} return Response.ok("Dato null").build();		
	}	
	
	@GET
	@Path("/listar")
	public Response listarUsuarios() {
		try {
			List<Object> lu = u.listarUsuarios();				
			JSONArray jsLu = new JSONArray();	
			for (Object i: lu)
				jsLu.add(i);
			return Response.ok(jsLu).build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}	
	}
	
	@GET
	@Path("/alcanceProceso")
	public Response alcanceProceso(@QueryParam("proceso") String nombreProceso, @QueryParam("user") String correo) {
		try {
			int[] k = u.enAlcanceDeProceso(correo, nombreProceso);
			if (k[2] == 1)
				return Response.ok("en alcance: edad usuario: " + k[0] + "; edad minima proceso: " + k[1]).build();
			else return Response.ok("usuario no cumple condicion de alcance: edad usuario: " + k[0] + "; edad minima proceso participativo: " + k[1]).build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}	
	}
	
	@GET
	@Path("/ifSigueI")
	public Response usuarioSigueIniciativa(@QueryParam("iniciativa") String nombre, @QueryParam("user") String correo) {
		try {			
			if (u.usuarioSigueIniciativa(nombre, correo))
				return Response.ok(true).build();
			else return Response.ok(false).build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}	
	}
	
	@GET
	@Path("/ifSigueP")
	public Response usuarioSigueProceso(@QueryParam("proceso") String nombre, @QueryParam("user") String correo) {
		try {	
			if (nombre != null) {
				if (correo != null) {
					if (u.usuarioSigueProceso(nombre, correo))
						return Response.ok(true).build();
					else return Response.ok(false).build();
				} else return Response.ok(false).build();
			} else Response.ok(false).build();
			
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}
		return Response.status(504).build();
	}
	
	@GET
	@Path("/ifAdherido")
	public Response usuarioAdheridoIniciativa(@QueryParam("iniciativa") String nombre, @QueryParam("user") String correo) {
		try {			
			if (u.usuarioAdheridoIniciativa(nombre, correo))
				return Response.ok(true).build();
			else return Response.ok(false).build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}	
	}
	
	@GET
	@Path("/ifCreadorI")
	public Response usuarioCreadorIniciativa(@QueryParam("iniciativa") String nombre, @QueryParam("user") String correo) {
		try {			
			if (u.usuarioCreadorIniciativa(nombre, correo))
				return Response.ok(true).build();
			else return Response.ok(false).build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}	
	}
	
	@GET
	@Path("/ifCreadorP")
	public Response usuarioCreadorProceso(@QueryParam("proceso") String nombre, @QueryParam("user") String correo) {
		try {			
			if (u.usuarioCreadorProceso(nombre, correo))
				return Response.ok(true).build();
			else return Response.ok(false).build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}	
	}
	
	@GET
	@Path("/ifParticipaP")
	public Response usuarioParticipaProceso(@QueryParam("proceso") String nombre, @QueryParam("user") String correo) {
		try {			
			if (u.usuarioParticipaProceso(nombre, correo))
				return Response.ok(true).build();
			else return Response.ok(false).build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}
	}
	
	@GET
	@Path("/participacion")
	public Response usuarioVerParticipacion(@QueryParam("proceso") String nombre, @QueryParam("user") String correo) {
		try {			
			if (u.usuarioParticipaProceso(nombre, correo)) 
				return Response.ok(u.verParticipacion(nombre, correo)).build();
			 else return Response.ok("Usuario no participó").build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}
	}	
	
	@GET
	@Path("/listarProcesos")
	public Response listarProcesosEnAlcance(@QueryParam("user") String correo) {
		try {
			List<DtProceso> lu = u.listarProcesosUsuarioEnAlcance(correo);				
			JSONArray jsLu = new JSONArray();	
			for (Object i: lu)
				jsLu.add(i);
			return Response.ok(jsLu).build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}	
	}
	
	@GET
	@Path("/listarCertificados")
	public Response listarCertificados(@QueryParam("user") String correo) {
		try {
			List<DtCertificado> lc = u.listarCertificadosUsuario(correo);				
			JSONArray jsLu = new JSONArray();	
			for (DtCertificado i: lc)
				jsLu.add(i);
			return Response.ok(jsLu).build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();			
		}	
	}
	
	@PUT
	@Path("/modificarCiudadano")
	public Response modificarUsuarioC(DtCiudadano user) {		
		if (user != null) {			
			try {	
				if (u.checkIdUser(user)) {
					u.modificarUsuario(user, null, null, null, null);
					user = (DtCiudadano) u.buscarUsuario(user.getCorreo());
					return Response.ok("usuario modificado" + user).build();	
				}
			} catch (Exception e) {
				return Response.ok(e.getMessage()).build();
			}
		} else return Response.ok("Datos mal").build();		
		return Response.status(403).build();	
	}	
	
	@PUT
	@Path("/modificarFuncionario")
	public Response modificarUsuarioF(DtFuncionario user) {
		if (user != null) {			
			try {
				if (u.checkIdUser(user)) {
					u.modificarUsuario(user, null, user.getOrganismo(), user.getCargo(), null);
					user = (DtFuncionario) u.buscarUsuario(user.getCorreo());
					return Response.ok("usuario modificado" + user).build();
				}
			} catch (Exception e) {
				return Response.ok(e.getMessage()).build();
			}				
		} else return Response.ok("Datos mal").build();
		return Response.status(403).build();
	}
	
	@DELETE
	@Path("/eliminarMensaje")
	public Response eliminarMensaje(@QueryParam("user") String user, @QueryParam("mensaje") String mensaje) {		
		try {	
			if (user != null) {		
				if (mensaje != null) {									
					u.eliminarMensaje(user, mensaje);
					return Response.ok("Mensaje eliminado").build();				
				} else return Response.ok("Dato mensaje null").build();
			} else return Response.ok("Dato user null").build();	
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();
		}
	}
	
	@DELETE
	@Path("/vaciarMensajes")
	public Response eliminarMensaje(@QueryParam("user") String user) {		
		try {	
			if (user != null) {												
				u.eliminarMensajes(user);
				return Response.ok("Mensajes eliminados").build();				
			} else return Response.ok("Dato user null").build();	
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();
		}
	}		
	
	@GET
	@Path("/pingPDI")
	public Response pingPDI(@QueryParam("cedula") String cedula) {
		try {			
			u.altaCiudadano(cedula);
			return Response.ok("PDI: " + cedula).build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();
		}	
	}	
	
}

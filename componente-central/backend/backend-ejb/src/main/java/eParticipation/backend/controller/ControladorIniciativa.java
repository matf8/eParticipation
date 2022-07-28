package eParticipation.backend.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import eParticipation.backend.dto.DtIniciativa;
import eParticipation.backend.dto.DtIniciativaEv;
import eParticipation.backend.excepciones.ComentarioException;
import eParticipation.backend.excepciones.IniciativaException;
import eParticipation.backend.excepciones.UsuarioException;
import eParticipation.backend.model.Estado;
import eParticipation.backend.model.Iniciativa;
import eParticipation.backend.service.IniciativaService;
import eParticipation.backend.service.InsigniaService;
import eParticipation.backend.service.NotificacionService;
import eParticipation.backend.service.PerifericoService;
import eParticipation.backend.service.PersistenciaLocal;
import eParticipation.backend.service.PersistenciaNSQLocal;

@Stateless
public class ControladorIniciativa implements IniciativaService {
	
	@EJB private PersistenciaLocal bd;	
	@EJB private PersistenciaNSQLocal nsql;	
	@EJB private InsigniaService is;		
	@EJB private NotificacionService ns;	
	@EJB private PerifericoService ps;
	
	public ControladorIniciativa() {	}	
	
	public void comentariosIniciales() {
		try {
			comentarIniciativa("Remodelar museo del fútbol", "manu.biurrun@gmail.com", "la verdad, muy mal estado");
			comentarIniciativa("Remodelar museo del fútbol", "rodrigoturak@gmail", "modernicemos nuestro museo");
			comentarIniciativa("Remodelar museo del fútbol", "maria@gmail", "Muy buena iniciativa, ojalá lo logren");
			comentarIniciativa("Remodelar museo del fútbol", "wr15@gmail", "Traigan vino q copas sobran!");
			comentarIniciativa("Venta garage mensual", "maria@gmail", "Se puede llevar electrodomésticos?");
			comentarIniciativa("Venta garage mensual", "gimekalt@gmail", "Me encanta, voy!!");
			comentarIniciativa("Bicicleteada por Battle & Ordoñez", "rubenstol@gmail", "yendo no, llegando");			
			comentarIniciativa("Bicicleteada por Battle & Ordoñez", "rodrigoturak@gmail", "tuki; los q sobrevivan,la proxima es Rivera");
			comentarIniciativa("Paseo por La Pataia", "maria@gmail", "el mejor dulce de leche de verdad");
		} catch (Exception e) {
			System.out.println("inicializando comentarios: " + e.getMessage());
		}
	}

	public void altaIniciativa(String nombre, String descripcion, String fecha, String creador, String recurso) throws Exception {		
		if (nombre != null) {
			try {	
				String nodo = null;
				if (creador.contains("-")) {
					String[] parts = creador.split("\\-");				
					creador = parts[0];
					nodo = parts[1];			
				}		
				if (bd.checkIniciativaDisp(nombre) == null) {
					Estado estado = Estado.En_espera;
					Date d = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);						
					List<String> m = new ArrayList<>();
					if (recurso != null) {
						String id = nsql.subirRecurso(recurso);
						System.out.println(id);
						m.add(id);
					}					
					Iniciativa i = new Iniciativa(nombre, descripcion, d, creador, estado, m);		
					bd.altaIniciativa(i);					
					if (nodo != null) 
						ps.saveNodoCreador(creador, nombre, nodo);					
					is.obtenerCertificadoDeParticipacion(creador, "Creadores");	
					is.obtenerCertificadoDeParticipacion(creador, null);		// cert proactive; reachin 100 participations
				} else throw new IniciativaException("La iniciativa ya existe con el nombre: " + nombre); 
			} catch (Exception e) {
				throw e;
			}
		} else throw new Exception("nombre vacio");
		
	}
				
	public String evaluarIniciativa(DtIniciativa d, String nodoEv) throws Exception {
		try {
			Iniciativa i = bd.buscarIniciativa(d.getNombre());					
			Client client = ClientBuilder.newClient();
		  	String uriEvaluar = nodoEv+"/evaluar";
			WebTarget target = client.target(uriEvaluar);	
			Invocation invocation = target.request().buildPost(Entity.entity(i, MediaType.APPLICATION_JSON));
			Response res = invocation.invoke();
			if (res.getStatus() == 200) {
				DtIniciativaEv ev = res.readEntity(DtIniciativaEv.class);	
				if (ev != null) {
					System.out.println(ev.toString());
					if (ev.isAceptada()) {						
						bd.modificarIniciativa(d.getNombre());
						is.obtenerCertificadoDeParticipacion(d.getCreador(), "Creadores");	
						is.obtenerCertificadoDeParticipacion(d.getCreador(), null);		// cert proactive; reachin 100 participations
						if (d.getCreador().contains("ORG")) {
							String nodoOrg = notificarOrgCreadora(d);							
							ns.notificaIniciativaCambio(d.getNombre(), nodoOrg);
						} else ns.notificaIniciativaCambio(d.getNombre(), d.getCreador());							
					}	
					client.close();
					return ev.getDescripcionEvaluacion();	
				} return "Problema con evaluación";					
			} throw new Exception("evaluar not 200 ok");
		} catch (Exception e) {
			throw e;
		}		
	}
	
	private String notificarOrgCreadora(DtIniciativa d) throws Exception {
		String nodoOrg = null;
		try {
			nodoOrg = ps.getNodoCreador(d.getCreador(), d.getNombre());		
			if (nodoOrg != null) {
				List<String> uris = ps.getUrisOrganizacion();
				if (nodoOrg.equals("heroku") || nodoOrg.equals("local") || nodoOrg.equals("ukoreh")) {
					int it = 0;
					boolean encontre = false;
					String k = null;
					if (nodoOrg.equals("ukoreh")) {										
						while (it < uris.size() && !encontre) {											
							k = uris.get(it);
							if (k.contains("-2")) {
								nodoOrg = k;
								encontre = true;
							}
							it++;
						}
					} else {
						it = 0;
						while (it < uris.size() && !encontre) {											
							k = uris.get(it);
							if (k.contains(nodoOrg)) {
								nodoOrg = k; 
								encontre = true;
							}
							it++;
						}	
					}
					System.out.println(nodoOrg);
					if (encontre == false)
						throw new Exception("problema eligiendo el nodo");	
					else return nodoOrg;
				}
			} throw new Exception ("notificar: Nodo periferico organizacion no encontrado");
		} catch (Exception e) {
			throw new Exception ("notificar: " + e.getMessage());
		}		
	}

	public DtIniciativa buscarIniciativa(String nombre) throws Exception {
		Iniciativa i = null;
		if (nombre != null) { 
			i = bd.buscarIniciativa(nombre);
			if (i != null) {
				DtIniciativa k = i.getDt();
				if (i.getIdRecurso().size() > 0)
					k.setRecurso(nsql.getRecursoIniciativa(i.getIdRecurso().get(0)));
				if (i.getIdComentario().size() > 0) 
					for (int c = 0; c < i.getIdComentario().size(); c++)			
						k.agregarComentario(nsql.getComentarios(i.getIdComentario().get(c)));
				return k;
			} else return null;
		} else return null;	
	}
	
	public List<DtIniciativa> listarIniciativas() throws Exception {
		return bd.listarIniciativas();
	}	

	public List<DtIniciativa> listarIniciativasPublicadas() throws Exception {
		return bd.listarIniciativasPublicadas();		
	}
	
	public List<DtIniciativa> listarIniciativasFechas(String fechaInicial, String fechaFinal) throws Exception {
		Date dInicio, dFinal;
		dInicio = dFinal = null;		
		try {
			dInicio = new SimpleDateFormat("yyyy-MM-dd").parse(fechaInicial);
			dFinal = new SimpleDateFormat("yyyy-MM-dd").parse(fechaFinal);
			return bd.listarIniciativasFechas(dInicio, dFinal);
		} catch (Exception e) {
			throw e;
		}				
	}
		
	public void adherirUsuario(String nombreIniciativa, String user) throws Exception {	
		try {
			if (buscarIniciativa(nombreIniciativa) == null)
				throw new IniciativaException("Iniciativa no existe");
			else {
				if (bd.buscarUsuario(user) == null) 
					throw new UsuarioException("Usuario no existe");
				else {
					bd.adherirUsuarioIniciativa(nombreIniciativa, user);
					is.obtenerCertificadoDeParticipacion(user, "Adhesiones");
					is.obtenerCertificadoDeParticipacion(user, null);		// cert proactive; reachin 100 participations
				}
			} 
		} catch (Exception e) {
			throw e;
		}				
	}

	public void desadherirUsuario(String nombreIniciativa, String user) throws Exception {	
		try {
			if (buscarIniciativa(nombreIniciativa) == null)
				throw new IniciativaException("Iniciativa no existe");
			else {
				if (bd.buscarUsuario(user) == null) 
					throw new UsuarioException("Usuario no existe");
				else 
					bd.desadherirUsuarioIniciativa(nombreIniciativa, user);					
			} 
		} catch (Exception e) {
			throw e;
		}				
	}	

	public void agregarSeguidor(String iniciativa, String correo) throws Exception {
		try {
			if (buscarIniciativa(iniciativa) == null)
				throw new IniciativaException("Iniciativa no existe");
			else {
				if (bd.buscarUsuario(correo) == null) 
					throw new UsuarioException("Usuario no existe");
				else {
					bd.seguirUsuarioIniciativa(iniciativa, correo);
					is.obtenerCertificadoDeParticipacion(correo, "Seguidores");
					is.obtenerCertificadoDeParticipacion(correo, null);		// cert proactive; reachin 100 participations
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void quitarSeguidor(String iniciativa, String correo) throws Exception  {
		try {
			if (buscarIniciativa(iniciativa) == null)
				throw new IniciativaException("Iniciativa no existe");
			else {
				if (bd.buscarUsuario(correo) == null) 
					throw new UsuarioException("Usuario no existe");
				else bd.dejarSeguirUsuarioIniciativa(iniciativa, correo);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<String> listarAdheridos(String iniciativa) {		
		return bd.listarAdheridosIniciativa(iniciativa);
	}


	public List<String> listarSeguidores(String iniciativa) {
		return bd.listarSeguidoresIniciativa(iniciativa);
	}

	public List<DtIniciativa> listarRelacionados() {
		return bd.listarRelacionadosIniciativas();
	}

	public int contarIniciativas() {
		return bd.contarIniciativas();
	}

	public int contarAdhesiones() {
		return bd.contarAdhesiones();
	}
	
	public int contarSeguidores() {
		return bd.contarSeguidores();
	}
	
	public int contarComentarios() {
		return bd.contarComentarios();
	}	
	
	public void bajaIniciativa(String nombre) throws Exception {
		if (nombre != null) 
			bd.bajaIniciativa(nombre);		
	}
	
	public void modificarIniciativa(DtIniciativa n) {
		if (n != null) {
			Iniciativa i = bd.buscarIniciativa(n.getNombre());
			if (i == null) // modifico el nombre
				i = bd.buscarIniciativa(Long.valueOf(n.getId()));			
			try {
				if (n.getRecurso() != null && !n.getRecurso().equals("")) {					
					if (i.getIdRecurso().size() > 0) {
						nsql.updateRecurso(i.getIdRecurso().get(0), n.getRecurso());
						bd.modificarIniciativa(n);	
					} else if (i.getIdRecurso().size() == 0) {
						String id = nsql.subirRecurso(n.getRecurso());							
						i.agregarRecurso(id);
						i.setNombre(n.getNombre());
						Date fc = new SimpleDateFormat("yyyy-MM-dd").parse(n.getFecha());	
						i.setFecha(fc);
						i.setDescripcion(n.getDescripcion());
						i.setEstado(Estado.valueOf(n.getEstado()));
						bd.modificarIniciativa(i);	
					}
				} else bd.modificarIniciativa(n);
			} catch (Exception e) {
				System.out.println("modificar iniciativa: " + e.getMessage());
			}
		}
	}
		
	public void comentarIniciativa(String nombreIniciativa, String correo, String comentario) throws Exception {
		if (nombreIniciativa != null) {
			if (correo != null) {
				if (bd.buscarUsuario(correo) != null) {
					if (comentario != null && !comentario.equals("")) {
						try {							
							if (bd.buscarIniciativa(nombreIniciativa) != null) {
								String id = nsql.subirComentarios(comentario, correo);	
								Iniciativa k = bd.buscarIniciativa(nombreIniciativa);
								k.agregarComentario(id);
								bd.modificarIniciativa(k);
								is.obtenerCertificadoDeParticipacion(correo, "Comentarios");	
								is.obtenerCertificadoDeParticipacion(correo, null);		// cert proactive; reachin 100 participations
							} else throw new IniciativaException("Iniciativa no existe");							
						} catch (Exception e) {
							throw e;
						}					
					} else throw new ComentarioException("Comentario no puede ser vacío");
				} else throw new UsuarioException("Usuario no existe");			
			}		
		}
	}

	public void borrarComentarioIniciativa(String comentario, String ini) {
		try {
			String id = nsql.borrarComentario(comentario);	
			System.out.println(comentario + ini + id);
			Iniciativa i = bd.buscarIniciativa(ini);
			List<String> lI = i.getIdComentario();
			if (lI.size() > 0) {
				for (String k: lI)
					if (k.equals(id))
						i.quitarComentario(id);					
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("borrar comentario iniciativa: " + e.getMessage());
		}		
	}		
}

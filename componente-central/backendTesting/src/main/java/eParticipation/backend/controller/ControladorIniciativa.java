package eParticipation.backend.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eParticipation.backend.dto.DtIniciativa;
import eParticipation.backend.excepciones.ComentarioException;
import eParticipation.backend.excepciones.IniciativaException;
import eParticipation.backend.excepciones.UsuarioException;
import eParticipation.backend.model.Estado;
import eParticipation.backend.model.Iniciativa;
import eParticipation.backend.service.Fabrica;
import eParticipation.backend.service.IniciativaService;
import eParticipation.backend.service.InsigniaService;
import eParticipation.backend.service.PersistenciaLocal;

public class ControladorIniciativa implements IniciativaService {	
	
	Fabrica f = Fabrica.getInstancia();	
	PersistenciaLocal bd = f.getPersistenciaService();	
	InsigniaService is = f.getInsigniaService();

public ControladorIniciativa() {	}	
	
	public void altaIniciativa(String nombre, String descripcion, String fecha, String creador, String recurso) throws Exception {		
		if (nombre != null) {
			try {				
				if (bd.checkIniciativaDisp(nombre) == null) {
					Estado estado = Estado.En_espera;
					Date d = null;
					try {
						d = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
					} catch (ParseException e) {
						e.getMessage();
					}	
					List<String> m = new ArrayList<>();
					if (recurso != null) {
						//String id = nsql.subirRecurso(recurso);
						String id = "1";
						m.add(id);
					}					
					Iniciativa i = new Iniciativa(nombre, descripcion, d, creador, estado, m);		
					bd.altaIniciativa(i);					
					is.obtenerCertificadoDeParticipacion(creador, "Creadores");	
				} else throw new IniciativaException("La iniciativa ya existe con el nombre: " + nombre);	
			} catch (Exception e) {
				throw e;
			}
		} else throw new Exception("nombre vacio");
		
	}
			
	public String evaluarIniciativa(DtIniciativa d) throws Exception {
		/*try {
			Iniciativa i = bd.buscarIniciativa(d.getNombre());					
			Client client = ClientBuilder.newClient();		
			//String nodo = ps.elegirNodoRandom("e");
		  	String uriEvaluar = "http://localhost:8786"+"/evaluar";
			WebTarget target = client.target(uriEvaluar);	
			Invocation invocation = target.request().buildPost(Entity.entity(i, MediaType.APPLICATION_JSON));
			Response res = invocation.invoke();
			DtIniciativaEv ev = res.readEntity(DtIniciativaEv.class);
			if (ev != null) {
				if (ev.isAceptada()) {			
					bd.modificarIniciativa(d.getNombre());
					ns.notificaIniciativaCambio(d.getNombre());
					return ev.getDescripcionEvaluacion();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	*/	
		return "Evaluador no disponible, reintente luego.";		
	}
	
	public DtIniciativa buscarIniciativa(String nombre) throws Exception {
		Iniciativa i = null;
		if (nombre != null) { 
			i = bd.buscarIniciativa(nombre);
			if (i != null) {
				DtIniciativa k = i.getDt();
				/*if (i.getIdRecurso().size() > 0)
					k.setRecurso(nsql.getRecursoIniciativa(i.getIdRecurso().get(0)));
				if (i.getIdComentario().size() > 0) 
					for (int c = 0; c < i.getIdComentario().size(); c++)			
						k.agregarComentario(nsql.getComentariosIniciativa(i.getIdComentario().get(c)));*/
				return k;
			} else return null;
		} else return null;	
	}
	
	public List<DtIniciativa> listarIniciativas() throws Exception {
		return bd.listarIniciativas();
	}
	
	public List<DtIniciativa> listarIniciativasFechas(String fechaInicial, String fechaFinal) throws Exception {
		Date dInicio, dFinal;
		dInicio = dFinal = null;		
		try {
			dInicio = new SimpleDateFormat("dd/MM/yyyy").parse(fechaInicial);
			dFinal = new SimpleDateFormat("dd/MM/yyyy").parse(fechaFinal);
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
	
	public void bajaIniciativa(String nombre) {
		if (nombre != null) {
			bd.bajaIniciativa(nombre);
		}
	}
	
	public void modificarIniciativa(DtIniciativa n) {
		if (n != null) {
			Iniciativa i = bd.buscarIniciativa(n.getNombre());
			if (i == null) // modifico el nombre
				i = bd.buscarIniciativa(Long.valueOf(n.getId()));			
			try {
				if (n.getRecurso() != null && !n.getRecurso().equals("")) {					
					if (i.getIdRecurso().size() > 0) {
						//nsql.updateRecurso(i.getIdRecurso().get(0), n.getRecurso());
						bd.modificarIniciativa(n);	
					} else if (i.getIdRecurso().size() == 0) {
						//String id = nsql.subirRecurso(n.getRecurso());	
						String id = "1";
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
								//String id = nsql.subirComentarios(comentario, correo);	
								String id = "1";
								Iniciativa k = bd.buscarIniciativa(nombreIniciativa);
								k.agregarComentario(id);
								bd.modificarIniciativa(k);
							} else throw new IniciativaException("Iniciativa no existe");							
						} catch (Exception e) {
							throw e;
						}					
					} else throw new ComentarioException("Comentario no puede ser vacío");
				} else throw new UsuarioException("Usuario no existe");			
			}		
		}
	}
	

}


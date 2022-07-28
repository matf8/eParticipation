package eParticipation.backend.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import eParticipation.backend.model.Ciudadano;
import eParticipation.backend.model.Funcionario;
import eParticipation.backend.model.Iniciativa;
import eParticipation.backend.dto.DtParticipa;
import eParticipation.backend.dto.DtProceso;
import eParticipation.backend.model.FaseProceso;
import eParticipation.backend.excepciones.ComentarioException;
import eParticipation.backend.excepciones.IniciativaException;
import eParticipation.backend.excepciones.ProcesoException;
import eParticipation.backend.excepciones.UsuarioException;
import eParticipation.backend.model.ProcesoParticipativo;
import eParticipation.backend.service.InsigniaService;
import eParticipation.backend.service.PersistenciaLocal;
import eParticipation.backend.service.PersistenciaNSQLocal;
import eParticipation.backend.service.ProcesoService;
import eParticipation.backend.service.UsuarioService;

@Stateless
public class ControladorProceso implements ProcesoService {
	
	@EJB private PersistenciaLocal bd;	
	@EJB private PersistenciaNSQLocal nsql;
	@EJB private UsuarioService us;
	@EJB private InsigniaService is;	
			
	// solo los funcionarios pueden dar de alta
	public void altaProceso(DtProceso p) throws Exception {
		try {
			Object k = bd.buscarUsuario(p.getCreador());		
			if (k != null)
				if (k instanceof Funcionario) 	
					if (buscarProceso(p.getNombre()) == null) {			
						Date d = new SimpleDateFormat("yyyy-mm-dd").parse(p.getFecha());										
						List<String> m = new ArrayList<>();
						if (p.getInstrumento() != null) {
							String [] s = p.getContenidoInstrumento();						
							List<String> lS = Arrays.asList(s);
							String id = nsql.subirInstrumento(p.getInstrumento(), lS);						
							m.add(id);						
						}
						ProcesoParticipativo pp = new ProcesoParticipativo(p.getNombre(), d, p.getDescripcionAlcance(), m, FaseProceso.valueOf(p.getFase()), p.getCreador());			
						bd.altaProceso(pp);			
					} else throw new ProcesoException("Proceso ya existe");
				else throw new UsuarioException("Usuario no es funcionario");
			else throw new UsuarioException("Usuario no existe");
		} catch (Exception e) {
			throw e;
		}				
	}
		
	public DtProceso buscarProceso(String nombre) throws Exception {
		if (nombre != null) {
			ProcesoParticipativo d = bd.buscarProceso(nombre);			
			if (d != null) {				
				DtProceso k = d.getDt();
				if (d.getIdInstrumento().size() > 0) {
					List<String> lD = nsql.getInstrumentoProceso(d.getIdInstrumento().get(0));				
					k.setInstrumento(lD.get(0));
					lD.remove(lD.get(0));
					String[] array = lD.toArray(new String[0]);
					k.setContenidoInstrumento(array);
				}
				if (d.getIdComentario().size() > 0) 
					for (int c = 0; c < d.getIdComentario().size(); c++)			
						k.agregarComentario(nsql.getComentarios(d.getIdComentario().get(c)));
				
				return k;
			}				
		}
		return null;
	}
		
	public List<DtProceso> listarProcesos() throws Exception {
		return bd.listarProcesos();
	}
	
	public int contarProcesos() {
		return bd.contarProcesos();
	}
	
	public void bajaProceso(String nombre) throws Exception {
		if (nombre != null) {
			bd.bajaProceso(nombre);
		}		
	}
	
	public void modificarProceso(DtProceso p) throws Exception {
		if (p != null) {
			try {
				bd.modificarProceso(p);
				if (p.getContenidoInstrumento() != null) {
					List<String> l = Arrays.asList(p.getContenidoInstrumento());
					updateInstrumento(p.getNombre(), l);
				}
			} catch (Exception e) {
				throw e;
			}
		}
	}
	
	private void updateInstrumento(String nombreProceso, List<String> l) throws Exception {
		if (nombreProceso != null) {			
			if (buscarProceso(nombreProceso) != null) {
				String id = bd.buscarProceso(nombreProceso).getIdInstrumento().get(0);
				nsql.updateInstrumento(id, l);
			} else throw new ProcesoException("Proceso no existe");
			
		} else throw new Exception("dato null");
	}
	
	public void agregarParticipanteProceso(DtParticipa a) throws Exception {		
		if (a.getProceso() != null) {
			try {
				ProcesoParticipativo p = bd.buscarProceso(a.getProceso());
				if (p !=null) {
					if (a.getUser() != null) {
						Ciudadano c = (Ciudadano) bd.buscarUsuario(a.getUser());
						if (c != null) {
							if (us.enAlcanceDeProceso(a.getUser(), p.getNombre())[2] == 1) {							
								List<Ciudadano> lC = p.getParticipantes();
								if (!lC.contains(c)) {			
									p.agregarUsuarioParticipante(c);
									bd.persistParticipante(p);
									is.obtenerCertificadoDeParticipacion(c.getCorreo(), "ParticiparProceso");
									is.obtenerCertificadoDeParticipacion(c.getCorreo(), null);		// cert proactive; reachin 100 participations
									if (p.getIdInstrumento().size() > 0)
										nsql.subirRespuesta(p.getIdInstrumento().get(0), a);
									else throw new ProcesoException("Proceso no tiene instrumento para participar");
								} else throw new UsuarioException("Usuario ya participa");		
							} else throw new UsuarioException("Usuario no puede participar debido a que no cumple la condición de alcance");	
						} else throw new UsuarioException("Usuario no encontrado");	
					}				
				} else throw new ProcesoException("Proceso no encontrado");
			} catch (Exception e) {
				throw e;
			}			
		}
	}

	public void quitarParticipanteProceso(String proceso, String correo) throws Exception {
		bd.quitarParticipanteProceso(proceso, correo);
	}
	
	public List<DtProceso> listarProcesosFechas(String fechaInicial, String fechaFinal) throws Exception {
		Date dInicio, dFinal;
		dInicio = dFinal = null;		
		try {
			dInicio = new SimpleDateFormat("yyyy-MM-dd").parse(fechaInicial);
			dFinal = new SimpleDateFormat("yyyy-MM-dd").parse(fechaFinal);
			return bd.listarProcesosFechas(dInicio, dFinal);
		} catch (Exception e) {
			throw e;
		}		
	}

	public void agregarSeguidor(String proceso, String correo) throws Exception {
		try {
			if (buscarProceso(proceso) == null)
				throw new IniciativaException("Proceso no existe");
			else {
				if (bd.buscarUsuario(correo) == null) 
					throw new UsuarioException("Usuario no existe");
				else bd.seguirUsuarioProceso(proceso, correo);				
			}
		} catch (Exception e) {
			throw e;
		}	
	}

	public void quitarSeguidor(String proceso, String correo) throws Exception {
		try {
			if (buscarProceso(proceso) == null)
				throw new IniciativaException("Proceso no existe");
			else {
				if (bd.buscarUsuario(correo) == null) 
					throw new UsuarioException("Usuario no existe");
				else bd.dejarSeguirUsuarioProceso(proceso, correo);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public List<String> listarSeguidores(String proceso) {
		return bd.listarSeguidoresProceso(proceso);
	}
	
	public void comentarProceso(String proceso, String user, String comentario) throws Exception {
		if (proceso != null) {
			if (user != null) {
				if (bd.buscarUsuario(user) != null) {
					if (comentario != null && !comentario.equals("")) {
						try {	
							ProcesoParticipativo k = bd.buscarProceso(proceso);
							if (k != null) {
								String id = nsql.subirComentarios(comentario, user);									
								k.agregarComentario(id);
								bd.persistComentario(k);
								is.obtenerCertificadoDeParticipacion(user, "Comentarios");	
								is.obtenerCertificadoDeParticipacion(user, null);		// cert proactive; reachin 100 participations
							} else throw new IniciativaException("Proceso no existe");							
						} catch (Exception e) {
							throw e;
						}					
					} else throw new ComentarioException("Comentario no puede ser vacío");
				} else throw new UsuarioException("Usuario no existe");			
			}		
		}	
	}
	
	public String resultVotacion(String proceso) throws Exception {
		if (proceso != null) {
			ProcesoParticipativo p = bd.buscarProceso(proceso);			
			return nsql.getResultVotacion(p.getIdInstrumento().get(0));
		} else return null;
	}
	
	public String resultEncuesta(String proceso) throws Exception {
		if (proceso != null) {
			ProcesoParticipativo p = bd.buscarProceso(proceso);
			return nsql.getResultEncuesta(p.getIdInstrumento().get(0));
		} else return null;
	}
	
	public void datosIniciales() {
		try {		    
		    String instrumentoV = "votacion";
		    String[] contenidoInstrumentoV = {
		    		"option:'si',votes:0",
	                "option:'en contra',votes:0",                  
	                "¿A favor de la eutanasia?" };		    
		    DtProceso p = new DtProceso("Eutanasia", "2022-07-30", "18", "Publicado", "susi22@gmail");
		    p.setInstrumento(instrumentoV);
		    p.setContenidoInstrumento(contenidoInstrumentoV); 		    		    
			altaProceso(p);	
			
			String[] contenidoInstrumentoV2 = {
			    		"option:'si',votes:0",
		                "option:'no',votes:0",                  
		                "¿Las ciclovías valen la pena?" };	
			p = new DtProceso("Ciclovías", "2022-07-30", "Montevideo,Canelones,Colonia", "Publicado", "jsff@gmail");
		    p.setInstrumento(instrumentoV);
		    p.setContenidoInstrumento(contenidoInstrumentoV2); 		    		    
			altaProceso(p);	
			
			String instrumentoE = "encuesta";
			String[] contenidoInstrumentoE = {
			    		"option:'Muy de acuerdo',votes:0",
		                "option:'No,podrian pensar otras atracciones',votes:0", 
		                "option:'Tal vez,queremos mas info',votes:0",
		                "option:'Inviable',votes:0", 
		                "¿Nuevo camping en Rocha?"};
			p = new DtProceso("Nuevo camping en Rocha", "2022-11-30", "15", "Publicado", "camizf@gmail");
		    p.setInstrumento(instrumentoE);
		    p.setContenidoInstrumento(contenidoInstrumentoE); 		    		    
			altaProceso(p);
						
			String[] respuesta1 = {"votacion","¿A favor de la eutanasia?","no"};
			DtParticipa dtp = new DtParticipa("Eutanasia", "jaimecoll@gmail", respuesta1);
			agregarParticipanteProceso(dtp);
			String[] respuesta2 = {"votacion","¿A favor de la eutanasia?","no"};
			dtp = new DtParticipa("Eutanasia", "rubenstol@gmail", respuesta2);
			agregarParticipanteProceso(dtp);
			String[] respuesta3 = {"votacion","¿A favor de la eutanasia?","si"};
			dtp = new DtParticipa("Eutanasia", "rodrigoturak@gmail", respuesta3);
			agregarParticipanteProceso(dtp);
			String[] respuesta4 = {"votacion","¿A favor de la eutanasia?","si"};
			dtp = new DtParticipa("Eutanasia", "wr15@gmail", respuesta4);
			agregarParticipanteProceso(dtp);
			String[] respuesta5 = {"votacion","¿A favor de la eutanasia?","no"};
			dtp = new DtParticipa("Eutanasia", "hugoDD@gmail", respuesta5);
			agregarParticipanteProceso(dtp);
			String[] respuesta6 = {"votacion","¿A favor de la eutanasia?","no"};
			dtp = new DtParticipa("Eutanasia", "mc22@gmail", respuesta6);
			agregarParticipanteProceso(dtp);			
		    String[] contenidoInstrumentoVM1 = {
		    		"option:'si',votes:2",
	                "option:'en contra',votes:4",                  
	                "¿A favor de la eutanasia?" };		    
		    DtProceso dtpr = buscarProceso("Eutanasia");
		    dtpr.setContenidoInstrumento(contenidoInstrumentoVM1); 		    		    
			modificarProceso(dtpr);	
			comentarProceso("Eutanasia", "maria@gmail", "tema delicado, siempre a favor de la vida.");
			comentarProceso("Eutanasia", "romicalero@gmail", "cuando el daño cerebral es permanente, es entendible.");

			String[] respuesta7 = {"encuesta","¿Nuevo camping en Rocha?","Muy de acuerdo"};
			dtp = new DtParticipa("Nuevo camping en Rocha", "jaimecoll@gmail", respuesta7);
			agregarParticipanteProceso(dtp);
			String[] respuesta8 = {"encuesta","¿Nuevo camping en Rocha?","Muy de acuerdo"};
			dtp = new DtParticipa("Nuevo camping en Rocha", "mc22@gmail", respuesta8);
			agregarParticipanteProceso(dtp);
			String[] respuesta9 = {"encuesta","¿Nuevo camping en Rocha?","Muy de acuerdo"};
			dtp = new DtParticipa("Nuevo camping en Rocha", "maria@gmail", respuesta9);
			agregarParticipanteProceso(dtp);
			String[] respuesta10 = {"encuesta","¿Nuevo camping en Rocha?","No, podrian pensar otras atracciones"};
			dtp = new DtParticipa("Nuevo camping en Rocha", "gimekalt@gmail", respuesta10);
			agregarParticipanteProceso(dtp);	
			String[] contenidoInstrumentoVE1 = {
			    		"option:'Muy de acuerdo',votes:3",
		                "option:'No, podrian pensar otras atracciones',votes:1", 
		                "option:'Tal vez, queremos mas info',votes:0",
		                "option:'Inviable',votes:0", 
		                "¿Nuevo camping en Rocha?"};	    
			dtpr = buscarProceso("Nuevo camping en Rocha");			
			dtpr.setContenidoInstrumento(contenidoInstrumentoVE1); 		    		    
			modificarProceso(dtpr);	
			comentarProceso("Nuevo camping en Rocha", "rubenstol@gmail", "que sea más barato que Santa teresa");
			comentarProceso("Nuevo camping en Rocha", "gimekalt@gmail", "porque o que? Hagan un parque de diversiones. Rocha esta muerto");						
		} catch (Exception e) {
			System.out.println("inicializnado procesos: " + e.getMessage());
		}	
	}
	
	public void borrarComentarioProceso(String comentario, String proceso) {		
		try {
			String id = nsql.borrarComentario(comentario);	
			ProcesoParticipativo i = bd.buscarProceso(proceso);
			List<String> lI = i.getIdComentario();
			if (lI.size() > 0) {
				for (String k: lI)
					if (k.equals(id))
						i.quitarComentario(id);					
			}
		} catch (Exception e) {
			System.out.println("borrar comentario iniciativa: " + e.getMessage());
		}		
	}		
}

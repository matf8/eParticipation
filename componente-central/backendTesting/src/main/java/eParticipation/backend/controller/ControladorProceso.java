package eParticipation.backend.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eParticipation.backend.dto.DtParticipa;
import eParticipation.backend.dto.DtProceso;
import eParticipation.backend.excepciones.ComentarioException;
import eParticipation.backend.excepciones.IniciativaException;
import eParticipation.backend.excepciones.ProcesoException;
import eParticipation.backend.excepciones.UsuarioException;
import eParticipation.backend.model.Ciudadano;
import eParticipation.backend.model.Funcionario;
import eParticipation.backend.model.ProcesoParticipativo;
import eParticipation.backend.service.Fabrica;
import eParticipation.backend.service.PersistenciaLocal;
import eParticipation.backend.service.ProcesoService;
import eParticipation.backend.service.UsuarioService;

public class ControladorProceso implements ProcesoService {	

	Fabrica f = Fabrica.getInstancia();	
	PersistenciaLocal bd = f.getPersistenciaService();
	UsuarioService us = f.getUsuarioService();
	
	// solo los funcionarios pueden dar de alta
		public void altaProceso(DtProceso p) throws Exception {
			Object k = bd.buscarUsuario(p.getCreador());		
			if (k != null)
				if (k instanceof Funcionario) 	
					if (buscarProceso(p.getNombre()) == null) {			
						Date d = null;
						try {
							d = new SimpleDateFormat("yyyy-MM-dd").parse(p.getFecha());									
							List<String> m = new ArrayList<>();
						//if (p.getInstrumento() != null) {
						//	String id = nsql.subirInstrumento(p.getInstrumento());
						//	m.add(id);
							Long id = null;
							if (p.getId() == null)
								id = Long.valueOf(bd.listarProcesos().size())+1;
							else id = Long.parseLong(p.getId());
							ProcesoParticipativo pp = new ProcesoParticipativo(id, p.getNombre(), d, p.getDescripcionAlcance(), m, p.getFase(), p.getCreador());			
							bd.altaProceso(pp);	
					} catch (Exception e) {
						throw e;
					}		
					} else throw new ProcesoException("Proceso ya existe");
			 else throw new UsuarioException("Usuario no es funcionario");
			else throw new UsuarioException("Usuario no existe");
		}
			
		public DtProceso buscarProceso(String nombre) {
			if (nombre != null) {
				ProcesoParticipativo d = bd.buscarProceso(nombre);			
				if (d != null) {				
					DtProceso k = d.getDt();
					//if (d.getIdInstrumento().size() > 0)
					//	k.setInstrumento(nsql.getInstrumentoProceso(d.getIdInstrumento().get(0)));
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
		
		public void bajaProceso(String nombre) {
			if (nombre != null) {
				bd.bajaProceso(nombre);
			}		
		}
		
		public void modificarProceso(DtProceso p) {
			if (p != null) {
				bd.modificarProceso(p);
			}
		}
		
		public void agregarParticipanteProceso(DtParticipa a) throws Exception {		
			if (a.getProceso() != null) {
				try {
					ProcesoParticipativo p = bd.buscarProceso(a.getProceso());
					if (p !=null) {
						if (a.getUser() != null) {
							Ciudadano c = (Ciudadano) bd.buscarUsuario(a.getUser());
							if (c != null) {
								System.out.println(c.toString());
								if (us.enAlcanceDeProceso(a.getUser(), p.getNombre())[2] == 1) {							
									List<Ciudadano> lC = p.getParticipantes();
									if (!lC.contains(c)) {			
										p.agregarUsuarioParticipante(c);
										bd.persistParticipante(p);
										/*if (p.getIdInstrumento().size() > 0)
											nsql.subirRespuesta(p.getIdInstrumento().get(0),a);*/
										//else throw new ProcesoException("Proceso no tiene instrumento para participar");
									} else throw new UsuarioException("Usuario ya participa");		
								} else throw new UsuarioException("Usuario no puede participar debido a que no cumple la condición de alcance");	
							} else throw new UsuarioException("Usuario no encontrado");	
						}				
					}  else throw new ProcesoException("Proceso no encontrado");
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
								if (bd.buscarProceso(proceso) != null) {
									//String id = nsql.subirComentarios(comentario, user);	
									ProcesoParticipativo k = bd.buscarProceso(proceso);
									//k.agregarComentario(id);
									bd.modificarProceso(k);
								} else throw new IniciativaException("Proceso no existe");							
							} catch (Exception e) {
								throw e;
							}					
						} else throw new ComentarioException("Comentario no puede ser vacío");
					} else throw new UsuarioException("Usuario no existe");			
				}		
			}	
		}
		
}

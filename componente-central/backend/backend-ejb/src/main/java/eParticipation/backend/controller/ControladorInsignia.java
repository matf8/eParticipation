package eParticipation.backend.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import eParticipation.backend.dto.DtCertificado;
import eParticipation.backend.dto.DtCiudadano;
import eParticipation.backend.excepciones.CertificadoException;
import eParticipation.backend.model.Certificado;
import eParticipation.backend.model.Iniciativa;
import eParticipation.backend.model.TipoCertificado;
import eParticipation.backend.model.TipoInsignia;
import eParticipation.backend.service.InsigniaService;
import eParticipation.backend.service.PersistenciaLocal;
import eParticipation.backend.service.PersistenciaNSQLocal;
import eParticipation.backend.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;

@Stateless
public class ControladorInsignia implements InsigniaService {
	
	@EJB private PersistenciaLocal bd;	
	@EJB private PersistenciaNSQLocal nsql;	
	@EJB private UsuarioService us;
	
	private byte[] logo;
	
	public ControladorInsignia() {	}
	
	public List<String> getLogos() { 
		List<String> logos = null;
		try {
			logos = bd.logos();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logos;
	}	

	public void altaCertificado(String nombre, Integer nivel, String tipo, Integer momento) {		
		if (logo != null) {
			try {				
				TipoInsignia t = new TipoInsignia(logo, new Date());
				Certificado c = new Certificado(nombre, t, nivel, tipo);
				bd.altaCertificado(c);						
			} catch (Exception e) {
				e.printStackTrace();
			}	
		} 
	}
			
	public void sendLogo(byte[] file) {
		this.logo = file;
	}
	
	public void addTipo(String tipo) {
		if (tipo != null)
			bd.altaTipoCertificado(tipo);
	}
	
	public List<String> getTipos() {
		TipoCertificado l = TipoCertificado.getInstancia();
		return l.getTipos();	
	}	
	
	public void obtenerCertificadoDeParticipacion(String user, String tipo) {		
		try {
			DtCiudadano c = (DtCiudadano) us.buscarUsuario(user);				
			if (c != null) {				
				int cantAdheridas, cantSeguidas, cantCreadas, cantPartProceso;
				cantAdheridas = cantSeguidas = cantCreadas = cantPartProceso = 0;
				if (tipo == null) {
					int cantParticipaciones = 0;
					int cantAdheridasP = c.getIniAdheridas().size();					
					int cantCreadasP = c.getIniCreadas().size();
					int cantSeguidasP = c.getIniSeguidas().size();
					int cantPartProcesoP = c.getProPart().size();
					cantParticipaciones = cantAdheridasP + cantCreadasP + cantSeguidasP + cantPartProcesoP;				
					if (cantParticipaciones == 50) {						
						Certificado cert = bd.buscarCertificadoTipo(3, "Pro");
						if (cert != null) {
							try {
								bd.agregarCertificadoUsuario(c.getCorreo(), cert);
							} catch (Exception e2) {
								throw e2;
							}
						}					
					} 
				} else if (tipo.equals("Adhesiones")) {
					cantAdheridas = c.getIniAdheridas().size();					
					if (cantAdheridas > 0) {
						Certificado cert = manejadorTrigger(cantAdheridas, tipo);	
						if (cert != null) {
							try {
								bd.agregarCertificadoUsuario(c.getCorreo(), cert);
							} catch (Exception e2) {
								throw e2;
							}
						}					
					}	
				} else if (tipo.equals("Seguidores")) {
					try {
						cantSeguidas = c.getIniSeguidas().size();
						if (cantSeguidas > 0) {
							Certificado cert = manejadorTrigger(cantSeguidas, tipo);
							if (cert != null) 								
								bd.agregarCertificadoUsuario(c.getCorreo(), cert);
						}
					} catch (Exception e2) {
						throw e2;					
					}
				} else if (tipo.equals("Creadores")) {		
					try {
					cantCreadas = c.getIniCreadas().size();	
						int cantPublicadas = 0; 
						List<String> inis = c.getIniCreadas();
						for (String k: inis) {
							if (bd.buscarIniciativa(k).getEstado().toString().equals("Publicado")) 
								cantPublicadas++;						
						}					
						if (cantCreadas > 0) {
							Certificado cert = manejadorTrigger(cantPublicadas, tipo);
							if (cert != null) 
									bd.agregarCertificadoUsuario(c.getCorreo(), cert);								
							
						}	
					} catch (Exception e2) {
						throw e2;
					}
				} else if (tipo.equals("ParticiparProceso")) {
					try {
						cantPartProceso = c.getProPart().size();
						if (cantPartProceso > 0) {
							Certificado cert = manejadorTrigger(cantPartProceso, tipo);	
							if (cert != null) 							
								bd.agregarCertificadoUsuario(c.getCorreo(), cert);
						}
					} catch (Exception e2) {
						throw e2;					
					}					
				} else if (tipo.equals("Comentarios")) {
					try {
						int cantComentarios = 0;	
						List<Iniciativa> iniciativas = bd.listarIniciativasObject();						
						for (Iniciativa i: iniciativas) {
							List<String> comentarios = i.getIdComentario();
							for (String cx: comentarios) {
								if (nsql.getComentarioUsuario(cx, c.getCorreo()) != null) {
									cantComentarios++;
								}
							}
						}
						if (cantComentarios > 0) {
							Certificado cert = manejadorTrigger(cantComentarios, tipo);	
							if (cert != null) 							
								bd.agregarCertificadoUsuario(c.getCorreo(), cert);
						}
					} catch (Exception e2) {
						throw e2;					
					}					
				}
		    } else throw new Exception("Usuario a certificar no encontrado");
		} catch (Exception e) {	
			e.getMessage();
		}
					
	}
	
	private Certificado manejadorTrigger(int momento, String tipo) throws Exception {
		Certificado c = null;
		if (tipo != null) {
			if (tipo.equals("Seguidores")) {
				try {
					c = triggerSeguidores(momento);					
				} catch (Exception e) {
					throw e;
				}
			}
			else if (tipo.equals("Adhesiones")) {
				try {
					c = triggerAdheridos(momento);
				} catch (Exception e) {
					throw e;
				}
			}
			else if (tipo.equals("Creadores")) {
				try {
					c = triggerCreadas(momento);
				} catch (Exception e) {
					throw e;
				}
			}
			else if (tipo.equals("ParticiparProceso")) {
				try {
					c = triggerParticipaciones(momento);
				} catch (Exception e) {
					throw e;
				}
			} else if (tipo.equals("Comentarios")) {
				try {
					c = triggerComentarios(momento);
				} catch (Exception e) {
					throw e;
				}
			} else if (tipo != null) { 
				try {
					c = triggerPersonalizados(momento, tipo);
				} catch (Exception e) {
					throw e;
				}
			}
		} else throw new Exception("Tipo invalido");	
		return c;		
	}	
	
	private int momentoToNivel(int momento) {
		int nivel = 0;		
		if (momento == 10)
			nivel = 1;
		else if (momento >= 20)	// multiplo de 10
			nivel = momento/10;	// 2 3 4 5 6 7 8 ..	
		return nivel;
	}
	
	public Certificado triggerSeguidores(int momento) throws Exception {		
		try {
			if ((momento % 10) == 0) {
				int nivel = momentoToNivel(momento);
				Certificado r = bd.buscarCertificadoTipo(nivel, "Seguidores");
				if (r != null && r.getTipo().equals("Seguidores")) 
					return r;			
				else throw new CertificadoException("Certificado incorrecto");
			} else throw new CertificadoException("nivel con coma");
		} catch (Exception e) {
			throw e;			
		}
	}	
	
	public Certificado triggerAdheridos(int momento) throws Exception {
		try {
			if ((momento % 10) == 0) {
				int nivel = momentoToNivel(momento);
				Certificado r = bd.buscarCertificadoTipo(nivel, "Adhesiones");
				if (r != null && r.getTipo().equals("Adhesiones")) 
					return r;			
				else throw new CertificadoException("Certificado incorrecto");	
			} else throw new CertificadoException("nivel con coma");
		} catch (Exception e) {
			throw e;			
		}
	}
	
	public Certificado triggerCreadas(int momento) throws Exception {
		try {
			if ((momento % 10) == 0) {
				int nivel = momentoToNivel(momento);		
				Certificado r = bd.buscarCertificadoTipo(nivel, "Creadores");
				if (r != null && r.getTipo().equals("Creadores")) {
					System.out.println(r.toString());
					return r;			
				}
				else throw new CertificadoException("Certificado incorrecto");
			} else throw new CertificadoException("nivel con coma");
		} catch (Exception e) {
			throw e;
		}
	}
	
	public Certificado triggerComentarios(int momento) throws Exception {
		try {
			if ((momento % 10) == 0) {
				int nivel = momentoToNivel(momento);		
				Certificado r = bd.buscarCertificadoTipo(nivel,"Comentarios");
				if (r != null && r.getTipo().equals("Comentarios")) 
					return r;			
				else throw new CertificadoException("Certificado incorrecto");
			} else throw new CertificadoException("nivel con coma");
		} catch (Exception e) {
			throw e;
		}
	}
	
	public Certificado triggerParticipaciones(int momento) throws Exception {
		try {
			if ((momento % 10) == 0) {
				int nivel = momentoToNivel(momento);		
				Certificado r = bd.buscarCertificadoTipo(nivel, "ParticiparProceso");
				if (r != null && r.getTipo().equals("ParticiparProceso")) 
					return r;			
				else throw new CertificadoException("Certificado incorrecto");
			} else throw new CertificadoException("nivel con coma");	
		} catch (Exception e) {
			throw e;
		}
	}
	
	public Certificado triggerPersonalizados(int momento, String tipo) throws Exception {
		try {
			if ((momento % 10) == 0) {
				int nivel = momentoToNivel(momento);		
				Certificado r = bd.buscarCertificadoTipo(nivel, tipo);
				if (r != null && r.getTipo().equals(tipo)) 
					return r;			
				else throw new CertificadoException("Certificado incorrecto");
			} else throw new CertificadoException("nivel con coma");	
		} catch (Exception e) {
			throw e;
		}
	}

	public List<DtCertificado> listarDtCertificados() {
		return bd.listarDtCertificados();
	}

	
	public void borrarCertificado(String nombre) throws Exception {
		bd.borrarCertificado(nombre);
	}
		
	
}

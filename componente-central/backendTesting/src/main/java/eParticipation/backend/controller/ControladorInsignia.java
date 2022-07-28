package eParticipation.backend.controller;

import java.util.Date;
import java.util.List;

import eParticipation.backend.dto.DtCertificado;
import eParticipation.backend.dto.DtCiudadano;
import eParticipation.backend.excepciones.CertificadoException;
import eParticipation.backend.model.Certificado;
import eParticipation.backend.model.TipoCertificado;
import eParticipation.backend.model.TipoInsignia;
import eParticipation.backend.service.Fabrica;
import eParticipation.backend.service.InsigniaService;
import eParticipation.backend.service.PersistenciaLocal;
import eParticipation.backend.service.UsuarioService;

public class ControladorInsignia implements InsigniaService {

	Fabrica f = Fabrica.getInstancia();	
	PersistenciaLocal bd = f.getPersistenciaService();
	UsuarioService us = f.getUsuarioService();
	
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
				int cantAdheridas = c.getIniAdheridas().size();
				int cantCreadas = c.getIniCreadas().size();
				int cantSeguidas = c.getIniSeguidas().size();
				int cantParticipaciones = cantAdheridas + cantCreadas + cantSeguidas;
				if (cantCreadas > 0) {
					Certificado cert = manejadorTrigger(cantCreadas, tipo);
					if (cert != null) {
						try {
							bd.agregarCertificadoUsuario(c.getCorreo(), cert);
						} catch (Exception e2) {
							throw e2;
						}
					}
				}					
				if (cantSeguidas > 0) {
					Certificado cert = manejadorTrigger(cantSeguidas, tipo);
					try {
						bd.agregarCertificadoUsuario(c.getCorreo(), cert);
					} catch (Exception e2) {	}
				}
				if (cantAdheridas > 0) {
					Certificado cert = manejadorTrigger(cantAdheridas, tipo);			
					try {
						bd.agregarCertificadoUsuario(c.getCorreo(), cert);
					} catch (Exception e2) {	}
				}					
				if (cantParticipaciones > 0) {
					Certificado cert = manejadorTrigger(cantParticipaciones, tipo);
					try {
						bd.agregarCertificadoUsuario(c.getCorreo(), cert);
					} catch (Exception e2) {	}
				}
			}
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
			else if (tipo != null) {
				try {
					c = triggerParticipaciones(momento, tipo);
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
				Certificado r = bd.buscarCertificadoTipo(nivel,"Seguidores");
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
				Certificado r = bd.buscarCertificadoTipo(nivel,"Adhesiones");
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
				Certificado r = bd.buscarCertificadoTipo(nivel,"Creadores");
				if (r != null && r.getTipo().equals("Creadores")) 
					return r;			
				else throw new CertificadoException("Certificado incorrecto");
			} else throw new CertificadoException("nivel con coma");
		} catch (Exception e) {
			throw e;
		}
	}
	
	public Certificado triggerParticipaciones(int momento, String tipo) throws Exception {
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
		
	
}
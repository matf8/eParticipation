package eParticipation.backend.controller;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import eParticipation.backend.dto.DtAdministrador;
import eParticipation.backend.dto.DtAutoridad;
import eParticipation.backend.dto.DtCertificado;
import eParticipation.backend.dto.DtCiudadano;
import eParticipation.backend.dto.DtFuncionario;
import eParticipation.backend.dto.DtOrganismo;
import eParticipation.backend.dto.DtProceso;
import eParticipation.backend.dto.DtUserPDI;
import eParticipation.backend.excepciones.OrganismoException;
import eParticipation.backend.excepciones.ProcesoException;
import eParticipation.backend.excepciones.UsuarioException;
import eParticipation.backend.model.Alcance;
import eParticipation.backend.model.Certificado;
import eParticipation.backend.model.CfgNoti;
import eParticipation.backend.model.Administrador;
import eParticipation.backend.model.Autoridad;
import eParticipation.backend.model.Ciudadano;
import eParticipation.backend.model.Funcionario;
import eParticipation.backend.model.Iniciativa;
import eParticipation.backend.model.Organismo;
import eParticipation.backend.model.ProcesoParticipativo;
import eParticipation.backend.model.TipoAlcance;
import eParticipation.backend.service.InsigniaService;
import eParticipation.backend.service.PerifericoService;
import eParticipation.backend.service.ConverterService;
import eParticipation.backend.service.PersistenciaLocal;
import eParticipation.backend.service.PersistenciaNSQLocal;
import eParticipation.backend.service.UsuarioService;
import eParticipation.backend.pdi.GestorPdi;
import eParticipation.backend.pdi.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
	
@Stateless
public class ControladorUsuario implements UsuarioService {
		
	@EJB private PersistenciaLocal bd;		
	@EJB private PersistenciaNSQLocal nsql;
	@EJB private ConverterService cc;	
	@EJB private PerifericoService cp;		
	@EJB private InsigniaService is;	
			
	public String crearToken(String correo, String role) {
		Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);		    
		
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    Date d1 = calendar.getTime();
	    calendar.add(Calendar.HOUR_OF_DAY, 1);   
	    Date d2 = calendar.getTime();	    
	    
		return Jwts.builder().setSubject("authorization")
		  .claim("id", correo).claim("rol", role)
		  .setIssuedAt(d1).setExpiration(d2)
		  .signWith(key).compact();		
	}	
	
	public boolean checkIdUser(Object user) throws Exception {
		if (user != null)
			return bd.checkIdUser(user);
		else return false;
	}
	
	@SuppressWarnings("deprecation")
	public void altaCiudadano(String cedula) throws Exception {	
		if (cedula != null) {			
			List<String> lm = null;
			List<Certificado> lc = null;		
			List<CfgNoti> ln = null;		
			//pedido a pdi con cedula				
			try {
				Ciudadano c;
				DtUserPDI updi = extraerDatosPDI(cedula);				
				if (updi != null) {					
					System.out.println(updi.toString());
					Date d = new SimpleDateFormat("dd/MM/yyyy").parse(updi.getFNac());					
					c = new Ciudadano(updi.getCedula(), updi.getNombre(), "default@"+cedula, d, updi.getNacionalidad(), updi.getDomicilio(), lm, lc, ln);
				} else c = new Ciudadano(cedula, "", "default@"+cedula, new Date("11/11/2011"), "", "", lm, lc, ln);
				bd.altaCiudadano(c);
			} catch (Exception e) {
				throw e;
			}
		} else throw new Exception("Dato null");	
	}	
	
	private DtUserPDI extraerDatosPDI(String cedula) throws Exception {				
		try {		
			GestorPdi pdi = new GestorPdi();
			DtUserPDI dus = new DtUserPDI();			
			User _updi = pdi.getPdiWSPort().getUser(cedula);
			if (_updi != null) {
				dus.setCedula(_updi.getCedula());
				dus.setNombre(_updi.getNombre());
				dus.setNacionalidad(_updi.getNacionalidad());
				dus.setDomicilio(_updi.getDomicilio());		
				dus.setFNac(_updi.getBirth());
				return dus;		
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
		return null;		
	}	

	public void altaCiudadanoFacebook(DtCiudadano c) throws Exception {
		if (c != null) {			
			List<String> lm = null;
			List<Certificado> lc = null;		
			List<CfgNoti> ln = null;
			try {
				Date d = new SimpleDateFormat("dd/MM/yyyy").parse(c.getFNac());	
				Ciudadano cx = new Ciudadano(c.getCedula(), c.getNombreCompleto(), c.getCorreo(), d, c.getNacionalidad(), c.getDomicilio(), lm, lc, ln);
				bd.altaCiudadano(cx);
			}
			catch (Exception e) {
				throw e;
			}
		} else throw new Exception("Dato null");	
	}	
	
	public void altaUsuario(Object a, String organismo) throws Exception { // general, para adm
		try {
			if (a instanceof DtAdministrador)
				bd.altaAdmin((DtAdministrador) a);
			else if (a instanceof DtAutoridad)
				bd.altaAutoridad((DtAutoridad) a);		
			else if (a instanceof DtCiudadano) 				
				bd.altaCiudadano((DtCiudadano) a);				
			else if (a instanceof DtFuncionario) {
				Organismo o = bd.buscarOrganismo(organismo);
				if (o != null)
					bd.altaFuncionario((DtFuncionario) a, o);
				else throw new OrganismoException("Organismo no existe, no se puede insertar funcionario");				
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public Object buscarUsuario(String correo) throws Exception {		
		try {
			Object o = bd.buscarUsuario(correo);
			if (o != null) 	
				if (o instanceof Ciudadano) {		
					DtCiudadano i = ((Ciudadano) o).getDt();
					i.setIniSeguidas(bd.listarIniciativasSeguidasUsuario((Ciudadano) o));	
					i.setIniAdheridas(bd.listarIniciativasAdheridasUsuario((Ciudadano) o));				
					i.setIniCreadas(bd.listarIniciativasCreadasUsuario((Ciudadano) o, null));					
					i.setProPart(bd.listarProcesoParticipandoUsuario((Ciudadano) o));
					i.setProSeguidos(bd.listarProcesoSeguidoUsuario((Ciudadano) o));
					i.setCertificados(bd.listarCertificadosUsuario(i.getCorreo()));
					i.setMensajes(bd.listarMensajesUsuario(i.getCorreo()));
					return i;					
				} else if (o instanceof Funcionario) {
					DtFuncionario i = ((Funcionario) o).getDt();
					i.setProcesoCreado(bd.listarProcesoCreadosUsuario((Funcionario) o));				
					return i;
				} else if (o instanceof Autoridad) {
					DtAutoridad i = ((Autoridad) o).getDt();
					return i;
				} else if (o instanceof Administrador) {
					DtAdministrador i = ((Administrador) o).getDt();
					return i;
				}						
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
			
	public List<Object> listarUsuarios() throws Exception {		
		return bd.listarUsuarios();		
	}
	
	public boolean usuarioSigueIniciativa(String iniciativa, String user) {
		boolean ret = false;
		Iniciativa e = bd.buscarIniciativa(iniciativa);
		try {
			Ciudadano c = (Ciudadano) bd.buscarUsuario(user);
			if (e.getSeguidores().contains(c))
				ret = true;			
		} catch (Exception e1) {
			System.out.println("usuario sigue ini: " + e1.getMessage());
		}		
		return ret;			
	}
	
	public boolean usuarioSigueProceso(String p, String user) {
		boolean ret = false;
		ProcesoParticipativo e = bd.buscarProceso(p);
		try {
			Ciudadano c = (Ciudadano) bd.buscarUsuario(user);
			if (e.getSeguidores().contains(c))
				ret = true;			
		} catch (Exception e1) {
			System.out.println("usuario sigue proceso: " + e1.getMessage());
		}		
		return ret;			
	}
	
	public boolean usuarioAdheridoIniciativa(String iniciativa, String user) {
		boolean ret = false;
		Iniciativa e = bd.buscarIniciativa(iniciativa);
		try {
			Ciudadano c = (Ciudadano) bd.buscarUsuario(user);
			if (e.getAdheridos().contains(c))
				ret = true;			
		} catch (Exception e1) {
			System.out.println("usuario adh ini: " + e1.getMessage());
		}		
		return ret;			
	}
	
	public boolean usuarioCreadorIniciativa(String iniciativa, String user) {
		boolean ret = false;
		Iniciativa e = bd.buscarIniciativa(iniciativa);
		try {
			Ciudadano c = (Ciudadano) bd.buscarUsuario(user);
			if (e.getCreador().equals(c.getCorreo()))
				ret = true;			
		} catch (Exception e1) {
			System.out.println("usuario creador ini: " + e1.getMessage());
		}		
		return ret;			
	}	
	
	public boolean usuarioCreadorProceso(String proceso, String user) {
		boolean ret = false;
		ProcesoParticipativo e = bd.buscarProceso(proceso);
		try {
			Funcionario c = (Funcionario) bd.buscarUsuario(user);
			if (e.getCreador().equals(c.getCorreo()))
				ret = true;			
		} catch (Exception e1) {
			System.out.println("usuario creador pro: " + e1.getMessage());
		}		
		return ret;			
	}	
	
	public boolean usuarioParticipaProceso(String nombre, String correo) {
		boolean ret = false;
		try {			
			DtProceso e = bd.buscarProceso(nombre).getDt();
			List<String> lC = e.getParticipantes();
			if (lC.contains(correo))
				ret = true;
		} catch (Exception e) {
			System.out.println("usuario participa pro: " + e.getMessage());
		}		
		return ret;
	}
	
		
	public int[] enAlcanceDeProceso(String correo, String nombreProceso) throws Exception {
		TipoAlcance habilitado = TipoAlcance.Fuera_de_Rango;
		DtProceso dp = null;
		int[] ret = new int[3];		
		ret[2] = 0; // 0 edad del usuario, 1 edad minima proceso, 2 si cumple o no la condicion 
		try {	
			DtCiudadano dc = ((Ciudadano) bd.buscarUsuario(correo)).getDt();	
			if (dc != null) {			
				ProcesoParticipativo p = bd.buscarProceso(nombreProceso);			
				if (p != null) {
					dp = p.getDt();
					LocalDate today = LocalDate.now();					
					LocalDate cumple = LocalDate.parse(dc.getFNac());					
					Long edadUsuario = calcular(cumple, today);
					ret[0] = edadUsuario.intValue();
					String descripcion = dp.getDescripcionAlcance();
					Integer edadMinima = null;
					if (isNumeric(descripcion)) {
						edadMinima = Integer.parseInt(descripcion); 						
						habilitado = Alcance.consultarAlcance(edadMinima, edadUsuario.intValue());
						ret[1] = edadMinima;
						if (habilitado == TipoAlcance.Disponible) 
							ret[2] = 1;			// true 			
					} else {
						String[] departamentosAlcance = descripcion.split(",");												
						String departamentoUsuario = dc.getDomicilio(); // domicilio = departamento						
						habilitado = Alcance.consultarAlcance(departamentosAlcance, departamentoUsuario);
						if (habilitado == TipoAlcance.Disponible)
							ret[2] = 1;				
					}
				} else throw new ProcesoException("Proceso no existe");	
			} else throw new UsuarioException("Usuario no existe");
		} catch (Exception e) {
			throw e;
		}	
		return ret;
	}
	
	public List<DtProceso> listarProcesosUsuarioEnAlcance(String correo) throws Exception {
		List<DtProceso> r = new ArrayList<>();
		if (correo != null) {
			List<DtProceso> lp = bd.listarProcesos();
			if (lp != null)
				for (DtProceso i: lp) {			
					try {
						int[] k = enAlcanceDeProceso(correo, i.getNombre());
						if (k[2] == 1) {
							if (i.getFase().equals("Publicado"))
								r.add(i);
						}
					} catch (Exception e) {
						throw e;
					}				
				}		
		}		
		return r;
	}
	
	public String verParticipacion(String proceso, String user) throws Exception {
		String r = null;
		if (proceso != null) { 
			if (user != null) {
				ProcesoParticipativo p = bd.buscarProceso(proceso);				
				r = nsql.getParticipacion(p.getIdInstrumento().get(0), user);	
				System.out.println(r);
			}
		}
		return r;
	}
	
	public List<DtCertificado> listarCertificadosUsuario(String correo) throws Exception {	
		List<DtCertificado> r = new ArrayList<>();
		try {
			Ciudadano c = (Ciudadano) bd.buscarUsuario(correo);
			List<Certificado> lc = c.getCertificados();
			if (lc != null) 
				for (Certificado i: lc)
					r.add(i.getDt());			
		} catch (Exception e) {
			throw new UsuarioException("Usuario no existe");
		}		
		return r;
		
	}
	
	private static boolean isNumeric(String str) {
	        return str != null && str.matches("[0-9.]+");
	}
	
	private static long calcular(LocalDate cumple, LocalDate now) {		
		long age = ChronoUnit.YEARS.between(cumple, now);
		return age;
    }
	
	public int contarCiudadanos() {
		return bd.contarCiudadanos();	
	}

	public int contarFuncionarios() {
		return bd.contarFuncionarios();
	}

	public int contarAdministradores() {
		return bd.contarAdministradores();
	}

	public int contarAutoridades() {
		return bd.contarAutoridades();	
	}
	
	public void altaOrganismo(String nombre, String depart) {
		if (nombre != null && depart != null ) {
			Organismo o = new Organismo(nombre, depart);
			bd.altaOrganismo(o);
		}
	}
		
	public void quitarOrganismo(String nombre) throws Exception {
		if (nombre != null) {			
			bd.bajaOrganismo(nombre);
		}
	}
	
	public List<DtOrganismo> listaOrganismo() {
		return bd.listaOrganismos();
	}
			
	public void bajaAdmin(String cedula) {
		bd.bajaAdmin(cedula);		
	}
	
	public void bajaAutoridad(String cedula) {
		bd.bajaAutoridad(cedula);		
	}
	
	public void bajaFuncionario(String correo) throws Exception {
		bd.bajaFuncionario(correo);		
	}
	
	public void bajaCiudadano(String correo) throws Exception {
		bd.bajaCiudadano(correo);		
	}
	
	public void modificarUsuario(Object u, String t, String o, String c, String p) throws Exception {		
		bd.modificarUsuario(u, t, o, c, p);
	}

	public void updatePassword(String id, String rol, String pass) {
		if (pass != null && !pass.equals("") && id != null) 
			bd.updatePassword(id, rol, pass);			
	}

	public void eliminarMensaje(String user, String mensaje) throws Exception {		
		bd.eliminarMensajeEvaluacion(user, mensaje);
	}	
	
	public void eliminarMensajes(String user) throws Exception {		
		bd.eliminarMensajesEvaluacion(user);
	}
}

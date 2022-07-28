package eParticipation.backend.data;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.io.IOUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import eParticipation.backend.dto.DtAdministrador;
import eParticipation.backend.dto.DtAutoridad;
import eParticipation.backend.dto.DtCertificado;
import eParticipation.backend.dto.DtCiudadano;
import eParticipation.backend.dto.DtEvaluador;
import eParticipation.backend.dto.DtFuncionario;
import eParticipation.backend.dto.DtIniciativa;
import eParticipation.backend.dto.DtOrganismo;
import eParticipation.backend.dto.DtOrganizacion;
import eParticipation.backend.dto.DtProceso;
import eParticipation.backend.excepciones.CertificadoException;
import eParticipation.backend.excepciones.IniciativaException;
import eParticipation.backend.excepciones.MensajeException;
import eParticipation.backend.excepciones.OrganismoException;
import eParticipation.backend.excepciones.OrganizacionException;
import eParticipation.backend.excepciones.ProcesoException;
import eParticipation.backend.excepciones.UsuarioException;
import eParticipation.backend.model.Administrador;
import eParticipation.backend.model.Autoridad;
import eParticipation.backend.model.Certificado;
import eParticipation.backend.model.CfgNoti;
import eParticipation.backend.model.Ciudadano;
import eParticipation.backend.model.Estado;
import eParticipation.backend.model.FaseProceso;
import eParticipation.backend.model.Evaluador;
import eParticipation.backend.model.Funcionario;
import eParticipation.backend.model.Iniciativa;
import eParticipation.backend.model.IniciativaNodo;
import eParticipation.backend.model.NodosPerifericos;
import eParticipation.backend.model.Organismo;
import eParticipation.backend.model.Organizacion;
import eParticipation.backend.model.ProcesoParticipativo;
import eParticipation.backend.model.TipoCertificado;
import eParticipation.backend.model.TipoInsignia;
import eParticipation.backend.model.UsuarioFrontOffice;
import eParticipation.backend.service.PersistenciaLocal;
import eParticipation.backend.service.PersistenciaNSQLocal;

@Singleton
@Startup
@Lock(LockType.READ)
@SuppressWarnings("unchecked")
public class Persistencia implements PersistenciaLocal {
	
	@PersistenceContext(unitName = "eParticipationDB")
	private EntityManager em;
	
	@EJB private PersistenciaNSQLocal nsql;
	
	public Persistencia() { }		
	
	@SuppressWarnings("deprecation")
	public void datosIniciales() {
		try {
			TipoCertificado tpc = TipoCertificado.getInstancia();
		  	altaTipoCertificadoInicial(tpc);		   
			altaCertificadoInicial(1);	// niveles
			altaCertificadoInicial(2);
			altaCertificadoInicial(3);			
			NodosPerifericos tpn = NodosPerifericos.getInstancia();
			altaUrisInicial(tpn);	
			
			// Usuarios perifericos			
			Organizacion o1 = new Organizacion("Organizacion1", "ORG1");
			altaOrganizacion(o1);
			o1 = new Organizacion("Organizacion2", "ORG2");		 
			altaOrganizacion(o1);
			o1 = new Organizacion("Organizacion3", "ORG3");
			altaOrganizacion(o1);			
			
			Evaluador e1 = new Evaluador("EV1");
			altaEvaluador(e1);
			e1 = new Evaluador("EV2");
			altaEvaluador(e1);
			e1 = new Evaluador("EV3");
			altaEvaluador(e1);
			e1 = new Evaluador("EV4");
			altaEvaluador(e1);
			e1 = new Evaluador("EV5");
			altaEvaluador(e1);
			e1 = new Evaluador("EV6");
			altaEvaluador(e1);
			e1 = new Evaluador("EV7"); 
			altaEvaluador(e1); 
			e1 = new Evaluador("EV8"); 
			altaEvaluador(e1);
			
			// Usuarios
			DtAdministrador adm = new DtAdministrador("5026", "f", "m@i", "mathias fernández", "Administrador"); 
			altaAdmin(adm);
			adm = new DtAdministrador("admin", "admin", "admin@mmm", "admin", "Administrador");
			altaAdmin(adm);		 		
	  		DtAutoridad aut = new DtAutoridad("1234", "f", "autoridad@gub.uy", "la poli", "Autoridad"); 
	  		altaAutoridad(aut);    		
	  		aut = new DtAutoridad("gestion", "gestion", "autoridad@mmm", "autoridad", "Autoridad");
	  		altaAutoridad(aut);   			
			
			List<String> lm = new ArrayList<>();	
			List<Certificado> ctf = new ArrayList<>();		
			List<CfgNoti> ln = null;
			String pastS = "1978-05-23";
			String pastZ = "2002-05-23";
			Date birthday = new SimpleDateFormat("yyyy-MM-dd").parse(pastS);
			Date birthday2 = new SimpleDateFormat("yyyy-MM-dd").parse(pastZ);
			Date randomBirthday = new Date(ThreadLocalRandom.current().nextLong(birthday.getTime(), birthday2.getTime()));			
			Ciudadano c = new Ciudadano("45668997", "Jaime Coll", "jaimecoll@gmail", randomBirthday, "UY", "Artigas", lm, ctf, ln);
			altaCiudadano(c);
			randomBirthday = new Date(ThreadLocalRandom.current().nextLong(birthday.getTime(), birthday2.getTime()));			
			c = new Ciudadano("51267892", "Ruben Stol", "rubenstol@gmail", randomBirthday, "UY", "Artigas", lm, ctf, ln);
			altaCiudadano(c);
			randomBirthday = new Date(ThreadLocalRandom.current().nextLong(birthday.getTime(), birthday2.getTime()));			
			c = new Ciudadano("45221224", "Rodrigo Turak", "rodrigoturak@gmail", randomBirthday, "UY", "Colonia", lm, ctf, ln);
			altaCiudadano(c);
			randomBirthday = new Date(ThreadLocalRandom.current().nextLong(birthday.getTime(), birthday2.getTime()));			
			c = new Ciudadano("26621237", "Walter Rodriguez", "wr15@gmail", randomBirthday, "UY", "Colonia", lm, ctf, ln);
			altaCiudadano(c);
			randomBirthday = new Date(ThreadLocalRandom.current().nextLong(birthday.getTime(), birthday2.getTime()));			
			c = new Ciudadano("12545788", "Hugo Dolores", "hugoDD@gmail", randomBirthday, "UY", "Salto", lm, ctf, ln);
			altaCiudadano(c);
			randomBirthday = new Date(ThreadLocalRandom.current().nextLong(birthday.getTime(), birthday2.getTime()));			
			c = new Ciudadano("46114532", "Marcos Colman", "mc22@gmail", randomBirthday, "AR", "Maldonado", lm, ctf, ln);
			altaCiudadano(c);
			randomBirthday = new Date(ThreadLocalRandom.current().nextLong(birthday.getTime(), birthday2.getTime()));			
			c = new Ciudadano("35664854", "Romina Calero", "romicalero@gmail", randomBirthday, "UY", "Montevideo", lm, ctf, ln);
			altaCiudadano(c);
			randomBirthday = new Date(ThreadLocalRandom.current().nextLong(birthday.getTime(), birthday2.getTime()));			
			c = new Ciudadano("43128654", "Gimena Kalt", "gimekalt@gmail", randomBirthday, "UY", "Montevideo", lm, ctf, ln);
			altaCiudadano(c);
			randomBirthday = new Date(ThreadLocalRandom.current().nextLong(birthday.getTime(), birthday2.getTime()));			
			c = new Ciudadano("54412214", "Kirby Clonson", "kirby.clonson@gmail", randomBirthday, "ENG", "Canelones", lm, ctf, ln);
			altaCiudadano(c);
			randomBirthday = new Date(ThreadLocalRandom.current().nextLong(birthday.getTime(), birthday2.getTime()));			
			c = new Ciudadano("23354529", "Maria Puller", "maria@gmail", randomBirthday, "UY", "Rocha", lm, ctf, ln);
			altaCiudadano(c);
			c = new Ciudadano("53463017", "Manuel Biurrun", "manu.biurrun@gmail.com", new Date("31/5/1999"), "UY", "Montevideo", lm, ctf, ln);
			altaCiudadano(c);
			c = new Ciudadano("55213703", "Mauricio Iglesias", "mauricio.andres.iglesias@gmail.com", new Date("26/8/1999"), "UY", "Montevideo", lm, ctf, ln);
			altaCiudadano(c);		
			
			Organismo or = new Organismo("ComunaCanaria", "Canelones");
			altaOrganismo(or);
			randomBirthday = new Date(ThreadLocalRandom.current().nextLong(birthday.getTime(), birthday2.getTime()));		
			Funcionario f = new Funcionario("47895412", "Ruben Ramirez", "rr.21@gmail", randomBirthday, "UY", "Canelones", lm, "empleado",  or);
			altaFuncionario(f);
			randomBirthday = new Date(ThreadLocalRandom.current().nextLong(birthday.getTime(), birthday2.getTime()));		
			f = new Funcionario("12354456", "Walter Cairo", "wc1900@gmail", randomBirthday, "UY", "Minas", lm, "empleado",  or);
			altaFuncionario(f);
			f = new Funcionario("50266567", "Mathias Fernández", "mathihd38@gmail.com", new Date("10/02/1996"), "UY", "Canelones", lm, "developer",  or);
			altaFuncionario(f);
	
			or = new Organismo("ZonaD", "Montevideo");
			altaOrganismo(or);
			randomBirthday = new Date(ThreadLocalRandom.current().nextLong(birthday.getTime(), birthday2.getTime()));		
			f = new Funcionario("25544896", "Marcos Cairo", "mc1900@gmail", randomBirthday, "UY", "Montevideo", lm, "empleado",  or);
			altaFuncionario(f);
			randomBirthday = new Date(ThreadLocalRandom.current().nextLong(birthday.getTime(), birthday2.getTime()));		
			f = new Funcionario("35242596", "Jessica Ferran", "jsff@gmail", randomBirthday, "UY", "Montevideo", lm, "empleada",  or);
			altaFuncionario(f);
				
			or = new Organismo("ZonaC", "Montevideo");		
			altaOrganismo(or);
			randomBirthday = new Date(ThreadLocalRandom.current().nextLong(birthday.getTime(), birthday2.getTime()));		
			f = new Funcionario("44523124", "Maria Kozak", "majo22@gmail", randomBirthday, "UY", "Colonia", lm, "empleada",  or);
			altaFuncionario(f);
			randomBirthday = new Date(ThreadLocalRandom.current().nextLong(birthday.getTime(), birthday2.getTime()));		
			f = new Funcionario("50445786", "Camila Zipitria", "camizf@gmail", randomBirthday, "UY", "Montevideo", lm, "gerenta",  or);
			altaFuncionario(f);
					
			or = new Organismo("SaltoAvanza", "Salto");
			altaOrganismo(or);
			randomBirthday = new Date(ThreadLocalRandom.current().nextLong(birthday.getTime(), birthday2.getTime()));		
			f = new Funcionario("44553134", "Susana De Amores", "susi22@gmail", randomBirthday, "UY", "Salto", lm, "jefa",  or);
			altaFuncionario(f);
			randomBirthday = new Date(ThreadLocalRandom.current().nextLong(birthday.getTime(), birthday2.getTime()));		
			f = new Funcionario("35242586", "Carmen Amorin", "caramorin@gmail", randomBirthday, "UY", "Salto", lm, "empleada",  or);
			altaFuncionario(f);
			
			or = new Organismo("Maldunidad", "Maldonado");
			altaOrganismo(or);		
			randomBirthday = new Date(ThreadLocalRandom.current().nextLong(birthday.getTime(), birthday2.getTime()));		
			f = new Funcionario("52214522", "Carla Gimenez", "carlaGG@gmail", randomBirthday, "UY", "Maldonado", lm, "encargada",  or);
			altaFuncionario(f);
			randomBirthday = new Date(ThreadLocalRandom.current().nextLong(birthday.getTime(), birthday2.getTime()));		
			f = new Funcionario("45221572", "Santiago Musto", "mustosanti@gmail", randomBirthday, "UY", "Maldonado", lm, "empleado",  or);
			altaFuncionario(f);
			
			//Certificados a ciudadano
			Certificado cert = buscarCertificadoTipo(1, "Seguidores");
			agregarCertificadoUsuario("mauricio.andres.iglesias@gmail.com", cert);
			
			cert = buscarCertificadoTipo(1, "Comentarios");
			agregarCertificadoUsuario("mauricio.andres.iglesias@gmail.com", cert);
			
			cert = buscarCertificadoTipo(1, "Adhesiones");
			agregarCertificadoUsuario("manu.biurrun@gmail.com", cert);
			
			cert = buscarCertificadoTipo(1, "ParticiparProceso");		
			agregarCertificadoUsuario("manu.biurrun@gmail.com", cert);
			
			cert = buscarCertificadoTipo(2, "ParticiparProceso");
			agregarCertificadoUsuario("manu.biurrun@gmail.com", cert);
			
			//Iniciativas		
			Iniciativa i = new Iniciativa("Remodelar museo del fútbol", "Despintado y mal cuidado", new Date("20/7/2022"), "jaimecoll@gmail", Estado.Publicado);
			altaIniciativa(i);		
			i = new Iniciativa("Salida a ver deflines", "En cabo polonio podemos ver la temporada de delfines", new Date("30/11/2022"), "romicalero@gmail", Estado.Publicado);
			altaIniciativa(i);		
			i = new Iniciativa("Reacondicionamiento peatonal Sarandi", "Botes de basura y colillas son necesarias", new Date("20/08/2022"), "gimekalt@gmail", Estado.En_espera);
			altaIniciativa(i);
			i = new Iniciativa("Concientizacion sobre comida chatarra", "Obesidad crece en 5% el ultimo mes", new Date("31/07/2022"), "rodrigoturak@gmail", Estado.En_espera);
			altaIniciativa(i);
			i = new Iniciativa("Paseo por La Pataia", "La Pataia Punta Ballena con nuevos espacios recreativos y el mejor dulce de leche", new Date("5/11/2022"), "mc22@gmail", Estado.Publicado);
			altaIniciativa(i);
			i = new Iniciativa("Bicicleteada por Battle & Ordoñez", "Convocamos a los fanaticos de la bici a recorrer de punta a punta una de las calles más extensas de Montevideo. Se creara un circuito especifico para controlar el tránsito.", new Date("14/7/2022"), "manu.biurrun@gmail.com", Estado.Publicado);
			altaIniciativa(i);
			i = new Iniciativa("Venta garage mensual", "Se aceptan todo tipo de elementos comerciables", new Date("5/11/2022"), "kirby.clonson@gmail", Estado.Publicado);
			altaIniciativa(i);
			
			adherirUsuarioIniciativa("Remodelar museo del fútbol", "manu.biurrun@gmail.com");			
			adherirUsuarioIniciativa("Remodelar museo del fútbol", "mauricio.andres.iglesias@gmail.com");	
			adherirUsuarioIniciativa("Remodelar museo del fútbol", "romicalero@gmail");	
			adherirUsuarioIniciativa("Remodelar museo del fútbol", "rodrigoturak@gmail");			
			adherirUsuarioIniciativa("Remodelar museo del fútbol", "kirby.clonson@gmail");	
			adherirUsuarioIniciativa("Remodelar museo del fútbol", "mc22@gmail");	
			adherirUsuarioIniciativa("Venta garage mensual", "gimekalt@gmail");			
			adherirUsuarioIniciativa("Venta garage mensual", "kirby.clonson@gmail");	
			adherirUsuarioIniciativa("Venta garage mensual", "rodrigoturak@gmail");			
			adherirUsuarioIniciativa("Venta garage mensual", "maria@gmail");	
			seguirUsuarioIniciativa("Bicicleteada por Battle & Ordoñez", "rubenstol@gmail");	
			seguirUsuarioIniciativa("Bicicleteada por Battle & Ordoñez", "maria@gmail");	
			seguirUsuarioIniciativa("Bicicleteada por Battle & Ordoñez", "wr15@gmail");	
			seguirUsuarioIniciativa("Bicicleteada por Battle & Ordoñez", "rodrigoturak@gmail");	
			seguirUsuarioIniciativa("Bicicleteada por Battle & Ordoñez", "jaimecoll@gmail");	
			seguirUsuarioIniciativa("Paseo por La Pataia", "manu.biurrun@gmail.com");	
			seguirUsuarioIniciativa("Paseo por La Pataia", "rubenstol@gmail");	
			seguirUsuarioIniciativa("Paseo por La Pataia", "maria@gmail");			
		} catch (Exception e) {
			System.out.println("inicializando: " + e.getMessage());			
		}		
	}
			
	//USUARIOS
	public void altaAdmin(DtAdministrador a) throws Exception {
		try {
			// hash con salt
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String pass = passwordEncoder.encode(a.getPassword());
			Administrador adm = new Administrador(a.getCedula(), pass, a.getCorreo(), a.getNombreCompleto());		
			em.persist(adm);	
			em.flush();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception("Usuario ya existe");
		}
	}
	
	public void bajaAdmin(String cedula) {
		Administrador f;
		try {
			f = (Administrador) buscarUsuario(cedula);
			if (f != null) {
				em.remove(f);	
				em.flush();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
	}

	public void altaAutoridad(DtAutoridad a) throws Exception {
		try {
			// hash con salt
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String pass = passwordEncoder.encode(a.getPassword());
			Autoridad aut = new Autoridad(a.getCedula(), pass, a.getCorreo(), a.getNombreCompleto());
			em.persist(aut);
			em.flush();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception("Usuario ya existe");
		}
	}
	
	public void bajaAutoridad(String cedula) {		 
		try {
			Autoridad f = (Autoridad) buscarUsuario(cedula);
			if (f != null) {
				em.remove(f);
				em.flush();
			}
		} catch (Exception e) {	}		
	}

	public void altaFuncionario(DtFuncionario a, Organismo o) throws Exception {		
		try {
			Date d = null;		
			d = new SimpleDateFormat("yyyy-MM-dd").parse(a.getFNac());		
			Funcionario f;
			if (o != null) {
				f = new Funcionario(a.getCedula(), a.getNombreCompleto(), a.getCorreo(), d, a.getNacionalidad(), a.getDomicilio(), null, a.getCargo(), o);
				o.agregarEmpleado(f);
				em.persist(f);	
				em.flush();
			}	
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception("Usuario ya existe");
		}
	}
	
	public void altaFuncionario(Funcionario c) throws Exception {	
		try {
			if(c != null) {	
				em.persist(c);
				em.flush();
			}			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception("Usuario ya existe");
		}
	}
	
	public void bajaFuncionario(String correo) throws Exception {
		Funcionario f;
		try {
			f = (Funcionario) buscarUsuario(correo);
			if (f != null) {
				em.remove(f);
				em.flush();
			}
		} catch (Exception e) {
			throw e;		
		}		
	}

	public void altaCiudadano(Ciudadano c) throws Exception {	
		try {
			if(c != null) {	
				em.persist(c);
				em.flush();
			}			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception("Usuario ya existe");
		}
	}
	
	public void altaCiudadano(DtCiudadano a) throws Exception {
		try {
			Date d = null;		
			d = new SimpleDateFormat("yyyy-MM-dd").parse(a.getFNac());	
			Ciudadano c = new Ciudadano(a.getCedula(), a.getNombreCompleto(), a.getCorreo(), d,
					a.getNacionalidad(), a.getDomicilio(), null, null, null);
			em.persist(c);	
			em.flush();
		} catch (Exception e) {
			throw e;		
		}
	}	
	
	public void bajaCiudadano(String correo) throws Exception {
		Ciudadano f;
		try {			
			f = (Ciudadano) buscarUsuario(correo);	
			if (f != null) {
				quitarDeps(f);
				em.remove(f);
				em.flush();
			}
		} catch (Exception e) {
			throw e;
		}		
	}
	
	public void mensajeEvaluacion(String creador, String notificacion) throws Exception {
		if (creador != null) {
			Ciudadano c = (Ciudadano) buscarUsuario(creador);
			if (c != null) {				
				c.agregarMensaje(notificacion);
				em.persist(c);
				em.flush();
			}
		}
	}
		
	public List<String> listarMensajesUsuario(String correo) throws Exception {
		List<String> mret = new ArrayList<>();
		if (correo != null) {
			Ciudadano c = (Ciudadano) buscarUsuario(correo);
			if (c != null) {
				List<String> lM = c.getMensajes();
				for (String m: lM) 				
					mret.add(m);									
			}
		}
		return mret;
	}
	
	private void quitarDeps(Ciudadano f) throws Exception {
		if (f != null) {
			try {				
				List<DtIniciativa> lI = listarIniciativas();
				if (lI != null) {
					for (DtIniciativa k: lI) {
						List<String> lIA = k.getAdheridos();	
						List<String> lIS = k.getSeguidores();
						try {
							if (lIA != null)
								if (lIA.contains(f.getCorreo())) 						
									desadherirUsuarioIniciativa(k.getNombre(), f.getCorreo());	
							if (lIS != null)
								if (lIS.contains(f.getCorreo())) 										
									dejarSeguirUsuarioIniciativa(k.getNombre(), f.getCorreo());
						} catch (Exception e) { }
					}
				}
				List<DtProceso> lP = listarProcesos();
				if (lP != null) 						
					for (DtProceso k: lP) {
						List<String> lPP = k.getParticipantes();
						List<String> lPS = k.getSeguidores();						
						try {
							if (lPP != null) 
								if (lPP.contains(f.getCorreo()))							
									quitarParticipanteProceso(k.getNombre(), f.getCorreo());					
							if (lPS != null)							
								if (lPS.contains(f.getCorreo()))
									dejarSeguirUsuarioProceso(k.getNombre(), f.getCorreo());
						} catch (Exception e) { }
					}
				em.persist(f);	
				em.flush();				
			} catch (Exception e) {
				throw e;
			}
		}		
	}
	
	public void updatePassword(String id, String rol, String pass) {
		Query q;
		if (rol.equals("Autoridad")) {				
			q = em.createQuery("update Autoridad set password=:p where cedula=:u");
			q.setParameter("p", pass);		
			q.setParameter("u", id);
			q.executeUpdate();			
		} else if (rol.equals("Administrador")) {			
			q = em.createQuery("update Administrador set password=:p where cedula=:u");
			q.setParameter("p", pass);
			q.setParameter("u", id);
			q.executeUpdate();			
		}			
		em.flush();
	}
	
	public boolean checkIdUser(Object u) throws Exception {
		boolean ret = false;		
		try {
			Query q = em.createQuery("select a from UsuarioFrontOffice a where a.idUsuarioFO=:u");
			if (u instanceof DtFuncionario)
				q.setParameter("u", Long.valueOf(((DtFuncionario)u).getId()));
			else if (u instanceof DtCiudadano)
				q.setParameter("u", Long.valueOf(((DtCiudadano)u).getId()));
			Object a = q.getSingleResult();
			em.refresh(a);
			if (a != null)
				ret = true;
		} catch (Exception e) {
			throw new UsuarioException("id usuario no existe");
		}		
		return ret;		
	}
	
	public boolean checkUser(String correo) throws Exception {
		boolean ret = false;		
		try {
			Query q = em.createQuery("select a from UsuarioFrontOffice a where a.correo=:u");			
			q.setParameter("u", correo);
			Object a = q.getSingleResult();
			em.refresh(a);
			if (a != null)
				ret = true;
		} catch (Exception e) {
			throw new UsuarioException("id usuario no existe");
		}		
		return ret;		
	}
		
	public Object buscarUsuario(String user) throws Exception {
		Object a = null;
		Query q = em.createQuery("select a from Administrador a where a.cedula=:u or a.correo=:u");
		q.setParameter("u", user);
		try {
			a = (Administrador) q.getSingleResult();
			em.refresh(a);
			if (a != null)
				return ((Administrador) a);
		} catch (Exception e) {
			try { 
				q = em.createQuery("select a from Autoridad a where a.cedula=:u or a.correo=:u");
				q.setParameter("u", user);
				a = (Autoridad) q.getSingleResult();
				em.refresh(a);
				if (a != null)
					return ((Autoridad) a);
			} catch (Exception e2) {
				try {
					q = em.createQuery("select c from UsuarioFrontOffice c where c.correo=:u or c.cedula=:u");
					q.setParameter("u", user);					
					a = (Ciudadano) q.getSingleResult();
					em.refresh(a);				
					if (a != null) 
						return ((Ciudadano) a);		
					
				} catch (Exception e3) {
					try {
						q = em.createQuery("select c from UsuarioFrontOffice c where c.correo=:u or c.cedula=:u");
						q.setParameter("u", user);					
						a = (Funcionario) q.getSingleResult();
						em.refresh(a);
						if (a != null)
							return ((Funcionario) a);
					} catch (Exception e4) {
						throw new UsuarioException("Usuario no existe");
					}
				}
			}
		}
		return a;
	}
	
	public void modificarUsuario(Object u, String tipoU, String organismo, String cargo, String pass) throws Exception { // llega un DtUsuario		
		if (u != null) {			
			try {	
				PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				Query q;
				Date d = null;				
				if (!checkCambioRol(u, tipoU)) {
					if (u instanceof DtCiudadano) {
						try {
							String dt = ((DtCiudadano)u).getFNac();
							d = new SimpleDateFormat("yyyy-MM-dd").parse(dt);						
							q = em.createQuery("update UsuarioFrontOffice set nombreCompleto=:n, cedula=:c, correo=:m, fNac=:f, domicilio=:d, nacionalidad=:nc where idUsuarioFO=:id").setHint("org.hibernate.cacheMode", "IGNORE");
							q.setParameter("n", ((DtCiudadano)u).getNombreCompleto());
							q.setParameter("c", ((DtCiudadano)u).getCedula());			
							q.setParameter("m", ((DtCiudadano)u).getCorreo());					
							q.setParameter("f", d);
							q.setParameter("d", ((DtCiudadano)u).getDomicilio());
							q.setParameter("nc", ((DtCiudadano)u).getNacionalidad());
							q.setParameter("id", Long.parseLong(((DtCiudadano)u).getId()));
							q.executeUpdate();
							em.flush();		
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} else if (u instanceof DtFuncionario) {
						try {
							String dt = ((DtFuncionario)u).getFNac();
							d = new SimpleDateFormat("yyyy-MM-dd").parse(dt);
						} catch (ParseException e) {
								e.printStackTrace();
						}
						Organismo o = buscarOrganismo(((DtFuncionario)u).getOrganismo());
						if (o != null) {
							q = em.createQuery("update UsuarioFrontOffice set nombreCompleto=:n, cedula=:c, correo=:m, fNac=:f, domicilio=:d, nacionalidad=:nc, oficina=:o, cargo=:cg where idUsuarioFO=:id").setHint("org.hibernate.cacheMode", "IGNORE");
							q.setParameter("n", ((DtFuncionario)u).getNombreCompleto());
							q.setParameter("c", ((DtFuncionario)u).getCedula());			
							q.setParameter("m", ((DtFuncionario)u).getCorreo());					
							q.setParameter("f", d);
							q.setParameter("d", ((DtFuncionario)u).getDomicilio());
							q.setParameter("nc", ((DtFuncionario)u).getNacionalidad());
							q.setParameter("cg", cargo);						
							q.setParameter("o", o);
							q.setParameter("id", Long.parseLong(((DtFuncionario)u).getId()));
							q.executeUpdate();
							em.flush();					
						} else throw new OrganismoException("Organismo no existe");
					} else if (u instanceof DtAutoridad) {
						q = em.createQuery("update Autoridad set nombreCompleto=:n, cedula=:c, correo=:m, password=:p where idAutoridad=:id").setHint("org.hibernate.cacheMode", "IGNORE");
						q.setParameter("n", ((DtAutoridad)u).getNombreCompleto());
						q.setParameter("c", ((DtAutoridad)u).getCedula());			
						q.setParameter("m", ((DtAutoridad)u).getCorreo());
						String p = null;
						if (pass == null) {
							p = ((DtAdministrador)u).getPassword();
						} else p = passwordEncoder.encode(pass);
						q.setParameter("p", p);
						q.setParameter("id", Long.parseLong(((DtAutoridad)u).getId()));
						q.executeUpdate();
						em.flush();					
					} else if (u instanceof DtAdministrador) {
						q = em.createQuery("update Administrador set nombreCompleto=:n, cedula=:c, correo=:m, password=:p where idAdmin=:id").setHint("org.hibernate.cacheMode", "IGNORE");
						q.setParameter("n", ((DtAdministrador)u).getNombreCompleto());
						q.setParameter("c", ((DtAdministrador)u).getCedula());			
						q.setParameter("m", ((DtAdministrador)u).getCorreo());
						String p = null;
						if (pass == null) {
							p = ((DtAdministrador)u).getPassword();
						} else p = passwordEncoder.encode(pass);
						q.setParameter("p", p);
						q.setParameter("id", Long.parseLong(((DtAdministrador)u).getId()));
						q.executeUpdate();
						em.flush();					
					}
				} else {	
					String nuevoRol = tipoU;
					if (u instanceof DtCiudadano) {						
						switch (nuevoRol) {
							case "Funcionario":								
								if (organismo != null) {
									Organismo o = buscarOrganismo(organismo);
									if (o != null) {
										DtFuncionario f = new DtFuncionario(((DtCiudadano)u).getCedula(), ((DtCiudadano)u).getNombreCompleto(), ((DtCiudadano)u).getCorreo(), ((DtCiudadano)u).getFNac(), ((DtCiudadano)u).getNacionalidad(), ((DtCiudadano)u).getDomicilio(), organismo, cargo, "Funcionario");
										bajaCiudadano(((DtCiudadano)u).getCorreo());
										altaFuncionario(f, o);
									} else throw new OrganismoException("imposible modificar, Organismo no existe");
								} else throw new OrganismoException("imposible modificar a funcionario, organismo vacio"); 
								break;
							case "Autoridad":
								DtAutoridad a = new DtAutoridad(((DtCiudadano)u).getCedula(), passwordEncoder.encode(pass), ((DtCiudadano)u).getCorreo(), ((DtCiudadano)u).getNombreCompleto(), "Autoridad"); 
								bajaCiudadano(((DtCiudadano)u).getCorreo());
								altaAutoridad(a);
								break;
							case "Administrador":
								DtAdministrador ad = new DtAdministrador(((DtCiudadano)u).getCedula(), passwordEncoder.encode(pass), ((DtCiudadano)u).getCorreo(), ((DtCiudadano)u).getNombreCompleto(), "Administrador"); 
								bajaCiudadano(((DtCiudadano)u).getCorreo());
								altaAdmin(ad);
								break;
							default: break;
						}						
					} else if (u instanceof DtFuncionario) {						
						switch (nuevoRol) {
							case "Ciudadano":	
								List<String> lm = null;
								List<Certificado> ctf = null;		
								List<CfgNoti> ln = null;
								try {
									String dt = ((DtFuncionario)u).getFNac();
									d = new SimpleDateFormat("yyyy-MM-dd").parse(dt);
									Ciudadano c = new Ciudadano(((DtFuncionario)u).getCedula(), ((DtFuncionario)u).getNombreCompleto(), ((DtFuncionario)u).getCorreo(), d, ((DtFuncionario)u).getNacionalidad(), ((DtFuncionario)u).getDomicilio(), lm, ctf, ln);
									bajaFuncionario(((DtFuncionario)u).getCorreo());
									altaCiudadano(c);	
									break;
								} catch (Exception e) {
									throw e;
								}							
							case "Autoridad":
								DtAutoridad a = new DtAutoridad(((DtFuncionario)u).getCedula(), passwordEncoder.encode(pass), ((DtFuncionario)u).getCorreo(), ((DtFuncionario)u).getNombreCompleto(), "Autoridad"); 
								bajaFuncionario(((DtFuncionario)u).getCorreo());
								altaAutoridad(a);
								break;
							case "Administrador":
								DtAdministrador ad = new DtAdministrador(((DtFuncionario)u).getCedula(), passwordEncoder.encode(pass), ((DtFuncionario)u).getCorreo(), ((DtFuncionario)u).getNombreCompleto(), "Administrador"); 
								bajaFuncionario(((DtFuncionario)u).getCorreo());
								altaAdmin(ad);
								break;
							default: break;
						}						
					} else if (u instanceof DtAutoridad) {						
						switch (nuevoRol) {
							case "Ciudadano":	
								List<String> lm = null;
								List<Certificado> ctf = null;		
								List<CfgNoti> ln = null;
								try {								
									d = new SimpleDateFormat("yyyy-MM-dd").parse("2011-11-31");
									Ciudadano c = new Ciudadano(((DtAutoridad)u).getCedula(), ((DtAutoridad)u).getNombreCompleto(), ((DtAutoridad)u).getCorreo(), d, "", "", lm, ctf, ln);
									bajaAutoridad(((DtAutoridad)u).getCedula());
									altaCiudadano(c);	
									break;
								} catch (Exception e) {
									throw e;
								}							
							case "Funcionario":								
								if (organismo != null) {								
									Organismo o = buscarOrganismo(organismo);
									if (o != null) {
										DtFuncionario f = new DtFuncionario(((DtAutoridad)u).getCedula(), ((DtAutoridad)u).getNombreCompleto(), ((DtAutoridad)u).getCorreo(), "2022-05-31", "", "", organismo, cargo, "Funcionario");
										bajaAutoridad(((DtAutoridad)u).getCedula());
										altaFuncionario(f, o);
									} else throw new OrganismoException("imposible modificar, Organismo no existe");
								} else throw new OrganismoException("imposible modificar a funcionario, organismo vacio"); 
								break;
							case "Administrador":
								DtAdministrador ad = new DtAdministrador(((DtAutoridad)u).getCedula(), passwordEncoder.encode(pass), ((DtAutoridad)u).getCorreo(), ((DtAutoridad)u).getNombreCompleto(), "Administrador"); 
								bajaAutoridad(((DtAutoridad)u).getCedula());
								altaAdmin(ad);
								break;
							default: break;
						}						
				  }	else if (u instanceof DtAdministrador) {						
					    switch (nuevoRol) {
							case "Ciudadano":	
								List<String> lm = null;
								List<Certificado> ctf = null;		
								List<CfgNoti> ln = null;
								try {								
									d = new SimpleDateFormat("yyyy-MM-dd").parse("2011-11-31");
									Ciudadano c = new Ciudadano(((DtAdministrador)u).getCedula(), ((DtAdministrador)u).getNombreCompleto(), ((DtAdministrador)u).getCorreo(), d, "", "", lm, ctf, ln);
									bajaAdmin(((DtAdministrador)u).getCedula());
									altaCiudadano(c);	
									break;
								} catch (Exception e) {
									throw e;
								}							
							case "Funcionario":								
								if (organismo != null) {
									Organismo o = buscarOrganismo(organismo);
									if (o != null) {
										DtFuncionario f = new DtFuncionario(((DtAdministrador)u).getCedula(), ((DtAdministrador)u).getNombreCompleto(), ((DtAdministrador)u).getCorreo(), "2022-05-31", "", "", organismo, cargo, "Funcionario");
										bajaAdmin(((DtAdministrador)u).getCedula());
										altaFuncionario(f, o);
									} else throw new OrganismoException("imposible modificar, Organismo no existe");
								} else throw new OrganismoException("imposible modificar a funcionario, organismo vacio"); 
								break;
							case "Autoridad":
								DtAutoridad a = new DtAutoridad(((DtAdministrador)u).getCedula(), passwordEncoder.encode(pass), ((DtAdministrador)u).getCorreo(), ((DtAdministrador)u).getNombreCompleto(), "Autoridad"); 
								bajaAdmin(((DtAdministrador)u).getCedula());
								altaAutoridad(a);
								break;
							default: break;
					  }						
				   }		
				}				
			} catch (Exception e) {
				throw e;
			}
		}
	}

	
	private boolean checkCambioRol(Object u, String tipoU) {		
		boolean res = false;
		if (tipoU != null && u != null) {			
			if (u instanceof DtCiudadano) {
				if (!tipoU.equals("Ciudadano")) 
					res = true;				
			} else if (u instanceof DtFuncionario) { 			
				if (!tipoU.equals("Funcionario")) 
					res = true;				
			} else if (u instanceof DtAdministrador) {  		
				if (!tipoU.equals("Administrador")) 
					res = true;				
			} else if (u instanceof DtAutoridad) 
				if (!tipoU.equals("Autoridad"))
					res = true;		
		}
		return res;
	}
	
	public List<Object> listarUsuarios() throws Exception {
		List<Object> todos =  new ArrayList<>();		
		Query q = em.createQuery("select u from UsuarioFrontOffice u");
		List<UsuarioFrontOffice> lu = q.getResultList();
		for(UsuarioFrontOffice u: lu)
			if (u instanceof Funcionario) {				
				DtFuncionario dt = ((Funcionario) u).getDt();
				dt.setProcesoCreado(listarProcesoCreadosUsuario((Funcionario) u));
				todos.add(dt);
			} else if (u instanceof Ciudadano) {
				DtCiudadano dt = ((Ciudadano) u).getDt();
				dt.setIniSeguidas(listarIniciativasSeguidasUsuario((Ciudadano) u));
				dt.setIniAdheridas(listarIniciativasAdheridasUsuario((Ciudadano) u));	
				dt.setIniCreadas(listarIniciativasCreadasUsuario((Ciudadano) u, null));	
				dt.setCertificados(listarCertificadosUsuario(dt.getCorreo()));	
				dt.setProPart(listarProcesoParticipandoUsuario((Ciudadano) u));
				dt.setProSeguidos(listarProcesoSeguidoUsuario((Ciudadano) u));
				dt.setMensajes(listarMensajesUsuario(dt.getCorreo()));
				todos.add(dt);
			}
		
		q = em.createQuery("select a from Administrador a");
		List<Administrador> lad = q.getResultList();
		for(Administrador u: lad)
			todos.add(u.getDt());		
		
		q = em.createQuery("select r from Autoridad r");
		List<Autoridad> lau = q.getResultList();
		for(Autoridad u: lau)
			todos.add(u.getDt());	
		
		return todos;
	}
	
	public int contarCiudadanos() {
		Query q = em.createQuery("select count(*) from Ciudadano");
		Long res = (Long) q.getSingleResult();
		return res.intValue();
	}

	public int contarFuncionarios() {
		Query q = em.createQuery("select count(*) from Funcionario");
		Long res = (Long) q.getSingleResult();
		return res.intValue();
	}

	public int contarAdministradores() {
		Query q = em.createQuery("select count(*) from Administrador");
		Long res = (Long) q.getSingleResult();
		return res.intValue();
	}

	public int contarAutoridades() {
		Query q = em.createQuery("select count(*) from Autoridad");
		Long res = (Long) q.getSingleResult();
		return res.intValue();
	}
	
	// ORGANISMO
	
	public void altaOrganismo(Organismo f) {
		if (f != null) {
			em.persist(f);	
			em.flush();
		}
	}
	
	public Organismo buscarOrganismo(String nombre) throws OrganismoException {
		try {
			Query q = em.createQuery("select a from Organismo a where a.nombre=:u");
			q.setParameter("u", nombre);
			Organismo a = (Organismo) q.getSingleResult();
			em.refresh(a);
			if (a != null)
				return a;
			else return null;
		} catch (Exception e) {
			throw new OrganismoException("Organismo no existe");
		}
	}
	
	public void bajaOrganismo(String f) throws OrganismoException {
		if (f != null) {
			Organismo o = buscarOrganismo(f);
			em.remove(o);
			em.flush();
		}
	}

	public List<DtOrganismo> listaOrganismos() {
		List<Organismo> lI = (List<Organismo>) em.createNamedQuery("Organismo.findAll").getResultList();
		List<DtOrganismo> ldt = new ArrayList<>();
		for (Organismo u : lI)
			ldt.add(u.getDt());
		return ldt;		
	}
	
	public void modificarOrganismo(Organismo o) {
		try {
			if (o != null) {
				Query q = em.createQuery("update Organismo set nombre=:n, departamento=:d where idOrganismo=:id").setHint("org.hibernate.cacheMode", "IGNORE");
				q.setParameter("n", o.getNombre());
				q.setParameter("d", o.getDepartamento());
				q.setParameter("id", o.getIdOrganismo());
				q.executeUpdate();	
				em.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	// PERIFERICOS
	
	public void altaUrisInicial(NodosPerifericos n) {
		if (n != null) {
			em.persist(n);	
			em.flush();
		}
	}
	
	public void altaUri(NodosPerifericos n) {
		if (n != null) {
			em.merge(n);	
			em.flush();
		}
	}
		
	public void bajaUri(String uri, String flag) throws Exception {
		if (uri != null) {
			NodosPerifericos n = NodosPerifericos.getInstancia();
			try {
				if (flag.equals("e"))
					n.removeUriEvaluador(uri);
				else if (flag.equals("o"))
					n.removeUriOrganizacion(uri);
				em.merge(n);	
				em.flush();
			} catch (Exception e) {
				throw e;
			}
		}		
	}
	
	public void altaOrganizacion(Organizacion o) {
		if (o != null) {
			em.persist(o);	
			em.flush();
		}		
	}
	
	public Organizacion buscarOrganizacion(String correo) throws Exception {
		Organizacion r = null;		
		Query q = em.createQuery("select o from Organizacion o where correo=:c");
		q.setParameter("c", correo);
		r = (Organizacion) q.getSingleResult();
		em.refresh(r);
		if (r != null) {
			return r;			
		}
		else throw new OrganizacionException("Organizacion no encontrada");
	}
	
	public List<DtOrganizacion> listarOrganizaciones() {
		List<Organizacion> lI = (List<Organizacion>) em.createNamedQuery("Organizacion.findAll").getResultList();
		List<DtOrganizacion> ldt = new ArrayList<>();
		for (Organizacion u: lI) {
			DtOrganizacion dt = ((Organizacion) u).getDt();	
			try {				
				dt.setIniciativaCreada(listarIniciativasCreadasUsuario(null, u));
				ldt.add(dt);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
		}
		return ldt;
	}

	public void borrarOrganizacion(String correo) {
		if (correo != null) {
			try {
				Organizacion i = buscarOrganizacion(correo);
				em.remove(i);	
				em.flush();
			} catch (Exception e) {
				e.getMessage();
			}			
		}
	}
	
	public void modificarOrganizacion(Organizacion o) {
		try {
			if (o != null) {
				Query q = em.createQuery("update Organizacion set nombre=:n, correo=:d where idOrganizacion=:id").setHint("org.hibernate.cacheMode", "IGNORE");
				q.setParameter("n", o.getNombre());
				q.setParameter("d", o.getCorreo());
				q.setParameter("id", o.getIdOrganizacion());
				q.executeUpdate();								
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
		
	public void altaEvaluador(Evaluador o) {
		if (o != null) {
			em.persist(o);
			em.flush();
		}		
	}
	
	public Evaluador buscarEvaluador(String n) throws Exception {
		Evaluador r = null;		
		Query q = em.createQuery("select e from Evaluador e where nombre=:n");
		q.setParameter("n", n);
		r = (Evaluador) q.getSingleResult();
		em.refresh(r);	
		if (r != null) {
			return r;			
		}
		else throw new Exception("Evaluador no encontrada");
	}
	
	public List<DtEvaluador> listarEvaluadores() {
		List<Evaluador> lI = (List<Evaluador>) em.createNamedQuery("Evaluador.findAll").getResultList();
		List<DtEvaluador> ldt = new ArrayList<>();
		for (Evaluador u : lI)
			ldt.add(u.getDt());
		return ldt;
	}	
	
	public void borrarEvaluador(String nombre) {
		if (nombre != null) {
			try {
				Evaluador i = buscarEvaluador(nombre);
				em.remove(i);	
				em.flush();
			} catch (Exception e) {
				e.getMessage();
			}			
		}
	}
	
	public void modificarEvaluador(Evaluador o) {
		try {
			if (o != null) {
				Query q = em.createQuery("update Evaluador set nombre=:n where idEvaluador=:id").setHint("org.hibernate.cacheMode", "IGNORE");
				q.setParameter("n", o.getNombre());
				q.setParameter("id", o.getIdEvaluador());
				q.executeUpdate();							
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	// INCIATIVAS
	@Lock(LockType.READ)
	public void altaIniciativa(Iniciativa i) throws Exception {
		try {
			if (i != null) {				
				if (checkUser(i.getCreador())) {	
					em.persist(i);	
					em.flush();
				} else throw new UsuarioException("Usuario no exite");
			} else throw new IniciativaException ("Error en los datos de la iniciativa");		
		} catch (Exception e) {
			try {								
				if (buscarOrganizacion(i.getCreador()) != null) {			
					em.persist(i);	
					em.flush();
				} else throw new UsuarioException("ORG no exite");							
			} catch (Exception e2) {			
				throw e2;
			}
		}	
	}
	

	public Iniciativa buscarIniciativa(Long id) {
		Iniciativa ret = null;
		Query q = em.createQuery("select i from Iniciativa i where i.idIniciativa=:n");
		q.setParameter("n", id);
		try {
			ret = (Iniciativa) q.getSingleResult();
			em.refresh(ret);			
		} catch (Exception e) {
			ret = null;
		}
		return ret;		
	}	

	public Iniciativa buscarIniciativa(String nombre) {
		Iniciativa ret = null;
		Query q = em.createQuery("select i from Iniciativa i where i.nombre=:n");
		q.setParameter("n", nombre);
		try {
			ret = (Iniciativa) q.getSingleResult();
			em.refresh(ret);			
		} catch (Exception e) {
			ret = null;
		}
		return ret;		
	}
	
	public Iniciativa checkIniciativaDisp(String nombre) throws Exception {			
		Iniciativa l = null;
		try {
			Query q = em.createQuery("select a from Iniciativa a where nombre=:n");
			q.setParameter("n", nombre);			
			l = (Iniciativa) q.getSingleResult();
			em.refresh(l);			
		} catch (Exception e) {	}	
		return l;
	}	
	
	public void bajaIniciativa(String nombre) throws Exception {
		if (nombre != null) {
			try {
				Iniciativa i = buscarIniciativa(nombre);
				em.remove(i);
				em.flush();
			} catch (Exception e) {
				throw new IniciativaException("Iniciativa no encontrada");
			}			
		}
	}
	
	public void modificarIniciativa(String nombre) {
		Iniciativa i = null;
		Query q = em.createQuery("select i from Iniciativa i where i.nombre=:n");
		q.setParameter("n", nombre);
		try {
			i = (Iniciativa) q.getSingleResult();
			em.refresh(i);				
			if (i != null) {
				Estado e = i.getEstado();
				if (e.equals(Estado.En_espera)) 
					i.setEstado(Estado.Procesando);
				else if (e.equals(Estado.Procesando)) 
					i.setEstado(Estado.Evaluando);
				else if (e.equals(Estado.Evaluando)) 
					i.setEstado(Estado.Aceptado);
				else if (e.equals(Estado.Aceptado)) 
					i.setEstado(Estado.Publicado);				
				em.persist(i);		
				em.flush();
			}			
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	public void modificarIniciativa(DtIniciativa d) {
		try {
			if (d != null) {				
				String dt = d.getFecha();
				Date fc = new SimpleDateFormat("yyyy-MM-dd").parse(dt);				
				Query q = em.createQuery("update Iniciativa set nombre=:n, descripcion=:d, fecha=:f, estado=:e, creador=:cr where idIniciativa=:id").setHint("org.hibernate.cacheMode", "IGNORE");
				q.setParameter("n", d.getNombre());
				q.setParameter("d", d.getDescripcion());
				q.setParameter("f", fc);
				q.setParameter("e", Estado.valueOf(d.getEstado()));
				q.setParameter("cr", d.getCreador());
				q.setParameter("id", Long.parseLong(d.getId()));
				q.executeUpdate();	
				em.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void modificarIniciativa(Iniciativa d) {
		try {
			if (d != null) {		
				em.persist(d);	
				em.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public List<DtIniciativa> listarIniciativas() throws Exception {
		List<Iniciativa> lI = (List<Iniciativa>) em.createNamedQuery("Iniciativa.findAll").getResultList();
		List<DtIniciativa> ldt = new ArrayList<>();
		for (Iniciativa u: lI) {
			DtIniciativa k = u.getDt();
			if (u.getIdRecurso().size() > 0) 
				k.setRecurso(nsql.getRecursoIniciativa(u.getIdRecurso().get(0)));				
			if (u.getIdComentario().size() > 0) 
				for (int c = 0; c < u.getIdComentario().size(); c++)			
					k.agregarComentario(nsql.getComentarios(u.getIdComentario().get(c)));
			ldt.add(k);
		}
		return ldt;
	}	
	
	public List<DtIniciativa> listarIniciativasPublicadas() throws Exception {
		Query q = em.createQuery("SELECT i FROM Iniciativa i WHERE estado=:p");
		q.setParameter("p", Estado.Publicado);
		List<Iniciativa> lI = (List<Iniciativa>) q.getResultList();
		List<DtIniciativa> ldt = new ArrayList<>();
		for (Iniciativa u: lI) {
			DtIniciativa k = u.getDt();
			if (u.getIdRecurso().size() > 0) 
				k.setRecurso(nsql.getRecursoIniciativa(u.getIdRecurso().get(0)));				
			if (u.getIdComentario().size() > 0) 
				for (int c = 0; c < u.getIdComentario().size(); c++)			
					k.agregarComentario(nsql.getComentarios(u.getIdComentario().get(c)));
			ldt.add(k);
		}
		return ldt;
	}

	public List<DtIniciativa> listarIniciativasFechas(Date f1, Date f2) throws Exception {
		Query q = em.createQuery("from Iniciativa where fecha between :f1 and :f2");
		q.setParameter("f1", f1);
		q.setParameter("f2", f2);
		List<Iniciativa> lI = q.getResultList();
		List<DtIniciativa> ldt = new ArrayList<>();
		for (Iniciativa u: lI) {
			DtIniciativa k = u.getDt();
			if (u.getIdRecurso().size() > 0) 
				k.setRecurso(nsql.getRecursoIniciativa(u.getIdRecurso().get(0)));				
			if (u.getIdComentario().size() > 0) 
				for (int c = 0; c < u.getIdComentario().size(); c++)			
					k.agregarComentario(nsql.getComentarios(u.getIdComentario().get(c)));
			ldt.add(k);
		}
		return ldt;
	}
	
	public void adherirUsuarioIniciativa(String nombreIniciativa, String user) throws Exception {
		Iniciativa i = buscarIniciativa(nombreIniciativa);
		Ciudadano c = (Ciudadano) buscarUsuario(user);			
		// implica seguirla
		if (!usuarioAdherido(i, c)) {
			if (i.getEstado().toString().equals("Publicado")) {
				i.agregarUsuarioAdherido(c);
				em.persist(i);			
				em.flush();
			} else throw new IniciativaException ("Iniciativa no está publicada");	
		} else throw new IniciativaException ("Usuario ya está adherido");		
	
	}
	
	public void desadherirUsuarioIniciativa(String nombreIniciativa, String user) throws Exception {
		Iniciativa i = buscarIniciativa(nombreIniciativa);
		Ciudadano c = (Ciudadano) buscarUsuario(user);		
		// implica dejar de seguir		
		if (usuarioAdherido(i, c)) {	
			i.quitarUsuarioAdherido(c);
			em.persist(i);	
			em.flush();
			} else throw new IniciativaException ("Iniciativa no está publicada");
	}
	
	public void seguirUsuarioIniciativa(String nombreIniciativa, String user) throws Exception {
		Iniciativa i = buscarIniciativa(nombreIniciativa);
		Ciudadano c = (Ciudadano) buscarUsuario(user);		
		
		// no implica adherirse
		if (!usuarioSiguiendoInicativa(i, c)) {
			if (i.getEstado().toString().equals("Publicado")) {
				i.agregarUsuarioSeguidor(c);			
				em.persist(i);		
				em.flush();
			} else throw new IniciativaException ("Iniciativa no esta publciada");		
		} else throw new IniciativaException("Usuario ya sigue la iniciativa");		
	}
	
	public void dejarSeguirUsuarioIniciativa(String nombreIniciativa, String user) throws Exception {
		Iniciativa i = buscarIniciativa(nombreIniciativa);
		Ciudadano c = (Ciudadano) buscarUsuario(user);
		
		// no implica deshaderirse
		if (usuarioSiguiendoInicativa(i, c)) {
			i.quitarUsuarioSeguidor(c);
			em.persist(i);		
			em.flush();
		} else throw new IniciativaException("Usuario no sigue la iniciativa");		
	}
		
	public boolean usuarioAdherido(Iniciativa i, Ciudadano cu) {	// true si esta, false si no
		List<Ciudadano> c = i.getAdheridos(); 				
		if (c.contains(cu)) 
			return true;		
		else 	
			return false;		
	}
	
	public boolean usuarioSiguiendoInicativa(Iniciativa i, Ciudadano cu) {	// true si esta, false si no	
		List<Ciudadano> c = i.getSeguidores(); 				
		if (c.contains(cu)) 
			return true;		
		else 	
			return false;		
	}
	
	public List<String> listarAdheridosIniciativa(String nombreIniciativa) {
		Iniciativa i = buscarIniciativa(nombreIniciativa);
		List<String> l = new ArrayList<>();
		if (i != null) {
			l.add("Iniciativa: " + nombreIniciativa + " - Adheridos");			
			List<Ciudadano> lc = i.getAdheridos();
			for (Ciudadano c: lc)
				l.add(c.getCorreo());
		}			
		return l;
	}
	
	public List<String> listarSeguidoresIniciativa(String nombreIniciativa) {
		Iniciativa i = buscarIniciativa(nombreIniciativa);
		List<String> l = new ArrayList<>();
		if (i != null) {
			l.add("Iniciativa: " + nombreIniciativa + " - Seguidores");			
			List<Ciudadano> lc = i.getSeguidores();
			for (Ciudadano c: lc)
				l.add(c.getCorreo());
		}			
		return l;
	}
	
	public List<DtIniciativa> listarRelacionadosIniciativas() {
		List<Iniciativa> lI = (List<Iniciativa>) em.createNamedQuery("Iniciativa.findAll").getResultList();
		List<DtIniciativa> ldt = new ArrayList<>();
		for (Iniciativa u: lI)			
			ldt.add(u.getDt());
		return ldt;
	}
	
	public List<String> listarIniciativasAdheridasUsuario(Ciudadano c) throws Exception {		
		List<String> ret = new ArrayList<>();
		if (c != null) {
			List<Iniciativa> lI = (List<Iniciativa>) em.createNamedQuery("Iniciativa.findAll").getResultList();
			for(Iniciativa k: lI) {
				List<Ciudadano> w = k.getAdheridos();
				if (w.size() > 0)
					if (w.contains(c)) 
						ret.add(k.getNombre());					
			}
		}
		return ret;			
	}
	
	public List<String> listarIniciativasSeguidasUsuario(Ciudadano c) throws Exception {	
		List<String> ret = new ArrayList<>();	
		if (c != null) {
			List<Iniciativa> lI = (List<Iniciativa>) em.createNamedQuery("Iniciativa.findAll").getResultList();
			for(Iniciativa k: lI) {
				List<Ciudadano> w = k.getSeguidores();
				if (w.size() > 0)
					if (w.contains(c)) 
						ret.add(k.getNombre());				
			}
		}
		return ret;			
	}
	
	public List<String> listarIniciativasCreadasUsuario(Ciudadano c, Organizacion o) throws Exception {		
		List<String> ret = new ArrayList<>();	
		List<Iniciativa> lI = (List<Iniciativa>) em.createNamedQuery("Iniciativa.findAll").getResultList();						
		if (c != null) {
			for(Iniciativa k: lI) 
				if (c.getCorreo().equals(k.getCreador())) 
					ret.add(k.getNombre());				
		} else if (o != null) {
				for(Iniciativa k: lI) 									
					if (o.getCorreo().equals(k.getCreador())) 
						ret.add(k.getNombre());						
		} else throw new Exception("Dato null");
		return ret;				
	}
	
	public List<String> listarProcesoCreadosUsuario(Funcionario c) throws Exception {	
		List<String> ret = new ArrayList<>();
		if (c != null) {
			List<ProcesoParticipativo> lI = (List<ProcesoParticipativo>) em.createNamedQuery("Proceso.findAll").getResultList();
			for(ProcesoParticipativo k: lI) 
				if (c.getCorreo().equals(k.getCreador()))
					ret.add(k.getNombre());		
		}
		return ret;			
	}
	
	public List<String> listarProcesoParticipandoUsuario(Ciudadano c) {
		List<String> ret = new ArrayList<>();		
		try {
			List<ProcesoParticipativo> lI = (List<ProcesoParticipativo>) em.createNamedQuery("Proceso.findAll").getResultList();
			if (c!=null) 
				for(ProcesoParticipativo k: lI) {
					List<Ciudadano> lC = k.getParticipantes();	
					if (lC.size() > 0)
						if (lC.contains(c))
							ret.add(k.getNombre());	
				}		
			} catch (Exception e) {
				e.printStackTrace();
			}	
		return ret;			
	}
	
	public List<String> listarProcesoSeguidoUsuario(Ciudadano c) {
		List<String> ret = new ArrayList<>();	
		if (c != null) {
			List<ProcesoParticipativo> lI = (List<ProcesoParticipativo>) em.createNamedQuery("Proceso.findAll").getResultList();
			for(ProcesoParticipativo k: lI) {
				List<Ciudadano> w = k.getSeguidores();
				if (w.size() > 0)
					if (w.contains(c)) 
						ret.add(k.getNombre());				
			}
		}
		return ret;		
	}
	
	public int contarIniciativas() {
		Query q = em.createQuery("select count(*) from Iniciativa");
		Long res = (Long) q.getSingleResult();
		return res.intValue();
	}
	
	public int contarAdhesiones() {
		List<Iniciativa> lI = (List<Iniciativa>) em.createNamedQuery("Iniciativa.findAll").getResultList();
		int cant = 0;
		for (Iniciativa i: lI) {
			List<Ciudadano> lC = i.getAdheridos();
			if (lC != null)
				cant = cant + lC.size();
		}
		return cant;
	}
	
	public int contarParticipaciones() {
		List<ProcesoParticipativo> lI = (List<ProcesoParticipativo>) em.createNamedQuery("Proceso.findAll").getResultList();
		int cant = 0;
		for (ProcesoParticipativo i: lI) {
			List<Ciudadano> lC = i.getParticipantes();
			if (lC != null)
				cant = cant + lC.size();
		}
		return cant;		
	}
		
	// PROCESOS	
	public void altaProceso(ProcesoParticipativo i) throws Exception {
		if (i != null) {	
			em.persist(i);		
			em.flush();
		}
	}

	public ProcesoParticipativo buscarProceso(String nombre) {
		ProcesoParticipativo ret = null;
		Query q = em.createQuery("select p from ProcesoParticipativo p where p.nombre=:n");
		q.setParameter("n", nombre);
		try {			
			ret = (ProcesoParticipativo) q.getSingleResult();
			em.refresh(ret);
			if (ret != null) {
				return ret;
			}
		} catch (Exception e) {
			System.out.println("nombre null " + nombre);
			return null;	
		}
		return null;		
	}
	
	public void modificarProceso(DtProceso d) {
		try {
			if (d != null) {				
				String dt = d.getFecha();
				Date fc = new SimpleDateFormat("MM-dd-yyyy").parse(dt);				
				Query q = em.createQuery("update ProcesoParticipativo set nombre=:n, descripcionAlcance=:d, fecha=:f, creador=:cr, fase=:fase where idProceso=:id").setHint("org.hibernate.cacheMode", "IGNORE");
				q.setParameter("n", d.getNombre());
				q.setParameter("d", d.getDescripcionAlcance());
				q.setParameter("f", fc);
				q.setParameter("fase", FaseProceso.valueOf(d.getFase()));
				q.setParameter("cr", d.getCreador());
				q.setParameter("id", Long.parseLong(d.getId()));
				q.executeUpdate();
				em.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void modificarProceso(ProcesoParticipativo d) {
		try {
			if (d != null) 				
				em.persist(d);			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void bajaProceso(String nombre) throws Exception {
		if (nombre != null) {
			try {
				ProcesoParticipativo i = buscarProceso(nombre);
				i.removeIns();
				i.removeP();
				persistParticipante(i);
				em.remove(i);	
				em.flush();
			} catch (Exception e) {
				throw new ProcesoException("Proceso no existe");
			}			
		}
	}
		
	public void persistParticipante(ProcesoParticipativo p) {
		if (p != null) {
			em.persist(p);
			em.flush();
		}
	}

	public void quitarParticipanteProceso(String proceso, String correo) throws Exception {
		if (proceso != null) {
			try {
				ProcesoParticipativo p = buscarProceso(proceso);
				if (p !=null) {
					if (correo != null) {
						Ciudadano c = (Ciudadano) buscarUsuario(correo);
						if (c != null) {
							List<Ciudadano> lC = p.getParticipantes();
							if (lC.contains(c))	{						
								p.quitarUsuarioParticipante(c);
								persistParticipante(p);
							} else throw new UsuarioException("Usuario no participa");
						} else throw new UsuarioException("Usuario no encontrado");						
					}				
				}  else throw new ProcesoException("Proceso no encontrado");
			} catch (Exception e) {
				throw e;
			}			
		}
	}

	public List<DtProceso> listarProcesos() throws Exception {
		List<ProcesoParticipativo> lI = (List<ProcesoParticipativo>) em.createNamedQuery("Proceso.findAll").getResultList();
		List<DtProceso> ldt = new ArrayList<>();
		for (ProcesoParticipativo u: lI) {
			DtProceso k = u.getDt();
			if (u.getIdInstrumento().size() > 0) {
				List<String> lD = nsql.getInstrumentoProceso(u.getIdInstrumento().get(0));				
				k.setInstrumento(lD.get(0));
				lD.remove(lD.get(0));
				String[] array = lD.toArray(new String[0]);
				k.setContenidoInstrumento(array);
			}
			if (u.getIdComentario().size() > 0) 
				for (int c = 0; c < u.getIdComentario().size(); c++)			
					k.agregarComentario(nsql.getComentarios(u.getIdComentario().get(c)));
			ldt.add(k);
		}
		return ldt;
	}
	
	public void persistComentario(ProcesoParticipativo k) {
		if (k != null) {
			em.persist(k);
			em.flush();			
		}		
	}
	
	public List<DtProceso> listarProcesosFechas(Date f1, Date f2) throws Exception {
		Query q = em.createQuery("from ProcesoParticipativo where fecha between :f1 and :f2");
		q.setParameter("f1", f1);
		q.setParameter("f2", f2);
		List<ProcesoParticipativo> lI = q.getResultList();
		List<DtProceso> ldt = new ArrayList<>();
		for (ProcesoParticipativo u: lI) {
			DtProceso k = u.getDt();
			if (u.getIdInstrumento().size() > 0) {
				List<String> lD = nsql.getInstrumentoProceso(u.getIdInstrumento().get(0));				
				k.setInstrumento(lD.get(0));
				lD.remove(lD.get(0));
				String[] array = lD.toArray(new String[0]);
				k.setContenidoInstrumento(array);
			}
			ldt.add(k);
		}
		return ldt;
	}
	
	public int contarProcesos() {
		Query q = em.createQuery("select count(*) from ProcesoParticipativo");
		Long res = (Long) q.getSingleResult();  
		return res.intValue();
	}
	
	public void seguirUsuarioProceso(String proceso, String user) throws Exception {
		ProcesoParticipativo i = buscarProceso(proceso);
		Ciudadano c = (Ciudadano) buscarUsuario(user);		
		if (!usuarioSiguiendoProceso(i, c)) {
			i.agregarSeguidor(c);			
			em.persist(i);	
			em.flush();
		} else throw new ProcesoException("Usuario ya sigue el proceso");	
	}

	public void dejarSeguirUsuarioProceso(String proceso, String user) throws Exception {
		ProcesoParticipativo i = buscarProceso(proceso);
		Ciudadano c = (Ciudadano) buscarUsuario(user);				
		if (usuarioSiguiendoProceso(i, c)) {
			i.quitarSeguidor(c);
			em.persist(i);	
			em.flush();			
		} else throw new ProcesoException("Usuario no sigue el proceso");	
	}
	
	public boolean usuarioSiguiendoProceso(ProcesoParticipativo i, Ciudadano cu) {	// true si esta, false si no	
		List<Ciudadano> c = i.getSeguidores(); 	
		if (c.contains(cu)) { 
			return true;		
		} else 	 
			return false;	
	}
	
	
	
	public List<String> listarSeguidoresProceso(String proceso) {
		ProcesoParticipativo i = buscarProceso(proceso);
		List<String> l = new ArrayList<>();
		if (i != null) {
			List<Ciudadano> lc = i.getSeguidores();
			for (Ciudadano c: lc)
				l.add(c.getCorreo());
		}			
		return l;
	}	
		
	// CERTIFICADOS
	
	public void altaCertificadoInicial(Integer nivel) throws Exception {	// certificados de dos niveles para comentarios, seguidores, adhesiones y creadores		
		if (nivel == 1) {			
			InputStream is = Persistencia.class.getResourceAsStream("/logos/creador1.png");
			byte[] logo = IOUtils.toByteArray(is);						
			TipoInsignia k = new TipoInsignia(logo, new Date());
			Certificado c = new Certificado("Creador lvl 1", k, nivel, "Creadores");
			em.persist(c);			
			
			is = Persistencia.class.getResourceAsStream("/logos/adhesion1.png");		
			logo = IOUtils.toByteArray(is);	
			k = new TipoInsignia(logo, new Date());
			c = new Certificado("Adhesion lvl 1", k, nivel, "Adhesiones");
			em.persist(c);			
			
			is = Persistencia.class.getResourceAsStream("/logos/comentario1.png");		
			logo = IOUtils.toByteArray(is);	
			k = new TipoInsignia(logo, new Date());
			c = new Certificado("Comentar lvl 1", k, nivel, "Comentarios");
			em.persist(c);		
			
			is = Persistencia.class.getResourceAsStream("/logos/seguidor1.png");		
			logo = IOUtils.toByteArray(is);	
			k = new TipoInsignia(logo, new Date());
			c = new Certificado("Seguidor lvl 1", k, nivel, "Seguidores");
			em.persist(c);	
			
			is = Persistencia.class.getResourceAsStream("/logos/part1.png");		
			logo = IOUtils.toByteArray(is);	
			k = new TipoInsignia(logo, new Date());
			c = new Certificado("Partcipar lvl 1", k, nivel, "ParticiparProceso");
			em.persist(c);				
		} else if (nivel == 2) {
			InputStream is = Persistencia.class.getResourceAsStream("/logos/creador2.png");
			byte[] logo = IOUtils.toByteArray(is);			
			TipoInsignia k = new TipoInsignia(logo, new Date());
			Certificado c = new Certificado("Creador lvl 2", k, nivel, "Creadores");
			em.persist(c);			
			
			is = Persistencia.class.getResourceAsStream("/logos/comentario2.png");		
			logo = IOUtils.toByteArray(is);	
			k = new TipoInsignia(logo, new Date());
			c = new Certificado("Comentar lvl 2", k, nivel, "Comentarios");
			em.persist(c);			
			
			is = Persistencia.class.getResourceAsStream("/logos/adhesion2.png");		
			logo = IOUtils.toByteArray(is);		
			k = new TipoInsignia(logo, new Date());
			c = new Certificado("Adhesion lvl 2", k, nivel, "Adhesiones");
			em.persist(c);			
			
			is = Persistencia.class.getResourceAsStream("/logos/seguidor2.png");		
			logo = IOUtils.toByteArray(is);				
			k = new TipoInsignia(logo, new Date());
			c = new Certificado("Seguidor lvl 2", k, nivel, "Seguidores");
			em.persist(c);	
			
			is = Persistencia.class.getResourceAsStream("/logos/part2.png");		
			logo = IOUtils.toByteArray(is);	
			k = new TipoInsignia(logo, new Date());
			c = new Certificado("Participar lvl 2", k, nivel, "ParticiparProceso");
			em.persist(c);				
		} else if (nivel == 3) {	
			InputStream is = Persistencia.class.getResourceAsStream("/logos/pro.png");
			byte[] logo = IOUtils.toByteArray(is);			
			TipoInsignia k = new TipoInsignia(logo, new Date());
			Certificado c = new Certificado("Pro", k, nivel, "Pro");
			em.persist(c);		
		}
	}
	
	public void altaCertificado(Certificado c) throws Exception {		
		if (c != null) {
			em.persist(c);	
			em.flush();
		}		
	}
	
	public void altaTipoCertificadoInicial(TipoCertificado l) {	
		if (l != null) 				
			em.persist(l);				
	}	
	
	public void altaTipoCertificado(String tipo) {
		if (tipo != null) {
			TipoCertificado l = TipoCertificado.getInstancia();
			l.agregarTipoCertificado(tipo);
			em.merge(l);
			em.flush();
		}						
	}	
	
	public void agregarCertificadoUsuario(String user, Certificado c) throws Exception {
		if (user != null && c != null) {
			Ciudadano cu = (Ciudadano) buscarUsuario(user);
			if (!cu.getCertificados().contains(c)) {
				cu.agregarCertificado(c);
				em.persist(cu);		
				em.flush();				
			} else throw new CertificadoException("Usuario ya tiene certificado");
		} else throw new Exception("Datos invalidos");
	}
	
	public DtCertificado buscarCertificado(Integer nivel) throws Exception {
		Query q = em.createQuery("select c from Certificado c where nivel=:n");
		q.setParameter("n", nivel);
		Certificado r = (Certificado) q.getSingleResult();  
		if (r != null) {
			byte [] c = r.getInsignia().getLogo();		
			String logo = getLogo(c);
			DtCertificado ret = new DtCertificado(r.getNombre(), logo, r.getInsignia().getFechaObtenido().toString());
			return ret;
		}
		return null;
		
	}
	
	public Certificado buscarCertificadoObject(Integer nivel) throws Exception {
		Query q = em.createQuery("select c from Certificado c where nivel=:n");
		q.setParameter("n", nivel);
		try {
			Certificado r = (Certificado) q.getSingleResult(); 		
			if (r != null)
				return r;
			else return null;
		} catch (Exception e) {
			throw new CertificadoException("Certificado de nivel " + nivel + " no está");
		}			
	}
	
	public Certificado buscarCertificadoTipo(Integer nivel, String tipo) throws Exception {
		Query q = em.createQuery("select c from Certificado c where nivel=:n and tipo=:t");
		q.setParameter("n", nivel);
		q.setParameter("t", tipo);
		try {
			Certificado r = (Certificado) q.getSingleResult(); 		
			if (r != null)
				return r;
			else return null;
		} catch (Exception e) {
			throw new CertificadoException("Certificado no está");
		}		
	}
	
	public List<DtCertificado> listarCertificadosUsuario(String correo) throws Exception {
		Ciudadano c = (Ciudadano) buscarUsuario(correo);
		List<Certificado> lC;
		List<DtCertificado> ret = new ArrayList<>();
		if (c != null) {
			 lC = c.getCertificados();			 
			 if (lC != null && lC.size() > 0)	
				 for(Certificado i: lC) 
					ret.add(i.getDt());				 			
		}		
		return ret;		
	}
	
	public List<DtCertificado> listarDtCertificados() {
		List<Certificado> lC = (List<Certificado>) em.createNamedQuery("Certificado.findAll").getResultList();	 
		List<DtCertificado> r = new ArrayList<>();	
		if (lC != null)	
			for(Certificado i: lC)
				try {
					r.add(i.getDt());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}			
		return r;		
	}
			
	private String getLogo(byte[] img) {	
		String logo64 = null;
		if (img != null) {
			logo64 = new String(Base64.getEncoder().encode(img));			
		}
		return logo64;
	}
	
	public String getLogro(Integer nivel) throws Exception {	
		Certificado r = buscarCertificadoObject(nivel);  
		if (r != null) {
			byte[] c = r.getInsignia().getLogo();		
			String logo = getLogo(c);
			return logo;
		} else return null;		
	}
	
	public List<String> logos() {
		List<Certificado> lI = (List<Certificado>) em.createNamedQuery("Certificado.findAll").getResultList();
		List<String> ret = new ArrayList<>();		
		if (lI != null) {
			for(Certificado i: lI) {
				byte[] k = i.getInsignia().getLogo();
				ret.add(new String(Base64.getEncoder().encode(k)));
			}
		}			
		return ret;
	}
	
	public void borrarCertificado(String nombre) throws Exception {
		Query q = em.createQuery("select c from Certificado c where nombre=:n");
		q.setParameter("n", nombre);
		Certificado r = (Certificado) q.getSingleResult();  
		if (r != null) {
			em.remove(r);		
		} else throw new CertificadoException("Certificado no se encontro con el nombre: " + nombre);
	}

	public int contarSeguidores() {
		List<Iniciativa> lI = (List<Iniciativa>) em.createNamedQuery("Iniciativa.findAll").getResultList();
		List<ProcesoParticipativo> lP = (List<ProcesoParticipativo>) em.createNamedQuery("Proceso.findAll").getResultList();
		int totalSeguidores = 0;
		if (lI != null) 
			for (Iniciativa i: lI) {		
				totalSeguidores = totalSeguidores + i.getSeguidores().size();
			}
		if (lP != null) 
			for (ProcesoParticipativo p: lP) 		
				totalSeguidores = totalSeguidores + p.getSeguidores().size();		
		return totalSeguidores;
	}

	public int contarComentarios() {
		List<Iniciativa> lI = (List<Iniciativa>) em.createNamedQuery("Iniciativa.findAll").getResultList();
		List<ProcesoParticipativo> lP = (List<ProcesoParticipativo>) em.createNamedQuery("Proceso.findAll").getResultList();
		int totalComentarios = 0;
		if (lI != null) 
			for (Iniciativa i: lI) {		
				totalComentarios = totalComentarios + i.getIdComentario().size();
			}
		if (lP != null) 
			for (ProcesoParticipativo p: lP) 		
				totalComentarios = totalComentarios + p.getIdComentario().size();		
		return totalComentarios;
	}


	public void persistNodoCreador(IniciativaNodo i) {
		if (i != null)
			em.persist(i);
	}

	public String getNodoCreador(String creador, String nombre) throws Exception {
		if (creador != null && nombre != null)
			try {
				Query q = em.createQuery("SELECT nodo FROM IniciativaNodo WHERE nombreIniciativa=:n and creador=:c");
				q.setParameter("n", nombre);
				q.setParameter("c", creador);
				String k = (String) q.getSingleResult();				
				return k;
		} catch (Exception e) {
			throw new Exception("Nodo iniciativa no encontrado");
		} else return null;
	}
	
	public List<Iniciativa> listarIniciativasObject() throws Exception {
		return (List<Iniciativa>) em.createNamedQuery("Iniciativa.findAll").getResultList();		
	}

	public void eliminarMensajeEvaluacion(String user, String mensaje) throws Exception {
		try {
			Ciudadano c = (Ciudadano) buscarUsuario(user); 
			if (c != null) {
				List<String> lM = c.getMensajes();
				for (int i=0; i < lM.size(); i++) {
					if (lM.get(i).equals(mensaje)) {			
						c.quitarMensaje(mensaje);
						em.persist(c);						
						em.flush();
					} else throw new MensajeException("Mensaje no encontrado");
				}
			} else throw new UsuarioException("Usuario no encontrado");
		} catch (Exception e) {
			throw e;
		}
	}
		
	public void eliminarMensajesEvaluacion(String user) throws Exception {
		try {
			Ciudadano c = (Ciudadano) buscarUsuario(user); 
			if (c != null) {				
				c.quitarTodos();
				em.persist(c);
				em.flush();	
			}	
		} catch (Exception e) {
			throw e;
		}
	}
}

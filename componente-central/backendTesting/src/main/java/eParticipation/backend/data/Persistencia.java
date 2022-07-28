package eParticipation.backend.data;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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
import eParticipation.backend.excepciones.ProcesoException;
import eParticipation.backend.excepciones.UsuarioException;
import eParticipation.backend.model.Administrador;
import eParticipation.backend.model.Autoridad;
import eParticipation.backend.model.Certificado;
import eParticipation.backend.model.CfgNoti;
import eParticipation.backend.model.Ciudadano;
import eParticipation.backend.model.Estado;
import eParticipation.backend.model.Evaluador;
import eParticipation.backend.model.Funcionario;
import eParticipation.backend.model.Iniciativa;
import eParticipation.backend.model.FaseProceso;
import eParticipation.backend.model.Organismo;
import eParticipation.backend.model.Organizacion;
import eParticipation.backend.model.ProcesoParticipativo;
import eParticipation.backend.model.TipoCertificado;
import eParticipation.backend.model.TipoInsignia;
import eParticipation.backend.model.UsuarioFrontOffice;
import eParticipation.backend.service.PersistenciaLocal;

@SuppressWarnings("unchecked")
public class Persistencia implements PersistenciaLocal {
	
	private static Persistencia instancia = null;
	
	EntityManagerFactory factory = Persistence.createEntityManagerFactory("eParticipationDBTesting");
	EntityManager em = factory.createEntityManager();
	EntityTransaction tx = em.getTransaction();
	
	
	public Persistencia() {	}	
	
	public static Persistencia getInstancia() {
		if (instancia == null) {
			instancia = new Persistencia();
		}
		return instancia;
		
	}
			
	//USUARIOS

	public void altaAdmin(DtAdministrador a){
		// hash con salt
		try {
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String pass = passwordEncoder.encode(a.getPassword());
			Long id;
			if (a.getId() != null)
				id = Long.parseLong(a.getId());
			else id = Long.valueOf(listarUsuarios().size())+1;
			Administrador adm = new Administrador(id, a.getCedula(), pass, a.getCorreo(), a.getNombreCompleto());
			tx.begin();
			em.persist(adm);
			em.flush();
			tx.commit();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void bajaAdmin(String cedula) {
		Administrador f;
		try {
			f = (Administrador) buscarUsuario(cedula);
			if (f != null) {
				tx.begin();
				em.remove(f);
				em.flush();
				tx.commit();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public void altaAutoridad(DtAutoridad a) {
		// hash con salt
		try {
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String pass = passwordEncoder.encode(a.getPassword());
			Long id;
			if (a.getId() != null)
				id = Long.parseLong(a.getId());
			else id = Long.valueOf(listarUsuarios().size())+1;
			Autoridad aut = new Autoridad(id, a.getCedula(), pass, a.getCorreo(), a.getNombreCompleto());
			tx.begin();
			em.persist(aut);
			em.flush();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void bajaAutoridad(String cedula) {
		Autoridad f;
		try {
			f = (Autoridad) buscarUsuario(cedula);
			if (f != null) {
				tx.begin();
				em.remove(f);
				em.flush();
				tx.commit();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public void altaFuncionario(DtFuncionario a, Organismo o) {
		Date d = null;
		try {
			d = new SimpleDateFormat("dd/MM/yyyy").parse(a.getFNac());
		
			Funcionario f;		
			if (buscarOrganismo(o.getNombre()) != null) {
				Long id;
				if (a.getId() != null)
					id = Long.parseLong(a.getId());
				else id = Long.valueOf(listarUsuarios().size())+1;
				f = new Funcionario(id, a.getCedula(), a.getNombreCompleto(), a.getCorreo(), d,
					a.getNacionalidad(), a.getDomicilio(), null, a.getCargo(), o);
				o.agregarEmpleado(f);	
				tx.begin();
				em.persist(f);
				em.flush();
				tx.commit();
			}		
		} catch (Exception e) {
			e.printStackTrace();
	}
	}
	
	public void bajaFuncionario(String correo) {
		Funcionario f;
		try {
			f = (Funcionario) buscarUsuario(correo);
			if (f != null) {
				tx.begin();
				em.remove(f);
				em.flush();
				tx.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void altaCiudadano(Ciudadano c) {	
		if (c != null) {
			tx.begin();
			em.persist(c);
			em.flush();
			tx.commit();
		}
	}
	
	public void altaCiudadano(DtCiudadano a) {		
		try {
			Date d = new SimpleDateFormat("dd/MM/yyy").parse(a.getFNac());			
			Long id;
			if (a.getId() != null)
				id = Long.parseLong(a.getId());
			else id = Long.valueOf(listarUsuarios().size())+2;			
			Ciudadano c = new Ciudadano(id, a.getCedula(), a.getNombreCompleto(), a.getCorreo(), d,	a.getNacionalidad(), a.getDomicilio(), null, null, null);
			System.out.println(c.toString());
			tx.begin();
			em.persist(c);
			em.flush();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void bajaCiudadano(String correo) {
		Ciudadano f;
		try {
			f = (Ciudadano) buscarUsuario(correo);			
			if (f != null) {
				quitarDeps(f);
				tx.begin();
				em.remove(f);
				em.flush();
				tx.commit();
			}
		} catch (Exception e) {	}
		
	}
	
	public void altaOrganismo(Organismo f) {
		if (f != null) {
			tx.begin();
			em.persist(f);
			em.flush();
			tx.commit();

		}
	}
	
	public void bajaOrganismo(String f) {
		if (f != null) {
			Organismo o = buscarOrganismo(f);
			tx.begin();
			em.remove(o);
			em.flush();
			tx.commit();
		}
	}
	
	public Organismo buscarOrganismo(String nombre) {
		Query q = em.createQuery("select a from Organismo a where a.nombre=:u");
		q.setParameter("u", nombre);
		Organismo a = (Organismo) q.getSingleResult();
		em.refresh(a);
		if (a != null)
			return a;
		else return null;
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
				tx.begin();
				Query q = em.createQuery("update Organismo set nombre=:n, departamento=:d where idOrganismo=:id").setHint("org.hibernate.cacheMode", "IGNORE");
				q.setParameter("n", o.getNombre());
				q.setParameter("d", o.getDepartamento());
				q.setParameter("id", o.getIdOrganismo());
				q.executeUpdate();
				em.flush();	
				tx.commit();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
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
			try { // cedula no esta en tabla ADMINS, busco en AUTORIDADES
				q = em.createQuery("select a from Autoridad a where a.cedula=:u or a.correo=:u");
				q.setParameter("u", user);
				a = (Autoridad) q.getSingleResult();
				em.refresh(a);
				if (a != null)
					return ((Autoridad) a);
			} catch (Exception e2) {
				try {
					q = em.createQuery("select c from UsuarioFrontOffice c where c.correo=:u");
					q.setParameter("u", user);					
					a = (Ciudadano) q.getSingleResult();
					em.refresh(a);				
					if (a != null) {
						return ((Ciudadano) a);					

					}
				} catch (Exception e3) {
					try {
						q = em.createQuery("select c from UsuarioFrontOffice c where c.correo=:u");
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
	
	public void modificarUsuario(Object u) { // llega un DtUsuario		
		if (u != null) {			
			try {		
				tx.begin();
				Query q;
				Date d = null;				
				if (!checkCambioRol()) {
					if (u instanceof DtCiudadano) {
						try {
							String dt = ((DtCiudadano)u).getFNac();
							d = new SimpleDateFormat("yyyy-MM-dd").parse(dt);
						} catch (ParseException e) {
								e.printStackTrace();
						}
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
						tx.commit();
					} else if (u instanceof DtFuncionario) {
						try {
							String dt = ((DtFuncionario)u).getFNac();
							d = new SimpleDateFormat("yyyy-MM-dd").parse(dt);
						} catch (ParseException e) {
								e.printStackTrace();
						}
						q = em.createQuery("update UsuarioFrontOffice set nombreCompleto=:n, cedula=:c, correo=:m, fNac=:f, domicilio=:d, nacionalidad=:nc where idUsuarioFO=:id").setHint("org.hibernate.cacheMode", "IGNORE");
						q.setParameter("n", ((DtFuncionario)u).getNombreCompleto());
						q.setParameter("c", ((DtFuncionario)u).getCedula());			
						q.setParameter("m", ((DtFuncionario)u).getCorreo());					
						q.setParameter("f", d);
						q.setParameter("d", ((DtFuncionario)u).getDomicilio());
						q.setParameter("nc", ((DtFuncionario)u).getNacionalidad());
						q.setParameter("id", Long.parseLong(((DtFuncionario)u).getId()));
						q.executeUpdate();
						em.flush();	
						tx.commit();
					} else if (u instanceof DtAutoridad) {
						q = em.createQuery("update Autoridad set nombreCompleto=:n, cedula=:c, correo=:m, password=:p where idAutoridad=:id").setHint("org.hibernate.cacheMode", "IGNORE");
						q.setParameter("n", ((DtAutoridad)u).getNombreCompleto());
						q.setParameter("c", ((DtAutoridad)u).getCedula());			
						q.setParameter("m", ((DtAutoridad)u).getCorreo());					
						q.setParameter("p", ((DtAutoridad)u).getPassword());
						q.setParameter("id", Long.parseLong(((DtAutoridad)u).getId()));
						q.executeUpdate();
						em.flush();	
						tx.commit();
					} else if (u instanceof DtAdministrador) {
						q = em.createQuery("update Administrador set nombreCompleto=:n, cedula=:c, correo=:m, password=:p where idAdmin=:id").setHint("org.hibernate.cacheMode", "IGNORE");
						q.setParameter("n", ((DtAdministrador)u).getNombreCompleto());
						q.setParameter("c", ((DtAdministrador)u).getCedula());			
						q.setParameter("m", ((DtAdministrador)u).getCorreo());					
						q.setParameter("p", ((DtAdministrador)u).getPassword());
						q.setParameter("id", Long.parseLong(((DtAdministrador)u).getId()));
						q.executeUpdate();
						em.flush();		
						tx.commit();
					}
				} else { System.out.println("cambio rol, duduso"); }
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void quitarDeps(Ciudadano f) {
		if (f != null) {
			List<DtIniciativa> lI = listarIniciativas();
			if (lI != null) {
				for (DtIniciativa k: lI) {
					List<String> lC = k.getAdheridos();
					if (lC.contains(f.getCorreo()))
						try {
							desadherirUsuarioIniciativa(k.getNombre(),f.getCorreo());
						} catch (Exception e) {	}
				}
				for (DtIniciativa k: lI) {
					List<String> lC2 = k.getSeguidores();
					if (lC2.contains(f.getCorreo()))
						try {
							dejarSeguirUsuarioIniciativa(k.getNombre(),f.getCorreo());
						} catch (Exception e) {	}
				}				
			}
			List<DtProceso> lP = listarProcesos();
			if (lP!= null) 							
				for (DtProceso k: lP) {
					List<String> lPC = k.getParticipantes();
					if (lPC != null)				
						if (lPC.contains(f.getCorreo()))
							try {
								quitarParticipanteProceso(k.getNombre(),f.getCorreo());
							} catch (Exception e) {	}
				}
			tx.begin();
			em.persist(f);
			em.flush();
			tx.commit();
		}
		
	}
	
	private boolean checkCambioRol() {
		return false;
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
	
	// PERIFERICOS
	
	public void altaOrganizacion(Organizacion o) {
		if (o != null) {	
			tx.begin();
			em.persist(o);
			em.flush();
			tx.commit();
		}		
	}
	
	public Organizacion buscarOrganizacion(String correo) throws Exception {
		Organizacion r = null;		
		Query q = em.createQuery("select o from Organizacion o where correo=:c");		
		q.setParameter("c", correo);
		try {
			r = (Organizacion) q.getSingleResult();
			em.refresh(r);
			if (r != null) {
				return r;			
			}
		} catch (Exception e) {
			throw new Exception("Organizacion no encontrada");
		}
		return r;	
	}
	
	public List<DtOrganizacion> listarOrganizaciones() {
		List<Organizacion> lI = (List<Organizacion>) em.createNamedQuery("Organizacion.findAll").getResultList();
		List<DtOrganizacion> ldt = new ArrayList<>();
		for (Organizacion u : lI)
			ldt.add(u.getDt());
		return ldt;
	}

	public void borrarOrganizacion(String correo) {
		if (correo != null) {
			try {
				Organizacion i = buscarOrganizacion(correo);
				tx.begin();
				em.remove(i);
				em.flush();
				tx.commit();
			} catch (Exception e) {
				e.getMessage();
			}			
		}
	}
	
	public void modificarOrganizacion(Organizacion o) {
		try {
			if (o != null) {
				tx.begin();
				Query q = em.createQuery("update Organizacion set nombre=:n, correo=:d where idOrganizacion=:id").setHint("org.hibernate.cacheMode", "IGNORE");
				q.setParameter("n", o.getNombre());
				q.setParameter("d", o.getCorreo());
				q.setParameter("id", o.getIdOrganizacion());
				q.executeUpdate();
				em.flush();		
				tx.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
		
	public void altaEvaluador(Evaluador o) {
		if (o != null) {
			tx.begin();
			em.persist(o);
			em.flush();	
			tx.commit();
		}		
	}
	
	public Evaluador buscarEvaluador(String n) throws Exception {
		Evaluador r = null;		
		Query q = em.createQuery("select e from Evaluador e where nombre=:n");
		q.setParameter("n", n);
		r = (Evaluador) q.getSingleResult();
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
				tx.begin();
				em.remove(i);
				em.flush();
				tx.commit();
			} catch (Exception e) {
				e.getMessage();
			}			
		}
	}
	
	public void modificarEvaluador(Evaluador o) {
		try {
			if (o != null) {
				tx.begin();
				Query q = em.createQuery("update Evaluador set nombre=:n where idEvaluador=:id").setHint("org.hibernate.cacheMode", "IGNORE");
				q.setParameter("n", o.getNombre());
				q.setParameter("id", o.getIdEvaluador());
				q.executeUpdate();
				em.flush();	
				tx.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
		
	// INCIATIVAS

	public void altaIniciativa(Iniciativa i) throws Exception {
		try {
			if (i != null) {	
				if (buscarUsuario(i.getCreador()) != null) {	
					Long id;
					if (i.getIdIniciativa() != null)
						id = i.getIdIniciativa();
					else id = Long.valueOf(listarIniciativas().size())+50;
					i.setIdIniciativa(id);
					tx.begin();
					em.persist(i);
					em.flush();		
					tx.commit();
				} else throw new UsuarioException("Usuario no exite");
			} else throw new IniciativaException ("Error en los datos de la iniciativa");		
		} catch (Exception e) {
			try {								
				if(buscarOrganizacion(i.getCreador()) != null) {	
					Long id;
					if (i.getIdIniciativa() != null)
						id = i.getIdIniciativa();
					else id = Long.valueOf(listarIniciativas().size())+1;
					i.setIdIniciativa(id);
					tx.begin();
					em.persist(i);
					em.flush();	
					tx.commit();
				} else throw new UsuarioException("ORG no exite");							
			} catch (Exception e2) {			
				throw e2;
			}
		}	
	}
	
	public void bajaIniciativa(String nombre) {
		if (nombre != null) {
			try {
				Iniciativa i = buscarIniciativa(nombre);
				tx.begin();
				em.remove(i);
				em.flush();
				tx.commit();
			} catch (Exception e) {
				e.getMessage();
			}			
		}
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
	
	public void modificarIniciativa(DtIniciativa d) {
		try {
			if (d != null) {				
				String dt = d.getFecha();
				Date fc = new SimpleDateFormat("yyyy-MM-dd").parse(dt);	
				tx.begin();
				Query q = em.createQuery("update Iniciativa set nombre=:n, descripcion=:d, fecha=:f, estado=:e, creador=:cr where idIniciativa=:id").setHint("org.hibernate.cacheMode", "IGNORE");
				q.setParameter("n", d.getNombre());
				q.setParameter("d", d.getDescripcion());
				q.setParameter("f", fc);
				q.setParameter("e", Estado.valueOf(d.getEstado()));
				q.setParameter("cr", d.getCreador());
				q.setParameter("id", Long.parseLong(d.getId()));
				q.executeUpdate();				
				em.flush();	
				tx.commit();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void modificarIniciativa(String nombre) {
		Iniciativa i = null;
		tx.begin();
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
				tx.commit();
			}			
		} catch (Exception e) {
			e.getMessage();
		}
	}	
	
	public List<DtIniciativa> listarIniciativas() {
		List<Iniciativa> lI = (List<Iniciativa>) em.createNamedQuery("Iniciativa.findAll").getResultList();
		List<DtIniciativa> ldt = new ArrayList<>();
		for (Iniciativa u : lI)
			ldt.add(u.getDt());
		return ldt;
	}		

	public List<DtIniciativa> listarIniciativasFechas(Date f1, Date f2) {
		Query q = em.createQuery("from Iniciativa where fecha between :f1 and :f2");
		q.setParameter("f1", f1);
		q.setParameter("f2", f2);
		List<Iniciativa> lI = q.getResultList();
		List<DtIniciativa> ldt = new ArrayList<>();
		for (Iniciativa u : lI)
			ldt.add(u.getDt());
		return ldt;
	}
	
	public void adherirUsuarioIniciativa(String nombreIniciativa, String user) throws Exception {
		Iniciativa i = buscarIniciativa(nombreIniciativa);
		Ciudadano c = (Ciudadano) buscarUsuario(user);		
		
		// implica seguirla
		if (!usuarioAdherido(i, c)) {
			i.agregarUsuarioAdherido(c);
			tx.begin();
			em.persist(i);
			em.flush();		
			tx.commit();

		} else throw new IniciativaException ("Usuario ya está adherido");		
	
	}
	
	public void desadherirUsuarioIniciativa(String nombreIniciativa, String user) throws Exception {
		Iniciativa i = buscarIniciativa(nombreIniciativa);
		Ciudadano c = (Ciudadano) buscarUsuario(user);		
		
		// implica dejar de seguir
		if (usuarioAdherido(i, c)) {
			i.quitarUsuarioAdherido(c);
			tx.begin();
			em.persist(i);
			em.flush();		
			tx.commit();

		} else throw new IniciativaException ("Usuario no está adherido");		
	}
	
	public void seguirUsuarioIniciativa(String nombreIniciativa, String user) throws Exception {
		Iniciativa i = buscarIniciativa(nombreIniciativa);
		Ciudadano c = (Ciudadano) buscarUsuario(user);		
		
		// no implica adherirse
		if (!usuarioSiguiendoInicativa(i, c)) {
			i.agregarUsuarioSeguidor(c);	
			tx.begin();
			em.persist(i);
			em.flush();	
			tx.commit();
		} else throw new IniciativaException ("Usuario ya sigue la iniciativa");		
	
	}
	
	public void dejarSeguirUsuarioIniciativa(String nombreIniciativa, String user) throws Exception {
		Iniciativa i = buscarIniciativa(nombreIniciativa);
		Ciudadano c = (Ciudadano) buscarUsuario(user);		
				
		// no implica deshaderirse
		if (usuarioSiguiendoInicativa(i, c)) {
			i.quitarUsuarioSeguidor(c);
			tx.begin();
			em.persist(i);
			em.flush();	
			tx.commit();
		} else throw new IniciativaException ("Usuario no sigue la iniciativa");		
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
				l.add(c.getCedula());
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
				l.add(c.getCedula());
		}			
		return l;
	}
	
	public List<DtIniciativa> listarRelacionadosIniciativas() {
		List<Iniciativa> lI = (List<Iniciativa>) em.createNamedQuery("Iniciativa.findAll").getResultList();
		List<DtIniciativa> ldt = new ArrayList<>();
		for (Iniciativa u : lI)
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
		if (c != null) {
			List<Iniciativa> lI = (List<Iniciativa>) em.createNamedQuery("Iniciativa.findAll").getResultList();						
			for(Iniciativa k: lI) 
				if (c.getCorreo().equals(k.getCreador())) 
					ret.add(k.getNombre());				
		} else if (o != null) {
				List<Iniciativa> lI = (List<Iniciativa>) em.createNamedQuery("Iniciativa.findAll").getResultList();	
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
		
	public List<String> listarProcesoParticipandoUsuario(String correo) {
		List<ProcesoParticipativo> lI = (List<ProcesoParticipativo>) em.createNamedQuery("Proceso.findAll").getResultList();
		List<String> ret = new ArrayList<>();		
		Ciudadano c;
		try {
			c = (Ciudadano) buscarUsuario(correo);
			if (c!=null) 
				for(ProcesoParticipativo k: lI) {
					List<Ciudadano> lC = k.getParticipantes();					
					if (lC.contains(c))
						ret.add(k.getNombre());						
				}
			return ret;	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;	
	}
	
	private List<String> listarProcesoParticipandoUsuario(Ciudadano c) {
		List<ProcesoParticipativo> lI = (List<ProcesoParticipativo>) em.createNamedQuery("Proceso.findAll").getResultList();
		List<String> ret = new ArrayList<>();			
		try {
			if (c!=null) 
				for(ProcesoParticipativo k: lI) {
					List<Ciudadano> lC = k.getParticipantes();	
					if (lC.size() > 0)
						if (lC.contains(c))
							ret.add(k.getNombre());	
				}
			return ret;	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;			
	}
	
	public int contarIniciativas() {
		Query q = em.createQuery("select count(*) from Iniciativa");
		Long res = (Long) q.getSingleResult();
		return res.intValue();
	}
	
	public int contarAdhesiones() {
		Query q = em.createQuery("select a from Iniciativa a");
		List<Iniciativa> lI = (List<Iniciativa>) q.getResultList();
		int cant = 0;
		for (Iniciativa i: lI) {
			List<Ciudadano> lC = i.getAdheridos();
			if (lC != null) 
				for (int j=0; j <= lC.size(); j++)
					cant++;			
		}
		return cant;
	}
	
	public int contarParticipaciones() {
		Query q = em.createQuery("select a from ProcesoParticipativo a");
		List<ProcesoParticipativo> lI = (List<ProcesoParticipativo>) q.getResultList();
		int cant = 0;
		for (ProcesoParticipativo i: lI) {
			List<Ciudadano> lC = i.getParticipantes();
			if (lC != null) 
				for (int j=0; j <= lC.size(); j++)
					cant++;			
		}
		return cant;
	}
	
	
	// PROCESOS
	
	public void altaProceso(ProcesoParticipativo i) throws Exception {
		if (i != null) {	
			tx.begin();
			em.persist(i);
			em.flush();	
			tx.commit();
		}
	}
	
	public void modificarProceso(DtProceso d) {
		try {
			if (d != null) {		
				tx.begin();
				String dt = d.getFecha();
				Date fc = new SimpleDateFormat("yyyy-MM-dd").parse(dt);				
				Query q = em.createQuery("update ProcesoParticipativo set nombre=:n, descripcionAlcance=:d, fecha=:f, creador=:cr, fase=:fase where idProceso=:id").setHint("org.hibernate.cacheMode", "IGNORE");
				q.setParameter("n", d.getNombre());
				q.setParameter("d", d.getDescripcionAlcance());
				q.setParameter("f", fc);
				q.setParameter("fase", FaseProceso.valueOf(d.getFase()));
				q.setParameter("cr", d.getCreador());
				q.setParameter("id", Long.parseLong(d.getId()));
				q.executeUpdate();
				em.flush();
				tx.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void persistParticipante(ProcesoParticipativo p) {
		if (p != null) {
			tx.begin();
			em.persist(p);
			em.flush();
			tx.commit();
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

	public ProcesoParticipativo buscarProceso(String nombre) {
		ProcesoParticipativo ret = null;
		Query q = em.createQuery("select p from ProcesoParticipativo p where p.nombre=:n");
		q.setParameter("n", nombre);
		try {
			ret = (ProcesoParticipativo) q.getSingleResult();
			em.refresh(ret);
			return ret;
		} catch (Exception e) {
			ret = null;
		}
		return null;		
	}

	public List<DtProceso> listarProcesos() {
		List<ProcesoParticipativo> lI = (List<ProcesoParticipativo>) em.createNamedQuery("Proceso.findAll").getResultList();
		List<DtProceso> ldt = new ArrayList<>();
		for (ProcesoParticipativo u : lI)
			ldt.add(u.getDt());
		return ldt;
	}
	
	public int contarProcesos() {
		Query q = em.createQuery("select count(*) from ProcesoParticipativo");
		Long res = (Long) q.getSingleResult();  
		return res.intValue();
	}
	
	// CERTIFICADOS
	
	public void altaCertificadoInicial(Integer nivel) throws Exception {	// certificados de dos niveles para comentarios, seguidores, adhesiones y creadores		
		if (nivel == 1) {
			
			InputStream is = Persistencia.class.getResourceAsStream("/logos/creador1.png");
			byte[] logo = IOUtils.toByteArray(is);						
			TipoInsignia k = new TipoInsignia(logo, new Date());
			Certificado c = new Certificado("CertCreador1", k, nivel, "Creadores");
			tx.begin();
			em.persist(c);
			em.flush();		
			
			is = Persistencia.class.getResourceAsStream("/logos/adhesion1.png");		
			logo = IOUtils.toByteArray(is);	
			k = new TipoInsignia(logo, new Date());
			c = new Certificado("CertAdhesion1", k, nivel, "Adhesiones");
			em.persist(c);
			em.flush();	
			
			is = Persistencia.class.getResourceAsStream("/logos/seguidor1.png");		
			logo = IOUtils.toByteArray(is);	
			k = new TipoInsignia(logo, new Date());
			c = new Certificado("CertSeguidor1", k, nivel, "Seguidores");
			em.persist(c);
			em.flush();				
			tx.commit();	
		}		
	}
	
	public void altaCertificado(Certificado c) throws Exception {		
		if (c != null) {
			tx.begin();
			em.persist(c);
			em.flush();
			tx.commit();

		}		
	}
		
	public void altaTipoCertificado(String tipo) {
		if (tipo != null) {
			TipoCertificado l = TipoCertificado.getInstancia();
			l.agregarTipoCertificado(tipo);
			//tx.begin();
			em.merge(l);
			em.flush();
			tx.commit();
		}						
	}	
	
	public void agregarCertificadoUsuario(String user, Certificado c) throws Exception {
		if (user != null && c != null) {
			Ciudadano cu = (Ciudadano) buscarUsuario(user);
			if (!cu.getCertificados().contains(c)) {
				cu.agregarCertificado(c);
				tx.begin();
				em.persist(cu);
				em.flush();
				tx.commit();
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
		Certificado r = (Certificado) q.getSingleResult(); 		
		if (r != null)
			return r;
		else return null;
		
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
	
	public List<String> listarCertificadosUsuario(String correo) throws Exception {
		List<String> ret = new ArrayList<>();
		try {
			Ciudadano c = (Ciudadano) buscarUsuario(correo);
			List<Certificado> lC;
			if (c != null) {
				 lC = c.getCertificados();
				 if (lC != null)	
					 for(Certificado i: lC) 
						ret.add(i.getNombre());				 			
			}	
		} catch (Exception e) {		}
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
			byte [] c = r.getInsignia().getLogo();		
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

	public void bajaProceso(String nombre) {
		if (nombre != null) {
			try {
				ProcesoParticipativo i = buscarProceso(nombre);
				tx.begin();
				em.remove(i);
				em.flush();
				tx.commit();
			} catch (Exception e) {
				e.getMessage();
			}			
		}
	}

	public void updatePassword(String id, String rol, String pass) {
		Query q;
		tx.begin();
		if (rol.equals("Autoridad")) {	
			System.out.println(id+rol+pass);
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
		tx.commit();
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
	
	public void modificarIniciativa(Iniciativa d) {
		try {
			if (d != null) {
				tx.begin();			
				em.persist(d);	
				tx.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public List<DtProceso> listarProcesosFechas(Date f1, Date f2) throws Exception {
		Query q = em.createQuery("from ProcesoParticipativo where fecha between :f1 and :f2");
		q.setParameter("f1", f1);
		q.setParameter("f2", f2);
		List<ProcesoParticipativo> lI = q.getResultList();
		System.out.println(lI.toString());
		List<DtProceso> ldt = new ArrayList<>();
		for (ProcesoParticipativo u: lI) {
			DtProceso k = u.getDt();
			/*if (u.getIdInstrumento().size() > 0) {
				List<String> lD = nsql.getInstrumentoProceso(u.getIdInstrumento().get(0));				
				k.setInstrumento(lD.get(0));
				lD.remove(lD.get(0));
				String[] array = lD.toArray(new String[0]);
				k.setContenidoInstrumento(array);
			}*/
			ldt.add(k);
		}
		return ldt;
	}	

	public void seguirUsuarioProceso(String proceso, String user) throws Exception {
		ProcesoParticipativo i = buscarProceso(proceso);
		Ciudadano c = (Ciudadano) buscarUsuario(user);		
		if (!usuarioSiguiendoProceso(i, c)) {
			i.agregarSeguidor(c);
			tx.begin();
			em.persist(i);
			em.flush();
			tx.commit();
		} else throw new ProcesoException("Usuario ya sigue el proceso");	
	}

	public void dejarSeguirUsuarioProceso(String proceso, String user) throws Exception {
		ProcesoParticipativo i = buscarProceso(proceso);
		Ciudadano c = (Ciudadano) buscarUsuario(user);				
		if (usuarioSiguiendoProceso(i, c)) {
			i.quitarSeguidor(c);
			tx.begin();
			em.persist(i);
			em.flush();
			tx.commit();
		} else throw new ProcesoException("Usuario no sigue al proceso");	
	}
	
	public boolean usuarioSiguiendoProceso(ProcesoParticipativo i, Ciudadano cu) {	// true si esta, false si no	
		List<Ciudadano> c = i.getSeguidores(); 				
		if (c.contains(cu)) 
			return true;		
		else 	
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

	public void modificarProceso(ProcesoParticipativo d) {
		try {
			if (d != null) { 				
				tx.begin();			
				em.persist(d);	
				tx.commit();
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}

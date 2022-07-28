package eParticipation.backend.backoffice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import eParticipation.backend.dto.DtAdministrador;
import eParticipation.backend.dto.DtAutoridad;
import eParticipation.backend.dto.DtCiudadano;
import eParticipation.backend.dto.DtFuncionario;
import eParticipation.backend.dto.DtOrganismo;
import eParticipation.backend.service.UsuarioService;
import lombok.Data;

@Named("cu")
@SessionScoped
@Data
public class BackUsuario implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB private UsuarioService u;
	private String rol;
	private String cedula;
	private String nombre;
	private String correo;
	private String password;
	private String departamento;
	private List<Object> lo;	
	private List<DtOrganismo> lorg;
	private List<String> tipos;
	private boolean renderList = false;
	private boolean renderSearch = false;
	private Object user;
	private String organismo = null;
	private String cargo;
	private String delU1;
	private String delU2;
	private String delU3;
	private String delO;
	private boolean renderListaOrg = false;
	private String tipoU;	
	private List<SortMeta> sortBy;
			
	public BackUsuario() { 	}
	
	@PostConstruct
	private void init() {
		tipos = new ArrayList<>();
		tipos.add("Funcionario");
		tipos.add("Ciudadano");
		tipos.add("Administrador");
		tipos.add("Autoridad");
	}
	
	public void crearUsuario() {	
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if (rol.equals("Ciudadano")) {
				DtCiudadano dtc = new DtCiudadano(cedula, nombre, correo, "2022-05-05", "", "", rol);
				u.altaUsuario(dtc, null);
			} else if (rol.equals("Administrador")) {
				DtAdministrador dtc = new DtAdministrador(cedula, password, correo, nombre, rol);
				u.altaUsuario(dtc, null);
			} else if (rol.equals("Autoridad")) {
				DtAutoridad dtc = new DtAutoridad(cedula, password, correo, nombre, rol);
				u.altaUsuario(dtc, null);	
			} else if (rol.equals("Funcionario")) {
				DtFuncionario dtc = new DtFuncionario(cedula, nombre, correo, "2022-05-05", "", "", organismo, cargo, rol);			
				u.altaUsuario(dtc, organismo);	
			}
			ctx.addMessage(null, new FacesMessage("usuario dado de alta"));			
			clean();
		} catch (Exception e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));	
		}
	}
	
	public void buscarUsuario() {
		if (getCorreo() != null) {
			try {
				user = u.buscarUsuario(correo);
				if (user instanceof DtCiudadano) 				
					tipoU = ((DtCiudadano) user).getRol();
				else if (user instanceof DtAutoridad) 				
					tipoU = ((DtAutoridad) user).getRol();
				else if (user instanceof DtAdministrador) 					
					tipoU = ((DtAdministrador) user).getRol();
				 else if (user instanceof DtFuncionario) {						
					tipoU = ((DtFuncionario) user).getRol();
					organismo = ((DtFuncionario) user).getOrganismo();
					cargo = ((DtFuncionario) user).getCargo();
				 }
				toggleRenderSearch();
			} catch (Exception c) {
				FacesContext ctx = FacesContext.getCurrentInstance();
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "usuario no encontrado", ""));			
			}
		}		
	}
	
	public void borrarUser() {	
		FacesContext ctx = FacesContext.getCurrentInstance();	
		try {
			if (delU3.equals("Ciudadano"))
				u.bajaCiudadano(delU2);
			else if (delU3.equals("Funcionario"))
				u.bajaFuncionario(delU2);
			else if (delU3.equals("Administrador"))
				u.bajaAdmin(delU1);
			else if (delU3.equals("Autoridad"))
				u.bajaAutoridad(delU1);
			lo = u.listarUsuarios();
			ctx.addMessage(null, new FacesMessage("usuario borrado"));			
			
		} catch (Exception c) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "imposible borrar", ""));			
		}	
	}
	
	public void borrarUserSearch() {
		FacesContext ctx = FacesContext.getCurrentInstance();	
		try {
			if (user instanceof DtCiudadano)
				u.bajaCiudadano(((DtCiudadano)user).getCorreo());
			else if (user instanceof DtFuncionario)
				u.bajaFuncionario(((DtFuncionario)user).getCorreo());
			else if (user instanceof DtAutoridad)
				u.bajaAutoridad(((DtAutoridad)user).getCedula());
			else if (user instanceof DtAdministrador)
				u.bajaAdmin(((DtAdministrador)user).getCedula());
			ctx.addMessage(null, new FacesMessage("usuario borrado"));			
			lo = u.listarUsuarios();
			toggleRenderSearch();
			setUser(null);
			setCorreo(null);
		} catch (Exception c) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "imposible borrar", ""));			
		}		
	}
	
	public void listarUsuario() {	
		FacesContext ctx = FacesContext.getCurrentInstance();	
		try {
			lo = u.listarUsuarios();			
			if (lo != null) {
				sortBy = new ArrayList<>();				
				sortBy.add(SortMeta.builder()
	                .field("rol")
	                .order(SortOrder.ASCENDING)
	                .priority(1)
	                .build()); 	
				toggleRenderList();	
			}										
		} catch (Exception e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
		}		
	}
	
	public void modificarUsuario() {	
		FacesContext ctx = FacesContext.getCurrentInstance();			
		try {							
			u.modificarUsuario(user, tipoU, organismo, cargo, password);
			ctx.addMessage(null, new FacesMessage("usuario modificado"));			
			lo = u.listarUsuarios();
			setCorreo(null);
			setUser(null);
			setTipoU(null);
			toggleRenderSearch();
		} catch (Exception e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));			
		}
	}
	
	public void crearOrganismo() {
		FacesContext ctx = FacesContext.getCurrentInstance();	
		try {
			u.altaOrganismo(nombre, departamento);
			ctx.addMessage(null, new FacesMessage("organismo dado de alta"));	
			lorg = u.listaOrganismo();
			setNombre(null);
			setDepartamento(null);
		} catch (Exception e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
		}
	}
	
	public void listarOrganismo() {
		lorg = u.listaOrganismo();
		if (lorg != null)
			toggleRenderListOrg();
		else setRenderListaOrg(false);		

	}
	
	private void toggleRenderListOrg() {
		setRenderListaOrg(!renderListaOrg);		
	}

	public void borrarOrganismo() {
		FacesContext ctx = FacesContext.getCurrentInstance();	
		try {
			u.quitarOrganismo(delO);
			lorg = u.listaOrganismo();
			ctx.addMessage(null, new FacesMessage("organismo dado de baja"));			
		} catch (Exception e) {			
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Organismo con dependencias, no puede borrar", ""));			
		}
	}
	
	public void toggleRenderList() {
		setRenderList(!renderList);
	}
	
	public void toggleRenderSearch() {
		setRenderSearch(!renderSearch);
	}
	
	public String redirectIndex() {
		clean();		
		return "/index?faces-redirect=true";
	}
	
	private void clean() {
		setRol(null);
		setCedula(null);
		setNombre(null);
		setCorreo(null);
		setPassword(null);
		setRenderList(false);	
		setRenderSearch(false);		
		setRenderListaOrg(false);
		setOrganismo(null);
		setCargo(null);
		setDepartamento(null);
		setUser(null);
		setTipoU(null);
		
	}	
}



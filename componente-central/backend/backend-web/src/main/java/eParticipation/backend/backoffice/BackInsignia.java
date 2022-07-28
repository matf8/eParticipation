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

import eParticipation.backend.dto.DtCertificado;
import eParticipation.backend.service.InsigniaService;
import lombok.Data;

@Named("ci")
@SessionScoped
@Data
public class BackInsignia implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private InsigniaService u;	
	
	private List<String> tipos;
	private List<Integer> momentos = new ArrayList<>();
	private List<DtCertificado> certificados;
	
	private String nombre;
	private Integer nivel;
	private String logo;
	private String logoURL;
	private String tipo;
	private String tipoURL;
	private String nombreTipo;
	private int momento;
	private int toolInt;
	private boolean renderCrear = false;	
	private boolean renderForm = false;
	private boolean renderCrearTipo = false;
	private DtCertificado certificado;
	private String cert;
	
	@PostConstruct
	public void getImagen() {	
		try {
			tipos = u.getTipos();
			certificados = u.listarDtCertificados();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void refreshList() {
		getImagen();
	}
	
	public void borrarInsignia(String nombre) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		if (nombre != null) {
			try {
				u.borrarCertificado(nombre);
				ctx.addMessage(null, new FacesMessage("Certificado eliminado"));	
			} catch (Exception e) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));				
			}
		}
	}
	
	public void setMomentos(int nivel) {
		int p = 10;				
		if (nivel == 1) {
			momento = (p);
			momentos.removeAll(momentos);
			momentos.add(p);							
		} else if (nivel > 1) {			
			momento = (p*nivel);
			momentos.clear();
			momentos.add(p*nivel);				
		}			
	}
		
	public void crearTipo() {
		List<String> c = u.getTipos();
		if(nombreTipo != null)
			if (!c.contains(nombreTipo))
				u.addTipo(nombreTipo);
		tipos = u.getTipos();
		tipo = tipoURL = nombreTipo;
		setNombreTipo(null);
	}
	
	public void altaCertificado() {			
		FacesContext ctx = FacesContext.getCurrentInstance();
		if (nombre != null && nivel != null) {	
			if (tipo == null && tipoURL != null) {
				tipo = tipoURL;
			}
			u.altaCertificado(nombre, nivel, tipoURL, momento);			
			ctx.addMessage(null, new FacesMessage("Certificado dado de alta para los ciudadanos que lleguen a " + momento + " de la categoría " + tipoURL));			
			clean();
		}
		
	}
	
	public String redirectIndex() {
		clean();
		return "/index?faces-redirect=true"; 
	}
	
	public void formInsignia() {
		if (nivel != null) {
			setMomentos(nivel);
			setToolInt(nivel*10);
			toggleRenderForm();
		} else {
			FacesContext ctx = FacesContext.getCurrentInstance();
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ingrese datos", ""));
		}
	}
	
	private void clean() {
		setNombre(null);
		setLogo(null);
		setTipo(null);
		setNivel(null);
		setTipoURL(null);
		setRenderCrearTipo(false);
		setRenderCrear(false);
		setRenderForm(false);
		//borrarInts();
	}
	
	private void borrarInts() {
		if (momentos != null && momentos.size() > 0)
		for (Integer i: momentos)
			momentos.remove(i);
	}
		
	public void toggleRenderCrear() {
		setRenderCrear(!renderCrear);
		setRenderCrearTipo(false);
	}
	
	public void toggleRenderCrearTipo() {
		setRenderCrearTipo(!renderCrearTipo);
	}
	
	public void toggleRenderForm() {	
		setRenderForm(!renderForm);
	}
	
}

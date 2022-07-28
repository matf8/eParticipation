package eParticipation.backend.backoffice;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import eParticipation.backend.dto.DtEvaluador;
import eParticipation.backend.dto.DtOrganizacion;
import eParticipation.backend.service.PerifericoService;
import lombok.Data;

@Named("cgp")
@SessionScoped
@Data
public class BackGestionNP implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB private PerifericoService u;		
	private List<DtEvaluador> evaluadores;
	private List<DtOrganizacion> organizaciones;
	private List<String> nodoUrisEvaluador;
	private List<String> nodoUrisOrganizacion;
	private String ev = "";	
	private String org = "";
	private int tab;
	private int tab2; 
	private int tab3; 
	private boolean renderAddEv = false;
	private boolean renderSearchEv = false;
	private boolean renderAddO = false;
	private boolean renderSearchO = false;
	private boolean renderAddNE = false;
	private boolean renderAddNO = false;
	private DtEvaluador evaluador;
	private DtOrganizacion organizacion;
	private String NEL = "";
	private String NADD = "";
	private String NOEL = "";
	private String NOADD = "";
	private String nodoSeleccionado = "";
	
	@PostConstruct
	public void getListas() {
		evaluadores = u.getEvaluadores();
		organizaciones = u.getOrganizaciones();	
		nodoUrisEvaluador = u.getUrisEvaluador();	
		nodoUrisOrganizacion = u.getUrisOrganizacion();	
		evaluador = new DtEvaluador();
		organizacion = new DtOrganizacion();
	}
		
	public void agregarEvaluador() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if (ev != null) {
				u.altaEvaluador(ev, nodoSeleccionado);
				getListas();				
				ctx.addMessage(null, new FacesMessage("alta evaluador de nombre: " + ev));		
				toggleRenderAltaEv();
			} else ctx.addMessage(null, new FacesMessage("nombre vacio"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
			
	public void buscarEvaluador() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		if (evaluador.getNombre() != null) {
			try {
				evaluador = u.buscarEvaluador(evaluador.getNombre());
			} catch (Exception e) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			}
		} else ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "nombre vacio", ""));			
	}

	public void borrarEvaluador() {
		setTab(0);
		FacesContext ctx = FacesContext.getCurrentInstance();
		if (ev != null) {
			try {
				if (nodoSeleccionado.equals("") || nodoSeleccionado == null)
					nodoSeleccionado = nodoUrisOrganizacion.get(0);
				u.borrarEvaluador(ev, nodoSeleccionado);
				getListas();					
				ctx.addMessage(null, new FacesMessage("evaluador " + ev + "eliminado"));
				} catch (Exception e) {
					 ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));			
				}		
			
		} else ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "nombre vacio", ""));			
	}
	
	public void agregarOrganizacion() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		if (organizacion != null) {
			u.altaOrganizacion(organizacion.getNombreCompleto(), organizacion.getCorreo(), nodoSeleccionado);
			getListas();
			setTab(1);
			ctx.addMessage(null, new FacesMessage("alta organizacion de nombre: " + organizacion.getNombreCompleto()));		
			toggleRenderAltaO();
		}	else ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "nombre vacio", ""));			
	}
	
	public void borrarOrganizacion() {
		FacesContext ctx = FacesContext.getCurrentInstance();	
		if (org != null) {
			if (nodoSeleccionado.equals("") || nodoSeleccionado == null)
				nodoSeleccionado = nodoUrisOrganizacion.get(0);
			u.borrarOrganizacion(org, nodoSeleccionado);
			setTab(1);
			getListas();
			ctx.addMessage(null, new FacesMessage("organizacion " + org + "eliminado"));	
		} else ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "nombre vacio", ""));			
	}	
	
	public void buscarOrganizacion() {
		if (organizacion.getCorreo() != null) {
			try {
				organizacion = u.buscarOrganizacion(organizacion.getCorreo());	
				setTab(1);
			} catch (Exception e) {
				FacesContext ctx = FacesContext.getCurrentInstance();
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			}
		}
	}	
	
	public void refreshList() {
		organizaciones = u.getOrganizaciones();	
		setTab(1);
	}
	
	public void getUrisEvaluador() {
		this.nodoUrisEvaluador = u.getUrisEvaluador();
	}
	
	public void getUrisOrganizacion() {
		this.nodoUrisOrganizacion = u.getUrisOrganizacion();
	}
	
	public void agregarNodoEvaluador() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		if (!NADD.equals("")) {
			try {
				u.addUriEvaluador(NADD);
				setTab(0);
				setTab2(1);	
				getUrisEvaluador();
				toggleRenderAddNE();
				ctx.addMessage(null, new FacesMessage("nodo evaluador agregado"));
			} catch (Exception e) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			}
		} else ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "dato vacio", ""));
	}
	
	public void borrarNodoEvaluador() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		if (!NEL.equals("")) {
			try {
				u.bajaUri(NEL,"e");
				setTab(0);
				setTab2(1);
				getUrisEvaluador();
				ctx.addMessage(null, new FacesMessage("nodo evaluador borrado"));
			} catch (Exception e) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			}
		} else ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "dato vacio", ""));
	}
	
	public void agregarNodoOrganizacion() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		if (!NOADD.equals("")) {
			try {
				u.addUriOrganizacion(NOADD);
				setTab(1);
				setTab3(1);	
				getUrisOrganizacion();
				setNOADD("");
				ctx.addMessage(null, new FacesMessage("nodo organizacion agregado"));
			} catch (Exception e) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			}
		} else ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "dato vacio", ""));
	}
	
	public void borrarNodoOrganizacion() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		if (!NOEL.equals("")) {
			try {
				u.bajaUri(NOEL,"o");
				setTab(0);
				setTab2(1);
				getUrisEvaluador();
				ctx.addMessage(null, new FacesMessage("nodo organizacion borrado"));
			} catch (Exception e) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			}
		} else ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "dato vacio", ""));
	}
		
	public void toggleRenderAddNE() {
		setNADD(null);
		setTab(0);
		setTab2(1);
		setRenderAddNE(!this.renderAddNE);	
	}
	
	public void toggleRenderAddNO() {
		setNOADD(null);
		setTab(1);
		setTab3(1);
		setRenderAddNO(!this.renderAddNO);	
	}	
	
	public void toggleRenderAltaEv() {
		setEv(null);	
		setTab(0);
		setTab2(0);
		setRenderAddEv(!this.renderAddEv);	
	}
	
	public void toggleRenderSearchE() {
		if (renderSearchEv) {
			setRenderSearchEv(!this.renderSearchEv);
			if (evaluador != null ) {
				evaluador.setNombre(null);
				evaluador.setId(null);
			}
		} else {
			setTab(0);
			setRenderSearchEv(!this.renderSearchEv);
		}
	}
	
	public void toggleRenderAltaO() {		
		organizacion.setNombreCompleto(null);
		organizacion.setCorreo(null);
		setRenderAddO(!this.renderAddO);	
		setTab(1);
		setTab3(0);
	}
	
	public void toggleRenderSearchO() {
		if (renderSearchO) {
			setRenderSearchO(!this.renderSearchO);
			if (evaluador != null ) {
				organizacion.setNombreCompleto(null);
				organizacion.setCedula(null);
				organizacion.setCorreo(null);
				organizacion.setIniciativaCreada(null);
			}
		} else {
			setTab(1);
			setRenderSearchO(!this.renderSearchO);		
		}
	}
	
	public String redirectGestorNodosPEv() {
		setTab(0);
		return "/gestionNodos?faces-redirect=true";
	}
	
	public String redirectGestorNodosPOrg() {
		setTab(1);
		return "/gestionNodos?faces-redirect=true";
	}
	
	public String redirectIndex() {
		clean();		
		return "/index?faces-redirect=true";
	}
	
	private void clean() {
		organizacion.setNombreCompleto(null);
		organizacion.setCorreo(null);
		setEvaluador(null);
		setEv(null);
		setOrg(null);
		setNEL("");
		setNADD("");
		setNOEL("");
		setNOADD("");		
		setRenderAddEv(false);
		setRenderSearchEv(false);
		setRenderAddO(false);
		setRenderSearchO(false);	
		setRenderAddNE(false);
		setRenderAddNO(false);
		setNodoSeleccionado("");
	}	
}

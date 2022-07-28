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

import eParticipation.backend.dto.DtIniciativa;
import eParticipation.backend.service.IniciativaService;
import eParticipation.backend.service.PerifericoService;
import lombok.Data;

@Named("cpr")
@SessionScoped
@Data
public class BackProcesar implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB private IniciativaService u;	
	@EJB private PerifericoService p;		
	private List<DtIniciativa> iniciativas;
	private List<String> urisEvaluador;
	private String iniciativaEv;
	private DtIniciativa di;
	private String nodo;
	private List<SortMeta> sortBy;
	
		
	@PostConstruct
	public void getLista() {	
		try {
			iniciativas = u.listarIniciativas();
			urisEvaluador = p.getUrisEvaluador();	
			if (iniciativas != null) {
				sortBy = new ArrayList<>();				
				sortBy.add(SortMeta.builder()
	                .field("nombre")
	                .order(SortOrder.ASCENDING)
	                .priority(1)
	                .build()); 	
			}							
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void refreshList() {
		getLista();
	}
	
	public void getIniciativa(String nombre) {
		try {
			this.di = u.buscarIniciativa(iniciativaEv);			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public String evaluarIniciativa() {	
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if (nodo != null) {
				String i = u.evaluarIniciativa(di, nodo);						
				ctx.addMessage(null, new FacesMessage(i));	
				ctx.getExternalContext().getFlash().setKeepMessages(true);
				clean();
				refreshList();	
			} else {
				String i = u.evaluarIniciativa(di, urisEvaluador.get(0));	
				ctx.addMessage(null, new FacesMessage(i));	
				ctx.getExternalContext().getFlash().setKeepMessages(true);
			}
		} catch (Exception e) {
			ctx.addMessage(null, new FacesMessage(e.getMessage()));
			ctx.getExternalContext().getFlash().setKeepMessages(true);
		}
		return "/procesar?faces-redirect=true";	
	}	
		
	public String redirectIndex() {
		clean();
		return "/index?faces-redirect=true"; 
	}
		
	public String procesarIniciativa() {
		getIniciativa(this.iniciativaEv);
		return "/gestionIniciativa?faces-redirect=true";
	}
	
	private void clean() {
		setIniciativaEv(null);	
		setDi(null);	
	}
	
}

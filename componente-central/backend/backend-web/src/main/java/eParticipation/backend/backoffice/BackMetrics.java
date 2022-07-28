package eParticipation.backend.backoffice;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.*;

import eParticipation.backend.dto.DtEstadisticas;
import eParticipation.backend.service.IniciativaService;
import eParticipation.backend.service.ProcesoService;
import eParticipation.backend.service.UsuarioService;
import lombok.Data;

@Named("metrics")
@RequestScoped
@Data
public class BackMetrics implements Serializable { 
	private static final long serialVersionUID = 1L;
	
	@EJB private UsuarioService cu;		
	@EJB private ProcesoService pu;	
	@EJB private IniciativaService iu;

    private BarChartModel barModelEspaciosParticipativos; 
    private PieChartModel pieModelUsuarios;
    private BarChartModel lineModelInteracciones;

    private DtEstadisticas est = new DtEstadisticas();  
    
    @PostConstruct
    public void init() {    
        createPieModels();      
        createBarModelsEP();
        createBarModelsI();      
    }    

    public void itemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
                "Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    } 
    
    private void createPieModels() {      
        createPieModel2();
    } 
    
    private void createBarModelsEP() {
        createBarModelEP();        
    }
    
    private void createBarModelsI() {
        createBarModelI();        
    }
    
	private void createPieModel2() {
	    	
    	int cantCiudadanos = cu.contarCiudadanos();
    	int cantFuncionarios = cu.contarFuncionarios();
    	int cantAdministradores = cu.contarAdministradores();
    	int cantAutoridades = cu.contarAutoridades();
    	est.setCantCiudadanos(Integer.valueOf(cantCiudadanos));
    	est.setCantFuncionarios(Integer.valueOf(cantFuncionarios));
    	est.setCantAdministradores(Integer.valueOf(cantAdministradores));
    	est.setCantAutoridades(Integer.valueOf(cantAutoridades));
            	
    	pieModelUsuarios = new PieChartModel();       
 
    	pieModelUsuarios.set("Ciudadanos", cantCiudadanos);
    	pieModelUsuarios.set("Funcionarios", cantFuncionarios);
    	pieModelUsuarios.set("Autoridades", cantAutoridades);
    	pieModelUsuarios.set("Administradores", cantAdministradores);
 
    	pieModelUsuarios.setTitle("Usuarios del sistema");
    	pieModelUsuarios.setLegendPosition("ne");
    	pieModelUsuarios.setFill(false);
    	pieModelUsuarios.setShowDataLabels(true);
    	pieModelUsuarios.setDiameter(250);
    	pieModelUsuarios.setShadow(false);    
      
	}	     
    
   private BarChartModel initBarModelEP() {
        BarChartModel model = new BarChartModel();
        int cantIniciativas = iu.contarIniciativas();
        est.setCantIniciativas(cantIniciativas);
        ChartSeries iniciativas = new ChartSeries();
        iniciativas.setLabel("Iniciativas");
        iniciativas.set("2022", cantIniciativas);    
        
        int cantProcesos = pu.contarProcesos();
        est.setCantProcesos(cantProcesos);
        ChartSeries procesos = new ChartSeries();
        procesos.setLabel("Procesos participativos");
        procesos.set("2022", cantProcesos);     
         
        model.addSeries(iniciativas);
        model.addSeries(procesos);
 
        return model;
    }	
	  
	private void createBarModelEP() {
		barModelEspaciosParticipativos = initBarModelEP();		 
		barModelEspaciosParticipativos.setTitle("Espacios participativos");
		barModelEspaciosParticipativos.setLegendPosition("ne");		 
        Axis xAxis = barModelEspaciosParticipativos.getAxis(AxisType.X);
        xAxis.setLabel("Años"); 
        Axis yAxis = barModelEspaciosParticipativos.getAxis(AxisType.Y);
        yAxis.setLabel("Cantidad");
		yAxis.setMin((int) 0);
		yAxis.setMax((int) 25);
	}	 
	
	 private BarChartModel initBarModelI() {
        BarChartModel model = new BarChartModel();
        int cantSeguidores = iu.contarSeguidores();
        est.setCantSeguidores(cantSeguidores);
        ChartSeries seguidores = new ChartSeries();
        seguidores.setLabel("Seguidores");
        seguidores.set("2022", cantSeguidores);    
        
        int cantComentarios = iu.contarComentarios();
        est.setCantComentarios(cantComentarios);
        ChartSeries comentarios = new ChartSeries();
        comentarios.setLabel("Comentarios");
        comentarios.set("2022", cantComentarios);   
        
        int cantAdhesiones = iu.contarAdhesiones();
        est.setCantAdhesiones(cantAdhesiones);
        ChartSeries adhesiones = new ChartSeries();
        adhesiones.setLabel("Adhesiones");
        adhesiones.set("2022", cantAdhesiones);   
                 
        model.addSeries(seguidores);
        model.addSeries(comentarios);
        model.addSeries(adhesiones);
 
        return model;
	}
	
	private void createBarModelI() {
		lineModelInteracciones = initBarModelI();		 
		lineModelInteracciones.setTitle("Interacciones - eParticipation");
		lineModelInteracciones.setLegendPosition("ne");		 
        Axis xAxis = lineModelInteracciones.getAxis(AxisType.X);
        xAxis.setLabel("Años"); 
        Axis yAxis = lineModelInteracciones.getAxis(AxisType.Y);
        yAxis.setLabel("Cantidad");
		yAxis.setMin((int) 0);
		yAxis.setMax((int) 25);
	}

}

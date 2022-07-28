package eParticipation.periferico.service;

import eParticipation.periferico.model.Iniciativa;

import java.util.List;

import eParticipation.periferico.model.DtIniciativaEv;
import eParticipation.periferico.model.Evaluador;

public interface EvaluadorService {
	
	public DtIniciativaEv evaluarIniciativa(Iniciativa i);
	public void addEvaluador(Evaluador e);	
	public void removeEvaluador(Evaluador e);		
	public Evaluador getEvaluador(Long id);	
	public List<Evaluador> getEvaluadores();    	
	public void cargarEvaluadores();
	
	public String ping();
	
	

}
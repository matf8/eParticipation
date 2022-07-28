package eParticipation.backend.service;

import javax.ejb.Local;
import javax.json.JsonObject;

import org.json.simple.JSONObject;

import eParticipation.backend.dto.DtAdministrador;
import eParticipation.backend.dto.DtAutoridad;
import eParticipation.backend.dto.DtCiudadano;
import eParticipation.backend.dto.DtFuncionario;
import eParticipation.backend.dto.DtIniciativa;
import eParticipation.backend.dto.DtProceso;
import eParticipation.backend.model.Evaluador;
import eParticipation.backend.model.Organizacion;

@Local
public interface ConverterService {	
	public JsonObject jsonAutoridad(DtAutoridad autoridad);	
	public JsonObject jsonAdmin(DtAdministrador admin);
	public JSONObject jsonCiudadano(DtCiudadano ciudadano);	
	public JSONObject jsonCiudadanoConToken(DtCiudadano ciudadano, String token);	
	public JSONObject jsonFuncionario(DtFuncionario funcionario);		
	public JSONObject jsonIniciativa(DtIniciativa i);
	public JSONObject jsonProceso(DtProceso i);
	public JSONObject jsonEvaluador(Evaluador e);
	public JSONObject jsonOrganizacion(Organizacion o);
}


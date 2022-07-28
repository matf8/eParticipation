package eParticipation.backend.controller;

import javax.ejb.Stateless;
import javax.json.Json;
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

@Stateless
@SuppressWarnings("unchecked")
public class ConverterController {	
		
	public JSONObject jsonCiudadano(DtCiudadano ciudadano) {
		JSONObject jsonCiudadano = new JSONObject();
		jsonCiudadano.put("correo", ciudadano.getCorreo());
		jsonCiudadano.put("nombre", ciudadano.getNombreCompleto());
		jsonCiudadano.put("domicilio", ciudadano.getDomicilio());
		jsonCiudadano.put("nacionalidad", ciudadano.getNacionalidad());
		jsonCiudadano.put("fechaNac", ciudadano.getFNac().toString());
		jsonCiudadano.put("rol", ciudadano.getRol().toUpperCase());
		jsonCiudadano.put("iniciativasCreadas", ciudadano.getIniCreadas());
		jsonCiudadano.put("iniciativasSeguidas", ciudadano.getIniSeguidas());
		jsonCiudadano.put("iniciativasAdheridas", ciudadano.getIniAdheridas());			
		jsonCiudadano.put("certificados", ciudadano.getCertificados());	
		return jsonCiudadano;	
	}
	
	
	public JSONObject jsonFuncionario(DtFuncionario funcionario) {
		JSONObject jsonFuncionario = new JSONObject();
		jsonFuncionario.put("correo", funcionario.getCorreo());
		jsonFuncionario.put("nombre", funcionario.getNombreCompleto());
		jsonFuncionario.put("domicilio", funcionario.getDomicilio());
		jsonFuncionario.put("nacionalidad", funcionario.getNacionalidad());
		jsonFuncionario.put("fechaNac", funcionario.getFNac().toString());
		jsonFuncionario.put("rol", funcionario.getRol().toUpperCase());
		jsonFuncionario.put("organismo", funcionario.getOrganismo());
		jsonFuncionario.put("cargo", funcionario.getCargo());
		jsonFuncionario.put("procesoCreados", funcionario.getProcesoCreado());
		return jsonFuncionario;			
		
    }
	
	// backoffice con error de json simple XD back to javax.json 
	public JsonObject jsonAutoridad(DtAutoridad autoridad) {
		JsonObject jsonAutoridad = Json.createObjectBuilder()
				.add("correo", autoridad.getCorreo())
				.add("nombre", autoridad.getNombreCompleto())
				.add("cedula", autoridad.getCedula())
				.add("password", autoridad.getPassword())
				.add("rol", autoridad.getRol())
				.add("token", autoridad.getToken()).build();      
		return jsonAutoridad;		
      
    }	
	
	public JsonObject jsonAdmin(DtAdministrador admin) {
		JsonObject jsonAdmin = Json.createObjectBuilder()
				.add("correo", admin.getCorreo())
				.add("nombre", admin.getNombreCompleto())
				.add("cedula", admin.getCedula())
				.add("password", admin.getPassword())
				.add("rol", admin.getRol())
				.add("token", admin.getToken()).build();
		return jsonAdmin;
	}

	public JSONObject jsonIniciativa(DtIniciativa i) {
		JSONObject jsonIni = new JSONObject();
		jsonIni.put("nombre", i.getNombre());
		jsonIni.put("fecha", i.getFecha().toString());
		jsonIni.put("estado", i.getEstado());	
		jsonIni.put("descripcion", i.getDescripcion());	
		jsonIni.put("adheridos", i.getAdheridos());
		jsonIni.put("seguidores", i.getSeguidores());
		jsonIni.put("comentarios", i.getComentarios());
		jsonIni.put("recurso", i.getRecurso());
		return jsonIni;	
	}

	public JSONObject jsonProceso(DtProceso i) {
		JSONObject jsonProceso = new JSONObject();
		jsonProceso.put("nombre", i.getNombre());
		jsonProceso.put("fecha", i.getFecha().toString());
		jsonProceso.put("alcance", i.getDescripcionAlcance());	
		jsonProceso.put("fase", i.getFase());
		jsonProceso.put("instrumento", i.getInstrumento());
		jsonProceso.put("contenidoInstrumento", i.getContenidoInstrumento());
		jsonProceso.put("seguidores", i.getSeguidores());
		jsonProceso.put("comentarios", i.getComentarios());
		return jsonProceso;	
	}

	public JSONObject jsonEvaluador(Evaluador e) {
		JSONObject jsonEvaluador = new JSONObject();
		jsonEvaluador.put("id", e.getIdEvaluador());
		jsonEvaluador.put("nombre", e.getNombre());
		return jsonEvaluador;
	}


	public JSONObject jsonOrganizacion(Organizacion o) {
		JSONObject jsonOrganizacion = new JSONObject();
		jsonOrganizacion.put("nombre", o.getCorreo());
		return jsonOrganizacion;
	}

}
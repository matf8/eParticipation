package eParticipation.backend.data;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import javax.ejb.Startup;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

import eParticipation.backend.dto.DtParticipa;
import eParticipation.backend.excepciones.InstrumentoException;
import eParticipation.backend.excepciones.MongoException;
import eParticipation.backend.model.Ciudadano;
import eParticipation.backend.service.PersistenciaLocal;
import eParticipation.backend.service.PersistenciaNSQLocal;

@Singleton
@Startup
@Lock(LockType.READ)
public class PersistenciaNSQL implements PersistenciaNSQLocal {	

	@EJB private PersistenciaLocal bd;		
	private static String uri = "mongodb+srv://m4thu8:roro10@cluster-eparticipation.inlmu.mongodb.net/eParticipation?retryWrites=true&w=majority";
	private static MongoClient mc = null;
	private static MongoDatabase db = null;

	@PostConstruct
	public void getConnection() {
		if (mc == null) 
			mc = MongoClients.create(uri);			
		if (db == null)
			db = mc.getDatabase("eParticipation");			
	}
		
	public String subirRecurso(String r) throws Exception {			
		try {
			MongoCollection<Document> c = db.getCollection("Recursos");			
			Document d = new Document().append("recurso", r);
			InsertOneResult ins = c.insertOne(d);
			String id = ins.getInsertedId().asObjectId().getValue().toString();				
			return id;		
		} catch (Exception me) {
            throw new MongoException("Unable to upload due to an error: " + me);
        } 
	}
	
	public String subirInstrumento(String i, List<String> k) throws Exception {
		try {
			MongoCollection<Document> c = db.getCollection("Instrumentos");		
			List<String> respuestas = new ArrayList<>();
			Document d = new Document().append("instrumento", i).append("contenido", k).append("respuestas", respuestas);
			InsertOneResult ins = c.insertOne(d);
			return ins.getInsertedId().asObjectId().getValue().toString();				
		} catch (Exception me) {
            throw new MongoException("Unable to upload due to an error: " + me);
        }  		
	}
	
	public void subirRespuesta(String id, DtParticipa d) throws Exception {	
		String[] arr = d.getRespuesta();
		arr = ArrayUtils.add(arr, d.getUser());
		List<String> lR = Arrays.asList(arr);		
		MongoCollection<Document> c = db.getCollection("Instrumentos");			
		ObjectId ido = new ObjectId(id);
        Document doc = c.find(eq("_id", ido)).first();
        if (doc == null) {
            throw new InstrumentoException("Instrumento no existe");
        } else {
        	try { 
	            Bson updates = Updates.combine(Updates.push("respuestas", lR));	            
	            UpdateOptions options = new UpdateOptions().upsert(true);         
                c.updateOne(doc, updates, options);               
            } catch (Exception me) {
                throw new MongoException("Unable to update due to an error: " + me);
            }           	
        }     
	}
		
	public String subirComentarios(String cm, String user) throws Exception {
		try {
			MongoCollection<Document> c = db.getCollection("Comentarios");		
			Document d = new Document().append("autor", user).append("comentario", cm);
			InsertOneResult ins = c.insertOne(d);
			String id = ins.getInsertedId().asObjectId().getValue().toString();		
			return id;
		} catch (Exception me) {
            throw new MongoException("Unable to upload due to an error: " + me);
        }  
	}
	
	public String getRecursoIniciativa(String id) throws Exception {
		MongoCollection<Document> c = db.getCollection("Recursos");		
		ObjectId ido = new ObjectId(id);
        Document doc = c.find(eq("_id", ido)).first();
        if (doc == null) {
        	return null;
        } else {
            return doc.get("recurso").toString();
        }		        
	}
	
	public List<String> getInstrumentoProceso(String id) throws Exception {
		MongoCollection<Document> c = db.getCollection("Instrumentos");		
		ObjectId ido = new ObjectId(id);
        Document doc = c.find(eq("_id", ido)).first();
        if (doc != null) {
             List<String> ret = new ArrayList<>();    	
        	ret.add(doc.get("instrumento").toString());
        	List<String> d = (List<String>) doc.get("contenido");    	
			if (d.size() > 0) 
				for (String s: d)
					ret.add(s);	               	
            return ret;
        } else return null;	        
	}
	
	public String getComentarioUsuario(String id, String correo) throws Exception {
		MongoCollection<Document> c = db.getCollection("Comentarios");		
		ObjectId ido = new ObjectId(id);
        Document doc = c.find(eq("_id", ido)).first();
        String cm = null;
        if (doc == null) {
        	 throw new MongoException("Unable to get due to null");
        } else {
        	Ciudadano user = (Ciudadano) bd.buscarUsuario(doc.get("autor").toString());
        	if (user != null)
        		if (user.getCorreo().equals(correo)) 
        			cm = doc.get("comentario").toString();  
        }	
        return cm;
	}
	
	public String getComentarios(String id) throws Exception {
		MongoCollection<Document> c = db.getCollection("Comentarios");		
		ObjectId ido = new ObjectId(id);
        Document doc = c.find(eq("_id", ido)).first();
        if (doc != null) { 
        	Ciudadano user = (Ciudadano) bd.buscarUsuario(doc.get("autor").toString());
        	String nombre;
        	if (user != null) nombre = user.getNombreCompleto(); else nombre = "Pepito"; 
        	String comentario = nombre + " dice: " + doc.get("comentario").toString();        
            return comentario;
        } else return null;	 		        
	}	
	
	public void updateRecurso(String id, String r) throws Exception {
		MongoCollection<Document> c = db.getCollection("Recursos");	
		ObjectId ido = new ObjectId(id);
        Document doc = c.find(eq("_id", ido)).first();
        if (doc == null) {
            throw new Exception("Recurso no existe");
        } else {
        	try { 
	            Bson updates = Updates.combine(Updates.set("recurso", r));
	            UpdateOptions options = new UpdateOptions().upsert(true);         
                c.updateOne(doc, updates, options);
            } catch (Exception me) {
                throw new MongoException("Unable to update due to an error: " + me);
            }        	
        }        
	}
	
	public void updateInstrumento(String id, List<String> l) throws Exception {
		MongoCollection<Document> c = db.getCollection("Instrumentos");	
		ObjectId ido = new ObjectId(id);
        Document doc = c.find(eq("_id", ido)).first();
        if (doc == null) {
            throw new InstrumentoException("Instrumento no existe");
        } else {
        	try { 
	            Bson updates = Updates.combine(Updates.set("contenido", l));
	            UpdateOptions options = new UpdateOptions().upsert(true);         
                c.updateOne(doc, updates, options);
            } catch (Exception me) {
                throw new MongoException("Unable to update due to an error: " + me);
            }        	
        }        
	}

	public String getParticipacion(String id, String user) throws Exception {
		MongoCollection<Document> c = db.getCollection("Instrumentos");		
		ObjectId ido = new ObjectId(id);
        Document doc = c.find(eq("_id", ido)).first();
        if (doc == null) {
        	 throw new MongoException("Unable to get due to null");
        } else {         	
        	List<List<String>> respuestas = (List<List<String>>) doc.get("respuestas");  
        	List<String> respuestaUser;
			if (respuestas.size() > 0) {				
				for (int i=0; i < respuestas.size(); i++) {
					respuestaUser = respuestas.get(i);
					for (int j=0; j < respuestaUser.size(); j++)
						if (user.equals(respuestaUser.get(j)))
							return respuestaUser.get(j-1);
				}	    
			}
        }
        return null;        
	}

	public String getResultVotacion(String id) throws Exception {
		MongoCollection<Document> c = db.getCollection("Instrumentos");		
		ObjectId ido = new ObjectId(id);
        Document doc = c.find(eq("_id", ido)).first();
        if (doc == null) {
        	 throw new MongoException("Unable to get due to null");
        } else {     
        	String instrumento = (String) doc.get("instrumento");
        	List<String> contenido = (List<String>) doc.get("contenido");   
        	String opt1 = contenido.get(0);
        	String[] parts1 = opt1.split("\\:");        	
        	opt1 = parts1[1];
        	parts1 = opt1.split("\\,");
        	opt1 = parts1[0];
        	String opt2 = contenido.get(1);
        	String[] parts2 = opt2.split("\\:");
        	opt2 = parts2[1]; 
        	parts2 = opt2.split("\\,");
        	opt2 = parts2[0];
        	String result = null;
        	if (instrumento.equals("votacion")) {
        		result = "Resultado de la votación -" + contenido.get(2) + "- "; 
        		List<List<String>> respuestas = (List<List<String>>) doc.get("respuestas"); 
        		int r1, r2;
        		r1 = r2 = 0;
            	List<String> respuestaUser;
        		for (int i=0; i < respuestas.size(); i++) {
					respuestaUser = respuestas.get(i);
					for (int j=0; j < respuestaUser.size(); j++) {
						if (respuestaUser.get(j).equals(opt1)) 
							r1++;
						else if (respuestaUser.get(j).equals(opt2))
							r2++;
					}
        		}
        		result = result + opt1 + "=" + r1 + " | " + opt2 + "=" + r2;
        		return result;
        	}  
        }        
        return null;   
	}

	public String getResultEncuesta(String id) throws Exception {
		MongoCollection<Document> c = db.getCollection("Instrumentos");		
		ObjectId ido = new ObjectId(id);
        Document doc = c.find(eq("_id", ido)).first();
        if (doc == null) {
        	 return null;
        } else {     
        	String instrumento = (String) doc.get("instrumento");
        	List<String> contenido = (List<String>) doc.get("contenido");          	
        	List<String> opcionesEncuesta = new ArrayList<String>();        	
        	String opt = null;
        	String[] parts;   
        	String result = null;
        	for (int i=0; i < contenido.size() - 1; i++) {
        		opt = contenido.get(i);
        		parts = opt.split("\\:");
        		opt = parts[1];
        		parts = opt.split("\\,");
        		opt = parts[0];
        		opcionesEncuesta.add(opt);        	
        	}  	
        	int opts = opcionesEncuesta.size();
        	if (opts > 0) {
        		int r0, r1, r2, r3, r4, r5;
        		r0 = r1 = r2 = r3 = r4 = r5 = 0;         	
		        	
	        	if (instrumento.equals("encuesta")) {
	        		result = "Resultado de la encuesta -" + contenido.get(contenido.size()-1) + "- "; 
	        		List<List<String>> respuestas = (List<List<String>>) doc.get("respuestas"); 
	        		List<Integer> r = new ArrayList<Integer>();
	            	List<String> respuestaUser;             	        		
	        		for (int i=0; i < respuestas.size(); i++) {
						respuestaUser = respuestas.get(i);
						for (int j=0; j < respuestaUser.size(); j++) {		
							if (opts > 2 && opts <= 3) {
								if (respuestaUser.get(j).equals(opcionesEncuesta.get(0))) 
									r0++;
								else if (respuestaUser.get(j).equals(opcionesEncuesta.get(1)))  
									r1++;
								else if (respuestaUser.get(j).equals(opcionesEncuesta.get(2))) 
									r2++;
							} else if (opts > 3 && opts <= 4) {
								if (respuestaUser.get(j).equals(opcionesEncuesta.get(0))) 
									r0++;
								else if (respuestaUser.get(j).equals(opcionesEncuesta.get(1)))  
									r1++;
								else if (respuestaUser.get(j).equals(opcionesEncuesta.get(2))) 
									r2++;
								else if (respuestaUser.get(j).equals(opcionesEncuesta.get(3))) 
									r3++;
							} else if (opts > 4 && opts <= 5) {
								if (respuestaUser.get(j).equals(opcionesEncuesta.get(0))) 
									r0++;
								else if (respuestaUser.get(j).equals(opcionesEncuesta.get(1)))  
									r1++;
								else if (respuestaUser.get(j).equals(opcionesEncuesta.get(2))) 
									r2++;
								else if (respuestaUser.get(j).equals(opcionesEncuesta.get(3))) 
									r3++;
								else if (respuestaUser.get(j).equals(opcionesEncuesta.get(4))) 
									r4++;
							} else if (opts > 5 && opts <= 6) {
								if (respuestaUser.get(j).equals(opcionesEncuesta.get(0))) 
									r0++;
								else if (respuestaUser.get(j).equals(opcionesEncuesta.get(1)))  
									r1++;
								else if (respuestaUser.get(j).equals(opcionesEncuesta.get(2))) 
									r2++;
								else if (respuestaUser.get(j).equals(opcionesEncuesta.get(3))) 
									r3++;
								else if (respuestaUser.get(j).equals(opcionesEncuesta.get(4))) 
									r4++;
								else if (respuestaUser.get(j).equals(opcionesEncuesta.get(5))) 
									r5++;
							}								
						}
	        		}
	        	} 
	        	if (opts > 2 && opts <= 3)
	        		result = result + opcionesEncuesta.get(opts-opts) + "=" + r0 + " | " + opcionesEncuesta.get(opts-(opts-1)) + "=" + r1
	    				 + " | " + opcionesEncuesta.get(opts-(opts-2)) + "=" + r2;
	        	else if (opts > 3 && opts <= 4) 
	        		result = result + opcionesEncuesta.get(opts-opts) + "=" + r0 + " | " + opcionesEncuesta.get(opts-(opts-1)) + "=" + r1
	        			+ " | " + opcionesEncuesta.get(opts-(opts-2)) + "=" + r2 + " | " + opcionesEncuesta.get(opts-(opts-3)) + "=" + r3;
	        	else if (opts > 4 && opts <= 5) 
	        		result = result + opcionesEncuesta.get(opts-opts) + "=" + r0 + " | " + opcionesEncuesta.get(opts-(opts-1)) + "=" + r1
        			+ " | " + opcionesEncuesta.get(opts-(opts-2)) + "=" + r2 + " | " + opcionesEncuesta.get(opts-(opts-3)) + "=" + r3 
        			+ " | " + opcionesEncuesta.get(opts-(opts-4)) + "=" + r4;
	        	else if (opts > 5 && opts <= 6)
	        		result = result + opcionesEncuesta.get(opts-opts) + "=" + r0 + " | " + opcionesEncuesta.get(opts-(opts-1)) + "=" + r1
        			+ " | " + opcionesEncuesta.get(opts-(opts-2)) + "=" + r2 + " | " + opcionesEncuesta.get(opts-(opts-3)) + "=" + r3 
        			+ " | " + opcionesEncuesta.get(opts-(opts-4)) + "=" + r4 + " | " + opcionesEncuesta.get(opts-(opts-5)) + "=" + r5;
	        	return result;
        	}
        }        
        return null;  	
	}

	public String borrarComentario(String comentario) throws Exception {
		try {			
			MongoCollection<Document> c = db.getCollection("Comentarios");				
	        Document doc = c.find(eq("comentario", comentario)).first();
	        if (doc != null) { 
	        	String id = doc.get("_id").toString();
	        	if (id != null) System.out.println(id); else  System.out.println("WTFFF");
	        	Bson query = eq("comentario", comentario);
	        	System.out.println(query.toString());
	        	c.deleteOne(query);		
	            return id;
	        } else {
	        	return null;	 				
	        }
		} catch (Exception e) {
			throw e;
		}	              
	}	
	
}
	
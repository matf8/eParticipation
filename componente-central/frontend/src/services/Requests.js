import axios from "axios";

// Crear base de Axios "instance"
const instance = axios.create({
  baseURL:
    "https://eparticipation.web.elasticloud.uy/backend-web/eParticipation",
  headers: {
    'Content-Type': 'application/json',    
  },
});

instance.interceptors.request.use(
  (config) => {
    const token = getToken();
    if (token) {
      config.headers["authorization"] = "Bearer " + token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

//esta funcion es para cerrar sesion
export const clearState = () => {
  localStorage.removeItem("token");
  localStorage.removeItem("userRole");
  localStorage.removeItem("userID");
  localStorage.removeItem("userCI");
  sessionStorage.clear(); 
  window.location.replace("/");
};

export const fetchUserRole = () => {
  return localStorage.getItem("userRole");
};

export const fetchUserID = () => {
  return localStorage.getItem("userID");
};

export const getToken = () => {
  return localStorage.getItem("token");
};

export const userLogin = () => {
  sessionStorage.setItem("facebookLogin", false);
  
  const authURL = "https://eparticipation.web.elasticloud.uy/backend-web/auth" 
  return window.location.replace(authURL); 
};

export const loginExterno = (datosUsuario) => {
  return instance.post(`/externalAuth/facebook`, datosUsuario);
};

export const getIniciativas = () => {
  return instance.get(
    "/iniciativa/listar"
  );
};

export const getIniciativasPublicadas = () => {
  return instance.get(
    "/iniciativa/listarPublicadas"
  );
};

export const getIniciativasRango = (fechaInicio, fechaFin) => {
  return instance.get(
    `/iniciativa/listarRango?fecha=${fechaInicio}&fecha=${fechaFin}`
  );
};

export const getProcesosRango = (fechaInicio, fechaFin) => {
  return instance.get(
    `/proceso/listarRango?fecha=${fechaInicio}&fecha=${fechaFin}`
  );
};

export const newIniciativa = (iniciativa) => {
  return instance.post("/iniciativa/alta", iniciativa);
};

export const updateIniciativa = (iniciativa) => {
  return instance.put("/iniciativa/modificar", iniciativa);
};

export const updateProceso = (proceso) => {
  return instance.put("/proceso/modificar", proceso);
}

export const deleteIniciativa = (iniciativa) => {
  return instance.delete(`/iniciativa/baja?nombre=${iniciativa}`);
};

export const deleteProceso = (proceso) => {
  return instance.delete(`/proceso/baja?nombre=${proceso}`);
};

export const getIniciativa = (nombreIniciativa) => {
  return instance.get(`/iniciativa/buscar/${nombreIniciativa}`);
};

export const getProceso = (nombreProceso) => {
  return instance.get(`proceso/buscar/${nombreProceso}`);
};

export const getProcesos = () => {
  return instance.get("/proceso/listar");
};

export const getProcesosCiudadano = (usuario) => {
  return instance.get(`/usuario/listarProcesos?user=${usuario}`);
};

export const newProceso = (proceso) => {
  return instance.post("/proceso/alta", proceso);
};

export const seguirAIniciativa = (idIniciativa, correoCiudadano) => {
  return instance.post(
    `/iniciativa/seguir?iniciativa=${idIniciativa}&user=${correoCiudadano}`
  );
};

export const dejarSeguirAIniciativa = (idIniciativa, correoCiudadano) => {
  return instance.post(
    `/iniciativa/dejarSeguir?iniciativa=${idIniciativa}&user=${correoCiudadano}`
  );
};

export const dejarSeguirAProceso = (idProceso, correoCiudadano) => {
  return instance.post(
    `/proceso/dejarSeguir?proceso=${idProceso}&user=${correoCiudadano}`
  );
};

export const comentarIniciativa = (comentario, usuario, iniciativa) => {
  return instance.post(
    `/iniciativa/comentar?comentario=${comentario}&user=${usuario}&iniciativa=${iniciativa}`,
    comentario
  );
};

export const comentarProceso = (comentario, usuario, proceso) => {
  return instance.post(
    `/proceso/comentar?comentario=${comentario}&user=${usuario}&proceso=${proceso}`,
    comentario
  );
};

export const adherirAIniciativa = (nombreIniciativa, correoCiudadano) => {
  return instance.post(
    `/iniciativa/adherir?iniciativa=${nombreIniciativa}&user=${correoCiudadano}`
  );
};

export const desadherirAIniciativa = (nombreIniciativa, correoCiudadano) => {
  return instance.post(
    `/iniciativa/desadherir?iniciativa=${nombreIniciativa}&user=${correoCiudadano}`
  );
};

export const getUsuario = (nombreCiudadano) => {
  return instance.get(`/usuario/buscar/${nombreCiudadano}`);
};

export const updateUsuario = (datos) => {
  return instance.put("/usuario/modificarUsuario", datos);
};

export const ciudadanoSigueIniciativa = (iniciativa, ciudadano) => {
  return instance.get(
    `/usuario/ifSigueI?iniciativa=${iniciativa}&user=${ciudadano}`
  );
};

export const ciudadanoSigueProceso = (proceso, ciudadano) => {
  return instance.get(
    `/usuario/ifSigueP?proceso=${proceso}&user=${ciudadano}`
  );
};

export const ciudadanoAdheridoIniciativa = (iniciativa, ciudadano) => {
  return instance.get(
    `/usuario/ifAdherido?iniciativa=${iniciativa}&user=${ciudadano}`
  );
};

export const ciudadanoParticipoProceso = (proceso, ciudadano) => {
  return instance.get(
    `/usuario/ifParticipaP?proceso=${proceso}&user=${ciudadano}`
  );
};

export const participarProceso = (data) => {
  return instance.post(
    "/proceso/participar", data
  );
};

export const eleccionProcesoCiudadano = (proceso, ciudadano) => {
  console.log(proceso, ciudadano);
  return instance.get(
    `/usuario/participacion?proceso=${proceso}&user=${ciudadano}`
  );
};

export const seguirAProceso = (proceso, ciudadano) => {
  return instance.post(`/proceso/seguir?proceso=${proceso}&user=${ciudadano}`);
};

export const eliminarMensaje = (user, mensaje) => {
 return instance.delete(`/usuario/eliminarMensaje?user=${user}&mensaje=${mensaje}`);
};

export const eliminarMensajes = (user) => {
  return instance.delete(`/usuario/vaciarMensajes?user=${user}`);
};
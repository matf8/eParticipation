import React from "react";
import { LoadingLogin } from "../../components/LoadingLogin";
import { useSearchParams } from "react-router-dom";
import { getUsuario } from "../../services/Requests";

function LoginGubUy() {
  const [params] = useSearchParams();
  const state = params.get("state"); 
  const code = params.get("code"); 
  const user = params.get("user");
  if (code != null) {   
    const authURL = 'https://eparticipation.web.elasticloud.uy/backend-web/token?state='+state+'&code='+code;   
    return window.location.replace(authURL);   
  } else if (user != null) {
    const cedula = user;
    const tokenLogout = params.get("tokenID");    // tokenId gub uy para logout
    const token = params.get("tokenSesion");
   
    getUsuario(cedula).then((response) => {     //JSON USUARIO
      console.log(response.data);  
      localStorage.setItem("token", token);  
      localStorage.setItem("userID", response.data.correo); //asignar id = correo
      localStorage.setItem("userRole", response.data.rol); //asignar perfil = rol
      sessionStorage.setItem("tokenId", tokenLogout);     
      window.location.replace("/"); // redirect 
    });

  }
  
  return (
    <>
      <LoadingLogin />
    </>
  );
 
}

export default LoginGubUy;

  /*
  WORKFLOW GUB UY : * TOCAR LOGIN CON GUB UY -> va al servlet auth y realiza log in en gub uy, retorna por uri el code y el state
                    * Cuando vuelve con el code y el state lo mando pal servlet token para mandar el form y obtener el acces token que aca jode por el cors choto, desde widlfy no hace drama
                    * En el back genero el pedido a user info de gub uy, con la cedula pedido a PDI, Si es priemra vez ALTA; sino RETURN USER, genero el JWT(tokenSesion) y te mando el ROL aparte
                    * y vamo las putas!
                    * falta logout -> si da problema de cors aca, se hace un servlet logout y se hace de ahi y fue

  /*
  //"end_session_endpoint": "https://auth-testing.iduruguay.gub.uy/oidc/v1/logout"

  // para borrar 
  const [params] = useSearchParams();
  const code = params.get("code");
  const state = params.get("state");
  const client_id = process.env.REACT_APP_CLIENT_ID;  
  const client_secret = process.env.REACT_APP_CLIENT_SECRET;

  const plainCredentials = client_id + ":" + client_secret;
  var bytes = [];
  for (var i = 0; i < plainCredentials.length; i++) {
    bytes.push(plainCredentials.charCodeAt(i));
  }
  const base64Credentials = Base64.encode(bytes);
  const authorizationHeader = "Basic " + base64Credentials;    
  const redirect_uri = process.env.REACT_APP_REDIRECT_URI;  
  var form = new FormData();
    form.append('grant_type', 'authorization_code');
    form.append('code', code);
    form.append('redirect_uri', redirect_uri);
    form.append('state', state);

  const tokenURL = axios.create({
    baseURL:"https://auth-testing.iduruguay.gub.uy",
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded',        
        'Authorization': authorizationHeader,
        'Access-Control-Allow-Origin': '*',
      },      
    });
    
    for (const value of form.values()) {
      console.log(value);
    }
    console.log(tokenURL.toString());

    tokenURL.post("/oidc/v1/token", form).then((response) => {
      console.log(response.access_token);
      //llamada a "https://auth-testing.iduruguay.gub.uy/oidc/v1/userinfo"
    })
    .catch((error) => console.log(error)); */

 
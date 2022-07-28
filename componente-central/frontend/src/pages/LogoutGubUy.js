import React from "react";
import { LoadingLogout } from "../components/LoadingLogout";
import { clearState } from "../services/Requests";

function LogoutGubUy() {  
  const token = sessionStorage.getItem("tokenId");
  console.log(token);
  if (token && token !== undefined) {   
    const logoutURL = 'https://auth-testing.iduruguay.gub.uy/oidc/v1/logout?id_token_hint=' + token 
    + '&post_logout_redirect_uri=https://we-participation.web.elasticloud.uy/logout&state=logout'
    sessionStorage.removeItem("tokenId");
    window.location.replace(logoutURL);     
  } else {
    clearState();   
  } 
   
  return (
    <div>
      <LoadingLogout />
    </div>
  );
}

export default LogoutGubUy;


import React from "react";
import { LoadingLogout } from "../components/LoadingLogout";
import { clearState } from "../services/Requests";

function LogoutFacebook() {  
  
  clearState();   
 
  return (
    <div>
      <LoadingLogout />
    </div>
  );
}

export default LogoutFacebook;


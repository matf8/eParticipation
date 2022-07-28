import React from "react";
import Ciudadano from "./pages/ciudadano/Ciudadano";
import Funcionario from "./pages/funcionario/Funcionario";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { Loading } from "./components/Loading";
import { toast } from "react-toastify";

import {
  getToken
} from "./services/Requests";
import Invitado from "./pages/invitado/Invitado";

toast.configure();
export default function App() { 
  console.log(getToken());
  console.log(localStorage.getItem("userID"));
  console.log(sessionStorage.getItem("tokenId"));

  if (getToken()) {
    const tipoUser = localStorage.getItem("userRole");
    /* if (sessionStorage.getItem("facebookLogin") === true) {
      fetchUserRole()
        .then((response) => {
          const rol = response.data;
          setTipoUser(rol);
        })
        .catch((error) => {
          console.log(error.data);
          clearState();
        });
    } else {
      getUsuario(localStorage.getItem("userCI"))
        .then((response) => {
          localStorage.setItem("userID", response.data.correo);
          setTipoUser(response.data.rol);
        })
        .catch((error) => {console.log(error.data)});
    } 
    console.log(getToken());
    setTipoUser(localStorage.getItem("userRole"));*/
    switch (tipoUser) {
      case "CIUDADANO":
        return <Ciudadano />;
      case "FUNCIONARIO":
        return <Funcionario />;     
      default:
        return (
          <BrowserRouter>
            <Routes>
              <Route exact path="/" element={<Loading />} />
            </Routes>
          </BrowserRouter>
        );
    }
  } else {    
      return <Invitado />;    
  }
 /* return (
    <BrowserRouter>
      <Routes>
        <Route exact path="/" element={<Loading />} />
      </Routes>
    </BrowserRouter>
  );*/
}

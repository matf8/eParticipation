import React, { useState } from "react";
import { Footer } from "../../components/Footer";
import { Layout } from "../../components/Layout";
import { NavigationBar } from "./NavBar";
import styled from "styled-components";
import { Form, Button } from "react-bootstrap";
import { loginExterno, userLogin } from "../../services/Requests";
import FacebookLogin from "react-facebook-login";
import { NotiError } from "../../components/Notification";
import { Error } from "../../components/Error";

const Styles = styled.div`
  #page-container {
    padding-top: 50%;
    margin-bottom: -50%;
    overflow: hidden;
  }

  #loginError {
    margin-top: 20px;
  }

  h4 {
    margin-bottom: 20px;
    text-align: center;
  }

  h5 {
    margin-top: 20px;
    text-align: center;
  }

  #tipo {
    width: 50%;
    margin-left: 25%;
    background-color: skyblue;
    color: black;
  }
  #gubuy {
    font-family: Arial, Helvetica, sans-serif;
    font-weight: bold;
    background-color: #ffc800;
    color: black;
  }

.form-alta {

  position: absolute;
  left: 50%;
  top 45%;
  -webkit-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  width: 400px;
  background: white;
  padding: 30px;
  margin: auto;
  border-radius: 10px;
  box-shadow: 7px 13px 37px #000;

  Button {
    width: 100%;
    color: white;
    background-color: #0c19cf;
    border: none;
    padding: 15px;
    margin-top: 15px;
    &:focus {
      box-shadow: 0 0 0 0.25rem rgba(232, 113, 33, 0.25);
      background-color: #0c19cf;
    }
    &:hover {
      background-color: #737cfa;
    }
    &:active {
      background-color: #0c19cf;
    }

  }
}
`;

function Login() {
  const [error, guardarError] = useState("");

  const handleFacebookClick = () => {
    sessionStorage.setItem("facebookLogin", true);
  };

  const responseFacebook = (response) => {
    if (response.status === "unknown") {
      guardarError("Login failed!");
      return false;
    }

    if (response.accessToken) {    
      console.log(response);
      sessionStorage.setItem("fbPicture", response.picture.data.url);
      let u = { 
        id: response.id,
        name: response.name,
        mail: response.email,        
        ci: "000"+
          + Math.floor(Math.random() * 10)
          + Math.floor(Math.random() * 10)
          + Math.floor(Math.random() * 10)
          + Math.floor(Math.random() * 10)
          + Math.floor(Math.random() * 10) 
      }
      console.log(u);
      loginExterno(u)
      .then((res) => {
        if (res.status === 200) {
          localStorage.setItem("token", res.data.token);
          localStorage.setItem("userID", res.data.correo);
          localStorage.setItem("userRole", res.data.rol);
          window.location.replace("/");
        } else {
          NotiError("hubo un error inesperado");
        }
      })
      .catch((err) => {
        if (err && err.response) {
          switch (err.response.status) {
            case 401:
              guardarError("Funcionario: inicie sesión con su correo de Gub.Uy por favor.");
              break;
            default:
              guardarError("Algo salio mal!!");
          }
        } else {
          guardarError("Algo salio mal!!");
        }
      });
    } else {
      guardarError("Login failed!");
    }
  };

  let componente;
  if (error !== "") {
    componente = <Error error={error} />;
  } else {
    componente = null;
  }

  return (
    <Styles>
      <Layout>
        <NavigationBar />
        <div id="page-container"></div>
        <section className="form-alta">
          <Form>
            <h4>Inicio de sesión</h4>
            <Button
              id="gubuy"
              onClick={() => {
                userLogin();
              }}
            >
              LOGIN WITH GUB.UY
            </Button>
            <FacebookLogin
              appId={process.env.REACT_APP_APP_ID}
              autoLoad={false}
              fields="name,email,picture"
              scope="email"
              onClick={handleFacebookClick}
              callback={responseFacebook}
            />
            <div id="loginError">{componente}</div>
          </Form>
        </section>
      </Layout>
      <Footer />
    </Styles>
  );
}

export default Login;

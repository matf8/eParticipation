import React, { useState } from "react";
import { Footer } from "../components/Footer";
import { Layout } from "../components/Layout";
import { NavigationBar } from "./invitado/NavBar";
import styled from "styled-components";
import { Form, Button } from "react-bootstrap";
import { loginExterno, userLogin } from "../services/Requests";
import FacebookLogin from "react-facebook-login";
import { NotiError } from "../components/Notification";
import { Error } from "../components/Error";
import Cookie from "js-cookie";

const Styles = styled.div`
  #page-container {
    padding-top: 50%;
    background-image: url("https://thumbs.dreamstime.com/b/hablando-con-la-gente-grupo-de-caricaturistas-coloridas-burbujas-palabras-ilustraci%C3%B3n-del-vector-200696176.jpg");
    filter: blur(6px);
    background-position: center;
    background-repeat: no-repeat;
    background-size: cover;
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
    loginExterno(response)
      .then((res) => {
        if (res.status === 200) {
          localStorage.setItem("token", Cookie.get("authorization"));
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
              guardarError("Algo salio mal!! correo o contraseña incorrectos");
              break;
            default:
              guardarError("Algo salio mal!!");
          }
        } else {
          guardarError("Algo salio mal!!");
        }
      });
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
              appId={process.env.APP_ID}
              autoLoad={false}
              fields="name,email,picture"
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

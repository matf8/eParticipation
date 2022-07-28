import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { Footer } from "../../components/Footer";
import { Layout } from "../../components/Layout";
import { Form, Button, Alert } from "react-bootstrap";
import { updateIniciativa } from "../../services/Requests";
import { Error } from "../../components/Error";
import { useSearchParams } from "react-router-dom";
import { getIniciativa } from "../../services/Requests";
import { NotiError } from "../../components/Notification";

const Styles = styled.div`
#page-container {
    padding-top: 50%;
    margin-bottom: -50%;
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

.form-alta {

position: absolute;
left: 50%;
top 45%;
-webkit-transform: translate(-50%, -50%);
transform: translate(-50%, -50%);
width: 400px;
height: auto;
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
#message {
    margin-top: 20px;
 }
 textarea {
    width: 100%;
    min-width: 100%;
    max-width: 100%;
    height: auto;
    min-height: 10rem;
    cursor: text;
    overflow: auto;
    resize: both;
}

`;

export default function ModificarIniciativa() {
  const [iniciativa, setIniciativa] = useState({
    id: "",
    nombre: "",
    fecha: "",
    descripcion: "",
  });
  const [params] = useSearchParams();
  useEffect(() => {
    const nombre = params.get("nombre");
    getIniciativa(nombre)
      .then((response) => {
        setIniciativa(response.data);
      })
      .catch((error) => {
        NotiError(error.response.data);
      });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const [error, setError] = useState("");
  const [success, setSuccess] = useState(null);

  const handleChange = (e) => {
    e.persist();
    setIniciativa((iniciativa) => ({
      ...iniciativa,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    updateIniciativa(iniciativa).then((response) => {
      if (response.status === 200) {
        setSuccess(
          <Alert variant="success">Iniciativa modificada con éxito!</Alert>
        );
        setTimeout(() => {
          window.location.replace("/");
        }, 3000);
      } else {
        setError("Algo salio mal!!");
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
        <div id="page-container"></div>
        <section className="form-alta">
          <Form onSubmit={handleSubmit}>
            <h4>modificar iniciativa</h4>
            <div className="form-floating">
              <input
                type="text"
                name="nombre"
                className="form-control"
                id="nombre"
                onChange={handleChange}
              />
              <label htmlFor="floatingInput">{iniciativa.nombre}</label>
              <div />
              <br />
              <div className="form-floating">
                <textarea
                  type="text"
                  name="descripcion"
                  className="form-control"
                  id="descripcion"
                  onChange={handleChange}
                />
                <label htmlFor="floatingInput">Descripcion</label>
              </div>
              <br />
              <div className="form-floating">
                <input
                  type="date"
                  name="fecha"
                  className="form-control"
                  id="fecha"
                  onChange={handleChange}
                />
                <label htmlFor="floatingInput">{iniciativa.fecha}</label>
              </div>
              <Button type="submit">Modificar</Button>
              <div id="message">{success}</div>
              <div id="message">{componente}</div>
            </div>
          </Form>
        </section>
      </Layout>
      <Footer />
    </Styles>
  );
}

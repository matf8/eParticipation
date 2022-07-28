import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { Footer } from "../../components/Footer";
import { Layout } from "../../components/Layout";
import { Form, Button, Alert } from "react-bootstrap";
import { Error } from "../../components/Error";
import { useSearchParams } from "react-router-dom";
import { getProceso, updateProceso } from "../../services/Requests";
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

export default function ModificarProceso() {
  const [proceso, setProceso] = useState({
    id: "",
    nombre: "",
    fecha: "",
    descripcionAlcance: "",
    fase: "",
  });
  const [params] = useSearchParams();
  useEffect(() => {
    const nombre = params.get("nombre");
    getProceso(nombre)
      .then((response) => {
        setProceso(response.data);
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
    setProceso((proceso) => ({
      ...proceso,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    updateProceso(proceso).then((response) => {
      if (response.status === 200) {
        setSuccess(
          <Alert variant="success">Proceso modificado con éxito!</Alert>
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
            <h4>modificar proceso</h4>
            <div className="form-floating">
              <input
                type="text"
                name="nombre"
                className="form-control"
                id="nombre"
                onChange={handleChange}
              />
              <label htmlFor="floatingInput">{proceso.nombre}</label>
              <div />
              <br />
              <div className="form-floating">
                <textarea
                  type="text"
                  name="descripcionAlcance"
                  className="form-control"
                  id="descripcionAlcance"
                  onChange={handleChange}
                />
                <label htmlFor="floatingInput">Alcance</label>
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
                <label htmlFor="floatingInput">{proceso.fecha}</label>
              </div>
              <br />
              <div className="form-floating">
                <select className="form-control" name="fase" id="fase" onChange={handleChange}>
                    <option selected>seleccione una fase</option>
                    <option value="fase_inicial">Fase Inicial</option>
                    <option value="fase_evolutiva">Fase Evolutiva</option>
                    <option value="fase_final">Fase Final</option>
                    <option value="publicado">Publicado</option>
                </select>
                <label htmlFor="floatingInput">fase</label>
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

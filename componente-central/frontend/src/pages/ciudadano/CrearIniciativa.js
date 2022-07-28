import React, { useState } from "react";
import styled from "styled-components";
import { Footer } from "../../components/Footer";
import { Layout } from "../../components/Layout";
import { Form, Button, Alert } from "react-bootstrap";
import { newIniciativa } from "../../services/Requests";
import { Error } from "../../components/Error";
import { useSearchParams } from "react-router-dom";

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

export default function CrearIniciativa() {
  const [params] = useSearchParams();
  const correo = params.get("ciudadano");

  const [iniciativa, setIniciativa] = useState({
    nombre: "",
    descripcion: "",
    fecha: "",
    creador: correo,
    recurso: "",
  });

  const [error, setError] = useState("");
  const [success, setSuccess] = useState(null);

  const handleChange = (e) => {
    e.persist();
    setIniciativa((iniciativa) => ({
      ...iniciativa,
      [e.target.name]: e.target.value,
    }));
  };

  const convertBase64 = (file) => {
    return new Promise((resolve, reject) => {
      const fileReader = new FileReader();
      fileReader.readAsDataURL(file);
      fileReader.onload = () => {
        resolve(fileReader.result);
      };
      fileReader.onerror = (error) => {
        reject(error);
      };
    });
  }

  const handleUpload = async (data) => {
    const file = data.target.files[0];
    const base64 = await convertBase64(file);
    iniciativa.recurso = base64;
    console.log(iniciativa);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    newIniciativa(iniciativa).then((response) => {
      if (response.status === 200) {
        setSuccess(
          <Alert variant="success">Iniciativa creada con éxito!</Alert>
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
            <h4>nueva iniciativa</h4>
            <div className="form-floating">
              <input
                type="text"
                name="nombre"
                className="form-control"
                id="nombre"
                onChange={handleChange}
                required
              />
              <label htmlFor="floatingInput">Nombre</label>
              <div />
              <br />
              <div className="form-floating">
                <textarea
                  type="text"
                  name="descripcion"
                  className="form-control"
                  id="descripcion"
                  onChange={handleChange}
                  required
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
                  required
                />
                <label htmlFor="floatingInput">Fecha</label>
              </div>
              <br />
              <div className="form-floating">
                <h6 className="mb-3">Recurso de la iniciativa</h6>
                <input
                  className="archivo mb-3"
                  type="file"
                  size="lg"
                  onChange={handleUpload}
                />
              </div>
              <Button type="submit">Crear</Button>
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

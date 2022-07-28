import React, { useEffect, useState } from "react";
import styled from "styled-components";
import { Footer } from "../../components/Footer";
import { Button } from "react-bootstrap";
import { NotiError } from "../../components/Notification";
import { getIniciativas, getProcesos, getToken } from "../../services/Requests";
import picture from "../../assets/default-picture.jpg";

const Styles = styled.div`
  h1 {
    padding-top: 15px;
  }
  #izquierda {
    width: 45%;
    float: left;
    color: black;
    padding: 15px;
    margin-top: 10px;
    margin-left: 30px;
    height: 45rem;
    border-radius: 10px;
    overflow: scroll;
  }
  #izquierda::-webkit-scrollbar {
    display: none;
  }
  #derecha {
    width: 45%;
    float: right;
    color: black;
    padding: 15px;
    margin-top: 10px;
    margin-right: 30px;
    height: 45rem;
    border-radius: 10px;
    overflow: scroll;
  }
  #derecha::-webkit-scrollbar {
    display: none;
  }
  article {
    width: 100%;
    padding: 10px;
    float: left;
    background-color: white;
    margin-bottom: 20px;
    margin-top: 10px;
    border-radius: 10px;
  }
  #vertodos {
    float: right;
    margin-left: 10px;
  }
  figure {
    float: right;
  }
  #header {
    color: black;
  }
`;

function PaginaPrincipal() {
  const token = getToken();
  var acceso = token !== null && token !== undefined;
  const [iniciativas, setIniciativas] = useState([]);
  const [procesos, setProcesos] = useState([]);

  useEffect(() => {
    getIniciativas()
      .then((response) => {
        console.log(response.data);
        setIniciativas(response.data);
      })
      .catch((error) => {
        NotiError(error.response.data);
      });
    getProcesos()
      .then((response) => {
        const procs = response.data.slice(0, 5);
        setProcesos(procs);
      })
      .catch((error) => {
        NotiError(error.response.data);
      });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const noPuede = () => {
    NotiError("para acceder a la informacion debes estar logueado");
  }

  return (
    <Styles>
      <nav>
        <h1 align="center" id="header" style={{ color: 'black'}}>Bienvenido!!</h1>
      </nav>
      <aside id="izquierda">
        {iniciativas.map((ini) => {
          return (
            <article key={ini.nombre}>
              <figure>
                <img
                  src={ini.recurso ? ini.recurso : picture}
                  alt="The Pulpit Rock"
                  width="160"
                  height="115"
                />
              </figure>
              <h6>{ini.fecha}</h6>
              <h1>{ini.nombre}</h1>
              <p>{ini.descripcion}</p>
              {acceso ? 
              <Button href={"/iniciativa?nombre=" + ini.nombre}>Ver mas</Button>
              :
              <Button onClick={() => {noPuede()}}>Ver mas</Button>
              }
            </article>
          );
        })}
        <Button id="verTodos" variant="light" href="/iniciativas">
          Ver mas iniciativas
        </Button>
      </aside>
      <aside id="derecha">
        {procesos.map((proc) => {
          return (
            <article key={proc.nombre}>
              <h6>{proc.fecha}</h6>
              <h1>{proc.nombre}</h1>
              <p>{proc.descripcion}</p>
              {acceso ? 
              <Button
              href={"proceso?nombre=" + proc.nombre}
              >
                Ver mas
              </Button>
              :
              <Button
              onClick={() => {noPuede()}}
              >
                Ver mas
              </Button>
              }
            </article>
          );
        })}
        <Button id="verTodos" variant="dark" href="/procesos">
          Ver mas procesos
        </Button>
      </aside>
      <Footer />
    </Styles>
  );
}

export default PaginaPrincipal;

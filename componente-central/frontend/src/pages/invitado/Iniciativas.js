import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { Footer } from "../../components/Footer";
import { Layout } from "../../components/Layout";
import { Button, ListGroup } from "react-bootstrap";
import DatePicker from "react-datepicker";
import { NotiError } from "../../components/Notification";
import { getIniciativas, getIniciativa, getIniciativasRango } from "../../services/Requests";
import { getToken}  from "../../services/Requests";

import "react-datepicker/dist/react-datepicker.css";

const Styles = styled.div`
  #buscador {
    position: absolute;
    height: 10%;
    width: 70%;
    margin-top: 15px;
    margin-left: 40px;
    margin-bottom: 15px;
    border-radius: 10px;
    color: black;
    padding-top: 50px;
    padding-left: 5%;
  }

  #listado {
    position: absolute;
    width: 70%;
    height: 70%;
    margin-top: 150px;
    margin-left: 40px;
    border-radius: 10px;
    overflow: scroll;
    border-style: solid;
    color: black;
  }
  input {
    margin-left: 5px;
  }
  #verMasButton {
    float: right;
  }
  #listado::-webkit-scrollbar {
    display: none;
  }
`;

function Iniciativas() {
  const token = getToken();
  var acceso = token !== null && token !== undefined;
  const [data, setData] = useState([]);
  const [nombre, setNombre] = useState({ nombre: "" });
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);

  useEffect(() => {
    getIniciativas()
      .then((response) => {
        setData(response.data);
      })
      .catch((error) => {
        NotiError(error.response.data);
      });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleChange = (e) => {
    e.persist();
    setNombre((nombre) => ({
      ...nombre,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if(nombre.nombre !== "") {
      setData([]);
      getIniciativa(nombre.nombre).then((response) => {
        if(response.data === "Dato null") {
          NotiError("iniciativa no existe");
        } else {
          let newData = [];
          newData.push(response.data);
          setData(newData);
        }
      })
      .catch((error) => {console.log(error.data)});
    } else if(nombre.nombre === "" && startDate === null && endDate === null) {
      getIniciativas()
      .then((response) => {
        setData(response.data);
      })
      .catch((error) => {
        NotiError(error.response.data);
      });
    } else {
      getIniciativasRango(startDate, endDate).then((response) => {
        setData(response.data);
      })
      .catch((error) => {console.log(error.data)});
    }
  }

  const onChangeDate = (dates) => {
    const [start, end] = dates;
    setStartDate(start);
    setEndDate(end);
  };

  const noPuede = () => {
    NotiError("para acceder a la informacion debes estar logueado");
  }

  const onClearDate = () => {
    setStartDate(null);
    setEndDate(null);
  };
  return (
    <Styles>
      <Layout>
        <form onSubmit={handleSubmit} id="buscador">
          <div class="row align-items-center">
            <div class="col-md-4">
              <label htmlFor="nombre">Nombre</label>
              <input
                onChange={handleChange}
                name="nombre"
                id="nombre"
                type="text"
              />
            </div>
            <div class="col-md-3">
              <DatePicker
                id="fechas"
                name="fechas"
                selected={startDate}
                onChange={onChangeDate}
                startDate={startDate}
                endDate={endDate}
                selectsRange
                dateFormat="yyyy-MM-dd"
                placeholderText="Fechas Entrega"
                disabled={nombre.nombre !== ""}
              />
            </div>
            <div class="col-md-3">
              <Button onClick={() => onClearDate()} variant="danger">
                clear date
              </Button>
            </div>
            <div class="col-md-2">
              <Button type="submit" variant="secondary">Buscar</Button>
            </div>
          </div>
        </form>
        <div id="listado">
          {data !== [] &&
            data.map((ini) => {
              return (
                <ListGroup class="pb-5">
                  <ListGroup.Item
                    id="item"
                    class="d-flex justify-content-between align-items-center mb-3"
                  >
                    <div class="container">
                      <div class="row">
                        <div class="col-md-3">
                          <p> {ini.nombre} </p>
                        </div>
                        <div id="comment" class="col-md-6">
                          <p>{ini.fecha}</p>
                          <p>{ini.comentario}</p>
                        </div>
                        <div id="verMasButton" class="col-md-2">
                          {acceso ? 
                          <Button
                            id="verMasButton"
                            href={"/iniciativa?nombre=" + ini.nombre}
                          >
                            ver mas
                          </Button>
                          :
                          <Button
                            id="verMasButton"
                            onClick={() => {noPuede()}}
                          >
                            ver mas
                          </Button>
                          }
                        </div>
                      </div>
                    </div>
                  </ListGroup.Item>
                </ListGroup>
              );
            })}
        </div>
      </Layout>
      <Footer />
    </Styles>
  );
}

export default Iniciativas;

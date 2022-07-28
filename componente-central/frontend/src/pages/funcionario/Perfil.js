import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { Footer } from "../../components/Footer";
import user from "../../tests/ciudadano";
import Modal, { ModalProvider } from "styled-react-modal";
import { CgClose } from "react-icons/cg";
import { Noti, NotiError } from "../../components/Notification";
import { Button } from "react-bootstrap";
import { GiMagnifyingGlass } from "react-icons/gi";
import {
  getUsuario,
  deleteIniciativa,
  updateUsuario,
} from "../../services/Requests";
import { fetchUserID } from "../../services/Requests";

const StyledModal = Modal.styled`
  border-radius: 5px;
  padding: 1.5%;
  width: 25%;
  align-items: center;
  justify-content: center;
  background-color: white;
  overflow-y:inherit !important;

  .cuerpo{
    margin-bottom: 15px;
  }
  .abajo{
    text-align: right;
  }
  Button {
    margin-left: 5px;
  }
`;

const Styles = styled.div`
  body {
    background: rgb(99, 39, 120);
  }

  .form-control:focus {
    box-shadow: none;
    border-color: #ba68c8;
  }

  .profile-button {
    background: rgb(99, 39, 120);
    box-shadow: none;
    border: none;
  }

  .profile-button:hover {
    background: #682773;
  }

  .profile-button:focus {
    background: #682773;
    box-shadow: none;
  }

  .profile-button:active {
    background: #682773;
    box-shadow: none;
  }

  .back:hover {
    color: #682773;
    cursor: pointer;
  }

  .labels {
    font-size: 11px;
  }

  .add-experience:hover {
    background: #ba68c8;
    color: #fff;
    cursor: pointer;
    border: solid 1px #ba68c8;
  }
  #ini {
    margin-left: 5px;
    margin-right: 10px;
    border-radius: 0.25rem;
    background-color: white;
    &:focus {
      box-shadow: none;
      border-color: #e87121;
    }
  }
  .dirBtn {
    margin: 0;
    border: 0;
    padding: 0;
    background: hsl(216, 100, 50);
    border-radius: 50%;
    width: 50px;
    height: 50px;
    display: flex;
    flex-flow: column nowrap;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    transition: all 150ms;
    background-color: white;

    &:hover {
      transform: rotateZ(90deg);
      background-color: transparent;
      &:focus {
        background: hsl(216, 100, 40);
        box-shadow: none;
      }
    }
  }
  #iniciativas {
    height: 40rem;
    overflow: scroll;
  }
  #iniciativas::-webkit-scrollbar {
    display: none;
  }

  #misIniciativas {
    height: 18rem;
    overflow: scroll;
  }
  #misIniciativas::-webkit-scrollbar {
    display: none;
  }
  #procesos {
    height: 16rem;
    overflow: scroll;
  }
  #procesos::-webkit-scrollbar {
    display: none;
  }
  h6 {
    margin-top: 10px;
  }
`;

export default function Perfil() {
  const usuario = fetchUserID();
  const [funcionario, setFuncionario] = useState({
    id: "",
    nombre: "",
    cedula: "",
    correo: "",
    fnac: "",
    organismo: "",
    nacionalidad: "",
    cargo: "",
    domicilio: "",
    procesoCreados: [],
  });

  //le agregamos la contraseña cuando tengamos el login
  const [datosFuncionario, setDatosFuncionario] = useState({
    id: funcionario.id,
    cedula: funcionario.cedula,
    nombreCompleto: "",
    correo: "",
    nacionalidad: funcionario.nacionalidad,
    domicilio: "",
    rol: "Funcionario",
    fnac: funcionario.fnac,
  });

  // eslint-disable-next-line no-unused-vars
  const [recurso, setRecurso] = useState();

  const handleChange = (e) => {
    e.persist();
    setDatosFuncionario((datosFuncionario) => ({
      ...datosFuncionario,
      [e.target.name]: e.target.value,
    }));
  };

  const handleUpload = (data) => {
    setRecurso(data.target.files[0]);
  };

  const [eliminarOpen, isEliminarOpen] = useState();
  const [proceso, setProceso] = useState();

  useEffect(() => {
    getUsuario(usuario)
      .then((response) => {
        if (response.status === 200) {
          console.log(response.data);
          setFuncionario(response.data);
        } else {
          console.log("error");
        }
      })
      .catch((error) => {
        NotiError(error.response.data);
      });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  const eliminarProceso = () => {
    isEliminarOpen(!eliminarOpen);
  };

  const onDelete = (ini) => {
    deleteIniciativa(ini).then(() => {
      setTimeout(() => {
        window.location.reload();
      }, 3000);
    });
  };

  const onGuardar = (e) => {
    e.preventDefault();
    updateUsuario(datosFuncionario).then((response) => {
      if (response.status === 200) {
        Noti("datos guardados con exito!!");
        setTimeout(() => {
          window.location.replace("/");
        }, 3000);
      } else {
        NotiError("Algo salio mal!!");
      }
    });
  };

  //logica para los parametros que se modifican una unica vez

  return (
    <Styles>
      <div class="container rounded bg-white mt-5 mb-5">
        <div class="row">
          <div class="col-md-4 border-right">
            <div class="d-flex flex-column align-items-center text-center p-3 py-5">
              <img
                class="rounded-circle mt-5"
                width="150px"
                src={user.imagen}
                alt="nose"
              />
              <span class="font-weight-bold">{funcionario.nombre}</span>
              <span class="font-weight-bold">{funcionario.fnac}</span>
              <span class="text-black-50">{funcionario.correo}</span>
              <span class="text-black-50">{funcionario.nacionalidad}</span>
              <span class="text-black-50">{funcionario.organismo}</span>
            </div>
          </div>
          <form onSubmit={onGuardar} class="col-md-4 border-right">
            <div>
              <div class="p-3 py-5">
                <div class="d-flex justify-content-between align-items-center mb-3">
                  <h4 class="text-right">Mi Perfil</h4>
                </div>
                <div class="row mt-2">
                  <div class="col-md-12">
                    <label class="labels">Nombre</label>
                    <input
                      type="text"
                      name="nombreCompleto"
                      onChange={handleChange}
                      class="form-control"
                      placeholder={funcionario.nombre}
                    />
                  </div>
                </div>
                <div class="row mt-3">
                  <div class="col-md-12">
                    <label class="labels">Domicilio</label>
                    <input
                      type="text"
                      name="domicilio"
                      onChange={handleChange}
                      class="form-control"
                      placeholder={funcionario.domicilio}
                    />
                  </div>
                  <div class="col-md-12">
                    <label class="labels">Email</label>
                    <input
                      type="email"
                      name="correo"
                      onChange={handleChange}
                      class="form-control"
                      placeholder={funcionario.correo}
                    />
                  </div>
                </div>
                <div class="row mt-3">
                  <div class="col-md-12">
                    <label class="labels">Imagen de perfil</label>
                    <input
                      onChange={handleUpload}
                      type="file"
                      class="form-control"
                    />
                  </div>
                </div>
                <div class="mt-5 text-center">
                  <button class="btn btn-primary profile-button" type="submit">
                    Guardar
                  </button>
                </div>
              </div>
            </div>
          </form>
          <div class="col-md-4">
            <div class="p-3 py-5">
              <div class="d-flex justify-content-between align-items-center">
                <h5>Mis Procesos</h5>
              </div>
              <br />
              <div id="misIniciativas" class="col-md-12">
                {funcionario.procesoCreados.map((proc, index) => {
                  return (
                    <div
                      className="d-flex justify-content-between align-items-center"
                      key={index}
                    >
                      <span className="numero">{index + 1}-</span>
                      <input
                        className="form-control"
                        id="proc"
                        value={proc}
                        readOnly
                      />
                      <Button
                        className="dirBtn"
                        href={"/proceso?nombre=" + proc}
                      >
                        <GiMagnifyingGlass color="#3d3d3d" fontSize="1.5rem" />
                      </Button>
                      <Button className="dirBtn">
                        <CgClose
                          color="#3d3d3d"
                          fontSize="1.5rem"
                          onClick={() => {
                            setProceso(proc);
                            eliminarProceso();
                          }}
                        />
                      </Button>
                    </div>
                  );
                })}
              </div>
            </div>
            <br />
          </div>
        </div>
      </div>
      <ModalProvider>
        <StyledModal
          isOpen={eliminarOpen}
          onBackgroundClick={eliminarProceso}
          onEscapeKeydown={eliminarProceso}
        >
          <h4>Eliminar {proceso}</h4>
          <hr />
          <div className="cuerpo">
            <h6>Seguro que quieres eliminar la iniciativa {proceso}?</h6>
          </div>
          <div className="abajo">
            <Button variant="secondary" onClick={eliminarProceso}>
              Cancelar
            </Button>
            <Button
              variant="danger"
              onClick={() => {
                onDelete(proceso);
                eliminarProceso();
              }}
            >
              Eliminar
            </Button>
          </div>
        </StyledModal>
      </ModalProvider>
      <Footer />
    </Styles>
  );
}

import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { Footer } from "../../components/Footer";
import picture from "../../assets/perfilUnico.png";
import Modal, { ModalProvider } from "styled-react-modal";
import { AiOutlineStop } from "react-icons/ai";
import { CgClose } from "react-icons/cg";
import { Noti, NotiError } from "../../components/Notification";
import { Button } from "react-bootstrap";
import { 
  getUsuario, 
  updateUsuario, 
  dejarSeguirAIniciativa, 
  dejarSeguirAProceso,
  eliminarMensaje,
  eliminarMensajes,
  desadherirAIniciativa
 } from "../../services/Requests";
import { GiMagnifyingGlass } from "react-icons/gi";
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
  #misCertificados {
    height: 10rem;
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
  .column {
    float: left;
    width: 33.33%;
    padding: 5px;
  }
  
  .row::after {
    content: "";
    clear: both;
    display: table;
  }

  .insignia {
    width: 80px;
    height: 80px;
  }
`;

export default function Perfil() {
  const usuario = fetchUserID();
  const [ciudadano, setCiudadano] = useState({
    id: "",
    cedula: "",
    nombreCompleto: "",
    correo: "",
    imagen: "",
    fechaNac: "",
    nacionalidad: "",
    domicilio: "",
    iniciativasCreadas: [],
    iniciativasAdheridas: [],
    iniciativasSeguidas: [],
    procesosSeguidos: [],
    certificados: [],
    mensajes: [],
  });

  const [datosCiudadano, setDatosCiudadano] = useState({
    id: ciudadano.id,
    cedula: "",
    correo: "",
    nacionalidad: ciudadano.nacionalidad,
    domicilio: "",
    rol: "Ciudadano",
    fnac: ciudadano.fnac,
  });

  const handleChange = (e) => {
    e.persist();
    setDatosCiudadano((datosCiudadano) => ({
      ...datosCiudadano,
      [e.target.name]: e.target.value,
    }));
  };
  const [desIni, isDeshadedirOpen] = useState();
  const [unfollowOpen, isUnfollowOpen] = useState();
  const [iniciativa, setIniciativa] = useState();
  const [proceso, setProceso] = useState();
  const [unfollowProc, isUnfollowProcOpen] = useState();
  const [deleteMensajeOpen, isDeleteMensajeOpen] = useState();
  const [deleteMensajesOpen, isDeleteMensajesOpen] = useState();
  const [mensaje, setMensaje] = useState();

   useEffect(() => {
    getUsuario(usuario)
      .then((response) => {
        if (response.status === 200) {
          console.log(response.data);
          setCiudadano(response.data);
        } else {
          console.log("error");
        }
      })
      .catch((error) => {
        NotiError(error.response.data);
      });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const unfollowIniciativa = () => {
    isUnfollowOpen(!unfollowOpen);
  };

  const deshaderirIni = () => {
    isDeshadedirOpen(!desIni);
  };

  const unfollowProceso = () => {
    isUnfollowProcOpen(!unfollowProc);
  };

  const deleteMensaje = () => {
    isDeleteMensajeOpen(!deleteMensajeOpen);
  };

  const deleteMensajes = () => {
    isDeleteMensajesOpen(!deleteMensajesOpen);
  };

  const onDeleteMensaje = () => {
    eliminarMensaje(usuario, mensaje)
      .then((res) => {
        console.log(res);
        window.location.reload();
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const onDeleteMensajes = () => {
    eliminarMensajes(usuario)
      .then((res) => {
        console.log(res);
        window.location.reload();
      })
      .catch((err) => {
        console.log(err);
      })
  };

  const onUnfollow = () => {
    dejarSeguirAIniciativa(iniciativa, usuario)
      .then((res) => {
        console.log(res);
        window.location.reload();
      })
      .catch((err) => {
        console.log(err.data);
      });
  };

  const onUnfollowProceso = () => {
    dejarSeguirAProceso(proceso, usuario)
      .then((res) => {
        console.log(res);
        window.location.reload();
      })
      .catch((err) => {
        console.log(err.data);
      });
  };

  
  const onDeshadedirIniciativa = () => {
    desadherirAIniciativa(iniciativa, usuario)
      .then((res) => {
        console.log(res);
        window.location.reload();
      })
      .catch((err) => {
        console.log(err.data);
      });
  };

  const onGuardar = (e) => {
    e.preventDefault();
    updateUsuario(datosCiudadano).then((response) => {
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

  var foto = null;
  var fb = sessionStorage.getItem("facebookLogin") === "true";
  if (fb === true) {
    foto = sessionStorage.getItem("fbPicture");
  } else {
    foto = picture;
  }

  const ciudadanoModificoEmail = () => {
    const mailFirst = ciudadano.correo.split("-");
    const defaultMail = mailFirst[0];
    if(defaultMail === "default") {
      return false;
    } else {
      return true;
    }
  };

  const ciudadanoModificoCedula = () => {
    console.log(ciudadano.cedula);   
    const cedulaFinal = ciudadano.cedula.slice(0,3);
    console.log(cedulaFinal);   
    if(cedulaFinal === '000') {
      return false;
    } else {
      return true;
    }
  };

  return (
    <Styles>
      <div class="container rounded bg-white mt-5 mb-5">
        <div class="row">
          <div class="col-md-4 border-right">
            <div class="d-flex flex-column align-items-center text-center p-3 py-1">
              <div class="p-2 py-1">
                <div class="d-flex justify-content-between align-items-center mb-3">
                  <h1>Certificados</h1>
                </div>
                <div id="misCertificados" class="col-md-12">
                {ciudadano.certificados !== [] ?
                  <div class="row">
                    {ciudadano.certificados.map((c, index) => {
                      return (
                          <div class="column" key={index}>
                            <img src={"data:image/png;base64," + c.insignia} className="insignia" alt={c.nombre} />
                          </div>
                      );
                    })}
                  </div>
                 : <h3>No tienes ningun certificado</h3>}
                </div>
              </div>
              <img
                class="rounded-circle mt-5"
                width="150px"
                src={foto}
                alt="fotito"           
              />
              <span class="font-weight-bold">{ciudadano.nombre}</span>
              <span class="font-weight-bold">{ciudadano.fNac}</span>
              <span class="text-black-50">{ciudadano.correo}</span>
              <span class="text-black-50">{ciudadano.nacionalidad}</span>
            </div>
            <div class="d-flex justify-content-between align-items-center experience">
              <h5>Mis Iniciativas</h5>
            </div>
            <br />
            <div id="misIniciativas" class="col-md-12">
              {ciudadano.iniciativasCreadas.map((ini, index) => {
                return (
                  <div
                    className="d-flex justify-content-between align-items-center"
                    key={index}
                  >
                    <span className="numero">{index + 1}-</span>
                    <input
                      className="form-control"
                      id="ini"
                      value={ini}
                      readOnly
                    />

                    <Button
                      className="dirBtn"
                      href={"/iniciativa?nombre=" + ini}
                    >
                      <GiMagnifyingGlass color="#3d3d3d" fontSize="1.5rem" />
                    </Button>
                  </div>
                );
              })}
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
                      class="form-control"
                      placeholder={ciudadano.nombre}
                      readOnly
                    />
                  </div>
                </div>
                <div class="row mt-3">
                  <div class="col-md-12">
                    <label class="labels">Departamento</label>
                    <input
                      type="text"
                      name="domicilio"
                      onChange={handleChange}
                      class="form-control"
                      placeholder={ciudadano.domicilio}
                    />
                  </div>
                  {fb ? 
                  <div class="col-md-12">
                  <label class="labels">Cedula</label>
                  {!ciudadanoModificoCedula() ?
                  <input
                    type="text"
                    name="cedula"
                    onChange={handleChange}
                    class="form-control"
                    placeholder={ciudadano.cedula}
                  />
                  :
                  <output
                    type="text"
                    name="cedula"
                    class="form-control"
                  >
                    {ciudadano.cedula}  
                  </output>
                  }
                  </div>
                  :
                  <div class="col-md-12">
                  <label class="labels">Email</label>
                  {ciudadanoModificoEmail() === false ?
                  <input
                    type="email"
                    name="correo"
                    onChange={handleChange}
                    class="form-control"
                    placeholder={ciudadano.correo}
                  />
                  :
                  <output
                    type="email"
                    name="correo"
                    class="form-control"
                  >
                    {ciudadano.correo}
                  </output>
                  }
                  </div>
                  }
                </div>
                <div class="mt-5 text-center">
                  <button class="btn btn-primary profile-button" type="submit">
                    Guardar
                  </button>
                </div>
              </div>
              <div class="p-3 py-5">
                <div class="d-flex justify-content-between align-items-center mb-3">
                  <Button align="center" variant="danger" onClick={() => {deleteMensajes();}}>Eliminar todos los mensajes</Button>
                </div>
                <br />
              <div id="iniciativas Seg" class="col-md-12">
                {ciudadano.mensajes.map((m, index) => {
                  return (
                    <div
                      className="d-flex justify-content-between align-items-center"
                      key={index}
                    >
                      <span className="numero">{index + 1}-</span>
                      <input
                        className="form-control"
                        id="ini"
                        value={m}
                        readOnly
                      />
                      <button
                        className="dirBtn"
                        onClick={() => {
                          setMensaje(m);
                          deleteMensaje();
                        }}
                      >
                        <CgClose color="#3d3d3d" fontSize="1.5rem" />
                      </button>
                    </div>
                  );
                })}
              </div>
              </div>
            </div>
          </form>
          <div class="col-md-4">
            <div class="p-3 py-5">
              <div class="d-flex justify-content-between align-items-center">
                <h5>Iniciativas seguidas</h5>
              </div>
              <br />
              <div id="iniciativas Seg" class="col-md-12">
                {ciudadano.iniciativasSeguidas.map((ini, index) => {
                  return (
                    <div
                      className="d-flex justify-content-between align-items-center"
                      key={index}
                    >
                      <span className="numero">{index + 1}-</span>
                      <input
                        className="form-control"
                        id="ini"
                        value={ini}
                        readOnly
                      />
                      <button
                        className="dirBtn"
                        onClick={() => {
                          setIniciativa(ini);
                          unfollowIniciativa();
                        }}
                      >
                        <AiOutlineStop color="#3d3d3d" fontSize="1.5rem" />
                      </button>
                    </div>
                  );
                })}
              </div>
            </div>
            <br />
            <div class="p-3 py-5">
              <div class="d-flex justify-content-between align-items-center">
                <h5>Iniciativas adheridas</h5>
              </div>
              <br />
              <div id="iniciativas Seg" class="col-md-12">
                {ciudadano.iniciativasAdheridas.map((ini, index) => {
                  return (
                    <div
                      className="d-flex justify-content-between align-items-center"
                      key={index}
                    >
                      <span className="numero">{index + 1}-</span>
                      <input
                        className="form-control"
                        id="ini"
                        value={ini}
                        readOnly
                      />
                      <button
                        className="dirBtn"
                        onClick={() => {
                          setIniciativa(ini);
                          deshaderirIni();
                        }}
                      >
                        <AiOutlineStop color="#3d3d3d" fontSize="1.5rem" />
                      </button>
                    </div>
                  );
                })}
              </div>              
            </div>
            <br/>
            <div class="p-3 py-5">
              <div class="d-flex justify-content-between align-items-center">
                <h5>Procesos Seguidos</h5>
              </div>
              <br />
              <div id="iniciativas Seg" class="col-md-12">
                {ciudadano.procesosSeguidos.map((ps, index) => {
                  return (
                    <div
                      className="d-flex justify-content-between align-items-center"
                      key={index}
                    >
                      <span className="numero">{index + 1}-</span>
                      <input
                        className="form-control"
                        id="ini"
                        value={ps}
                        readOnly
                      />
                      <button
                        className="dirBtn"
                        onClick={() => {
                          setProceso(ps);
                          unfollowProceso();
                        }}
                      >
                        <AiOutlineStop color="#3d3d3d" fontSize="1.5rem" />
                      </button>
                    </div>
                  );
                })}
              </div>              
            </div>
          </div>
        </div>
      </div>
      <ModalProvider>
        <StyledModal
          isOpen={unfollowOpen}
          onBackgroundClick={unfollowIniciativa}
          onEscapeKeydown={unfollowIniciativa}
        >
          <h4>Dejar de seguir {iniciativa}</h4>
          <hr />
          <div className="cuerpo">
            <h6>
              Seguro que quieres dejar de seguir la iniciativa {iniciativa}?
            </h6>
          </div>
          <div className="abajo">
            <Button variant="secondary" onClick={unfollowIniciativa}>
              Cancelar
            </Button>
            <Button
              variant="danger"
              onClick={() => {
                onUnfollow();
                unfollowIniciativa();
              }}
            >
              Confirmar
            </Button>
          </div>
        </StyledModal>
        <StyledModal
          isOpen={unfollowProc}
          onBackgroundClick={unfollowProceso}
          onEscapeKeydown={unfollowProceso}
        >
          <h4>Dejar de seguir {proceso}</h4>
          <hr />
          <div className="cuerpo">
            <h6>
              Seguro que quieres dejar de seguir al proceso {proceso}?
            </h6>
          </div>
          <div className="abajo">
            <Button variant="secondary" onClick={unfollowProceso}>
              Cancelar
            </Button>
            <Button
              variant="danger"
              onClick={() => {
                onUnfollowProceso();
                unfollowProceso();
              }}
            >
              Confirmar
            </Button>
          </div>
        </StyledModal>
        <StyledModal
          isOpen={desIni}
          onBackgroundClick={deshaderirIni}
          onEscapeKeydown={onDeshadedirIniciativa}
        >
          <h4>Dejar de seguir {iniciativa}</h4>
          <hr />
          <div className="cuerpo">
            <h6>
              Seguro que quieres deshaderirte a la iniciativa {iniciativa}?
            </h6>
          </div>
          <div className="abajo">
            <Button variant="secondary" onClick={deshaderirIni}>
              Cancelar
            </Button>
            <Button
              variant="danger"
              onClick={() => {
                onDeshadedirIniciativa();
                deshaderirIni();
              }}
            >
              Confirmar
            </Button>
          </div>
        </StyledModal>



        <StyledModal
          isOpen={deleteMensajeOpen}
          onBackgroundClick={deleteMensaje}
          onEscapeKeydown={deleteMensaje}
        >
          <h4>Se eliminar el mensaje</h4>
          <hr />
          <div className="cuerpo">
            <h6>
              Seguro que quieres eliminar este mensaje?
            </h6>
          </div>
          <div className="abajo">
            <Button variant="secondary" onClick={deleteMensaje}>
              Cancelar
            </Button>
            <Button
              variant="danger"
              onClick={() => {
                onDeleteMensaje();
                deleteMensaje();
              }}
            >
              Confirmar
            </Button>
          </div>
        </StyledModal>
        <StyledModal
          isOpen={deleteMensajesOpen}
          onBackgroundClick={deleteMensajes}
          onEscapeKeydown={deleteMensajes}
        >
          <h4>Se eliminaran todos los mensajes</h4>
          <hr />
          <div className="cuerpo">
            <h6>
              Seguro que quieres eliminar todos tus mensajes?
            </h6>
          </div>
          <div className="abajo">
            <Button variant="secondary" onClick={deleteMensajes}>
              Cancelar
            </Button>
            <Button
              variant="danger"
              onClick={() => {
                onDeleteMensajes();
                deleteMensajes();
              }}
            >
              Confirmar
            </Button>
          </div>
        </StyledModal>
      </ModalProvider>
      <Footer />
    </Styles>
  );
}

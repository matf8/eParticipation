import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export const Noti = (mensaje) =>
  toast.success(mensaje, {
    position: "top-right",
    autoClose: 8000,
    hideProgressBar: false,
    closeOnClick: true,
    pauseOnHover: true,
    pauseOnFocusLoss: false,
    draggable: true,
    progress: undefined,
  });

export const NotiError = (mensaje) =>
  toast.error(mensaje, {
    position: "top-right",
    autoClose: 8000,
    hideProgressBar: false,
    closeOnClick: true,
    pauseOnHover: true,
    pauseOnFocusLoss: false,
    draggable: true,
    progress: undefined,
  });

export const NotiBienvenida = (mensaje) =>
  toast.info(mensaje, {
    position: "top-center",
    autoClose: 8000,
    hideProgressBar: false,
    closeOnClick: true,
    pauseOnHover: true,
    draggable: true,
    progress: undefined,
  });

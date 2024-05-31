package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ControladorPrincipal {

    @FXML
    private Label mensajeBienvenida;

    @FXML
    private Button botonGestionUsuarios;
    @FXML
    private Button botonGestionRoles;
    @FXML
    private Button botonGestionTarifas;
    @FXML
    private Button botonGestionReportes;
    @FXML
    private Button botonGestionMantenimiento;
    @FXML
    private Button botonGestionPagos;
    @FXML
    private Button botonGestionSanciones;
    @FXML
    private Button botonGestionNotificaciones;
    @FXML
    private Button botonGestionEventos;
    @FXML
    private Button botonGestionTarjetas;
    @FXML
    private Button botonGestionIncidencias;
    @FXML
    private Button botonGestionEntradasSalidas;
    @FXML
    private Button botonGestionRegistros;
    @FXML
    private Button botonGestionPlazasAparcamiento;
    @FXML
    private Button botonGestionReservas;
    @FXML
    private Button botonCerrarSesion;

    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        mensajeBienvenida.setText("Bienvenido, " + usuario.getNombre() + " " + usuario.getApellidos());
    }

    @FXML
    public void initialize() {
        // El método initialize se llamará automáticamente después de la inyección de FXML, así que puedes dejarlo vacío si no tienes inicializaciones adicionales.
    }

    @FXML
    public void handleGestionUsuariosButton(ActionEvent event) {
        cambiarEscena("/com/tfg/parkplatesystem/fxml/usuarios.fxml", "Park Plate System - Gestión de Usuarios");
    }

    @FXML
    public void handleGestionRolesButton(ActionEvent event) {
        cambiarEscena("/com/tfg/parkplatesystem/fxml/roles.fxml", "Park Plate System - Gestión de Roles");
    }

    @FXML
    public void handleGestionTarifasButton(ActionEvent event) {
        cambiarEscena("/com/tfg/parkplatesystem/fxml/tarifas.fxml", "Park Plate System - Gestión de Tarifas");
    }

    @FXML
    public void handleGestionReportesButton(ActionEvent event) {
        cambiarEscena("/com/tfg/parkplatesystem/fxml/reportes.fxml", "Park Plate System - Gestión de Reportes");
    }

    @FXML
    public void handleGestionMantenimientoButton(ActionEvent event) {
        cambiarEscena("/com/tfg/parkplatesystem/fxml/mantenimiento.fxml", "Park Plate System - Gestión de Mantenimiento");
    }

    @FXML
    public void handleGestionPagosButton(ActionEvent event) {
        cambiarEscena("/com/tfg/parkplatesystem/fxml/pagos.fxml", "Park Plate System - Gestión de Pagos");
    }

    @FXML
    public void handleGestionSancionesButton(ActionEvent event) {
        cambiarEscena("/com/tfg/parkplatesystem/fxml/sanciones.fxml", "Park Plate System - Gestión de Sanciones");
    }

    @FXML
    public void handleGestionNotificacionesButton(ActionEvent event) {
        cambiarEscena("/com/tfg/parkplatesystem/fxml/notificaciones.fxml", "Park Plate System - Gestión de Notificaciones");
    }

    @FXML
    public void handleGestionEventosButton(ActionEvent event) {
        cambiarEscena("/com/tfg/parkplatesystem/fxml/eventos.fxml", "Park Plate System - Gestión de Eventos");
    }

    @FXML
    public void handleGestionTarjetasButton(ActionEvent event) {
        cambiarEscena("/com/tfg/parkplatesystem/fxml/tarjetas.fxml", "Park Plate System - Gestión de Tarjetas");
    }

    @FXML
    public void handleGestionIncidenciasButton(ActionEvent event) {
        cambiarEscena("/com/tfg/parkplatesystem/fxml/incidencias.fxml", "Park Plate System - Gestión de Incidencias");
    }

    @FXML
    public void handleGestionEntradasSalidasButton(ActionEvent event) {
        cambiarEscena("/com/tfg/parkplatesystem/fxml/entradaSalida.fxml", "Park Plate System - Gestión de Entradas y Salidas");
    }

    @FXML
    public void handleGestionRegistrosButton(ActionEvent event) {
        cambiarEscena("/com/tfg/parkplatesystem/fxml/registro.fxml", "Park Plate System - Gestión de Registros");
    }

    @FXML
    public void handleGestionPlazasAparcamientoButton(ActionEvent event) {
        cambiarEscena("/com/tfg/parkplatesystem/fxml/plazas.fxml", "Park Plate System - Gestión de Plazas de Aparcamiento");
    }

    @FXML
    public void handleGestionReservasButton(ActionEvent event) {
        cambiarEscena("/com/tfg/parkplatesystem/fxml/reservas.fxml", "Park Plate System - Gestión de Reservas");
    }

    @FXML
    public void handleCerrarSesionButton(ActionEvent event) {
        try {
            Stage stage = (Stage) mensajeBienvenida.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/tfg/parkplatesystem/fxml/inicioSesion.fxml")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Park Plate System - Inicio de Sesión");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cambiarEscena(String fxmlPath, String titulo) {
        try {
            Stage stage = (Stage) mensajeBienvenida.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Pasa el usuario al controlador de la nueva escena
            Object controlador = loader.getController();
            if (controlador instanceof ControladorPrincipal) {
                ((ControladorPrincipal) controlador).setUsuario(usuario);
            } else if (controlador instanceof ControladorPlazasAparcamiento) {
                ((ControladorPlazasAparcamiento) controlador).setUsuario(usuario);
            } else if (controlador instanceof ControladorPagos) {
                ((ControladorPagos) controlador).setUsuario(usuario);
            } else if (controlador instanceof ControladorSanciones) {
                ((ControladorSanciones) controlador).setUsuario(usuario);
            } else if (controlador instanceof ControladorNotificaciones) {
                ((ControladorNotificaciones) controlador).setUsuario(usuario);
            } else if (controlador instanceof ControladorEventos) {
                ((ControladorEventos) controlador).setUsuario(usuario);
            } else if (controlador instanceof ControladorTarjetas) {
                ((ControladorTarjetas) controlador).setUsuario(usuario);
            } else if (controlador instanceof ControladorIncidencias) {
                ((ControladorIncidencias) controlador).setUsuario(usuario);
            } else if (controlador instanceof ControladorEntradasSalidas) {
                ((ControladorEntradasSalidas) controlador).setUsuario(usuario);
            } else if (controlador instanceof ControladorRegistro) {
                ((ControladorRegistro) controlador).setUsuario(usuario);
            } else if (controlador instanceof ControladorUsuarios) {
                ((ControladorUsuarios) controlador).setUsuario(usuario);
            } else if (controlador instanceof ControladorRoles) {
                ((ControladorRoles) controlador).setUsuario(usuario);
            } else if (controlador instanceof ControladorTarifas) {
                ((ControladorTarifas) controlador).setUsuario(usuario);
            } else if (controlador instanceof ControladorReportes) {
                ((ControladorReportes) controlador).setUsuario(usuario);
            } else if (controlador instanceof ControladorMantenimiento) {
                ((ControladorMantenimiento) controlador).setUsuario(usuario);
            } else if (controlador instanceof ControladorReservas) {
                ((ControladorReservas) controlador).setUsuario(usuario);
            }

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

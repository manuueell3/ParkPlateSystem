package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    private Button botonGestionPlazasAparcamiento;
    @FXML
    private Button botonGestionReservas;
    @FXML
    private Button botonCerrarSesion;

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

    private Usuario usuario;
    private boolean controlPressed = false;
    private Map<KeyCode, Button> keyButtonMap;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        mensajeBienvenida.setText("Bienvenido, " + usuario.getNombre() + " " + usuario.getApellidos());
        initializeKeyButtonMap();
    }

    private void initializeKeyButtonMap() {
        keyButtonMap = new HashMap<>();
        // Verifica si los botones son no nulos antes de agregarlos al mapa
        if (botonGestionUsuarios != null) keyButtonMap.put(KeyCode.U, botonGestionUsuarios);
        if (botonGestionRoles != null) keyButtonMap.put(KeyCode.R, botonGestionRoles);
        if (botonGestionTarifas != null) keyButtonMap.put(KeyCode.T, botonGestionTarifas);
        if (botonGestionReportes != null) keyButtonMap.put(KeyCode.P, botonGestionReportes);
        if (botonGestionMantenimiento != null) keyButtonMap.put(KeyCode.M, botonGestionMantenimiento);
        if (botonGestionPlazasAparcamiento != null) keyButtonMap.put(KeyCode.A, botonGestionPlazasAparcamiento);
        if (botonGestionReservas != null) keyButtonMap.put(KeyCode.V, botonGestionReservas);
        if (botonCerrarSesion != null) keyButtonMap.put(KeyCode.S, botonCerrarSesion);

        if (botonGestionPagos != null) keyButtonMap.put(KeyCode.G, botonGestionPagos);
        if (botonGestionSanciones != null) keyButtonMap.put(KeyCode.Y, botonGestionSanciones);
        if (botonGestionNotificaciones != null) keyButtonMap.put(KeyCode.N, botonGestionNotificaciones);
        if (botonGestionEventos != null) keyButtonMap.put(KeyCode.E, botonGestionEventos);
        if (botonGestionTarjetas != null) keyButtonMap.put(KeyCode.C, botonGestionTarjetas);
        if (botonGestionIncidencias != null) keyButtonMap.put(KeyCode.I, botonGestionIncidencias);
        if (botonGestionEntradasSalidas != null) keyButtonMap.put(KeyCode.O, botonGestionEntradasSalidas);
        if (botonGestionRegistros != null) keyButtonMap.put(KeyCode.D, botonGestionRegistros);
    }

    @FXML
    public void manejarPresionTecla(KeyEvent event) {
        if (event.getCode() == KeyCode.CONTROL) {
            controlPressed = true;
            subrayarAtajos(true);
        } else if (controlPressed && keyButtonMap.containsKey(event.getCode())) {
            keyButtonMap.get(event.getCode()).fire();
        }
    }

    @FXML
    public void limpiarSubrayado(KeyEvent event) {
        if (event.getCode() == KeyCode.CONTROL) {
            controlPressed = false;
            subrayarAtajos(false);
        }
    }

    private void subrayarAtajos(boolean subrayar) {
        for (Map.Entry<KeyCode, Button> entry : keyButtonMap.entrySet()) {
            String estilo = subrayar ? "-fx-underline: true;" : "";
            entry.getValue().setStyle(estilo);
        }
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
    public void handleGestionPlazasAparcamientoAdminButton(ActionEvent event) {
        cambiarEscena("/com/tfg/parkplatesystem/fxml/plazasAdmin.fxml", "Park Plate System - Gestión de Plazas de Aparcamiento (Admin)");
    }

    @FXML
    public void handleGestionReservasAdminButton(ActionEvent event) {
        cambiarEscena("/com/tfg/parkplatesystem/fxml/reservasAdmin.fxml", "Park Plate System - Gestión de Reservas (Admin)");
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
    public void handleGestionPlazasAparcamientoUsuarioButton(ActionEvent event) {
        cambiarEscena("/com/tfg/parkplatesystem/fxml/plazasUsuario.fxml", "Park Plate System - Gestión de Plazas de Aparcamiento (Usuario)");
    }

    @FXML
    public void handleGestionReservasUsuarioButton(ActionEvent event) {
        cambiarEscena("/com/tfg/parkplatesystem/fxml/reservasUsuario.fxml", "Park Plate System - Gestión de Reservas (Usuario)");
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
            } else if (controlador instanceof ControladorPlazasAdmin) {
                ((ControladorPlazasAdmin) controlador).setUsuario(usuario);
            } else if (controlador instanceof ControladorPlazasUsuario) {
                ((ControladorPlazasUsuario) controlador).setUsuario(usuario);
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
            } else if (controlador instanceof ControladorReservasAdmin) {
                ((ControladorReservasAdmin) controlador).setUsuario(usuario);
            } else if (controlador instanceof ControladorReservasUsuario) {
                ((ControladorReservasUsuario) controlador).setUsuario(usuario);
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
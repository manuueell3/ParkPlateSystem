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
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
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
        // Escuchar cuando la escena esté completamente cargada
        mensajeBienvenida.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obs, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        // Añadir atajos de teclado a la escena
                        addKeyboardShortcuts(newScene);
                    }
                });
            }
        });
    }

    private void addKeyboardShortcuts(Scene scene) {
        // Crear combinaciones de teclas
        KeyCombination gestionUsuariosKeyCombination = new KeyCodeCombination(KeyCode.U, KeyCombination.CONTROL_DOWN);
        KeyCombination gestionRolesKeyCombination = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
        KeyCombination gestionTarifasKeyCombination = new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN);
        KeyCombination gestionReportesKeyCombination = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);
        KeyCombination gestionMantenimientoKeyCombination = new KeyCodeCombination(KeyCode.M, KeyCombination.CONTROL_DOWN);
        KeyCombination gestionPagosKeyCombination = new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN);
        KeyCombination gestionSancionesKeyCombination = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        KeyCombination gestionNotificacionesKeyCombination = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
        KeyCombination gestionEventosKeyCombination = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN);
        KeyCombination gestionTarjetasKeyCombination = new KeyCodeCombination(KeyCode.J, KeyCombination.CONTROL_DOWN);
        KeyCombination gestionIncidenciasKeyCombination = new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN);
        KeyCombination gestionEntradasSalidasKeyCombination = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);
        KeyCombination gestionRegistrosKeyCombination = new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN);
        KeyCombination gestionPlazasAparcamientoKeyCombination = new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN);
        KeyCombination gestionReservasKeyCombination = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);

        // Añadir manejadores de eventos de teclado a la escena
        scene.getAccelerators().put(gestionUsuariosKeyCombination, () -> {
            handleGestionUsuariosButton(new ActionEvent());
            underlineButtonText(botonGestionUsuarios, true);
        });
        scene.getAccelerators().put(gestionRolesKeyCombination, () -> {
            handleGestionRolesButton(new ActionEvent());
            underlineButtonText(botonGestionRoles, true);
        });
        scene.getAccelerators().put(gestionTarifasKeyCombination, () -> {
            handleGestionTarifasButton(new ActionEvent());
            underlineButtonText(botonGestionTarifas, true);
        });
        scene.getAccelerators().put(gestionReportesKeyCombination, () -> {
            handleGestionReportesButton(new ActionEvent());
            underlineButtonText(botonGestionReportes, true);
        });
        scene.getAccelerators().put(gestionMantenimientoKeyCombination, () -> {
            handleGestionMantenimientoButton(new ActionEvent());
            underlineButtonText(botonGestionMantenimiento, true);
        });
        scene.getAccelerators().put(gestionPagosKeyCombination, () -> {
            handleGestionPagosButton(new ActionEvent());
            underlineButtonText(botonGestionPagos, true);
        });
        scene.getAccelerators().put(gestionSancionesKeyCombination, () -> {
            handleGestionSancionesButton(new ActionEvent());
            underlineButtonText(botonGestionSanciones, true);
        });
        scene.getAccelerators().put(gestionNotificacionesKeyCombination, () -> {
            handleGestionNotificacionesButton(new ActionEvent());
            underlineButtonText(botonGestionNotificaciones, true);
        });
        scene.getAccelerators().put(gestionEventosKeyCombination, () -> {
            handleGestionEventosButton(new ActionEvent());
            underlineButtonText(botonGestionEventos, true);
        });
        scene.getAccelerators().put(gestionTarjetasKeyCombination, () -> {
            handleGestionTarjetasButton(new ActionEvent());
            underlineButtonText(botonGestionTarjetas, true);
        });
        scene.getAccelerators().put(gestionIncidenciasKeyCombination, () -> {
            handleGestionIncidenciasButton(new ActionEvent());
            underlineButtonText(botonGestionIncidencias, true);
        });
        scene.getAccelerators().put(gestionEntradasSalidasKeyCombination, () -> {
            handleGestionEntradasSalidasButton(new ActionEvent());
            underlineButtonText(botonGestionEntradasSalidas, true);
        });
        scene.getAccelerators().put(gestionRegistrosKeyCombination, () -> {
            handleGestionRegistrosButton(new ActionEvent());
            underlineButtonText(botonGestionRegistros, true);
        });
        scene.getAccelerators().put(gestionPlazasAparcamientoKeyCombination, () -> {
            handleGestionPlazasAparcamientoButton(new ActionEvent());
            underlineButtonText(botonGestionPlazasAparcamiento, true);
        });
        scene.getAccelerators().put(gestionReservasKeyCombination, () -> {
            handleGestionReservasButton(new ActionEvent());
            underlineButtonText(botonGestionReservas, true);
        });

        // Remover el subrayado cuando el atajo ya no esté activo (esto es opcional y puede variar según la necesidad)
        scene.setOnKeyReleased(event -> {
            if (!gestionUsuariosKeyCombination.match(event)) {
                underlineButtonText(botonGestionUsuarios, false);
            }
            if (!gestionRolesKeyCombination.match(event)) {
                underlineButtonText(botonGestionRoles, false);
            }
            if (!gestionTarifasKeyCombination.match(event)) {
                underlineButtonText(botonGestionTarifas, false);
            }
            if (!gestionReportesKeyCombination.match(event)) {
                underlineButtonText(botonGestionReportes, false);
            }
            if (!gestionMantenimientoKeyCombination.match(event)) {
                underlineButtonText(botonGestionMantenimiento, false);
            }
            if (!gestionPagosKeyCombination.match(event)) {
                underlineButtonText(botonGestionPagos, false);
            }
            if (!gestionSancionesKeyCombination.match(event)) {
                underlineButtonText(botonGestionSanciones, false);
            }
            if (!gestionNotificacionesKeyCombination.match(event)) {
                underlineButtonText(botonGestionNotificaciones, false);
            }
            if (!gestionEventosKeyCombination.match(event)) {
                underlineButtonText(botonGestionEventos, false);
            }
            if (!gestionTarjetasKeyCombination.match(event)) {
                underlineButtonText(botonGestionTarjetas, false);
            }
            if (!gestionIncidenciasKeyCombination.match(event)) {
                underlineButtonText(botonGestionIncidencias, false);
            }
            if (!gestionEntradasSalidasKeyCombination.match(event)) {
                underlineButtonText(botonGestionEntradasSalidas, false);
            }
            if (!gestionRegistrosKeyCombination.match(event)) {
                underlineButtonText(botonGestionRegistros, false);
            }
            if (!gestionPlazasAparcamientoKeyCombination.match(event)) {
                underlineButtonText(botonGestionPlazasAparcamiento, false);
            }
            if (!gestionReservasKeyCombination.match(event)) {
                underlineButtonText(botonGestionReservas, false);
            }
        });
    }

    private void underlineButtonText(Button button, boolean underline) {
        if (button != null) {
            button.setStyle(underline ? "-fx-underline: true;" : "-fx-underline: false;");
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
            } else if (controlador instanceof ControladorPlazas) {
                ((ControladorPlazas) controlador).setUsuario(usuario);
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

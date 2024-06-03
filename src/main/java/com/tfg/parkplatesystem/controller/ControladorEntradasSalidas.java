package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.RegistroEntradaSalida;
import com.tfg.parkplatesystem.model.Usuario;
import com.tfg.parkplatesystem.util.UtilMysql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControladorEntradasSalidas {

    @FXML
    private TableView<RegistroEntradaSalida> tablaEntradasSalidas;
    @FXML
    private TableColumn<RegistroEntradaSalida, Long> columnaIdRegistro;
    @FXML
    private TableColumn<RegistroEntradaSalida, Long> columnaIdVehiculo;
    @FXML
    private TableColumn<RegistroEntradaSalida, String> columnaFechaHoraEntrada;
    @FXML
    private TableColumn<RegistroEntradaSalida, String> columnaFechaHoraSalida;
    @FXML
    private TableColumn<RegistroEntradaSalida, Long> columnaIdPlaza;
    @FXML
    private TextField campoMatricula;
    @FXML
    private Button botonRegistrarEntrada;
    @FXML
    private Button botonRegistrarSalida;
    @FXML
    private Label labelEstadoPlazas;

    private ObservableList<RegistroEntradaSalida> entradasSalidasData;

    private static final Logger LOGGER = Logger.getLogger(ControladorEntradasSalidas.class.getName());

    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    private void initialize() {
        configurarTabla();
        cargarEntradasSalidas();
        actualizarEstadoPlazas();
    }

    private void configurarTabla() {
        columnaIdRegistro.setCellValueFactory(new PropertyValueFactory<>("idRegistro"));
        columnaIdVehiculo.setCellValueFactory(new PropertyValueFactory<>("idVehiculo"));
        columnaFechaHoraEntrada.setCellValueFactory(new PropertyValueFactory<>("fechaHoraEntrada"));
        columnaFechaHoraSalida.setCellValueFactory(new PropertyValueFactory<>("fechaHoraSalida"));
        columnaIdPlaza.setCellValueFactory(new PropertyValueFactory<>("idPlaza"));

        entradasSalidasData = FXCollections.observableArrayList();
        tablaEntradasSalidas.setItems(entradasSalidasData);
    }

    @FXML
    private void registrarEntrada() {
        String matricula = campoMatricula.getText();
        if (!esMatriculaValida(matricula)) {
            mostrarAlerta("Error", "La matrícula no es válida.", Alert.AlertType.ERROR);
            return;
        }

        try (Connection connection = UtilMysql.getConnection()) {
            String queryVehiculo = "SELECT id_vehiculo FROM Vehiculos WHERE matricula = ?";
            PreparedStatement stmtVehiculo = connection.prepareStatement(queryVehiculo);
            stmtVehiculo.setString(1, matricula);
            ResultSet rsVehiculo = stmtVehiculo.executeQuery();

            if (rsVehiculo.next()) {
                long idVehiculo = rsVehiculo.getLong("id_vehiculo");
                long idPlaza = obtenerPlazaDisponible();

                if (idPlaza == -1) {
                    mostrarAlerta("Error", "No hay plazas disponibles.", Alert.AlertType.ERROR);
                    return;
                }

                RegistroEntradaSalida registro = new RegistroEntradaSalida(null, idVehiculo, obtenerFechaHoraActual(), null, idPlaza);
                registro.guardar();
                actualizarEstadoPlaza(idPlaza, "ocupada");

                registrarEvento("Entrada registrada para vehículo con matrícula: " + matricula);

                mostrarAlerta("Éxito", "Entrada registrada correctamente.", Alert.AlertType.INFORMATION);
                cargarEntradasSalidas();
                actualizarEstadoPlazas();
            } else {
                mostrarAlerta("Error", "Vehículo no encontrado.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al registrar entrada", e);
            mostrarAlerta("Error", "Ocurrió un error al registrar la entrada.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void registrarSalida() {
        String matricula = campoMatricula.getText();
        if (!esMatriculaValida(matricula)) {
            mostrarAlerta("Error", "La matrícula no es válida.", Alert.AlertType.ERROR);
            return;
        }

        try (Connection connection = UtilMysql.getConnection()) {
            String queryVehiculo = "SELECT id_vehiculo FROM Vehiculos WHERE matricula = ?";
            PreparedStatement stmtVehiculo = connection.prepareStatement(queryVehiculo);
            stmtVehiculo.setString(1, matricula);
            ResultSet rsVehiculo = stmtVehiculo.executeQuery();

            if (rsVehiculo.next()) {
                long idVehiculo = rsVehiculo.getLong("id_vehiculo");

                String queryPlaza = "SELECT id_plaza FROM EntradasSalidas WHERE id_vehiculo = ? AND fecha_hora_salida IS NULL";
                PreparedStatement stmtPlaza = connection.prepareStatement(queryPlaza);
                stmtPlaza.setLong(1, idVehiculo);
                ResultSet rsPlaza = stmtPlaza.executeQuery();

                if (rsPlaza.next()) {
                    long idPlaza = rsPlaza.getLong("id_plaza");

                    String querySalida = "UPDATE EntradasSalidas SET fecha_hora_salida = NOW() WHERE id_vehiculo = ? AND fecha_hora_salida IS NULL";
                    PreparedStatement stmtSalida = connection.prepareStatement(querySalida);
                    stmtSalida.setLong(1, idVehiculo);

                    int filasAfectadas = stmtSalida.executeUpdate();
                    if (filasAfectadas > 0) {
                        actualizarEstadoPlaza(idPlaza, "disponible");
                        registrarEvento("Salida registrada para vehículo con matrícula: " + matricula);

                        mostrarAlerta("Éxito", "Salida registrada correctamente.", Alert.AlertType.INFORMATION);
                        cargarEntradasSalidas();
                        actualizarEstadoPlazas();
                    } else {
                        mostrarAlerta("Error", "No se encontró una entrada activa para el vehículo.", Alert.AlertType.ERROR);
                    }
                } else {
                    mostrarAlerta("Error", "No se encontró una entrada activa para el vehículo.", Alert.AlertType.ERROR);
                }
            } else {
                mostrarAlerta("Error", "Vehículo no encontrado.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al registrar salida", e);
            mostrarAlerta("Error", "Ocurrió un error al registrar la salida.", Alert.AlertType.ERROR);
        }
    }

    private void cargarEntradasSalidas() {
        entradasSalidasData.clear();
        try {
            List<RegistroEntradaSalida> registros = RegistroEntradaSalida.obtenerTodos();
            entradasSalidasData.addAll(registros);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar entradas y salidas", e);
            mostrarAlerta("Error", "Ocurrió un error al cargar los registros de entradas y salidas.", Alert.AlertType.ERROR);
        }
    }

    private long obtenerPlazaDisponible() {
        try (Connection connection = UtilMysql.getConnection()) {
            String queryPlaza = "SELECT id_plaza FROM Plazas WHERE estado = 'disponible' LIMIT 1";
            PreparedStatement stmtPlaza = connection.prepareStatement(queryPlaza);
            ResultSet rsPlaza = stmtPlaza.executeQuery();

            if (rsPlaza.next()) {
                return rsPlaza.getLong("id_plaza");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener plaza disponible", e);
        }
        return -1;
    }

    private void actualizarEstadoPlaza(long idPlaza, String estado) {
        try (Connection connection = UtilMysql.getConnection()) {
            String queryActualizarPlaza = "UPDATE Plazas SET estado = ? WHERE id_plaza = ?";
            PreparedStatement stmtActualizarPlaza = connection.prepareStatement(queryActualizarPlaza);
            stmtActualizarPlaza.setString(1, estado);
            stmtActualizarPlaza.setLong(2, idPlaza);
            stmtActualizarPlaza.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar estado de la plaza", e);
        }
    }

    private void actualizarEstadoPlazaPorVehiculo(long idVehiculo, String estado) {
        try (Connection connection = UtilMysql.getConnection()) {
            String query = "SELECT id_plaza FROM EntradasSalidas WHERE id_vehiculo = ? AND fecha_hora_salida IS NULL";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, idVehiculo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                long idPlaza = rs.getLong("id_plaza");
                actualizarEstadoPlaza(idPlaza, estado);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar estado de la plaza por vehículo", e);
        }
    }

    private void actualizarEstadoPlazas() {
        try (Connection connection = UtilMysql.getConnection()) {
            String query = "SELECT COUNT(*) AS total, " +
                    "SUM(CASE WHEN estado = 'disponible' THEN 1 ELSE 0 END) AS disponibles, " +
                    "SUM(CASE WHEN estado = 'ocupada' THEN 1 ELSE 0 END) AS ocupadas, " +
                    "SUM(CASE WHEN estado = 'reservada' THEN 1 ELSE 0 END) AS reservadas " +
                    "FROM Plazas";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                int disponibles = rs.getInt("disponibles");
                int ocupadas = rs.getInt("ocupadas");
                int reservadas = rs.getInt("reservadas");
                labelEstadoPlazas.setText(String.format("Total: %d, Disponibles: %d, Ocupadas: %d, Reservadas: %d", total, disponibles, ocupadas, reservadas));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar el estado de las plazas", e);
            mostrarAlerta("Error", "Ocurrió un error al actualizar el estado de las plazas.", Alert.AlertType.ERROR);
        }
    }

    private boolean esMatriculaValida(String matricula) {
        return matricula != null && matricula.matches("[A-Z0-9]{1,7}");
    }

    private void registrarEvento(String descripcion) {
        try (Connection connection = UtilMysql.getConnection()) {
            String queryEvento = "INSERT INTO Eventos (id_usuario, tipo_evento, descripcion, fecha_hora) VALUES (?, ?, ?, NOW())";
            PreparedStatement stmtEvento = connection.prepareStatement(queryEvento);
            stmtEvento.setLong(1, usuario.getIdUsuario());
            stmtEvento.setString(2, "Registro");
            stmtEvento.setString(3, descripcion);
            stmtEvento.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al registrar evento", e);
        }
    }

    private String obtenerFechaHoraActual() {
        return java.time.LocalDateTime.now().toString();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    public void handleBackButton() {
        try {
            Stage stage = (Stage) tablaEntradasSalidas.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tfg/parkplatesystem/fxml/principalUsuario.fxml"));
            Parent root = loader.load();

            ControladorPrincipal controladorPrincipal = loader.getController();
            controladorPrincipal.setUsuario(usuario);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Menú Principal");
            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar la vista principal", e);
            mostrarAlerta("Error", "No se pudo cargar el menú principal.", Alert.AlertType.ERROR);
        }
    }
}

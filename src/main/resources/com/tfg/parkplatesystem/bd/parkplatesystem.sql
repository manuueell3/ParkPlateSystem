-- Borrar base de datos parkplatesytem

DROP DATABASE parkplatesystem;

-- Crear base de datos parkplatesystem

CREATE DATABASE parkplatesystem;

-- Usar base de datos parkplatesystem

USE parkplatesystem;

-- Tabla 1: Usuarios

CREATE TABLE Usuarios
(
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre     VARCHAR(50)                         NOT NULL,
    apellidos  VARCHAR(50)                         NOT NULL,
    email      VARCHAR(100)                        NOT NULL UNIQUE,
    contraseña VARCHAR(255)                        NOT NULL,
    rol        ENUM ('administrador', 'conductor') NOT NULL,
    fecha_alta DATE                                NOT NULL
);

-- Tabla 2: Vehículos

CREATE TABLE Vehículos
(
    id_vehículo INT AUTO_INCREMENT PRIMARY KEY,
    matrícula   VARCHAR(20) NOT NULL UNIQUE,
    marca       VARCHAR(50) NOT NULL,
    modelo      VARCHAR(50) NOT NULL,
    color       VARCHAR(30),
    id_usuario  INT,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios (id_usuario)
);

-- Tabla 3: Plazas

CREATE TABLE Plazas
(
    id_plaza      INT AUTO_INCREMENT PRIMARY KEY,
    número_plaza  INT                                         NOT NULL,
    estado        ENUM ('disponible', 'ocupada', 'reservada') NOT NULL,
    fecha_bloqueo DATE,
    fecha_alta    DATE
);

-- Tabla 4: EntradasSalidas

CREATE TABLE EntradasSalidas
(
    id_registro        INT AUTO_INCREMENT PRIMARY KEY,
    id_vehículo        INT,
    fecha_hora_entrada DATETIME NOT NULL,
    fecha_hora_salida  DATETIME,
    id_plaza           INT,
    FOREIGN KEY (id_vehículo) REFERENCES vehiculos (id_vehiculo),
    FOREIGN KEY (id_plaza) REFERENCES Plazas (id_plaza)
);

-- Tabla 5: Configuración

CREATE TABLE Configuración
(
    id_config INT AUTO_INCREMENT PRIMARY KEY,
    parámetro VARCHAR(50)  NOT NULL,
    valor     VARCHAR(255) NOT NULL
);

-- Tabla 6: Pagos

CREATE TABLE Pagos
(
    id_pago         INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario      INT,
    id_registro     INT,
    id_vehículo     INT,
    monto           DECIMAL(10, 2) NOT NULL,
    fecha_hora_pago DATETIME       NOT NULL,
    forma_pago      VARCHAR(50)    NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios (id_usuario),
    FOREIGN KEY (id_registro) REFERENCES EntradasSalidas (id_registro),
    FOREIGN KEY (id_vehículo) REFERENCES vehiculos (id_vehiculo)
);

-- Tabla 7: Tarjetas

CREATE TABLE Tarjetas
(
    id_tarjeta       INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario       INT,
    número_tarjeta   VARCHAR(16) NOT NULL,
    fecha_expiración DATE        NOT NULL,
    cvv              VARCHAR(3)  NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios (id_usuario)
);

-- Tabla 8: Sanciones

CREATE TABLE Sanciones
(
    id_sanción     INT AUTO_INCREMENT PRIMARY KEY,
    id_vehículo    INT,
    id_usuario     INT,
    motivo         VARCHAR(255)   NOT NULL,
    monto          DECIMAL(10, 2) NOT NULL,
    fecha_hora     DATETIME       NOT NULL,
    fecha_max_pago DATE           NOT NULL,
    FOREIGN KEY (id_vehículo) REFERENCES vehiculos (id_vehiculo),
    FOREIGN KEY (id_usuario) REFERENCES Usuarios (id_usuario)
);

-- Tabla 9: Incidencias

CREATE TABLE Incidencias
(
    id_incidencia INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario    INT,
    descripción   TEXT                           NOT NULL,
    fecha_hora    DATETIME                       NOT NULL,
    estado        ENUM ('pendiente', 'resuelto') NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios (id_usuario)
);

-- Tabla 10: Roles

CREATE TABLE RolHistorial
(
    id_historial   INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario     INT                                 NOT NULL,
    nombre_usuario VARCHAR(50)                         NOT NULL,
    rol_anterior   ENUM ('administrador', 'conductor') NOT NULL,
    rol_nuevo      ENUM ('administrador', 'conductor') NOT NULL,
    fecha_cambio   TIMESTAMP                           NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios (id_usuario)
);

-- Tabla 11: Eventos

CREATE TABLE Eventos
(
    id_evento   INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario  INT,
    tipo_evento VARCHAR(50) NOT NULL,
    descripción TEXT,
    fecha_hora  DATETIME    NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios (id_usuario)
);

-- Tabla 12: Notificaciones

CREATE TABLE Notificaciones
(
    id_notificación INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario      INT,
    mensaje         TEXT     NOT NULL,
    leída           BOOLEAN  NOT NULL DEFAULT FALSE,
    fecha_hora      DATETIME NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios (id_usuario)
);

-- Tabla 13: Mantenimientos

CREATE TABLE Mantenimientos
(
    id_mantenimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_plaza         INT,
    descripción      TEXT,
    fecha_inicio     DATE                             NOT NULL,
    fecha_fin        DATE,
    estado           ENUM ('pendiente', 'completado') NOT NULL,
    FOREIGN KEY (id_plaza) REFERENCES Plazas (id_plaza)
);

-- Tabla 14: Tarifas

CREATE TABLE Tarifas
(
    id_tarifa      INT AUTO_INCREMENT PRIMARY KEY,
    descripcion    TEXT,
    monto_por_hora DECIMAL(10, 2) NOT NULL,
    monto_por_dia  DECIMAL(10, 2) NOT NULL
);

-- Tabla 15: Historial de Tarifas

CREATE TABLE HistorialTarifas
(
    id_historial            INT AUTO_INCREMENT PRIMARY KEY,
    id_tarifa               INT,
    descripcion_anterior    TEXT,
    monto_por_hora_anterior DECIMAL(10, 2),
    monto_por_dia_anterior  DECIMAL(10, 2),
    descripcion_nueva       TEXT,
    monto_por_hora_nueva    DECIMAL(10, 2),
    monto_por_dia_nueva     DECIMAL(10, 2),
    fecha_cambio            TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla 15: Reservas

CREATE TABLE Reservas
(
    id_reserva        INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario        INT,
    id_plaza          INT,
    fecha_hora_inicio DATETIME                                      NOT NULL,
    fecha_hora_fin    DATETIME                                      NOT NULL,
    estado            ENUM ('pendiente', 'confirmada', 'cancelada') NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios (id_usuario),
    FOREIGN KEY (id_plaza) REFERENCES Plazas (id_plaza)
);

-- Tabla 16: Reportes

CREATE TABLE Reportes
(
    id_reporte  INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario  INT,
    descripcion TEXT        NOT NULL,
    fecha       DATE        NOT NULL,
    tipo        VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios (id_usuario)
);

-- Tabla 17: Registros

CREATE TABLE Registros
(
    id_registro INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario  INT,
    tipo        VARCHAR(50)                      NOT NULL,
    fecha       DATE                             NOT NULL,
    estado      ENUM ('pendiente', 'completado') NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios (id_usuario)
);



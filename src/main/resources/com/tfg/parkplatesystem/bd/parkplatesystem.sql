
-- Borrar base de datos parkplatesytem

DROP DATABASE parkplatesystem;

-- Crear base de datos parkplatesystem

CREATE DATABASE parkplatesystem;

-- Usar base de datos parkplatesystem

USE parkplatesystem;

-- Tabla 1: Usuarios

CREATE TABLE Usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contraseña VARCHAR(255) NOT NULL,
    rol ENUM('administrador', 'conductor') NOT NULL,
    fecha_alta DATE NOT NULL
);

-- Tabla 2: Vehículos

CREATE TABLE Vehículos (
    id_vehículo INT AUTO_INCREMENT PRIMARY KEY,
    matrícula VARCHAR(20) NOT NULL UNIQUE,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    color VARCHAR(30),
    id_usuario INT,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

-- Tabla 3: Plazas

CREATE TABLE Plazas (
    id_plaza INT AUTO_INCREMENT PRIMARY KEY,
    número_plaza INT NOT NULL,
    estado ENUM('disponible', 'ocupada', 'reservada') NOT NULL,
    fecha_bloqueo DATE,
    fecha_alta DATE
);

-- Tabla 4: EntradasSalidas

CREATE TABLE EntradasSalidas (
    id_registro INT AUTO_INCREMENT PRIMARY KEY,
    id_vehículo INT,
    fecha_hora_entrada DATETIME NOT NULL,
    fecha_hora_salida DATETIME,
    id_plaza INT,
    FOREIGN KEY (id_vehículo) REFERENCES Vehículos(id_vehículo),
    FOREIGN KEY (id_plaza) REFERENCES Plazas(id_plaza)
);

-- Tabla 5: Configuración

CREATE TABLE Configuración (
    id_config INT AUTO_INCREMENT PRIMARY KEY,
    parámetro VARCHAR(50) NOT NULL,
    valor VARCHAR(255) NOT NULL
);

-- Tabla 6: Pagos

CREATE TABLE Pagos (
    id_pago INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    id_registro INT,
    id_vehículo INT,
    monto DECIMAL(10, 2) NOT NULL,
    fecha_hora_pago DATETIME NOT NULL,
    forma_pago VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario),
    FOREIGN KEY (id_registro) REFERENCES EntradasSalidas(id_registro),
    FOREIGN KEY (id_vehículo) REFERENCES Vehículos(id_vehículo)
);

-- Tabla 7: Tarjetas

CREATE TABLE Tarjetas (
    id_tarjeta INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    número_tarjeta VARCHAR(16) NOT NULL,
    fecha_expiración DATE NOT NULL,
    cvv VARCHAR(3) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

-- Tabla 8: Sanciones

CREATE TABLE Sanciones (
    id_sanción INT AUTO_INCREMENT PRIMARY KEY,
    id_vehículo INT,
    id_usuario INT,
    motivo VARCHAR(255) NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    fecha_hora DATETIME NOT NULL,
    fecha_max_pago DATE NOT NULL,
    FOREIGN KEY (id_vehículo) REFERENCES Vehículos(id_vehículo),
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

-- Tabla 9: Incidencias

CREATE TABLE Incidencias (
    id_incidencia INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    descripción TEXT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    estado ENUM('pendiente', 'resuelto') NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

-- Tabla 10: Roles

CREATE TABLE Roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL,
    descripción TEXT
);

-- Tabla 11: Eventos

CREATE TABLE Eventos (
    id_evento INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    tipo_evento VARCHAR(50) NOT NULL,
    descripción TEXT,
    fecha_hora DATETIME NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

-- Tabla 12: Notificaciones

CREATE TABLE Notificaciones (
    id_notificación INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    mensaje TEXT NOT NULL,
    leída BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_hora DATETIME NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

-- Tabla 13: Historial de Mantenimientos

CREATE TABLE HistorialMantenimientos (
    id_mantenimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_plaza INT,
    descripción TEXT,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    estado ENUM('pendiente', 'completado') NOT NULL,
    FOREIGN KEY (id_plaza) REFERENCES Plazas(id_plaza)
);

-- Tabla 14: Tarificaciones

CREATE TABLE Tarificaciones (
    id_tarifa INT AUTO_INCREMENT PRIMARY KEY,
    descripción TEXT,
    monto_por_hora DECIMAL(10, 2) NOT NULL,
    monto_por_día DECIMAL(10, 2) NOT NULL
);

-- Tabla 15: Reservas

CREATE TABLE Reservas (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    id_plaza INT,
    fecha_hora_inicio DATETIME NOT NULL,
    fecha_hora_fin DATETIME NOT NULL,
    estado ENUM('pendiente', 'confirmada', 'cancelada') NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario),
    FOREIGN KEY (id_plaza) REFERENCES Plazas(id_plaza)
);


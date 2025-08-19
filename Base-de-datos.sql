-- Crear base de datos
CREATE DATABASE agenda;
GO

USE agenda;
GO

-- Crear tabla Personas
CREATE TABLE Personas (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(200)
);
GO

-- Crear tabla Telefonos
CREATE TABLE Telefonos (
    id INT IDENTITY(1,1) PRIMARY KEY,
    personaId INT NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    FOREIGN KEY (personaId) REFERENCES Personas(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
GO

-- Insertar datos de prueba
INSERT INTO Personas (nombre, direccion)
VALUES ('John Doe', 'Calle Falsa 123');

INSERT INTO Telefonos (personaId, telefono)
VALUES (1, '555-7654321');

INSERT INTO Telefonos (personaId, telefono)
VALUES (1, '666-1234567');

-- Consultar los datos
SELECT * FROM Personas;
SELECT * FROM Telefonos;

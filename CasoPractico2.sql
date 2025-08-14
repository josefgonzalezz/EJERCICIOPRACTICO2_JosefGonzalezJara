
DROP SCHEMA IF EXISTS cine;
DROP USER IF EXISTS 'usuario_prueba'@'%';


CREATE SCHEMA cine;
CREATE USER 'usuario_prueba'@'%' IDENTIFIED BY 'Usuario_Clave.';
GRANT ALL PRIVILEGES ON cine.* TO 'usuario_prueba'@'%';
FLUSH PRIVILEGES;

USE cine;


CREATE TABLE categoria (
  id_categoria INT NOT NULL AUTO_INCREMENT,
  descripcion VARCHAR(30) NOT NULL,
  ruta_imagen VARCHAR(1024),
  activo BOOLEAN,
  PRIMARY KEY (id_categoria)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* Crear tabla de productos (películas) */
CREATE TABLE pelicula (
  id_pelicula INT NOT NULL AUTO_INCREMENT,
  id_categoria INT NOT NULL,
  titulo VARCHAR(100) NOT NULL,
  director VARCHAR(50),
  duracion_min INT,
  costo INT,
  existencias INT DEFAULT 0,
  reservado BOOLEAN,
  ruta_imagen VARCHAR(1024),  
  PRIMARY KEY (id_pelicula),
  CONSTRAINT fk_pelicula_categoria FOREIGN KEY (id_categoria)
    REFERENCES categoria(id_categoria)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


/* Crear tabla de usuarios */
CREATE TABLE usuario (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(20) NOT NULL,
  password VARCHAR(512) NOT NULL,
  nombre VARCHAR(20) NOT NULL,
  apellidos VARCHAR(30) NOT NULL,
  correo VARCHAR(75),
  telefono VARCHAR(15),
  ruta_imagen VARCHAR(1024),
  activo BOOLEAN,
  PRIMARY KEY (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* Crear tabla de roles */
CREATE TABLE rol (
  id_rol INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(20),
  id_usuario INT,
  PRIMARY KEY (id_rol),
  CONSTRAINT fk_rol_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* Crear tabla de facturas */
CREATE TABLE factura (
  id_factura INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  fecha DATE,  
  total DOUBLE,
  estado INT,
  PRIMARY KEY (id_factura),
  FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* Crear tabla de ventas (con producto = pelicula) */
CREATE TABLE venta (
  id_venta INT NOT NULL AUTO_INCREMENT,
  id_factura INT NOT NULL,
  id_producto INT NOT NULL,
  precio DOUBLE, 
  cantidad INT,
  PRIMARY KEY (id_venta),
  FOREIGN KEY (id_factura) REFERENCES factura(id_factura),
  FOREIGN KEY (id_producto) REFERENCES pelicula(id_pelicula)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* Insertar categorías */
INSERT INTO categoria (descripcion, ruta_imagen, activo) VALUES
  ('Película', 'https://example.com/pelicula.jpg', true),
  ('Obra', 'https://example.com/obra.jpg', true),
  ('Función', 'https://example.com/funcion.jpg', true);

/* Insertar películas */
INSERT INTO pelicula (
  id_categoria, titulo, director, duracion_min, costo, existencias, reservado, ruta_imagen
) VALUES
  (1, 'Spider-Man: No Way Home', 'Jon Watts', 148, 280, 85, false, 'https://es.web.img3.acsta.net/c_310_420/pictures/21/12/01/12/00/3517534.jpg'),
  (1, 'The Batman', 'Matt Reeves', 176, 320, 90, false, 'https://es.web.img3.acsta.net/c_310_420/pictures/22/01/31/10/23/0710374.jpg'),
  (1, 'Top Gun: Maverick', 'Joseph Kosinski', 130, 290, 95, false, 'https://es.web.img3.acsta.net/c_310_420/pictures/22/03/29/17/42/2387742.jpg'),
  (2, 'La La Land', 'Damien Chazelle', 128, 180, 60, false, 'https://es.web.img3.acsta.net/c_310_420/pictures/16/12/12/11/49/4726264.jpg'),
  (2, 'El Gran Showman', 'Michael Gracey', 105, 170, 70, false, 'https://es.web.img3.acsta.net/c_310_420/pictures/17/12/04/15/47/3330409.jpg'),
  (1, 'Dune', 'Denis Villeneuve', 155, 310, 80, false, 'https://es.web.img3.acsta.net/c_310_420/pictures/21/08/10/12/20/4633954.jpg'),
  (3, 'Hamilton', 'Thomas Kail', 160, 220, 40, false, 'https://es.web.img3.acsta.net/c_310_420/pictures/20/06/29/16/31/2288176.jpg'),
  (1, 'Oppenheimer', 'Christopher Nolan', 180, 350, 110, false, 'https://es.web.img3.acsta.net/c_310_420/pictures/23/05/08/16/46/2793085.jpg'),
  (2, 'Mamma Mia!', 'Phyllida Lloyd', 108, 160, 55, false, 'https://es.web.img3.acsta.net/c_310_420/pictures/08/06/18/11/29/565704.jpg'),
  (1, 'Interstellar', 'Christopher Nolan', 169, 300, 65, false, 'https://es.web.img3.acsta.net/c_310_420/pictures/14/09/24/12/08/2233371.jpg');

/* Insertar usuarios */
INSERT INTO usuario (username, password, nombre, apellidos, correo, telefono, ruta_imagen, activo) VALUES 
  ('admin', '$2a$10$P1.w58XvnaYQUQgZUCk4aO/RTRl8EValluCqB3S2VMLTbRt.tlre.', 'Ana', 'Admin Teatro', 'ana@teatro.com', '2222-3333', NULL, true),
  ('camila', '$2a$10$koGR7eS22Pv5KdaVJKDcge04ZB53iMiw76.UjHPY.XyVYlYqXnPbO', 'Camila', 'Chacon', 'camila@teatro.com', '8888-9999', NULL, true);

/* Insertar roles */
INSERT INTO rol (nombre, id_usuario) VALUES
  ('ROLE_ADMIN', 1),
  ('ROLE_USER', 2);

/* Insertar facturas */
INSERT INTO factura (id_usuario, fecha, total, estado) VALUES
  (1, '2025-08-01', 600.00, 1),
  (2, '2025-08-03', 300.00, 2);

/* Insertar ventas (películas vendidas) */
INSERT INTO venta (id_factura, id_producto, precio, cantidad) VALUES
  (1, 1, 300.00, 2),
  (2, 2, 150.00, 2);

/* Tabla de request matcher por rol */
CREATE TABLE request_matcher (
    id_request_matcher INT AUTO_INCREMENT NOT NULL,
    pattern VARCHAR(255) NOT NULL,
    role_name VARCHAR(50) NOT NULL,
	PRIMARY KEY (id_request_matcher)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

INSERT INTO request_matcher (pattern, role_name) VALUES 
('/pelicula/nuevo', 'ADMIN'),
('/pelicula/guardar', 'ADMIN'),
('/pelicula/modificar/**', 'ADMIN'),
('/pelicula/eliminar/**', 'ADMIN'),
('/categoria/nuevo', 'ADMIN'),
('/categoria/guardar', 'ADMIN'),
('/categoria/modificar/**', 'ADMIN'),
('/categoria/eliminar/**', 'ADMIN'),
('/usuario/nuevo', 'ADMIN'),
('/usuario/guardar', 'ADMIN'),
('/usuario/modificar/**', 'ADMIN'),
('/usuario/eliminar/**', 'ADMIN'),
('/reportes/**', 'ADMIN'),
('/pelicula/listado', 'USER'),
('/categoria/listado', 'USER'),
('/usuario/listado', 'USER'),
('/facturar/carrito', 'USER');

/* Tabla de request matcher públicos */
CREATE TABLE request_matcher_all (
    id_request_matcher INT AUTO_INCREMENT NOT NULL,
    pattern VARCHAR(255) NOT NULL,
	PRIMARY KEY (id_request_matcher)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

INSERT INTO request_matcher_all (pattern) VALUES 
  ('/'),
  ('/index'),
  ('/errores/**'),
  ('/carrito/**'),
  ('/pruebas/**'),
  ('/reportes/**'),
  ('/registro/**'),
  ('/js/**'),
  ('/webjars/**');
# Creo la Base de Datos 
CREATE DATABASE parquimetros;
USE parquimetros;


#-------------------------------------------------------------------------
# Creación de tablas para las entidades

CREATE TABLE Conductores (
	dni INT unsigned NOT NULL, 
    nombre VARCHAR(45) NOT NULL,
    apellido VARCHAR(45) NOT NULL,
    direccion VARCHAR(45) NOT NULL,
    telefono VARCHAR(45),
    registro INT UNSIGNED NOT NULL,
    
    CONSTRAINT PK_Conductores 
PRIMARY KEY (dni)
    
)ENGINE=InnoDB;


CREATE TABLE automoviles(

    patente VARCHAR(6) NOT NULL,
    marca VARCHAR(45) NOT NULL,
    modelo VARCHAR(45) NOT NULL,
    color VARCHAR(45) NOT NULL,
    dni INT unsigned NOT NULL,

    CONSTRAINT pk_automoviles 
PRIMARY KEY (patente),
   
    CONSTRAINT FK_Automoviles_dni 
FOREIGN KEY (dni) REFERENCES Conductores(dni)
ON DELETE RESTRICT ON UPDATE CASCADE
    

) ENGINE=InnoDB;

CREATE TABLE ubicaciones(
    calle VARCHAR(45) NOT NULL,
    altura SMALLINT unsigned NOT NULL,
    tarifa DECIMAL(5,2) unsigned NOT NULL,

    CONSTRAINT pk_ubicaciones 
PRIMARY KEY (calle, altura)
) ENGINE = InnoDB;

CREATE TABLE Parquimetros(
	id_parq INT unsigned NOT NULL,
	numero INT unsigned NOT NULL,
    calle VARCHAR(45) NOT NULL,
    altura SMALLINT unsigned NOT NULL,
    
    CONSTRAINT PK_Parquimetros 
PRIMARY KEY(id_parq),
	
    CONSTRAINT FK_Parquimetros_calle_altura
FOREIGN KEY (calle,altura) REFERENCES ubicaciones(calle,altura)
ON DELETE RESTRICT ON UPDATE CASCADE


)ENGINE=InnoDB;


CREATE TABLE inspectores(
    legajo INT UNSIGNED NOT NULL,
    dni INT UNSIGNED NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    password VARCHAR(32) NOT NULL, 

    CONSTRAINT pk_inspectores PRIMARY KEY (legajo)
) ENGINE = InnoDB;




CREATE TABLE Tipos_tarjeta(
    tipo VARCHAR(45) NOT NULL,
    descuento DECIMAL(3,2) unsigned  NOT NULL,

    CONSTRAINT pk_tipostarjeta 
PRIMARY KEY(tipo)


) ENGINE=InnoDB;

CREATE TABLE Tarjetas(

    id_tarjeta INT unsigned NOT NULL AUTO_INCREMENT,
    saldo DECIMAL(5,2) NOT NULL,
    tipo VARCHAR(45) NOT NULL,
    patente VARCHAR(6) NOT NULL,

    CONSTRAINT pk_tarjeta PRIMARY KEY(id_tarjeta),

    CONSTRAINT FK_Tarjeta_tipo 
FOREIGN KEY(tipo) REFERENCES Tipos_tarjeta(tipo)
ON DELETE RESTRICT ON UPDATE CASCADE,
    

    CONSTRAINT FK_Tarjeta_patente
 FOREIGN KEY (patente) REFERENCES Automoviles(patente)
	ON DELETE RESTRICT ON UPDATE CASCADE
    

) ENGINE=InnoDB;






#-------------------------------------------------------------------------
# Creación de tablas para las relaciones

CREATE TABLE Estacionamientos(
	id_tarjeta INT UNSIGNED NOT NULL,
	id_parq INT UNSIGNED NOT NULL,
	fecha_ent DATE NOT NULL,
	hora_ent TIME NOT NULL,
	fecha_sal DATE ,
	hora_sal TIME ,
    
    CONSTRAINT PK_Estacionamientos PRIMARY KEY(id_parq,fecha_ent,hora_ent),
    
    CONSTRAINT FK_Estacionamientos_id_tarjeta 
FOREIGN KEY (id_tarjeta) REFERENCES Tarjetas(id_tarjeta)
ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT FK_Estacionamientos_id_parq 
FOREIGN KEY (id_parq) REFERENCES Parquimetros(id_parq)
ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB;

CREATE TABLE Accede (
	legajo INT UNSIGNED NOT NULL,
    id_parq INT UNSIGNED NOT NULL,
    fecha DATE NOT NULL ,
    hora TIME NOT NULL ,
    
    CONSTRAINT PK_Accede PRIMARY KEY (id_parq,fecha,hora),
    
    CONSTRAINT FK_Accede_legajo 
FOREIGN KEY (legajo) REFERENCES inspectores(legajo)
ON DELETE RESTRICT ON UPDATE CASCADE,

CONSTRAINT FK_Accede_id_parq 
FOREIGN KEY (id_parq) REFERENCES Parquimetros(id_parq)
ON DELETE RESTRICT ON UPDATE CASCADE
    
)ENGINE=InnoDB;

CREATE TABLE asociado_con(
    id_asociado_con INT UNSIGNED NOT NULL AUTO_INCREMENT,
    legajo INT UNSIGNED NOT NULL,
    calle VARCHAR(30) NOT NULL,
    altura SMALLINT UNSIGNED NOT NULL,
    dia ENUM('Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa') NOT NULL,
    turno ENUM('M', 'T') NOT NULL,

    CONSTRAINT pk_asociado_con 
PRIMARY KEY (id_asociado_con),

    CONSTRAINT fk_asociado_con_legajo_inspectores 
FOREIGN KEY (legajo) REFERENCES inspectores(legajo)
ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_asociado_con_calle_altura_ubicacion 
FOREIGN KEY (calle,altura) REFERENCES ubicaciones(calle,altura)
ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE = InnoDB;

CREATE TABLE multa(
    numero INT unsigned NOT NULL AUTO_INCREMENT,
    fecha DATE NOT NULL,
    hora TIME(0) NOT NULL,
    patente VARCHAR(6) NOT NULL,
    id_asociado_con INT unsigned NOT NULL,

    CONSTRAINT pk_multa PRIMARY KEY (numero),

    CONSTRAINT fk_multa_patente_automoviles 
FOREIGN KEY (patente) REFERENCES Automoviles(patente)
ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_multa_id_asociado_con_asociado_con 
FOREIGN KEY (id_asociado_con) REFERENCES asociado_con(id_asociado_con)
ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE = InnoDB;




#-------------------------------------------------------------------------
#Creacion de vistas

CREATE VIEW estacionados AS SELECT calle,altura,patente 
  FROM estacionamientos as e, Parquimetros as p, Tarjetas as t
  WHERE (e.id_parq=p.id_parq AND fecha_sal IS NULL AND e.id_tarjeta=t.id_tarjeta);


#--------------------------------------------------------------------
# Creacion de usuarios y privilegios

#Creacion de usuarios

#admin 
	CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';
    GRANT ALL PRIVILEGES ON Parquimetros.* TO 'admin'@'localhost' with grant option;
	
#venta

	CREATE USER 'venta'@'%' IDENTIFIED BY 'venta';
    GRANT INSERT ON parquimetros.Tarjetas TO 'venta'@'%';
    GRANT SELECT ON parquimetros.Automoviles TO 'venta'@'%';
    GRANT SELECT ON parquimetros.Tarjetas TO 'venta'@'%';
    GRANT SELECT ON parquimetros.Tipos_tarjeta TO 'venta'@'%';
	
#inspector

	CREATE USER 'inspector'@'%' IDENTIFIED BY 'inspector';
#Se encripta el password del usuario 'inspector':
UPDATE parquimetros.inspectores SET password = MD5(password);
    GRANT INSERT ON parquimetros.multa TO 'inspector'@'%';
    GRANT SELECT ON parquimetros.inspectores TO 'inspector'@'%';
    GRANT INSERT ON parquimetros.accede TO 'inspector'@'%';
    GRANT SELECT ON parquimetros.multa TO 'inspector'@'%';
    GRANT SELECT ON parquimetros.accede TO 'inspector'@'%';
	GRANT SELECT ON parquimetros.estacionados TO 'inspector'@'%';
	GRANT SELECT ON parquimetros.asociado_con TO 'inspector'@'%';
	GRANT SELECT ON parquimetros.parquimetros TO 'inspector'@'%';


FLUSH PRIVILEGES;




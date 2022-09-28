#-----------------------------------------------------
#CARGA DE DATOS

INSERT INTO Conductores VALUES (12345678, "Juan", "Germani", "Chancay 1223" ,"156273849" ,123423);
INSERT INTO Conductores VALUES (87654321, "Manuel", "Fernandez", "Pellegrini 1343" ,"156273849" ,143433);
INSERT INTO Conductores VALUES (98765432, "Clara", "Solanas", "Moreno 458" ,"156273849" ,111143);
INSERT INTO Conductores VALUES (1357924, "Pedro", "Rodriguez", "Mitre 876" ,"156273849" ,313242);
INSERT INTO Conductores VALUES (24688765, "Carla", "Landino", "Salta 2450" ,"156273849" ,885422);
INSERT INTO Conductores VALUES (23542678, "Tomas", "Castillo", "Lavalle 2323" ,"156273849" ,131342);

INSERT INTO Automoviles VALUES ("AAB243", "Peugeot", "Classic", "Negro" ,12345678);
INSERT INTO Automoviles VALUES ("AKS121", "Wolsvagen", "Amarok", "Rojo" ,87654321);
INSERT INTO Automoviles VALUES ("AJS832", "Fiat", "Uno", "Blanco" ,1357924);
INSERT INTO Automoviles VALUES ("BBD998", "Chevrolet", "Joy", "Gris" ,98765432);
INSERT INTO Automoviles VALUES ("HBS111", "Dodge", "Caliber", "Gris" ,24688765);
INSERT INTO Automoviles VALUES ("GTD123", "Wolsvagen", "Onix", "Blanco" ,23542678);

INSERT INTO Tipos_tarjeta VALUES ("Visa Electron", 0.25);
INSERT INTO Tipos_tarjeta VALUES ("Mastercard", 0.15);
INSERT INTO Tipos_tarjeta VALUES ("American Express", 0.20);

INSERT INTO Tarjetas VALUES (null, 324.34,"Visa Electron", "AAB243");
INSERT INTO Tarjetas VALUES (null, 234.00,"American Express", "AKS121");
INSERT INTO Tarjetas VALUES (null, 435.00,"American Express", "AJS832");
INSERT INTO Tarjetas VALUES (null, 56.00,"Mastercard", "BBD998");
INSERT INTO Tarjetas VALUES (null, 122.00,"Visa Electron", "HBS111");
INSERT INTO Tarjetas VALUES (null, 885.00,"Mastercard", "GTD123");

insert into Inspectores values (10100,64893512,"Ricardo","Montaner",md5("1111"));
insert into Inspectores values (10101,88821377,"Mengana","Rodrigues",md5("2222"));
insert into Inspectores values (10102,78351209,"Mengano","Fernandez",md5("3333"));
insert into Inspectores values (10103,12344321,"Leonardo","Caballero",md5("4444"));    

insert into Ubicaciones values ("Sarmiento",900,185.42);
insert into Ubicaciones values ("Sarmiento",321,126.48);
insert into Ubicaciones values ("Sarmiento",100,126.48);
insert into Ubicaciones values ("Sarmiento",0,110.48);
insert into Ubicaciones values ("Alvarado",926,190.42);
insert into Ubicaciones values ("Alvarado",426,125.42);
insert into Ubicaciones values ("Alvarado",126,125.42);
insert into Ubicaciones values ("Alvarado",0,115.42);
insert into Ubicaciones values ("Zapiola",904,180.42);
insert into Ubicaciones values ("Zapiola",404,129.42);
insert into Ubicaciones values ("Zapiola",104,129.42);
insert into Ubicaciones values ("Zapiola",0,110.42);
insert into Ubicaciones values ("Viamonte",320,126.12);
insert into Ubicaciones values ("Alsina",178,129.42);
insert into Ubicaciones values ("Mitre",56,121.42);
insert into Ubicaciones values ("Soler",356,124.42);
insert into Ubicaciones values ("Lamadrid",400,200.42);

insert into Parquimetros values (100,1,"Sarmiento",900);
insert into Parquimetros values (101,2,"Sarmiento",321);
insert into Parquimetros values (102,3,"Sarmiento",100);
insert into Parquimetros values (103,4,"Sarmiento",0);
insert into Parquimetros values (104,5,"Alvarado",926);
insert into Parquimetros values (105,6,"Alvarado",426);
insert into Parquimetros values (106,7,"Alvarado",126);
insert into Parquimetros values (107,8,"Alvarado",0);
insert into Parquimetros values (108,10,"Zapiola",904);
insert into Parquimetros values (109,11,"Zapiola",404);
insert into Parquimetros values (110,12,"Zapiola",104);
insert into Parquimetros values (111,13,"Zapiola",0);
insert into Parquimetros values (112,14,"Viamonte",320);
insert into Parquimetros values (113,15,"Alsina",178);
insert into Parquimetros values (114,16,"Mitre",56);
insert into Parquimetros values (115,17,"Soler",356);
insert into Parquimetros values (116,18,"Lamadrid",400);


insert into Estacionamientos values (1,100,'2020-9-8','13:27:33','2020-09-08','14:32:34');
insert into Estacionamientos values (2,103,'2020-9-8','15:23:35','2020-09-02','17:10:19');
insert into Estacionamientos values (3,105,'2020-10-02','08:45:23','2020-10-02','09:25:50');
insert into Estacionamientos values (4,104,'2020-9-8','19:33:24',null,null);
insert into Estacionamientos values (5,101,'2020-9-8','23:59:34',null,null);
insert into Estacionamientos values (6,106,'2020-10-02','22:14:56',null,null);

insert into Asociado_con values (null,10100,"Sarmiento",900,"Lu","M");
insert into Asociado_con values (null,10101,"Sarmiento",321,"Ma","T");  
insert into Asociado_con values (null,10102,"Sarmiento",100,"Mi","M");  
insert into Asociado_con values (null,10103,"Sarmiento",0,"Ju","T");
insert into Asociado_con values (null,10100,"Alvarado",926,"Ma","M");
insert into Asociado_con values (null,10101,"Alvarado",426,"Mi","T");  
insert into Asociado_con values (null,10102,"Alvarado",126,"Ju","M");  
insert into Asociado_con values (null,10103,"Alvarado",0,"Vi","T");
insert into Asociado_con values (null,10100,"Zapiola",904,"Mi","M");
insert into Asociado_con values (null,10101,"Zapiola",404,"Ju","T");  
insert into Asociado_con values (null,10102,"Zapiola",104,"Vi","M");  
insert into Asociado_con values (null,10103,"Zapiola",0,"Lu","T");
insert into Asociado_con values (null,10100,"Viamonte",320,"Ju","M");
insert into Asociado_con values (null,10101,"Alsina",178,"Vi","T");  
insert into Asociado_con values (null,10102,"Mitre",56,"Lu","M");  
insert into Asociado_con values (null,10103,"Soler",356,"Ma","T");
insert into Asociado_con values (null,10100,"Lamadrid",400,"Vi","T");

insert into Accede values (10100,100,'2020-09-07','13:01:33');
insert into Accede values (10101,101,'2020-09-08','14:02:33');
insert into Accede values (10102,102,'2020-09-09','15:03:33');
insert into Accede values (10103,103,'2020-09-10','16:14:33');
insert into Accede values (10100,104,'2020-09-15','17:15:33');
insert into Accede values (10101,105,'2020-09-16','18:16:33');
insert into Accede values (10102,106,'2020-09-17','19:17:33');
insert into Accede values (10103,107,'2020-09-18','20:28:33');
insert into Accede values (10100,108,'2020-09-23','10:29:33');
insert into Accede values (10101,109,'2020-09-24','11:30:33');
insert into Accede values (10102,110,'2020-09-25','12:35:33');
insert into Accede values (10103,111,'2020-09-28','13:36:33');
insert into Accede values (10100,112,'2020-10-01','14:47:33');
insert into Accede values (10101,113,'2020-10-02','15:48:33');
insert into Accede values (10102,114,'2020-10-05','16:49:33');
insert into Accede values (10103,115,'2020-10-06','17:59:33');

insert into Multa values (1234,'2020-9-8','14:02:35',"AAB243",1);
insert into Multa values (1236,'2020-10-1','14:47:39',"AAB243",13);
insert into Multa values (1237,'2020-10-6','18:05:00',"GTD123",16);
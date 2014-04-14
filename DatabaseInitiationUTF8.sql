USE crimedatabase;

insert into user(username, password) values('Max Muster', '12345');
insert into user(username, password) values('Test', '1');

Insert into category(CatName) values ('Personal Crime');
Insert into category(CatName, SuperCat) values ('Assault', 'Personal Crime');
Insert into category(CatName, SuperCat) values ('Murder', 'Personal Crime');
Insert into category(CatName, SuperCat) values ('Kidnapping', 'Personal Crime');
Insert into category(CatName, SuperCat) values ('OtherPers','Personal Crime');
Insert into category(CatName) values ('Property Crime');
Insert into category(CatName, SuperCat) values ('Theft', 'Property Crime');
Insert into category(CatName, SuperCat) values ('Fraud', 'Property Crime');
Insert into category(CatName, SuperCat) values ('Burglary', 'Property Crime');
Insert into category(CatName, SuperCat) values ('OtherProp', 'Property Crime');

Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Andre', 'Rubbia', '1950-12-31');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Jürg', 'Gutknecht', '1921-4-13');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Bertrand', 'Meyer', '1928-4-14');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Frank', 'Gürkaynak', '1967-9-2');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Emo', 'Welzl', '1971-3-25');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Timothy', 'Roscoe', '1961-6-1');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Donald', 'Kossmann', '1963-11-15');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Dr. J. W.','Müller', '1920-02-14');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('General','Alcazar', '1933-09-29');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Palle','Tintin', '1929-10-10');

Insert into noteperson(idPersonOfInterest,text,username) values ('1', 'Sehr kräftig gebaut','Max Muster');
Insert into noteperson(idPersonOfInterest,text,username) values ('1', 'Besitzt eine Vorliebe für Fastfood','Max Muster');
Insert into noteperson(idPersonOfInterest,text,username) values ('2', 'Höchstgefährlich','Max Muster');
Insert into noteperson(idPersonOfInterest,text,username) values ('3', 'Auch bekannt als "Der Witwenmacher"','Max Muster');
insert into address(country, zipCode, city, street, streetNo) values ('Schweiz',8000,'Zürich','Rämistrasse',101);
insert into address(country, zipCode, city, street, streetNo) values ('Schweiz',8000,'Zürich','Universitätsstrasse',6);
insert into address(country, zipCode, city, street, streetNo) values ('Schweiz',8000,'Zürich','',-1);
insert into address(country, zipCode, city, street, streetNo) values ('Frankreich',85113,'Port-Joinville','',-1);

Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('Entwendung einer Schreibmaschine', 'Auf einmal war sie weg...', false, '2010-10-30', '13:45:00',  'Theft', 1,'Test');
Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('Mord an der ETH', 'Brutal mit Schreibmaschine erschlagen', true, '2010-10-31', '23:55:00', 'Murder', 1,'Test');
Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('Älterer Mann überfahren', 'Ein renommierter Professor wurde von einem Auto mit französischem Kennzeichen angefahren, welches anschliessend Fahrerflucht beging', true, '2010-11-01', '09:32:13', 'Assault', 1,'Test');
Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('Einbruch ins CAB', 'Entwendet wurden: 9 Flaschen Bier, 2 Tiefkühlpizzas und eine nicht funktionierende Kaffeemaschine', true, '2014-3-20', '00:48:19', 'Burglary', 2,'Test');
Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('Entführung des ETH Maskottchens', 'Opfer wurde mit Elektroschocker ausser Gefecht gestetzt und anschliessend in schwarzen Van verfrachtet', false, '2013-2-28', '21:34:51',  'Kidnapping', 1,'Test');
Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('Bösartige Turingmaschine', 'Transferierte das Budget des VIS auf ausländisches Konto', false, '2011-1-21', '15:12:34', 'Fraud', 2,'Test');
Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('Daten Verkauf', 'ETH Daten an ausländischen Geheimdienst verkauft', true, null, null, 'OtherProp', 3,'Max Muster');
Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('Mensa-Essen vergiftet','Alle Studenten bekamen Bauchweh.', true, '2012-10-11','12:00:00', 'OtherPers', 1, 'Max Muster');
Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('Uranentwendung','Uran wurde aus dem ETH Forschungsreaktor entwendet', true, '2012-03-13','03:07:16', 'Burglary', 1, 'Test');
Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('Tintin entführt','Auf der Insel Île d\'Yeu wurde Tintin entführt.', false, '2009-08-17','14:13:12', 'Kidnapping', 4, 'Test');
Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('Kap Haddock Whiskydiebstahl', 'Kapitän Haddock hat seine Whiskyflasche abgestellt. Auf einmal war sie weg...', false, '2001-10-30', '13:45:00',  'Theft', 4,'Test');
Insert into notecase(idCase,text,username) values ('2', 'Opfer war bekannt dafür, Witze über die Programmiersprache Eiffel zu reissen.','Max Muster');
Insert into notecase(idCase,text,username) values (7, '"Alle Professoren sind mit einem ausländischen Geheimdienst unter einer Decke" - angebliches Zitat von D.K.','Max Muster');

Insert into conviction(beginDate,endDate,idCase,idPersonOfInterest) values ('2010-11-2','2010-12-2',1,3);
Insert into conviction(beginDate,endDate,idCase,idPersonOfInterest) values ('2011-3-5','2011-9-13',6,5);
Insert into conviction(beginDate,endDate,idCase,idPersonOfInterest) values ('2012-7-5','2018-4-28',5,4);
Insert into conviction(beginDate,endDate,idCase,idPersonOfInterest) values ('2010-11-3','2012-3-22',1,1);
Insert into conviction(beginDate,endDate,idCase,idPersonOfInterest) values ('2009-08-17','2012-3-22',10,8);

Insert into involved(idPerson,idCase,role) values (3,1,'Suspect');
Insert into involved(idPerson,idCase,role) values (5,6,'Suspect');
Insert into involved(idPerson,idCase,role) values (4,5,'Suspect');
Insert into involved(idPerson,idCase,role) values (1,1,'Suspect');

Insert into involved(idPerson,idCase,role) values (2,1,'Witness');
Insert into involved(idPerson,idCase,role) values (1,2,'Suspect');
Insert into involved(idPerson,idCase,role) values (3,2,'Suspect');
Insert into involved(idPerson,idCase,role) values (1,4,'Suspect');
Insert into involved(idPerson,idCase,role) values (3,4,'Witness');

Insert into involved(idPerson,idCase,role) values (2,3,'Victim');
Insert into involved(idPerson,idCase,role) values (7,5,'Victim');
Insert into involved(idPerson,idCase,role) values (6,6,'Witness');
Insert into involved(idPerson,idCase,role) values (6,4,'Victim');
Insert into involved(idPerson,idCase,role) values (7,7,'Suspect');

Insert into involved(idPerson,idCase,role) values (8,8,'Suspect');
Insert into involved(idPerson,idCase,role) values (1,8,'Victim');
Insert into involved(idPerson,idCase,role) values (9,8,'Witness');

Insert into involved(idPerson,idCase,role) values (9,9,'Suspect');

Insert into involved(idPerson,idCase,role) values (10,10,'Victim');
Insert into involved(idPerson,idCase,role) values (8,10,'Suspect');
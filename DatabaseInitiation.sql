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
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('J�rg', 'Gutknecht', '1921-4-13');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Bertrand', 'Meyer', '1921-4-14');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Frank', 'G�rkaynak', '1967-9-2');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Emo', 'Welzl', '1971-3-25');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Timothy', 'Roscoe', '1961-6-1');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Donald', 'Kossmann', '1963-11-15');

Insert into noteperson(idPersonOfInterest,text,username) values ('1', 'Sehr kr�ftig gebaut','Max Muster');
Insert into noteperson(idPersonOfInterest,text,username) values ('1', 'Besitzt eine Vorliebe f�r Fastfood','Max Muster');
Insert into noteperson(idPersonOfInterest,text,username) values ('2', 'H�chstgef�hrlich','Max Muster');
Insert into noteperson(idPersonOfInterest,text,username) values ('3', 'Auch bekannt als "Der Witwenmacher"','Max Muster');
insert into address(country, zipCode, city, street, streetNo) values ('Switzerland',8000,'Z�rich','R�mistrasse',101);
insert into address(country, zipCode, city, street, streetNo) values ('Switzerland',8000,'Z�rich','',-1);

Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('Entwendung einer Schreibmaschine', 'Auf einmal war sie weg...', false, '2010-10-30', '13:45:00',  'Theft', 1,'Test');
Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('Mord an der ETH', 'Brutal mit Schreibmaschine erschlagen', true, '2010-10-31', '23:55:00', 'Murder', 1,'Test');
Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('�lterer Mann �berfahren', 'Ein renommierter Professor wurde von einem Auto mit franz�sischem Kennzeichen angefahren, welches anschliessend Fahrerflucht beging', true, '2010-11-01', '09:32:13', 'Assault', 1,'Test');
Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('Einbruch ins CAB', 'Entwendet wurden: 9 Flaschen Bier, 2 Tiefk�hlpizzas und eine nicht funktionierende Kaffeemaschine', true, '2014-3-20', '00:48:19', 'Burglary', 1,'Test');
Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('Entf�hrung des ETH Maskottchens', 'Opfer wurde mit Elektroschocker ausser Gefecht gestetzt und anschliessend in schwarzen Van verfrachtet', false, '2008-2-29', '21:34:51',  'Kidnapping', 1,'Test');
Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('B�sartige Turingmaschine', 'Transferierte das Budget des VIS auf ausl�ndisches Konto', false, '2000-1-21', '15:12:34', 'Fraud', 1,'Test');
Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('Älterer Mann überfahren', 'Ein renommierter Professor wurde von einem Auto mit franz�sischem Kennzeichen angefahren, welches anschliessend Fahrerflucht beging', true, '2010-11-01', '09:32:13', 'Assault', 1,'Test');
Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('Einbruch ins CAB', 'Entwendet wurden: 9 Flaschen Bier, 2 Tiefkühlpizzas und eine nicht funktionierende Kaffeemaschine', true, '2014-3-20', '00:48:19', 'Burglary', 1,'Test');
Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('Entführung des ETH Maskottchens', 'Opfer wurde mit Elektroschocker ausser Gefecht gestetzt und anschliessend in schwarzen Van verfrachtet', false, '2008-2-29', '21:34:51',  'Kidnapping', 1,'Test');
Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('Bösartige Turingmaschine', 'Transferierte das Budget des VIS auf ausländisches Konto', false, '2000-1-21', '15:12:34', 'Fraud', 1,'Test');
Insert into cases(Title,Description,open,date,time,CatName,idAddress,username) values ('Daten Verkauf', 'ETH Daten an ausl�ndischen Geheimdienst verkauft', true, null, null, 'OtherProp', 2,'Max Muster');
Insert into notecase(idCase,text,username) values ('2', 'Opfer war bekannt daf�r, Witze �ber die Programmiersprache Eiffel zu reissen.','Max Muster');
Insert into notecase(idCase,text,username) values (7, '"Alle Professoren sind mit einem ausl�ndischen Geheimdienst unter einer Decke" - angebliches Zitat von D.K.','Max Muster');

Insert into conviction(beginDate,endDate,idCase,idPersonOfInterest) values ('2010-11-2','2010-12-2',1,3);
Insert into conviction(beginDate,endDate,idCase,idPersonOfInterest) values ('2000-3-5','2000-9-13',6,5);
Insert into conviction(beginDate,endDate,idCase,idPersonOfInterest) values ('2008-7-5','2013-4-28',5,4);
Insert into conviction(beginDate,endDate,idCase,idPersonOfInterest) values ('2006-9-3','2012-3-22',1,1);

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

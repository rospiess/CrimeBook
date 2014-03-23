USE crimedatabase;

insert into user(username, password) values('Max Muster', '12345');
insert into user(username, password) values('Test', '1');

Insert into category(CatName) values ('Personal Crime');
Insert into category(CatName, SuperCat) values ('Assault', 'Personal Crime');
Insert into category(CatName, SuperCat) values ('Murder', 'Personal Crime');
Insert into category(CatName, SuperCat) values ('Kidnapping', 'Personal Crime');
Insert into category(CatName) values ('Property Crime');
Insert into category(CatName, SuperCat) values ('Theft', 'Property Crime');
Insert into category(CatName, SuperCat) values ('Fraud', 'Property Crime');
Insert into category(CatName, SuperCat) values ('Burglary', 'Property Crime');

Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Andre', 'Rubbia', '1950-12-31');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Jörg', 'Gutknecht', '1921-4-13');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Bertrand', 'Meyer', '1921-4-14');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Frank', 'Gürkaynak', '1967-9-2');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Emo', 'Welzl', '1971-3-25');

Insert into noteperson(idPersonOfInterest,text,username) values ('1', 'Sehr kräftig gebaut','Max Muster');
Insert into noteperson(idPersonOfInterest,text,username) values ('1', 'Besitzt eine Vorliebe für Fastfood','Max Muster');
Insert into noteperson(idPersonOfInterest,text,username) values ('2', 'Höchstgefährlich','Max Muster');
Insert into noteperson(idPersonOfInterest,text,username) values ('3', 'Auch bekannt als "Der Witwenmacher"','Max Muster');

insert into address(country, zipCode, city, street, streetNo) values ('Switzerland',8000,'Zürich','Rämistrasse',101);

Insert into cases(Title,Description,open,date,time,CatName,idAddress) values ('Entwendung einer Schreibmaschine', 'Auf einmal war sie weg...', false, '2010-10-30', '13:45:00',  'Theft', 1);
Insert into cases(Title,Description,open,date,time,CatName,idAddress) values ('Mord an der ETH', 'Brutal mit Schreibmaschine erschlagen', true, '2010-10-31', '23:55:00', 'Murder', 1);
Insert into cases(Title,Description,open,date,time,CatName,idAddress) values ('Schwere Körperverletzung', 'Älterer Mann von Auto mit französischem Kennzeichen angefahren', true, '2010-11-01', '09:32:13', 'Assault', 1);
Insert into cases(Title,Description,open,date,time,CatName,idAddress) values ('Einbruch ins CAB', 'Entwendet wurden: 9 Flaschen Bier, 2 Tiefkühlpizzas und eine nicht funktionierende Kaffeemaschine', true, '2014-3-20', '00:48:19', 'Burglary', 1);
Insert into cases(Title,Description,open,date,time,CatName,idAddress) values ('Entführung des ETH Maskottchens', 'Opfer wurde mit Elektroschocker ausser Gefecht gestezt wurde und anschliessend in schwarzen Van verfrachtet', false, '2008-2-29', '21:34:51',  'Kidnapping', 1);
Insert into cases(Title,Description,open,date,time,CatName,idAddress) values ('Bösartige Turingmaschine', 'Transferierte das Budget des VIS auf ausländisches Konto', false, '2000-1-21', '15:12:34', 'Fraud', 1);

Insert into notecase(idCase,text,username) values ('2', 'Opfer war bekannt dafür, Witze über die Programmiersprache Eiffel zu reissen.','Max Muster');

Insert into conviction(type,beginDate,endDate,idCase,idPersonOfInterest) values ('Diebstahl','2010-11-2','2010-12-2',1,3);
Insert into conviction(type,beginDate,endDate,idCase,idPersonOfInterest) values ('Betrug','2000-3-5','2000-9-13',6,5);
Insert into conviction(type,beginDate,endDate,idCase,idPersonOfInterest) values ('Freiheitsberaubung','2008-7-5','2013-4-28',5,4);



Insert into involved(idPerson,idCase,role) values (2,1,'Witness');
Insert into involved(idPerson,idCase,role) values (1,2,'Suspect');
Insert into involved(idPerson,idCase,role) values (3,2,'Suspect');
Insert into involved(idPerson,idCase,role) values (4,5,'Suspect');
Insert into involved(idPerson,idCase,role) values (5,6,'Suspect');
Insert into involved(idPerson,idCase,role) values (1,4,'Suspect');
Insert into involved(idPerson,idCase,role) values (3,4,'Witness');

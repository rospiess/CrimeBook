Insert into category(CatName) values ('Personal Crime');
Insert into category(CatName, SuperCat) values ('Assault', 'Personal Crime');
Insert into category(CatName, SuperCat) values ('Murder', 'Personal Crime');
Insert into category(CatName) values ('Property Crime');
Insert into category(CatName, SuperCat) values ('Theft', 'Property Crime');
Insert into category(CatName, SuperCat) values ('Fraud', 'Property Crime');

Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Andre', 'Rubbia', '1950-12-31');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Jörg', 'Gutknecht', '1921-4-13');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Bertrand', 'Meyer', '1921-4-14');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Frank', 'Gürkaynak', '1967-9-2');
Insert into personofinterest(FirstName, LastName, DateOfBirth) values ('Emo', 'Welzl', '1971-3-25');

Insert into noteperson(idPersonOfInterest,text) values ('1', 'Sehr kräftig gebaut"');
Insert into noteperson(idPersonOfInterest,text) values ('2', 'Höchstgefährlich');
Insert into noteperson(idPersonOfInterest,text) values ('3', 'Auch bekannt als "Der Witwenmacher"');

Insert into cases(Title,Description,open,date,time,location,CatName) values ('Entwendung einer Schreibmaschine', 'Auf einmal war sie weg...', false, '2010-10-30', '13:45:00', 'Rämistrasse 101', 'Theft');
Insert into cases(Title,Description,open,date,time,location,CatName) values ('Mord an der ETH', 'Brutal mit Schreibmaschine erschlagen', true, '2010-10-31', '23:55:00', 'Rämistrasse 101', 'Murder');
Insert into cases(Title,Description,open,date,time,location,CatName) values ('Schwere Körperverletzung', 'Älterer Mann von Auto mit französischem Kennzeichen angefahren', true, '2010-11-01', '09:32:13', 'Rämistrasse 101', 'Assault');

Insert into notecase(idCase,text) values ('2', 'Opfer war bekannt dafür, Witze über die Programmiersprache Eiffel zu reissen.');

Insert into conviction(type,beginDate,endDate,idCase,idPersonOfInterest) values ('Diebstahl','2010-11-2','2010-12-2',1,3);

Insert into involved(idPerson,idCase,role) values (2,1,'Witness');
Insert into involved(idPerson,idCase,role) values (1,2,'Suspect');
Insert into involved(idPerson,idCase,role) values (3,2,'Suspect');

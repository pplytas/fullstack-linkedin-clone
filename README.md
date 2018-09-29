# Semester assignment for the course of Web Application Technologies
This assignment contains a full-stack application that simulates a LinkedIn environment, according to the specifications [here](https://eclass.uoa.gr/modules/document/file.php/D53/%CE%95%CF%81%CE%B3%CE%B1%CF%83%CE%AF%CE%B5%CF%82/%CE%A5%CF%80%CE%BF%CF%87%CF%81%CE%B5%CF%89%CF%84%CE%B9%CE%BA%CE%AE_%CE%95%CF%81%CE%B3%CE%B1%CF%83%CE%AF%CE%B1_2018.pdf).

The application contains 2 types of users, normal access users that can be created by registering during runtime and admin access users that are created programmatically at the initialization of the app.

To build, you need to have MYSQL service running and maven installed. Project requires Java 8.
Port, name and credentials for database can be found in application.properties.

If you decide to keep the default database settings, you can initialize it by using the 3 following commands:
mysql -u root -ptest
CREATE DATABASE tedidb
USE tedidb

Build server using "mvn clean install" inside the tedi_server folder. Run the server using "mvn spring-boot:run" in the same folder path.

Architecture-wise, the server has controller classes which contain the endpoints and state the i/o models for each of them. Most of the logic is propagated from the controllers to the service layer. Communication with the database happens through repository interfaces and entity classes. Utilities package contains helping static functions for our application.
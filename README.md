# Imbox-Server

## LINKS:

* [imbox-infra](https://github.com/jaiyalas/imbox-infra)
* [imbox-client](https://github.com/jaiyalas/imbox-client)
* [imbox-client-network](https://github.com/teddywinglee/imbox-client-network)
* [imbox-server](https://github.com/teddywinglee/imbox-server)

## DESCRIPTION:

**Imbox** is a system to provide a file hosting service. It offers [1] cloud storage, [2] file synchronization, and [3] cross-platform client application.

**Imbox** consists of two parts: the [imbox-client](https://github.com/jaiyalas/imbox-client) and the [imbox-server](https://github.com/teddywinglee/imbox-server). They both require the basic library [imbox-infra](https://github.com/jaiyalas/imbox-infra) which defines and provides essential data structures and tools. The [imbox-client](https://github.com/jaiyalas/imbox-client) also needs to import another package, the [imbox-client-network](https://github.com/teddywinglee/imbox-client-network), to handle connections between client and server.

!!**This repository is the server-part of the Imbox system.**!!

# Imbox-Server

## REQUIREMENTS:

* Java 8 (tested with Oracle Java 1.8.0_20-ea) 
* Maven 3 (tested with Apache Maven 3.2.1)
* MySQL (tested with MySQL 5.5, server only)

## INSTALLATION:

### Database

Install Mysql in ubuntu

    > sudo apt-get install mysql mysql-server 
    > sudo service mysqld start
    > mysqladmin -u root passwordyoulike
			
Configure mysql database

    > mysql -u userid -p [-h localhost]
    > create database [group2_db];
    > mysql -u userid -p [-h localhost] group2_db < group2_db.sql > 

### Server

The easiest way is to use Eclipse to run this project. Simply import it (File>import>Existing project into workspace). Then, run it as Java application, VoilÃ !
		
Or, you can try to use maven

    > git clone https://github.com/teddywinglee/imbox-server.git
    > cd imbox-server/
    > mvn clean compile assembly:single

Please note that the "libs" folder is for maven, "lib" is for eclipse, download the one you need.

The server now runs on 8080 port, if you need to change it, please look at org.imbox.server.main.Imboxserver. The server now connects to localhost DB with default account and password, pleas change it at **org.imbox.database.db_connect**.

## SYNOPSIS:

	> java -jar ./imbox-server

or

	> java -jar ./target/Imbox-Server-0.1-jar-with-dependencies.jar

if you are using Maven.	

## CONTRIBUTORS:

* [@teddywinglee](https://github.com/teddywinglee)
* [@cindyboy](https://github.com/cindyboy)

## LICENSE:

Apache License 2.0

Copyright (c) 2014, Yih-Der Lee (teddywinglee)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
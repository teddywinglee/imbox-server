# Imbox

## LINKS:

* [imbox-infra](https://github.com/jaiyalas/imbox-infra)
* [imbox-client](https://github.com/jaiyalas/imbox-client)
* [imbox-client-network](https://github.com/teddywinglee/imbox-client-network)
* [imbox-server](https://github.com/teddywinglee/imbox-server)

## DESCRIPTION:

Imbox is a system to provide a file hosting service. It offers [1] cloud storage, [2] file synchronization, and [3] cross-platform client application.

Imbox consists of two applications: [imbox-client](https://github.com/jaiyalas/imbox-client) and [imbox-server](https://github.com/teddywinglee/imbox-server). They both require [imbox-infra](https://github.com/jaiyalas/imbox-infra) which defines and provides essential data structures and tools. The [imbox-client](https://github.com/jaiyalas/imbox-client) also needs to import [imbox-client-network](https://github.com/teddywinglee/imbox-client-network) to handle connections between client and server.

## REQUIREMENTS:

* Java 8 (tested with Oracle Java 1.8.0_20-ea) 
* Maven 3 (tested with Apache Maven 3.2.1)
* MySQL (tested with MySQL 5.5, server only)

## INSTALLATION:

### Server
	There are two parts that are required to run the server side properly.
	I. server itself
		The easiest way is to use Eclipse to run this project.
		Simply import it (File>import>Existing project into workspace).
		Then, run it as Java application, Voilà!
		note that the "libs" folder is for maven, "lib" is for eclipse, download the one you need
		
		Or, you can try to use maven
			> git clone https://github.com/teddywinglee/imbox-server.git
			> cd imbox-server/
			> mvn clean compile assembly:single
	II. Database 
		Install Mysql in ubuntu
			> sudo apt-get install mysql mysql-server 
			> sudo service mysqld start
			> mysqladmin -u root passwordyoulike
			
		Configure mysql database
			> mysql -u userid -p [-h localhost]
			> create database [group2_db];
			> mysql -u userid -p [-h localhost] group2_db < group2_db.sql > 

### Client

    > git clone https://github.com/jaiyalas/imbox-client.git
    > cd imbox-client/client
    > mvn clean compile assembly:single

## SYNOPSIS:

### Server

	> java -jar ./imbox-server

### Client

    > java -jar ./target/imbox-client-<version>-jar-with-dependencies.jar

## CONTRIBUTORS:

* [Yun-Yan Chi@(jaiyalas)](https://github.com/jaiyalas)
* [@teddywinglee](https://github.com/teddywinglee)
* [@cindyboy](https://github.com/cindyboy)

## LICENSE:

Apache License 2.0

Copyright (c) 2011-2014, Lin Jen-Shin (godfat)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
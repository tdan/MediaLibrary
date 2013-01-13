# The Media Library Project #

A project by students Trung An, Cyle Dawson, and Gavin Monroe for the COM S 362 course at Iowa State University in the spring of 2012

## Building ##

### Eclipse ###

Eclipse will build the project automatically.

### Apache Ant ###

Alternatively you can use Apache Ant to build the project. There is a build.xml file in the root of the project with the following targets:

- clean
	- Removes .jar files
- build (default)
	- Builds entire project including client, example client, and example server
- client
	- Builds the client
- exampleclient
	- Builds the example client
- exampleserver
	- Builds the example server
- startclient
	- Starts the client
- startexampleclient
	- Starts the example client
- startexampleserver
	- Starts the example server
- javadoc
	- Generate API Documentation. After generating, view doc/javadoc/index.html in your browser.

*NOTE: __DO NOT__ commit the .jar files to the repository.*

## Package Structure ##

The project is organized into the following packages:

- cajo.*
	- The cajo project SDK
- controller
	- Controller
- database
	- Database interaction
- model
	- Data model
- proxy
	- Service proxies
- service
	- Services
- ui
	- Graphical user interface

## Included Libraries ##

- grail.jar
	- Supports interactions between Java Virtual Machines
- sqlitejdbc-v056.jar
	- An SQLite JDBC driver

## Important Notes ##

- If using Eclipse, import this project into Eclipse (DO NOT copy files into an existing project). This preserves project specific settings.

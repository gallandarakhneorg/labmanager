# Guidelines for contributors.

This document provides general guidelines for the contributors.

## 1. Installation for developers

### 1.1. Base tools

The following tools must be installed for contributing to the project, and testing these contributions:

* **Java:** the project is implemented with Java 11. You must downlad and install [Java Development Kit (JDK) 11](https://www.oracle.com/fr/java/technologies/javase/jdk11-archive-downloads.html).
* **Maven:** Maven is the compilation framework that is used for the project. You must [download and install Maven](https://maven.apache.org/download.cgi) (at least the version 3.8.4) in order to have it available from the command-line interface.
* **Git:** It is recommended to install a Git tool that could be used outside the development environment, e.g. the command-line interface or windows tools.
* **Eclipse:** You must install a specific version of Eclipse: [Eclipse IDE for Enterprise Java and Web Developers](https://www.eclipse.org/downloads/)
* **Database:**
  * *For developers,* you don't need to install a database system. The project is configured for using the Apache Derby Database system that is local file-based.
  * *For production,* the project is configured for using MySQL 5.0.3 (or higher). Therefore, you must [download and install MySQL](https://dev.mysql.com/downloads/installer/).

### 1.2. First creation of the project

* **Fork the project:** The source code of the project is stored on a Git repository hosted by [Bitbucket](https://bitbucket.org/ciadlabfr/labmanager-server-spring/) as the *Master server*. However, you will not have access rights to push up your commits during the development stage. Therefore, it is recommended to create a *fork* of the Master server on Bitbucket. For the rest of this document, the URL of your fork repository is assumed to be `https://bitbucket.org/myself/labmanager-server-spring`.
* **Clone of the source code:** You should clone your Git repository on your local computer to let you work locally on the source code:
```
$> git clone https://bitbucket.org/myself/labmanager-server-spring
```
The folder in which the source code is copied is assumed to be named `/path/to/src` (using Unix standard).
* **First compilation on CLI:** In order to be sure that all the libraries are downloaded and installed on your computer, it is recommended to launch a maven compilation from the command-line interface:
```
$> cd /path/to/src
$> mvn clean install
```
The compilation must be successful.

* **Import the source code in Eclipse:** For creating the project in the Eclipse IDE, you have to follow the steps:
  * Launch the Eclipse IDE
  * Open the import wizard with `Menu > File > Import`
  * Select the importer `Maven > Existing Maven Projects` ![Screenshot 1](./resources/eclipse0.png)
  * On the next page of the wizard, you must select the root source folder that contains a `pom.xml` file, i.e., `/path/to/src/labmanager-2`
  * Click on the `Finish` button to create the project
* **Change of the development configuration:** During the development stage, you will have to use a specific Spring configuration. This configuration is stored into the file `src/main/resources/application-dev.yml` as a YAML configuration file associated to the profile "dev". You must edit this file with your local information as described below. *Do not forget to not save these changes on the Git repository`.
  * *Change the Derby database configuration:* The content of the configuration file has:
```
# Derby database
driver-class-name: org.apache.derby.jdbc.EmbeddedDriver
url: jdbc:derby:/tmp/ciadlab/db;create=true
```
    Replace `/tmp/ciadlab/db` by the folder name in which you wold like to save your database
  * *Change the logging configuration:* The content of the configuration file has:
```
# Path to the log file
external-file: /tmp/tomcat9/CIADSpringRestHibernate.log
```
    Replace `/tmp/tomcat9/CIADSpringRestHibernate.log` by the filename of the file that must contain the logs.

### 1.3. Launching of the project

For launching the backend services into a local Tomcat server, you could launch it into the Eclipse environment. To do so, you have to create a configuration for Spring Boot App:

* Open the menu `Run > Run configurations`
* Select the type `Spring Boot App` and click on the `Create new launch configuration` button at the top of the list
* Enter the name of the launch configuration on the right part of the wizard windows, e.g. with `labmanager`
* Select the project `labmanager`
* Select the `labmanager-2` project below
* Enter the main type: `fr.ciadlab.labmanager.LabManagerApplication`
* Enter the profile: `dev`
* Click on the `Run` button for saving the launch configuration and launching the Spring Boot application

### 1.4. Initial database schema creation

By default, JPA is not creating the database schema. Indeed, it is configured for updating the content of the database from an existing schema.
Therefore, it is necessary to create the initial database schema when you start from an empty database.

The project is configured for creating automatically a SQL script for creating the database schema. It is created into the project folder, i.e., `./labmanager-2/`.
The name of the script is `schema-<platform>.-gensql`, where `<platform>` is the name of the database platform that is configured in the `spring.sql.init.platform` configuration property.
By default, in the development environment, the platform is `derby`.

Caution: if the file `schema-<platform>.-gensql` does not exist in your source folder, you have to launch the project into Eclipse once for forcing its generation.

For creating the database schema, you have to follow the steps:

* Copy `./labmanager-2/schema-<platform>-gen.sql` into `./labmanager-2/src/main/resources/schema-<platform>.sql`
* Launch the project in Eclipse
* Wait for successful project launching, and then stop the run.
* Delete the file `./labmanager-2/src/main/resources/schema-<platform>.sql`

Now you database should be ready with an up-to-date schema and with empty tables.

### 1.5. Inject initial data into the database

The development teams has a not-public JSON file that contains the initial content of the database (research organizations, persons, memberships, journals, etc.).

The project is configured for inserting automatically the data that is described into a specific JSON file with the location and name `./labmanager-2/src/main/resources/data.json`.
Copy the JSON file provided by the projec team into the folder `./labmanager-2/src/main/resources/` and ensures that the name of the new file is `data.json`.

For injecting initial data in the database, you have to follow the steps:

* Copy the JSON file into `./labmanager-2/src/main/resources/data.json`
* Launch the project in Eclipse
* Wait for successful project launching, and then stop the run.
* Delete the file `./labmanager-2/src/main/resources/data.json`

Now you database contains the based data that is validated by the project development team.

## 2. Coding

### 2.1. Create JUnit5 tests 

The guideline of the Master development team recommend to create JUnit5 tests for all the significant contributions. You must create your tests into the source folder `src/test/java` and copy additionnal testing resources into the source folder `src/test/resources`.

For launching the tests, you could create and use a specific launch configuration of type `JUnit`:

* Open the menu `Run > Run configurations`
* Select the type `JUnit` and click on the `Create new launch configuration` button at the top of the list
* Enter the name of the launch configuration on the right part of the wizard windows, e.g. with `All tests`
* Select `Run all tests in the selected project, package or source folder`
* Select the `labmanager` project below
* Ensure that the test runner is `JUnit 5`
* Click on the `Run` button for saving the launch configuration and launching the tests

### 2.2. Adding a Field in an JPA Entity

In order to add a field in the JPA entity, i.e., a column in the database, it it recommended to follow the steps below:

* Add a field int the entity, and mark it with the appropriate JPA annotations:
```java
@Column
private int newField;
```
* Add the initialization of the field in the entity constructor:
```java
public Type(String numericField) {
	this.newField = newField;
}
```
* Add the field in the computation of the hash code:
```java
public int hashCode() {
	int h = HashCodeUtils.start();
	...
	h = HashCodeUtils.add(h, this.newField);
	...
	return h;
}
```
* Add the field in the equality test:
```java
public boolean equals(Object obj) {
	...
	if (this.newField != other.newField) {
		return false;
	}
	...
	return true;
}
```
* Add the field in the function for providing the field's value to external objects:
```java
public void forEachAttribute(BiConsumer<String, Object> consumer) {
	...
	if (getNewField())) {
		consumer.accept("newField", getNewField()); //$NON-NLS-1$
	}
	...
}
```
* Add the getter function(s):
```java
public int getNewField() {
	return this.newField;
}
```
* Add the setter function(s):
```java
public void setNewField(int value) {
	this.newField = value;
}
```
* Add the setter function for the JSON importer; the JSON importer accepts to set the fields' values if the setter functions has a formal argument of type `String`, `Number` or `Boolean`
  (no primitive type). If the type of a field is not one of the three previous types, it is necessary to define a specific setter function for the JSON importer:
```java
public void setNewField(Number value) {
	if (value == null) {
		this.newField = 0;
	} else {
		this.newField = value.intValue();
	}
}
```
* **Because you have added a column for the JPA entity, it is mandatory to reset the database. You have to delete completely the database and create it again.**


## 3. Merge your contributions to the Master server

As soon as all your contributions are comitted on your fork Git repository, it is possible to provide your changes to the Master development team through a *Pull Request* (PR) on Bitbucket:

* Open the Master Git Repository on [Bitbucket](https://bitbucket.org/ciadlabfr/labmanager-server-spring/)
* Open the PR page
* Create a new PR
* Select your fork Git repository as the source of the PR
* Fill-up the PR form



# Guidelines for contributors

This document provides general guidelines for the contributors.

## 1. Installation for developers

### 1.1. Prerequirements

The following tools must be installed for contributing to the project, and testing these contributions:

* **Java:** the project is implemented with Java 17. You must downlad and install [Java Development Kit (JDK) 17](https://www.oracle.com/fr/java/technologies/javase/jdk17-archive-downloads.html).
* **Maven:** Maven is the compilation framework that is used for the project. You must [download and install Maven](https://maven.apache.org/download.cgi) (at least the version 3.9.0) in order to have it available from the command-line interface.
* **Git:** It is recommended to install a Git tool that could be used outside the development environment, e.g. the command-line interface or windows tools.
* **Eclipse:** You must install a specific version of Eclipse: [Eclipse IDE for Enterprise Java and Web Developers](https://www.eclipse.org/downloads/)
* **Database:**
  * *For developers,* you don't need to install a database system. The project is configured for using a Apache Derby database that is in-memory and local file-based.
  * *For production,* the project is configured for using MySQL. Therefore, you must [download and install MySQL](https://dev.mysql.com/downloads/installer/) on the production server.

### 1.2. First creation of the project

#### 1.2.1. Fork the project:
The source code of the project is stored on a Git repository hosted by [Github](https://github.com/gallandarakhneorg/labmanager) as the *Master server*. However, you will not have access rights to push up your commits during the development stage. Therefore, it is recommended to create a *fork* of the Master server. For the rest of this document, the URL of your fork repository is assumed to be `https://github.com/myself/labmanager`.

#### 1.2.2. Clone of the source code:
You should clone your Git repository on your local computer to let you work locally on the source code:
```
$> git clone https://github.com/myself/labmanager.git
```
The folder in which the source code is copied is assumed to be named `/path/to/src/labmanager` (using Unix standard).
This folder contains the root file `pom.xml`.

#### 1.2.3. First compilation on CLI:
In order to be sure that all the libraries are downloaded and installed on your computer, it is recommended to launch a maven compilation from the command-line interface:
```
$> cd /path/to/src/labmanager
$> mvn clean install -Dmaven.test.skip=true
```
The compilation must be successful.

#### 1.2.4. Import the source code in Eclipse:
For creating the project in the Eclipse IDE, you have to follow the steps:
  * Launch the Eclipse IDE
  * Open the import wizard with `Menu > File > Import`
  * Select the importer `Maven > Existing Maven Projects` ![Screenshot 1](./resources/eclipse0.png)
  * On the next page of the wizard, you must select the root source folder that contains a `pom.xml` file, i.e., `/path/to/src/labmanager`
  * Click on the `Finish` button to create the project

#### 1.2.5. Change of the development configuration:
During the development stage, you will have to use a specific SpringBoot configuration. This configuration is stored into the file `/path/to/src/labmanagersrc/main/resources/application-dev.yml` as a YAML configuration file associated to the profile "dev". You must edit this file with your local information as described below. *Don't save these changes on the Git repository*.
* *Change the developer database configuration:* Apache Derby was selected as a local database engine for contributors because it is easy to install and use. The configuration file uses Derby local database by default. To indicate where Derby is saving the database, replace `/tmp/labmanager-tmp/db` by the path of the folder that should contains all the database.
```
spring:
  datasource:
    url: jdbc:derby:directory:/tmp/labmanager-tmp/db;create=true
```
* *Change the logging configuration:* SpringBoot logs the messages into files. The content of the configuration file contains the text below. Replace `/tmp/labmanager-tmp` by the path to the folder in which the log files should be written.
```
logging:
  file:
    path: /tmp/labmanager-tmp

management:
  endpoint:
    logfile:
      enabled: true
      external-file: /tmp/labmanager-tmp/LabManager.log
```

* *Change the initialization configuration:* LabManager is able to automatically populate the database with existing data when the application is starting up. This feature is based on the reading of a file, named `data.json` or `data.zip` that contains the data to be imported. The application search this file in a specific directory that is specified below. Change `/tmp/labmanager-tmp` to change the location of this initialization file. Also, you have to rename or remove the file when the initialization is done. In this way, the content of your database will not change at each startup.
```
labmanager:
  init:
    enable: true
    data-source: /tmp/labmanager-tmp
```
* *Change the upload configuration:* LabManager app enables to upload files into the back-end server, e.g., the PDF files of the publication. You must specify a folder in which all the files could be copied. The content of the configuration file contains the text below. Replace `/tmp/labmanager-tmp` by the name of the folder in which all the uploaded files must be copied, including PDF of the publications and award certificates. This folder will contains in the sub-folder `Downloadables` the files that could be downloaded form the front-end.
```
labmanager:
  file:
    upload-directory: /tmp/labmanager-tmp
```
* *Change the temp directory configuration:* LabManager app uses a folder for creating temporary files. The content of the configuration file contains the text below. Replace `/tmp/labmanager-tmp/tmp` by the name of the folder in which all the temporary files are created.
```
labmanager:
  file:
    temp-directory: /tmp/labmanager-tmp/tmp
```
* *Change the publishing configuration of the resources:* Many documents and files are stored into the back-end server and should be accessible from the client browser. To enable this access, the configuration file must be updated for giving to the app the name of the folder that must be published. The content of the configuration file contains the text below. Replace `/tmp/labmanager-tmp/Downloadables/` by the name of the folder in which all the public resources of the app are located. The name `Downloadables` must not be changed because it is hard-coded in the app. The folder name `/tmp/labmanager-tmp` is usually the same as for the `upload-directory` configuration to enable the access of the uploaded PDF files from a browser.
```
labmanager:
  web:
    server-side-resources: /tmp/labmanager-tmp/Downloadables
```


### 1.3. Launching of the project

For launching the backend services into a local Tomcat server, you could launch it into the Eclipse environment. To do so, you have to create a run configuration. You may launch a SpringBoot app if you have installed the SpringBoot plugins in your IDE, or launch a "Java" app if you have not installed them.

* *For SpringBoot App:*
  * Open the menu `Run > Run configurations`
  * Select the type `SpringBoot App` and click on the `Create new run configuration` button at the top of the list
  * Enter the name of the run configuration on the right part of the wizard windows, e.g. with `labmanager`
  * Select the `labmanager` project below
  * Enter the main type: `fr.utbm.ciad.labmanager.LabManagerApplication`
  * Enter the profile: `dev` (or the name of the YAML configuration you would like to use, see `application-dev.yml`)
  * Click on the `Run` button for saving the launch configuration and launching the SpringBoot application
* *For Regular Java App:*
  * Open the menu `Run > Run configurations`
  * Select the type `Java Application` and click on the `Create new run configuration` button at the top of the list
  * Enter the name of the run configuration on the right part of the wizard windows, e.g. with `labmanager`
  * Enter the main type: `fr.utbm.ciad.labmanager.LabManagerApplication`
  * In the "Arguments" tab, enter the following command-line option for the Virtual Machine : `-Dspring.profiles.active=dev`, where `dev` is the name of the configuration with an `application-dev.yml` file.
  * Click on the `Run` button for saving the launch configuration and launching the regular Java application

### 1.4. Initial database schema creation

By default, JPA is creating the database schema.
The project is configured for creating automatically a schema in the database from the JPA entities.

#### 1.4.1. Injecting data from data.json

The project is configured for inserting automatically the data that is described into a specific JSON file in a folder that is specified in the developer YAML confiuration file (see above).

For injecting initial data in the database, you have to follow the steps:

* Copy the JSON into the folder specified in the developer YAML configuration
* Launch the project in Eclipse
* Wait for successful project launching, and then stop the run.
* Delete the JSON file

Now you database contains the base data that is validated by the project development team.

#### 1.4.2. Injecting data from data.zip

The previous JSON-based data injection approach works well for populating the database tables. However, extra data is not injected, such as the images or the PDF files associated to the entities in the database.
To inject a complete set of data (with associated files) in the database and the associated folders, you could use an archive file named `data.zip` that contains:

* a file named `dbcontent.json` that is the JSON file mentionned above.
* a set of folders and files that must be uploaded on the server.

For injecting initial data in the database, you have to follow the steps:

* Copy the ZIP file into the folder specified in the developer YAML configuration
* Launch the project in Eclipse
* Wait for successful project launching, and then stop the run.
* Delete the ZIP file

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
public void forEachAttribute(...) {
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

**Because you have added a column for the JPA entity, it is mandatory to reset the database. You have to delete completely the database and create it again.**


## 3. Merge your contributions to the Master server

### 3.1. Project Software License

All your contributions to the project will be distributed under the project license. See [LICENSE](COPYRIGHT.txt) for more details.
You must accept this license for pushing your contributions to the project.

### 3.2. Pushing your contributions

As soon as all your contributions are committed on your fork Git repository, it is possible to provide your changes to the Master development team through a *Pull Request* (PR) on Github:

* Open the Master Git Repository on [Github](https://github.com/gallandarakhneorg/labmanager)
* Open the PR page
* Create a new PR
* Select your fork Git repository as the source of the PR
* Fill-up the PR form
* Submit

Your PR will be reviewed by the Master development team to determine if it will be accepted or rejected.


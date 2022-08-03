# Research Laboratory Online Management Tools

This project is the backend implementation of the online tools for managing the database of a research laboratory.

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

### 1.6. Create JUnit5 tests 

The guideline of the Master development team recommend to create JUnit5 tests for all the significant contributions. You must create your tests into the source folder `src/test/java` and copy additionnal testing resources into the source folder `src/test/resources`.

For launching the tests, you could create and use a specific launch configuration of type `JUnit`:

* Open the menu `Run > Run configurations`
* Select the type `JUnit` and click on the `Create new launch configuration` button at the top of the list
* Enter the name of the launch configuration on the right part of the wizard windows, e.g. with `All tests`
* Select `Run all tests in the selected project, package or source folder`
* Select the `labmanager` project below
* Ensure that the test runner is `JUnit 5`
* Click on the `Run` button for saving the launch configuration and launching the tests

### 1.7. Merge your contributions to the Master server

As soon as all your contributions are comitted on your fork Git repository, it is possible to provide your changes to the Master development team through a *Pull Request* (PR) on Bitbucket:

* Open the Master Git Repository on [Bitbucket](https://bitbucket.org/ciadlabfr/labmanager-server-spring/)
* Open the PR page
* Create a new PR
* Select your fork Git repository as the source of the PR
* Fill-up the PR form

## 2. Model Definition

The model of the application is based on the definition of JEE entities that represent the data managed by the software.

### 2.1 Research Organizations

Research organizations are structure that have the role to do Research tasks. They may by *universities*, *faculties*, *laboratories*, *teams*, etc.
A research origanisation has several information associated to it: acronym, name, description, url...
It has also a list of members, which are described in Section 2.2.

In addition, a hierarchical structure may exist between different research organizations. Therefore, an organization may be composed of other organizations.

![UML - Entities - Organizations](./resources/Entities_Organizations.png)

### 2.2 Persons

Persons are involved in a research organization with different possible status (full professor, associate profesor, PhD student...). Each person is associated to different informations (name, gender, email, ORCID number...) that are usually considered for building activity reports for a research unit.

As it is briefly explained in the previous section, a person may be involved in a research organization. The relationship is supported by the concept of *membership*. This membership is for a specific time windows, and for a position (or member status).

![UML - Entities - Persons](./resources/Entities_Persons.png)

### 2.3 Journals

In academic publishing, a (scientific) journal is a periodical publication intended to further the progress of science, usually by reporting new research. Articles in journals are mostly written by active scientists such as students, researchers and professors instead of professional journalists. There are thousands of scientific journals in publication, and many more have been published at various points in the past. Most journals are highly specialized, although some of the oldest journals such as Nature publish articles and scientific papers across a wide range of scientific fields. Scientific journals contain articles that have been peer reviewed, in an attempt to ensure that articles meet the journal's standards of quality, and scientific validity.

Journal ranking is widely used in the evaluation of an academic journal's impact and quality. Journal rankings are intended to reflect the place of a journal within its field, the relative difficulty of being published in that journal, and the prestige associated with it. They have been introduced as official research evaluation tools in several countries.
Consequently, the project consider different ranking scores for the journals:

* *Impact factor* and CiteScore: reflecting the average number of citations to articles published in journals.
* *SCImago Quartile*: Each scientific field of the journals is divided into four quartiles: Q1, Q2, Q3, Q4. Q1 is occupied by the top 25% of journals in the list; Q2 is occupied by journals in the 25 to 50% group; Q3 is occupied by journals in the 50 to 75% group and Q4 is occupied by journals in the 75 to 100% group. This quartile is based on the Scimago Journal Rank (SJR), which is the numeric score associated to the journal.
* *Web-of-Science Quartile* As for Scimago, Web-of-Science also ranks the journals in four different quartiles.

Open access (OA) is a set of principles and a range of practices through which research outputs are distributed online, free of access charges or other barriers. Several journals are OA and others are not. This information is stored into the journal entity.

![UML - Entities - Journals](./resources/Entities_Journals.png)

### 2.4 Publications

Academic publishing is the subfield of publishing which distributes academic research and scholarship. Most academic work is published in academic journal articles, conferences, books or theses. Most scientific and scholarly journals, and many academic and scholarly books, though not all, are based on some form of peer review or editorial refereeing to qualify texts for publication. Different types of academic publishing may be condidered, including:

* Academic paper (also called scholarly paper), which is in academic journals or conferences, and contains original research results or reviews existing results or shows a totally new invention;
* Position paper, an essay that represents the author's opinion;
* Thesis or dissertation, a document submitted in support of a candidature for a degree or professional qualification, presenting the author's research and findings;
* Book or book chapters.

![UML - Entities - Publications](./resources/Entities_Publications.png)

Consquently, a publication (i.e., the result of an academic publishing) is associated to several information, such as its title, abstract text, keywords, DOI number, etc. Each publication is also associated to an ordered list of persons, who are considered as authors (through the `Authorship` type). EAch publication has also a type as defined in the following table.

| Type                | Scopes                            |
| --------------------|-----------------------------------|
| Journal paper       | International or national         |
| Conference paper    | International or national         |
| Oral communication  | International or national         |
| Poster              | International or national         |
| Book                | International or national         |
| Book chapter        | International or national         |
| Journal editing     | International or national         |
| Keynote             | International or national         |
| Thesis              | HDR, PhD, Master                  |
| Patent              | International, european, national |
| Artistic production | Theory of Art                     |
| Reports             | Tech., transfert, documentation   |
| Research tools      | Software...                       |


Each publication type is associated to a category that is defined by the French research agencies (ANR, HCERES, CNU): 
* ACL: Articles in international or national journals with selection commitee and ranked in international databases.
* ACLN: Articles in international or national journals with selection committee and not ranked in international databases.
* ASCL: Articles in international or national journals without selection committee.
* C_ACTI: Papers in the proceedings of an international conference.
* C_ACTN: Papers in the proceedings of a national conference.
* C_COM: Oral Communications without proceeding in international or national conference.
* C_AFF: Posters in international or national conference.
* OS: Scientific books.
* COS: Chapters in scientific books.
* DO: Editor of books or journals.
* C_INV: Keynotes in international or national conference, or seminars.
* TH: HDR, PhD or Master theses.
* PT: Publications for research transfer.
* BRE: Patents.
* OR: Research tools.
* OV: Books for scientific culture dissemination.
* COV: Chapters in a book for scientific culture dissemination.
* PV: Papers for scientific culture dissemination.
* PAT: Artistic research productions.
* AP: Other productions.


## 3. Software License

```
Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
All rights reserved.

This software is the confidential and proprietary information
of the CIAD laboratory and the Université de Technologie
de Belfort-Montbéliard ("Confidential Information").
You shall not disclose such Confidential Information and shall use
it only in accordance with the terms of the license agreement
you entered into with the CIAD-UTBM.

http://www.ciad-lab.fr/
```


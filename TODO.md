# To-Do for Developpers

* Application extensions:
  * Welcome page that may contain configuration panels for each user
  * Connection form 2 CAS (UTBM or UB) + send a query to the administrators for the creation of an account if it is not already created in LabManager (but CAS authentifcation is successfull)
  * Replace Vaadin Grid by Vaadin VGrid (See the Maven dependency below)
		
		<dependency>
		   <groupId>in.virit</groupId>
		   <artifactId>viritin</artifactId>
		   <version>2.8.5</version>
		</dependency>
  * Inconsistency detectors:
    * Invalid number of authors for a publication....
	* Notification of inconsistencies on the welcome page
	* No ORCID for persons
	* No DOI for publications
  * Access rights to pages
    * Review the access rights to the Vaadin pages according to the role of the authenticated user

* Back-end services for front-end components:
  * Person's card for the public page
  * Person biography (if public biography)
* Front-end components:

* Rest API to get data in JSON format:
  * ~~Person's card for the public page~~
  * ~~Person biography (if public biography)~~
  * ~~Person Publication Viewer~~
  * ~~Person invitations Viewer~~
  * ~~Person Jury memberships Viewer~~
  * ~~Person memberships Viewer~~
  * ~~Person Supervisions Viewer~~
  * ~~Lab Publication Viewer~~
  * ~~Member list Viewer~~
  * ~~Organization address Viewer~~
  * ~~Public component for showing the global indicators~~
  * ~~Projects (public description - page)~~

The following points must be discussed:
  * Projects (gallery)
  * Projects (partners banner)
  * Projects (banner)

The full list of endpoints available can be found at the URL: <contextPath>/api/swagger-ui/index.html
  
* Front-end
  * Create a front-end. Either stay on Vaadin on the lab manager project or create public views on another project
with the help of a modern web framework. Use the Rest API and create components.
  * Generate rest API documentation with swagger/OpenAPI + code generation with openAPI generator.


* Admin tools:
  * Listing/Edition/Deletion of memberships
  * Private component for selecting the public global indicators
  * Tool for extending the contract of a person (with a potential change of position type)
  * Tool for creating the user account on the different platforms
	* LabManager
	* Sympa mailing lists
	* MS Teams
  * Charts:
	* projects
	* publications
	* members
  * Merge similar entities
	* Persons
	* organizations
	* journals
	* conferences
	* Publications
* Exports:
  * Export Pubs to HAL
  * Annual activity report Excel for CIAD UB (Voir besoins à l'UB)
  * Activity report Word for REPEC-C3

# To-Do for production

* Update exports:
  * Application users
  
	   "applicationUsers": [
	      {
	         "@id": "/auser#0",
	         "login": "sgalland",
	         "role": "admin",
	         "person": {
	            "@id": "/pers#8"
	         }
	      }
	   ],  
  
  * Super organizations of CIAD
  
       "superOrganizations" : [
            {
               "@id" : "/ro#1"
            },
            {
               "@id" : "/ro#2"
            }
         ],
  
* Memberships for CN, AB and SG (direction change)
* Review and update of all the organizations in memberships
* Add idhal
* Remplacer dans certains projets recents le financement PIA par France 2030
* Revoir la structure entre les organizations UBFC, UTBM, UB, UFC
* Reclassifier les publications de type AP pour intégrer les nouveaux types de publications
* Move some person emails from primary to secondary

# To-Do for Developpers

* Application extensions:
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
* Cleaning:
  * Clean code `fr.utbm.ciad.labmanager.configuration.Constants`
  * Remove unnecessary functions from services
* Front-end components:
  * Person's card for the public page
  * Person biography (if public biography)
  * Lab Publication Viewer
  * Person Publication Viewer
  * Person invitations Viewer
  * Person Jury memberships Viewer
  * Person Supervisions Viewer
  * Member list Viewer
  * Organization address Viewer
  * Public component for showing the global indicators
  * Projects (banner)
  * Projects (public description - page)
  * Projects (partners banner)
  * Projects (gallery)
* Admin tools:
  * Listing/Edition/Deletion of memberships
  * Private component for selecting the public global indicators
  * Regenerate thumbnails images of publications
  * Tool for extending the contract of a person (with a potential change of position type)
  * Tool for creating the user account on the different platforms
	* LabManager
	* Sympa mailing lists
	* MS Teams
  * Charts:
	* projects
	* publications
	* members
  * Update journal rankings
  * Update conference rankings
  * Update person rankings
  * Merge similar entities
	* Persons
	* organizations
	* journals
	* conferences
	* Publications
* Imports:
  * Import Pubs from BibTeX
  * Import Pubs from RIS
  * Import Pubs from JSON
* Exports:
  * Export Pubs to HAL
  * Annual activity report Excel for UTBM
  * Annual activity report Excel for UB
  * Annual activity report Excel for Carnot
  * Annual activity report Excel for SPIM
  * Annual activity report Excel for PPST
  * Annual activity report Excel for CIAD UB (Voir besoins à l'UB)
* Tests
  * Extend the Unit Tests and Integration Tests.

# To-Do for production

* Memberships for CN, AB and SG (direction change)
* Review and update of all the organizations in memberships
* Add idhal
* PIA -> France 2030

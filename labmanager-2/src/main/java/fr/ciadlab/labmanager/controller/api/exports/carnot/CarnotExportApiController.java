/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the CIAD laboratory and the Université de Technologie
 * de Belfort-Montbéliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD-UTBM.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.controller.api.exports.carnot;

import java.io.IOException;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.assostructure.AssociatedStructureType;
import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.entities.project.ProjectActivityType;
import fr.ciadlab.labmanager.entities.project.ProjectBudget;
import fr.ciadlab.labmanager.entities.project.ProjectContractType;
import fr.ciadlab.labmanager.entities.project.ProjectMember;
import fr.ciadlab.labmanager.entities.project.ProjectStatus;
import fr.ciadlab.labmanager.entities.publication.PublicationCategory;
import fr.ciadlab.labmanager.entities.supervision.Supervision;
import fr.ciadlab.labmanager.service.assostructure.AssociatedStructureService;
import fr.ciadlab.labmanager.service.member.MembershipService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.service.project.ProjectService;
import fr.ciadlab.labmanager.service.publication.PublicationService;
import fr.ciadlab.labmanager.service.supervision.SupervisionService;
import fr.ciadlab.labmanager.utils.trl.TRL;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/** REST Controller for exporting to the Carnot institution.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@RestController
@CrossOrigin
public class CarnotExportApiController extends AbstractApiController {

	private static final String LAB_COLUMN = "Laboratoire de rattachement"; //$NON-NLS-1$

	private static final String NAME_COLUMN = "Nom"; //$NON-NLS-1$

	private static final String FIRSTNAME_COLUMN = "Prénom"; //$NON-NLS-1$

	private static final String OTHER_LAB_COLUMN = "Rattachement à autre(s) laboratoire(s)"; //$NON-NLS-1$

	private static final String PERMANENT_STAFF_COLUMN = "Personnel permanent (oui/non)"; //$NON-NLS-1$

	private static final String POSITION_COLUMN = "Corps Grade"; //$NON-NLS-1$

	private static final String HDR_COLUMN = "HDR"; //$NON-NLS-1$

	private static final String EMPLOYER_COLUMN = "Employeur"; //$NON-NLS-1$

	private static final String OTHER_EMPLOYER_COLUMN = "Si Employeur = \"HORS TUTELLE iC ARTS\" précisez ici"; //$NON-NLS-1$

	private static final String MONTHS_COLUMN = "Présence au laboratoire sur l'année {0} en mois"; //$NON-NLS-1$

	private static final String ACRONYM_COLUMN = "Acronyme"; //$NON-NLS-1$

	private static final String PROJECT_NAME_COLUMN = "Institulé"; //$NON-NLS-1$

	private static final String BUDGET_COLUMN = "Montant"; //$NON-NLS-1$

	private static final String FUNDER_COLUMN = "Financeur"; //$NON-NLS-1$

	private static final String CONTRACT_TYPE_COLUMN = "Type de contrat"; //$NON-NLS-1$

	private static final String HAS_PHD_THESIS_COLUMN = "Thèse ?"; //$NON-NLS-1$

	private static final String CATEGORY_COLUMN = "Category"; //$NON-NLS-1$

	private static final String TRL_COLUMN = "TRL"; //$NON-NLS-1$

	private static final String YES_VALUE = "OUI"; //$NON-NLS-1$

	private static final String NO_VALUE = "NON"; //$NON-NLS-1$

	private static final String PR_VALUE = "PR ou éq."; //$NON-NLS-1$

	private static final String MCF_VALUE = "MCF ou éq."; //$NON-NLS-1$

	private static final String DR_VALUE = "DR ou éq."; //$NON-NLS-1$

	private static final String CR_VALUE = "CR ou éq."; //$NON-NLS-1$

	private static final String TEACHER_VALUE = "ENSEIGNANT"; //$NON-NLS-1$

	private static final String POSTDOC_VALUE = "POST-DOC"; //$NON-NLS-1$

	private static final String ENGINEER_VALUE = "INGENIEUR"; //$NON-NLS-1$

	private static final String CIFRE_VALUE = "DOCTORANT CIFRE"; //$NON-NLS-1$

	private static final String PHD_STUDENT_VALUE = "DOCTORANT"; //$NON-NLS-1$

	private static final String UB_EMPLOYER_VALUE = "Univ. Bourgogne"; //$NON-NLS-1$

	private static final String UTBM_EMPLOYER_VALUE = "UTBM"; //$NON-NLS-1$

	private static final String OTHER_EMPLOYER_VALUE = "HORS TUTELLE iC ARTS"; //$NON-NLS-1$

	private static final String PI_TYPE_VALUE = "PI : Propriété Intellectuelle"; //$NON-NLS-1$
	
	private static final String PR_TYPE_VALUE = "PR : Prestation"; //$NON-NLS-1$

	private static final String RCD_TYPE_VALUE = "RCD : recherche contractuelle directe"; //$NON-NLS-1$

	private static final String RCO_TYPE_VALUE = "RCO : recherche contractuelle collaborative"; //$NON-NLS-1$

	private static final String NOT_ELIGIBLE_VALUE = "NE : Non éligible"; //$NON-NLS-1$

	private static final String RF_TYPE_VALUE = "RF : Recherche Fondamentale"; //$NON-NLS-1$

	private static final String RA_TYPE_VALUE = "RA : Recherche Appliquée"; //$NON-NLS-1$

	private static final String DE_TYPE_VALUE = "DE : Développement Expérimentale"; //$NON-NLS-1$

	private static final String TRL1_VALUE = "1 - Principes de base obsevés et rapportés"; //$NON-NLS-1$

	private static final String TRL2_VALUE = "2 - Concepts ou applications de la technologie formulée"; //$NON-NLS-1$

	private static final String TRL3_VALUE = "3 - Fonction critique analysée et expérimentée ou preuve caractéristique du concept"; //$NON-NLS-1$

	private static final String TRL4_VALUE = "4 - Validation en laboratoire du composant oude l'artefact produit"; //$NON-NLS-1$

	private static final String TRL5_VALUE = "5 - Validation dans un environnement significatif du composant ou de l'artefact produit"; //$NON-NLS-1$

	private static final String TRL6_VALUE = "6 - Démonstration du modèle système / Sous système ou du prototype dans un environnement significatif"; //$NON-NLS-1$

	private static final String TRL7_VALUE = "7 - Démonstration du système prototype en environnement opérationnel"; //$NON-NLS-1$

	private static final String TRL8_VALUE = "8 - Système réel complet et vol de qualification à travers des tests et des démonstrations"; //$NON-NLS-1$

	private static final String TRL9_VALUE = "9 - Système réel prouvé à travers des opérations / Missions réussies"; //$NON-NLS-1$

	private static final String PHD_THESIS_SUPERVISION_VALUE = "T : Thèse"; //$NON-NLS-1$

	private static final String CIFRE_SUPERVISION_VALUE = "C : CIFRE"; //$NON-NLS-1$

	private static final String POSTDOC_SUPERVISION_VALUE = "P : Post Doctorat"; //$NON-NLS-1$

	private static final String PUBS_SYNTHESIS_LABEL = "Nombre de publications de rang A dans l'année :"; //$NON-NLS-1$

	private static final String COMPANIES_SYNTHESIS_LABEL = "Nombre de sociétés créées dans l'année :"; //$NON-NLS-1$

	private static final String LABS_SYNTHESIS_LABEL = "Nombre de laboratoires communs créés dans l'année :"; //$NON-NLS-1$

	private SupervisionService supervisionService;

	private MembershipService membershipService;

	private PublicationService publicationService;

	private AssociatedStructureService associatedStructureService;

	private ProjectService projectService;

	private ResearchOrganizationService organizationService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param constants the constants of the app.
	 * @param supervisionService the supervision service.
	 * @param membershipService the membership service.
	 * @param projectService the project service.
	 * @param publicationService the publication service.
	 * @param associatedStructureService the service for accessing the associated structures.
	 * @param organizationService the service for accessing the organizations.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public CarnotExportApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired SupervisionService supervisionService,
			@Autowired MembershipService membershipService,
			@Autowired ProjectService projectService,
			@Autowired PublicationService publicationService,
			@Autowired AssociatedStructureService associatedStructureService,
			@Autowired ResearchOrganizationService organizationService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.supervisionService = supervisionService;
		this.membershipService = membershipService;
		this.projectService = projectService;
		this.publicationService = publicationService;
		this.associatedStructureService = associatedStructureService;
		this.organizationService = organizationService;
	}

	/**
	 * Export the persons who must be declared as part of the laboratory to the IC ARTS institution.
	 *
	 * @param organization the identifier or the name of the organization for which the members must be extracted.
	 * @param year the reference year.
	 * @return the CSV content.
	 * @throws Exception if it is impossible to redirect to the error page.
	 */
	@GetMapping(value = "/exportCarnotAnnualMembers")
	@ResponseBody
	public ResponseEntity<String> exportCarnotAnnualMembers(
			@RequestParam(required = true) String organization,
			@RequestParam(required = true) int year) throws Exception {
		final ResearchOrganization organizationObj = getOrganizationWith(organization, this.organizationService);
		if (organizationObj == null) {
			throw new IllegalArgumentException("Organization not found for: " + organization); //$NON-NLS-1$
		}
		//
		final String content = exportEtpCsv(organizationObj, year);
		//
		BodyBuilder bb = ResponseEntity.ok().contentType(MIME_TYPE_CSV);
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
		bb = bb.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" //$NON-NLS-1$
				+ Constants.DEFAULT_MEMBERS_ATTACHMENT_BASENAME
				+ "_" + simpleDateFormat.format(new Date()) + ".csv\""); //$NON-NLS-1$ //$NON-NLS-2$
		return bb.body(content);
	}

	/**
	 * Export the projects which must be declared for a given year to the IC ARTS institution.
	 *
	 * @param organization the identifier or the name of the organization for which the synthesis must be extracted.
	 * @param year the reference year.
	 * @return the CSV content.
	 * @throws Exception if it is impossible to redirect to the error page.
	 */
	@GetMapping(value = "/exportCarnotAnnualSynthesis")
	@ResponseBody
	public ResponseEntity<String> exportCarnotAnnualSynthesis(
			@RequestParam(required = true) String organization,
			@RequestParam(required = true) int year) throws Exception {
		final ResearchOrganization organizationObj = getOrganizationWith(organization, this.organizationService);
		if (organizationObj == null) {
			throw new IllegalArgumentException("Organization not found for: " + organization); //$NON-NLS-1$
		}
		//
		final String content = exportSynthesisCsv(organizationObj, year);
		//
		BodyBuilder bb = ResponseEntity.ok().contentType(MIME_TYPE_CSV);
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
		bb = bb.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" //$NON-NLS-1$
				+ Constants.DEFAULT_SYNTHESIS_ATTACHMENT_BASENAME
				+ "_" + simpleDateFormat.format(new Date()) + ".csv\""); //$NON-NLS-1$ //$NON-NLS-2$
		return bb.body(content);
	}

	/**
	 * Export the annual synthesis to the IC ARTS institution.
	 *
	 * @param organization the identifier or the name of the organization for which the projects must be extracted.
	 * @param year the reference year.
	 * @return the CSV content.
	 * @throws Exception if it is impossible to redirect to the error page.
	 * @since 3.6
	 */
	@GetMapping(value = "/exportCarnotAnnualProjects")
	@ResponseBody
	public ResponseEntity<String> exportCarnotAnnualProjects(
			@RequestParam(required = true) String organization,
			@RequestParam(required = true) int year) throws Exception {
		final ResearchOrganization organizationObj = getOrganizationWith(organization, this.organizationService);
		if (organizationObj == null) {
			throw new IllegalArgumentException("Organization not found for: " + organization); //$NON-NLS-1$
		}
		//
		final String content = exportProjectCsv(organizationObj, year);
		//
		BodyBuilder bb = ResponseEntity.ok().contentType(MIME_TYPE_CSV);
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
		bb = bb.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" //$NON-NLS-1$
				+ Constants.DEFAULT_PROJECTS_ATTACHMENT_BASENAME
				+ "_" + simpleDateFormat.format(new Date()) + ".csv\""); //$NON-NLS-1$ //$NON-NLS-2$
		return bb.body(content);
	}

	/** Export the annual synthesis in a CSV file that corresponds to the Carnot standard.
	 * 
	 * @param organization the organization for which the synthesis must be extracted.
	 * @param year the reference year that is used for filtering the publications.
	 * @return the CSV content.
	 * @throws IOException if the CSV cannot be generated.
	 */
	protected String exportSynthesisCsv(ResearchOrganization organization, int year) throws IOException {
		String csv = null;
		final StringWriter stream = new StringWriter();
		final CSVWriterBuilder builder = new CSVWriterBuilder(stream);
		try (final ICSVWriter writer = builder.build()) {
			// Publications
			final long pubs = this.publicationService.getPublicationsByOrganizationId(organization.getId(), true, false).stream()
				.filter(it -> it.getPublicationYear() == year
					&& (it.getCategory() == PublicationCategory.OS
						|| ((it.getCategory() == PublicationCategory.C_ACTI || it.getCategory() == PublicationCategory.ACL) && it.isRanked())))
				.count();
			writer.writeNext(new String[] {
					PUBS_SYNTHESIS_LABEL,
					Long.toString(pubs),
			});
			// Created companies
			final long companies = this.associatedStructureService.getAssociatedStructuresByOrganizationId(organization.getId()).stream()
				.filter(it -> it.getCreationDate().getYear() == year && it.getType() == AssociatedStructureType.PRIVATE_COMPANY)
				.count();
			writer.writeNext(new String[] {
					COMPANIES_SYNTHESIS_LABEL,
					Long.toString(companies),
			});
			// Created labs
			final long labs = this.associatedStructureService.getAssociatedStructuresByOrganizationId(organization.getId()).stream()
					.filter(it -> it.getCreationDate().getYear() == year
						&& (it.getType() == AssociatedStructureType.INTERNATIONAL_RESEARCH_LAB
								|| it.getType() == AssociatedStructureType.NATIONAL_RESEARCH_LAB))
					.count();
			writer.writeNext(new String[] {
					LABS_SYNTHESIS_LABEL,
					Long.toString(labs),
			});
			csv = stream.toString();
		}
		return Strings.nullToEmpty(csv);
	}

	/** Export the projects in a CSV file that corresponds to the Carnot standard.
	 * The columns of the CSV are:<ul>
	 * <li>Acronyme</li>
	 * <li>Intitulé</li>
	 * <li>Montant</li>
	 * <li>Financeur</li>
	 * <li>Type de contrat</li>
	 * <li>Thèse ?</li>
	 * <li>Catégorie</li>
	 * <li>TRL</li>
	 * </ul>
	 * 
	 * @param organization the organization for which the publications must be extracted.
	 * @param year the reference year that is used for filtering the publications.
	 * @return the CSV content.
	 * @throws IOException if the CSV cannot be generated.
	 */
	protected String exportProjectCsv(ResearchOrganization organization, int year) throws IOException {
		final List<String[]> content = this.projectService.getAllProjects().stream()
				.filter(it -> it.getStartDate().getYear() == year && it.getStatus() == ProjectStatus.ACCEPTED)
				.map(it -> {
					return new String[] {
							it.getAcronym(),
							it.getScientificTitle(),
							Integer.toString((int) it.getTotalLocalOrganizationBudget() * 1000),
							funders(it),
							projectType(it.getContractType()),
							projectPhD(organization, it),
							projectType(it.getActivityType()),
							projectTrl(it.getTRL()),
					};
				}).collect(Collectors.toList());
		//
		String csv = null;
		final StringWriter stream = new StringWriter();
		final CSVWriterBuilder builder = new CSVWriterBuilder(stream);
		try (final ICSVWriter writer = builder.build()) {
			writer.writeNext(new String[] {
					ACRONYM_COLUMN,
					PROJECT_NAME_COLUMN,
					BUDGET_COLUMN,
					FUNDER_COLUMN,
					CONTRACT_TYPE_COLUMN,
					HAS_PHD_THESIS_COLUMN,
					CATEGORY_COLUMN,
					TRL_COLUMN,
			});
			writer.writeAll(content);
			csv = stream.toString();
		}
		return Strings.nullToEmpty(csv);
	}

	/** Replies the type of supervision for the project.
	 *
	 * @param organization the organization of the proejct.
	 * @param project the project to analyze.
	 * @return the string representing the type of supervision.
	 */
	protected String projectPhD(ResearchOrganization organization, Project project) {
		final LocalDate startDate = project.getStartDate();
		final LocalDate endDate = project.getEndDate();
		for (final ProjectMember member : project.getParticipants()) {
			final Person person = member.getPerson();
			final Optional<Membership> mbr = this.membershipService.getMemberships(organization.getId(), person.getId()).stream()
				.filter(it -> it.isActiveIn(startDate, endDate))
				.findAny();
			if (mbr.isPresent()) {
				final Membership membership = mbr.get();
				if (membership.getMemberStatus() == MemberStatus.POSTDOC) {
					return POSTDOC_SUPERVISION_VALUE;
				}
				if (membership.getMemberStatus() == MemberStatus.PHD_STUDENT) {
					if (isCifre(membership)) {
						return CIFRE_SUPERVISION_VALUE;
					}
					return PHD_THESIS_SUPERVISION_VALUE;
				}
			}
		}
		return ""; //$NON-NLS-1$
	}

	/** Replies the TRL of project.
	 *
	 * @param trl the TRL.
	 * @return the string representing the TRL.
	 */
	protected static String projectTrl(TRL trl) {
		switch (trl) {
		case TRL1:
			return TRL1_VALUE;
		case TRL2:
			return TRL2_VALUE;
		case TRL3:
			return TRL3_VALUE;
		case TRL4:
			return TRL4_VALUE;
		case TRL5:
			return TRL5_VALUE;
		case TRL6:
			return TRL6_VALUE;
		case TRL7:
			return TRL7_VALUE;
		case TRL8:
			return TRL8_VALUE;
		case TRL9:
			return TRL9_VALUE;
		default:
			//
		}
		return ""; //$NON-NLS-1$
	}

	/** Replies the type of project's activity.
	 *
	 * @param type the activity type.
	 * @return the type of project's activity.
	 */
	protected static String projectType(ProjectActivityType type) {
		switch (type) {
		case APPLIED_RESEARCH:
			return RA_TYPE_VALUE;
		case EXPERIMENTAL_DEVELOPMENT:
			return DE_TYPE_VALUE;
		case FUNDAMENTAL_RESEARCH:
			return RF_TYPE_VALUE;
		default:
			//
		}
		return ""; //$NON-NLS-1$
	}

	/** Replies the type of project.
	 *
	 * @param type the type.
	 * @return the type of project.
	 */
	protected static String projectType(ProjectContractType type) {
		switch (type) {
		case PI:
			return PI_TYPE_VALUE;
		case PR:
			return PR_TYPE_VALUE;
		case RCD:
			return RCD_TYPE_VALUE;
		case RCO:
			return RCO_TYPE_VALUE;
		case NOT_SPECIFIED:
		default:
			return NOT_ELIGIBLE_VALUE;
		}
	}

	/** Replies the list of the funders for the given project.
	 *
	 * @param project the project to analyze.
	 * @return the list of funders.
	 */
	protected static String funders(Project project) {
		final StringBuilder buf = new StringBuilder();
		for (final ProjectBudget budget : project.getBudgets()) {
			if (buf.length() > 0) {
				buf.append(", "); //$NON-NLS-1$
			}
			switch (budget.getFundingScheme()) {
			case CIFRE:
			case EU_COMPANY:
			case EU_UNIVERSITY:
			case FRENCH_COMPANY:
			case FRENCH_OTHER:
			case FRENCH_UNIVERSITY:
			case INTERNATIONAL_COMPANY:
			case INTERNATIONAL_UNIVERSITY:
			case INTERNTATIONAL_OTHER:
			case EU_OTHER:
			case HOSTING_ORGANIZATION:
				buf.append(project.getCoordinator().getNameOrAcronym());
				break;
			case LOCAL_INSTITUTION:
				buf.append(project.getLearOrganization().getNameOrAcronym());
				break;
			case ADEME:
			case ANR:
			case CAMPUS_FRANCE:
			case CARNOT:
			case CONACYT:
			case COST_ACTION:
			case CPER:
			case CSC:
			case EDIH:
			case EUREKA:
			case EUROSTAR:
			case FEDER:
			case FITEC:
			case FUI:
			case H2020:
			case HORIZON_EUROPE:
			case IDEX:
			case INTERREG:
			case ISITE:
			case JPIEU:
			case LIFE:
			case NICOLAS_BAUDIN:
			case NOT_FUNDED:
			case PHC:
			case PIA:
			case REGION_BFC:
			case SELF_FUNDING:
				buf.append(budget.getFundingScheme().getLabel(Locale.FRANCE));
				break;
			default:
				break;
			}
		}
		return buf.toString();
	}

	/** Export the full-time equivalent persons in a CSV file that corresponds to the Carnot standard.
	 * The columns of the CSV are:<ul>
	 * <li>Laboratory</li>
	 * <li>Name</li>
	 * <li>Firstname</li>
	 * <li>Other labs</li>
	 * <li>Permanent staff</li>
	 * <li>Position</li>
	 * <li>HDR</li>
	 * <li>Organization</li>
	 * <li>Details on employer</li>
	 * <li>Number of months</li>
	 * </ul>
	 * 
	 * @param organization the organization for which the publications must be extracted.
	 * @param year the reference year that is used for filtering the publications.
	 * @return the CSV content.
	 * @throws IOException if the CSV cannot be generated.
	 */
	protected String exportEtpCsv(ResearchOrganization organization, int year) throws IOException {
		final LocalDate startDate = LocalDate.of(year, 1, 1);
		final LocalDate endDate = LocalDate.of(year, 12, 31);
		final List<String[]> content = organization.getMemberships().parallelStream()
				.filter(it -> it.isActiveIn(startDate, endDate) && membershipStatus(it) != null)
				.map(it -> {
					final List<ResearchOrganization> employers = extractEmployers(it);
					final String employerValue = employerValue(employers);
					return new String[] {
							organization.getAcronym(), // Laboratory
							it.getPerson().getLastName(),
							it.getPerson().getFirstName(),
							"", // No other lab //$NON-NLS-1$
							booleanValue(it.isPermanentPosition()),
							membershipStatus(it),
							booleanValue(it.getMemberStatus().isHdrOwner()),
							Strings.isNullOrEmpty(employerValue) ? OTHER_EMPLOYER_VALUE : employerValue,
							Strings.isNullOrEmpty(employerValue) ? otherEmployerValue(employers) : "", //$NON-NLS-1$
							monthsValue(startDate, endDate, it.getMemberSinceWhen(), it.getMemberToWhen()),
					};
				}).collect(Collectors.toList());
		//
		String csv = null;
		final StringWriter stream = new StringWriter();
		final CSVWriterBuilder builder = new CSVWriterBuilder(stream);
		try (final ICSVWriter writer = builder.build()) {
			writer.writeNext(new String[] {
					LAB_COLUMN,
					NAME_COLUMN,
					FIRSTNAME_COLUMN,
					OTHER_LAB_COLUMN,
					PERMANENT_STAFF_COLUMN,
					POSITION_COLUMN,
					HDR_COLUMN,
					EMPLOYER_COLUMN,
					OTHER_EMPLOYER_COLUMN,
					MessageFormat.format(MONTHS_COLUMN, Integer.valueOf(year)),
			});
			writer.writeAll(content);
			csv = stream.toString();
		}
		return Strings.nullToEmpty(csv);
	}

	/** Extract the employers from the memberships.
	 *
	 * @param membership the membership for which the status should be converted.
	 * @return the Carnot category.
	 */
	protected static List<ResearchOrganization> extractEmployers(Membership membership) {
		return membership.getPerson().getMemberships().parallelStream()
			.filter(it -> it.getId() != membership.getId() && it.getResearchOrganization().getType().isEmployer())
			.map(it -> it.getResearchOrganization())
			.collect(Collectors.toList());
	}

	/** Extract the employer value.
	 *
	 * @param employers the employers.
	 * @return the employer value.
	 */
	protected static String employerValue(List<ResearchOrganization> employers) {
		for (final ResearchOrganization organization : employers) {
			if (UTBM_EMPLOYER_VALUE.equalsIgnoreCase(organization.getAcronym())) {
				return UTBM_EMPLOYER_VALUE;
			}
			if ("UB".equalsIgnoreCase(organization.getAcronym())) { //$NON-NLS-1$
				return UB_EMPLOYER_VALUE;
			}
		}
		return null;
	}

	/** Extract the other employer value.
	 *
	 * @param employers the employers.
	 * @return the other employer value.
	 */
	protected static String otherEmployerValue(List<ResearchOrganization> employers) {
		StringBuilder buf = new StringBuilder();
		for (final ResearchOrganization organization : employers) {
			if (buf.length() > 0) {
				buf.append("; "); //$NON-NLS-1$
			}
			buf.append(organization.getAcronymOrName());
		}
		return buf.toString();
	}

	/** Convert the boolean value to its equivalent in the Carnot CSV.
	 *
	 * @param value the boolean value.
	 * @return the string representation of the boolean value.
	 */
	protected static String booleanValue(boolean value) {
		return value ? YES_VALUE : NO_VALUE;
	}

	/** Compute the number of months for the given time windows.
	 *
	 * @param yearStart the first date of the year.
	 * @param yearEnd the last date of the year.
	 * @param membershipStart the first date of the membership.
	 * @param membershipEnd the last date of the membership.
	 * @return the number of months.
	 */
	protected static String monthsValue(LocalDate yearStart, LocalDate yearEnd, LocalDate membershipStart, LocalDate membershipEnd) {
		int firstMonth = 1;
		if (membershipStart != null && membershipStart.isAfter(yearStart)) {
			firstMonth = membershipStart.getMonthValue();
		}
		int lastMonth = 12;
		if (membershipEnd != null && membershipEnd.isBefore(yearEnd)) {
			lastMonth = membershipEnd.getMonthValue();
		}
		final int numberOfMonths = lastMonth - firstMonth + 1; 
		return Integer.toString(numberOfMonths);
	}

	/** Replies if the given person has a CIFRE contract during the time windows.
	 *
	 * @param membership the membership for which the status should be converted.
	 * @return {@code true} if a CIFRE exists.
	 */
	protected boolean isCifre(Membership membership) {
		final List<Supervision> res = this.supervisionService.getSupervisionsForMembership(membership.getId());
		if (!res.isEmpty()) {
			final Supervision supervision = res.get(0);
			switch (supervision.getFunding()) {
			case CIFRE:
			case EU_COMPANY:
			case FRENCH_COMPANY:
			case INTERNATIONAL_COMPANY:
				return true;
			case ADEME:
			case ANR:
			case CAMPUS_FRANCE:
			case CARNOT:
			case CONACYT:
			case COST_ACTION:
			case CPER:
			case CSC:
			case EDIH:
			case EUREKA:
			case EUROSTAR:
			case EU_OTHER:
			case EU_UNIVERSITY:
			case FEDER:
			case FITEC:
			case FRENCH_OTHER:
			case FRENCH_UNIVERSITY:
			case FUI:
			case H2020:
			case HORIZON_EUROPE:
			case HOSTING_ORGANIZATION:
			case IDEX:
			case INTERNATIONAL_UNIVERSITY:
			case INTERNTATIONAL_OTHER:
			case INTERREG:
			case ISITE:
			case JPIEU:
			case LIFE:
			case LOCAL_INSTITUTION:
			case NICOLAS_BAUDIN:
			case NOT_FUNDED:
			case PHC:
			case PIA:
			case REGION_BFC:
			case SELF_FUNDING:
			default:
				//
			}
		}
		return false;
	}

	/** Convert the member status to its equivalent category for Carnot.
	 *
	 * @param membership the membership for which the status should be converted.
	 * @return the Carnot category.
	 */
	protected String membershipStatus(Membership membership) {
		switch (membership.getMemberStatus()) {
		case FULL_PROFESSOR:
			return PR_VALUE;
		case RESEARCH_DIRECTOR:
			return DR_VALUE;
		case ASSOCIATE_PROFESSOR:
		case ASSOCIATE_PROFESSOR_HDR:
		case CONTRACTUAL_RESEARCHER_TEACHER_PHD:
		case CONTRACTUAL_RESEARCHER_TEACHER:
			return MCF_VALUE;
		case TEACHER:
		case TEACHER_PHD:
			return TEACHER_VALUE;
		case RESEARCHER:
		case RESEARCHER_PHD:
			return CR_VALUE;
		case POSTDOC:
			return POSTDOC_VALUE;
		case PHD_STUDENT:
			return isCifre(membership) ? CIFRE_VALUE : PHD_STUDENT_VALUE;
		case ENGINEER:
		case ENGINEER_PHD:
		case RESEARCH_ENGINEER:
		case RESEARCH_ENGINEER_PHD:
			return ENGINEER_VALUE;
		// The following positions are not considered by Carnot
		case ADMIN:
		case ASSOCIATED_MEMBER:
		case ASSOCIATED_MEMBER_PHD:
		case EMERITUS_ASSOCIATE_PROFESSOR:
		case EMERITUS_ASSOCIATE_PROFESSOR_HDR:
		case EMERITUS_FULL_PROFESSOR:
		case MASTER_STUDENT:
		case OTHER_STUDENT:
		default:
			return null;
		}
	}

}

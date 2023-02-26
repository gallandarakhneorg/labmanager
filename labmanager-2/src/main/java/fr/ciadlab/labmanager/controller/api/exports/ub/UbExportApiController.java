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

package fr.ciadlab.labmanager.controller.api.exports.ub;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.assostructure.AssociatedStructureType;
import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.member.Responsibility;
import fr.ciadlab.labmanager.entities.organization.OrganizationAddress;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.entities.project.ProjectBudget;
import fr.ciadlab.labmanager.entities.project.ProjectCategory;
import fr.ciadlab.labmanager.entities.project.ProjectContractType;
import fr.ciadlab.labmanager.entities.project.ProjectStatus;
import fr.ciadlab.labmanager.entities.publication.PublicationCategory;
import fr.ciadlab.labmanager.entities.supervision.Supervision;
import fr.ciadlab.labmanager.entities.supervision.Supervisor;
import fr.ciadlab.labmanager.io.od.OdfSpreadsheetHelper;
import fr.ciadlab.labmanager.io.od.OdfSpreadsheetHelper.TableContentHelper;
import fr.ciadlab.labmanager.io.od.OdfSpreadsheetHelper.TableContentRowHelper;
import fr.ciadlab.labmanager.io.od.OdfSpreadsheetHelper.TableHelper;
import fr.ciadlab.labmanager.service.assostructure.AssociatedStructureService;
import fr.ciadlab.labmanager.service.invitation.PersonInvitationService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.service.project.ProjectService;
import fr.ciadlab.labmanager.service.publication.PublicationService;
import fr.ciadlab.labmanager.service.supervision.SupervisionService;
import fr.ciadlab.labmanager.utils.CountryCodeUtils;
import fr.ciadlab.labmanager.utils.funding.FundingScheme;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/** REST Controller for exporting to the uB university.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@RestController
@CrossOrigin
public class UbExportApiController extends AbstractApiController {

	private static final String TEMPLATE_PATH = "/ods/template_indicators_ub.ods"; //$NON-NLS-1$

	private static final String YEAR_TOKEN = "<YEAR>"; //$NON-NLS-1$

	private static final String NEXT_YEAR_TOKEN = "<NYEAR>"; //$NON-NLS-1$

	private static final String ACADEMIC_YEAR_TOKEN = "<AYEAR>"; //$NON-NLS-1$

	private static final String SYNTHESIS_TABLE_NAME = "Caractéristiques {0}"; //$NON-NLS-1$
	
	private static final String SCIENTIFIC_PRODUCTION_TABLE_NAME = "Production Scientifique {0}"; //$NON-NLS-1$

	private static final String HUMAN_RESOURCES_TABLE_NAME = "CIAD-RH {0}"; //$NON-NLS-1$

	private static final String PHD_STUDENT_TABLE_NAME = "Doctorants CIAD {0}"; //$NON-NLS-1$

	private static final String ACADEMIC_PROJECTS_TABLE_NAME = "Financements Crédits {0}"; //$NON-NLS-1$
	
	private static final String INDUSTRIAL_PROJECTS_TABLE_NAME = "Valorisation {0}"; //$NON-NLS-1$

	private static final String INVESTMENT_TABLE_NAME = "Budget Dépenses {0}"; //$NON-NLS-1$

	private static final String CONFERENCES_TABLE_NAME = "Colloques {0}"; //$NON-NLS-1$

	private static final String PROJECT_LIST_TABLE_NAME = "Programmes Nat.&Internat. {0}"; //$NON-NLS-1$

	private static final String HDR_DEGREE = "HDR"; //$NON-NLS-1$

	private static final String PHD_DEGREE = "Doctorat"; //$NON-NLS-1$

	private static final String MASTER_DEGREE = "Master ou diplôme d'ingénieur"; //$NON-NLS-1$

	private static final String PERMANENT_POSITION = "Titulaire"; //$NON-NLS-1$

	private static final String CDD_POSITION = "CDD"; //$NON-NLS-1$

	private static final String CDI_POSITION = "CDI"; //$NON-NLS-1$

	private static final String OTHER_POSITION = "Autre"; //$NON-NLS-1$

	private static final String EC_STATUS = "EC & Chercheur"; //$NON-NLS-1$

	private static final String ITRF_STATUS = "Techniciens & Administratifs"; //$NON-NLS-1$
	
	private static final String OTHER_STATUS = "Autre"; //$NON-NLS-1$
	
	private static final String UB_NAME = "uB"; //$NON-NLS-1$

	private static final String SAYENS_NAME = "Sayens"; //$NON-NLS-1$

	private static final String SPIM_NAME = "SPIM"; //$NON-NLS-1$

	private ProjectService projectService;

	private PublicationService publicationService;

	private SupervisionService supervisionService;

	private PersonInvitationService invitationService;

	private AssociatedStructureService associatedStructureService;

	private ResearchOrganizationService organizationService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param constants the constants of the app.
	 * @param projectService the project service.
	 * @param publicationService the service for the publications.
	 * @param supervisionService the service for the supervisions.
	 * @param invitationService the service for person invitations.
	 * @param associatedStructureService the service for associated structures.
	 * @param organizationService the service for accessing the organizations.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public UbExportApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ProjectService projectService,
			@Autowired PublicationService publicationService,
			@Autowired SupervisionService supervisionService,
			@Autowired PersonInvitationService invitationService,
			@Autowired AssociatedStructureService associatedStructureService,
			@Autowired ResearchOrganizationService organizationService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.projectService = projectService;
		this.publicationService = publicationService;
		this.supervisionService = supervisionService;
		this.invitationService = invitationService;
		this.associatedStructureService = associatedStructureService;
		this.organizationService = organizationService;
	}

	/**
	 * Export annual indicators to the uB requirements.
	 *
	 * @param organization the identifier or the name of the organization for which the publications must be extracted.
	 * @param year the reference year.
	 * @param username the name of the logged-in user.
	 * @return the document for the publications.
	 * @throws Exception if it is impossible to redirect to the error page.
	 * @since 3.6
	 */
	@GetMapping(value = "/exportUbAnnualIndicators")
	@ResponseBody
	public ResponseEntity<byte[]> exportUbAnnualIndicators(
			@RequestParam(required = true) String organization,
			@RequestParam(required = true) int year,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		readCredentials(username, "exportUbAnnualIndicators", organization, Integer.valueOf(year)); //$NON-NLS-1$
		final ResearchOrganization organizationObj = getOrganizationWith(organization, this.organizationService);
		if (organizationObj == null) {
			throw new IllegalArgumentException("Organization not found for: " + organization); //$NON-NLS-1$
		}
		//
		final byte[] content;
		final MediaType mediaType;
		final String filenameExtension;
		try (final OdfSpreadsheetHelper ods = new OdfSpreadsheetHelper(TEMPLATE_PATH)) {
			getLogger().info("Generating uB indicators' spreadsheet for synthesis"); //$NON-NLS-1$
			exportSynthesis(ods, organizationObj, year);

			getLogger().info("Generating uB indicators' spreadsheet for scientific production"); //$NON-NLS-1$
			exportScientificProduction(ods, organizationObj, year);

			getLogger().info("Generating uB indicators' spreadsheet for human resources"); //$NON-NLS-1$
			exportHumanResources(ods, organizationObj, year);

			getLogger().info("Generating uB indicators' spreadsheet for PhD students"); //$NON-NLS-1$
			exportPhDStudents(ods, organizationObj, year);

			getLogger().info("Generating uB indicators' spreadsheet for academic projects"); //$NON-NLS-1$
			exportAcademicProjects(ods, organizationObj, year);

			getLogger().info("Generating uB indicators' spreadsheet for investments"); //$NON-NLS-1$
			exportInvestments(ods, organizationObj, year);

			getLogger().info("Generating uB indicators' spreadsheet for industrial projects"); //$NON-NLS-1$
			exportIndustrialProjects(ods, organizationObj, year);

			getLogger().info("Generating uB indicators' spreadsheet for conferences"); //$NON-NLS-1$
			exportConferences(ods, organizationObj, year);

			getLogger().info("Generating uB indicators' spreadsheet for list of projects"); //$NON-NLS-1$
			exportProjectList(ods, organizationObj, year);

			getLogger().info("Generating spreadsheet bytes"); //$NON-NLS-1$
			content = ods.toByteArray();
			mediaType = ods.getMediaType();
			filenameExtension = ods.getFileExtension();
		}
		//
		BodyBuilder bb = ResponseEntity.ok().contentType(mediaType);
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
		bb = bb.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" //$NON-NLS-1$
				+ Constants.DEFAULT_UB_INDICATORS_ATTACHMENT_BASENAME
				+ "_" + simpleDateFormat.format(new Date()) + "." + filenameExtension + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return bb.body(content);
	}

	private static void getLegalInstitution(Set<ResearchOrganization> organizations, ResearchOrganization organization) {
		ResearchOrganization org = organization;
		while (org != null && !org.getType().isEmployer()) {
			org = org.getSuperOrganization();
		}
		if (org != null) {
			organizations.add(org);
		}
	}

	private static String toOrganizationList(Iterable<ResearchOrganization> organizations) {
		final StringBuilder buffer = new StringBuilder();
		for (final ResearchOrganization organization : organizations) {
			if (buffer.length() > 0) {
				buffer.append(", "); //$NON-NLS-1$
			}
			buffer.append(organization.getAcronymOrName());
		}
		return buffer.toString();
	}

	/** Fill up the PhD students table in a spreadsheet.
	 * 
	 * @param document the output document.
	 * @param organization the organization for which the publications must be extracted.
	 * @param year the reference year that is used for filtering the publications.
	 * @throws Exception if the Excel cannot be generated.
	 */
	protected void exportPhDStudents(OdfSpreadsheetHelper document, ResearchOrganization organization, int year) throws Exception {
		final String now = Integer.toString(year);
		final String academicNow = now + "-" + Integer.toString(year + 1); //$NON-NLS-1$
		final LocalDate startDate = LocalDate.of(year, 9, 1);
		final LocalDate endDate = LocalDate.of(year + 1, 8, 31);
		final TableHelper table = document.getTable(
				MessageFormat.format(PHD_STUDENT_TABLE_NAME, YEAR_TOKEN),
				MessageFormat.format(PHD_STUDENT_TABLE_NAME, now));

		table.getCell("A1").replace(ACADEMIC_YEAR_TOKEN, academicNow); //$NON-NLS-1$

		final TableContentHelper content = table.getContent(2);
		organization.getMemberships().stream()
			.filter(it -> it.isActiveIn(startDate, endDate) && it.getMemberStatus() == MemberStatus.PHD_STUDENT)
			.map(it -> {
				List<Supervision> supervisions = this.supervisionService.getSupervisionsForMembership(it.getId());
				return supervisions.isEmpty() ? null : supervisions.get(0);
			})
			.filter(it -> it != null)
			.forEach(it -> {
				final Membership mbr = it.getSupervisedPerson();
				final TableContentRowHelper row = content.appendRow();
				row.append(organization.getAcronymOrName());
				row.append(mbr.getMemberSinceWhen());
				row.append(mbr.getPerson().getGender().getCivilTitle(Locale.FRANCE));
				row.append(mbr.getPerson().getLastName());
				row.append(mbr.getPerson().getFirstName());
				row.append((String) null);
				row.append((String) null);
				row.append((String) null);
				row.append((String) null);
				row.append(mbr.getPerson().getEmail());
				final StringBuilder supervisors = new StringBuilder();
				for (final Supervisor supervisor : it.getSupervisors()) {
					if (supervisors.length() > 0) {
						supervisors.append(", "); //$NON-NLS-1$
					}
					supervisors.append(supervisor.getSupervisor().getFullName());
				}
				row.append(supervisors.toString());
				row.append(SPIM_NAME);
				row.append(MASTER_DEGREE);
				final StringBuilder funding = new StringBuilder();
				funding.append(it.getFunding().getLabel(Locale.FRENCH));
				final String fundingDetails = it.getFundingDetails();
				if (!Strings.isNullOrEmpty(fundingDetails)) {
					funding.append("; "); //$NON-NLS-1$
					funding.append(fundingDetails);
				}
				row.append(funding.toString());
				final ResearchOrganization employer = EntityUtils.getUniversityOrSchoolOrCompany(
						mbr.getPerson(),
						it0 -> it.getId() != it0.getId() && it0.isActiveIn(startDate, endDate));
				row.append(employer != null ? employer.getAcronymOrName() : null);
				row.append(mbr.getMemberSinceWhen());
				row.append(mbr.getMemberToWhen());
			});
	}

	/** Fill up the human resource table in a spreadsheet.
	 * 
	 * @param document the output document.
	 * @param organization the organization for which the publications must be extracted.
	 * @param year the reference year that is used for filtering the publications.
	 * @throws Exception if the Excel cannot be generated.
	 */
	@SuppressWarnings("static-method")
	protected void exportHumanResources(OdfSpreadsheetHelper document, ResearchOrganization organization, int year) throws Exception {
		final String now = Integer.toString(year);
		final LocalDate startDate = LocalDate.of(year, 1, 1);
		final LocalDate endDate = LocalDate.of(year, 12, 31);
		final TableHelper table = document.getTable(
				MessageFormat.format(HUMAN_RESOURCES_TABLE_NAME, YEAR_TOKEN),
				MessageFormat.format(HUMAN_RESOURCES_TABLE_NAME, now));

		table.getCell("A1").replace(YEAR_TOKEN, now); //$NON-NLS-1$

		final TableContentHelper content = table.getContent(2);
		organization.getMemberships().stream()
			.filter(it -> it.isActiveIn(startDate, endDate) && !it.getMemberStatus().isExternalPosition()
					&& it.getMemberStatus().getHierachicalLevel() < MemberStatus.PHD_STUDENT.getHierachicalLevel())
			.forEach(it -> {
				final TableContentRowHelper row = content.appendRow();
				row.append(organization.getAcronymOrName());
				row.append(it.getPerson().getGender().getCivilTitle(Locale.FRANCE));
				row.append(it.getPerson().getLastName());
				row.append(it.getPerson().getLastName());
				row.append(it.getPerson().getFirstName());
				row.append((String) null);
				row.append((String) null);
				switch (it.getMemberStatus()) {
				case ASSOCIATE_PROFESSOR_HDR:
				case EMERITUS_ASSOCIATE_PROFESSOR_HDR:
				case EMERITUS_FULL_PROFESSOR:
				case FULL_PROFESSOR:
				case RESEARCH_DIRECTOR:
					row.append(HDR_DEGREE);
					break;
				case ASSOCIATE_PROFESSOR:
				case EMERITUS_ASSOCIATE_PROFESSOR:
				case CONTRACTUAL_RESEARCHER_TEACHER_PHD:
				case ENGINEER_PHD:
				case POSTDOC:
				case RESEARCHER_PHD:
				case RESEARCH_ENGINEER_PHD:
				case TEACHER_PHD:
					row.append(PHD_DEGREE);
					break;
				case CONTRACTUAL_RESEARCHER_TEACHER:
				case ENGINEER:
				case PHD_STUDENT:
				case RESEARCHER:
				case RESEARCH_ENGINEER:
					row.append(MASTER_DEGREE);
					break;
				case ADMIN:
				case ASSOCIATED_MEMBER:
				case ASSOCIATED_MEMBER_PHD:
				case MASTER_STUDENT:
				case OTHER_STUDENT:
				case TEACHER:
				default:
					row.append((String) null);
					break;
				}

				switch (it.getMemberStatus()) {
				case ASSOCIATE_PROFESSOR_HDR:
				case FULL_PROFESSOR:
				case RESEARCH_DIRECTOR:
				case ASSOCIATE_PROFESSOR:
					row.append(PERMANENT_POSITION);
					break;
				case EMERITUS_ASSOCIATE_PROFESSOR_HDR:
				case EMERITUS_FULL_PROFESSOR:
				case EMERITUS_ASSOCIATE_PROFESSOR:
					row.append(OTHER_POSITION);
					break;
				case POSTDOC:
				case PHD_STUDENT:
					row.append(CDD_POSITION);
					break;
				case CONTRACTUAL_RESEARCHER_TEACHER_PHD:
				case CONTRACTUAL_RESEARCHER_TEACHER:
				case ENGINEER_PHD:
				case ENGINEER:
				case RESEARCHER_PHD:
				case RESEARCH_ENGINEER_PHD:
				case TEACHER_PHD:
				case MASTER_STUDENT:
				case OTHER_STUDENT:
				case TEACHER:
				case RESEARCHER:
				case RESEARCH_ENGINEER:
				case ADMIN:
					if (it.isPermanentPosition()) {
						row.append(PERMANENT_POSITION);
					} else if (it.getMemberToWhen() != null) {
						row.append(CDD_POSITION);
					} else {
						row.append(CDI_POSITION);
					}
					break;
				case ASSOCIATED_MEMBER:
				case ASSOCIATED_MEMBER_PHD:
				default:
					row.append((String) null);
					break;
				}

				if (it.getMemberStatus().isResearcher()) {
					row.append(EC_STATUS);
				} else if (it.getMemberStatus().isTechnicalStaff() || it.getMemberStatus().isAdministrativeStaff()) {
					row.append(ITRF_STATUS);
				} else {
					row.append(OTHER_STATUS);
				}

				row.append((String) null);
				row.append((String) null);
				
				final ResearchOrganization employer = EntityUtils.getUniversityOrSchoolOrCompany(
						it.getPerson(),
						it0 -> it.getId() != it0.getId() && it0.isActiveIn(startDate, endDate));
				final boolean isUb;
				if (employer != null) {
					isUb = UB_NAME.equalsIgnoreCase(employer.getAcronymOrName());
					row.append(employer.getAcronymOrName());
				} else {
					isUb = false;
					row.append((String) null);
				}

				row.append(Double.valueOf(it.getMemberStatus().getUsualResearchFullTimeEquivalent()));
				row.append(it.getMemberSinceWhen());
				row.append(it.getMemberToWhen());
				row.append(isUb ? it.getPerson().getEmail() : null);

				if (isUb && it.getOrganizationAddress() != null) {
					row.append(it.getOrganizationAddress().getName());
				} else {
					row.append(it.getResearchOrganization().getAcronymOrName());
				}
			});
	}

	/** Fill up the scientific production table in a spreadsheet.
	 * 
	 * @param document the output document.
	 * @param organization the organization for which the publications must be extracted.
	 * @param year the reference year that is used for filtering the publications.
	 * @throws Exception if the Excel cannot be generated.
	 */
	protected void exportScientificProduction(OdfSpreadsheetHelper document, ResearchOrganization organization, int year) throws Exception {
		final String now = Integer.toString(year);
		final TableHelper table = document.getTable(
				MessageFormat.format(SCIENTIFIC_PRODUCTION_TABLE_NAME, YEAR_TOKEN),
				MessageFormat.format(SCIENTIFIC_PRODUCTION_TABLE_NAME, now));

		final AtomicInteger aclCount = new AtomicInteger();
		final AtomicInteger aclnCount = new AtomicInteger();
		final AtomicInteger asclCount = new AtomicInteger();
		final AtomicInteger invCount = new AtomicInteger();
		final AtomicInteger cactiCount = new AtomicInteger();
		final AtomicInteger cactnCount = new AtomicInteger();
		final AtomicInteger comCount = new AtomicInteger();
		final AtomicInteger affCount = new AtomicInteger();
		final AtomicInteger osCount = new AtomicInteger();
		final AtomicInteger ovCount = new AtomicInteger();
		final AtomicInteger edCount = new AtomicInteger();
		this.publicationService.getPublicationsByOrganizationId(organization.getId(), true, false).stream()
			.filter(it -> it.getPublicationYear() == year)
			.forEach(it -> {
				switch (it.getCategory()) {
				case ACL:
					aclCount.incrementAndGet();
					break;
				case ACLN:
					aclnCount.incrementAndGet();
					break;
				case ASCL:
					asclCount.incrementAndGet();
					break;
				case C_INV:
					invCount.incrementAndGet();
					break;
				case C_ACTI:
					cactiCount.incrementAndGet();
					break;
				case C_ACTN:
					cactnCount.incrementAndGet();
					break;
				case C_COM:
					comCount.incrementAndGet();
					break;
				case C_AFF:
					affCount.incrementAndGet();
					break;
				case COS:
				case OS:
					osCount.incrementAndGet();
					break;
				case COV:
				case OV:
					ovCount.incrementAndGet();
					break;
				case DO:
					edCount.incrementAndGet();
					break;
				case AP:
				case BRE:
				case OR:
				case PAT:
				case PT:
				case PV:
				case TH:
				default:
					break;
				}
			});

		table.getCell("A1").replace(YEAR_TOKEN, now); //$NON-NLS-1$

		table.getCell("C9").replace(YEAR_TOKEN, now); //$NON-NLS-1$

		table.getCell("C11").set(Long.valueOf(aclCount.longValue())); //$NON-NLS-1$

		table.getCell("C12").set(Long.valueOf(aclnCount.longValue())); //$NON-NLS-1$

		table.getCell("C13").set(Long.valueOf(asclCount.longValue())); //$NON-NLS-1$

		table.getCell("C15").set(Long.valueOf(invCount.longValue())); //$NON-NLS-1$

		table.getCell("C16").set(Long.valueOf(cactiCount.longValue())); //$NON-NLS-1$

		table.getCell("C17").set(Long.valueOf(cactnCount.longValue())); //$NON-NLS-1$

		table.getCell("C18").set(Long.valueOf(comCount.longValue())); //$NON-NLS-1$

		table.getCell("C19").set(Long.valueOf(affCount.longValue())); //$NON-NLS-1$

		table.getCell("C21").set(Long.valueOf(osCount.longValue())); //$NON-NLS-1$

		table.getCell("C22").set(Long.valueOf(ovCount.longValue())); //$NON-NLS-1$

		table.getCell("C23").set(Long.valueOf(edCount.longValue())); //$NON-NLS-1$
	}

	/** Fill up the synthesis table in a spreadsheet.
	 * 
	 * @param document the output document.
	 * @param organization the organization for which the publications must be extracted.
	 * @param year the reference year that is used for filtering the publications.
	 * @throws Exception if the Excel cannot be generated.
	 */
	protected void exportSynthesis(OdfSpreadsheetHelper document, ResearchOrganization organization, int year) throws Exception {
		final String now = Integer.toString(year);
		final LocalDate startDate = LocalDate.of(year, 1, 1);
		final LocalDate endDate = LocalDate.of(year, 12, 31);
		final TableHelper table = document.getTable(
				MessageFormat.format(SYNTHESIS_TABLE_NAME, YEAR_TOKEN),
				MessageFormat.format(SYNTHESIS_TABLE_NAME, now));

		Optional<Membership> director = Optional.empty(); 
		Optional<Membership> deputyDirector = Optional.empty(); 
		Optional<Membership> ubleader = Optional.empty(); 
		int hdrCount = 0;
		int ecCount = 0;
		int defendedPhDCount = 0;
		int defendedJointPhDCount = 0;
		final AtomicInteger outgoingInvitations = new AtomicInteger();
		final AtomicInteger incomingInvitations = new AtomicInteger();
		final Iterator<Membership> iterator = organization.getMemberships().stream()
				.filter(it -> it.isActiveIn(startDate, endDate) && !it.getMemberStatus().isExternalPosition()).iterator();
		final Set<ResearchOrganization> superOrganizations = new TreeSet<>(EntityUtils.getPreferredResearchOrganizationComparator());
		getLegalInstitution(superOrganizations, organization);
		while (iterator.hasNext()) {
			final Membership membership = iterator.next();
			if (membership.getResponsibility() == Responsibility.DIRECTOR || membership.getResponsibility() == Responsibility.EXECUTIVE_DIRECTOR) {
				director = Optional.of(membership);
				for (final Membership other : membership.getPerson().getMemberships()) {
					if (other.getId() != membership.getId()) {
						getLegalInstitution(superOrganizations, other.getResearchOrganization());
					}
					if (UB_NAME.equalsIgnoreCase(other.getResearchOrganization().getAcronym())) {
						ubleader = Optional.of(membership);
					}
				}
			} else if (membership.getResponsibility() == Responsibility.DEPUTY_DIRECTOR) {
				deputyDirector = Optional.of(membership);
				for (final Membership other : membership.getPerson().getMemberships()) {
					if (other.getId() != membership.getId()) {
						getLegalInstitution(superOrganizations, other.getResearchOrganization());
					}
					if (ubleader.isEmpty() && UB_NAME.equalsIgnoreCase(other.getResearchOrganization().getAcronym())) {
						ubleader = Optional.of(membership);
					}
				}
			}
			final MemberStatus status = membership.getMemberStatus();
			if (status.isResearcher() && membership.isPermanentPosition()) {
				++ecCount;
				if (status.isHdrOwner()) {
					++hdrCount;
				}
			} else if (status == MemberStatus.PHD_STUDENT) {
				final Optional<Supervision> supervision = this.supervisionService.getSupervisionsForSupervisedPerson(membership.getPerson().getId()).stream()
					.filter(it -> it.getDefenseDate() != null && it.getDefenseDate().getYear() == year)
					.findAny();
				if (supervision.isPresent()) {
					++defendedPhDCount;
					if (supervision.get().isJointPosition()) {
						++defendedJointPhDCount;
					}
				}
			}
			this.invitationService.getInvitationsForPerson(membership.getPerson().getId()).stream()
				.filter(it -> it.getStartDate().getYear() <= year && year <= it.getEndDate().getYear())
				.forEach(it -> {
					if (!it.getType().isOutgoing() && !CountryCodeUtils.isFrance(it.getCountry())) {
						incomingInvitations.incrementAndGet();
					} else if (it.getType().isOutgoing() && !CountryCodeUtils.isFrance(it.getCountry())) {
						outgoingInvitations.incrementAndGet();
					}
				});
		}

		table.getCell("A1").replace(YEAR_TOKEN, now); //$NON-NLS-1$
		
		table.getCell("B3").set(organization.getName() + " - " + organization.getAcronym()); //$NON-NLS-1$ //$NON-NLS-2$
		
		table.getCell("B4").set(director.isPresent() ? director.get().getPerson().getFullName() : null); //$NON-NLS-1$
		
		table.getCell("B5").set(deputyDirector.isPresent() ? deputyDirector.get().getPerson().getFullName() : null); //$NON-NLS-1$

		table.getCell("B6").set(toOrganizationList(superOrganizations)); //$NON-NLS-1$

		table.getCell("B7").set(organization.getNationalIdentifier()); //$NON-NLS-1$
		
		int n = 1;
		for (final OrganizationAddress address : organization.getAddresses()) {
			table.getCell(n, 7).set(address.getName() + " : " + address.getFullAddress()); //$NON-NLS-1$
			++n;
		}

		table.getCell("B13").set(ubleader.isPresent() ? ubleader.get().getPerson().getFullName() : null); //$NON-NLS-1$

		table.getCell("A15").replace(YEAR_TOKEN, now); //$NON-NLS-1$
		
		table.getCell("B16").set(Long.valueOf(hdrCount)); //$NON-NLS-1$

		table.getCell("B17").set(Double.valueOf((double) hdrCount / (double) ecCount)); //$NON-NLS-1$

		table.getCell("A18").replace(YEAR_TOKEN, now); //$NON-NLS-1$
		table.getCell("B18").set(Long.valueOf(defendedPhDCount)); //$NON-NLS-1$

		table.getCell("A19").replace(YEAR_TOKEN, now); //$NON-NLS-1$

		table.getCell("B20").set(Long.valueOf(outgoingInvitations.longValue())); //$NON-NLS-1$

		table.getCell("B21").set(Long.valueOf(incomingInvitations.longValue())); //$NON-NLS-1$

		table.getCell("B22").set(Long.valueOf(defendedJointPhDCount)); //$NON-NLS-1$
		
		final AtomicInteger industrialChairs = new AtomicInteger();
		final AtomicInteger researchChairs = new AtomicInteger();
		this.associatedStructureService.getAssociatedStructuresByOrganizationId(organization.getId()).stream()
			.filter(it -> (it.getType() == AssociatedStructureType.INDUSTRIAL_CHAIR
					|| it.getType() == AssociatedStructureType.RESEARCH_CHAIR)
					&& it.getCreationDate() != null && it.getCreationDate().getYear() >= 2010)
			.forEach(it -> {
				if (it.getType() == AssociatedStructureType.INDUSTRIAL_CHAIR) {
					industrialChairs.incrementAndGet();
				} else {
					researchChairs.incrementAndGet();
				}
			});
		table.getCell("B30").set(Long.valueOf(researchChairs.longValue())); //$NON-NLS-1$
		table.getCell("B31").set(Long.valueOf(industrialChairs.longValue())); //$NON-NLS-1$		
	}

	private static String notNull(String... values) {
		for (final String txt : values) {
			if (!Strings.isNullOrEmpty(txt)) {
				return txt;
			}
		}
		return null;
	}
	
	private static double computeMonthFactor(int year, Project project) {
		final int duration = project.getDuration();
		//
		final LocalDate projectStart = project.getStartDate();
		final int firstMonth = projectStart.getYear() < year ? 1 : projectStart.getMonthValue();
		//
		final LocalDate projectEnd = project.getEndDate().minus(1, ChronoUnit.DAYS);
		final int lastMonth = projectEnd.getYear() > year ? 12 : projectEnd.getMonthValue();
		//
		final int lduration = lastMonth - firstMonth + 1;
		return (double) lduration / (double) duration;
	}

	private static double annualBudget(double factor, double kbudget) {
		return Math.floor(kbudget * 1000. * factor);
	}

	/** Fill up the academic sproject table in a spreadsheet.
	 * 
	 * @param document the output document.
	 * @param organization the organization for which the publications must be extracted.
	 * @param year the reference year that is used for filtering the publications.
	 * @throws Exception if the Excel cannot be generated.
	 */
	protected void exportAcademicProjects(OdfSpreadsheetHelper document, ResearchOrganization organization, int year) throws Exception {
		final String now = Integer.toString(year);
		final TableHelper table = document.getTable(
				MessageFormat.format(ACADEMIC_PROJECTS_TABLE_NAME, YEAR_TOKEN),
				MessageFormat.format(ACADEMIC_PROJECTS_TABLE_NAME, now));

		final Map<String, Pair<Double, String>> cellContent = new HashMap<>();
		
		this.projectService.getAllProjects().stream()
			.filter(it -> it.isActiveAt(year) && it.getStatus() == ProjectStatus.ACCEPTED
				&& it.getEndDate() != null && it.getStartDate() != null && it.getDuration() > 0)
			.forEach(it -> {
				final ResearchOrganization orga = it.getLocalOrganization();
				final ResearchOrganization superOrga = it.getSuperOrganization();
				final ResearchOrganization learOrga = it.getLearOrganization();
				final ResearchOrganization coordOrga = it.getCoordinator();
				final boolean isUb = UB_NAME.equalsIgnoreCase(orga.getAcronym())
						|| UB_NAME.equalsIgnoreCase(superOrga.getAcronym())
						|| UB_NAME.equalsIgnoreCase(learOrga.getAcronym())
						|| UB_NAME.equalsIgnoreCase(coordOrga.getAcronym());
				final String comment = isUb ? null : EntityUtils.getUniversityOrSchoolOrCompanyName(superOrga, learOrga, orga, coordOrga);
				final double monthFactor = computeMonthFactor(year, it);

				for (final ProjectBudget budget : it.getBudgets()) {
					final String cellName;
					switch (budget.getFundingScheme()) {
					case REGION_BFC:
						// Province
						cellName = isUb ? "15" : "18"; //$NON-NLS-1$ //$NON-NLS-2$
						break;
					case HOSTING_ORGANIZATION:
						// Other local institution
						cellName = isUb ? "16" : "19"; //$NON-NLS-1$ //$NON-NLS-2$
						break;
					case CPER:
						// Other regional funding
						cellName = isUb ? "17" : "20"; //$NON-NLS-1$ //$NON-NLS-2$
						break;
					case ANR:
						// ANR
						cellName = isUb ? "24" : "30"; //$NON-NLS-1$ //$NON-NLS-2$
						break;
					case FUI:
						// FUI
						cellName = isUb ? "25" : "31"; //$NON-NLS-1$ //$NON-NLS-2$
						break;
					case IDEX:
					case ISITE:
					case PIA:
						// PIA
						cellName = isUb ? "27" : "33"; //$NON-NLS-1$ //$NON-NLS-2$
						break;
					case CARNOT:
						// Carnot
						cellName = isUb ? "28" : "34"; //$NON-NLS-1$ //$NON-NLS-2$
						break;
					case ADEME:
					case CAMPUS_FRANCE:
					case FRENCH_OTHER:
						// Other nat. funding
						cellName = isUb ? "29" : "35"; //$NON-NLS-1$ //$NON-NLS-2$
						break;
					case EDIH:
					case H2020:
					case HORIZON_EUROPE:
					case LIFE:
						// EU programme funding
						cellName = isUb ? "39" : "42"; //$NON-NLS-1$ //$NON-NLS-2$
						break;
					case COST_ACTION:
					case EUREKA:
					case EUROSTAR:
					case EU_OTHER:
					case FEDER:
					case INTERREG:
					case JPIEU:
						// Other EU funding
						cellName = isUb ? "40" : "43"; //$NON-NLS-1$ //$NON-NLS-2$
						break;
					case CONACYT:
					case CSC:
					case FITEC:
					case INTERNTATIONAL_OTHER:
					case NICOLAS_BAUDIN:
					case PHC:
						// Other int. funding
						cellName = isUb ? "41" : "44"; //$NON-NLS-1$ //$NON-NLS-2$
						break;
					case CIFRE:
					case FRENCH_COMPANY:
					case FRENCH_UNIVERSITY:
					case EU_COMPANY:
					case EU_UNIVERSITY:
					case INTERNATIONAL_COMPANY:
					case INTERNATIONAL_UNIVERSITY:
					case SELF_FUNDING:
					case NOT_FUNDED:
					case LOCAL_INSTITUTION:
					default:
						cellName = null;
						break;
					}
					if (!Strings.isNullOrEmpty(cellName)) {
						Pair<Double, String> pair = cellContent.get(cellName);
						if (pair == null) {
							pair = Pair.of(Double.valueOf(annualBudget(monthFactor, budget.getBudget())), comment);
						} else {
							final String cmt = notNull(pair.getRight(), comment);
							pair = Pair.of(Double.valueOf(annualBudget(monthFactor, budget.getBudget()) + pair.getKey().doubleValue()), cmt);
						}
						cellContent.put(cellName, pair);
					}
				}
			});

		for (final Entry<String, Pair<Double, String>> entry : cellContent.entrySet()) {
			table.getCell("C" + entry.getKey()).setCurrency(entry.getValue().getLeft()); //$NON-NLS-1$
			table.getCell("E" + entry.getKey()).set(entry.getValue().getRight()); //$NON-NLS-1$
		}

		table.getCell("A1").replace(YEAR_TOKEN, now); //$NON-NLS-1$
		table.getCell("A47").replace(YEAR_TOKEN, now); //$NON-NLS-1$
		table.getCell("A48").replace(YEAR_TOKEN, now); //$NON-NLS-1$
		table.getCell("A49").replace(YEAR_TOKEN, now); //$NON-NLS-1$

	}

	/** Fill up the investment table in a spreadsheet.
	 * 
	 * @param document the output document.
	 * @param organization the organization for which the publications must be extracted.
	 * @param year the reference year that is used for filtering the publications.
	 * @throws Exception if the Excel cannot be generated.
	 */
	@SuppressWarnings("static-method")
	protected void exportInvestments(OdfSpreadsheetHelper document, ResearchOrganization organization, int year) throws Exception {
		final String now = Integer.toString(year);
		final String nextYear = Integer.toString(year + 1);
		final String academicYear = now + "-" + nextYear; //$NON-NLS-1$
		final TableHelper table = document.getTable(
				MessageFormat.format(INVESTMENT_TABLE_NAME, ACADEMIC_YEAR_TOKEN),
				MessageFormat.format(INVESTMENT_TABLE_NAME, now));
		table.getCell("A1").replace(ACADEMIC_YEAR_TOKEN, academicYear); //$NON-NLS-1$
		table.getCell("A2").replace(YEAR_TOKEN, now); //$NON-NLS-1$
		table.getCell("A7").replace(NEXT_YEAR_TOKEN, now); //$NON-NLS-1$
	}

	/** Fill up the industrial project table in a spreadsheet.
	 * 
	 * @param document the output document.
	 * @param organization the organization for which the publications must be extracted.
	 * @param year the reference year that is used for filtering the publications.
	 * @throws Exception if the Excel cannot be generated.
	 */
	protected void exportIndustrialProjects(OdfSpreadsheetHelper document, ResearchOrganization organization, int year) throws Exception {
		final String now = Integer.toString(year);
		final LocalDate startDate = LocalDate.of(year, 1, 1);
		final LocalDate endDate = LocalDate.of(year, 12, 31);
		final TableHelper table = document.getTable(
				MessageFormat.format(INDUSTRIAL_PROJECTS_TABLE_NAME, YEAR_TOKEN),
				MessageFormat.format(INDUSTRIAL_PROJECTS_TABLE_NAME, now));

		final Map<Integer, Triple<Integer, Double, String>> projectContent = new HashMap<>();
		final Map<Integer, Triple<Integer, Double, String>> piContent = new HashMap<>();
		
		this.projectService.getAllProjects().stream()
			.filter(it -> it.isActiveAt(year) && it.getStatus() == ProjectStatus.ACCEPTED
				&& it.getEndDate() != null && it.getStartDate() != null && it.getDuration() > 0)
			.forEach(it -> {
				final ResearchOrganization orga = it.getLocalOrganization();
				final ResearchOrganization superOrga = it.getSuperOrganization();
				final ResearchOrganization learOrga = it.getLearOrganization();
				final ResearchOrganization coordOrga = it.getCoordinator();
				final boolean isUb = UB_NAME.equalsIgnoreCase(orga.getAcronym())
						|| UB_NAME.equalsIgnoreCase(superOrga.getAcronym())
						|| UB_NAME.equalsIgnoreCase(learOrga.getAcronym())
						|| UB_NAME.equalsIgnoreCase(coordOrga.getAcronym());
				final boolean isSayens = SAYENS_NAME.equalsIgnoreCase(orga.getAcronym())
						|| SAYENS_NAME.equalsIgnoreCase(superOrga.getAcronym())
						|| SAYENS_NAME.equalsIgnoreCase(learOrga.getAcronym())
						|| SAYENS_NAME.equalsIgnoreCase(coordOrga.getAcronym());
				final String comment = isUb ? null : EntityUtils.getUniversityOrSchoolOrCompanyName(superOrga, learOrga, orga, coordOrga);
				final double monthFactor = computeMonthFactor(year, it);

				if (it.getContractType() == ProjectContractType.PI) {
					final int cellName = isSayens ? 3 : (isUb ? 1 : 5);
					Triple<Integer, Double, String> triple = piContent.get(Integer.valueOf(cellName));
					if (triple == null) {
						triple = Triple.of(Integer.valueOf(1), Double.valueOf(annualBudget(monthFactor, it.getGlobalBudget())), comment);
					} else {
						final String cmt = notNull(triple.getRight(), comment);
						triple = Triple.of(
								Integer.valueOf(triple.getLeft().intValue() + 1),
								Double.valueOf(annualBudget(monthFactor, it.getGlobalBudget()) + triple.getMiddle().doubleValue()),
								cmt);
					}
					piContent.put(Integer.valueOf(cellName), triple);
				} else {
					for (final ProjectBudget budget : it.getBudgets()) {
						final int cellName;
						switch (budget.getFundingScheme()) {
						case CIFRE:
						case FRENCH_COMPANY:
						case FRENCH_UNIVERSITY:
						case EU_COMPANY:
						case EU_UNIVERSITY:
						case INTERNATIONAL_COMPANY:
						case INTERNATIONAL_UNIVERSITY:
						case SELF_FUNDING:
						case NOT_FUNDED:
						case LOCAL_INSTITUTION:
							cellName = isSayens ? 3 : (isUb ? 1 : 5);
							break;
						case REGION_BFC:
						case HOSTING_ORGANIZATION:
						case CPER:
						case ANR:
						case FUI:
						case IDEX:
						case ISITE:
						case PIA:
						case CARNOT:
						case ADEME:
						case CAMPUS_FRANCE:
						case FRENCH_OTHER:
						case EDIH:
						case H2020:
						case HORIZON_EUROPE:
						case LIFE:
						case COST_ACTION:
						case EUREKA:
						case EUROSTAR:
						case EU_OTHER:
						case FEDER:
						case INTERREG:
						case JPIEU:
						case CONACYT:
						case CSC:
						case FITEC:
						case INTERNTATIONAL_OTHER:
						case NICOLAS_BAUDIN:
						case PHC:
						default:
							cellName = -1;
							break;
						}
						if (cellName >= 0) {
							Triple<Integer, Double, String> triple = projectContent.get(Integer.valueOf(cellName));
							if (triple == null) {
								triple = Triple.of(Integer.valueOf(1), Double.valueOf(annualBudget(monthFactor, budget.getBudget())), comment);
							} else {
								final String cmt = notNull(triple.getRight(), comment);
								triple = Triple.of(
										Integer.valueOf(triple.getLeft().intValue() + 1),
										Double.valueOf(annualBudget(monthFactor, budget.getBudget()) + triple.getMiddle().doubleValue()),
										cmt);
							}
							projectContent.put(Integer.valueOf(cellName), triple);
						}
					}
				}
			});

		for (final Entry<Integer, Triple<Integer, Double, String>> entry : projectContent.entrySet()) {
			table.getCell(entry.getKey().intValue(), 4).set(Long.valueOf(entry.getValue().getLeft().longValue()));
			table.getCell(entry.getKey().intValue() + 1, 4).setCurrency(entry.getValue().getMiddle());
			if (!Strings.isNullOrEmpty(entry.getValue().getRight())) {
				table.getCell("H5").set(entry.getValue().getRight()); //$NON-NLS-1$
			}
		}

		for (final Entry<Integer, Triple<Integer, Double, String>> entry : projectContent.entrySet()) {
			table.getCell(entry.getKey().intValue(), 5).set(Long.valueOf(entry.getValue().getLeft().longValue()));
			table.getCell(entry.getKey().intValue() + 1, 5).setCurrency(entry.getValue().getMiddle());
			if (!Strings.isNullOrEmpty(entry.getValue().getRight())) {
				table.getCell("H6").set(entry.getValue().getRight()); //$NON-NLS-1$
			}
		}

		final AtomicInteger companyCreation = new AtomicInteger();
		final AtomicInteger activeCompany = new AtomicInteger();
		final AtomicInteger hostedCompanies = new AtomicInteger();
		this.associatedStructureService.getAssociatedStructuresByOrganizationId(organization.getId()).stream()
			.filter(it -> it.getCreationDate().getYear() <= year)
			.forEach(it -> {
				switch (it.getType()) {
				case PRIVATE_COMPANY:
					if (it.getCreationDate().getYear() == year) {
						companyCreation.incrementAndGet();
					} else if (it.getCreationDate().getYear() >= (year -10)) {
						activeCompany.incrementAndGet();
					}
					break;
				case HOSTED_EUROPEAN_COMPANY:
				case HOSTED_INTERNATIONAL_COMPANY:
				case HOSTED_NATIONAL_COMPANY:
					hostedCompanies.incrementAndGet();
					break;
				case INTERNATIONAL_RESEARCH_LAB:
				case INTERNATIONAL_SCIENTIFIC_INTEREST_GROUP:
				case INDUSTRIAL_CHAIR:
				case RESEARCH_CHAIR:
				case NATIONAL_SCIENTIFIC_INTEREST_GROUP:
				case NATIONAL_RESEARCH_LAB:
				case INTERNATIONAL_RESEARCH_GROUP:
				case NATIONAL_RESEARCH_GROUP:
				case EUROPEAN_RESEARCH_GROUP:
				case EUROPEAN_RESEARCH_LAB:
				case EUROPEAN_SCIENTIFIC_INTEREST_GROUP:
				default:
					break;
				}
			});
		table.getCell("B22").set(Long.valueOf(companyCreation.longValue())); //$NON-NLS-1$
		table.getCell("B23").set(Long.valueOf(activeCompany.longValue())); //$NON-NLS-1$
		table.getCell("B24").set(Long.valueOf(hostedCompanies.longValue())); //$NON-NLS-1$

		final AtomicInteger sayensPatents = new AtomicInteger();
		final AtomicInteger otherPatents = new AtomicInteger();
		final AtomicReference<String> patentComment = new AtomicReference<>();
		final StringBuilder patentNames = new StringBuilder();
		this.publicationService.getPublicationsByYear(year).stream()
			.filter(it -> it.getCategory() == PublicationCategory.BRE)
			.forEach(it -> {
				boolean foundSayens = false;
				boolean foundOther = false;
				for (final Person person : it.getAuthors()) {
					final ResearchOrganization authorOrganization = EntityUtils.getUniversityOrSchoolOrCompany(person,
							it0 -> it.getId() != it0.getId() && it0.isActiveIn(startDate, endDate));
					if (authorOrganization != null) {
						if (!foundSayens && (SAYENS_NAME.equalsIgnoreCase(authorOrganization.getAcronym())
								|| UB_NAME.equalsIgnoreCase(authorOrganization.getAcronym()))) {
							foundSayens = true;
							sayensPatents.incrementAndGet();
						} else if (!foundOther) {
							foundOther = true;
							otherPatents.incrementAndGet();
							if (Strings.isNullOrEmpty(patentComment.get())) {
								patentComment.set(authorOrganization.getAcronymOrName());
							}
						}
					}
					if (foundSayens && foundOther) {
						break;
					}
				}
				if (patentNames.length() > 0) {
					patentNames.append("; "); //$NON-NLS-1$
				}
				patentNames.append(it.getTitle());
			});
		table.getCell("B15").set(Long.valueOf(sayensPatents.longValue())); //$NON-NLS-1$
		table.getCell("C15").set(Long.valueOf(otherPatents.longValue())); //$NON-NLS-1$
		table.getCell("B21").set(Long.valueOf(sayensPatents.longValue() + otherPatents.longValue())); //$NON-NLS-1$
		table.getCell("C21").set(patentNames.toString()); //$NON-NLS-1$

		table.getCell("A1").replace(YEAR_TOKEN, now); //$NON-NLS-1$
		table.getCell("A4").replace(YEAR_TOKEN, now); //$NON-NLS-1$
		table.getCell("A14").replace(YEAR_TOKEN, now); //$NON-NLS-1$
		table.getCell("A20").replace(YEAR_TOKEN, now); //$NON-NLS-1$
	}

	/** Fill up the conference table in a spreadsheet.
	 * 
	 * @param document the output document.
	 * @param organization the organization for which the publications must be extracted.
	 * @param year the reference year that is used for filtering the conferences.
	 * @throws Exception if the Excel cannot be generated.
	 */
	@SuppressWarnings("static-method")
	protected void exportConferences(OdfSpreadsheetHelper document, ResearchOrganization organization, int year) throws Exception {
		final String now = Integer.toString(year);
		final String nextYear = Integer.toString(year + 1);
		final String academicYear = now + "-" + nextYear; //$NON-NLS-1$
		final TableHelper table = document.getTable(
				MessageFormat.format(CONFERENCES_TABLE_NAME, YEAR_TOKEN),
				MessageFormat.format(CONFERENCES_TABLE_NAME, now));

		table.getCell("A1").replace(ACADEMIC_YEAR_TOKEN, academicYear); //$NON-NLS-1$
		table.getCell("A2").replace(YEAR_TOKEN, now); //$NON-NLS-1$
		table.getCell("A5").replace(NEXT_YEAR_TOKEN, nextYear); //$NON-NLS-1$
	}

	/** Fill up the project-list table in a spreadsheet.
	 * 
	 * @param document the output document.
	 * @param organization the organization for which the publications must be extracted.
	 * @param year the reference year that is used for filtering the conferences.
	 * @throws Exception if the Excel cannot be generated.
	 */
	protected void exportProjectList(OdfSpreadsheetHelper document, ResearchOrganization organization, int year) throws Exception {
		final String now = Integer.toString(year);
		final TableHelper table = document.getTable(
				MessageFormat.format(PROJECT_LIST_TABLE_NAME, YEAR_TOKEN),
				MessageFormat.format(PROJECT_LIST_TABLE_NAME, now));

		table.getCell("A1").replace(YEAR_TOKEN, now); //$NON-NLS-1$
		table.getCell("A2").replace(YEAR_TOKEN, now); //$NON-NLS-1$

		final List<String[]> natProjects = new ArrayList<>();
		final List<String[]> intProjects = new ArrayList<>();
		final List<String[]> piaProjects = new ArrayList<>();
		
		this.projectService.getProjectsByOrganizationId(organization.getId()).stream()
			.filter(it -> it.isActiveAt(year) && it.getStatus() == ProjectStatus.ACCEPTED
				&& it.getEndDate() != null && it.getStartDate() != null && it.getDuration() > 0
				&& it.getCategory() == ProjectCategory.COMPETITIVE_CALL_PROJECT)
			.forEach(it -> {
				final String[] projectDetails = new String[] {
						it.getAcronym() + " - " + it.getScientificTitle(), //$NON-NLS-1$
						toPersonList(EntityUtils.getProjectLocalHeads(it)),
						toOrganizationList(it.getOtherPartners()),
						format(it.getStartDate()),
						format(it.getEndDate()),
						it.getLearOrganization() != null ? it.getLearOrganization().getAcronymOrName() : null,
					};
				switch (projectFundingScheme(it)) {
				case 1:
					natProjects.add(projectDetails);
					break;
				case 2:
					intProjects.add(projectDetails);
					break;
				case 3:
					piaProjects.add(projectDetails);
					break;
				default:
					break;
				}
			});

		final List<String[]> natLabcom = new ArrayList<>();
		final List<String[]> euLabcom = new ArrayList<>();
		final List<String[]> intLabcom = new ArrayList<>();
		final List<String[]> gdr = new ArrayList<>();
		final List<String[]> gis = new ArrayList<>();
		final List<String[]> gdri = new ArrayList<>();

		this.associatedStructureService.getAssociatedStructuresByOrganizationId(organization.getId()).stream()
		.filter(it -> it.getCreationDate().getYear() <= year)
		.forEach(it -> {
			final Set<ResearchOrganization> partners = EntityUtils.getPartnersForProjects(it.getProjects());
			final String[] details = new String[] {
					it.getAcronym() + " - " + it.getName(), //$NON-NLS-1$
					toPersonList(EntityUtils.getAssociatedStructureLocalHeads(it)),
					toOrganizationList(partners),
					format(it.getCreationDate()),
					null,
					it.getFundingOrganization() != null ? it.getFundingOrganization().getAcronymOrName() : null,
				};
			switch (it.getType()) {
			case INTERNATIONAL_SCIENTIFIC_INTEREST_GROUP:
			case EUROPEAN_SCIENTIFIC_INTEREST_GROUP:
			case NATIONAL_SCIENTIFIC_INTEREST_GROUP:
				gis.add(details);
				break;
			case INTERNATIONAL_RESEARCH_GROUP:
				gdri.add(details);
				break;
			case EUROPEAN_RESEARCH_GROUP:
				gdri.add(details);
				break;
			case NATIONAL_RESEARCH_GROUP:
				gdr.add(details);
				break;
			case INTERNATIONAL_RESEARCH_LAB:
				intLabcom.add(details);
				break;
			case EUROPEAN_RESEARCH_LAB:
				euLabcom.add(details);
				break;
			case NATIONAL_RESEARCH_LAB:
				natLabcom.add(details);
				break;
			case INDUSTRIAL_CHAIR:
			case RESEARCH_CHAIR:
			case PRIVATE_COMPANY:
			case HOSTED_EUROPEAN_COMPANY:
			case HOSTED_INTERNATIONAL_COMPANY:
			case HOSTED_NATIONAL_COMPANY:
			default:
				break;
			}
		});

		appendProjectLines(table, 19, gdri);
		appendProjectLines(table, 17, gis);
		appendProjectLines(table, 15, gdr);
		appendProjectLines(table, 13, intLabcom);
		appendProjectLines(table, 11, euLabcom);
		appendProjectLines(table, 9, natLabcom);
		appendProjectLines(table, 7, piaProjects);
		appendProjectLines(table, 5, intProjects);
		appendProjectLines(table, 3, natProjects);
	}

	private static int projectFundingScheme(Project project) {
		int max = -1;
		for (final ProjectBudget budget : project.getBudgets()) {
			int idx = -1;
			if (budget.getFundingScheme() == FundingScheme.PIA || budget.getFundingScheme() == FundingScheme.ISITE
					 || budget.getFundingScheme() == FundingScheme.IDEX) {
				idx = 3;
			} else if (budget.getFundingScheme().isRegional()) {
				idx = 0;
			} else if (budget.getFundingScheme().isNational()) {
				idx = 1;
			} else if (budget.getFundingScheme().isInternational()) {
				idx = 2;
			}
			if (idx > max) {
				max = idx;
			}
		}
		return max;
	}
	
	private static void appendProjectLines(TableHelper table, int rowIndex, List<String[]> lines) {
		int endIndex = rowIndex;
		for (final String[] cells : lines) {
			TableContentRowHelper row = table.insertRowAfter(endIndex);
			row.append((String) null);
			for (final String cell : cells) {
				row.append(cell);
			}
			++endIndex;
		}
		//table.mergeVerticalCells(0, rowIndex, endIndex + 1);
	}

	private static String format(LocalDate date) {
		if (date != null) {
			return date.format(DateTimeFormatter.ofPattern("dd/MM/uuuu")); //$NON-NLS-1$
		}
		return null;
	}
	private static String toPersonList(Iterable<Person> persons) {
		final StringBuilder buffer = new StringBuilder();
		for (final Person person : persons) {
			if (buffer.length() > 0) {
				buffer.append(", "); //$NON-NLS-1$
			}
			buffer.append(person.getFullName());
		}
		return buffer.toString();
	}


}

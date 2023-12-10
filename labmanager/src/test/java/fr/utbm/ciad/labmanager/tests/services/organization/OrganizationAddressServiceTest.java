/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
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

package fr.utbm.ciad.labmanager.tests.services.organization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.organization.OrganizationAddress;
import fr.utbm.ciad.labmanager.data.organization.OrganizationAddressRepository;
import fr.utbm.ciad.labmanager.services.organization.OrganizationAddressService;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.multipart.MultipartFile;

/** Tests for {@link OrganizationAddressService}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class OrganizationAddressServiceTest {

	private OrganizationAddress adr0;

	private OrganizationAddress adr1;

	private MessageSourceAccessor messages;

	private OrganizationAddressRepository addressRepository;

	private DownloadableFileManager fileManager;

	private OrganizationAddressService test;

	@BeforeEach
	public void setUp() {
		this.messages = mock(MessageSourceAccessor.class);
		this.addressRepository = mock(OrganizationAddressRepository.class);
		this.fileManager = mock(DownloadableFileManager.class);
		
		this.test = new OrganizationAddressService(this.messages, new Constants(), this.fileManager, this.addressRepository);

		// Prepare some organizations to be inside the repository
		// The lenient configuration is used to configure the mocks for all the tests
		// at the same code location for configuration simplicity
		this.adr0 = mock(OrganizationAddress.class);
		lenient().when(this.adr0.getId()).thenReturn(123);
		lenient().when(this.adr0.getName()).thenReturn("N1");
		lenient().when(this.adr0.getComplement()).thenReturn("C1");
		lenient().when(this.adr0.getStreet()).thenReturn("S1");
		lenient().when(this.adr0.getZipCode()).thenReturn("Z1");
		lenient().when(this.adr0.getCity()).thenReturn("C1");
		this.adr1 = mock(OrganizationAddress.class);
		lenient().when(this.adr1.getId()).thenReturn(234);
		lenient().when(this.adr1.getName()).thenReturn("N2");
		lenient().when(this.adr1.getComplement()).thenReturn("C2");
		lenient().when(this.adr1.getStreet()).thenReturn("S2");
		lenient().when(this.adr1.getZipCode()).thenReturn("Z2");
		lenient().when(this.adr1.getCity()).thenReturn("C2");

		lenient().when(this.addressRepository.findAll()).thenReturn(
				Arrays.asList(this.adr0, this.adr1));
		lenient().when(this.addressRepository.findById(anyInt())).then(it -> {
			switch (((Integer) it.getArgument(0)).intValue()) {
			case 123:
				return Optional.of(this.adr0);
			case 234:
				return Optional.of(this.adr1);
			}
			return Optional.empty();
		});
	}

	@Test
	public void getAllAddresses() {
		List<OrganizationAddress> list = this.addressRepository.findAll();
		assertNotNull(list);
		assertEquals(2, list.size());
		assertSame(this.adr0, list.get(0));
		assertSame(this.adr1, list.get(1));
	}

	@Test
	public void getAddressById() {
		assertNull(this.test.getAddressById(-4756));
		assertNull(this.test.getAddressById(0));
		assertSame(this.adr0, this.test.getAddressById(123));
		assertSame(this.adr1, this.test.getAddressById(234));
		assertNull(this.test.getAddressById(7896));
	}

	@Test
	public void createAddress() throws IOException {
		MultipartFile file = mock(MultipartFile.class);
		when(file.isEmpty()).thenReturn(true);
		final Optional<OrganizationAddress> res = this.test.createAddress(true, "NN", "NC", "NS", "NZC", "NC", "NMC", "NGL", file);
		assertNotNull(res);
		assertNotNull(res.get());

		final ArgumentCaptor<OrganizationAddress> arg = ArgumentCaptor.forClass(OrganizationAddress.class);
		verify(this.addressRepository, only()).save(arg.capture());
		final OrganizationAddress actual = arg.getValue();
		assertNotNull(actual);
		assertTrue(actual.isValidated());
		assertEquals("NN", actual.getName());
		assertEquals("NC", actual.getComplement());
		assertEquals("NS", actual.getStreet());
		assertEquals("NZC", actual.getZipCode());
		assertEquals("NC", actual.getCity());
		assertEquals("NMC", actual.getMapCoordinates());
		assertEquals("NGL", actual.getGoogleMapLink());
	}

	@Test
	public void updateAddress() throws IOException {
		MultipartFile file = mock(MultipartFile.class);
		when(file.isEmpty()).thenReturn(true);
		Optional<OrganizationAddress> res = this.test.updateAddress(234, true, "NN", "NC", "NS", "NZC", "NC", "NMC", "NGL", file, false);
		assertNotNull(res);
		assertNotNull(res.get());

		verify(this.addressRepository, atLeastOnce()).findById(eq(234));
		verify(this.addressRepository, atLeastOnce()).save(same(this.adr1));

		verify(this.adr1, atLeastOnce()).setValidated(eq(true));
		verify(this.adr1, atLeastOnce()).setName(eq("NN"));
		verify(this.adr1, atLeastOnce()).setComplement(eq("NC"));
		verify(this.adr1, atLeastOnce()).setStreet(eq("NS"));
		verify(this.adr1, atLeastOnce()).setZipCode(eq("NZC"));
		verify(this.adr1, atLeastOnce()).setCity(eq("NC"));
		verify(this.adr1, atLeastOnce()).setMapCoordinates(eq("NMC"));
		verify(this.adr1, atLeastOnce()).setGoogleMapLink(eq("NGL"));
	}

	@Test
	public void removeAddress() throws Exception {
		this.test.removeAddress(234);
		verify(this.addressRepository, atLeastOnce()).deleteById(eq(234));
	}

}

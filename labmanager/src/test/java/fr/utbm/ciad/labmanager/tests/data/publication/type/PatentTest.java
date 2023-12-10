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

package fr.utbm.ciad.labmanager.tests.data.publication.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.data.publication.type.Patent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link Patent}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class PatentTest extends AbstractTypedPublicationTest<Patent> {

	@Override
	protected Patent createTest() {
		return new Patent();
	}

	@Override
	protected Patent createTest(Publication prePublication) {
		return new Patent(prePublication, null, null, null, null);
	}

	@Test
	public void isRanked() {
		assertFalse(this.test.isRanked());
	}

	@Test
	public void getAddress() {
		assertNull(this.test.getAddress());
	}

	@Test
	public void setAddress() {
		this.test.setAddress("xyz");
		assertEquals("xyz", this.test.getAddress());

		this.test.setAddress("");
		assertNull(this.test.getAddress());

		this.test.setAddress(null);
		assertNull(this.test.getAddress());
	}

	@Test
	public void getPatentType() {
		assertNull(this.test.getPatentType());
	}

	@Test
	public void setPatentType() {
		this.test.setPatentType("xyz");
		assertEquals("xyz", this.test.getPatentType());

		this.test.setPatentType("");
		assertNull(this.test.getPatentType());

		this.test.setPatentType(null);
		assertNull(this.test.getPatentType());
	}

	@Test
	public void getPatentNumber() {
		assertNull(this.test.getPatentNumber());
	}

	@Test
	public void setPatentNumber() {
		this.test.setPatentNumber("xyz");
		assertEquals("xyz", this.test.getPatentNumber());

		this.test.setPatentNumber("");
		assertNull(this.test.getPatentNumber());

		this.test.setPatentNumber(null);
		assertNull(this.test.getPatentNumber());
	}

}

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

package fr.utbm.ciad.labmanager.tests.utils.phone;

import static org.junit.jupiter.api.Assertions.*;

import fr.utbm.ciad.labmanager.utils.HashCodeUtils;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import fr.utbm.ciad.labmanager.utils.phone.PhoneNumber;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for {@link PhoneNumber}.
 * 
 * @author <a target="_blank" href="http://www.arakhne.org/homes/galland.html">St&eacute;phane GALLAND</a>
 * @version 18.0
 * @mavengroupid org.arakhne.afc.core
 * @mavenartifactid util
 * @since 18.0
 */
@SuppressWarnings("all")
public class PhoneNumberTest {

	private PhoneNumber number;

	@BeforeEach
	public void setUp() {
		this.number = new PhoneNumber(CountryCode.FRANCE, "123456789");
	}

	@Test
	@DisplayName("+33123456789 != null")
	public void equals_int_null() {
		assertFalse(this.number.equals(null));
	}

	@Test
	@DisplayName("+33123456789 == +33123456789 (equals)")
	public void equals_int_int() {
		final PhoneNumber o = new PhoneNumber(CountryCode.FRANCE, "123456789");
		assertTrue(this.number.equals(o));
		assertTrue(o.equals(this.number));
	}

	@Test
	@DisplayName("+33123456789 != +93123456789")
	public void equals_int_int2() {
		final PhoneNumber o = new PhoneNumber(CountryCode.AFGHANISTAN, "123456789");
		assertFalse(this.number.equals(o));
		assertFalse(o.equals(this.number));
	}

	@Test
	@DisplayName("+33123456789 != +23987654321")
	public void equals_int_int3() {
		final PhoneNumber o = new PhoneNumber(CountryCode.FRANCE, "987654321");
		assertFalse(this.number.equals(o));
		assertFalse(o.equals(this.number));
	}

	@Test
	@DisplayName("+33123456789 > null")
	public void compareTo_int_null() {
		assertTrue(this.number.compareTo(null) > 0);
	}

	@Test
	@DisplayName("+33123456789 == +33123456789 (compare)")
	public void compareTo_int_int() {
		final PhoneNumber o = new PhoneNumber(CountryCode.FRANCE, "123456789");
		assertEquals(0, this.number.compareTo(o));
		assertEquals(0, o.compareTo(this.number));
	}

	@Test
	@DisplayName("+33123456789 > +93123456789")
	public void compareTo_int_int2() {
		final PhoneNumber o = new PhoneNumber(CountryCode.AFGHANISTAN, "123456789");
		assertTrue(this.number.compareTo(o) > 0);
		assertTrue(o.compareTo(this.number) < 0);
	}

	@Test
	@DisplayName("+33123456789 < +33987654321")
	public void compareTo_int_int3() {
		final PhoneNumber o = new PhoneNumber(CountryCode.FRANCE, "987654321");
		assertTrue(this.number.compareTo(o) < 0);
		assertTrue(o.compareTo(this.number) > 0);
	}

	@Test
	@DisplayName("getCountry")
	public void getCountry() {
		assertSame(CountryCode.FRANCE, this.number.getCountry());
	}

	@Test
	@DisplayName("setCountry(null)")
	public void setCountry_null() {
		assertThrows(AssertionError.class, () -> {
			this.number.setCountry(null);
		});
	}

	@Test
	@DisplayName("setCountry(c)")
	public void setCountry_c() {
		this.number.setCountry(CountryCode.ALBANIA);
		assertSame(CountryCode.ALBANIA, this.number.getCountry());
	}

	@Test
	@DisplayName("getLocalNumber")
	public void getLocalNUmber() {
		assertEquals("123456789", this.number.getLocalNumber());
	}

	@Test
	@DisplayName("setLocalNumber(null)")
	public void setLocalNumber_null() {
		assertThrows(AssertionError.class, () -> {
			this.number.setLocalNumber(null);
		});
	}

	@Test
	@DisplayName("setLocalNumber(\"\")")
	public void setLocalNumber_empty() {
		assertThrows(AssertionError.class, () -> {
			this.number.setLocalNumber("");
		});
	}

	@Test
	@DisplayName("setLocalNumber(\"ab0def\")")
	public void setLocalNumber_n() {
		this.number.setLocalNumber("ab0def");
		assertEquals("AB0DEF", this.number.getLocalNumber());
	}

	@Test
	@DisplayName("setLocalNumber(\"01 23 45 67 89\")")
	public void setLocalNumber_npn() {
		this.number.setLocalNumber("01 23 45 67 89");
		assertEquals("123456789", this.number.getLocalNumber());
	}

	@Test
	@DisplayName("setLocalNumber(\"001 23 45 67 89\")")
	public void setLocalNumber_nnpn() {
		this.number.setLocalNumber("001 23 45 67 89");
		assertEquals("0123456789", this.number.getLocalNumber());
	}
	
	@Test
	@DisplayName("toNationalForm with pair size")
	public void toNationalForm_0() {
		this.number = new PhoneNumber(CountryCode.FRANCE, "123");
		assertEquals("01 23", this.number.toNationalForm());
	}	

	@Test
	@DisplayName("toNationalForm with 3-multiple size")
	public void toNationalForm_1() {
		assertEquals("01 23 45 67 89", this.number.toNationalForm());
		//
		this.number = new PhoneNumber(CountryCode.FRANCE, "12345");
		assertEquals("012 345", this.number.toNationalForm());
	}	

	@Test
	@DisplayName("toNationalForm with other size")
	public void toNationalForm_3() {
		this.number = new PhoneNumber(CountryCode.FRANCE, "1234");
		assertEquals("01 23 4", this.number.toNationalForm());
	}

	@Test
	@DisplayName("toInternationalForm with pair size")
	public void toInternationalForm_0() {
		this.number = new PhoneNumber(CountryCode.FRANCE, "1234");
		assertEquals("+33 12 34", this.number.toInternationalForm());
	}	

	@Test
	@DisplayName("toInternationalForm with 3-multiple size")
	public void toInternationalForm_1() {
		assertEquals("+33 123 456 789", this.number.toInternationalForm());
		//
		this.number = new PhoneNumber(CountryCode.FRANCE, "123456");
		assertEquals("+33 123 456", this.number.toInternationalForm());
	}	

	@Test
	@DisplayName("toInternationalForm with other size")
	public void toInternationalForm_3() {
		this.number = new PhoneNumber(CountryCode.FRANCE, "12345");
		assertEquals("+33 12 34 5", this.number.toInternationalForm());
	}	

	@Test
	@DisplayName("toInternationalFormWithNationalExitPrefix with pair size")
	public void toInternationalFormWithNationalExitPrefix_0() {
		this.number = new PhoneNumber(CountryCode.FRANCE, "1234");
		assertEquals("0033 12 34", this.number.toInternationalFormWithNationalExitPrefix());
	}	

	@Test
	@DisplayName("toInternationalFormWithNationalExitPrefix with 3-multiple size")
	public void toInternationalFormWithNationalExitPrefix_1() {
		assertEquals("0033 123 456 789", this.number.toInternationalFormWithNationalExitPrefix());
		//
		this.number = new PhoneNumber(CountryCode.FRANCE, "123456");
		assertEquals("0033 123 456", this.number.toInternationalFormWithNationalExitPrefix());
	}	

	@Test
	@DisplayName("toInternationalFormWithNationalExitPrefix with other size")
	public void toInternationalFormWithNationalExitPrefix_3() {
		this.number = new PhoneNumber(CountryCode.FRANCE, "12345");
		assertEquals("0033 12 34 5", this.number.toInternationalFormWithNationalExitPrefix());
	}	

	@Test
	@DisplayName("toInternationalNationalForm with pair size")
	public void toInternationalNationalForm_0() {
		this.number = new PhoneNumber(CountryCode.FRANCE, "1234");
		assertEquals("+33 (0) 12 34", this.number.toInternationalNationalForm());
	}	

	@Test
	@DisplayName("toInternationalNationalForm with 3-multiple size")
	public void toInternationalNationalForm_1() {
		assertEquals("+33 (0) 123 456 789", this.number.toInternationalNationalForm());
		//
		this.number = new PhoneNumber(CountryCode.FRANCE, "123456");
		assertEquals("+33 (0) 123 456", this.number.toInternationalNationalForm());
	}	

	@Test
	@DisplayName("toInternationalNationalForm with other size")
	public void toInternationalNationalForm_3() {
		this.number = new PhoneNumber(CountryCode.FRANCE, "12345");
		assertEquals("+33 (0) 12 34 5", this.number.toInternationalNationalForm());
	}	

	@Test
	@DisplayName("parse(\"0 3 / 84 / 58 / 34 / 18   \", false)")
	public void parse_false_0() {
		PhoneNumber num = PhoneNumber.parse("0 3 / 84 / 58 / 34 / 18   ", false);
		assertSame(CountryCode.AFGHANISTAN, num.getCountry());
		assertEquals("384583418", num.getLocalNumber());
	}	

	@Test
	@DisplayName("parse(\"00 / 33 / 3 / 84 / 58 / 34 / 18   \", false)")
	public void parse_false_1() {
		PhoneNumber num = PhoneNumber.parse("00 / 33 / 3 / 84 / 58 / 34 / 18   ", false);
		assertSame(CountryCode.FRANCE, num.getCountry());
		assertEquals("384583418", num.getLocalNumber());
	}	

	@Test
	@DisplayName("parse(\"+ 33 / 3 / 84 / 58 / 34 / 18   \", false)")
	public void parse_false_2() {
		PhoneNumber num = PhoneNumber.parse("+ 33 / 3 / 84 / 58 / 34 / 18   ", false);
		assertSame(CountryCode.FRANCE, num.getCountry());
		assertEquals("384583418", num.getLocalNumber());
	}	

	@Test
	@DisplayName("parse(\"+ 33 ( 0 ) / 3 / 84 / 58 / 34 / 18   \", false)")
	public void parse_false_3() {
		PhoneNumber num = PhoneNumber.parse("+ 33 ( 0 ) / 3 / 84 / 58 / 34 / 18   ", false);
		assertSame(CountryCode.FRANCE, num.getCountry());
		assertEquals("384583418", num.getLocalNumber());
	}	

	@Test
	@DisplayName("parse(\"+ 33 [ 0 ] / 3 / 84 / 58 / 34 / 18   \", false)")
	public void parse_false_4() {
		PhoneNumber num = PhoneNumber.parse("+ 33 [ 0 ] / 3 / 84 / 58 / 34 / 18   ", false);
		assertSame(CountryCode.FRANCE, num.getCountry());
		assertEquals("384583418", num.getLocalNumber());
	}	

	@Test
	@DisplayName("parse(\"+ 33 ( 18 ) / 3 / 84 / 58 / 34 / 18   \", false)")
	public void parse_false_5() {
		PhoneNumber num = PhoneNumber.parse("+ 33 ( 18 ) / 3 / 84 / 58 / 34 / 18   ", false);
		assertSame(CountryCode.FRANCE, num.getCountry());
		assertEquals("384583418", num.getLocalNumber());
	}	

	@Test
	@DisplayName("parse(\"+ 33 [ 18 ] / 3 / 84 / 58 / 34 / 18   \", false)")
	public void parse_false_6() {
		PhoneNumber num = PhoneNumber.parse("+ 33 [ 18 ] / 3 / 84 / 58 / 34 / 18   ", false);
		assertSame(CountryCode.FRANCE, num.getCountry());
		assertEquals("384583418", num.getLocalNumber());
	}	

	@Test
	@DisplayName("parse(null, false)")
	public void parse_false_7() {
		assertThrows(IllegalArgumentException.class, () -> {
			PhoneNumber.parse(null, false);
		});
	}	

	@Test
	@DisplayName("parse(\"    \", false)")
	public void parse_false_8() {
		assertThrows(IllegalArgumentException.class, () -> {
			PhoneNumber.parse("    ", false);
		});
	}	

	@Test
	@DisplayName("parse(\"--\", false)")
	public void parse_false_9() {
		assertThrows(IllegalArgumentException.class, () -> {
			PhoneNumber.parse("--", false);
		});
	}	

	@Test
	@DisplayName("parse(\"0 3 / 84 / 58 / 34 / 18   \", true)")
	public void parse_true_0() {
		PhoneNumber num = PhoneNumber.parse("0 3 / 84 / 58 / 34 / 18   ", true);
		assertSame(CountryCode.AFGHANISTAN, num.getCountry());
		assertEquals("384583418", num.getLocalNumber());
	}	

	@Test
	@DisplayName("parse(\"00 / 33 / 3 / 84 / 58 / 34 / 18   \", true)")
	public void parse_true_1() {
		PhoneNumber num = PhoneNumber.parse("00 / 33 / 3 / 84 / 58 / 34 / 18   ", true);
		assertSame(CountryCode.FRANCE, num.getCountry());
		assertEquals("384583418", num.getLocalNumber());
	}	

	@Test
	@DisplayName("parse(\"+ 33 / 3 / 84 / 58 / 34 / 18   \", true)")
	public void parse_true_2() {
		PhoneNumber num = PhoneNumber.parse("+ 33 / 3 / 84 / 58 / 34 / 18   ", true);
		assertSame(CountryCode.FRANCE, num.getCountry());
		assertEquals("384583418", num.getLocalNumber());
	}	

	@Test
	@DisplayName("parse(\"+ 33 ( 0 ) / 3 / 84 / 58 / 34 / 18   \", true)")
	public void parse_true_3() {
		PhoneNumber num = PhoneNumber.parse("+ 33 ( 0 ) / 3 / 84 / 58 / 34 / 18   ", true);
		assertSame(CountryCode.FRANCE, num.getCountry());
		assertEquals("384583418", num.getLocalNumber());
	}	

	@Test
	@DisplayName("parse(\"+ 33 [ 0 ] / 3 / 84 / 58 / 34 / 18   \", true)")
	public void parse_true_4() {
		PhoneNumber num = PhoneNumber.parse("+ 33 [ 0 ] / 3 / 84 / 58 / 34 / 18   ", true);
		assertSame(CountryCode.FRANCE, num.getCountry());
		assertEquals("384583418", num.getLocalNumber());
	}	

	@Test
	@DisplayName("parse(\"+ 33 ( 18 ) / 3 / 84 / 58 / 34 / 18   \", true)")
	public void parse_true_5() {
		assertThrows(IllegalArgumentException.class, () -> {
			PhoneNumber.parse("+ 33 ( 18 ) / 3 / 84 / 58 / 34 / 18   ", true);
		});
	}	

	@Test
	@DisplayName("parse(\"+ 33 [ 18 ] / 3 / 84 / 58 / 34 / 18   \", true)")
	public void parse_true_6() {
		assertThrows(IllegalArgumentException.class, () -> {
			PhoneNumber.parse("+ 33 [ 18 ] / 3 / 84 / 58 / 34 / 18   ", true);
		});
	}	

	@Test
	@DisplayName("parse(null, true)")
	public void parse_true_7() {
		assertThrows(IllegalArgumentException.class, () -> {
			PhoneNumber.parse(null, true);
		});
	}	

	@Test
	@DisplayName("parse(\"    \", true)")
	public void parse_true_8() {
		assertThrows(IllegalArgumentException.class, () -> {
			PhoneNumber.parse("    ", true);
		});
	}	

	@Test
	@DisplayName("parse(\"--\", true)")
	public void parse_true_9() {
		assertThrows(IllegalArgumentException.class, () -> {
			PhoneNumber.parse("--", true);
		});
	}	

	@Test
	@DisplayName("serialize")
	public void serialize_0() {
		String ser = this.number.serialize();
		assertEquals("FRANCE/123456789", ser);
	}	

	@Test
	@DisplayName("unserialize(null)")
	public void unserialize_null() {
		assertThrows(IllegalArgumentException.class, () -> {
			PhoneNumber.unserialize(null);
		});
	}	

	@Test
	@DisplayName("unserialize(\"\")")
	public void unserialize_empty() {
		assertThrows(IllegalArgumentException.class, () -> {
			PhoneNumber.unserialize("");
		});
	}	

	@Test
	@DisplayName("unserialize(\"France/987654321\")")
	public void unserialize_valid0() {
		PhoneNumber num = PhoneNumber.unserialize("France/987654321");
		assertSame(CountryCode.FRANCE, num.getCountry());
		assertEquals("987654321", num.getLocalNumber());
	}	

	@Test
	@DisplayName("unserialize(\"Albania/475X45863\")")
	public void unserialize_valid1() {
		PhoneNumber num = PhoneNumber.unserialize("Albania/475X45863");
		assertSame(CountryCode.ALBANIA, num.getCountry());
		assertEquals("475X45863", num.getLocalNumber());
	}	

	@Test
	@DisplayName("unserialize(\"France\")")
	public void unserialize_invalid0() {
		assertThrows(IllegalArgumentException.class, () -> {
			PhoneNumber.unserialize("France");
		});
	}	

	@Test
	@DisplayName("unserialize(\"0123456789\")")
	public void unserialize_invalid1() {
		assertThrows(IllegalArgumentException.class, () -> {
			PhoneNumber.unserialize("0123456789");
		});
	}	

	@Test
	@DisplayName("unserialize(\"France/\")")
	public void unserialize_invalid2() {
		assertThrows(IllegalArgumentException.class, () -> {
			PhoneNumber.unserialize("France/");
		});
	}	

	@Test
	@DisplayName("unserialize(\"/France\")")
	public void unserialize_invalid3() {
		assertThrows(IllegalArgumentException.class, () -> {
			PhoneNumber.unserialize("/France");
		});
	}	

}
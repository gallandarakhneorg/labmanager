/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.ciadlab.labmanager.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

/** Unit of values.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
public enum Unit {

	/** No units: {@code 0 <= value <= 999}. 
	 */
	NONE("", 1e0), //$NON-NLS-1$

	/** Kilo units: {@code 1,000 <= value <= 9,999}.
	 */
	KILO("k", 1e3), //$NON-NLS-1$

	/** Mega units: {@code 1,000,000 <= value <= 9,999,999}.
	 */
	MEGA("m", 1e6), //$NON-NLS-1$

	/** Giga units: {@code 1,000,000,000 <= value <= 9,999,999,999}.
	 */
	GIGA("g", 1e9), //$NON-NLS-1$

	/** Tera units: {@code 1,000,000,000,000 <= value <= 9,999,999,999,999}.
	 */
	TERA("t", 1e12), //$NON-NLS-1$

	/** Peta units: {@code 1,000,000,000,000,000 <= value <= 9,999,999,999,999,999}.
	 */
	PETA("p", 1e15), //$NON-NLS-1$

	/** Exa units: {@code 1,000,000,000,000,000,000 <= value <= 9,999,999,999,999,999,999}.
	 */
	EXA("e", 1e18), //$NON-NLS-1$

	/** Zetta units: {@code 1,000,000,000,000,000,000,000 <= value <= 9,999,999,999,999,999,999,999}.
	 */
	ZETTA("z", 1e21), //$NON-NLS-1$

	/** Yotta units: {@code 1,000,000,000,000,000,000,000,000 <= value}.
	 */
	YOTTA("y", 1e24); //$NON-NLS-1$

	private final String label;

	private final double factor;
	
	private Unit(String label, double factor) {
		this.label = label;
		this.factor = factor;
	}

	/** Replies the label for the unit.
	 *
	 * @return the label.
	 */
	public String getLabel() {
		return this.label;
	}

	/** Convert the value to the given in the given unit.
	 *
	 * @param value the unit value.
	 * @return the value expressed in the given unit.
	 */
	public Number convertFromUnit(Number value) {
		if (value == null) {
			return null;
		}
		return Double.valueOf(convertFromUnit(value.doubleValue()));
	}

	/** Convert the value to the given in the given unit.
	 *
	 * @param value the unit value.
	 * @return the value expressed in the given unit.
	 */
	public double convertFromUnit(double value) {
		return value / this.factor;
	}

	/** Convert the value to the given from the given unit.
	 *
	 * @param value the value in the current unit.
	 * @return the value expressed in the unit.
	 * @since 3.2
	 */
	public Number convertToUnit(Number value) {
		if (value == null) {
			return null;
		}
		return Double.valueOf(convertFromUnit(value.doubleValue()));
	}

	/** Convert the value to the given from the given unit.
	 *
	 * @param value the value in the current unit.
	 * @return the value expressed in the unit.
	 * @since 3.2
	 */
	public double convertToUnit(double value) {
		return value * this.factor;
	}

	/** Replies the unit that corresponds to the given label.
	 *
	 * @param label the label of the unit.
	 * @return the unit.
	 */
	public static Unit fromUnitLabel(String label) {
		for (final Unit unit : values()) {
			if (label.equalsIgnoreCase(unit.getLabel())) {
				return unit;
			}
		}
		throw new IllegalArgumentException("Unknown label: " + label); //$NON-NLS-1$
	}

	/** Replies the unit that corresponds to the given value.
	 *
	 * @param value the value of the unit.
	 * @param valueUnit the original unit of the provided value. If it is {@code null}, {@link Unit#NONE} is assumed.
	 * @return the unit, never {@code null}.
	 * @since 3.2
	 */
	public static Unit fromNumericValue(Number value) {
		if (value != null) {
			final int digitCount = numberOfDigits(value);
			final int groups = (digitCount - 1) / 3;
			final Unit[] values = values();
			if (groups >= values.length) {
				return values[values.length - 1];
			}
			return values[groups];
		}
		throw new IllegalArgumentException("Unknown value: " + value); //$NON-NLS-1$
	}

	private static int numberOfDigits(BigInteger value) {
		BigInteger dvalue = value.abs();
		BigInteger accumulator = new BigInteger("10"); //$NON-NLS-1$
		int count = 1;
		while (dvalue.compareTo(accumulator) >= 0) {
			++count;
			if (count >= 25) {
				// In the context of this Unit enumeration, we do not need to count the digits of larger numbers
				return count;
			}
			accumulator = accumulator.multiply(BigInteger.TEN);
		}
		return count;
	}
	
	private static int numberOfDigits(Number value) {
		if (value instanceof BigInteger) {
			return numberOfDigits((BigInteger) value);
		}
		if (value instanceof BigDecimal) {
			return numberOfDigits(((BigDecimal) value).toBigInteger());
		}
		final long lvalue = Math.abs(value.longValue());
		long accumulator = 10;
		int count = 1;
		while (lvalue >= accumulator) {
			++count;
			if (count >= 25) {
				// In the context of this Unit enumeration, we do not need to count the digits of larger numbers
				return count;
			}
			accumulator *= 10;
		}
		return count;
	}

}

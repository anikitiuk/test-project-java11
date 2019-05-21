package com.somecompany.app;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import java.math.BigDecimal;

/**
 * Test cases for {@link CalculationModelDetail} class.
 */
public class CalculationModelDetailTest extends TestCase {
	private final static Log LOG = LogFactory.getLog(CalculationModelDetailTest.class);

	public final void testConstructor1() {
		final CalculationModelDetail detail = new CalculationModelDetail(new BigDecimal("0.12"), new Price(5),
				null);
		LOG.info("perCent: " + detail.getPerCent() + ", absolute: " + detail.getAbsolute() + ", priceRange: " + detail.getPriceRange());
		assertEquals(detail.getPerCent().toString(), "0.12");
		assertEquals(detail.getAbsolute().toString(), "5");
		assertNull(detail.getPriceRange());
	}

	// perCent==null, absolute!=null
	public void testCalculate1() {
		final CalculationModelDetail detail = new CalculationModelDetail();
		detail.setAbsolute(new Price(5));
		detail.setPerCent(null);

		final Price in = new Price(1);

		final Price calculated = detail.calculate(in, true);

		assertEquals("6.00", calculated.toString());
	}

	// perCent==null; absolute==null
	public void testCalculate2() {
		final CalculationModelDetail detail = new CalculationModelDetail();
		detail.setAbsolute(null);
		detail.setPerCent(null);

		final Price in = new Price(1);

		final Price calculated = detail.calculate(in, true);

		assertEquals("1.00", calculated.toString());
	}

	// perCent!=null; absolute==null
	public void testCalculate3() {
		final CalculationModelDetail detail = new CalculationModelDetail();
		detail.setAbsolute(null);
		detail.setPerCent(BigDecimal.valueOf(0.5));

		final Price in = new Price(100);

		final Price calculated = detail.calculate(in, true);

		assertEquals("50.00", calculated.toString());
	}

	// perCent!=null; absolute!=null; firstPerCent = true
	public void testCalculate4() {
		final CalculationModelDetail detail = new CalculationModelDetail();
		detail.setAbsolute(new Price(10));
		detail.setPerCent(BigDecimal.valueOf(0.1));

		final Price in = new Price(100);

		final Price calculated = detail.calculate(in, true);

		assertEquals("120.00", calculated.toString());
	}
	// perCent!=null; absolute!=null; firstPerCent = false
	public void testCalculate5() {
		final CalculationModelDetail detail = new CalculationModelDetail();
		detail.setAbsolute(new Price(10));
		detail.setPerCent(BigDecimal.valueOf(0.1));

		final Price in = new Price(100);

		final Price calculated = detail.calculate(in, false);

		assertEquals("121.00", calculated.toString());
	}

	public void testCalculate6() {
		final CalculationModelDetail detail = new CalculationModelDetail();
		detail.setAbsolute(new Price(10));
		detail.setPerCent(BigDecimal.valueOf(0.1));

		final Price in = new Price("10.25");

		final Price calculated = detail.calculate(in, false);

		assertEquals("22.28", calculated.toString());
	}

	// The price is not inside the passed range!
	public void testCalculate7() {
		final CalculationModelDetail detail = new CalculationModelDetail();
		detail.setPriceRange(new PriceRange(0,10));
		final Price p = new Price(30);
		try {
			final Price calculated = detail.calculate(p, true);
			fail();
		} catch (final Exception ex) {
			LOG.info("Caught expected " + ex.getMessage());
		}
	}

	public void testEquals1() {
		assertEquals(new CalculationModelDetail(BigDecimal.valueOf(0.11), new Price(5), new PriceRange(0,19)),
				new CalculationModelDetail(BigDecimal.valueOf(0.11), new Price(5), new PriceRange(0,19)));
		assertEquals(new CalculationModelDetail(BigDecimal.valueOf(1), new Price("7"), new PriceRange(null,null)),
				new CalculationModelDetail(BigDecimal.valueOf(1), new Price("7"), new PriceRange(null, null)));
		assertNotSame(new CalculationModelDetail(null, new Price("7"), new PriceRange(null,null)),
				new CalculationModelDetail(null, new Price(9), new PriceRange(null, null)));

		final CalculationModelDetail detail = new CalculationModelDetail(null, null, null);
		assertTrue(detail.equals(detail));
	}

	/**
	 * mainly for line coverage and to make sure toString() works without
	 * 
	 * {@link NullPointerException}
	 */
	public void testToString() {
		assertNotNull(new CalculationModelDetail().toString());
	}

	public void testToXmlAndParse1() {
		final CalculationModelDetail written = new CalculationModelDetail(BigDecimal.valueOf(0.11), new Price(5)
				, new PriceRange(0,19));

		final Element writtenElement = written.toXml();
		LOG.info("written: " + writtenElement.asXML());

		final CalculationModelDetail parsed = CalculationModelDetail.parse(writtenElement);
		assertNotNull(parsed);
		assertEquals(parsed, written);
	}

	public void testToXmlAndParse2() {
		final CalculationModelDetail written = new CalculationModelDetail(null, new Price(16)
				, new PriceRange(17,76));

		final Element writtenElement = written.toXml();
		LOG.info("written: " + writtenElement.asXML());

		final CalculationModelDetail parsed = CalculationModelDetail.parse(writtenElement);
		assertNotNull(parsed);
		assertEquals(parsed, written);
	}

	public void testToXmlAndParse3() {
		final CalculationModelDetail written = new CalculationModelDetail(BigDecimal.valueOf(0.12), new Price(88)
				, null);

		final Element writtenElement = written.toXml();
		LOG.info("written: " + writtenElement.asXML());

		final CalculationModelDetail parsed = CalculationModelDetail.parse(writtenElement);
		assertNotNull(parsed);
		assertEquals(parsed, written);
	}

	public void testToXmlAndParse4() {
		final CalculationModelDetail written = new CalculationModelDetail(null, null
				, null);

		final Element writtenElement = written.toXml();
		LOG.info("written: " + writtenElement.asXML());

		final CalculationModelDetail parsed = CalculationModelDetail.parse(writtenElement);
		assertNotNull(parsed);
		assertEquals(parsed, written);
	}
}

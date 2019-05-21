package com.somecompany.app;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import java.math.BigDecimal;

/**
 * Test cases for {@link CalculationModelDetailList} class.
 */
public class CalculationModelDetailListTest extends TestCase {
    private final static Log LOG = LogFactory.getLog(CalculationModelDetailTest.class);
	// details with price ranges
	public final void testFind1() {
		final CalculationModelDetailList list = new CalculationModelDetailList();

		final CalculationModelDetail detail0 = new CalculationModelDetail();
		list.add(detail0);
		detail0.setPriceRange(new PriceRange(0, 19));

		final CalculationModelDetail detail1 = new CalculationModelDetail();
		list.add(detail1);
		detail1.setPriceRange(new PriceRange(20, 29));

		assertEquals(detail0, list.find(new Price(19)));
		assertEquals(detail1, list.find(new Price(20)));
		assertNull(list.find(new Price(30)));
	}

	// one detail without price range
	public final void testFind2() {
		final CalculationModelDetailList list = new CalculationModelDetailList();

		final CalculationModelDetail detail0 = new CalculationModelDetail();
		list.add(detail0);
		detail0.setPriceRange(new PriceRange(0, 19));

		final CalculationModelDetail detail1 = new CalculationModelDetail();
		list.add(detail1);
		detail1.setPriceRange(null);

		assertEquals(detail0, list.find(new Price(19)));
		assertEquals(detail1, list.find(new Price(20)));
		assertNotNull(list.find(new Price(30)));
	}

    // one detail without price range
    public final void testSort1() {
        final CalculationModelDetailList list = new CalculationModelDetailList();

        final CalculationModelDetail detail0 = new CalculationModelDetail();
        list.add(detail0);
        detail0.setPriceRange(new PriceRange(21, 30));

        final CalculationModelDetail detail1 = new CalculationModelDetail();
        list.add(detail1);
        detail1.setPriceRange(new PriceRange(10, 19));

        final CalculationModelDetail detail2 = new CalculationModelDetail();
        list.add(detail2);
        PriceRange p = new PriceRange(null, new Price(9));
        detail2.setPriceRange(p);

        final CalculationModelDetail detail3 = new CalculationModelDetail();
        list.add(detail3);
        detail3.setPriceRange(new PriceRange(34, 41));

        final CalculationModelDetail detail4 = new CalculationModelDetail();
        list.add(detail4);
        detail4.setPriceRange(null);

        list.sortByMinimumAscending();

        assertEquals(new PriceRange(null, new Price(9)), list.get(0).getPriceRange());
        assertEquals(new PriceRange(10,19), list.get(1).getPriceRange());
        assertEquals(new PriceRange(21,30), list.get(2).getPriceRange());
        assertEquals(new PriceRange(34,41), list.get(3).getPriceRange());
        assertNull(list.get(4).getPriceRange());
    }

    public void testToXmlAndParse1() {
        final CalculationModelDetailList written = new CalculationModelDetailList();

        final CalculationModelDetail detail0 = new CalculationModelDetail();
        written.add(detail0);
        detail0.setPerCent(new BigDecimal("0.12"));
        detail0.setAbsolute(new Price(132));
        detail0.setPriceRange(new PriceRange(21, 30));

        final CalculationModelDetail detail1 = new CalculationModelDetail();
        written.add(detail1);
        detail1.setPerCent(new BigDecimal("0.14"));
        detail1.setAbsolute(new Price(8));
        detail1.setPriceRange(new PriceRange(new Price(7), null));

        final Element writtenElement = written.toXml();
        LOG.info("written: " + writtenElement.asXML());

        final CalculationModelDetailList parsed = CalculationModelDetailList.parse(writtenElement);
        assertNotNull(parsed);
        assertEquals(parsed, written);
    }

    public void testToXmlAndParse2() {
        final CalculationModelDetailList written = new CalculationModelDetailList();

        final Element writtenElement = written.toXml();
        LOG.info("written: " + writtenElement.asXML());

        final CalculationModelDetailList parsed = CalculationModelDetailList.parse(writtenElement);
        assertNotNull(parsed);
        assertEquals(parsed, written);
    }
}
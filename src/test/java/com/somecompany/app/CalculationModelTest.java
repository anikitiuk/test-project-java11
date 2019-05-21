package com.somecompany.app;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import java.math.BigDecimal;

public class CalculationModelTest extends TestCase {
    private final static Log LOG = LogFactory.getLog(CalculationModelTest.class);

    public final void testConstructor1() {
        try {
            new CalculationModel((CalculationModelDetailList) null);
            fail();
        } catch (final Exception ex) {
            LOG.info("Caught expected " + ex.getMessage());
        }
    }

    public final void testConstructor2() {
        CalculationModelDetailList list = new CalculationModelDetailList();
        list.add(0, new CalculationModelDetail(BigDecimal.valueOf(0.5), new Price("7"), null));
        list.add(1, new CalculationModelDetail(BigDecimal.valueOf(0.75), new Price("55"), null));
        final CalculationModel model = new CalculationModel(list);
        LOG.info("index: 0, perCent: " + model.getDetailList().get(0).getPerCent() +
                ", absolute: " + model.getDetailList().get(0).getAbsolute() +
                ", priceRange: " + model.getDetailList().get(0).getPriceRange());
        assertEquals(model.getDetailList().get(0).getPerCent().toString(), "0.5");
        assertEquals(model.getDetailList().get(0).getAbsolute().toString(), "7");
        assertNull(model.getDetailList().get(0).getPriceRange());
        LOG.info("index: 1, perCent: " + model.getDetailList().get(1).getPerCent() +
                ", absolute: " + model.getDetailList().get(1).getAbsolute() +
                ", priceRange: " + model.getDetailList().get(1).getPriceRange());
        assertEquals(model.getDetailList().get(1).getPerCent().toString(), "0.75");
        assertEquals(model.getDetailList().get(1).getAbsolute().toString(), "55");
        assertNull(model.getDetailList().get(1).getPriceRange());
    }

    public final void testCalculate1() {
        CalculationModelDetailList list = new CalculationModelDetailList();
        list.add(0, new CalculationModelDetail(null, null,
                new PriceRange(null, new Price(10))));
        list.add(1, new CalculationModelDetail(BigDecimal.valueOf(0.5), new Price("7"),
                new PriceRange(11, 30)));
        list.add(2, new CalculationModelDetail(BigDecimal.valueOf(0.75), new Price("55"),
                new PriceRange(new Price(31), null)));
        final CalculationModel model = new CalculationModel(list);
        assertEquals(new BigDecimal("5.00"), model.calculate(new Price(5), true).getAmount());
        assertEquals(new BigDecimal("9.00"), model.calculate(new Price(9), true).getAmount());
        assertEquals(new BigDecimal("25.00"), model.calculate(new Price(12), true).getAmount());
        assertEquals(new BigDecimal("175.00"), model.calculate(new Price(45), false).getAmount());
    }

    public final void testCalculate2() {
        CalculationModelDetailList list = new CalculationModelDetailList();
        list.add(0, new CalculationModelDetail(BigDecimal.valueOf(1), new Price("10"),
                null));
        final CalculationModel model = new CalculationModel(list);
        assertEquals(new BigDecimal("40.00"), model.calculate(new Price(10), false).getAmount());
        assertEquals(new BigDecimal("60.00"), model.calculate(new Price(20), false).getAmount());
        assertEquals(new BigDecimal("80.00"), model.calculate(new Price(30), false).getAmount());
    }

    public final void testEquals() {
        CalculationModelDetailList list = new CalculationModelDetailList();
        list.add(0, new CalculationModelDetail(BigDecimal.valueOf(1), new Price("10"),
                null));
        CalculationModelDetailList list1 = new CalculationModelDetailList();
        list1.add(0, new CalculationModelDetail(new BigDecimal("1"), new Price("10"),
                null));
        CalculationModelDetailList list2 = new CalculationModelDetailList();
        list2.add(0, new CalculationModelDetail(null, new Price("10"),
                null));
        final CalculationModel model = new CalculationModel(list);
        assertTrue(model.equals(new CalculationModel(list1)));
        assertFalse(model.equals(new CalculationModel(list2)));
    }

    public void testToXmlAndParse1() {
        CalculationModelDetailList list = new CalculationModelDetailList();
        list.add(0, new CalculationModelDetail(null, null,
                new PriceRange(null, new Price(10))));
        list.add(1, new CalculationModelDetail(BigDecimal.valueOf(0.5), new Price("7"),
                new PriceRange(11, 30)));
        list.add(2, new CalculationModelDetail(BigDecimal.valueOf(0.75), new Price("55"),
                new PriceRange(new Price(31), null)));
        final CalculationModel written = new CalculationModel(list);

        final Element writtenElement = written.toXml();
        LOG.info("written: " + writtenElement.asXML());

        final CalculationModel parsed = CalculationModel.parse(writtenElement);
        assertNotNull(parsed);
        assertEquals(parsed, written);
    }
}

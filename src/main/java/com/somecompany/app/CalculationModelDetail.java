package com.somecompany.app;

import java.util.Iterator;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Detail for a {@link CalculationModel}.
 * 
 */
public class CalculationModelDetail {
	public CalculationModelDetail(BigDecimal perCent, Price absolute, PriceRange priceRange) {
		super();
		this.perCent = perCent;
		this.absolute = absolute;
		this.priceRange = priceRange;
	}

	public CalculationModelDetail() {
		super();
	}

	private final static String XML_PERCENT = "perCent";

	private final static String XML_ABSOLUTE = "absolute";

	private BigDecimal perCent;

	/**
	 * PerCent (0.5 = 50%).
	 * 
	 * @return
	 */
	public final BigDecimal getPerCent() {
		return perCent;
	}

	public final void setPerCent(BigDecimal perCent) {
		this.perCent = perCent;
	}

	private Price absolute;

	public final Price getAbsolute() {
		return absolute;
	}

	public final void setAbsolute(Price absolute) {
		this.absolute = absolute;
	}

	private PriceRange priceRange;

	public final PriceRange getPriceRange() {
		return priceRange;
	}

	public final void setPriceRange(PriceRange priceRange) {
		this.priceRange = priceRange;
	}

	public final Price calculate(final Price price, final boolean firstPerCent) {
		// TODO - homework: support firstPerCent method argument
		// TODO - homework: support perCent attribute
		// TODO - homework: check if the price is inside the passed range, if
		// not, throw an IllegalArgumentException

		Price out = price;

		if(getPriceRange() != null && false == getPriceRange().contains(out)){
			throw new IllegalArgumentException("The price is not inside the passed range!");
		}

		if (getAbsolute() != null && getPerCent()!= null) {
			if (firstPerCent) {
				out = price.multiply(getPerCent()).add(price).add(getAbsolute());
		}
			else {
				out = price.add(getAbsolute()).multiply(getPerCent()).add(price.add(getAbsolute()));
			}
		}
		else if (getAbsolute() != null) {
			out = price.add(getAbsolute());
		}
		else if (getPerCent() != null) {
			out = price.multiply(getPerCent());
		}

		return new Price(out.getAmount().setScale(2, RoundingMode.HALF_EVEN));
	}

	/**
	 * Override of non-static equals method.
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		// includes obj == null and obj is not a CalculationModelDetail object
		if (false == (obj instanceof CalculationModelDetail)) {
			return false;
		}

		// now that we are sure that obj != null and obj is a CalculationModelDetail
		// object, do the cast and compare perCent, absolute and priceRange
		final CalculationModelDetail that = (CalculationModelDetail) obj;

		if (!comparePerCents(this.getPerCent(),that.getPerCent())) {
			return false;
		}

		if (false == Price.equals(this.getAbsolute(), that.getAbsolute())) {
			return false;
		}

		if (false == PriceRange.equals(this.getPriceRange(),that.getPriceRange())) {
			return false;
		}

		return true;
	}

	/**
	 * Compare {@BigDecimal} objects returning boolean.
	 * @param perCent1
	 * @param perCent2
	 * @return
	 */
	public boolean comparePerCents(BigDecimal perCent1, BigDecimal perCent2){
		if(perCent1 == null && perCent2 == null){
			return true;
		}
		else if(perCent1 == null || perCent2 == null){
			return false;
		}
		else {
			return perCent1.compareTo(perCent2) == 0;
		}
	}

	@Override
	public String toString() {
		return "CalculationModelDetail: " + getAbsolute() + ", " + getPerCent() + "%" + "; priceRange: "
				+ getPriceRange();
	}

	public Element toXml() {
		final Element out = DocumentHelper.createElement("calculationModelDetail");

		if (getPerCent() != null) {
			out.add(DocumentHelper.createElement("perCent").addAttribute("value", getPerCent().toString()));
		}

		if (getAbsolute() != null) {
			out.add(getAbsolute().toXml(XML_ABSOLUTE));
		}

		if (getPriceRange() != null) {
			out.add(getPriceRange().toXml());
		}

		return out;
	}

	public final static CalculationModelDetail parse(final Element parent) {
		BigDecimal perCent = null;
		Price absolute = null;
		PriceRange priceRange = null;

		for (final Iterator<?> it = parent.elementIterator(); it.hasNext();) {
			final Element element = (Element) it.next();

			if (element.getName().equals(XML_PERCENT)) {
				perCent = new BigDecimal(element.attributeValue("value"));
			} else if (element.getName().equals(XML_ABSOLUTE)) {
				absolute = Price.parse(element);
			} else if (element.getName().equals("priceRange")){
				priceRange = PriceRange.parse(element);
			}
		}

		return new CalculationModelDetail(perCent, absolute, priceRange);
	}
}

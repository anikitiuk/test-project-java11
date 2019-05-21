package com.somecompany.app;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * List of {@link CalculationModelDetail} objects.
 */
@SuppressWarnings("serial")
public class CalculationModelDetailList extends ArrayList<CalculationModelDetail> {
	public CalculationModelDetail find(final Price price) {
		return this.stream()
				.filter((CalculationModelDetail d) -> d.getPriceRange() == null || d.getPriceRange().contains(price))
				.findFirst().orElse(null);
	}

	/**
	 * Sort {@link CalculationModelDetail} objects by the minimum of their {@link PriceRange} attribute.
	 *
	 * @return
	 */
	public void sortByMinimumAscending() {
		sort((CalculationModelDetail d1, CalculationModelDetail d2) -> compare(d1, d2));
	}

	/**
	 * Compares two {@link CalculationModelDetail} objects considering that the objects with {@link PriceRange} = null
	 * should be placed at the end of the sorted list.
	 *
	 * @return
	 */
	private int compare(CalculationModelDetail d1, CalculationModelDetail d2) {
		if(d1.getPriceRange() != null && d2.getPriceRange() != null) {
			if (d1.getPriceRange().getMinimum() == null && d2.getPriceRange().getMinimum() == null)
				return 0;
			else if (d1.getPriceRange().getMinimum() == null)
				return Integer.MIN_VALUE;
			else if (d2.getPriceRange().getMinimum() == null)
				return Integer.MAX_VALUE;
			else
				return d1.getPriceRange().getMinimum().getAmount().compareTo(d2.getPriceRange().getMinimum().getAmount());
		}
		else if (d1.getPriceRange() == null && d2.getPriceRange() == null)
				return 0;
		else if (d1.getPriceRange() == null)
			return Integer.MAX_VALUE;
		else
			return Integer.MIN_VALUE;
	}

	public Element toXml() {
		final Element out = DocumentHelper.createElement("calculationModelDetailList");

		for (final Iterator<CalculationModelDetail> it = this.listIterator(); it.hasNext();){
			out.add(it.next().toXml());
		}

		return out;
	}

	public final static CalculationModelDetailList parse(final Element parent) {
		CalculationModelDetailList out = new CalculationModelDetailList();
		int i = 0;

		for (final Iterator<?> it = parent.elementIterator(); it.hasNext();) {
			final Element element = (Element) it.next();
			if (element.getName().equals("calculationModelDetail")) {
				if (element != null) {
					out.add(i, CalculationModelDetail.parse(element));
					i++;
				}
			}
		}

		return out;
	}
}
package com.somecompany.app;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Iterator;

public class CalculationModel {
    public CalculationModel(CalculationModelDetailList detailList){
        super();

        if (detailList == null) {
            throw new NullPointerException("Tried to construct a new CalculationModel object with detailList==null");
        }

        this.detailList = detailList;
    }

    private CalculationModelDetailList detailList;

    public final CalculationModelDetailList getDetailList() {
        return detailList;
    }

    /**
     * Returns the changed price using an implemented calculate method in {@link CalculationModelDetail} class.
     *
     * @return
     */
    public final Price calculate(final Price price, final boolean firstPerCent){
        return detailList.find(price).calculate(price, firstPerCent);
    }

    public Element toXml() {
        final Element out = DocumentHelper.createElement("calculationModel");

        if (getDetailList() != null) {
            out.add(getDetailList().toXml());
        }

        return out;
    }

    public final static CalculationModel parse(final Element parent) {
        CalculationModelDetailList detailList = null;

        for (final Iterator<?> it = parent.elementIterator(); it.hasNext();) {
            final Element element = (Element) it.next();

            if (element.getName().equals("calculationModelDetailList")) {
                detailList = CalculationModelDetailList.parse(element);
            }
        }

        return new CalculationModel(detailList);
    }

    @Override
    public boolean equals(Object obj){
        if (false == (obj instanceof CalculationModel)) {
            return false;
        }

        final CalculationModel that = (CalculationModel) obj;

        if (!this.detailList.equals(that.detailList)) {
            return false;
        }

        return true;
    }
}

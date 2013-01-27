package org.williamjoy.gexpense.googlechart;

public class ColumnChart extends AbstractChart {
    public ColumnChart(String template) {
        super(template);
    }

    public ColumnChart(StringBuilder template) {
        super(template);
    }

    public static final String ColumnChart = "AreaChart";

    @Override
    protected void doTemplate() {
        chartArea.left = "50";
        chartArea.width="\"90%\"";
//        mTemplator.setVariable("chartArea.left", chartArea.left);
//        mTemplator.setVariable("chartArea.top", chartArea.top);
//        mTemplator.setVariable("chartArea.width", chartArea.width);
//        mTemplator.setVariable("chartArea.height", chartArea.height);
//        mTemplator.addBlock("chartArea");
        mTemplator.setVariable("ChartFunction", ColumnChart);
        // width = width << 2;
    }
}

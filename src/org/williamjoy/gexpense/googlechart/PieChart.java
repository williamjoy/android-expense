package org.williamjoy.gexpense.googlechart;

public class PieChart extends AbstractChart {
    public PieChart(String template) {
        super(template);
    }

    public PieChart(StringBuilder template) {
        super(template);
    }

    public static final String PIE_CHART = "PieChart";

    private boolean is3D = false;

    public boolean is3D() {
        return is3D;
    }

    public void setEnable3D(boolean is3d) {
        this.is3D = is3d;
    }

    @Override
    protected void doTemplate() {
        mTemplator.setVariable("ChartFunction", PIE_CHART);
        
        position = "left";
//        if (is3D) {
//            mTemplator.setVariable("is3D", "is3D: true");
//            mTemplator.addBlock("ChartOption");
//        }
    }
}

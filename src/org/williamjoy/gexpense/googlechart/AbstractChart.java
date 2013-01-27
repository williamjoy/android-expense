package org.williamjoy.gexpense.googlechart;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.miniTemplator.MiniTemplator;

import android.util.Log;

public abstract class AbstractChart {

    public AbstractChart(String template) {
        this.mTemplator = new MiniTemplator(new StringBuilder(template));
    }

    public AbstractChart(StringBuilder template) {
        this.mTemplator = new MiniTemplator(template);
    }

    protected String title = "";
    protected int width = 480;
    protected int height = 800;
    protected String position = "none";
    protected String backgroundColor = "#eeeeee";

    protected abstract void doTemplate();

    protected ChartArea chartArea = new ChartArea();

    protected class ChartArea {
        String left = "5";
        String top = "25";
        String width = "\"90%\"";
        String height = "\"90%\"";
    }

    public String getHTMLData() {

        doTemplate();

        mTemplator.setVariable("backgroundColor", backgroundColor);
        mTemplator.setVariable("position", position);
        mTemplator.setVariable("width", width);
        mTemplator.setVariable("height", height);
        mTemplator.setVariable("title", title);
        mTemplator.setVariable("dataTable", dataTable.toString());
        return mTemplator.generateOutput();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        this.title = mTitle;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int mWidth) {
        this.width = mWidth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int mHeight) {
        this.height = mHeight;
    }

    MiniTemplator mTemplator;
    private StringBuilder dataTable = new StringBuilder();

    public void setDataTable(HashMap<String, Double> map) {
        DecimalFormat format = new DecimalFormat("#.##");
        dataTable = new StringBuilder("new google.visualization.DataTable();\n"
                + "data.addColumn('string', 'Category');\n"
                + "data.addColumn('number', 'Money');\n" + "data.addRows([\n");
        for (String key : map.keySet()) {
            dataTable.append(String.format("\t['%s',%s],\n", key,
                    format.format(map.get(key))));
            Log.d("Total", map.get(key) + "");
        }
        dataTable.replace(dataTable.length() - 2, dataTable.length() - 1,
                "\t])");
    }

    public void setDataTable(String rawData) {
        dataTable = new StringBuilder("new google.visualization.DataTable();\n"
                + "data.addColumn('date', 'Date');\n"
                + "data.addColumn('number', 'Money');\n"
                + "data.addRows([\n");
        dataTable.append(rawData);
        dataTable.replace(dataTable.length() - 2, dataTable.length() - 1,
                "\t]);");
    }
}

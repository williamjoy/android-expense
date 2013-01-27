package org.williamjoy.gexpense.googlechart;

import java.util.HashMap;

public class CloumnChart {
    private String mTitle = "Pie Chart";
    private int mWidth = 480;
    private int mHeight = 800;
    private boolean enable3D = true;
    private int _id;

    public String getHTMLData() {
        return _head + String.format(_data)
                + String.format(options, mTitle, enable3D, mWidth, mHeight);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public boolean isEnable3D() {
        return enable3D;
    }

    public void setEnable3D(boolean enable3D) {
        this.enable3D = enable3D;
    }

    static String _head = "<html>\n"
            + "  <head>\n"
            + "    <script type=\"text/javascript\" src=\"http://www.google.com/jsapi\"></script>\n"
            + "    <script type=\"text/javascript\">\n"
            + "      google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});\n"
            + "      google.setOnLoadCallback(drawChart);\n"
            + "      function drawChart() {\n";

    static String _data = "var data = google.visualization.arrayToDataTable([\n"
            + "          ['Type', 'Example'],\n"
            + "          ['Hello',  3.14],\n" 
            + "          ['World',  1.59]\n" 
            + "        ]);\n";

    static String options = "        var options = {\n"
            + "          title: '%s',\n"
            + "          is3D: '%b',\n"
            + "          legend: {position: 'bottom'},\n"
            + "          chartArea:{left:10,top:20,width:\"65\\u0025\",height:\"45\\u0025\"}\n"
            + "        };\n"
            + "        var chart = new google.visualization.PieChart(document.getElementById('chart_div'));\n"
            + "        chart.draw(data, options);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body>\n"
            + "    <div id=\"chart_div\" style=\"width: %d; height: %dpx;\"></div>\n"
            + "  </body>\n</html>\n";

    public void setDataTable(HashMap<String, Double> pieChartData) {
        StringBuilder data = new StringBuilder(
                "var data = google.visualization.arrayToDataTable([\n          ['Type', 'Money'],\n");
        for (String category : pieChartData.keySet()) {
            data.append(String.format("          ['%s',      %.2f],\n",
                    category, pieChartData.get(category)));
        }
        data.append("        ]);\n");
        _data = data.toString();
    }
}

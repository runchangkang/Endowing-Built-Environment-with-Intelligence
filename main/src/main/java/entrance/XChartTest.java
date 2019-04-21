package entrance;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class XChartTest {
    XYChart xyChart;
    List<Double> userXData;
    List<Double> userYData;
    SwingWrapper<XYChart> swingWrapper;
    public XChartTest() {
        xyChart = new XYChartBuilder().xAxisTitle("X").yAxisTitle("Y").build();
        xyChart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        xyChart.getStyler().setMarkerSize(16);
        xyChart.getStyler().setYAxisMax(10000.0);
        xyChart.getStyler().setYAxisMin(-1000.0);
        xyChart.getStyler().setXAxisMax(6000.0);
        xyChart.getStyler().setXAxisMin(-5000.0);
//        xyChart.getStyler().
        List<Double> xData = Arrays.asList(0.0,4170.0,0.0,4520.0);
        List<Double> yData = Arrays.asList(0.0,0.0,8680.0,8690.0);

        xyChart.addSeries("anchors",xData,yData);
        userXData = Arrays.asList(0.0);
        userYData = Arrays.asList(0.0);
        xyChart.addSeries("userLoc",userXData,userYData);
        swingWrapper = new SwingWrapper<>(xyChart);
        swingWrapper.displayChart();


    }
    public void updateUserData(double x, double y){
        this.userYData = new LinkedList<>();
        userYData.add(y);
        this.userXData = new LinkedList<>();
        userXData.add(x);
        xyChart.updateXYSeries("userLoc",userXData,userYData,null);
        display();

    }
    public void display(){

        swingWrapper.repaintChart();


    }

    public static void main(String[] args) {
        XChartTest xChartTest = new XChartTest();

    }







}

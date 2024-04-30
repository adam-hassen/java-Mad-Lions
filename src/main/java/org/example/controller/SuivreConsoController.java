package org.example.controller;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.example.entity.Action;
import org.example.entity.TypeName;
import org.example.service.ActionService;
import org.example.service.TypeNameService;
import org.jfree.chart.*;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.geom.Ellipse2D;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import javafx.scene.control.Label;
import org.jfree.data.xy.DefaultXYDataset;

import java.awt.*;

public class SuivreConsoController {
    private ActionService query;
    private TypeNameService query2;
    @FXML
    private VBox vboxside;
    @FXML
    private StackPane  chartPanel;
    @FXML
    private StackPane  scatterPanel;
    @FXML
    private Label firstcharterror;
    @FXML
    public void initialize() {
        query2 = new TypeNameService();
        query = new ActionService();
        //first chart
        ActionService.ChartData chartData = query.firstChart(1);
        List<Double> data2 = chartData.getData();
        List<String> labels2 = chartData.getLabels();
        if ((data2!=null) && (labels2!=null)){
            DefaultPieDataset dataset = new DefaultPieDataset();
            for (int i = 0; i < labels2.size(); i++) {
                dataset.setValue(labels2.get(i), data2.get(i));
            }
            firstcharterror.setVisible(true);
            JFreeChart chart = createStyledPieChart(dataset);
            ChartViewer chartViewer = new ChartViewer(chart);
            chartPanel.getChildren().add(chartViewer);
        }
        else {
            firstcharterror.setVisible(false);
        }
        //end first chart
        //scatter chart
        ActionService.ChartData scatterData = query.scatterchart(1);
        List<Double> data3 = chartData.getData();
        List<String> labels3 = scatterData.getLabels();
        DefaultXYDataset dataset = new DefaultXYDataset();
        JFreeChart chart = createStyledScatterPlot(dataset);
        ChartViewer chartViewer = new ChartViewer(chart);
        scatterPanel.getChildren().add(chartViewer);
        //end scatter chart
    }

    private static class ModernColorGenerator {
        private final Random random = new Random();

        public Color getRandomColor() {
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);
            int alpha = 178;
            return new Color(red, green, blue, alpha);
        }
    }
    private JFreeChart createStyledPieChart(DefaultPieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart("", dataset, true, false, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(null);
        ModernColorGenerator colorGenerator = new ModernColorGenerator();
        for (int i = 0; i < dataset.getItemCount(); i++) {
            Color color = colorGenerator.getRandomColor();
            int alpha = 235;
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            plot.setSectionPaint(dataset.getKey(i), color);
        }
        chart.setBackgroundPaint(new Color(0, 0, 0, 0));
        chart.setBorderVisible(false);
        plot.setBackgroundPaint(new Color(0, 0, 0, 0));
        plot.setLabelBackgroundPaint(new Color(0, 0, 0, 0));
        plot.setLabelOutlinePaint(new Color(0, 0, 0, 0));
        plot.setShadowPaint(new Color(0, 0, 0, 0));
        LegendItemCollection legendItems = new LegendItemCollection();
        for (int i = 0; i < dataset.getItemCount(); i++) {
            Comparable key = dataset.getKey(i);
            Paint paint = plot.getSectionPaint(key);
            LegendItem item = new LegendItem(key.toString(), null, null, null, Plot.DEFAULT_LEGEND_ITEM_BOX, paint);
            item.setLabelPaint(Color.WHITE);
            legendItems.add(item);
        }
        LegendTitle legend = new LegendTitle(new LegendItemSource() {
            @Override
            public LegendItemCollection getLegendItems() {
                return legendItems;
            }
        });
        legend.setPosition(RectangleEdge.BOTTOM);
        legend.setBackgroundPaint(new Color(0, 0, 0, 0));
        legend.setMargin(0, 0, 0, 0);
        legend.setItemFont(new Font("Segoe UI\", sans-serif", Font.BOLD, 14));
        chart.removeLegend();
        chart.addLegend(legend);
        return chart;
    }
    private JFreeChart createStyledScatterPlot(DefaultXYDataset dataset) {
        JFreeChart chart = ChartFactory.createScatterPlot("", "X", "Y", dataset, PlotOrientation.VERTICAL, true, false, false);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainCrosshairVisible(false);
        plot.setRangeCrosshairVisible(false);
        plot.setDomainZeroBaselineVisible(false);
        plot.setRangeZeroBaselineVisible(false);
        chart.setBackgroundPaint(new Color(0, 0, 0, 0));
        chart.setBorderVisible(true);
        plot.setBackgroundPaint(new Color(0, 0, 0, 0));
        plot.setOutlinePaint(new Color(0, 0, 0, 0));
        ValueAxis domainAxis = plot.getDomainAxis();
        domainAxis.setAxisLineVisible(true);
        domainAxis.setTickLabelsVisible(true);
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setAxisLineVisible(true);
        rangeAxis.setTickLabelsVisible(true);
        XYItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesShape(0, new Ellipse2D.Double(-5, -5, 10, 10));
        renderer.setSeriesPaint(0, Color.BLUE);
        LegendTitle legend = chart.getLegend();
        legend.setBackgroundPaint(new Color(0, 0, 0, 0));

        return chart;
    }
   /* @FXML
    public void naviguerVersHome(ActionEvent event) {
        try{
            Parent root= FXMLLoader.load(getClass().getResource("/Client/Gestion Consommation/HomeGestionAction.fxml"));
            home.getScene().setRoot(root);
        }
        catch (IOException ex){
            System.err.println("Error loading FXML document: " + ex);
            ex.printStackTrace();
        }
    }
    public void addHoverAnimation(Label label) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), label);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);

        label.setOnMouseEntered(event -> {
            scaleTransition.playFromStart();
        });

        label.setOnMouseExited(event -> {
            scaleTransition.stop();
            scaleTransition.setRate(-1);
            scaleTransition.play();
        });
    }*/
}
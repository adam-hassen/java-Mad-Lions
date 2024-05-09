package org.example.controller;
import EDU.userjava1.controllers.Login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.entity.Action;
import org.example.entity.ActionLocation;
import org.example.entity.TypeName;
import org.example.service.ActionService;
import org.example.service.TypeNameService;
import org.jfree.chart.*;
import org.jfree.chart.axis.SymbolAxis;
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
import com.google.gson.Gson;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
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
import org.jfree.data.json.impl.JSONObject;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.DefaultXYZDataset;

import javax.mail.MessagingException;
import java.awt.*;
import EDU.userjava1.entities.User1;
import EDU.userjava1.interfaces.MyListener;
import EDU.userjava1.interfaces.MyListener1;
import EDU.userjava1.services.UserServices;
public class SuivreConsoController {
    @FXML
    public Button Home;
    private ActionService query;
    private TypeNameService query2;
    @FXML
    private VBox vboxside;
    @FXML
    private StackPane  chartPanel;
    @FXML
    private HBox challSpace;
    @FXML
    private StackPane  scatterPanel;
    @FXML
    private Label firstcharterror;
    @FXML
    private Label scattercharterror;
    @FXML
    private WebView webView;
    @FXML
    public void initialize() throws IOException {
        query2 = new TypeNameService();
        query = new ActionService();
        showChallenges();
        //first chart
        ActionService.ChartData chartData = query.firstChart(Login.v.getId());
        List<Double> data2 = chartData.getData();
        List<String> labels2 = chartData.getLabels();
        if ((data2.size() != 0) && (labels2.size() != 0)){
            DefaultPieDataset dataset = new DefaultPieDataset();
            for (int i = 0; i < data2.size(); i++) {
                dataset.setValue(labels2.get(i), data2.get(i));
            }
            firstcharterror.setVisible(false);
            JFreeChart chart = createStyledPieChart(dataset);
            ChartViewer chartViewer = new ChartViewer(chart);
            chartPanel.getChildren().add(chartViewer);
        }
        else {
            firstcharterror.setVisible(true);
        }
        //end first chart
        // Scatter chart
        ActionService.ChartData chartData2 = query.scatterchart(Login.v.getId());
        List<Double> data3 = chartData2.getData();
        List<String> labels3 = chartData2.getLabels();

        if (data3.size() != 0 && labels3.size() != 0) {
            scattercharterror.setVisible(false);
            DefaultXYZDataset dataset = new DefaultXYZDataset();
            double[][] data = new double[3][data3.size()];
            for (int i = 0; i < data3.size(); i++) {
                data[0][i] = i;
                data[1][i] = data3.get(i);
                data[2][i] = 0.5;
            }
            dataset.addSeries("Niveau de Danger", data);
            XYBubbleRenderer renderer = new XYBubbleRenderer() {
                @Override
                public Paint getSeriesPaint(int series) {
                    return Color.RED;
                }
            };
            JFreeChart chart = ChartFactory.createBubbleChart(
                    null, "Dates", "Niveau De Danger", dataset, PlotOrientation.VERTICAL, true, true, false);
            XYPlot plot = chart.getXYPlot();
            plot.setRenderer(renderer);
            SymbolAxis xAxis = new SymbolAxis("Date", labels3.toArray(new String[0]));
            xAxis.setTickLabelFont(new Font("Arial", Font.BOLD, 10));
            xAxis.setTickLabelPaint(Color.BLACK);
            plot.setDomainAxis(xAxis);
            double aspectRatio = (double) plot.getDomainAxis().getRange().getLength()
                    / (double) plot.getRangeAxis().getRange().getLength();
            double bubbleSize = 0.5;
            double adjustedBubbleSize = bubbleSize * aspectRatio;

            chart.setBackgroundPaint(new Color(0,0,0,0));
            LegendTitle legend = chart.getLegend();
            legend.setBackgroundPaint(new Color(0, 0, 0, 0));
            plot.setBackgroundPaint(new Color(0,0,0,0));
            renderer.setSeriesShape(0, new Ellipse2D.Double(-adjustedBubbleSize / 2, -adjustedBubbleSize / 2, adjustedBubbleSize, adjustedBubbleSize));

            ChartViewer chartViewer = new ChartViewer(chart);
            scatterPanel.getChildren().add(chartViewer);
        } else {
            scattercharterror.setVisible(true);
        }
        //end scatter
        // leaftlet js
        WebEngine webEngine = webView.getEngine();
        String htmlFilePath = getClass().getResource("/Client/Gestion Consommation/Java.html").toString();
        webView.getEngine().load(htmlFilePath);
        webEngine.documentProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                //webView.getEngine().load(htmlFilePath);
                List<Action> dataList = query.afficherActions(Login.v.getId());
                List<ActionLocation> actionLocs = new ArrayList<>();
                for (Action action : dataList){
                    if (action.getLocation_id().getNom() != null ) {
                        actionLocs.add(action.getLocation_id());
                    }
                }
                Gson gson = new Gson();
                String jsonData = gson.toJson(actionLocs);
                String jsonEscapedData = jsonData.replace("'", "\\'");
                //System.out.println("JSON data from Java: " + jsonEscapedData);
                //System.out.println("Pure Java: " + actionLocs);
                String script = "var data = JSON.parse('" + jsonEscapedData + "'); processData(data);";
                webEngine.executeScript(script);
            }
        });

        webEngine.setOnError(event -> {
            System.out.println("WebView Error: " + event.getMessage());
        });
        //end leaflet api

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
   public void showChallenges() {
       try {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Gestion Consommation/ChallContainers.fxml"));
           Parent root = loader.load();
           ChallContainers challContainers = loader.getController();
           challSpace.getChildren().clear();
           challContainers.showChallenges(challSpace);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
    public void handleHome(ActionEvent event){
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
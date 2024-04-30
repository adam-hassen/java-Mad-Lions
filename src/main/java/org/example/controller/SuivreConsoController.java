package org.example.controller;
import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.example.entity.Action;
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

import javax.mail.MessagingException;
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
    private Label scattercharterror;
    @FXML
    public void initialize() {
        query2 = new TypeNameService();
        query = new ActionService();
        //email
        try {
            query.sendEmail("youssefbenarous@gmail.com", "Alerte : Dépassement du seuil de danger pour l'environnement", "Cher/Chère [Nom du destinataire],\n" +
                    "\n" +
                    "Nos systèmes de surveillance ont détecté une augmentation significative des indicateurs environnementaux, indiquant un dépassement du seuil de danger.\n" +
                    "\n" +
                    "Nous vous exhortons à agir immédiatement pour faire face à cette situation <b>cruciale</b>. Il est impératif que nous trouvions ensemble des solutions viables afin d'atténuer les défis environnementaux auxquels nous sommes confrontés.\n" +
                    "\n" +
                    "Merci pour votre attention à cette demande. Nous attendons votre réponse et votre collaboration.\n" +
                    "\n" +
                    "Cordialement,\n" +
                    "Votre nom");
            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            System.out.println("Failed to send email: " + e.getMessage());
        }
        //end emaim
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
        // Scatter chart
        ActionService.ChartData chartData2 = query.scatterchart(1);
        List<Double> data3 = chartData2.getData();
        List<String> labels3 = chartData2.getLabels();
        if ((data2!=null) && (labels2!=null)) {
            scattercharterror.setVisible(false);
            DefaultXYDataset dataset = new DefaultXYDataset();
            double[][] data = new double[2][data3.size()];
            for (int i = 0; i < data3.size(); i++) {
                data[0][i] = i;
                data[1][i] = data3.get(i);
            }
            dataset.addSeries("Niveau de Danger", data);
            JFreeChart chart = createStyledScatterPlot(dataset);
            XYPlot plot = chart.getXYPlot();
            SymbolAxis xAxis = new SymbolAxis("Date", labels3.toArray(new String[0]));
            xAxis.setTickLabelFont(new Font("Arial", Font.BOLD, 10));
            xAxis.setTickLabelPaint(Color.BLACK);
            plot.setDomainAxis(xAxis);
            ChartViewer chartViewer = new ChartViewer(chart);
            scatterPanel.getChildren().add(chartViewer);
        }
        else{
            scattercharterror.setVisible(true);
        }
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
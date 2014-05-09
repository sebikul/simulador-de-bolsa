package poo.proyecto.helpers;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import poo.proyecto.modelos.Titulo;

import java.awt.*;
import java.util.Collection;

public class GraficarTitulo extends Thread {

    private final Titulo titulo;
    private final XYSeriesCollection dataset = new XYSeriesCollection();
    private ChartPanel chartPanel;

    public GraficarTitulo(final Titulo titulo) {
        this.titulo = titulo;
        createDataset(titulo);

    }

    public XYSeriesCollection getDataset() {
        return dataset;
    }

    public void run() {
        final JFreeChart chart = createChart(dataset, titulo);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

    }

    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    private XYDataset createDataset(Titulo titulo) {

        final XYSeries series1 = new XYSeries("Precio");

        int i = 0;

        synchronized (titulo.getHistorico()) {
            for (double p : titulo.getHistorico().get()) {
                series1.add(i++, p);
            }
        }

        dataset.addSeries(series1);

        return dataset;

    }

    public void addToSeries(Collection<Double> col) {

        dataset.getSeries().addAll(col);
    }

    private JFreeChart createChart(final XYDataset dataset, Titulo titulo) {

        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Grafico de precios: " + titulo.getSimbolo(), "Iteracion", "Valor",
                dataset, PlotOrientation.VERTICAL, true, true, false);

        chart.setBackgroundPaint(Color.white);

        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);

        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        plot.setRenderer(renderer);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return chart;

    }

}
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

public class GraficarTitulo {

    private ChartPanel chartPanel;

    public GraficarTitulo(final Titulo titulo) {

        final XYDataset dataset = createDataset(titulo);
        final JFreeChart chart = createChart(dataset, titulo);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

    }

    public static ChartPanel graficar(final Titulo titulo) {

        final GraficarTitulo demo = new GraficarTitulo(titulo);

        return demo.getChartPanel();

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


        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);

        return dataset;

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
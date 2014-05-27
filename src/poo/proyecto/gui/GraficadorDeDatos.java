package poo.proyecto.gui;

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
import poo.proyecto.modelos.Ciclo;
import poo.proyecto.modelos.HistoricStore;

import java.awt.*;

/**
 * Esta clase representa un grafico con datos historicos.
 * <p/>
 * Corre en un hilo separado para que el evento repaint() no se ejecute en serie
 * con la ventana principal. Esto permite un graficado fluido
 * y una simulacion más rapida.
 */
class GraficadorDeDatos<T extends HistoricStore> extends Thread {

    final XYSeries series1 = new XYSeries("Precio");
    private final T obj;
    private final XYSeriesCollection dataset = new XYSeriesCollection();
    private double last = 0;
    private ChartPanel chartPanel;
    private boolean isActive = false;

    public GraficadorDeDatos(T obj) {
        this.obj = obj;
        dataset.addSeries(series1);

        if (!obj.getHistorico().isEmpty()) {
            int i = 0;

            for (Ciclo ciclo : obj.getHistorico().getRawData()) {

                series1.add(i, ciclo.getValor());
                i++;

            }
        }

    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive() {
        this.isActive = true;
    }

    public void setInactive() {
        this.isActive = false;
    }

    public void run() {
        final JFreeChart chart = createChart(dataset, obj);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 270));

        chartPanel.repaint();

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    /**
     * FIXME
     * <p/>
     * Agrega un nuevo dato al modelo del graficador.
     * <p/>
     * Idealmente deberia pedir los datos del objeto que esta graficando, ya que se esta
     * almacenando una copia de los datos. Sin embargo, acceder a estos datos
     * implica sincronizar el hilo del graficador con el del simulador
     * que contiene el dato que desea acceder. Esto implica agregar complejidad y errores
     * innecesarios.
     *
     * @param ciclo
     * @param value
     */
    public void addToSeries(int ciclo, double value) {

        if (last == 0) {
            last = value;
        }
        series1.add(ciclo, (last + value) / 2);

        if (isActive) {
            chartPanel.repaint();
        }

    }

    public void repaintChart() {
        chartPanel.repaint();
    }

    private JFreeChart createChart(final XYDataset dataset, T obj) {

        final JFreeChart chart = ChartFactory.createXYLineChart("Grafico: "
                        + obj.toString(), "Iteracion", "Valor", dataset,
                PlotOrientation.VERTICAL, true, true, false
        );

        chart.setBackgroundPaint(Color.white);

        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);

        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(renderer);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return chart;

    }

}

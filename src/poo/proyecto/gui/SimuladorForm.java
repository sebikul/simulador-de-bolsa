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
import poo.proyecto.mercados.Merval;
import poo.proyecto.modelos.AgenteDeBolsa;
import poo.proyecto.modelos.Inversor;
import poo.proyecto.modelos.Titulo;
import poo.proyecto.simulador.GuiSimulador;
import poo.proyecto.simulador.SimuladorHook;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;


public class SimuladorForm {
    GuiSimulador simulador;
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JPanel panelTitulos;
    private JPanel panelInversores;
    private JList listTitulos;
    private JList listInversores;
    private JLabel labelEstado;
    private JButton detenerButton;
    private JButton iniciarBoton;
    private JLabel labelCiclo;
    private JSplitPane splitPanelTitulos;
    private DefaultListModel modelTitulos = new DefaultListModel();
    private DefaultListModel modelInversores = new DefaultListModel();
    private CycleThread cycleThread = new CycleThread();


    private HashMap<Titulo, GraficadorDeTitulo> graficadores = new HashMap<Titulo, GraficadorDeTitulo>();

    public SimuladorForm() {

        cargarSimulador();

        listTitulos.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                titulosSelectionChanged();
            }
        });
        listInversores.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                inversoresSelectionChanged();
            }
        });
        detenerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                detenerButtonClicked();
            }
        });
        iniciarBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                iniciarButtonClicked();
            }
        });


    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("SimuladorForm");
        frame.setContentPane(new SimuladorForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setTitle("Simulador");

    }

    private void cargarSimulador() {

        simulador = new GuiSimulador(new SimuladorHook() {

            @Override
            public void preIteracion() {
                preIteracionNotification();
            }

            @Override
            public void postIteracion() {
                postIteracionNotification();
            }
        });

        pedirDatos();


    }

    private void pedirDatos() {
        ParametrosForm dialog = new ParametrosForm();
        dialog.setTitle("Ingrese los parametros de la simulacion.");
        dialog.pack();
        dialog.setVisible(true);

        if (dialog.getAgentes() == 0 || dialog.getInversores() == 0) {
            return;
        }

        simulador.generarAgentes(dialog.getAgentes());
        try {
            simulador.generarInversores(dialog.getInversores());
            simulador.setMercado(new Merval());
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Titulo titulo : simulador.getMercado().getTitulos().values()) {
            modelTitulos.addElement(titulo);

            GraficadorDeTitulo tmp = new GraficadorDeTitulo(titulo);
            tmp.start();
            graficadores.put(titulo, tmp);
        }

        for (AgenteDeBolsa agente : simulador.getAgentes()) {

            for (Inversor inversor : agente.getClientes().keySet()) {
                modelInversores.addElement(inversor);
            }

        }

        listTitulos.setModel(modelTitulos);
        listInversores.setModel(modelInversores);
    }

    private void titulosSelectionChanged() {

        for (GraficadorDeTitulo graficador : graficadores.values()) {
            graficador.setInactive();
        }


        Titulo titulo = (Titulo) listTitulos.getSelectedValue();

        GraficadorDeTitulo graficador = graficadores.get(titulo);

        splitPanelTitulos.setRightComponent(graficador.getChartPanel());
        graficador.setActive();

    }

    private void inversoresSelectionChanged() {

    }

    private void preIteracionNotification() {


    }

    private void postIteracionNotification() {

        for (Titulo titulo : simulador.getMercado().getTitulos().values()) {

            graficadores.get(titulo).addToSeries((int) simulador.getCiclo(), titulo.getValor());
        }


    }

    public void detenerButtonClicked() {

        detenerButton.setEnabled(false);
        iniciarBoton.setEnabled(true);

        cycleThread.setRunning(false);
        simulador.detenerSimulador();

    }

    public void iniciarButtonClicked() {

        if (!simulador.isReady()) {
            pedirDatos();
            if (!simulador.isReady()) {
                return;
            }
        }

        if (!simulador.hasStarted()) {
            simulador.start();
            cycleThread.start();
        } else {
            simulador.resumirSimulador();
        }

        detenerButton.setEnabled(true);
        iniciarBoton.setEnabled(false);

        cycleThread.setRunning(true);


    }


    private class CycleThread extends Thread {

        private boolean running = true;

        @Override
        public void run() {


            while (true) {
                if (running) {
                    labelCiclo.setText("" + simulador.getCiclo());
                    labelEstado.setText("Ejecutando...");

                } else {
                    labelEstado.setText("Detenido.");
                }

                listTitulos.repaint();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        public void setRunning(boolean v) {
            running = v;
        }
    }


    class GraficadorDeTitulo extends Thread {

        final XYSeries series1 = new XYSeries("Precio");
        private final Titulo titulo;
        private final XYSeriesCollection dataset = new XYSeriesCollection();
        private ChartPanel chartPanel;
        private boolean isActive = false;

        public GraficadorDeTitulo(Titulo titulo) {
            this.titulo = titulo;
            dataset.addSeries(series1);
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
            final JFreeChart chart = createChart(dataset, titulo);
            chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

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


        public void addToSeries(int ciclo, double value) {

            series1.add(ciclo, value);

            if (isActive) {
                chartPanel.repaint();
            }

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
            renderer.setSeriesLinesVisible(0, true);
            renderer.setSeriesShapesVisible(0, false);
            plot.setRenderer(renderer);

            final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

            return chart;

        }

    }

}
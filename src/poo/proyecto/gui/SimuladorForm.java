package poo.proyecto.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
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
    private GuiSimulador simulador;
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1 = new JTabbedPane();
        panel1.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(800, 800), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Simulador", panel2);
        final JLabel label1 = new JLabel();
        label1.setText("Estado del simulador");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelEstado = new JLabel();
        labelEstado.setText("Detenido.");
        panel2.add(labelEstado, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        iniciarBoton = new JButton();
        iniciarBoton.setText("Iniciar");
        panel3.add(iniciarBoton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        detenerButton = new JButton();
        detenerButton.setEnabled(false);
        detenerButton.setText("Detener");
        panel3.add(detenerButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Ciclo");
        panel2.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelCiclo = new JLabel();
        labelCiclo.setText("0");
        panel2.add(labelCiclo, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelTitulos = new JPanel();
        panelTitulos.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelTitulos.setToolTipText("");
        tabbedPane1.addTab("Titulos", panelTitulos);
        splitPanelTitulos = new JSplitPane();
        splitPanelTitulos.setDividerSize(100);
        splitPanelTitulos.setResizeWeight(0.5);
        panelTitulos.add(splitPanelTitulos, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(800, 800), null, 0, false));
        listTitulos = new JList();
        listTitulos.setSelectionMode(0);
        splitPanelTitulos.setLeftComponent(listTitulos);
        final Spacer spacer3 = new Spacer();
        splitPanelTitulos.setRightComponent(spacer3);
        panelInversores = new JPanel();
        panelInversores.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Inversores", panelInversores);
        final JSplitPane splitPane1 = new JSplitPane();
        panelInversores.add(splitPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy(31);
        splitPane1.setLeftComponent(scrollPane1);
        listInversores = new JList();
        scrollPane1.setViewportView(listInversores);
        final Spacer spacer4 = new Spacer();
        splitPane1.setRightComponent(spacer4);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
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


    private class GraficadorDeTitulo extends Thread {

        private final XYSeries series1 = new XYSeries("Precio");
        private final Titulo titulo;
        private final XYSeriesCollection dataset = new XYSeriesCollection();
        private ChartPanel chartPanel = null;
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
            chartPanel.setPreferredSize(new Dimension(500, 270));

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
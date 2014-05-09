package poo.proyecto.gui;

import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import poo.proyecto.helpers.GraficarTitulo;
import poo.proyecto.mercados.Merval;
import poo.proyecto.modelos.AgenteDeBolsa;
import poo.proyecto.modelos.Inversor;
import poo.proyecto.modelos.Titulo;
import poo.proyecto.simulador.GuiSimulador;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;


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

    private GraficarTitulo graficadorActual;

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
    }

    private void cargarSimulador() {

        simulador = new GuiSimulador();

        ParametrosForm dialog = new ParametrosForm();
        dialog.pack();
        dialog.setVisible(true);

        simulador.generarAgentes(dialog.getAgentes());
        try {
            simulador.generarInversores(dialog.getInversores());
            simulador.setMercado(new Merval());
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Titulo titulo : simulador.getMercado().getTitulos().values()) {
            modelTitulos.addElement(titulo);
        }

        for (AgenteDeBolsa agente : simulador.getAgentes()) {

            for (Inversor inversor : agente.getClientes().keySet()) {
                modelInversores.addElement(inversor);
            }

        }

        listTitulos.setModel(modelTitulos);
        listInversores.setModel(modelInversores);
    }

    public void titulosSelectionChanged() {

        graficadorActual = new GraficarTitulo((Titulo) listTitulos.getSelectedValue());

        graficadorActual.start();

        try {
            graficadorActual.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        splitPanelTitulos.setRightComponent(graficadorActual.getChartPanel());

    }

    public void inversoresSelectionChanged() {

    }

    public void detenerButtonClicked() {

        detenerButton.setEnabled(false);
        iniciarBoton.setEnabled(true);

        cycleThread.setRunning(false);
        simulador.detenerSimulador();

    }

    public void iniciarButtonClicked() {

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

            int lastCycle = 0;

            while (true) {
                if (running) {
                    labelCiclo.setText("" + simulador.getCiclo());
                    labelEstado.setText("Ejecutando...");

                    Titulo titulo = (Titulo) listTitulos.getSelectedValue();

                    if (titulo != null && graficadorActual != null) {

                        System.out.println("Ciclo: " + lastCycle + "-->" + simulador.getCiclo());
                        Collection<Double> paraAgregar = titulo.getHistorico().getFromCycle(lastCycle);

                        XYSeries series = (XYSeries) graficadorActual.getDataset().getSeries();

                        int i = (int) series.getMaxX();

                        for (double val : paraAgregar) {
                            series.add(i++, val);
                        }

                    }



                } else {
                    labelEstado.setText("Detenido.");
                }

                listTitulos.repaint();
                lastCycle = (int) simulador.getCiclo();

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

}
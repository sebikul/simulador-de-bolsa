package poo.proyecto.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import poo.proyecto.Algoritmos.PriceAlgorithm;
import poo.proyecto.mercados.Merval;
import poo.proyecto.modelos.AgenteDeBolsa;
import poo.proyecto.modelos.Inversor;
import poo.proyecto.modelos.ResultadosSimulacion;
import poo.proyecto.modelos.Titulo;
import poo.proyecto.simulador.GuiSimulador;
import poo.proyecto.simulador.Simulador;
import poo.proyecto.simulador.SimuladorHook;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class SimuladorForm extends JFrame {
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
    private JSplitPane splitPanelInversores;
    private JButton guardarButton;
    private DefaultListModel modelTitulos = new DefaultListModel();
    private DefaultListModel modelInversores = new DefaultListModel();
    private CycleThread cycleThread = new CycleThread();

    private HashMap<Titulo, GraficadorDeDatos<Titulo>> graficadoresDeTitulos = new HashMap<Titulo, GraficadorDeDatos<Titulo>>();
    private HashMap<Inversor, GraficadorDeDatos<Inversor>> graficadoresDeInversores = new HashMap<Inversor, GraficadorDeDatos<Inversor>>();

    public SimuladorForm(ResultadosSimulacion resultadosSimulacion) {

        cargarResultados(resultadosSimulacion);
        iniciarBoton.setEnabled(false);

        labelCiclo.setText("" + resultadosSimulacion.getCiclos());

        setActionListeners();
    }


    public SimuladorForm() {

        cargarSimulador();

        setActionListeners();

    }

    public static void main() {
        JFrame frame = new JFrame("SimuladorForm");

        SimuladorForm simuladorForm = new SimuladorForm();

        frame.setContentPane(simuladorForm.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setTitle("Simulador");

    }

    public static void main(ResultadosSimulacion resultadosSimulacion) {
        JFrame frame = new JFrame("SimuladorForm");

        SimuladorForm simuladorForm = new SimuladorForm(resultadosSimulacion);

        frame.setContentPane(simuladorForm.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setTitle("Simulador");


    }

    private void setActionListeners() {


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

        guardarButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                guardarButtonClicked();
            }
        });
    }

    private void guardarButtonClicked() {

        JFileChooser fc = new JFileChooser();

        int returnVal = fc.showSaveDialog(guardarButton);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            String filePath = file.getAbsolutePath();
            if (!filePath.endsWith(Simulador.FILE_TYPE)) {
                file = new File(filePath + Simulador.FILE_TYPE);
            }

            try {

                FileOutputStream fileOutputStream = new FileOutputStream(file);

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                objectOutputStream.writeObject(simulador.resultados());

                JOptionPane.showMessageDialog(this, "Simulacion guardada");

            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            System.out.println("cancelado");
        }


    }

    public void cargarResultados(ResultadosSimulacion resultadosSimulacion) {

        for (Titulo titulo : resultadosSimulacion.getTitulos()) {
            modelTitulos.addElement(titulo);

            GraficadorDeDatos<Titulo> tmp = new GraficadorDeDatos<Titulo>(
                    titulo);
            tmp.start();
            graficadoresDeTitulos.put(titulo, tmp);
        }

        for (Inversor inversor : resultadosSimulacion.getInversores()) {
            modelInversores.addElement(inversor);

            GraficadorDeDatos<Inversor> tmp = new GraficadorDeDatos<Inversor>(
                    inversor);
            tmp.start();
            graficadoresDeInversores.put(inversor, tmp);
        }

        listTitulos.setModel(modelTitulos);
        listInversores.setModel(modelInversores);

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
        ParametrosDialog dialog = new ParametrosDialog();
        dialog.setTitle("Ingrese los parametros de la simulacion.");
        dialog.pack();
        dialog.setVisible(true);

        if (dialog.getAgentes() == 0 || dialog.getInversores() == 0
                || dialog.getCiclos() == 0) {
            return;
        }

        simulador.setMaxCiclos(dialog.getCiclos());

        simulador.generarAgentes(dialog.getAgentes());
        try {
            simulador.generarInversores(dialog.getInversores());
            simulador.setMercado(new Merval(PriceAlgorithm.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Titulo titulo : simulador.getMercado().getTitulos().values()) {
            modelTitulos.addElement(titulo);

            GraficadorDeDatos<Titulo> tmp = new GraficadorDeDatos<Titulo>(
                    titulo);
            tmp.start();
            graficadoresDeTitulos.put(titulo, tmp);
        }

        for (AgenteDeBolsa agente : simulador.getAgentes()) {

            for (Inversor inversor : agente.getClientes().keySet()) {
                modelInversores.addElement(inversor);

                GraficadorDeDatos<Inversor> tmp = new GraficadorDeDatos<Inversor>(
                        inversor);
                tmp.start();
                graficadoresDeInversores.put(inversor, tmp);
            }

        }

        listTitulos.setModel(modelTitulos);
        listInversores.setModel(modelInversores);
    }

    private void titulosSelectionChanged() {

        for (GraficadorDeDatos<Titulo> graficador : graficadoresDeTitulos
                .values()) {
            graficador.setInactive();
        }

        Titulo titulo = (Titulo) listTitulos.getSelectedValue();

        GraficadorDeDatos<Titulo> graficador = graficadoresDeTitulos
                .get(titulo);

        splitPanelTitulos.setRightComponent(graficador.getChartPanel());
        graficador.setActive();

    }

    private void inversoresSelectionChanged() {

        for (GraficadorDeDatos<Inversor> graficador : graficadoresDeInversores
                .values()) {
            graficador.setInactive();
        }

        Inversor inversor = (Inversor) listInversores.getSelectedValue();

        GraficadorDeDatos<Inversor> graficador = graficadoresDeInversores
                .get(inversor);

        splitPanelInversores.setRightComponent(graficador.getChartPanel());
        graficador.setActive();

    }

    private void preIteracionNotification() {

    }

    private void postIteracionNotification() {

        for (Titulo titulo : simulador.getMercado().getTitulos().values()) {

            graficadoresDeTitulos.get(titulo).addToSeries(simulador.getCiclo(),
                    titulo.getValor());
        }

        for (AgenteDeBolsa agente : simulador.getAgentes()) {

            for (Inversor inversor : agente.getClientes().keySet()) {
                graficadoresDeInversores.get(inversor).addToSeries(
                        simulador.getCiclo(), inversor.getPatrimonio());
            }

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
        panel3.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        iniciarBoton = new JButton();
        iniciarBoton.setText("Iniciar");
        panel3.add(iniciarBoton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        detenerButton = new JButton();
        detenerButton.setEnabled(false);
        detenerButton.setText("Detener");
        panel3.add(detenerButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        guardarButton = new JButton();
        guardarButton.setEnabled(false);
        guardarButton.setText("Guardar");
        panel3.add(guardarButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        splitPanelTitulos.setResizeWeight(0.2);
        panelTitulos.add(splitPanelTitulos, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(800, 800), null, 0, false));
        listTitulos = new JList();
        listTitulos.setSelectionMode(0);
        splitPanelTitulos.setLeftComponent(listTitulos);
        final Spacer spacer3 = new Spacer();
        splitPanelTitulos.setRightComponent(spacer3);
        panelInversores = new JPanel();
        panelInversores.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Inversores", panelInversores);
        splitPanelInversores = new JSplitPane();
        splitPanelInversores.setResizeWeight(0.2);
        panelInversores.add(splitPanelInversores, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy(31);
        splitPanelInversores.setLeftComponent(scrollPane1);
        listInversores = new JList();
        scrollPane1.setViewportView(listInversores);
        final Spacer spacer4 = new Spacer();
        splitPanelInversores.setRightComponent(spacer4);
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

                labelCiclo.setText("" + simulador.getCiclo() + " / "
                        + simulador.getMaxCiclos());

                labelEstado.setText(running ? "Ejecutando..." : "Detenido.");


                if (simulador.hasFinished())
                    guardarButton.setEnabled(true);

                listTitulos.repaint();
                listInversores.repaint();

                try {
                    Thread.sleep(100);
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
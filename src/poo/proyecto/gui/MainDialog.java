package poo.proyecto.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import poo.proyecto.modelos.ResultadosSimulacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MainDialog extends JDialog {
    private JPanel contentPane;
    private JButton btnNew;
    private JButton btnLoad;

    public MainDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btnNew);

        btnNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onNew();
            }
        });

        btnLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onLoad();
            }
        });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }

    public static void main(String[] args) {
        MainDialog dialog = new MainDialog();
        dialog.pack();
        dialog.setVisible(true);
    }

    private void onNew() {
        SimuladorForm.main();
        dispose();
    }

    private void onLoad() {

        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

//        fc.addChoosableFileFilter(new FileFilter() {
//            @Override
//            public boolean accept(File file) {
//
//                return true;
//
////                if (file == null) {
////                    return false;
////                }
////
////                return getExtension(file).equals(Simulador.FILE_TYPE);
//            }
//
//            @Override
//            public String getDescription() {
//                return "Resultados de simulacion";
//            }
//
//            public String getExtension(File f) {
//                String ext = null;
//                String s = f.getName();
//                int i = s.lastIndexOf('.');
//
//                if (i > 0 && i < s.length() - 1) {
//                    ext = s.substring(i + 1).toLowerCase();
//                }
//                return ext;
//            }
//        });


        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            if (!file.exists()) {
                return;
            }

            try {

                FileInputStream fileInputStream = new FileInputStream(file);

                InputStream buffer = new BufferedInputStream(fileInputStream);


                ObjectInputStream objectInputStream = new ObjectInputStream(buffer);

                ResultadosSimulacion resultadosSimulacion = (ResultadosSimulacion) objectInputStream.readObject();

                SimuladorForm.main(resultadosSimulacion);


            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            return;
        }

        dispose();
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
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnNew = new JButton();
        btnNew.setText("Nueva simulacion");
        panel2.add(btnNew, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnLoad = new JButton();
        btnLoad.setText("Cargar Simulacion");
        panel2.add(btnLoad, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
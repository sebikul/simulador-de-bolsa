package poo.proyecto.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ParametrosForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextField textInversores;
    private JTextField textAgentes;

    private int agentes = 0;
    private int inversores = 0;

    public ParametrosForm() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

    }


    private void onOK() {

        try {
            agentes = Integer.parseInt(textAgentes.getText());
            inversores = Integer.parseInt(textInversores.getText());

            if (agentes <= 0 || inversores <= 0) {
                throw new NumberFormatException();
            }


        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Los valores ingresados deben ser enteros mayores que 0",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }


        dispose();
    }


    public int getInversores() {
        return inversores;
    }

    public int getAgentes() {
        return agentes;
    }
}

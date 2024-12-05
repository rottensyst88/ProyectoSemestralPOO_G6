package GUI;

import controlador.ControladorEmpresas;
import excepciones.SVPException;
import utilidades.Rut;
import vista.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class GUICreaEmpresa extends JDialog {

    private JPanel panel1;
    private JTextField RUTtextField;
    private JTextField URLtextField;
    private JTextField NombretextField;
    private JButton cancelButton;
    private JButton OKButton;

    Scanner sc = new Scanner(System.in);


    public GUICreaEmpresa() {
        setContentPane(panel1);
        setModal(true);
        getRootPane().setDefaultButton(OKButton);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        OKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();

            }
        });
        RUTtextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (RUTtextField.getText().equals("11.111.111-1")) {
                    RUTtextField.setText("");
                    RUTtextField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (RUTtextField.getText().isEmpty()) {
                    RUTtextField.setText("11.111.111-1");
                    RUTtextField.setForeground(Color.LIGHT_GRAY);
                }
            }
        });


    }

    private void onOK() {
        try {

            String rut_st = RUTtextField.getText();
            String nombre = NombretextField.getText();
            String url = URLtextField.getText();


            if (nombre.isEmpty() || url.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un nombre y URL válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ControladorEmpresas.getInstance().createEmpresa(Rut.of(rut_st), nombre, url);
            //empresa creada
            JOptionPane.showMessageDialog(this, "Empresa guardada exitosamente", "Atención", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (SVPException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {

        dispose();
    }


}


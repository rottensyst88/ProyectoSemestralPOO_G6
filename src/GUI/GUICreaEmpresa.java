package GUI;

import controlador.ControladorEmpresas;
import excepciones.SistemaVentaPasajesException;
import utilidades.Rut;
import vista.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    }

    private void onOK() {
        try {

            String rut_st = RUTtextField.getText();
            String nombre = NombretextField.getText();
            String url = URLtextField.getText();

            ControladorEmpresas.getInstance().createEmpresa(Rut.of(rut_st), nombre, url);
            //empresa creada
            JOptionPane.showMessageDialog(this, "gracias por su atención", "Atención", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (SistemaVentaPasajesException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {

        dispose();
    }


}


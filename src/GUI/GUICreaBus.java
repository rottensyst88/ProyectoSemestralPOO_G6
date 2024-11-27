package GUI;

import controlador.*;
import excepciones.SistemaVentaPasajesException;
import utilidades.Rut;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;


public class GUICreaBus extends JDialog {
    private JPanel panel1;
    private JTextField NroAsientosTextField;
    private JTextField ModeloTextField;
    private JTextField MarcaTextField;
    private JTextField PatenteTextField;
    private JButton cancelButton;
    private JButton OKButton;
    private JComboBox NombreComboBox;
    private JComboBox RUTComboBox;

    Scanner sc = new Scanner(System.in);

    public GUICreaBus() {
        setContentPane(panel1);
        setModal(true);
        getRootPane().setDefaultButton(OKButton);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });


        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
    }

    public void onOK() {
        try {
            int nroAsientos = Integer.parseInt(NroAsientosTextField.getText().trim());
            String modelo = ModeloTextField.getText();
            String marca = MarcaTextField.getText();
            String patente = PatenteTextField.getText();

            String nombre = NombreComboBox.getSelectedItem().toString();
            Rut rut = Rut.of(RUTComboBox.getSelectedItem().toString().trim());

            ControladorEmpresas.getInstance().createBus(patente, marca, modelo, nroAsientos, rut);
            JOptionPane.showMessageDialog(this, "Damian weko", "¡¡¡¡¡¡¡ATENCION!!!!!!", JOptionPane.INFORMATION_MESSAGE);
        } catch (SistemaVentaPasajesException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "sexo gratis", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void onCancel() {
        dispose();
    }

}

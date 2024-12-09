package GUI;

import controlador.*;
import excepciones.SVPException;
import utilidades.Rut;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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

        try {
            cargarDatos();
        } catch (SVPException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        if (empresas != null && empresas.length > 0) {
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

            //Solución original de Diego
            NombreComboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cambiarRutAutomatico();
                }
            });

            RUTComboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cambiarNombreAutomatico();
                }
            });
            // Fin solución original de Diego

            PatenteTextField.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (PatenteTextField.getText().equals("AA.BB-12")) {
                        PatenteTextField.setText("");
                        PatenteTextField.setForeground(Color.BLACK);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (PatenteTextField.getText().isEmpty()) {
                        PatenteTextField.setText("AA.BB-12");
                        PatenteTextField.setForeground(Color.LIGHT_GRAY);
                    }
                }
            });

        }


    }

    public static void display() {
        String[][] empresas = ControladorEmpresas.getInstance().listEmpresas();

        if (empresas.length > 0) {
            GUICreaBus dialog = new GUICreaBus();
            dialog.setLocationRelativeTo(null);
            dialog.pack();
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "no hay empresas creadas", "Información", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public void onOK() {
        boolean verif = true;

        do {
            try {
                int nroAsientos = 1;

                nroAsientos = Integer.parseInt(NroAsientosTextField.getText().trim());
                String modelo = ModeloTextField.getText();
                String marca = MarcaTextField.getText();
                String patente = PatenteTextField.getText();
                String nombre = NombreComboBox.getSelectedItem().toString();
                Rut rut = Rut.of(RUTComboBox.getSelectedItem().toString().trim());

                if (nroAsientos < 0) {
                    JOptionPane.showMessageDialog(this, "Numero de asientos invalido", "ERROR", JOptionPane.ERROR_MESSAGE);
                    NroAsientosTextField.setText("");
                } else {
                    ControladorEmpresas.getInstance().createBus(patente, marca, modelo, nroAsientos, rut);
                    JOptionPane.showMessageDialog(this, "Bus guardado exitosamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                    verif = false;
                    dispose();
                }

            } catch (SVPException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                verif = false;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Datos invalidos", "Error", JOptionPane.ERROR_MESSAGE);
                NroAsientosTextField.setText("");
                verif = false;
            }
        } while (verif);

    }

    //Solución original de Diego
    private String[][] empresas = ControladorEmpresas.getInstance().listEmpresas();

    private void cambiarRutAutomatico() {
        String empresaSelec = (String) NombreComboBox.getSelectedItem();

        for (String[] empresa : empresas) {
            if (empresa[1].equals(empresaSelec)) {
                RUTComboBox.setSelectedItem(empresa[0]);
                break;
            }
        }
    }

    private void cambiarNombreAutomatico() {
        String rutSelec = (String) RUTComboBox.getSelectedItem();

        for (String[] empresa : empresas) {
            if (empresa[0].equals(rutSelec)) {
                NombreComboBox.setSelectedItem(empresa[1]);
                break;
            }
        }
    }

    private void cargarDatos() {

        for (String[] empresa : empresas) {
            NombreComboBox.addItem(empresa[1]);
            RUTComboBox.addItem(empresa[0]);
        }
    }
    //Fin solucion original de Diego

    public void onCancel() {
        dispose();
    }
    /*
    public static void display(){
        ControladorEmpresas ce = ControladorEmpresas.getInstance();
        String[][] lista = ce.listEmpresas();
                if(lista.length>0) {
                    GUICreaBus dialog = new GUICreaBus();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                    dialog.pack();

                }else{
                    JOptionPane.showMessageDialog(null, "No hay empresas", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
    } */
}

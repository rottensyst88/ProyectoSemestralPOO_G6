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
        try{
            cargarDatos();
        } catch (SistemaVentaPasajesException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            System.exit(0);
        }
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
            JOptionPane.showMessageDialog(this, "Bus guardado exitosamente", "Información", JOptionPane.INFORMATION_MESSAGE);
        } catch (SistemaVentaPasajesException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Datos invalidos", "Error", JOptionPane.ERROR_MESSAGE);
        }


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
    private void cargarDatos() throws  SistemaVentaPasajesException {

        if(empresas.length == 0){
            throw new SistemaVentaPasajesException("No existen empresas en el registro");
        }
        for (String[] empresa : empresas) {
            NombreComboBox.addItem(empresa[1]);
            RUTComboBox.addItem(empresa[0]);
        }
    }

    //Fin solucion original de Diego

    public void onCancel() {
        dispose();
    }

}

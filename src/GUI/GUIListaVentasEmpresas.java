package GUI;

import controlador.ControladorEmpresas;
import excepciones.SistemaVentaPasajesException;
import utilidades.Rut;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUIListaVentasEmpresas extends JDialog{
    private JPanel panel1;
    private JComboBox boxRut;
    private JTable tablaVentas;
    private JButton cancelButton;
    private JButton OKButton;
    private JComboBox boxNombreEmp;


    // EL CODIGO QUE ESTA COMENTADO ES EXLUSIVAMENTE PARA CUANDO SE ESTE INTERACCIONANDO CON
    // EL CONTROLADOR, POR AHORA ESTOY SOLO CON DATOS DE PRUEBA.

    private String[][] empresas = ControladorEmpresas.getInstance().listEmpresas();
    private String [] columnas= {"FECHA", "TIPO", "MONTO PAGADO", "TIPO PAGO"};




    public GUIListaVentasEmpresas() throws SistemaVentaPasajesException{
        cargarDatos();

        setContentPane(panel1);
        setLocationRelativeTo(null);
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
        boxNombreEmp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cambiarRutAutomatico();
            }
        });

        boxRut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cambiarNombreAutomatico();
            }
        });

    }

    private void onOK() throws SistemaVentaPasajesException {
        try {
            rellenarTabla();
        } catch (SistemaVentaPasajesException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }


    private void onCancel() {
        dispose();
    }

    private void cargarDatos() throws  SistemaVentaPasajesException {

        if(empresas.length == 0){
            throw new SistemaVentaPasajesException("No existen empresas en el registro");
        }
        for (String[] empresa : empresas) {
            boxNombreEmp.addItem(empresa[1]);
            boxRut.addItem(empresa[0]);
        }
    }



    private void cambiarRutAutomatico() {
        String empresaSelec = (String) boxNombreEmp.getSelectedItem();

        for (String[] empresa : empresas) {
            if (empresa[1].equals(empresaSelec)) {
                boxRut.setSelectedItem(empresa[0]);
                break;
            }
        }
    }
    private void cambiarNombreAutomatico() {
        String rutSelec = (String) boxRut.getSelectedItem();

        for (String[] empresa : empresas) {
            if (empresa[0].equals(rutSelec)) {
                boxNombreEmp.setSelectedItem(empresa[1]);
                break;
            }
        }
    }

    private void rellenarTabla() throws SistemaVentaPasajesException {
        String rutSeleccionado = (String) boxRut.getSelectedItem();
        String [][] ventasEmpresa = ControladorEmpresas.getInstance().listVentasEmpresa(Rut.of(rutSeleccionado));


        if (ventasEmpresa.length == 0) {
            throw new SistemaVentaPasajesException("La empresa seleccionada no tiene ventas");
        }

        tablaVentas.setModel(new DefaultTableModel(ventasEmpresa, columnas));
    }


    public static void display() {
        GUIListaVentasEmpresas dialog = new GUIListaVentasEmpresas();
        dialog.setSize(600, 700);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }



}

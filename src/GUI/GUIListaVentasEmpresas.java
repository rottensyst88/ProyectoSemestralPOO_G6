package GUI;

import excepciones.SistemaVentaPasajesException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

public class GUIListaVentasEmpresas extends JDialog{
    private JPanel panel1;
    private JComboBox boxRut;
    private JTable tablaVentas;
    private JButton cancelButton;
    private JButton OKButton;
    private JComboBox boxNombreEmp;


    // EL CODIGO QUE ESTA COMENTADO ES EXLUSIVAMENTE PARA CUANDO SE ESTE INTERACCIONANDO CON
    // EL CONTROLADOR, POR AHORA ESTOY SOLO CON DATOS DE PRUEBA.

    //private String[][] empresas = ControladorEmpresas.getInstance().listEmpresas();
    private String[][] empresas = {
            {"12345678-9", "Empresa A"},
            {"98765432-1", "Empresa B"},
            {"13579246-0", "Empresa C"},
            {"11.111.111-1", "PRUEBA"}
    };

    private String[][] empresas2 = new String[0][0];

    private String [] columnas= {"FECHA", "TIPO", "MONTO PAGADO", "TIPO PAGO"};

    private String[][] ventasPorEmpresa = {
            // Empresa A (RUT: 12345678-9)
            {"12345678-9", "12/11/2024", "Factura", "100000", "Tarjeta"},
            {"12345678-9", "13/11/2024", "Boleta", "150000", "Efectivo"},
            {"12345678-9", "14/11/2024", "Factura", "200000", "Tarjeta"},
            {"12345678-9", "15/11/2024", "Boleta", "300000", "Tarjeta"},
            {"12345678-9", "16/11/2024", "Factura", "50000", "Efectivo"},
            {"12345678-9", "17/11/2024", "Boleta", "250000", "Efectivo"},
            {"12345678-9", "18/11/2024", "Factura", "120000", "Efectivo"},

            // Empresa B (RUT: 98765432-1)
            {"98765432-1", "14/11/2024", "Factura", "200000", "Efectivo"},
            {"98765432-1", "15/11/2024", "Boleta", "250000", "Tarjeta"},
            {"98765432-1", "16/11/2024", "Factura", "300000", "Efectivo"},
            {"98765432-1", "17/11/2024", "Boleta", "400000", "Tarjeta"},
            {"98765432-1", "18/11/2024", "Factura", "60000", "Efectivo"},
            {"98765432-1", "19/11/2024", "Boleta", "120000", "Tarjeta"},
            {"98765432-1", "20/11/2024", "Factura", "450000", "Efectivo"},

            // Empresa C (RUT: 13579246-0)
            {"13579246-0", "16/11/2024", "Factura", "300000", "Efectivo"},
            {"13579246-0", "17/11/2024", "Boleta", "400000", "Tarjeta"},
            {"13579246-0", "18/11/2024", "Factura", "50000", "Efectivo"},
            {"13579246-0", "19/11/2024", "Boleta", "150000", "Tarjeta"},
            {"13579246-0", "20/11/2024", "Factura", "200000", "Efectivo"},
            {"13579246-0", "21/11/2024", "Boleta", "250000", "Tarjeta"},
            {"13579246-0", "22/11/2024", "Factura", "120000", "Efectivo"},

            {"11.111.111-1", "0", "0", "0", "0"}
    };




    public GUIListaVentasEmpresas() throws SistemaVentaPasajesException{
        try{
            cargarEmpresas();
        } catch (SistemaVentaPasajesException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            System.exit(0);
        }
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
                actualizarRutDesdeNombre();
            }
        });

        boxRut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarNombreDesdeRut();
            }
        });

        setLocationRelativeTo(null);
    }

    private void onOK() throws SistemaVentaPasajesException {
        try {
            generarReporte();
        } catch (SistemaVentaPasajesException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }


    private void onCancel() {
        dispose();
    }

    private void cargarEmpresas() throws  SistemaVentaPasajesException {

        if(empresas.length == 0){
            throw new SistemaVentaPasajesException("No hay empresas en el registro");
        }
        for (String[] empresa : empresas) {
            boxNombreEmp.addItem(empresa[1]);
            boxRut.addItem(empresa[0]);
        }
    }



    private void actualizarRutDesdeNombre() {
        String empresaSeleccionada = (String) boxNombreEmp.getSelectedItem();

        for (String[] empresa : empresas) {
            if (empresa[1].equals(empresaSeleccionada)) {
                boxRut.setSelectedItem(empresa[0]);
                break;
            }
        }
    }
    private void actualizarNombreDesdeRut() {
        String rutSeleccionado = (String) boxRut.getSelectedItem();

        for (String[] empresa : empresas) {
            if (empresa[0].equals(rutSeleccionado)) {
                boxNombreEmp.setSelectedItem(empresa[1]);
                break;
            }
        }
    }

    private String[][] imprimirPorRut(String rut){
        return Arrays.stream(ventasPorEmpresa)
                .filter(empresa -> empresa[0].equals(rut))
                .map(empresa -> new String[]{
                        empresa[1],
                        empresa[2],
                        empresa[3],
                        empresa[4]
                }).toArray(String[][]::new);
    }


    private void generarReporte() throws SistemaVentaPasajesException {
        String rutSeleccionado = (String) boxRut.getSelectedItem();
        // String [][] ventasPorEmpresa = ControladorEmpresas.getInstance().listVentasEmpresa(rutSeleccionado);
        String[][] empresaSeleccionada = imprimirPorRut(rutSeleccionado);

        /*if (ventasPorEmpresa.length == 0) {
            throw new SistemaVentaPasajesException("La empresa seleccionada no posee ventas");
        }

         */

        // Verificar si la empresa tiene ventas
        boolean tieneVentas = false;
        for (String[] venta : empresaSeleccionada) {
            if (!venta[3].equals("0")) {
                tieneVentas = true;
                break;
            }
        }
        if (!tieneVentas) {
            throw new SistemaVentaPasajesException("La empresa seleccionada no posee ventas, porfavor seleccione otra empresa");
        }

        tablaVentas.setModel(new DefaultTableModel(empresaSeleccionada, columnas));
    }




    public static void main(String[] args) {

        GUIListaVentasEmpresas dialog = new GUIListaVentasEmpresas();
        dialog.setSize(600, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        System.exit(0);
    }


}

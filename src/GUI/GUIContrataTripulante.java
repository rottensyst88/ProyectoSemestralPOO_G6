package GUI;

import controlador.ControladorEmpresas;
import excepciones.SistemaVentaPasajesException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUIContrataTripulante extends JDialog{
    private JPanel panel1;
    private JRadioButton srRadioButton;
    private JRadioButton conductorRadioButton;
    private JRadioButton sraRadioButton;
    private JRadioButton auxiliarRadioButton;
    private JRadioButton rUTRadioButton;
    private JRadioButton pasaporteRadioButton;
    private JTextField pasaporteOrut;
    private JComboBox comunaCBox;
    private JTextField nombreTextField;
    private JTextField apellidoPTextField;
    private JButton cancelButton;
    private JButton OKButton;
    private JComboBox boxRut;
    private JComboBox boxNombreEmp;
    private JTextField apellidoMTextField;
    private JTextField calleTextField;
    private JTextField numCalleTextfield;
    private JComboBox nacionalidadCBox;

    private ButtonGroup tipoTripulante = new ButtonGroup();
    private ButtonGroup tipoIdentificacion = new ButtonGroup();
    private ButtonGroup tipoGenero = new ButtonGroup();
    private String[][] empresas = ControladorEmpresas.getInstance().listEmpresas();


    public GUIContrataTripulante() throws SistemaVentaPasajesException{

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
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarRutDesdeNombre();
            }
        });

        boxRut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarNombreDesdeRut();
            }
        });

        conductorRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { }
        });

        auxiliarRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { }
        });

        nombreTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {  }
        });

        apellidoPTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {  }
        });

        apellidoMTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {  }
        });

        calleTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {  }
        });

        nacionalidadCBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {  }
        });

        comunaCBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {  }
        });

        pasaporteOrut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        srRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        sraRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        //buttongroup
        tipoTripulante.add(conductorRadioButton);
        tipoTripulante.add(auxiliarRadioButton);
        tipoGenero.add(sraRadioButton);
        tipoGenero.add(srRadioButton);
        tipoIdentificacion.add(pasaporteRadioButton);
        tipoIdentificacion.add(rUTRadioButton);


        nombreTextField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!(Character.isAlphabetic(e.getKeyChar())))  {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Ingrese un caracter valido", "Caracter invalido", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


        apellidoPTextField.addKeyListener(new KeyAdapter() {
            public  void keyTyped(KeyEvent e) {
                if (!(Character.isAlphabetic(e.getKeyChar())))  {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Ingrese un caracter valido", "Caracter invalido", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


        apellidoMTextField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!(Character.isAlphabetic(e.getKeyChar())))  {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Ingrese un caracter valido", "Caracter invalido", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


        calleTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!(Character.isAlphabetic(e.getKeyChar()) || Character.isSpaceChar(e.getKeyChar())))  {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Ingrese un caracter valido", "Caracter invalido", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


        numCalleTextfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!(Character.isDigit(e.getKeyChar())))  {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Ingrese un caracter valido", "Caracter invalido", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        pasaporteOrut.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e){
                if (pasaporteRadioButton.isSelected()){
                    if (!(Character.isDigit(e.getKeyChar())))  {
                        e.consume();
                        JOptionPane.showMessageDialog(null, "Ingrese un caracter valido", "Caracter invalido", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        pasaporteRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pasaporteOrut.setText("");
            }
        });

        rUTRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pasaporteOrut.setText("");
                pasaporteOrut.setText("11.111.111-1");
                pasaporteOrut.setForeground(Color.LIGHT_GRAY);
            }
        });

        pasaporteOrut.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (pasaporteOrut.getText().equals("11.111.111-1")) {
                    pasaporteOrut.setText("");
                    pasaporteOrut.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (pasaporteOrut.getText().isEmpty() && rUTRadioButton.isSelected()) {
                    pasaporteOrut.setText("11.111.111-1");
                    pasaporteOrut.setForeground(Color.LIGHT_GRAY);
                }
            }
        });






    }

    private void onOK() {
        verificacionDatos();

    }




    private void onCancel() {
        dispose();
    }




    //METODOS PRIVADOS

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

    private void verificacionDatos() {
        if(tipoTripulante.getSelection() == null || tipoGenero.getSelection() == null
        || tipoIdentificacion.getSelection() == null || pasaporteOrut.getText().isEmpty()
        || nombreTextField.getText().isEmpty() || apellidoPTextField.getText().isEmpty()
        || apellidoMTextField.getText().isEmpty() || calleTextField.getText().isEmpty()
        || numCalleTextfield.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Por favor, rellene todos los campos",
                    "Campos incompletos",
                    JOptionPane.WARNING_MESSAGE);

        }
        guardarDatos();
    }

    private void guardarDatos() {
        String calle = calleTextField.getText().trim();
        int numCalle = Integer.parseInt(numCalleTextfield.getText());
        String nombre = nombreTextField.getText();
        String apellidoP = apellidoPTextField.getText();
        String apellidoM = apellidoMTextField.getText();
        String nacionalidad = nacionalidadCBox.getSelectedItem().toString();
        String comuna = comunaCBox.getSelectedItem().toString();





    }


    public static void display() {
        GUIContrataTripulante dialog = new GUIContrataTripulante();
        dialog.setSize(600, 800);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }





}

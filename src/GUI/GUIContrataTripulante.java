package GUI;

import controlador.ControladorEmpresas;
import excepciones.SVPException;
import utilidades.*;
import vista.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUIContrataTripulante extends JDialog {
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


    public GUIContrataTripulante() throws SVPException {

        cargarEmpresas();

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

        //buttongroup
        tipoTripulante.add(conductorRadioButton);
        tipoTripulante.add(auxiliarRadioButton);
        tipoGenero.add(sraRadioButton);
        tipoGenero.add(srRadioButton);
        tipoIdentificacion.add(pasaporteRadioButton);
        tipoIdentificacion.add(rUTRadioButton);


        nombreTextField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!(Character.isAlphabetic(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Ingrese un caracter valido", "Caracter invalido", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        apellidoPTextField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!(Character.isAlphabetic(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Ingrese un caracter valido", "Caracter invalido", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        apellidoMTextField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!(Character.isAlphabetic(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Ingrese un caracter valido", "Caracter invalido", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        calleTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!(Character.isAlphabetic(e.getKeyChar()) || Character.isSpaceChar(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Ingrese un caracter valido", "Caracter invalido", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        numCalleTextfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!(Character.isDigit(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Ingrese un caracter valido", "Caracter invalido", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        pasaporteOrut.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (pasaporteRadioButton.isSelected()) {
                    if (!(Character.isDigit(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE)) {
                        e.consume();
                        JOptionPane.showMessageDialog(null, "Ingrese un caracter valido", "Caracter invalido", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });


        //si pasaporte es seleccionado, se elimina el ejemplo de rut
        // y se deja la seleccion de nacionalidad habilitada
        pasaporteRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nacionalidadCBox.setEnabled(true);
                pasaporteOrut.setText("");
                pasaporteOrut.setForeground(Color.BLACK);
            }
        });

        //cuando se selecciona el boton RUT, se desactiva la opcion de nacionalidad
        // y se deja el ejemplo de rut para el usuario, en color gris
        rUTRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nacionalidadCBox.setEnabled(false);
                pasaporteOrut.setText("00.000.000-0");
                pasaporteOrut.setForeground(Color.LIGHT_GRAY);
            }
        });

        //Cuando recibe atencion, se elimina el texto ejemplo y se deja el texto en color negro


        pasaporteOrut.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) throws ArrayIndexOutOfBoundsException {

                //Cuando el cuadro de texto pierde atencion, dependiendo de los requerimientos
                //se deja el ejemplo de rut para el usuario
                if (rUTRadioButton.isSelected()) {
                    String rut = pasaporteOrut.getText();

                    try {
                        if (Rut.of(rut) == null) {
                            JOptionPane.showMessageDialog(null, "Formato de RUT incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
                            pasaporteOrut.setText("");
                        }

                    } catch (ArrayIndexOutOfBoundsException exception) {
                        JOptionPane.showMessageDialog(null, "Formato de RUT incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
                        pasaporteOrut.setText("");
                    }


                    if ((!pasaporteRadioButton.isSelected()))
                    {
                        pasaporteOrut.setText("00.000.000-0");
                        pasaporteOrut.setForeground(Color.LIGHT_GRAY);
                    }
                }
            }
        });


        pasaporteOrut.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (pasaporteOrut.getText().equals("00.000.000-0")) {
                    pasaporteOrut.setText("");
                    pasaporteOrut.setForeground(Color.BLACK);
                }

            }
        });

    }

    private void onOK() throws SVPException {

        try {
            verificacionDatos();

        } catch (SVPException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);

            limpiarDatos();
        }

    }

    private void onCancel() {
        dispose();
    }


    //METODOS PRIVADOS

    private void cargarEmpresas() throws SVPException {

        if (empresas.length == 0) {
            throw new SVPException("No hay empresas en el registro");
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

    private void verificacionDatos() throws SVPException {
        if (tipoTripulante.getSelection() == null || tipoGenero.getSelection() == null
                || tipoIdentificacion.getSelection() == null || pasaporteOrut.getText().isEmpty()
                || nombreTextField.getText().isEmpty() || apellidoPTextField.getText().isEmpty()
                || apellidoMTextField.getText().isEmpty() || calleTextField.getText().isEmpty()
                || numCalleTextfield.getText().isEmpty() || pasaporteOrut.getText().equals("00.000.000-0")) {
            JOptionPane.showMessageDialog(null,
                    "Por favor, rellene todos los campos",
                    "Campos incompletos",
                    JOptionPane.WARNING_MESSAGE);

        } else {
            guardarDatos();

        }

    }

    private void guardarDatos() throws SVPException {
        String calle = calleTextField.getText().trim();
        int numCalle = Integer.parseInt(numCalleTextfield.getText());
        String nombre = nombreTextField.getText();
        String apellidoP = apellidoPTextField.getText();
        String apellidoM = apellidoMTextField.getText();
        Rut rutEmp = Rut.of(boxRut.getSelectedItem().toString());
        String comuna = comunaCBox.getSelectedItem().toString();
        String genero = "";
        if (srRadioButton.isSelected()) {
            genero = "Sr.";
        } else if (sraRadioButton.isSelected()) {
            genero = "Sra.";
        }


        Direccion dir = new Direccion(calle, numCalle, comuna);
        Nombre tripulante = new Nombre();
        IdPersona id;
        Tratamiento tratamiento;
        tratamiento = selectorTratamiento(genero);

        tripulante.setNombre(nombre);
        tripulante.setApellidoPaterno(apellidoP);
        tripulante.setApellidoMaterno(apellidoM);
        tripulante.setTratamiento(tratamiento);

        if (rUTRadioButton.isSelected()) {
            id = Rut.of(pasaporteOrut.getText());
        } else {
            String pasaporte = pasaporteOrut.getText();
            String nacionalidad = nacionalidadCBox.getSelectedItem().toString();
            id = Pasaporte.of(pasaporte, nacionalidad);
        }


        if (conductorRadioButton.isSelected()) {
            ControladorEmpresas.getInstance().hireConductorForEmpresa(rutEmp, id, tripulante, dir);
            JOptionPane.showMessageDialog(null, "Conductor contratado exitosamente", "Contratacion exitosa", JOptionPane.INFORMATION_MESSAGE);
        } else {
            ControladorEmpresas.getInstance().hireAuxiliarForEmpresa(rutEmp, id, tripulante, dir);
            JOptionPane.showMessageDialog(null, "Auxiliar contratado exitosamente", "Contratacion exitosa", JOptionPane.INFORMATION_MESSAGE);

        }
    }





    private Tratamiento selectorTratamiento(String genero) {
        if(genero == "Sr."){
            return Tratamiento.SR;
        }
        return Tratamiento.SRA;
    }



    private void limpiarDatos(){

    tipoTripulante.clearSelection();
    tipoIdentificacion.clearSelection();
    tipoGenero.clearSelection();
    nombreTextField.setText("");
    apellidoPTextField.setText("");
    apellidoMTextField.setText("");
    calleTextField.setText("");
    numCalleTextfield.setText("");
    pasaporteOrut.setText("");

    }



    public static void display() {
        GUIContrataTripulante dialog = new GUIContrataTripulante();
        dialog.setSize(600, 800);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }





}

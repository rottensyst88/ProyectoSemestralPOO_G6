package persistencia;
import utilidades.*;
import modelo.*;

import java.io.Serializable;
import java.io.*;
import java.util.*;

public class IOSVP implements Serializable {
    private static IOSVP instance = new IOSVP();
    public static IOSVP getInstance() {
        return instance;
    }

    public Object[] readDatosIniciales(){
        ObjectInputStream archivo = null;

        try{
            archivo = new ObjectInputStream(new FileInputStream ("archivo.obj"));

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveControladores(Object[] controladores){

    }

    public Object[] readControladores(){
        ObjectInputStream archivo = null;

        try{
            archivo = new ObjectInputStream(new FileInputStream("archivo.obj"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void savePasajesDeVenta(Pasaje[] pasajes, String nombreArchivos){

    }

    private Optional<Empresa> findEmpresa(List<Empresa> empresas, Rut rut){

    }

    private Optional<Tripulante> findTripulante(Empresa empresa, IdPersona id){

    }

    private Optional<Bus> findBus(List<Bus> buses, String patente){

    }

    private Optional<Terminal> findTerminal(List<Terminal> terminales, String nombre){

    }
}

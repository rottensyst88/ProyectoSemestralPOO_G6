package persistencia;
import controlador.SistemaVentaPasajes;
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


        List<Object> objetos = new ArrayList<>();
        List<String> datosArchivo = new ArrayList<>();
        Scanner archivo = null;
        byte contador_pos = 0;

        try{
            archivo = new Scanner(new File("SVPDatosIniciales.txt"));

            archivo.useLocale(Locale.ENGLISH);
            while(archivo.hasNextLine()){
                datosArchivo.add(archivo.next());
            }

            for(String linea : datosArchivo){

            }


            // SECCION DE CLIENTES O PASAJEROS
            while(archivo.hasNextLine()){
                String[] temporal = archivo.nextLine().split(";");
                if(archivo.next().equals("+")){
                    contador_pos++;
                    break; // CONTADOR DE POSICIONES
                }
                switch(temporal[0]){
                    case "CP":{ //CLIENTE Y PASAJERO
                        Nombre nombre = new Nombre();
                        Nombre nContacto = new Nombre();
                        IdPersona id = Rut.of(temporal[1]);
                        nombre.setTratamiento(Enum.valueOf(Tratamiento.class, temporal[2]));
                        nombre.setNombre(temporal[3]);
                        nombre.setApellidoPaterno(temporal[4]);
                        nombre.setApellidoMaterno(temporal[5]);
                        String tel = temporal[6];
                        String email = temporal[7];
                        nContacto.setTratamiento(Enum.valueOf(Tratamiento.class, temporal[8]));
                        nContacto.setNombre(temporal[9]);
                        nContacto.setApellidoPaterno(temporal[10]);
                        nContacto.setApellidoMaterno(temporal[11]);
                        String telContacto = temporal[12];

                        Cliente c = new Cliente(id,nombre,email);
                        c.setTelefono(tel);

                        Pasajero p = new Pasajero(id,nombre,tel);
                        p.setTelefono(tel);
                        p.setNomContacto(nContacto);
                        p.setFonoContacto(telContacto);
                        break;
                    }
                    case "C":{ //CLIENTE
                        Nombre nombre = new Nombre();
                        IdPersona id = Rut.of(temporal[1]);
                        nombre.setTratamiento(Enum.valueOf(Tratamiento.class, temporal[2]));
                        nombre.setNombre(temporal[3]);
                        nombre.setApellidoPaterno(temporal[4]);
                        nombre.setApellidoMaterno(temporal[5]);
                        String tel = temporal[6];
                        String email = temporal[7];

                        Cliente c = new Cliente(id,nombre,email);
                        c.setTelefono(tel);
                        break;
                    }
                    case "P":{ //PASAJERO
                        Nombre nombre = new Nombre();
                        Nombre nContacto = new Nombre();
                        IdPersona id = Rut.of(temporal[1]);
                        nombre.setTratamiento(Enum.valueOf(Tratamiento.class, temporal[2]));
                        nombre.setNombre(temporal[3]);
                        nombre.setApellidoPaterno(temporal[4]);
                        nombre.setApellidoMaterno(temporal[5]);
                        String tel = temporal[6];
                        nContacto.setTratamiento(Enum.valueOf(Tratamiento.class, temporal[7]));
                        nContacto.setNombre(temporal[8]);
                        nContacto.setApellidoPaterno(temporal[9]);
                        nContacto.setApellidoMaterno(temporal[10]);
                        String telContacto = temporal[11];

                        Pasajero p = new Pasajero(id,nombre,telContacto);
                        p.setTelefono(tel);
                        p.setNomContacto(nContacto);
                        break;
                    }
                    default:{
                        System.out.println("ERROR! ESTO NO DEBERIA PASAR!");
                    }
                }
            }
            while(archivo.hasNextLine() && )

            while()



        } catch (FileNotFoundException e){
            System.out.println("Error! El archivo requerido no ha sido encontrado!");
            return null;
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

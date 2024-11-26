package persistencia;

import excepciones.SVPException;
import utilidades.*;
import modelo.*;

import java.io.Serializable;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class IOSVP implements Serializable {
    private static IOSVP instance = new IOSVP();

    public static IOSVP getInstance() {
        return instance;
    }

    public Object[] readDatosIniciales() throws SVPException {
        List<String> datosArchivo = new ArrayList<>();

        List<String> clientes_pasajeros = new ArrayList<>();
        List<String> empresas = new ArrayList<>();
        List<String> tripulantes = new ArrayList<>();
        List<String> terminales = new ArrayList<>();
        List<String> buses = new ArrayList<>();
        List<String> viajes = new ArrayList<>();

        List<Object> objetos = new ArrayList<>();

        Scanner archivo;
        byte contador_pos = 0;

        try {
            archivo = new Scanner(new File("SVPDatosIniciales.txt"));

            archivo.useLocale(Locale.ENGLISH);
            while (archivo.hasNextLine()) {
                datosArchivo.add(archivo.next());
            }

        } catch (FileNotFoundException e) {
            throw new SVPException("No existe o no se puede abrir el archivo SVPDatosIniciales.txt");
        }

        archivo.close();

        for (String linea : datosArchivo) {
            if (linea.equals("+")) {
                contador_pos++;
            } else {
                switch (contador_pos) {
                    case 0 -> clientes_pasajeros.add(linea);
                    case 1 -> empresas.add(linea);
                    case 2 -> tripulantes.add(linea);
                    case 3 -> terminales.add(linea);
                    case 4 -> buses.add(linea);
                    case 5 -> viajes.add(linea);
                }
            }
            datosArchivo.remove(linea);
        }

        for (String s : clientes_pasajeros) {
            String[] x = s.split(";");

            IdPersona rut = Rut.of(x[1]);
            Nombre nombre = new Nombre();
            nombre.setTratamiento(Tratamiento.valueOf(x[2]));
            nombre.setNombre(x[3]);
            nombre.setApellidoPaterno(x[4]);
            nombre.setApellidoMaterno(x[5]);
            String telefono = x[6];

            if (x[0].equals("C") || x[0].equals("CP")) {
                String email = x[7];
                if (x[0].equals("CP")) {
                    Nombre nContacto = new Nombre();
                    nContacto.setTratamiento(Tratamiento.valueOf(x[8]));
                    nContacto.setNombre(x[9]);
                    nContacto.setApellidoPaterno(x[10]);
                    nContacto.setApellidoMaterno(x[11]);
                    String telContacto = x[12];

                    Cliente c = new Cliente(rut, nombre, email);
                    c.setTelefono(telefono);
                    objetos.add(c);

                    Pasajero p = new Pasajero(rut, nombre, telContacto);
                    p.setTelefono(telefono);
                    p.setNomContacto(nContacto);
                    objetos.add(p);
                } else {
                    Cliente c = new Cliente(rut, nombre, email);
                    c.setTelefono(telefono);
                    objetos.add(c);
                }
            } else {
                Nombre nContacto = new Nombre();
                nContacto.setTratamiento(Tratamiento.valueOf(x[7]));
                nContacto.setNombre(x[8]);
                nContacto.setApellidoPaterno(x[9]);
                nContacto.setApellidoMaterno(x[10]);
                String telContacto = x[11];

                Pasajero p = new Pasajero(rut, nombre, telContacto);
                p.setTelefono(telefono);
                p.setNomContacto(nContacto);
                objetos.add(p);
            }
        }

        for (String s : empresas) {
            String[] x = s.split(";");
            Empresa e = new Empresa(Rut.of(x[0]), x[1], x[2]);
            objetos.add(e);
        }

        for (String s : tripulantes) {
            String[] x = s.split(";");

            Nombre nombre = new Nombre();
            Rut rut = Rut.of(x[1]);
            nombre.setTratamiento(Tratamiento.valueOf(x[2]));
            nombre.setNombre(x[3]);
            nombre.setApellidoPaterno(x[4]);
            nombre.setApellidoMaterno(x[5]);

            Direccion dir = new Direccion(x[6], Integer.parseInt(x[7]), x[8]);
            String rutEmpresa = x[9];

            List<Empresa> empresasF = new ArrayList<>();
            for (Object emp : objetos) {
                if (emp instanceof Empresa) {
                    empresasF.add((Empresa) emp);
                }
            }

            Optional<Empresa> e = findEmpresa(empresasF, Rut.of(rutEmpresa));

            if (e.isPresent()) {
                if (x[0].equals("A")) {
                    e.get().addAuxiliar(rut, nombre, dir);
                } else {
                    e.get().addConductor(rut, nombre, dir);
                }
            }
        }

        for (String s : terminales) {
            String[] x = s.split(";");
            Direccion dir = new Direccion(x[1], Integer.parseInt(x[2]), x[3]);
            Terminal t = new Terminal(x[0], dir);
            objetos.add(t);
        }

        for (String s : buses) {
            String[] x = s.split(";");
            Bus b = new Bus(x[0], Integer.parseInt(x[3]));
            b.setMarca(x[1]);
            b.setModelo(x[2]);
            String rutEmpresa = x[4];

            List<Empresa> empresasF = new ArrayList<>();
            for (Object emp : objetos) {
                if (emp instanceof Empresa) {
                    empresasF.add((Empresa) emp);
                }
            }
            Optional<Empresa> e = findEmpresa(empresasF, Rut.of(rutEmpresa));
            if (e.isPresent()) {
                e.get().addBus(b);
                objetos.add(b);
            }
        }

        for (String s : viajes) {
            String[] x = s.split(";");
            LocalDate fecha = LocalDate.parse(x[0], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            LocalTime hora = LocalTime.parse(x[1], DateTimeFormatter.ofPattern("hh:mm"));
            String patenteBus = x[4];
            String rutAuxiliar = x[5];
            String rutConductor = x[6];
            String nTerminalSalida = x[7];
            String nTerminalLlegada = x[8];

            List<Bus> busesF = new ArrayList<>();
            for(Object o : objetos){
                if(o instanceof Bus){
                    busesF.add((Bus) o);
                }
            }
            Optional<Bus> busBuscado = findBus(busesF, patenteBus);

            Optional<Tripulante> auxBusqueda = null;
            Optional<Tripulante> condBusqueda = null;

            if(busBuscado.isPresent()){
                Empresa eV = busBuscado.get().getEmp();
                auxBusqueda = findTripulante(eV,Rut.of(rutAuxiliar),"Auxiliar");
                condBusqueda = findTripulante(eV,Rut.of(rutConductor),"Conductor");
            }

            List<Terminal> terminalF = new ArrayList<>();
            for(Object o : objetos){
                if(o instanceof Terminal){
                    terminalF.add((Terminal) o);
                }
            }

            Optional<Terminal> termSalida = findTerminal(terminalF,nTerminalSalida);
            Optional<Terminal> termLlegada = findTerminal(terminalF,nTerminalLlegada);


            Viaje v = new Viaje(fecha, hora, Integer.parseInt(x[2]), Integer.parseInt(x[3]), busBuscado.get(),
                    (Auxiliar) auxBusqueda.get(), (Conductor) condBusqueda.get(), termSalida.get(), termLlegada.get()); //TODO FALTAN LOS FIND
            objetos.add(v);
        }
        return objetos.toArray();
    }

    public void saveControladores(Object[] controladores) throws SVPException {
        ObjectOutputStream objetoArch = null;

        try {
            objetoArch = new ObjectOutputStream(new FileOutputStream("SVPObjetos.obj"));

            for (Object o : controladores) {
                objetoArch.writeObject(o);
            }

            objetoArch.close();

        } catch (IOException e) {
            throw new SVPException("");
        }
    }

    public Object[] readControladores() throws SVPException {
        ObjectInputStream objetoArch = null;
        List<Object> objetosLeidos = new ArrayList<>();

        try {
            objetoArch = new ObjectInputStream(new FileInputStream("SVPObjetos.obj"));

            while (true) {
                objetosLeidos.add(objetoArch.readObject());
            }

        } catch (EOFException ex) {
            try {
                objetoArch.close();
            } catch (IOException ex2) {
                throw new SVPException("");
            }
            return objetosLeidos.toArray();
        } catch (ClassNotFoundException | IOException exFinal) {
            throw new SVPException("");
        }
    }

    public void savePasajesDeVenta(Pasaje[] pasajes, String nombreArchivos) throws SVPException {
        PrintStream out = null;

        try {
            out = new PrintStream(new File(nombreArchivos));

            for (Pasaje p : pasajes) {
                out.println(p.toString());
            }

            out.close();

        } catch (FileNotFoundException e) {
            throw new SVPException("");
        }
    }

    private Optional<Empresa> findEmpresa(List<Empresa> empresas, Rut rut) {
        Optional<Empresa> valor = Optional.empty();
        for (Empresa e : empresas) {
            if (e.getRut().equals(rut)) {
                valor = Optional.of(e);
                break;
            }
        }

        return valor;
    }

    private Optional<Tripulante> findTripulante(Empresa empresa, IdPersona id, String tipoDato) {
        Optional<Tripulante> valor = Optional.empty();
        Tripulante[] trip = empresa.getTripulantes();

        for (Tripulante t : trip) {
            if (t.getIdPersona().equals(id)) {
                if (t instanceof Auxiliar && tipoDato.equalsIgnoreCase("Auxiliar")) {
                    valor = Optional.of(t);
                    break;
                }

                if (t instanceof Conductor && tipoDato.equalsIgnoreCase("Conductor")) {
                    valor = Optional.of(t);
                    break;
                }
            }
        }
        return valor;
    }

    private Optional<Bus> findBus(List<Bus> buses, String patente) {
        Optional<Bus> valor = Optional.empty();
        for (Bus b : buses) {
            if (b.getPatente().equals(patente)) {
                valor = Optional.of(b);
                break;
            }
        }
        return valor;
    }

    private Optional<Terminal> findTerminal(List<Terminal> terminales, String nombre) {
        Optional<Terminal> valor = Optional.empty();

        for (Terminal t : terminales) {
            if (t.getNombre().equals(nombre)) {
                valor = Optional.of(t);
            }
        }
        return valor;
    }
}

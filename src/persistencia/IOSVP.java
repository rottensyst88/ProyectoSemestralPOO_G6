package persistencia;

import excepciones.SVPException;
import utilidades.*;
import modelo.*;

import java.io.Serializable;
import java.io.*;
import java.lang.reflect.Array;
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
        Scanner archivo;
        byte contador_pos = 0;
        List<String> datosArchivo = new ArrayList<>();
        List<String> clientes_pasajeros = new ArrayList<>();
        List<String> empresas = new ArrayList<>();
        List<String> tripulantes = new ArrayList<>();
        List<String> terminales = new ArrayList<>();
        List<String> buses = new ArrayList<>();
        List<String> viajes = new ArrayList<>();
        List<Object> objetos = new ArrayList<>();
        try {
            archivo = new Scanner(new File("SVPDatosIniciales.txt"));
            while (archivo.hasNextLine()) {
                datosArchivo.add(archivo.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new SVPException("No existe o no se puede abrir el archivo SVPDatosIniciales.txt");
        }
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
        }
        for (String s : clientes_pasajeros) {
            String[] x = s.split("',");

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
                    c.setTelefono(telef0no);
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

            Tripulante var = null;
            Optional<Empresa> e = findEmpresa(empresasF, Rut.of(rutEmpresa));
            if (e.isPresent()) {
                if (x[0].equals("A")) {
                    var = new Auxiliar(rut, nombre, dir);
                    e.get().addAuxiliar(var.getIdPersona(), var.getNombreCompleto(), var.getDireccion());
                } else {
                    var = new Conductor(rut, nombre, dir);
                    e.get().addConductor(var.getIdPersona(), var.getNombreCompleto(), var.getDireccion());
                }
            }
            objetos.add(var);
        }
        for (String s : terminales) {
            String[] x = s.split(";");
            Direccion dir = new Direccion(x[1], Integer.parseInt(x[2]), x[3]);
            Terminal t = new Terminal(x[0], dir);

            objetos.add(t);
        }
        for (String s : buses) {
            String[] x = s.split(";");
            String rutEmpresa = x[4];

            List<Empresa> empresasF = new ArrayList<>();
            for (Object emp : objetos) {
                if (emp instanceof Empresa) {
                    empresasF.add((Empresa) emp);
                }
            }
            Optional<Empresa> e = findEmpresa(empresasF, Rut.of(rutEmpresa));
            if (e.isPresent()) {
                Bus b = new Bus(x[0], Integer.parseInt(x[3]), e.get());
                b.setMarca(x[1]);
                b.setModelo(x[2]);
                e.get().addBus(b);
            }
        }
        for (String s : viajes) {
            String[] x = s.split(";");
            LocalDate fecha = LocalDate.parse(x[0], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            LocalTime hora = LocalTime.parse(x[1], DateTimeFormatter.ofPattern("HH:mm"));
            String precio = x[2];
            String duracion = x[3];
            String patenteBus = x[4];
            String rutAuxiliar = x[5];
            String rutConductor = x[6];
            String nTerminalSalida = x[7];
            String nTerminalLlegada = x[8];


            ArrayList<Bus> listaBuses = new ArrayList<>();
            ArrayList<Terminal> listaTerminal = new ArrayList<>();

            for (Object o : objetos) {
                if (o instanceof Bus) {
                    listaBuses.add((Bus) o);
                }
                if (o instanceof Terminal) {
                    listaTerminal.add((Terminal) o);
                }
            }
            Optional<Bus> bus = findBus(listaBuses, patenteBus);

            if (bus.isPresent()) {
                Empresa e = bus.get().getEmp();
                Optional<Tripulante> aux = findTripulante(e, Rut.of(rutAuxiliar), "Auxiliar");
                Optional<Tripulante> cond = findTripulante(e, Rut.of(rutConductor), "Conductor");
                Optional<Terminal> tLlegada = findTerminal(listaTerminal, nTerminalLlegada);
                Optional<Terminal> tSalida = findTerminal(listaTerminal, nTerminalSalida);

                if (aux.isPresent() && cond.isPresent() && tLlegada.isPresent() && tSalida.isPresent()) {
                    Viaje v = new Viaje(fecha, hora, Integer.parseInt(precio), Integer.parseInt(duracion), bus.get(), (
                            Auxiliar) aux.get(), (Conductor) cond.get(), tSalida.get(), tLlegada.get());

                    objetos.add(v);
                }
            }
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
        } catch (IOException e) {
            throw new SVPException("No se puede abrir o crear el archivo SVPObjetos.obj");
        } finally {
            if (objetoArch != null) {
                try {
                    objetoArch.close();
                } catch (IOException e) {
                    throw new SVPException("No se puede grabar en el archivo SVPObjetos.obj");
                }
            }
        }
    }

    public Object[] readControladores() throws SVPException {
        ObjectInputStream objetoArch = null;
        List<Object> objetosLeidos = new ArrayList<>();
        try {
            objetoArch = new ObjectInputStream(new FileInputStream("SVPObjetos.obj"));
            Object objeto;
            while (true) {
                try {
                    objeto = objetoArch.readObject();
                    objetosLeidos.add(objeto);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new SVPException("No existe o no se puede abrir el archivo SVPObjetos.obj");
        } catch (ClassNotFoundException e) {
            throw new SVPException("No se puede leer el archivo SVPObjetos.obj");
        } finally {
            if (objetoArch != null) {
                try {
                    objetoArch.close();
                } catch (IOException e) {
                    throw new SVPException("No se puede leer el archivo SVPObjetos.obj");
                }
            }
        }
        return objetosLeidos.toArray();
    }

    public void savePasajesDeVenta(Pasaje[] pasajes, String nombreArchivos) throws SVPException {
        PrintStream out;
        try {
            out = new PrintStream(nombreArchivos);
            for (Pasaje pasaje : pasajes) {
                out.println(pasaje.toString());
            }
            out.close();
        } catch (FileNotFoundException e) {
            throw new SVPException("No se puede abrir o crear el archivo" + nombreArchivos);
        }
    }

    /* METODOS FIND USADOS PARA DIVERSOS MOTIVOS */
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
            if (t.getIdPersona().equals("id")) {
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

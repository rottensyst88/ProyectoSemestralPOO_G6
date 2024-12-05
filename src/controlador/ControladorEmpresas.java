package controlador;

import modelo.*;
import excepciones.*;
import utilidades.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ControladorEmpresas {
    //SINGLETON
    private static final ControladorEmpresas instance = new ControladorEmpresas();

    public static ControladorEmpresas getInstance() {
        return instance;
    }

    private ControladorEmpresas() {
    }

    //ATRIBUTOS CLASE

    private List<Terminal> terminales = new ArrayList<>();
    private List<Bus> buses = new ArrayList<>();
    private List<Empresa> empresas = new ArrayList<>();


    //METODOS

    public void createEmpresa(Rut rut, String nombre, String url) throws SistemaVentaPasajesException {
        Optional<Empresa> empresaExist = findEmpresa(rut);
        if (empresaExist.isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe empresa con el rut indicado");
        }
        Empresa nuevaEmpresa = new Empresa(rut, nombre, url);
        empresas.add(nuevaEmpresa);
    }

    public void createBus(String pat, String marca, String modelo, int nroAsientos, Rut rutEmp) throws SistemaVentaPasajesException {

        Optional<Bus> busExist = findBus(pat);
        Optional<Empresa> empresaExist = findEmpresa(rutEmp);
        if (busExist.isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe bus con la patente indicada");
        }
        if (empresaExist.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado");
        }
        Empresa e = empresaExist.get();
        Bus nuevoBus = new Bus(pat, nroAsientos, e);
        buses.add(nuevoBus);
    }

    public void createTerminal(String nombre, Direccion direccion) throws SistemaVentaPasajesException {
        Optional<Terminal> terminalNombre = findTerminal(nombre);
        Optional<Terminal> terminalComuna = findTerminal(direccion.getComuna());

        if (terminalNombre.isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe terminal con el nombre indicado");
        }
        if (terminalComuna.isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe terminal en la comuna indicada");
        }

        Terminal nuevoTerminal = new Terminal(nombre, direccion);
        terminales.add(nuevoTerminal);
    }

    public void hireConductorForEmpresa(Rut rutEmp, IdPersona id, Nombre nom, Direccion dir) throws SistemaVentaPasajesException {
        Optional<Empresa> empresaExist = findEmpresa(rutEmp);
        Optional<Conductor> conductorExist = findConductor(id, rutEmp);

        Optional<Auxiliar> auxiliarExist = findAuxiliar(id, rutEmp);

        if (empresaExist.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado");
        }

        if (conductorExist.isPresent() || auxiliarExist.isPresent()) {
            throw new SistemaVentaPasajesException("Ya está contratado conductor/auxiliar con el id dado en la empresa señalada");
        }

        Empresa e = empresaExist.get();
        e.addConductor(id, nom, dir);
    }

    public void hireAuxiliarForEmpresa(Rut rutEmp, IdPersona id, Nombre nom, Direccion dir) throws SistemaVentaPasajesException {
        Optional<Empresa> empresaExist = findEmpresa(rutEmp);
        Optional<Auxiliar> auxiliarExist = findAuxiliar(id, rutEmp);

        Optional<Conductor> conductorExist = findConductor(id, rutEmp);

        if (empresaExist.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado");
        }
        if (auxiliarExist.isPresent() || conductorExist.isPresent()) {
            throw new SistemaVentaPasajesException("Ya está contratado auxiliar/conductor con el id dado en la empresa señalada");
        }

        Empresa e = empresaExist.get();
        e.addAuxiliar(id, nom, dir);
    }

    public String[][] listEmpresas() {
        return empresas.stream()
                .map(e -> new String[]{
                        e.getRut().toString(),
                        e.getNombre(),
                        e.getUrl(),
                        String.valueOf(e.getTripulantes().length),
                        String.valueOf(e.getBuses().length),
                        String.valueOf(e.getVentas().length)
                }).toArray(String[][]::new);

    }

    public String[][] listLlegadasSalidasTerminal(String nombre, LocalDate fecha) throws SistemaVentaPasajesException {
        Optional<Terminal> terminalNombre = findTerminal(nombre);

        if (terminalNombre.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe terminal con el nombre indicado");
        }

        Terminal t = terminalNombre.get();

        //UTILIZO Stream.concat para combinar los stream de Salidas y Llegadas

        //Stream de llegadas
        Stream<String[]> llegadas = Arrays.stream(t.getLlegadas())
                .filter(viaje -> viaje.getFecha().equals(fecha))
                .map(viaje -> new String[]{
                        "Llegada",
                        viaje.getFechaHoraTermino().toLocalTime().toString(),
                        viaje.getBus().getPatente(),
                        viaje.getBus().getEmp().getNombre(),
                        String.valueOf(viaje.getListaPasajeros().length),
                });


        Stream<String[]> salidas = Arrays.stream(t.getSalidas())
                .filter(viaje -> viaje.getFecha().equals(fecha))
                .map(viaje -> new String[]{
                        "Salida",
                        viaje.getHora().toString(),
                        viaje.getBus().getPatente(),
                        viaje.getBus().getEmp().getNombre(),
                        String.valueOf(viaje.getListaPasajeros().length),
                });

        return Stream.concat(llegadas, salidas)
                .toArray(String[][]::new);

    }

    public String[][] listVentasEmpresa(Rut rut) throws SistemaVentaPasajesException {

        Optional<Empresa> empresaExist = findEmpresa(rut);
        if (empresaExist.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado");
        }
        Venta[] ventas = empresaExist.get().getVentas();
        DateTimeFormatter fechaFormateada = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        return Arrays.stream(ventas)
                .map(v -> new String[]{
                        fechaFormateada.format(v.getFecha()),
                        v.getTipo().toString(),
                        String.valueOf(v.getMonto()),
                        v.getTipoPago()
                }).toArray(String[][]::new);

    }


    protected Optional<Empresa> findEmpresa(Rut rut) {
        return empresas.stream()
                .filter(e -> e.getRut().equals(rut))
                .findFirst();
    }

    protected Optional<Terminal> findTerminal(String nombre) {
        return terminales.stream()
                .filter(t -> t.getNombre().equals(nombre))
                .findFirst();
    }

    protected Optional<Terminal> findTerminalPorComuna(String comuna) {
        return terminales.stream()
                .filter(t -> t.getDireccion().getComuna().equals(comuna))
                .findFirst();
    }

    protected Optional<Bus> findBus(String patente) {
        return buses.stream()
                .filter(bus -> bus.getPatente()
                        .equals(patente)).findFirst();
    }

    protected Optional<Conductor> findConductor(IdPersona id, Rut rutEmpresa) {
        return empresas.stream()
                .filter(empresa -> empresa.getRut().equals(rutEmpresa))
                .flatMap(empresa -> Arrays.stream(empresa.getTripulantes()))
                .filter(tripulante -> tripulante instanceof Conductor)
                .map(tripulante -> (Conductor) tripulante)
                .filter(conductor -> conductor.getIdPersona().equals(id))
                .findFirst();
    }

    protected Optional<Auxiliar> findAuxiliar(IdPersona id, Rut rutEmpresa) {
        return empresas.stream()
                .filter(empresa -> empresa.getRut().equals(rutEmpresa))
                .flatMap(empresa -> Arrays.stream(empresa.getTripulantes()))
                .filter(tripulante -> tripulante instanceof Auxiliar)
                .map(tripulante -> (Auxiliar) tripulante)
                .filter(auxiliar -> auxiliar.getIdPersona().equals(id))
                .findFirst();
    }


}
package controlador;

import modelo.*;
import excepciones.*;
import utilidades.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        if(rut==null){ throw new SistemaVentaPasajesException("Rut invalido"); }
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
        if (empresaExist.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado");
        }

        if (conductorExist.isPresent()) {
            throw new SistemaVentaPasajesException("Ya está contratado conductor/auxiliar con el id dado en la empresa señalada");
        }

        Empresa e = empresaExist.get();
        e.addConductor(id, nom, dir);

    }


    public void hireAuxiliarForEmpresa(Rut rutEmp, IdPersona id, Nombre nom, Direccion dir) throws SistemaVentaPasajesException {
        Optional<Empresa> empresaExist = findEmpresa(rutEmp);
        Optional<Auxiliar> auxiliarExist = findAuxiliar(id, rutEmp);

        if (empresaExist.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado");
        }
        if (auxiliarExist.isPresent()) {
            throw new SistemaVentaPasajesException("Ya está contratado auxiliar/conductor con el id dado en la empresa señalada");

        }

        Empresa e = empresaExist.get();
        e.addAuxiliar(id, nom, dir);
    }

    public String[][] listEmpresas() {
        String[][] out = new String[empresas.size()][6];

        if (empresas.isEmpty()) {
            return out;
        }

        for (int i = 0; i < empresas.size(); i++) {
            Empresa empresa = empresas.get(i);
            out[i][0] = empresa.getRut().toString();
            out[i][1] = empresa.getNombre();
            out[i][2] = empresa.getUrl();
            out[i][3] = String.valueOf(empresa.getTripulantes().length); // Cantidad de tripulantes
            out[i][4] = String.valueOf(empresa.getBuses().length);// Cantidad de buses
            out[i][5] = String.valueOf(empresa.getVentas().length);
        }
        return out;
    }

    public String[][] listLlegadasSalidasTerminal(String nombre, LocalDate fecha) throws SistemaVentaPasajesException {
        Optional<Terminal> terminalNombre = findTerminal(nombre);

        if (terminalNombre.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe terminal con el nombre indicado");
        }

        Terminal t = terminalNombre.get();
        List<String[]> viajesFiltrados = new ArrayList<>();

        //LLENO LA LISTA DE FILTRADOS CON LOS VIAJES QUE TENGAN LA MISMA FECHA
        for (Viaje viaje : t.getLlegadas()) {
            if (viaje.getFecha().equals(fecha)) {
                String[] datosViaje = new String[5];
                datosViaje[0] = "Llegada";
                datosViaje[1] = viaje.getFechaHoraTermino().toLocalTime().toString();
                datosViaje[2] = viaje.getBus().getPatente();
                datosViaje[3] = viaje.getBus().getEmp().getNombre();
                datosViaje[4] = String.valueOf(viaje.getListaPasajeros().length);

                viajesFiltrados.add(datosViaje);
            }
        }

        for (Viaje viaje : t.getSalidas()) {
            if (viaje.getFecha().equals(fecha)) {
                String[] datosViaje = new String[5];
                datosViaje[0] = "Salida";
                datosViaje[1] = viaje.getHora().toString();
                datosViaje[2] = viaje.getBus().getPatente();
                datosViaje[3] = viaje.getBus().getEmp().getNombre();
                datosViaje[4] = String.valueOf(viaje.getListaPasajeros().length);

                viajesFiltrados.add(datosViaje);
            }
        }

        String[][] out = new String[viajesFiltrados.size()][5];

        if (viajesFiltrados.isEmpty()) {
            return out;
        }

        for (int i = 0; i < viajesFiltrados.size(); i++) {
            out[i] = viajesFiltrados.get(i);
        }

        return out;
    }

    public String[][] listVentasEmpresa(Rut rut) throws SistemaVentaPasajesException {

        Optional<Empresa> empresaExist = findEmpresa(rut);
        if (empresaExist.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado");
        }

        Venta[] ventas = empresaExist.get().getVentas();

        if (ventas.length == 0) {
            return new String[0][4];
        }

        String[][] out = new String[ventas.length][4];
        DateTimeFormatter fechaFormateada = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (int i = 0; i < ventas.length; i++) {
            Venta venta = ventas[i];

            out[i][0] = fechaFormateada.format(venta.getFecha());
            out[i][1] = venta.getTipo().toString();
            out[i][2] = String.valueOf(venta.getMonto());
            out[i][3] = venta.getTipoPago();
        }
        return out;
    }

    protected Optional<Empresa> findEmpresa(Rut rut) {
        for (Empresa e : empresas) {
            if (e.getRut().equals(rut)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    protected Optional<Terminal> findTerminal(String nombre) {
        for (Terminal t : terminales) {
            if (t.getNombre().equals(nombre)) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    protected Optional<Terminal> findTerminalPorComuna(String comuna) {
        for (Terminal t : terminales) {
            if (t.getDireccion().getComuna().equals(comuna)) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    protected Optional<Bus> findBus(String patente) {
        for (Bus b : buses) {
            if (b.getPatente().equals(patente)) {
                return Optional.of(b);
            }
        }
        return Optional.empty();
    }

    protected Optional<Conductor> findConductor(IdPersona id, Rut rutEmpresa) {
        for (Bus bus : buses) {
            if (bus.getEmp().getRut().equals(rutEmpresa)) {
                Tripulante[] trip = bus.getEmp().getTripulantes();
                for (Tripulante t : trip) {
                    if (t instanceof Conductor) {
                        if (t.getIdPersona().equals(id)) {
                            return Optional.of((Conductor) t);
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

    protected Optional<Auxiliar> findAuxiliar(IdPersona id, Rut rutEmpresa) {
        for (Bus bus : buses) {
            if (bus.getEmp().getRut().equals(rutEmpresa)) {
                Tripulante[] trip = bus.getEmp().getTripulantes();
                for (Tripulante t : trip) {
                    if (t instanceof Auxiliar) {
                        if (t.getIdPersona().equals(id)) {
                            return Optional.of((Auxiliar) t);
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }
}
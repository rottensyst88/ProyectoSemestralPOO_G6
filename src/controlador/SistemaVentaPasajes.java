package controlador;

import modelo.*;
import utilidades.*;
import excepciones.*;
import persistencia.IOSVP;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class SistemaVentaPasajes implements Serializable {
    private static SistemaVentaPasajes instancia;

    public static SistemaVentaPasajes getInstancia() {
        if (instancia == null) {
            instancia = new SistemaVentaPasajes();
        }
        return instancia;
    }

    private ArrayList<Cliente> clientes = new ArrayList<>();
    private ArrayList<Pasajero> pasajeros = new ArrayList<>();
    private ArrayList<Viaje> viajes = new ArrayList<>();
    private ArrayList<Venta> ventas = new ArrayList<>();
    private String fechaFormateada = "dd/MM/yyyy";
    private String horaFormateada = "HH:mm";

    public void createCliente(IdPersona id, Nombre nom, String fono, String email) throws SVPException {
        Cliente c = new Cliente(id, nom, email);
        c.setTelefono(fono);
        if (findCliente(id).isEmpty()) {
            clientes.add(c);
        } else {
            throw new SVPException("Ya existe cliente con el ID indicado!");
        }
    }

    public void createPasajero(IdPersona id, Nombre nom, String fono, Nombre nomContacto, String
            fonoContacto) throws SVPException {
        Pasajero p = new Pasajero(id, nom, fonoContacto);
        p.setNomContacto(nomContacto);
        p.setTelefono(fono);
        if (findPasajero(id).isEmpty()) {
            pasajeros.add(p);
        } else {
            throw new SVPException("Ya existe pasajero con el ID indicado!");
        }
    }

    public void createViaje(LocalDate fecha, LocalTime hora, int precio, int duracion, String patBus, IdPersona[]
            idTripulantes, String[] nomComunas) throws SVPException {
        if (findViaje(fecha.toString(), hora.toString(), patBus).isPresent()) {
            throw new SVPException("Ya existe viaje con fecha, hora y patente de bus indicados");
        }
        Optional<Bus> bus = ControladorEmpresas.getInstance().findBus(patBus);
        Auxiliar aux;
        Conductor con = null;
        if (bus.isEmpty()) {
            throw new SVPException("No existe bus con la patente indicada");
        }
        if (ControladorEmpresas.getInstance().findAuxiliar(idTripulantes[0], bus.get().getEmp().getRut()).isEmpty()) {
            throw new SVPException("No existe auxiliar con el ID indicado en la empresa con el RUT indicado");
        } else {
            aux = ControladorEmpresas.getInstance().findAuxiliar(idTripulantes[0], bus.get().getEmp().getRut()).get();
        }
        if (idTripulantes.length == 2) {
            if (ControladorEmpresas.getInstance().findConductor(idTripulantes[1], bus.get().getEmp().getRut()).isPresent()) {
                con = ControladorEmpresas.getInstance().findConductor(idTripulantes[1], bus.get().getEmp().getRut()).get();
            } else {
                throw new SVPException("No existe conductor con el ID indicado en la empresa con el RUT indicado");
            }
        }
        if (ControladorEmpresas.getInstance().findTerminalPorComuna(nomComunas[0]).isEmpty()) {
            throw new SVPException("No existe terminal de salida en la comuna indicada");
        }
        Terminal tSalida = ControladorEmpresas.getInstance().findTerminalPorComuna(nomComunas[0]).get();
        if (ControladorEmpresas.getInstance().findTerminalPorComuna(nomComunas[1]).isEmpty()) {
            throw new SVPException("No existe terminal de llegada en la comuna indicada");
        }
        Terminal tEntrada = ControladorEmpresas.getInstance().findTerminalPorComuna(nomComunas[1]).get();
        Viaje viaje = new Viaje(fecha, hora, precio, duracion, bus.get(), aux, con, tSalida, tEntrada);
        if (idTripulantes.length == 3) {
            if (ControladorEmpresas.getInstance().findConductor(idTripulantes[2], bus.get().getEmp().getRut()).isPresent()) {
                con = ControladorEmpresas.getInstance().findConductor(idTripulantes[2], bus.get().getEmp().getRut()).get();
                viaje.addConductor(con);
            } else {
                throw new SVPException("No existe conductor con el ID indicado en la empresa con el RUT indicado");
            }
        }
        if (findViaje(fecha.toString(), hora.toString(), patBus).isEmpty()) {
            viajes.add(viaje);
        } else {
            throw new SVPException("Ya existe viaje con fecha, hora y patente de bus indicados");
        }
    }

    public void iniciaVenta(String idDocumento, TipoDocumento tipo, LocalDate fecha, String comSalida, String comLlegada, IdPersona idCliente, int nroPasajes)
            throws SVPException {

        Optional<Venta> venta = findVenta(idDocumento, tipo);
        if (venta.isPresent()) {
            throw new SVPException("Ya existe venta con ID y Tipo de Doc. indicado");
        }
        Optional<Cliente> cliente = findCliente(idCliente);
        if (cliente.isEmpty()) {
            throw new SVPException("No existe un cliente con ID integrado!");
        }

        boolean centinela = viajes.stream().anyMatch
                (viaje -> viaje.getNroAsientosDisponibles() >= nroPasajes &&
                        viaje.getFecha().equals(fecha) &&
                        viaje.getTerminalLlegada().getDireccion().getComuna().equals(comLlegada) &&
                        viaje.getTerminalSAlida().getDireccion().getComuna().equals(comSalida));

        /*boolean centinela = false;

        for (Viaje viaje : viajes) {
            if (viaje.getNroAsientosDisponibles() >= nroPasajes && viaje.getFecha().equals(fecha)) {
                if (viaje.getTerminalLlegada().getDireccion().getComuna().equals(comLlegada)) {
                    if (viaje.getTerminalSAlida().getDireccion().getComuna().equals(comSalida)) {
                        centinela = true;
                    }
                }
            }
        }
         */
        if (!centinela) {
            throw new SVPException("No existen viajes disponibles en la fecha y con terminales en las comunas de salida y llegada indicados");
        }

        Venta nuevaVenta = new Venta(idDocumento, tipo, fecha, cliente.get());
        ventas.add(nuevaVenta);
    }

    public String[][] getHorariosDisponibles(LocalDate fechaViaje, String comunaSalida, String comunaLlegada, int nroPasajes) {
        return viajes.stream()
                .filter(v -> v.getFecha().equals(fechaViaje) &&
                        v.getTerminalLlegada().getDireccion().getComuna().equals(comunaLlegada) &&
                        v.getTerminalSAlida().getDireccion().getComuna().equals(comunaSalida) &&
                        v.getNroAsientosDisponibles() >= nroPasajes)
                .map(v -> new String[] {
                        v.getBus().getPatente(),
                        v.getHora().toString(),
                        String.valueOf(v.getPrecio()),
                        String.valueOf(v.getNroAsientosDisponibles())
                })
                .toArray(String[][]::new);
        /*ArrayList<Viaje> viajesFecha = new ArrayList<>();

        for (Viaje viaje : viajes) {
            if (viaje.getFecha().equals(fechaViaje)) {
                if (viaje.getTerminalLlegada().getDireccion().getComuna().equals(comunaLlegada) && viaje.getTerminalSAlida().getDireccion().getComuna().equals(comunaSalida)) {
                    if (viaje.getNroAsientosDisponibles() >= nroPasajes) {
                        viajesFecha.add(viaje);
                    }
                }
            }
        }

        if (viajesFecha.isEmpty()) {
            return new String[0][0];
        }

        String[][] horariosDisponibles = new String[viajesFecha.size()][4];

        for (int i = 0; i < viajesFecha.size(); i++) {
            Viaje viaje = viajesFecha.get(i);
            Bus bus = viaje.getBus();

            String horaComoString = viaje.getHora().toString();

            horariosDisponibles[i][0] = bus.getPatente();
            horariosDisponibles[i][1] = horaComoString;
            horariosDisponibles[i][2] = String.valueOf(viaje.getPrecio());
            horariosDisponibles[i][3] = String.valueOf(viaje.getNroAsientosDisponibles());

        }
        return horariosDisponibles;

         */
    }

    public String[] listAsientosDeViaje(LocalDate fecha, LocalTime hora, String patBus) {

        String horaComoString = hora.toString();
        String fechaComoString = fecha.toString();
        Optional<Viaje> viaje = findViaje(fechaComoString, horaComoString, patBus);

        if (viaje.isEmpty()) {
            return new String[0];
        }

        return viaje.get().getAsientos();
    }

    public Optional<String> getNombrePasajero(IdPersona idPasajero) {
        return findPasajero(idPasajero).map(pasajero -> pasajero.getNomContacto().getNombre());

        /*Optional<Pasajero> pasajero = findPasajero(idPasajero);

        if (pasajero.isPresent()) {
            return Optional.of(pasajero.get().getNomContacto().getNombre());
        } else {
            return Optional.empty();
        }

         */
    }

    public Optional<Integer> getMontoVenta(String idDocumento, TipoDocumento tipo) {
        return findVenta(idDocumento, tipo).map(Venta::getMonto);

        /*Optional<Venta> venta = findVenta(idDocumento, tipo);
        if (venta.isPresent()) {
            return Optional.of(venta.get().getMonto());
        }
        return Optional.empty();

         */
    }

    public void vendePasaje(String idDoc, TipoDocumento tipo, LocalTime hora, LocalDate fecha,
                            String patBus, int asiento, IdPersona idPasajero) throws SVPException {

        String horaComoString = hora.toString();
        String fechaComoString = fecha.toString();
        Optional<Viaje> viaje = findViaje(fechaComoString, horaComoString, patBus);
        Optional<Venta> venta = findVenta(idDoc, tipo);
        Optional<Pasajero> pasajero = findPasajero(idPasajero);

        if (venta.isEmpty()) {
            throw new SVPException("No existe venta con el ID y Tipo de Doc. indicados");
        }
        if (pasajero.isEmpty()) {
            throw new SVPException("No existe pasajero con el ID indicado");

        }
        if (viaje.isEmpty()) {
            throw new SVPException("No existe viaje con la fecha, hora y patente de bus indicados");
        }

        venta.get().createPasaje(asiento, viaje.get(), pasajero.get());
    }

    public void pagaVenta(String idDocumento, TipoDocumento tipo) throws SVPException {
        Optional<Venta> venta = findVenta(idDocumento, tipo);

        if (venta.isEmpty()) {
            throw new SVPException("No existe venta con el ID y Tipo de Doc. indicados");
        }

        if (venta.get().getMontoPagado() != 0) {
            throw new SVPException("La venta ya fue pagada");
        }
        venta.get().pagaMonto();
    }

    public void pagaVenta(String idDocumento, TipoDocumento tipo, long nroTarjeta) throws SVPException {
        Optional<Venta> venta = findVenta(idDocumento, tipo);

        if (venta.isEmpty()) {
            throw new SVPException("No existe venta con el ID y Tipo de Doc. indicados");
        }
        if (venta.get().getMontoPagado() != 0) {
            throw new SVPException("La venta ya fue pagada");
        }
        venta.get().pagaMonto(nroTarjeta);
    }

    public String[][] listVentas() {

        return ventas.stream()
                .map(venta -> new String[]{
                        venta.getIdDocumento(),
                        venta.getTipo().toString(),
                        fechaFormateada.format(venta.getFecha()),
                        venta.getCliente().getIdPersona().toString(),
                        venta.getCliente().getNombreCompleto().toString(),
                        String.valueOf(venta.getPasajes().length),
                        String.valueOf(venta.getMonto()),
                }).toArray(String[][]::new);


        /*String[][] arregloVentas = new String[ventas.size()][7];

        if (arregloVentas.length == 0) {
            return new String[0][0];
        }

        for (int i = 0; i < ventas.size(); i++) {
            Venta venta = ventas.get(i);
            arregloVentas[i][0] = venta.getIdDocumento();
            arregloVentas[i][1] = venta.getTipo().toString();
            arregloVentas[i][2] = venta.getFecha().format(DateTimeFormatter.ofPattern(fechaFormateada));
            arregloVentas[i][3] = venta.getCliente().getIdPersona().toString();
            arregloVentas[i][4] = venta.getCliente().getNombreCompleto().toString();
            String stringCantBoletos = "" + venta.getPasajes().length;
            arregloVentas[i][5] = stringCantBoletos;
            String stringTotalVenta = "" + venta.getMonto();
            arregloVentas[i][6] = stringTotalVenta;
        }
        return arregloVentas;

         */
    }

    public String[][] listViajes() {

        return viajes.stream()
                .map(viaje -> new String[]{
                        fechaFormateada.format(viaje.getFecha()),
                        horaFormateada.format(viaje.getHora()),
                        horaFormateada.format(viaje.getFechaHoraTermino()),
                        String.valueOf(viaje.getPrecio()),
                        String.valueOf(viaje.getNroAsientosDisponibles()),
                        viaje.getBus().getPatente(),
                        viaje.getTerminalSAlida().getDireccion().getComuna().toUpperCase(),
                        viaje.getTerminalLlegada().getDireccion().getComuna().toUpperCase(),
                }).toArray(String[][]::new);

        /*String[][] arregloViajes = new String[viajes.size()][8];

        if (viajes.isEmpty()) {
            return new String[0][0];
        }

        for (int i = 0; i < viajes.size(); i++) {
            Viaje viaje = viajes.get(i);
            arregloViajes[i][0] = viaje.getFecha().format(DateTimeFormatter.ofPattern(fechaFormateada));
            arregloViajes[i][1] = viaje.getHora().format(DateTimeFormatter.ofPattern(horaFormateada));
            arregloViajes[i][2] = viaje.getFechaHoraTermino().format(DateTimeFormatter.ofPattern(horaFormateada));
            arregloViajes[i][3] = String.valueOf(viaje.getPrecio());
            arregloViajes[i][4] = String.valueOf(viaje.getNroAsientosDisponibles());
            arregloViajes[i][5] = viaje.getBus().getPatente();
            arregloViajes[i][6] = viaje.getTerminalSAlida().getDireccion().getComuna().toUpperCase();
            arregloViajes[i][7] = viaje.getTerminalLlegada().getDireccion().getComuna().toUpperCase();
        }

        return arregloViajes;

         */
    }

    public String[][] listPasajerosViaje(LocalDate fecha, LocalTime hora, String patBus) throws SVPException {

        Optional<Viaje> viajes = findViaje(fecha.toString(), hora.toString(), patBus);

        if (viajes.isEmpty()) {
            throw new SVPException("No existe viaje con la fecha, hora y patente de bus indicadas");
        }

        String[][] arregloPasajeros = new String[pasajeros.size()][5];

        if (arregloPasajeros.length == 0) {
            return new String[0][0];
        }

        for (int i = 0; i < pasajeros.size(); i++) {
            Pasajero pasajero = pasajeros.get(i);

            for (Venta venta : ventas) {
                for (Pasaje pasaje : venta.getPasajes()) {
                    if (pasaje.getPasajero().getIdPersona().equals(pasajero.getIdPersona())) {
                        arregloPasajeros[i][0] = String.valueOf(pasaje.getAsiento());
                        break;
                    }
                }
            }

            arregloPasajeros[i][1] = pasajero.getIdPersona().toString();
            arregloPasajeros[i][2] = pasajero.getNombreCompleto().toString();
            arregloPasajeros[i][3] = pasajero.getNomContacto().toString();
            arregloPasajeros[i][4] = pasajero.getFonoContacto();
        }
        return arregloPasajeros;
    }

    /* CLASES REFERENTES A MANEJO DE IO*/

    public void generatePasajesVenta(String idDocumento, TipoDocumento tipo) throws SVPException {
        Optional<Venta> venta = findVenta(idDocumento, tipo);
        try {
            if (venta.isEmpty()) {
                throw new SVPException("La venta no existe");
            }
            String nombreObjeto = tipo.name() + "_" + idDocumento + ".txt";
            IOSVP.getInstance().savePasajesDeVenta(venta.get().getPasajes(),nombreObjeto);
        } catch (SVPException e) {
            throw new SVPException(e.getMessage());
        }
    }

    public void readDatosIniciales() throws SVPException {
        Object[] objetosDesdeIO;
        ArrayList<Object> objetosEmpresa = new ArrayList<>();

        try {
            objetosDesdeIO = IOSVP.getInstance().readDatosIniciales();
            for (Object o : objetosDesdeIO) {
                switch (o) {
                    case Pasajero pasajero -> pasajeros.add(pasajero);
                    case Viaje viaje -> viajes.add(viaje);
                    case Cliente cliente -> clientes.add(cliente);
                    case null, default -> objetosEmpresa.add(o);
                }
            }
            ControladorEmpresas.getInstance().setDatosIniciales(objetosEmpresa.toArray());
        } catch (SVPException e) {
            throw new SVPException(e.getMessage());
        }
    }

    public void saveDatosSistema() throws SVPException {
        ArrayList<Object> objetosEmpresa = new ArrayList<>();
        try {
            objetosEmpresa.add(this);
            objetosEmpresa.add(ControladorEmpresas.getInstance());
            IOSVP.getInstance().saveControladores(objetosEmpresa.toArray());
        } catch (SVPException e) {
            throw new SVPException(e.getMessage());
        }
    }

    public void readDatosSistema() throws SVPException {
        Object[] controladoresIO;
        try {
            controladoresIO = IOSVP.getInstance().readControladores();
            for (Object o : controladoresIO) {
                if (o instanceof SistemaVentaPasajes) {
                    instancia = (SistemaVentaPasajes) o;
                }
                if (o instanceof ControladorEmpresas) {
                    ControladorEmpresas.getInstance().setInstanciaPersistente((ControladorEmpresas) o);
                }
            }
        } catch (SVPException e) {
            throw new SVPException(e.getMessage());
        }
    }

    /* METODOS FIND */
    private Optional<Cliente> findCliente(IdPersona id) {
        /*for (Cliente cliente : clientes) {
            if (cliente.getIdPersona().equals(id)) {
                return Optional.of(cliente);
            }
        }
        return Optional.empty();
         */
        return clientes.stream()
                .filter(c -> c.getIdPersona().equals(id))
                .findFirst();
    }

    private Optional<Venta> findVenta(String idDocumento, TipoDocumento tipoDocumento) {
        /*for (Venta venta : ventas) {
            if (venta.getIdDocumento().equals(idDocumento) && venta.getTipo().equals(tipoDocumento)) {
                return Optional.of(venta);
            }
        }
        return Optional.empty();

         */
        return ventas.stream()
                .filter(v -> v.getIdDocumento().equals(idDocumento)
                        && v.getTipo().equals(tipoDocumento))
                .findFirst();
    }

    private Optional<Viaje> findViaje(String fecha, String hora, String patenteBus) {
        /*for (Viaje viaje : viajes) {
            if (viaje.getFecha().toString().equals(fecha)
                    && viaje.getHora().toString().equals(hora)
                    && viaje.getBus().getPatente().equals(patenteBus)) {
                return Optional.of(viaje);
            }
        }
        return Optional.empty();
       */
        return viajes.stream()
                .filter(v -> v.getFecha().toString().equals(fecha)
                        && v.getHora().toString().equals(hora) &&
                        v.getBus().getPatente().equals(patenteBus))
                .findFirst();
    }

    private Optional<Pasajero> findPasajero(IdPersona id) {
        /*for (Pasajero pasajero : pasajeros) {
            if (pasajero.getIdPersona().equals(id)) {
                return Optional.of(pasajero);
            }
        }
        return Optional.empty();
         */
        return pasajeros.stream()
                .filter(p -> p.getIdPersona().equals(id))
                .findFirst();

    }


    public String[] pasajesAlImprimir(String idDocumento, TipoDocumento tipo) {

        Optional<Venta> venta = findVenta(idDocumento, tipo);

        if (venta.isEmpty()) {
            return new String[0];
        }

        Pasaje[] pasajes = venta.get().getPasajes();

        return Arrays.stream(pasajes)
                .map(p -> "NUMERO DE PASAJE : " + p.getNumero() + "\n" +
                        "FECHA DEL VIAJE : " + p.getViaje().getFecha().toString() + "\n" +
                        "HORA DEL VIAJE : " + p.getViaje().getHora().toString() + "\n" +
                        "PATENTE BUS : " + p.getViaje().getBus().getPatente() + "\n" +
                        "ASIENTO : " + p.getAsiento() + "\n" +
                        "RUT - PASAPORTE : " + p.getPasajero().getIdPersona().toString() + "\n" +
                        "NOMBRE PASAJERO : " + p.getPasajero().getNombreCompleto())
                .toArray(String[]::new);


        //String[] pasajesString = new String[pasajes.length];

        /*for (int i = 0; i < pasajes.length; i++) {
            Pasaje pasaje = pasajes[i];

            pasajesString[i] = "NUMERO DE PASAJE : " + pasaje.getNumero() + "\n" +
                    "FECHA DEL VIAJE : " + pasaje.getViaje().getFecha().toString() + "\n" +
                    "HORA DEL VIAJE : " + pasaje.getViaje().getHora().toString() + "\n" +
                    "PATENTE BUS : " + pasaje.getViaje().getBus().getPatente() + "\n" +
                    "ASIENTO : " + pasaje.getAsiento() + "\n" +
                    "RUT - PASAPORTE : " + pasaje.getPasajero().getIdPersona().toString() + "\n" +
                    "NOMBRE PASAJERO : " + pasaje.getPasajero().getNombreCompleto();
        }
        return pasajesString;

         */
    }
}
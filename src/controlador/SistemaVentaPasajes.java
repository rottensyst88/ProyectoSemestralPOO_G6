package controlador;

import modelo.*;
import utilidades.*;
import excepciones.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class SistemaVentaPasajes {
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

    private DateTimeFormatter fechaFormateada = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private DateTimeFormatter horaFormateada = DateTimeFormatter.ofPattern("HH:mm");

    public void createCliente(IdPersona id, Nombre nom, String fono, String email) throws SistemaVentaPasajesException {

        Cliente c = new Cliente(id, nom, email);
        c.setTelefono(fono);

        if (findCliente(id).isEmpty()) {
            clientes.add(c);
        } else {
            throw new SistemaVentaPasajesException("Ya existe cliente con el ID indicado!");
        }
    }

    public void createPasajero(IdPersona id, Nombre nom, String fono, Nombre nomContacto, String
            fonoContacto) throws SistemaVentaPasajesException {

        Pasajero p = new Pasajero(id, nom, fonoContacto);
        p.setNomContacto(nomContacto);
        p.setTelefono(fono);

        if (findPasajero(id).isEmpty()) {
            pasajeros.add(p);
        } else {
            throw new SistemaVentaPasajesException("Ya existe pasajero con el ID indicado!");
        }
    }

    public void createViaje(LocalDate fecha, LocalTime hora, int precio, int duracion, String patBus, IdPersona[]
            idTripulantes, String[] nomComunas) throws SistemaVentaPasajesException {

        Optional<Bus> bus = ControladorEmpresas.getInstance().findBus(patBus);
        Auxiliar aux;
        Conductor con = null;

        if (bus.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe bus con la patente indicada");
        }


        if (ControladorEmpresas.getInstance().findAuxiliar(idTripulantes[0], bus.get().getEmp().getRut()).isEmpty()) {
            throw new SistemaVentaPasajesException("No existe auxiliar con el ID indicado en la empresa con el RUT indicado");
        } else {
            aux = ControladorEmpresas.getInstance().findAuxiliar(idTripulantes[0], bus.get().getEmp().getRut()).get();
        }

        if (idTripulantes.length == 2) {
            if (ControladorEmpresas.getInstance().findConductor(idTripulantes[1], bus.get().getEmp().getRut()).isPresent()) {
                con = ControladorEmpresas.getInstance().findConductor(idTripulantes[1], bus.get().getEmp().getRut()).get();
            } else {
                throw new SistemaVentaPasajesException("No existe conductor con el ID indicado en la empresa con el RUT indicado");
            }
        }

        if (ControladorEmpresas.getInstance().findTerminalPorComuna(nomComunas[0]).isEmpty()) {
            throw new SistemaVentaPasajesException("No existe terminal de salida en la comuna indicada");
        }
        Terminal tSalida = ControladorEmpresas.getInstance().findTerminalPorComuna(nomComunas[0]).get();

        if (ControladorEmpresas.getInstance().findTerminalPorComuna(nomComunas[1]).isEmpty()) {
            throw new SistemaVentaPasajesException("No existe terminal de llegada en la comuna indicada");
        }
        Terminal tEntrada = ControladorEmpresas.getInstance().findTerminalPorComuna(nomComunas[1]).get();


        Viaje viaje = new Viaje(fecha, hora, precio, duracion, bus.get(), aux, con, tSalida, tEntrada);

        if (idTripulantes.length == 3) {
            if (ControladorEmpresas.getInstance().findConductor(idTripulantes[2], bus.get().getEmp().getRut()).isPresent()) {
                con = ControladorEmpresas.getInstance().findConductor(idTripulantes[2], bus.get().getEmp().getRut()).get();
                viaje.addConductor(con);
            } else {
                throw new SistemaVentaPasajesException("No existe conductor con el ID indicado en la empresa con el RUT indicado");
            }
        }

        if (findViaje(fecha.toString(), hora.toString(), patBus).isEmpty()) {
            viajes.add(viaje);
        } else {
            throw new SistemaVentaPasajesException("Ya existe pasaje viaje con fecha, hora y patente de bus indicados");
        }
    }

    public void iniciaVenta(String idDocumento, TipoDocumento tipo, LocalDate fecha, String comSalida, String comLlegada, IdPersona idCliente, int nroPasajes)
            throws SistemaVentaPasajesException {

        Optional<Venta> venta = findVenta(idDocumento, tipo);
        if (venta.isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe venta con ID y Tipo de Doc. indicado");
        }
        Optional<Cliente> cliente = findCliente(idCliente);
        if (cliente.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe un cliente con ID integrado!");
        }

        boolean centinela = false;

        for (Viaje viaje : viajes) {
            if (viaje.getNroAsientosDisponibles() >= nroPasajes && viaje.getFecha().equals(fecha)) {
                if (viaje.getTerminalLlegada().getDireccion().getComuna().equals(comLlegada)) {
                    if (viaje.getTerminalSAlida().getDireccion().getComuna().equals(comSalida)) {
                        centinela = true;
                    }
                }
            }
        }
        if (!centinela) {
            throw new SistemaVentaPasajesException("No existen viajes disponibles en la fecha y con terminales en las comunas de salida y llegada indicados");
        }

        Venta nuevaVenta = new Venta(idDocumento, tipo, fecha, cliente.get());
        ventas.add(nuevaVenta);
    }

    public String[][] getHorariosDisponibles(LocalDate fechaViaje, String comunaSalida, String comunaLlegada, int nroPasajes) {
        ArrayList<Viaje> viajesFecha = new ArrayList<>();

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
        Optional<Pasajero> pasajero = findPasajero(idPasajero);

        if (pasajero.isPresent()) {
            return Optional.of(pasajero.get().getNomContacto().getNombre());
        } else {
            return Optional.empty();
        }
    }

    public Optional<Integer> getMontoVenta(String idDocumento, TipoDocumento tipo) {
        Optional<Venta> venta = findVenta(idDocumento, tipo);
        if (venta.isPresent()) {
            return Optional.of(venta.get().getMonto());
        } else {
            return Optional.empty();
        }
    }

    public void vendePasaje(String idDoc, TipoDocumento tipo, LocalTime hora, LocalDate fecha,
                            String patBus, int asiento, IdPersona idPasajero) throws SistemaVentaPasajesException {

        String horaComoString = hora.toString();
        String fechaComoString = fecha.toString();
        Optional<Viaje> viaje = findViaje(fechaComoString, horaComoString, patBus);
        Optional<Venta> venta = findVenta(idDoc, tipo);
        Optional<Pasajero> pasajero = findPasajero(idPasajero);

        if (venta.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe venta con el ID y Tipo de Doc. indicados");
        }
        if (pasajero.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe pasajero con el ID indicado");

        }
        if (viaje.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe viaje con la fecha, hora y patente de bus indicados");
        }

        venta.get().createPasaje(asiento, viaje.get(), pasajero.get());
    }

    public void pagaVenta(String idDocumento, TipoDocumento tipo) throws SistemaVentaPasajesException {
        Optional<Venta> venta = findVenta(idDocumento, tipo);

        if (venta.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe venta con el ID y Tipo de Doc. indicados");
        }

        if (venta.get().getMontoPagado() != 0) {
            throw new SistemaVentaPasajesException("La venta ya fue pagada");
        }
        venta.get().pagaMonto();
    }

    public void pagaVenta(String idDocumento, TipoDocumento tipo, long nroTarjeta) throws SistemaVentaPasajesException {
        Optional<Venta> venta = findVenta(idDocumento, tipo);

        if (venta.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe venta con el ID y Tipo de Doc. indicados");
        }
        if (venta.get().getMontoPagado() != 0) {
            throw new SistemaVentaPasajesException("La venta ya fue pagada");
        }
        venta.get().pagaMonto(nroTarjeta);
    }

    public String[][] listVentas() {
        String[][] arregloVentas = new String[ventas.size()][7];

        if (arregloVentas.length == 0) {
            return new String[0][0];
        }

        for (int i = 0; i < ventas.size(); i++) {
            Venta venta = ventas.get(i);
            arregloVentas[i][0] = venta.getIdDocumento();
            arregloVentas[i][1] = venta.getTipo().toString();
            //DEBE SER EN FORMATO DD/MM/AAAA
            arregloVentas[i][2] = fechaFormateada.format(venta.getFecha());
            //REVISAR ID PERSONA debería dar RUT/PASAPORTE
            arregloVentas[i][3] = venta.getCliente().getIdPersona().toString();
            //REVISAR SI TRAE TRATAMIENTO
            arregloVentas[i][4] = venta.getCliente().getNombreCompleto().toString(); // TODO Arreglar error!
            //pasando int a String
            String stringCantBoletos = "" + venta.getPasajes().length;
            arregloVentas[i][5] = stringCantBoletos;
            //convertir monto INT a String
            String stringTotalVenta = "" + venta.getMonto();
            arregloVentas[i][6] = stringTotalVenta;

        }
        return arregloVentas;
    }

    public String[][] listViajes() {
        String[][] arregloViajes = new String[viajes.size()][8];

        if (viajes.isEmpty()) {
            return new String[0][0];
        }

        for (int i = 0; i < viajes.size(); i++) {
            Viaje viaje = viajes.get(i);
            arregloViajes[i][0] = fechaFormateada.format(viaje.getFecha());
            arregloViajes[i][1] = horaFormateada.format(viaje.getHora());
            arregloViajes[i][2] = horaFormateada.format(viaje.getFechaHoraTermino().toLocalTime());
            arregloViajes[i][3] = String.valueOf(viaje.getPrecio());
            arregloViajes[i][4] = String.valueOf(viaje.getNroAsientosDisponibles());
            arregloViajes[i][5] = viaje.getBus().getPatente();
            arregloViajes[i][6] = viaje.getTerminalSAlida().getDireccion().getComuna().toUpperCase();
            arregloViajes[i][7] = viaje.getTerminalLlegada().getDireccion().getComuna().toUpperCase();
        }

        return arregloViajes;
    }

    public String[][] listPasajerosViaje(LocalDate fecha, LocalTime hora, String patBus) throws SistemaVentaPasajesException {

        Optional<Viaje> viajes = findViaje(fecha.toString(),hora.toString(),patBus);

        if(viajes.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe viaje con la fecha, hora y patente de bus indicadas");
        }

        String[][] arregloPasajeros = new String[pasajeros.size()][5];

        if (arregloPasajeros.length == 0) {
            return new String[0][0];
        }

        for (Venta venta : ventas){
            for(Pasaje pasaje : venta.getPasajes()){

            }
        }

        for (int i = 0; i < pasajeros.size(); i++) {
            Pasajero pasajero = pasajeros.get(i);

            for (Venta venta : ventas){
                for(Pasaje pasaje : venta.getPasajes()){
                    if (pasaje.getPasajero().getIdPersona().equals(pasajero.getIdPersona())){
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

    private Optional<Cliente> findCliente(IdPersona id) {
        for (Cliente cliente : clientes) {
            if (cliente.getIdPersona().equals(id)) {
                return Optional.of(cliente);
            }
        }
        return Optional.empty();
    }

    private Optional<Venta> findVenta(String idDocumento, TipoDocumento tipoDocumento) {
        for (Venta venta : ventas) {
            if (venta.getIdDocumento().equals(idDocumento) && venta.getTipo().equals(tipoDocumento)) {
                return Optional.of(venta);
            }
        }
        return Optional.empty();
    }

    private Optional<Viaje> findViaje(String fecha, String hora, String patenteBus) {
        for (Viaje viaje : viajes) {
            if (viaje.getFecha().toString().equals(fecha) && viaje.getHora().toString().equals(hora) && viaje.getBus().getPatente().equals(patenteBus)) {
                return Optional.of(viaje);
            }
        }
        return Optional.empty();
    }

    private Optional<Pasajero> findPasajero(IdPersona id) {
        for (Pasajero pasajero : pasajeros) {
            if (pasajero.getIdPersona().equals(id)) {
                return Optional.of(pasajero);
            }
        }
        return Optional.empty();
    }

    public String[] pasajesAlImprimir(String idDocumento, TipoDocumento tipo) {

        Optional<Venta> venta = findVenta(idDocumento, tipo);
        Pasaje[] pasajes = venta.get().getPasajes();

        if (pasajes == null) {
            return new String[0];
        }

        String[] pasajesString = new String[pasajes.length];

        for (int i = 0; i < pasajes.length; i++) {
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
    }
}
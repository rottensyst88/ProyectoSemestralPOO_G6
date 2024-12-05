package controlador;

import modelo.*;
import utilidades.*;
import excepciones.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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

        if (findViaje(fecha.toString(), hora.toString(), patBus).isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe viaje con fecha, hora y patente de bus indicados");
        }

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

        boolean centinela = viajes.stream().anyMatch
                (viaje -> viaje.getNroAsientosDisponibles() >= nroPasajes &&
                        viaje.getFecha().equals(fecha) &&
                        viaje.getTerminalLlegada().getDireccion().getComuna().equals(comLlegada) &&
                        viaje.getTerminalSAlida().getDireccion().getComuna().equals(comSalida));

        if (!centinela) {
            throw new SistemaVentaPasajesException("No existen viajes disponibles en la fecha y con terminales en las comunas de salida y llegada indicados");
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

    }

    public Optional<Integer> getMontoVenta(String idDocumento, TipoDocumento tipo) {
        return findVenta(idDocumento, tipo).map(Venta::getMonto);

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

        return clientes.stream()
                .filter(c -> c.getIdPersona().equals(id))
                .findFirst();
    }

    private Optional<Venta> findVenta(String idDocumento, TipoDocumento tipoDocumento) {

        return ventas.stream()
                .filter(v -> v.getIdDocumento().equals(idDocumento)
                        && v.getTipo().equals(tipoDocumento))
                .findFirst();
    }

    private Optional<Viaje> findViaje(String fecha, String hora, String patenteBus) {
        return viajes.stream()
                .filter(v -> v.getFecha().toString().equals(fecha)
                        && v.getHora().toString().equals(hora) &&
                        v.getBus().getPatente().equals(patenteBus))
                .findFirst();
    }

    private Optional<Pasajero> findPasajero(IdPersona id) {

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



    }
}
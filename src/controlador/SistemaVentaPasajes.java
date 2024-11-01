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

    public static SistemaVentaPasajes getInstancia(){
        if (instancia == null){
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

    public void createCliente(IdPersona id, Nombre nom, String fono, String email) throws SistemaVentaPasajesException{

        Cliente c = new Cliente(id, nom, email);
        c.setTelefono(fono);

        if (findCliente(id).isEmpty()) {
            clientes.add(c);
        }else{
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
        }else{
            throw new SistemaVentaPasajesException("Ya existe pasajero con el ID indicado!");
        }
    }

    public void createViaje(LocalDate fecha, LocalTime hora, int precio, String patBus) throws SistemaVentaPasajesException{ //todo FALTA ARREGLARLO!
        Bus bus = findBus(patBus); //TODO Arreglar esto!
        if (bus == null) {
            throw new SistemaVentaPasajesException("No existe bus con la patente indicada");
        }

        Viaje viaje = new Viaje(fecha, hora, precio, bus);
        if (findViaje(fecha.toString(), hora.toString(), patBus).isEmpty()) {
            viajes.add(viaje);
        } else{
            throw new SistemaVentaPasajesException("Ya existe pasaje viaje con fecha, hora y patente de bus indicados");
        }
    }

    public void iniciaVenta(String idDocumento, TipoDocumento tipo, LocalDate fecha, IdPersona idCliente)
    throws SistemaVentaPasajesException {
        Optional<Venta> venta = findVenta(idDocumento, tipo);
        if (venta.isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe venta con ID y Tipo de Doc. indicado");
        }
        Optional<Cliente> cliente = findCliente(idCliente);
        if (cliente.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe un cliente con ID integrado!");
        }
        Venta nuevaVenta = new Venta(idDocumento, tipo, fecha, cliente.get());
        ventas.add(nuevaVenta);
    }

    public String[][] getHorariosDisponibles(LocalDate fecha) {
        ArrayList<Viaje> viajesFecha = new ArrayList<>();

        //aquí busco si es que hay algún viaje con esa fecha en la lista de viajes
        //si es que hay lo agrego a un nuevo arraylist que cree para guardarlos ahi
        for (Viaje viaje : viajes) {
            if (viaje.getFecha().equals(fecha)) {
                viajesFecha.add(viaje);
            }
        }

        // si el arraylist esta vacío se retorna un arreglo vacio, segun las instrucciones
        if (viajesFecha.isEmpty()) {
            return new String[0][0];
        }

        String[][] horariosDisponibles = new String[viajesFecha.size()][4];

        //relleno el arreglo bidimensional mediante un ciclo for
        //accediendo  a los datos que me piden mediante viajesFecha.get(i)
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

    public String[] listAsientosDeViaje(LocalDate fecha, LocalTime hora,
                                        String patBus) {

        //ocupo toString(), para poder utilizar el metodo findViaje el cual solo recibe string

        String horaComoString = hora.toString();
        String fechaComoString = fecha.toString();
        Optional<Viaje> viaje = findViaje(fechaComoString, horaComoString, patBus);

        if (viaje.isEmpty()) {
            return new String[0];
        }

        String[][] infoSobreAsientos = viaje.get().getAsientos();

        String[] asientos = new String[infoSobreAsientos.length];

        for (int i = 0; i < asientos.length; i++) {
            asientos[i] = infoSobreAsientos[i][1];
        }
        return asientos;
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
                               String patBus, int asiento, IdPersona idPasajero) throws SistemaVentaPasajesException{

        String horaComoString = hora.toString();
        String fechaComoString = fecha.toString();
        Optional<Viaje> viaje = findViaje(fechaComoString, horaComoString, patBus);
        Optional<Venta> venta = findVenta(idDoc, tipo);
        Optional<Pasajero> pasajero = findPasajero(idPasajero);
        // Bus bus = findBus(patBus); //TODO arreglar!

        if (venta.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe venta con el ID y Tipo de Doc. indicados");
        }
        if (pasajero.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe pasajero con el ID indicado");

        }
        if (viaje.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe viaje con la fecha, hora y patente de bus indicados");
        }

        /*
        if (bus == null) {
            throw new SistemaVentaPasajesException("")
        }*/

        venta.get().createPasaje(asiento, viaje.get(), pasajero.get());
    }

    public void pagaVenta(String idDocumento, TipoDocumento tipo){

    }

    public void pagaVenta(String idDocumento, TipoDocumento tipo, long nroTarjeta){

    }

    public String[][] listVentas() {
        //Dado que el metodo devuelve un ARREGLO BIDIMENSIONAL, el mensaje apropiado en caso de existir ventas se debe desplegar desde el main
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
        String[][] arregloViajes = new String[viajes.size()][5];

        if (viajes.isEmpty()) {
            return new String[0][0];
        }

        for (int i = 0; i < viajes.size(); i++) {
            Viaje viaje = viajes.get(i);
            arregloViajes[i][0] = fechaFormateada.format(viaje.getFecha());
            arregloViajes[i][1] = horaFormateada.format(viaje.getHora());
            arregloViajes[i][2] = String.valueOf(viaje.getPrecio());
            arregloViajes[i][3] = String.valueOf(viaje.getNroAsientosDisponibles());
            arregloViajes[i][4] = viaje.getBus().getPatente();
        }

        return arregloViajes;
    }

    public String[][] listPasajerosViaje(LocalDate fecha, LocalTime hora, String patBus) throws SistemaVentaPasajesException{
        String[][] arregloPasajeros = new String[pasajeros.size()][5];

        if (arregloPasajeros.length == 0) {
            throw new SistemaVentaPasajesException("No existe viaje con la fecha, hora y patente de bus indicadas");
        }

        for (int i = 0; i < pasajeros.size(); i++) {
            Pasajero pasajero = pasajeros.get(i);
            arregloPasajeros[i][0] = "nulo"; //TODO dado el problema con relacionar el número del asiento con los demás datos del pasajero, esa parte estará omitida por el momntoBus bus = findBus(patBus);
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

    //TODO A PARTIR DE ABAJO, LOS METODOS DEBEN SER ELIMINADOS!!!
    /*
    public String[] pasajesAlImprimir(String idDocumento, TipoDocumento tipo) {

        Venta venta = findVenta(idDocumento, tipo);
        Pasaje[] pasajes = venta.getPasajes();

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

    private Bus findBus(String patente) {
        for (Bus bus : buses) {
            if (bus.getPatente().equals(patente)) {
                return bus;
            }
        }
        return null;
    }

    public boolean createBus(String patente, String marca, String modelo, int nroAsientos) {
        Bus bus = new Bus(patente, nroAsientos);
        bus.setMarca(marca);
        bus.setModelo(modelo);

        if (findBus(patente) == null) {
            buses.add(bus);
            return true;
        }
        return false;
    }
    */
}
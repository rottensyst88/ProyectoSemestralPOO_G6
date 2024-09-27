package uml_2;

import uml_1.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@SuppressWarnings({"CanBeFinal", "FieldMayBeFinal"})
public class SistemaVentaPasajes {

    // ATRIBUTOS DE LA CLASE SISTEMA_VENDE_PASAJES

    private ArrayList<Cliente> clientes = new ArrayList<>();
    private ArrayList<Pasajero> pasajeros = new ArrayList<>();
    private ArrayList<Viaje> viajes = new ArrayList<>();
    private ArrayList<Bus> buses = new ArrayList<>();
    private ArrayList<Venta> ventas = new ArrayList<>(); // REVISAR!

    private DateTimeFormatter fechaFormateada = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private DateTimeFormatter horaFormateada = DateTimeFormatter.ofPattern("HH/mm");

    // METODOS FIND

    private Cliente findCliente(IdPersona id) {
        for (Cliente cliente : clientes) {
            if (cliente.getIdPersona().equals(id)) {
                return cliente;
            }
        }
        return null;
    }

    private Pasajero findPasajero(IdPersona id) {
        for (Pasajero pasajero : pasajeros) {
            if (pasajero.getIdPersona().equals(id)) {
                return pasajero;
            }
        }
        return null;
    }

    private Bus findBus(String patente) {
        for (Bus bus : buses) {
            if (bus.getPatente().equals(patente)) {
                return bus;
            }
        }
        return null;
    }

    private Viaje findViaje(String fecha, String hora, String patenteBus) {
        for (Viaje viaje : viajes) {
            if (viaje.getFecha().toString().equals(fecha) && viaje.getHora().toString().equals(hora) && viaje.getBus().getPatente().equals(patenteBus)) {
                return viaje;
            }
        }
        return null;
    }

    private Venta findVenta(String idDocumento, TipoDocumento tipoDocumento) {
        for (Venta venta : ventas) {
            //Venta venta = ventas.get(i); TODO REVISAR ESTO, NO SE SI ESTA BIEN.
            if (venta.getIdDocumento().equals(idDocumento) && venta.getTipo().equals(tipoDocumento)) {
                return venta;
            }
        }
        return null;
    }

    // FIN METODOS FIND, TOTAL DE METODOS -> 5/5

    // METODOS GET

    public int getMontoVenta(String idDocumento, TipoDocumento tipo) {
        Venta venta = findVenta(idDocumento, tipo);
        if (venta != null) {
            return venta.getMonto();
        } else return 0;
    }

    public String getNombrePasajero(IdPersona idPasajero) {
        Pasajero pasajero = findPasajero(idPasajero);
        if (pasajero != null) {
            return pasajero.getNomContacto().getNombre();
        } else return null;
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

    // FIN METODOS GET, TOTAL METODOS -> 3/3

    // METODOS CREATE

    public boolean createCliente(IdPersona id, Nombre nom, String fono, String email) {

        Cliente c = new Cliente(id, nom, email);
        c.setTelefono(fono);

        if (findCliente(id) == null) {
            clientes.add(c);
            return true;
        }
        return false;
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean createPasajero(IdPersona id, Nombre nom, String fono, Nombre nomContacto, String
            fonoContacto) { // TODO Implementar!

        Pasajero p = new Pasajero(id, nom, fonoContacto);
        p.setNomContacto(nomContacto);
        p.setTelefono(fono);

        if (findPasajero(id) == null) {
            pasajeros.add(p);
            return true;
        }
        return false;
    }

    public boolean createViaje(LocalDate fecha, LocalTime hora, int precio, String patBus) {
        Bus bus = findBus(patBus);
        if (bus == null) {
            return false;
        }

        Viaje viaje = new Viaje(fecha, hora, precio, bus);
        if (findViaje(fecha.toString(), hora.toString(), patBus) == null) {
            viajes.add(viaje);
            return true;
        }

        return false;
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

    // FIN METODOS CREATE, TOTAL METODOS -> 4/4

    // METODOS LIST

    public String[] listAsientosDeViaje(LocalDate fecha, LocalTime hora,
                                        String patBus) {

        //ocupo toString(), para poder utilizar el metodo findViaje el cual solo recibe string

        String horaComoString = hora.toString();
        String fechaComoString = fecha.toString();
        Viaje viaje = findViaje(fechaComoString, horaComoString, patBus);

        if (viaje == null) {
            return new String[0];
        }

        //obtengo la información sobre los asientos desde la clase Viaje
        String[][] infoSobreAsientos = viaje.getAsientos();

        String[] asientos = new String[infoSobreAsientos.length];

        for (int i = 0; i < asientos.length; i++) {
            /* Cabe aclarar que no sé en qué orden está el arreglo bidimensional
            getAsientos, no sé si está primero el número o el estado, pero eso se puede
            arreglar luego
            Tampoco entiendo si debo agregar solo el estado o también el número, pero eso
            es facil de cambiar
             */
            // String numAsiento = infoSobreAsientos[i][0];
            // String estadoAsiento = infoSobreAsientos[i][1];

            asientos[i] = infoSobreAsientos[i][1];
        }
        return asientos;
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
            arregloVentas[i][4] = venta.getCliente().getNombreCompleto().toString();
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

        if (arregloViajes.length == 0) {
            return new String[0][0];
        }

        for (int i = 0; i < viajes.size(); i++) {
            Viaje viaje = viajes.get(i);
            arregloViajes[i][0] = fechaFormateada.format(viaje.getFecha());

            arregloViajes[i][1] = horaFormateada.format(viaje.getHora());
            String stringPrecio = "" + viaje.getPrecio();
            arregloViajes[i][2] = stringPrecio;
            arregloViajes[i][3] = viaje.getAsientos().toString(); //TODO ARREGLAR!
            arregloViajes[i][4] = viaje.getBus().getPatente();
        }
        return arregloViajes;
    }

    public String[][] listPasajeros(LocalDate fecha, LocalTime hora, String patBus) {
        String[][] arregloPasajeros = new String[pasajeros.size()][5];

        if (arregloPasajeros.length == 0) {
            return new String[0][0];
        }

        for (int i = 0; i < pasajeros.size(); i++) {
            Pasajero pasajero = pasajeros.get(i);
            //dado el problema con relacionar el número del asiento con los demás datos del pasajero, esa parte estará omitida por el momnto
            arregloPasajeros[i][0] = "0";
            arregloPasajeros[i][1] = pasajero.getIdPersona().toString();
            arregloPasajeros[i][2] = pasajero.getNombreCompleto().toString();
            arregloPasajeros[i][3] = pasajero.getNomContacto().toString();
            arregloPasajeros[i][4] = pasajero.getFonoContacto();
        }
        return arregloPasajeros;
    }

    // FIN METODOS LIST, TOTAL METODOS 4/4

    // METODO VENDE_PASAJE

    public boolean vendePasaje(String idDoc, TipoDocumento tipo, LocalTime hora, LocalDate fecha,
                               String patBus, int asiento, IdPersona idPasajero) {
        /*
        -Se debe verificar si existe una venta con el idDoc y tipo dados
        -Verificar si existe un viaje, y si existe el pasajero
         */
        Venta venta = findVenta(idDoc, tipo);
        if (venta == null) {
            return false;
            //No existe venta
        }

        Pasajero pasajero = findPasajero(idPasajero);
        if (pasajero == null) {
            return false;
            // no existe un cliente con este id
        }

        //ocupo toString(), para poder utilizar el metodo findViaje el cual solo recibe string
        String horaComoString = hora.toString();
        String fechaComoString = fecha.toString();
        Viaje viaje = findViaje(fechaComoString, horaComoString, patBus);
        if (viaje == null) {
            return false;
            //no existe un viaje
        }
        Bus bus = findBus(patBus);
        if (bus == null) {
            return false;
            //no existe un Bus
        }

        Pasaje nuevoPasaje = new Pasaje(asiento, viaje, pasajero, venta);

        ventas.add(nuevoPasaje.getVenta());
        return true;
    }

    // FIN METODO VENDE_PASAJE

    // METODO INICIA_VENTA

    public boolean iniciaVenta(String idDocumento, TipoDocumento tipo, LocalDate fecha, IdPersona idCliente) {
        //primero debo verificar si es que el cliente existe con su id
        //luego verificar si es que existe la venta con esa idDocumento

        Venta venta = findVenta(idDocumento, tipo);
        if (venta != null) {
            return false;
            //ya existe una venta con esos datos
        }

        Cliente cliente = findCliente(idCliente);
        if (cliente == null) {
            return false;
            // no existe un cliente con este id
        }
        Venta nuevaVenta = new Venta(idDocumento, tipo, fecha, cliente);
        ventas.add(nuevaVenta);
        return true;
    }

    // FIN METODO INICIA_VENTA

    //TOTAL METODOS -> 18/18

    public String[] pasajesAlImprimir(String idDocumento, TipoDocumento tipo) {

        Venta venta = findVenta(idDocumento, tipo);
        Pasaje[] pasajes = venta.getPasajes();

        for (Pasaje pasaje : pasajes) {
            return new String[]{String.valueOf(pasaje.getNumero()),
                    pasaje.getViaje().getFecha().toString(),
                    pasaje.getViaje().getHora().toString(),
                    pasaje.getViaje().getBus().getPatente(),
                    String.valueOf(pasaje.getAsiento()),
                    pasaje.getPasajero().getIdPersona().toString(),
                    String.valueOf(pasaje.getPasajero().getNombreCompleto())};
        }

        return new String[0];
    }
}
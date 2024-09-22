package uml_2;
import uml_1.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SistemaVentaPasajes {

    // ATRIBUTOS DE LA CLASE SISTEMA_VENDE_PASAJES

    ArrayList<Cliente> clientes = new ArrayList<>();
    ArrayList<Pasajero> pasajeros = new ArrayList<>();
    ArrayList<Viaje> viajes = new ArrayList<>();
    ArrayList<Bus> buses = new ArrayList<>();
    ArrayList<Venta> ventas = new ArrayList<>(); // REVISAR!

    DateTimeFormatter fechaFormateada = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter horaFormateada = DateTimeFormatter.ofPattern("HH/mm");

    // METODOS FIND

    public Cliente findCliente(IdPersona id) {
        for (Cliente cliente : clientes) {
            if (cliente.getIdPersona().equals(id)) {
                return cliente;
            }
        }
        return null;
    }

    public Pasajero findPasajero(IdPersona IdPersona) {
        for (Pasajero pasajero : pasajeros) {
            if (pasajero.getIdPersona().equals(IdPersona)) {
                return pasajero;
            }
        }
        return null;
    }

    public Bus findBus(String patente) {
        for (Bus bus : buses) {
            if (bus.getPatente().equals(patente)) {
                return bus;
            }
        }
        return null;
    }

    public Viaje findViaje(String fecha, String hora, String patenteBus) {
        for (Viaje viaje : viajes) {
            if (viaje.getFecha().toString().equals(fecha) && viaje.getHora().toString().equals(hora) && viaje.getBus().getPatente().equals(patenteBus)) {
                return viaje;
            }
        }
        return null;
    }

    public Venta findVenta(String idDocumento, TipoDocumento tipoDocumento) {
        for (Venta venta : ventas) {
            if (venta.getIdDocumento().equals(idDocumento) && venta.getTipo().equals(tipoDocumento)) {
                return venta;
            }
        }
        return null;
    }

    public boolean createCliente(IdPersona id, Nombre nom, String fono, String email) {

        Cliente c = new Cliente(id, nom, email);
        c.setTelefono(fono);

        if (findCliente(id) == null) {
            clientes.add(c);
            return true;
        }

        return clientes.add(c);
    }
    //no estoy seguro de donde agregar el ArrayList donde van los clientes y pasajeros, dado que no sé si esta implementda por el otro lado la asociacion
    //NO ESTOY SEGURO si los errores por parametros de los objetos cliente y psajeros son error mio o está mal implementada la herencia en sus respectivas clases


    public boolean createPasajero(IdPersona id, Nombre nom, String fono, Nombre nomContacto, String
            fonoContacto) {

        Pasajero p = new Pasajero(id, nom, fonoContacto);
        p.setNomContacto(nomContacto);
        p.setTelefono(fono);

        if (findPasajero(id) == null) {
            pasajeros.add(p);
            return true;
        }
        return false;
    }

    //No estoy del to do seguro de si  viaje.getPatente está bien usado, se verá cuando esté la clase Viaje
    public boolean createViaje(LocalDate fecha, LocalTime hora, int precio, String patBus) {
        Bus bus = findBus(patBus);
        if (bus == null){
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

        //obtengo la informacion sobre los asientos desde la clase viaje
        String[][] infoSobreAsientos = viaje.getAsientos();

        String[] asientos = new String[infoSobreAsientos.length];

        for (int i = 0; i < infoSobreAsientos.length; i++) {
            /* Cabe aclarar que no se en que orden esta el arreglo bidimensional
            getAsientos, no se si esta primero el numero o el estado, pero eso se puede
            arreglar luego
            Tampoco entiendo si debo agregar solo el estado o tambien el numero pero eso
            es facil de cambiar
             */
            String numAsiento = infoSobreAsientos[i][0];
            String estadoAsiento = infoSobreAsientos[i][1];

            asientos[i] = "Asiento " + numAsiento + ": " + estadoAsiento;
        }
        return asientos;
    }

    public String[][] listVentas() {
        //Dado que el metodo devuelve un ARREGLO BIDIMENSIONAL, el mensaje apropiado en caso de n existir ventas se debe desplegar desde el main
        String[][] arregloVentas = new String[ventas.size()][7];
        for (int i=0; i<ventas.size(); i++) {
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
            String stringCantBoletos =""+ venta.getPasajes().length;
            arregloVentas[i][5] = stringCantBoletos;
            //convertir monto INT a String
            String stringTotalVenta = "" + venta.getMonto();
            arregloVentas[i][6] = stringTotalVenta;

        }
        return arregloVentas;
    }
    public String[][] listViajes(){
        String[][] arregloViajes = new String[viajes.size()][5];
        for (int i=0; i<viajes.size(); i++) {
            Viaje viaje = viajes.get(i);
            arregloViajes[i][0] = fechaFormateada.format(viaje.getFecha());

            arregloViajes[i][1] = horaFormateada.format(viaje.getHora());
            String stringPrecio = "" + viaje.getPrecio();
            arregloViajes[i][2] = stringPrecio;
            arregloViajes[i][3] = viaje.getAsientos().toString();
            arregloViajes[i][4] = viaje.getBus().getPatente();

        }

        return arregloViajes;
    }

    public String[][] listPasajeros(LocalDate fecha, LocalTime hora, String patBus) {
        String[][] arregloPasajeros = new String[pasajeros.size()][5];
        //Recorrer los viajes para encontrar el que busco
        for (int i = 0; i < pasajeros.size(); i++) {
            Viaje viaje = viajes.get(i);
            //Si el viaje es igual, retorna lo requerido
            if (viaje.getFecha().equals(fecha) && viaje.getHora().equals(hora) && viaje.getBus().getPatente().equals(patBus)) {
                arregloPasajeros[i][0] =
                arregloPasajeros[i][1] =
            }

        }
    }




    // FIN METODOS LIST, TOTAL METODOS 4/4

    // METODO VENDE_PASAJE

    public boolean vendePasaje(String idDoc, TipoDocumento tipo, LocalTime hora, LocalDate fecha,
                               String patBus, int asiento, IdPersona idPasajero) {
        /*
        -Se debe verificar si existe una venta con el idDoc y tipo dados
        -Verificar si existe un viaje
        -Verificar si existe el pasajero
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
}
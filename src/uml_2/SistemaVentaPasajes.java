package uml_2;
import uml_1.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SistemaVentaPasajes {
    ArrayList<Cliente> clientes = new ArrayList<>();
    ArrayList<Pasajero> pasajeros = new ArrayList<>();
    ArrayList<Viaje> viajes = new ArrayList<>();
    ArrayList<Bus> buses = new ArrayList<>();
    ArrayList<Venta> ventas = new ArrayList<>(); // REVISAR!

    DateTimeFormatter fechaFormateada = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter horaFormateada = DateTimeFormatter.ofPattern("HH/mm");

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
    //arreglar
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
    //revisar cuando esté la clase ventas
    public String[][] listVentas(){
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
    public String[][] listPasajeros(LocalDate fecha, LocalTime hora, String patBus){
    String[][] arregloPasajeros = new String[pasajeros.size()][5];
    return arregloPasajeros;
}
}
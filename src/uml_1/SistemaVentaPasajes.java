package uml_1;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class SistemaVentaPasajes {
    ArrayList<Cliente> clientes = new ArrayList<>();
    ArrayList<Pasajero> pasajeros = new ArrayList<>();
    ArrayList<Viaje> viajes = new ArrayList<>();
    ArrayList<Bus> buses = new ArrayList<>();

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
            if (viaje.getFecha().equals(fecha) && viaje.getHora().equals(hora) && viaje.getPatente.equals(patBus)) {
                return viaje;
            }
        }
        return null;
    }


    //revisar luego de la creacion de la clase Venta
    public Venta findVenta(String idDocumento, TipoDocumento tipoDocumento) {
        for (Venta venta : ventas) {
            if (venta.getidDocumento().equals(idDocumento) && venta.gettipoDocumento().equals(tipoDocumento)) {
                return venta;
            }
        }
        return null;
    }

    public boolean createCliente(IdPersona id, Nombre nom, String fono, String email) {

        Cliente c = new Cliente(id, nom, fono, email);

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

        Pasajero p = new Pasajero(id, nom, fono, nomContacto, fonoContacto);
        if (findPasajero(IdPersona) == null) {
            pasajeros.add(p);
            return true;
        }
        return false;
    }

    //No estoy del to do seguro de si  viaje.getPatente está bien usado, se verá cuando esté la clase Viaje
    public boolean createViaje(LocalDate fecha, LocalTime hora, int precio, String patBus) {
        Viaje viaje = new Viaje(fecha, hora, precio, patBus);
        if (findViaje(fecha, hora, patenteBus) == null) {
            viajes.add(viaje);
            return true;
        }
        return false;
    }


    public boolean createBus(String patente, String marca, String modelo, int nroAsientos) {
        Bus bus = new bus(patente, marca, modelo, nroAsientos);
        if (findBus(patente) == null) {
            buses.add(bus);
            return true;
        }
        return false;
    }
    //revisar cuando esté la clase ventas
    private String[][] listVentas(){
        Object[][] arregloVentas = new Object[Ventas.size()][7];
        for (int i=0; i<ventas.size(); i++) {


        }
    }

}
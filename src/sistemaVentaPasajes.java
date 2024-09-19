import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class sistemaVentaPasajes {

    private List<Cliente> clientes ;
    private List<Venta> ventas;
    private List<Viaje> viajes;
    private List<Pasajero> pasajeros ;
    private List<Bus> buses;

    public sistemaVentaPasajes() {
        clientes = new ArrayList<>();
        ventas = new ArrayList<>();
        viajes = new ArrayList<>();
        pasajeros = new ArrayList<>();
        buses = new ArrayList<>();
    }


    public int getMontoVenta(String idDocumento, TipoDocumento tipo){

        Venta venta = findVenta(idDocumento, tipo);
        if(venta != null){
            return venta.getMonto();
        } else  return 0;
    }

    public String getNombrePasajero(idPersona idPasajero){

        Pasajero pasajero = findPasajero(idPasajero);
        if(pasajero != null){
            return pasajero.getNomContacto();
        } else return null;
    }

    public boolean iniciaVenta(String idDocumento, TipoDocumento tipo, LocalDate fecha, idPersona idCliente){
        //primero debo verificar si es que el cliente existe con su id
        //luego verificar si es que existe la venta con esa idDocumento

        Venta venta = findVenta(idDocumento, tipo);
        if(venta != null){
            return false;
            //ya existe una venta con esos datos
        }

        Cliente cliente = findCliente(idCliente);
        if(cliente == null){
            return false;
            // no existe un cliente con este id
        }
        Venta nuevaVenta = new Venta(idDocumento, tipo, fecha, cliente);
        ventas.add(nuevaVenta);
        return true;
    }



    public String[]listAsientosDeViaje(LocalDate fecha, LocalTime hora,
                                       String patBus) {
        //ocupo toString(), para poder utilizar el metodo findViaje el cual solo recibe string
        //aun no se si esta bien aplicado el tostring lo debo revisar
        Viaje viaje = findViaje(fecha.toString(), hora.toString(), patBus);

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


    public boolean vendePasaje(String idDoc, TipoDocumento tipo, LocalTime hora, LocalDate fecha,
                               String patBus, int asiento, idPersona idPasajero){
        /*
        -Se debe verificar si existe una venta con el idDoc y tipo dados
        -Verificar si existe un viaje
        -Verificar si existe el pasajero
         */
        Venta venta = findVenta(idDoc, tipo);
        if(venta == null){
            return false;
            //No existe venta
        }

        Pasajero pasajero = findPasajero(idPasajero);
        if(pasajero == null){
            return false;
            // no existe un cliente con este id
        }

        //ocupo toString(), para poder utilizar el metodo findViaje el cual solo recibe string
        //aun no se si esta bien aplicado el tostring lo debo revisar
        Viaje viaje = findViaje(fecha.toString(), hora.toString(), patBus);
        if(viaje == null){
            return false;
            //no existe un viaje
        }

        Bus bus = findBus(patBus);
        if(bus == null){
            return false;
            //no existe un Bus
        }

        Pasaje nuevoPasaje = new Pasaje(asiento, viaje, pasajero, venta);

        //no se como asociarlo a venta
        //venta.addPasaje(nuevoPasaje);
        //venta.pasajes.add(nuevoPasaje);
        return true;

    }

    public String[][] getHorariosDisponibles(LocalDate fecha){
        List<Viaje> viajesFecha = new ArrayList<>();

        //aqui busco si es que hay algun viaje con esa fecha en la lista de viajes
        //si es que hay lo agrego a un nuevo arrayist que cree para guardarlos ahi
        for (Viaje viaje : viajes) {
            if (viaje.getFecha().equals(fecha)) {
                viajesFecha.add(viaje);
            }
        }

        // si el arraylist esta vacio se retorna un arreglo vacio, segun las instrucciones
        if (viajesFecha.isEmpty()) {
            return new String[0][0];
        }

        String[][] horariosDisponibles = new String[viajesFecha.size()][4];

        //relleno el arreglo bidimensional mediante un ciclo for
        //accediento  a los datos que me piden mediante viajesfecha.get(i)
        for (int i = 0; i < viajesFecha.size(); i++) {
            Viaje viaje = viajesFecha.get(i);

            fechaFormateada =
            // aun me falta llenar el arreglo bidimensional con los datos requeridos

        }

        return horariosDisponibles;
    }



}

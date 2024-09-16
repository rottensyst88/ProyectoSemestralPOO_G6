import java.time.LocalDate;
import java.time.LocalTime;

public class sistemaVentaPasajes {

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

    }

    public boolean vendePasaje(String idDoc, LocalTime hora, LocalDate fecha,
                               String patBus, int asiento, idPasajero idPersona){


    }

    public String[][] getHorariosDisponibles(LocalDate fecha){
        //



    }





}


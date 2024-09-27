package uml_2;
import uml_1.*;

import java.time.LocalDate;
import java.util.ArrayList;

@SuppressWarnings({"CanBeFinal", "FieldMayBeFinal"})
public class Venta {
    private String idDocumento;
    private TipoDocumento tipo;
    private LocalDate fecha;
    ArrayList<Pasaje> pasajes;

    private Cliente cliente;

    public Venta(String id, TipoDocumento tipo, LocalDate fec, Cliente cli) {
        idDocumento = id;
        this.tipo = tipo;
        fecha = fec;
        cliente = cli;
        cliente.addVenta(this);

        pasajes = new ArrayList<>();
    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public TipoDocumento getTipo(){
        return tipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void createPasaje(int asiento, Viaje viaje, Pasajero pasajero){
        pasajes.add(new Pasaje(asiento, viaje, pasajero, this));
    }

    public Pasaje[] getPasajes(){
        return pasajes.toArray(new Pasaje[pasajes.size()]);
    }

    public int getMonto(){
        int valor_monto = 0;
        for (Pasaje pasaje : pasajes) {
            valor_monto += pasaje.getViaje().getPrecio();
        }
        return valor_monto;
    }


}
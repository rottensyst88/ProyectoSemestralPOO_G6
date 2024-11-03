package modelo;
import utilidades.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Venta {
    private String idDocumento;
    private TipoDocumento tipo;
    private LocalDate fecha;

    private List<Pasaje> pasajes = new ArrayList<>();
    private Pago pago;
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
        return pasajes.toArray(new Pasaje[0]);
    }

    public int getMonto(){
        int valor_monto = 0;
        for (Pasaje pasaje : pasajes) {
            valor_monto += pasaje.getViaje().getPrecio();
        }
        return valor_monto;
    }
}
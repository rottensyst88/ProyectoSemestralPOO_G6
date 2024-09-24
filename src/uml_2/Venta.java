package uml_2;

import uml_1.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class Venta {
    private String idDocumento;
    private TipoDocumento tipo;
    private LocalDate fecha;
    private Cliente cliente;

    ArrayList<Pasaje> pasajes = new ArrayList<>();

    public Venta(String id, TipoDocumento tipo, LocalDate fec, Cliente cli) {
        idDocumento = id;
        this.tipo = tipo;
        fecha = fec;
        cliente = cli;
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
        // System.out.println(pasajes.size()); USADO PARA DEBUG!

        Pasaje[] arregloPasajes = new Pasaje[pasajes.size()];
        return pasajes.toArray(arregloPasajes);
    }

    public int getMonto(){
        int valor_monto = 0;
        for (Pasaje pasaje : pasajes) {
            valor_monto += pasaje.getViaje().getPrecio();
        }
        return valor_monto;
    }
}
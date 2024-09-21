package uml_2;

import uml_1.*;

import java.lang.Math;

public class Pasaje {
    private long numero;
    private int asiento;

    private Viaje viaje;
    private Venta venta;
    private Pasajero pasajero;

    public Pasaje(int asiento, Viaje viaje, Pasajero pasajero, Venta venta) {
        this.numero = (long) (Math.random() * Math.pow(10, 12));
        this.asiento = asiento;
        this.viaje = viaje;
        this.venta = venta;
        this.pasajero = pasajero;
    }

    public int getAsiento() {
        return asiento;
    }

    public long getNumero() {
        return numero;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public Venta getVenta() {
        return venta;
    }

    public Viaje getViaje() {
        return viaje;
    }
}

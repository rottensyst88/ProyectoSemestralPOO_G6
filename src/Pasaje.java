public class Pasaje {
    private long numero;
    private int asiento;
    private Viaje viaje;
    private Venta venta;
    private Pasajero pasajero;

    public Pasaje2(int asiento, Viaje viaje, Pasajero pasajero, Venta venta) {
        this.numero = 0;
        this.asiento = asiento;
        this.viaje = viaje;
        this.venta = venta;
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

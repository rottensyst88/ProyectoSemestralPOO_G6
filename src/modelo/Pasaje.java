package modelo;

@SuppressWarnings({"CanBeFinal", "FieldMayBeFinal"})
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
        this.pasajero = pasajero;
        viaje.addPasaje(this);
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

    @Override
    public String toString(){
        String cabecera_inicio = "------------------------------ PASAJE ELECTRONICO ------------------------------";
        String linea1 = String.format("%-15s %-10s %-15s", "Nombre Empresa","","Numero de pasaje");
        String linea2 = String.format("%-15s %-10s %-15s", this.viaje.getBus().getEmp().getNombre().toUpperCase(),"",this.numero);
        String linea3 = String.format("%-30s %-10s %-15s","Nombre Pasajero","","Rut/Pasaporte");
        String linea5 = String.format("%-30s %-10s %-15s",this.pasajero.getNombreCompleto().toString(),"",this.pasajero.getIdPersona().toString());

        return String.join("\n",cabecera_inicio, linea1, linea2, linea3, linea5);
    }
}

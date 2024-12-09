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
    public String toString() {
        String cabecera_inicio = "---------------------------------------- PASAJE ELECTRONICO ----------------------------------------";
        String linea1 = String.format("%-30s %-30s", "Nombre Empresa", "Número de pasaje");
        String linea2 = String.format("%-30s %-30s", this.viaje.getBus().getEmp().getNombre().toUpperCase(), this.numero);
        String linea3 = String.format("%-50s %-30s", "Nombre Pasajero", "RUT/Pasaporte");
        String linea4 = String.format("%-50s %-30s", this.pasajero.getNombreCompleto(), this.pasajero.getIdPersona());
        String linea5 = String.format("%-25s %-25s %-25s", "Patente bus", "Asiento", "Valor Pagado");
        String linea6 = String.format("%-25s %-25s %-25s", this.viaje.getBus().getPatente(), this.asiento, this.venta.getMonto());
        String linea7 = String.format("%-30s %-30s %-30s %-30s", "Terminal origen", "Terminal destino", "Fecha", "Hora");
        String linea8 = String.format("%-30s %-30s %-30s %-30s", this.viaje.getTerminalSAlida().getNombre().toUpperCase(),
                this.viaje.getTerminalLlegada().getNombre().toUpperCase(), this.viaje.getFecha(), this.viaje.getHora());
        String cabecera_fin = "---------------------------------------------------------------------------------------------------";
        return String.join("\n", cabecera_inicio, linea1, linea2, linea3, linea4, linea5, linea6, linea7, linea8, cabecera_fin);
    }
}

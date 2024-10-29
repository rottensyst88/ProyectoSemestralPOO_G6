package modelo;

import java.time.*;
import java.util.ArrayList;

@SuppressWarnings({"CanBeFinal", "FieldMayBeFinal"})
public class Viaje {
    private LocalDate fecha;
    private LocalTime hora;
    private int precio;

    private Bus bus;
    private ArrayList<Pasaje> pasajes = new ArrayList<>();

    public Viaje(LocalDate fecha, LocalTime hora, int precio, Bus bus) {
        this.fecha = fecha;
        this.hora = hora;
        this.precio = precio;

        this.bus = bus;
        this.bus.addViaje(this);
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public Bus getBus() {
        return bus;
    }

    public String[][] getAsientos() {
        String[][] asientos = new String[bus.getNroAsientos()][2];
        for (int z = 0; z < bus.getNroAsientos(); z++) {
            asientos[z][0] = Integer.toString(z + 1);
            asientos[z][1] = "Libre";
        }
        for (Pasaje pasajeros : pasajes) {
            asientos[pasajeros.getAsiento() - 1][1] = "Ocupado";
        }
        return asientos;
    }

    public void addPasaje(Pasaje pasaje) {
        pasajes.add(pasaje);
    }

    public String[][] getListaPasajeros() {
        String[][] listaPasajeros = new String[pasajes.size()][4];

        for (Pasaje pasajeros : pasajes) {
            listaPasajeros[pasajes.indexOf(pasajeros)][0] = String.valueOf(pasajeros.getPasajero().getIdPersona());
            listaPasajeros[pasajes.indexOf(pasajeros)][1] = pasajeros.getPasajero().getNombreCompleto().toString();
            listaPasajeros[pasajes.indexOf(pasajeros)][2] = pasajeros.getPasajero().getNomContacto().toString();
            listaPasajeros[pasajes.indexOf(pasajeros)][3] = pasajeros.getPasajero().getFonoContacto();
        }
        return listaPasajeros;
    }

    public boolean existeDisponibilidad() {
        return pasajes.size() < bus.getNroAsientos();
    }

    public int getNroAsientosDisponibles() {
        return bus.getNroAsientos() - pasajes.size();
    }
}
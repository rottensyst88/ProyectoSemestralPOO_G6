package modelo;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings({"CanBeFinal", "FieldMayBeFinal"})
public class Viaje {

    private LocalDate fecha;
    private LocalTime hora;
    private int precio;

    //duracion en minutos
    private int duracion;

    private Bus bus;
    private ArrayList<Pasaje> pasajes = new ArrayList<>();
    //???
    private String[] Comunas = new String[2];

    //los conductores serán 1 o 2
    private ArrayList<Conductor> conductores = new ArrayList<>();
    private Auxiliar auxiliar;
    private Tripulante tripulantes[];
    private Terminal terminalLlegada;
    private Terminal terminalSalida;

    public Viaje(LocalDate fecha, LocalTime hora, int precio, int dur, Bus bus, Auxiliar aux, Conductor cond, Terminal sale, Terminal llega) {
        this.fecha = fecha;
        this.hora = hora;
        this.precio = precio;
        this.bus = bus;
        this.bus.addViaje(this);
        this.auxiliar = aux;
        auxiliar.addViaje(this);
        this.conductores.add(cond);
        this.duracion = dur;
        this.tripulantes = new Tripulante[3];
        this.terminalLlegada = sale;
        this.terminalSalida = llega;
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

    /* aparentemente to do este metodo cambió respecto al avance pasado
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
    este metodo corresponde al reemplazo*/

    public String[] getAsientos() {
        String[] Asientos = new String[bus.getNroAsientos()];
        Arrays.fill(Asientos, "*");
        for (Pasaje pasaje : pasajes) {
            int asiento = pasaje.getAsiento();
            if (asiento >= 1 && asiento <= bus.getNroAsientos()) {
                Asientos[asiento - 1] = Integer.toString(asiento);
            }
        }

        return Asientos;
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

    public boolean existeDisponibilidad(int nroAsientos) {
        return pasajes.size() >= nroAsientos;
    }

    public int getNroAsientosDisponibles() {
        return bus.getNroAsientos() - pasajes.size();
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    private int duracionMinutos;
    public LocalDateTime getFechaHoraTermino() {
        LocalDateTime inicio = LocalDateTime.of(fecha, hora);
        Duration duracion = Duration.ofMinutes(duracionMinutos);
        LocalDateTime termino = inicio.plus(duracion);
        return termino;

    }

    //?????
    public Venta[] getVentas() {
        ArrayList<Venta> ventas = new ArrayList<>();

        if(!pasajes.isEmpty()){
            for(Pasaje pasaje : pasajes){
                ventas.add(pasaje.getVenta());
            }
            return ventas.toArray(new Venta[ventas.size()]);
        }
        return new Venta[0];
    }

    public void addConductor(Conductor conductor) {
        conductores.add(conductor);
        conductor.addViaje(this);
    }

    public Terminal getTerminalLlegada(){
        return terminalLlegada;
    }
    public Terminal getTerminalSAlida() {
        return terminalSalida;

    }
    public Tripulante[] getTripulantes() {
        return tripulantes;
    }
}
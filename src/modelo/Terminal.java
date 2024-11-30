package modelo;
import utilidades.Direccion;

import java.io.Serializable;
import java.util.*;

public class Terminal implements Serializable {
    String nombre;
    Direccion direccion;
    private List<Viaje> llegadas = new ArrayList<>();
    private List<Viaje> salidas = new ArrayList<>();

    public Terminal(String nombre, Direccion direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    // Añadir viaje a la lista de llegadas
    public void addLlegada(Viaje viaje) {
        llegadas.add(viaje);
    }

    // Añadir viaje a la lista de salidas
    public void addSalida(Viaje viaje) {
        salidas.add(viaje);
    }

    // Obtener la lista de llegadas
    public Viaje[] getLlegadas() {
        return llegadas.toArray(new Viaje[0]);
    }

    // Obtener la lista de salidas
    public Viaje[] getSalidas() {
        return salidas.toArray(new Viaje[0]);
    }


}

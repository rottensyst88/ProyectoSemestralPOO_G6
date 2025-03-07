package modelo;

import utilidades.*;

import java.util.ArrayList;
import java.util.List;

public class Auxiliar extends Tripulante {
    private List<Viaje> viajes = new ArrayList<>();

    public Auxiliar(IdPersona id, Nombre nom, Direccion dir) {
        super(id, nom, dir);
    }

    @Override
    public void addViaje(Viaje viaje) {
        if (!viajes.contains(viaje)) {
            viajes.add(viaje);
        }
    }

    @Override
    public int getNroViajes() {
        return viajes.size();
    }
}

package Modelo;
import modelo.Tripulante;
import utilidades.*;

import java.util.*;

public class Conductor extends Tripulante {
    private List<Viaje> viajes = new ArrayList<Viaje>();
    //FALTA IMPLEMENTAR ASOCIACION CON VIAJE

    public Conductor(idPersona id, Nombre nom, Direccion dir) {
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

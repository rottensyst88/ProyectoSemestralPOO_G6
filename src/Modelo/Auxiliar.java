package Modelo;
import modelo.Tripulante;
import utilidades.Direccion;

import java.util.*;

public class Auxiliar extends Tripulante {
    private List<Viaje> viajes = new ArrayList<Viaje>();
    //FALTA IMPLEMENTAR ASOCIACION CON VIAJE


    public Auxiliar(idPersona id, Nombre nom, Direccion dir) {
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

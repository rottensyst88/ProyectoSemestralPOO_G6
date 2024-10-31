package modelo;
import utilidades.*;
import java.util.ArrayList;
import java.util.List;

public class Conductor extends Tripulante {
    private List<Viaje> viajes = new ArrayList<>();
    //todo FALTA IMPLEMENTAR ASOCIACION CON VIAJE

    public Conductor(IdPersona id, Nombre nom, Direccion dir) {
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

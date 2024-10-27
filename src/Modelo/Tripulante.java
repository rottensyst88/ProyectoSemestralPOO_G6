package modelo;

import Modelo.Viaje;
import utilidades.Direccion;

public abstract class Tripulante {
    private Direccion direccion;

    public  Tripulante(idPersona id, Nombre nom, Direccion dir){
        super(nom, id);
        this.direccion = dir;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public abstract void addViaje(Viaje viaje);



    public abstract int getNroViajes();


}

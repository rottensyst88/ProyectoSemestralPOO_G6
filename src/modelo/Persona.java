package modelo;
import utilidades.*;

import java.io.Serializable;

public class Persona implements Serializable {
    private IdPersona idPersona;
    private Nombre nombreCompleto;
    private String telefono;

    public Persona(IdPersona id, Nombre nombre) {
        this.idPersona = id;
        this.nombreCompleto = nombre;
    }

    public IdPersona getIdPersona() {
        return idPersona;
    }

    public Nombre getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(Nombre nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return (idPersona + ", " + nombreCompleto + ", " + telefono);
    }

    @Override
    public boolean equals(Object otro) {
        if (otro instanceof Persona otraPersona) {
            return this.idPersona.equals(otraPersona.idPersona);
        }
        return false;
    }
}

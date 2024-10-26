package utilidades;

import modelo.Tratamiento;

public class Nombre {
    private Tratamiento tratamiento;
    private String nombre;
    private String ApellidoPaterno;
    private String ApellidoMaterno;

    public Tratamiento getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        ApellidoPaterno = apellidoPaterno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        ApellidoMaterno = apellidoMaterno;
    }

    @Override
    public String toString() {
        return (tratamiento + " " + nombre + " " + ApellidoPaterno + " " + ApellidoMaterno);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Nombre otroNombre) {
            return ((this.nombre.equals(otroNombre.nombre)) && (this.ApellidoPaterno.equals(otroNombre.ApellidoPaterno))
                    && (this.ApellidoMaterno.equals(otroNombre.ApellidoMaterno)));
        }
        return false;
    }
}

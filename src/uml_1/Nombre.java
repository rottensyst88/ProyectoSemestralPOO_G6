package uml_1;

import java.util.Objects;

public class Nombre {

    private Tratamiento tratamiento;
    private String nombre;
    private String ApellidoPaterno;
    private String ApellidoMaterno;
    //geters y setters

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
    public String toString() {
        return "uml_1.Tratamiento=" + tratamiento + ", uml_1.Nombre=" + nombre + ", ApelldidoPaterno=" + ApellidoPaterno + ",ApellidoMaterno=" + ApellidoMaterno;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nombre nombre1 = (Nombre) o;
        return tratamiento == nombre1.tratamiento && Objects.equals(nombre, nombre1.nombre) && Objects.equals(ApellidoPaterno, nombre1.ApellidoPaterno) && Objects.equals(ApellidoMaterno, nombre1.ApellidoMaterno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tratamiento, nombre, ApellidoPaterno, ApellidoMaterno);
    }
}

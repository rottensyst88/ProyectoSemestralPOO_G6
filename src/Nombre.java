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
        return "Tratamiento=" + tratamiento + ", Nombre=" + nombre + ", ApelldidoPaterno=" + ApellidoPaterno + ",ApellidoMaterno=" + ApellidoMaterno;

    }

    public boolean equals(Nombre otro) {
        if (this == otro) {
            return true;
        }
        return this.nombre.equals(otro.nombre) && this.ApellidoPaterno.equals(otro.ApellidoPaterno) && this.ApellidoMaterno.equals(otro.ApellidoMaterno);
    }
}

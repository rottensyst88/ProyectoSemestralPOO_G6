package uml_1;

public class Persona {
    // Atributos
    private IdPersona idPersona;
    private Nombre nombreCompleto;
    private String telefono;

    // Constructor
    public Persona(IdPersona id, Nombre nombre) {
        this.idPersona = id;
        this.nombreCompleto = nombre;
    }

    // Getters y Setters
    public IdPersona getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(IdPersona idPersona) {
        this.idPersona = idPersona;
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

    // Método toString
    @Override
    public String toString() {
        return "uml_1.Persona[idPersona=" + idPersona + ", nombreCompleto=" + nombreCompleto + ", telefono=" + telefono + "]";
    }

    // Método equals
    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (otro == null || getClass() != otro.getClass()) {
            return false;
        }
        Persona persona = (Persona) otro;
        return idPersona.equals(persona.idPersona);
    }
}

//falta revisar el to string, equals.

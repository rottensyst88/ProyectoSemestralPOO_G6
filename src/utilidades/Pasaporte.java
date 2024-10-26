package utilidades;

@SuppressWarnings({"CanBeFinal", "FieldMayBeFinal"})
public class Pasaporte implements IdPersona {
    private String numero;
    private String nacionalidad;

    private Pasaporte(String num, String nacionalidad) {
        numero = num;
        this.nacionalidad = nacionalidad.substring(0, 1).toUpperCase() + nacionalidad.substring(1);
    }

    public String getNumero() {
        return numero;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    @Override
    public String toString() {
        return (this.numero + " " + this.nacionalidad);
    }

    @Override
    public boolean equals(Object otro) {
        if (otro instanceof Pasaporte otroPasaporte) {
            return (this.numero.equalsIgnoreCase(otroPasaporte.numero) && this.nacionalidad.equalsIgnoreCase(otroPasaporte.nacionalidad));
        }
        return false;
    }

    static public Pasaporte of(String num, String nacionalidad) {
        if (num != null && nacionalidad != null) {
            return new Pasaporte(num, nacionalidad);
        }
        return null;
    }
}
package uml_1;

public class Pasaporte implements IdPersona { // CLASE TERMINADA!
    private String numero;
    private String nacionalidad;

    private Pasaporte(String num, String nacionalidad) {
        this.numero = num;
        this.nacionalidad = nacionalidad.substring(0, 1).toUpperCase() + nacionalidad.substring(1); // ¡HABLAR CON EL PROFESOR!
    }

    public String getNumero() {
        return this.numero;
    }

    public String getNacionalidad() {
        return this.nacionalidad;
    }

    public String toString() {
        return (this.numero + " " + this.nacionalidad);
    }

    public Boolean equals(Pasaporte otro) {
        return (this.numero.equalsIgnoreCase(otro.numero) && this.nacionalidad.equalsIgnoreCase(otro.nacionalidad)); // ¡HABLAR CON EL PROFESOR!
    }

    public Pasaporte of(String num, String nacionalidad) {
        return new Pasaporte(num, nacionalidad);
    }
}

/* SI LEES ESTO, PREGUNTARLE AL PROFESOR SOBRE EL FORMATO A UTILIZAR
 * UNA COSA ES Chile Y OTRA COSA ES CHILE    !
 * EN EL CONSTRUCTOR DE PASAPORTE, LA PRIMERA LETRA DE LA NACIONALIDAD SERÁ SIEMPRE MAYÚSCULA
 * EN EL MÉTODO DE EQUALS, SE IGNORAN LAS CAPITALIZACIONES, PERO MEJOR PREGUNTARLE AL PROFESOR!
 *
 * ~AB
 * */
public class Pasaporte {
    private String numero;
    private String nacionalidad;

    private Pasaporte(String num, String nacionalidad){
        this.numero = num;
        this.nacionalidad = nacionalidad;
    }

    public String getNumero(){
        return this.numero;
    }

    public String getNacionalidad() {
        return this.nacionalidad;
    }

    public Pasaporte of(String num, String nacionalidad){
        return new Pasaporte(num, nacionalidad);
    }
}

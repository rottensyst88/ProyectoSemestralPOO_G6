public class Rut {
    private int numero;
    private char dv;

    private Rut(int num, char dv){
        this.numero = num;
        this.dv = dv;
    }

    public int getNumero() {
        return numero;
    }

    public char getDv() {
        return dv;
    }

    public Rut of (String rutConDv){
        return new Rut(Integer.parseInt(rutConDv), dv); // ESTA LINEA NO TENGO IDEA DE LO QUE HACE, ALGUIEN AYUDEME!
    }
}
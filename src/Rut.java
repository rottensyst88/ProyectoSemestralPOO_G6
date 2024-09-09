public class Rut {
    private int numero;
    private char dv;

    private Rut (int num, char dv){
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
        String[] rut_y_dv = rutConDv.split("-");
        rut_y_dv[0] = rut_y_dv[0].replace(".","");

        return new Rut(Integer.parseInt(rut_y_dv[0]),rut_y_dv[1].charAt(0));
    }
}

public class Rut implements IdPersona {
    private int numero;
    private char dv;

    private Rut(int num, char dv) {
        this.numero = num;
        this.dv = dv;
    }

    public int getNumero() {
        return numero;
    }

    public char getDv() {
        return dv;
    }

    public String toString() {
        String num_conv = String.valueOf(numero);
        if (num_conv.length() == 8) { // SE REALIZA UNA DIFERENCIACIÓN ENTRE LOS RUTS DE 7 U 8 DIGITOS
            return (num_conv.substring(0, 2) + "." + num_conv.substring(2, 5) + "." + num_conv.substring(5, 8) + "-" + dv); // RUT 8 DIGITOS
        } else {
            return (num_conv.charAt(0) + "." + num_conv.substring(1, 4) + "." + num_conv.substring(4, 7) + "-" + dv); // RUT 7 DIGITOS
        }
    }

    public Boolean equals(Rut otro) {
        return (this.numero == otro.numero && this.dv == otro.dv);
    }

    public Rut of(String rutConDv) {
        String[] rut_y_dv = rutConDv.split("-"); // DIVIDE STRING EN DOS PEDAZOS (.... - ... )
        rut_y_dv[0] = rut_y_dv[0].replace(".", ""); // ELIMINA LOS PUNTOS

        return new Rut(Integer.parseInt(rut_y_dv[0]), rut_y_dv[1].charAt(0)); // CREA NUEVO RUT EN BASE AL STRING ANTERIOR
    }
}

/* ESTA CLASE DEBERÍA ESTAR TERMINADA! POR FAVOR, EVITEN MOVERLE COSAS
    RECUERDEN EL DICHO...

    SI FUNCIONA, NO LO TOQUE

    VAN A SER LAS 1 DE LA MAÑANA, NO ME HAGAN ARREGLAR PORQUERÍAS QUE YO NO CORROMPÍ!

    (-:
    ~AB
 */

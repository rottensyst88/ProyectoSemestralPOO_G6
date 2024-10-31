// TODO REVISAR LA CORRECTA IMPLEMENTATION DE CLASES PAGO Y SUS DERIVADAS!
package modelo;

public abstract class Pago {
    private int monto;

    public Pago(int monto) {
        this.monto = monto;
    }

    public int getMonto() {
        return monto;
    }

}

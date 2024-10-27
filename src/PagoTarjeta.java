public class PagoTarjeta extends Pago {

    private long nroTarjeta;

    public PagoTarjeta(long nroTarjeta, int monto) {
        super(monto);
    }
    public long getNroTarjeta() {
        return nroTarjeta;
    }
}

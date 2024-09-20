import java.time.LocalDate;
public class Venta {
    private TipoDocumento idDocumento;
    private LocalDate fecha;
    private Cliente cliente;

    public Venta(TipoDocumento idDocumento, LocalDate fecha, Cliente cliente) {
        this.idDocumento = idDocumento;
        this.fecha = fecha;
        this.cliente = cliente;
    }

    public TipoDocumento getIdDocumento() {
        return idDocumento;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }
}

//clase prueba
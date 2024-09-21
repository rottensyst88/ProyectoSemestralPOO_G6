package uml_2;

import uml_1.*;

import java.time.LocalDate;

public class Venta {
    private String idDocumento;
    private TipoDocumento tipo;
    private LocalDate fecha;
    private Cliente cliente;

    public Venta(String id, TipoDocumento tipo, LocalDate fec, Cliente cli) {
        idDocumento = id;
        this.tipo = tipo;
        fecha = fec;
        cliente = cli;
    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }
}
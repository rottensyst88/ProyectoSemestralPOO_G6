package modelo;
import utilidades.*;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Persona {

    private String email;
    private List<Venta> ventas = new ArrayList<>();

    public Cliente(IdPersona id, Nombre nom, String email) {
        super(id, nom);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addVenta(Venta venta) {
        ventas.add(venta);
    }

    public Venta[] getVentas() {
        return ventas.toArray(new Venta[0]); //todo HACER
    }
}
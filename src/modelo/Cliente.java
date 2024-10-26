package modelo;

import java.util.ArrayList;

@SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
public class Cliente extends Persona {

    private String email;
    private ArrayList<Venta> ventas = new ArrayList<>();

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
        return ventas.toArray(new Venta[0]);
    }
}
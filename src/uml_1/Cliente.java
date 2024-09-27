package uml_1;

public class Cliente extends Persona {
    private String email;
    private ArrayList<Venta> ventas;

    public Cliente(IdPersona id, Nombre nom, String email) {
        super(id, nom);
        this.email = email;
        this.ventas = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void addVenta(Venta venta) {
        return ventas.add(venta);
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
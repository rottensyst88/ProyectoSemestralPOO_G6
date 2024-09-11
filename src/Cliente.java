public class Cliente extends Persona{
    private String email;

    public Cliente(IdPersona id, Nombre nom, String email){
        super(id, nom);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

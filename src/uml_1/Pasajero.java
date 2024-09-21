package uml_1;

public class Pasajero extends Persona {
    private Nombre nomContacto;
    private String fonoContacto;

    public Pasajero(IdPersona id, Nombre nom, String fonoContacto) {
        super(id, nom);
        this.fonoContacto = fonoContacto;
    }

    public Nombre getNomContacto() {
        return nomContacto;
    }

    public void setNomContacto(Nombre nomContacto) {
        this.nomContacto = nomContacto;
    }

    public String getFonoContacto() {
        return fonoContacto;
    }

    public void setFonoContacto(String fonoContacto) {
        this.fonoContacto = fonoContacto;
    }
}

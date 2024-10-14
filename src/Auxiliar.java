public class Auxiliar extends Tripulante{

    public Auxiliar(idPersona id, Nombre nom, Direccion dir) {
        super(id, nom, dir);
    }

    @Override
    public void addViaje() {

    }

    @Override
    public int getNroViajes() {
        return 0;
    }
}

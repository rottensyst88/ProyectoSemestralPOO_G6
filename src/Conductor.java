public class Conductor extends Tripulante{

    public Conductor(idPersona id, Nombre nom, Direccion dir) {
        super(id, nom, dir);
    }

    @Override
    public void addViaje(Viaje viaje) {


    }

    @Override
    public int getNroViajes() {
        return 0;
    }
}

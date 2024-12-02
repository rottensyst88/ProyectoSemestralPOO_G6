package modelo;
import utilidades.*;

import java.util.*;

public class Empresa {
    private Rut rut;
    private String nombre;
    private String url;
    private List<Bus> buses;
    private List<Conductor> conductores;
    private List <Auxiliar> auxiliares;

    public Empresa(Rut rut, String nombre, String url) {
        this.rut = rut;
        this.nombre = nombre;
        this.url = url;
        buses = new ArrayList<>();
        auxiliares = new ArrayList<>();
        conductores = new ArrayList<>();
    }

    public Rut getRut() {
        return rut;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void addBus(Bus bus){
        if(!buses.contains(bus)){
            buses.add(bus);
        }
    }

    public Bus[] getBuses(){
        return buses.toArray(new Bus[0]);
    }

    public boolean addConductor(IdPersona id, Nombre nom, Direccion dir){
        if(!existeConducPorId(id)){
            Conductor c = new Conductor(id, nom, dir);
            return conductores.add(c);
        }return false;
    }


    public boolean addAuxiliar(IdPersona id, Nombre nom, Direccion dir){
        if(!existeAuxPorId(id)){
            Auxiliar a = new Auxiliar(id, nom, dir);
            return auxiliares.add(a);
        }return false;
    }

    private boolean existeAuxPorId(IdPersona id){
        for (Auxiliar a : auxiliares) {
            if(a.getIdPersona().equals(id)){
                return true;
            }
        }return false;
    }
    private boolean existeConducPorId(IdPersona id){
        for (Conductor c : conductores) {
            if(c.getIdPersona().equals(id)){
                return true;
            }
        }return false;
    }

    public Tripulante[] getTripulantes(){
        int totalTripulantes = conductores.size() + auxiliares.size();
        Tripulante[] tripulantes = new Tripulante[totalTripulantes];
        int k = 0;
        for (Conductor conductor : conductores) {
            tripulantes[k++] = conductor;
        }
        for (Auxiliar auxiliar : auxiliares) {
            tripulantes[k++] = auxiliar;
        }

        return tripulantes;
    }

    public Venta[] getVentas(){
        ArrayList<Venta> ventas = new ArrayList<>();

        for (Bus bus : buses){
            for (Viaje viaje : bus.getViajes()){
                for(Venta venta : viaje.getVentas()){
                    ventas.add(venta);
                }
            }
        }
        return ventas.toArray(new Venta[0]);
    }
}

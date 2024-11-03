package modelo;

import java.util.ArrayList;

public class Bus {
    private String patente;
    private String marca;
    private String modelo;
    private int nroAsientos;

    private Empresa emp;
    private ArrayList<Viaje> viajes = new ArrayList<>();

    public Bus(String patente, int nroAsientos, Empresa emp) {
        this.patente = patente;
        this.nroAsientos = nroAsientos;
        this.emp = emp;
        emp.addBus(this);
    }

    public Bus(String patente, int nroAsientos) {
        this.patente = patente;
        this.nroAsientos = nroAsientos;
    }

    public String getPatente() {
        return patente;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getNroAsientos() {
        return nroAsientos;
    }

    public Empresa getEmp() {
        return emp;
    }

    public void addViaje(Viaje viaje) {
        if (!viajes.contains(viaje)) {
            viajes.add(viaje);
        }
    }

    public Viaje[] getViajes() {
        return viajes.toArray(new Viaje[viajes.size()]);
    }


}

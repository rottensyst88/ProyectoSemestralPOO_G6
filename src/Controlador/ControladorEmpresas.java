package Controlador;

import A.*;
import Modelo.*;
import Exepcion.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ControladorEmpresas {
    //SINGLETON
    private static final ControladorEmpresas instance = new ControladorEmpresas();

    public static ControladorEmpresas getInstance() {
        return instance;
    }

    private ControladorEmpresas() {}

    //ATRIBUTOS CLASE

    private List<Terminal> terminales =  new ArrayList<Terminal>();
    private List<Bus> buses = new ArrayList<Bus>();
    private List <Empresa> empresas = new ArrayList<Empresa>();


    //METODOS

    public void createEmpresa(Rut rut, String nombre, String url) throws SistemaVentaPasajesException{
        Optional<Empresa> empresaExist = findEmpresa(rut);
        if(empresaExist.isPresent()){
            throw new SistemaVentaPasajesException("Ya existe empresa con el rut indicado");
        }
        Empresa nuevaEmpresa = new Empresa(rut, nombre, url);
        empresas.add(nuevaEmpresa);
    }

    public void createBus(String pat, String marca, String modelo, int nroAsientos, Rut rutEmp) throws SistemaVentaPasajesException{

        Optional<Bus> busExist = findBus(pat);
        Optional<Empresa> empresaExist = findEmpresa(rutEmp);
        if(busExist.isPresent()){
            throw new SistemaVentaPasajesException("Ya existe bus con la patente indicada");
        }
        if(empresaExist.isEmpty()){
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado");
        }
        Empresa e = empresaExist.get();
        Bus nuevoBus = new Bus(pat,nroAsientos, e);
        buses.add(nuevoBus);
    }

    public void createTerminal(String nombre, Direccion direccion) throws SistemaVentaPasajesException{
        Optional<Terminal> terminalNombre = findTerminal(nombre);
        Optional<Terminal> terminalComuna = findTerminal(direccion.getComuna());

        if(terminalNombre.isPresent()){
            throw new SistemaVentaPasajesException("Ya existe terminal con el nombre indicado");
        }
        if(terminalComuna.isPresent()){
            throw new SistemaVentaPasajesException("Ya existe terminal en la comuna indicada");
        }

        Terminal nuevoTerminal = new Terminal(nombre, direccion);
        terminales.add(nuevoTerminal);
    }

    public void hireConductorForEmpresa(Rut rutEmp, IdPersona id, Nombre nom, Direccion dir) throws SistemaVentaPasajesException{
        Optional<Empresa> empresaExist = findEmpresa(rutEmp);
        Optional<Conductor> conductorExist = findConductor(id, rutEmp);
        if (empresaExist.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado");
        }

        if (conductorExist.isPresent()) {
            throw new SistemaVentaPasajesException("Ya está contratado conductor/auxiliar con el id dado en la empresa señalada");
        }

        Empresa e = empresaExist.get();
        e.addConductor(id,nom,dir);

    }


    public void hireAuxiliarForEmpresa(Rut rutEmp, IdPersona id, Nombre nom, Direccion dir) throws SistemaVentaPasajesException{
        Optional<Empresa> empresaExist = findEmpresa(rutEmp);
        Optional<Auxiliar> auxiliarExist = findAuxiliar(id, rutEmp);

        if(empresaExist.isEmpty()){
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado");
        }
        if(auxiliarExist.isPresent()){
            throw new SistemaVentaPasajesException("Ya está contratado auxiliar/conductor con el id dado en la empresa señalada");

        }

        Empresa e = empresaExist.get();
        e.addAuxiliar(id,nom,dir);
    }


}

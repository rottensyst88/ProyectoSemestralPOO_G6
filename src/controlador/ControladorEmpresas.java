package controlador;

public class ControladorEmpresas {
    private static ControladorEmpresas instancia;

    public static ControladorEmpresas getInstancia(){
        if(instancia == null){
            instancia = new ControladorEmpresas();
        }
        return instancia;
    }

    public void createBus(){

    }

    public void createEmpresa(){

    }

    public void createTerminal(){

    }

    // CLASES FALTANTES, ARREGLANDO LO ANTERIOR!

}
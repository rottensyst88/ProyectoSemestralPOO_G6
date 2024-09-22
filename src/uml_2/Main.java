package uml_2;

import uml_1.*;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main {

    private Scanner sc = new Scanner(System.in);
    private final SistemaVentaPasajes sistemaCentral = new SistemaVentaPasajes();

    public static void main(String[] args) {
        Main mainInstance = new Main();
        mainInstance.menu();
    }

    private void menu() {
        boolean verificador = true;

        System.out.println("============================");
        System.out.println("...::: Menú principal :::...\n");
        System.out.println(" 1) Crear cliente");
        System.out.println(" 2) Crear bus");
        System.out.println(" 3) Crear viaje");
        System.out.println(" 4) Vender pasajes");
        System.out.println(" 5) Lista de pasajeros");
        System.out.println(" 6) Lista de ventas");
        System.out.println(" 7) Lista de viajes");
        System.out.println(" 8) Salir");
        System.out.println("----------------------------");

        do {
            System.out.print("..:: Ingrese número de opción: ");
            int valor = sc.nextInt();

            switch (valor) {
                case 1:
                    createCliente();
                    break;
                case 2:
                    createBus();
                    break;
                case 3:
                    createViaje();
                    break;
                case 4:
                    vendePasajes();
                    break;
                case 5:
                    listPasajerosViaje();
                    break;
                case 6:
                    listVentas();
                    break;
                case 7:
                    listViajes();
                    break;
                case 8:
                    verificador = false;
                    break;
                default:
                    System.out.println("Error! Ingrese los datos de forma correcta!");
            }
        } while (verificador);
    }

    private void createCliente() {
        Nombre usuario = new Nombre();
        Tratamiento tratamiento = null;
        IdPersona id = null;

        System.out.println("...:::: Crear un nuevo Cliente ::::...\n");

        do {
            id = SelectorRut_Pasaporte();
        } while (id == null);

        do {
            System.out.print("Sr.[1] o Sra. [2] : ");

            int sr_o_sra = sc.nextInt();
            if (sr_o_sra == 1 || sr_o_sra == 2) {
                if (sr_o_sra == 1) {
                    tratamiento = Tratamiento.SR;
                } else {
                    tratamiento = Tratamiento.SRA;
                }
            } else {
                System.out.println("Error! Valor invalido!");
            }
        } while (tratamiento == null);

        sc.nextLine();
        System.out.print("Nombres: ");
        String nombres = sc.nextLine();

        System.out.print("Apellido Paterno : ");
        String apPaterno = sc.next();

        System.out.print("Apellido Materno : ");
        String apMaterno = sc.next();

        System.out.print("Teléfono movil : ");
        String telefono = sc.next();

        System.out.print("Email : ");
        String email = sc.next();

        usuario.setTratamiento(tratamiento);
        usuario.setNombre(nombres);
        usuario.setApellidoMaterno(apMaterno);
        usuario.setApellidoPaterno(apPaterno);

        if (sistemaCentral.createCliente(id, usuario, telefono, email)) {
            System.out.println("\n...:::: Cliente guardado exitosamente ::::...");
        } else {
            System.out.println("\n...:::: Error al guardar cliente ::::...");
        }
    }

    private void createBus() {
        System.out.println("...:::: Creación de un nuevo BUS ::::...\n");

        System.out.print("Patente : ");
        String patente = sc.next();

        sc.nextLine();
        System.out.print("Marca : ");
        String marca = sc.nextLine();

        System.out.print("Modelo : ");
        String modelo = sc.next();

        System.out.print("Numero de asientos : ");
        int nroAsientos = sc.nextInt();

        if (sistemaCentral.createBus(patente, marca, modelo, nroAsientos)) {
            System.out.println("\n...:::: Bus guardado exitosamente ::::...");
        } else {
            System.out.println("\n...:::: Error al guardar bus ::::...");
        }
    }

    private void createViaje() {
        System.out.println("...:::: Creación de un nuevo Viaje ::::...\n");

        System.out.print("Fecha[dd/mm/yyyy] : ");
        String fechaSN = sc.next();
        LocalDate fecha = LocalDate.parse(fechaSN, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        System.out.print("Hora[hh:mm] : ");
        String horaSN = sc.next();
        LocalTime hora = LocalTime.parse(horaSN, DateTimeFormatter.ofPattern("HH:mm"));

        System.out.print("Precio : ");
        int precio = sc.nextInt();

        System.out.print("Patente Bus : ");
        String patente = sc.next();

        if (sistemaCentral.createViaje(fecha, hora, precio, patente)) {
            System.out.println("\n...:::: Viaje guardado exitosamente ::::...");
        } else {
            System.out.println("\n...:::: Error al guardar viaje ::::...");
        }
    }

    private void vendePasajes() {
        IdPersona id = null;
        TipoDocumento tipo = null;

        System.out.println("....:::: Venta de pasajes ::::....\n\n");
        System.out.println(":::: Datos de la Venta");

        System.out.print("ID Documento : ");
        String idDocumento = sc.next();

        do {
            System.out.print("Tipo documento: [1] Boleta [2] Factura : ");

            int tipoDoc = sc.nextInt();
            if (tipoDoc == 1 || tipoDoc == 2) {
                if (tipoDoc == 1) {
                    tipo = TipoDocumento.BOLETA;
                } else {
                    tipo = TipoDocumento.FACTURA;
                }
            } else {
                System.out.println("Error! Valor invalido!");
            }
        } while (tipo == null);

        System.out.print("Fecha de venta[dd/mm/yyyy] : ");
        String fechaVenta = sc.next();

        System.out.println("\n:::: Datos del cliente\n");

        do {
            id = SelectorRut_Pasaporte();
        } while (id == null);

        System.out.print("Nombre Cliente : ");
        String nombreCliente = sc.next();

        // IMPRESION DE LOS PASAJES DISPONIBLES!

        System.out.println("\n\n::::Pasajes a vender");
        System.out.print("Cantidad de pasajes : ");
        int cantidadPasajes = sc.nextInt();

        System.out.print("Fecha de viaje[dd/mm/yyyy] : ");
        String fechaViajeSN = sc.next();
        LocalDate fechaViaje = LocalDate.parse(fechaViajeSN, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        System.out.println("::::Listado de horarios disponibles");

        String[][] horariosDisponibles = sistemaCentral.getHorariosDisponibles(fechaViaje);
        String[] titulos = {"|BUS", "|SALIDA", "|VALOR", "|ASIENTOS"};

        impresora(titulos, horariosDisponibles);


    }

    private void listPasajerosViaje() {


    }

    private void listVentas() {

    }

    private void listViajes() {

    }

    // METODO PRIVADOS PARA OPTIMIZAR EL PROCESO!

    private IdPersona SelectorRut_Pasaporte() {

        System.out.print("Rut[1] o Pasaporte[2] : ");
        int rut_o_pasaporte = sc.nextInt();

        if (rut_o_pasaporte == 1 || rut_o_pasaporte == 2) {
            if (rut_o_pasaporte == 1) {
                System.out.print("R.U.T : ");
                String rutConDV = sc.next();

                return Rut.of(rutConDV);

            } else {
                System.out.print("Numero de pasaporte : ");
                String nroPasaporte = sc.next();

                System.out.print("Nacionalidad : ");
                String nacionalidad = sc.next();

                return Pasaporte.of(nroPasaporte, nacionalidad);
            }
        } else {
            System.out.println("Error! Valor invalido!");
            return null;
        }
    }

    private void impresora(String[] cabeceras, String[][] arregloImpresora){
        int contador_letras = 0;
        int contador_for;
        int contador_todo = 0;

        // CALCULA LONGITUD DE LA CABECERA!
        for (String cabecera : cabeceras) {
            if (cabecera.length() > contador_letras) {
                contador_letras = cabecera.length();
            }
        }

        contador_for = contador_letras;

        // SE CUENTA TODO EL STRING, JUNTO A SUS ESPACIOS!
        for (String cabecera : cabeceras) {
            int espacios = contador_letras - cabecera.length();
            contador_todo += (espacios + cabecera.length());
        }

        for(int z = 0; z <= arregloImpresora.length;z++){
            if (z == 0 || z == arregloImpresora.length){
                for(int x = 0; x < contador_todo;x++ ){
                    if(contador_todo / cabeceras.length == contador_for){
                        System.out.print("*");
                        contador_for = 0;
                    }else{
                        System.out.print("-");
                    }
                    contador_for++;
                }
                System.out.println("*");
            }

            if(z == 0){
                for (String cabecera : cabeceras) {
                    int espacios = contador_letras - cabecera.length();
                    System.out.print(cabecera + " ".repeat(espacios));
                }

                System.out.println("|");
            }



        }
    }
}
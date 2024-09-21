package uml_2;

import uml_1.*;

import java.util.Scanner;

public class Main {

    private Scanner sc = new Scanner(System.in);
    private SistemaVentaPasajes sistemaCentral = new SistemaVentaPasajes();

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
            System.out.print("Rut[1] o Pasaporte[2] : ");
            int rut_o_pasaporte = sc.nextInt();

            if (rut_o_pasaporte == 1 || rut_o_pasaporte == 2) {
                if (rut_o_pasaporte == 1) {
                    System.out.print("R.U.T : ");
                    String rutConDV = sc.next();

                    id = Rut.of(rutConDV);

                } else {
                    System.out.print("Numero de pasaporte : ");
                    String nroPasaporte = sc.next();

                    System.out.print("Nacionalidad : ");
                    String nacionalidad = sc.next();

                    id = Pasaporte.of(nroPasaporte, nacionalidad);
                }
            } else {
                System.out.println("Error! Valor invalido!");
            }
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

        System.out.print("Telefono movil : ");
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

    }

    private void createViaje() {

    }

    private void vendePasajes() {

    }

    private void listPasajerosViaje() {

    }

    private void listVentas() {

    }

    private void listViajes() {

    }
}

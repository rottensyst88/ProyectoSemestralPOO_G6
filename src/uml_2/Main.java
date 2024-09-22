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

        System.out.print("Fecha de venta_lista[dd/mm/yyyy] : ");
        String fechaVenta = sc.next();
        LocalDate fec = LocalDate.parse(fechaVenta, DateTimeFormatter.ofPattern("dd/MM/yyyy"));


        // DATOS DEL CLIENTE!

        System.out.println("\n:::: Datos del cliente\n");

        do {
            id = SelectorRut_Pasaporte();
        } while (id == null);

        System.out.print("Nombre Cliente : ");
        sc.nextLine();
        String cliente = sc.nextLine();

        Nombre nombreCliente = new Nombre();

        String[] nombres = cliente.split(" ");
        if (nombres[0].equals("Sr.")) {
            nombreCliente.setTratamiento(Tratamiento.SR);
        } else {
            nombreCliente.setTratamiento(Tratamiento.SRA);
        }
        nombreCliente.setNombre(nombres[1]);
        nombreCliente.setApellidoPaterno(nombres[2]);
        nombreCliente.setApellidoMaterno(nombres[3]);

        System.out.println(sistemaCentral.iniciaVenta(idDocumento, tipo, fec, id)); // INICIA LA VENTA!

        Venta venta = sistemaCentral.findVenta(idDocumento, tipo);

        // IMPRESION DE LOS PASAJES DISPONIBLES!

        System.out.println("\n\n::::Pasajes a vender");
        System.out.print("Cantidad de pasajes : ");
        int cantidadPasajes = sc.nextInt();

        System.out.print("Fecha de viaje[dd/mm/yyyy] : ");
        String fechaViajeSN = sc.next();
        LocalDate fechaViaje = LocalDate.parse(fechaViajeSN, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        System.out.println("::::Listado de horarios disponibles");

        String[][] horariosDisponibles = sistemaCentral.getHorariosDisponibles(fechaViaje); // RETORNA ARREGLO CON DATOS SOBRE VIAJES DE BUS

        Viaje viajeAbordar = impresoraListadoViajes(new String[]{"|BUS", "|SALIDA", "|VALOR", "|ASIENTOS"}, horariosDisponibles, fechaViaje);
        sistemaCentral.createViaje(viajeAbordar.getFecha(), viajeAbordar.getHora(), viajeAbordar.getPrecio(), viajeAbordar.getBus().getPatente());

        impresoraListadoAsientos(viajeAbordar.getAsientos());

        System.out.print("Seleccione sus asientos [separe por ,] : ");
        String asientos = sc.next();

        String[] split = asientos.split(",");

        for (int i = 0; i < cantidadPasajes; i++) {

            String asiento = split[i];
            System.out.println(":::: Datos del pasajero " + (i + 1));

            IdPersona id_pasajero = SelectorRut_Pasaporte();

            if (sistemaCentral.findPasajero(id_pasajero) == null) {
                sistemaCentral.createPasajero(id_pasajero, null, null, null, null);
                System.out.print("Nombre : ");

                sc.nextLine();
                String nombre = sc.nextLine();
                Nombre nombrePasajero = new Nombre();

                String[] nombres_1 = nombre.split(" ");

                System.out.println(nombres_1[0]);
                System.out.println(nombres_1[1]);
                System.out.println(nombres_1[2]);
                System.out.println(nombres_1[3]);

                if (nombres_1[0].equals("Sr.")) {
                    nombrePasajero.setTratamiento(Tratamiento.SR);
                } else {
                    nombrePasajero.setTratamiento(Tratamiento.SRA);
                }

                nombrePasajero.setNombre(nombres_1[1]);
                nombrePasajero.setApellidoPaterno(nombres_1[2]);
                nombrePasajero.setApellidoMaterno(nombres_1[3]);

                System.out.print("Telefono : ");
                String telefono = sc.next();

                System.out.print("Nombre de contacto : ");
                sc.nextLine();
                String nombreContacto = sc.nextLine();

                Nombre nombreContactoPasajero = new Nombre();

                String[] nombresContacto = nombreContacto.split(" ");

                if (nombresContacto[0].equals("Sr.")) {
                    nombreContactoPasajero.setTratamiento(Tratamiento.SR);
                } else {
                    nombreContactoPasajero.setTratamiento(Tratamiento.SRA);
                }

                nombreContactoPasajero.setNombre(nombresContacto[1]);
                nombreContactoPasajero.setApellidoPaterno(nombresContacto[2]);
                nombreContactoPasajero.setApellidoMaterno(nombresContacto[3]);

                System.out.print("Telefono de contacto : ");
                String telefonoContacto = sc.next();

                sistemaCentral.findPasajero(id_pasajero).setNombreCompleto(nombrePasajero);
                sistemaCentral.findPasajero(id_pasajero).setTelefono(telefono);
                sistemaCentral.findPasajero(id_pasajero).setNomContacto(nombreContactoPasajero);
                sistemaCentral.findPasajero(id_pasajero).setFonoContacto(telefonoContacto);


            } else {
                sistemaCentral.findPasajero(id_pasajero).setNombreCompleto(sistemaCentral.findPasajero(id).getNombreCompleto());
                sistemaCentral.findPasajero(id_pasajero).setTelefono(sistemaCentral.findPasajero(id_pasajero).getTelefono());
                sistemaCentral.findPasajero(id_pasajero).setNomContacto(sistemaCentral.findPasajero(id_pasajero).getNomContacto());

            }
            if (sistemaCentral.vendePasaje(idDocumento, tipo, viajeAbordar.getHora(), viajeAbordar.getFecha(), viajeAbordar.getBus().getPatente(), Integer.parseInt(asiento), id_pasajero)) {
                venta.createPasaje(Integer.parseInt(asiento), viajeAbordar, sistemaCentral.findPasajero(id_pasajero));
                System.out.println(":::: Pasaje agregado exitosamente!");
            } else {
                System.out.println(":::: Error al agregar pasaje!");
            }
        }

        System.out.println(venta);

        System.out.println(":::: Monto total de la venta_lista : " + venta.getMonto());

        System.out.println(":::: Venta realizada exitosamente!");

        System.out.println(":::: Imprimiendo los pasajes");

        for (int f = 0; f < cantidadPasajes; f++) {
            System.out.println("------------------- PASAJE -------------------");
            System.out.println("NUMERO DE PASAJE : " + venta.getPasajes()[f].getNumero());
            System.out.println("FECHA DEL VIAJE : " + venta.getPasajes()[f].getViaje().getFecha());
            System.out.println("HORA DEL VIAJE : " + venta.getPasajes()[f].getViaje().getHora());
            System.out.println("PATENTE BUS : " + venta.getPasajes()[f].getViaje().getBus().getPatente());
            System.out.println("ASIENTO : " + venta.getPasajes()[f].getAsiento());
            System.out.println("RUT - PASAPORTE : " + venta.getPasajes()[f].getPasajero().getIdPersona());
            System.out.println("NOMBRE PASAJERO : " + venta.getPasajes()[f].getPasajero().getNombreCompleto());
            System.out.println("-----------------------------------------------");
        }
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

    private Viaje impresoraListadoViajes(String[] cabeceras, String[][] arregloImpresora, LocalDate fecha) {
        int contador_letras = 0;
        int contador_for;
        int contador_todo = 0;
        int z;

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

        // IMPRESION DE LA TABLA!
        for (z = 0; z < arregloImpresora.length; z++) {
            if (z == 0) {

                // ESTA WEA IMPRIME LOS CONTENEDORES DE LA TABLA!

                System.out.print("   ");
                for (int x = 0; x < contador_todo; x++) {
                    if (contador_todo / cabeceras.length == contador_for) {
                        System.out.print("*");
                        contador_for = 0;
                    } else {
                        System.out.print("-");
                    }
                    contador_for++;
                }
                System.out.println("*");

                // ESTA OTRA WEA IMPRIME LA CABECERA!
                System.out.print("   ");
                for (String cabecera : cabeceras) {
                    int espacios = contador_letras - cabecera.length();
                    System.out.print(cabecera + " ".repeat(espacios));
                }

                System.out.println("|");
            }

            // ESTA OTRA WEA IMPRIME LOS SEPARADORES!
            System.out.print("   ");
            for (int x = 0; x < contador_todo; x++) {
                if (contador_todo / cabeceras.length == contador_for) {
                    System.out.print("+");
                    contador_for = 0;
                } else {
                    System.out.print("-");
                }
                contador_for++;
            }
            System.out.println("+");

            System.out.print(" " + (z + 1) + " |");

            int f;

            for (f = 0; f < arregloImpresora[z].length; f++) {
                int espacios = contador_letras - arregloImpresora[z][f].length();
                System.out.print(arregloImpresora[z][f] + " ".repeat(espacios - 1) + "|");
            }
            System.out.println();
        }

        System.out.print("   ");
        for (int x = 0; x < contador_todo; x++) {
            if (contador_todo / cabeceras.length == contador_for) {
                System.out.print("*");
                contador_for = 0;
            } else {
                System.out.print("-");
            }
            contador_for++;
        }
        System.out.print("*");
        System.out.println("\n");

        System.out.print("Seleccione viaje en [1.." + z + "] : ");
        int seleccion = sc.nextInt();

        return sistemaCentral.findViaje(String.valueOf(fecha), arregloImpresora[seleccion - 1][1], arregloImpresora[seleccion - 1][0]);
        // return sistemaCentral.listAsientosDeViaje(fecha, LocalTime.of(Integer.parseInt(arregloImpresora[seleccion - 1][1].substring(0, 2)), Integer.parseInt(arregloImpresora[seleccion - 1][1].substring(3, 5))), arregloImpresora[seleccion - 1][0]);

    }

    private void impresoraListadoAsientos(String[][] listadoBuses) {
        int y = 3;
        int z;
        int q = 0;
        int contador_for = 4;
        int contador_f;
        int h = 1;

        System.out.println(listadoBuses.length);

        if (listadoBuses.length % 4 == 0) {
            contador_f = listadoBuses.length;
        } else {
            do {
                contador_f = 4 * h;
                h++;

            } while (contador_f < listadoBuses.length);
        }

        for (z = 0; z < contador_f; z++) {
            if (z < listadoBuses.length) {
                if (z == 0) {

                    // ESTA WEA IMPRIME LOS CONTENEDORES DE LA TABLA!
                    for (int x = 0; x < 21; x++) {
                        if (contador_for == 4) {
                            System.out.print("*");
                            contador_for = 0;
                        } else {
                            System.out.print("-");
                        }
                        contador_for++;
                    }
                    System.out.println();
                }

                if (y != 5) {
                    if (z >= 0 && z < 9) {
                        System.out.print("| " + (z + 1) + " ");
                    } else {
                        System.out.print("| " + (z + 1));

                    }
                } else {
                    System.out.print("|   ");
                    y = 0;
                    z -= 1;
                }

                if (q == 4) {
                    System.out.println("|");
                    q = -1;

                    contador_for = 4;

                    for (int x = 0; x < 21; x++) {
                        if (contador_for == 4) {
                            System.out.print("+");
                            contador_for = 0;
                        } else {
                            System.out.print("-");
                        }
                        contador_for++;
                    }
                    System.out.println();
                }

                y++;
                q++;

            } else {
                if (y != 5) {
                    System.out.print("| * ");
                } else {
                    System.out.print("|   ");
                    y = 0;
                    z -= 1;
                }

                y++;
                q++;

            }
        }

        System.out.print("|\n");

        contador_for = 4;

        for (int x = 0; x < 21; x++) {
            if (contador_for == 4) {
                System.out.print("*");
                contador_for = 0;
            } else {
                System.out.print("-");
            }
            contador_for++;
        }

        System.out.print("\n   ");
    }
}
package uml_2;

import uml_1.*;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings({"CanBeFinal", "FieldMayBeFinal", "", "SpellCheckingInspection"})
public class Main {
    private Scanner sc = new Scanner(System.in);
    private static SistemaVentaPasajes sistemaCentral = new SistemaVentaPasajes();

    public static void main(String[] args) {

        Main main = new Main();

        // PRUEBAS DE CREACION DE CLIENTES Y BUSES!

        Nombre n1 = new Nombre();
        n1.setTratamiento(Tratamiento.SR);
        n1.setNombre("Juan");
        n1.setApellidoPaterno("Perez");
        n1.setApellidoMaterno("Gonzalez");

        Nombre n2 = new Nombre();
        n2.setTratamiento(Tratamiento.SRA);
        n2.setNombre("Maria");
        n2.setApellidoPaterno("Gonzalez");
        n2.setApellidoMaterno("Perez");

        Nombre n3 = new Nombre();
        n3.setTratamiento(Tratamiento.SR);
        n3.setNombre("Pedro");
        n3.setApellidoPaterno("Gonzalez");
        n3.setApellidoMaterno("Lopez");

        sistemaCentral.createCliente(Rut.of("11.111.111-1"), n1, "123456789", "aaa@aaa");
        sistemaCentral.createCliente(Rut.of("11.111.111-2"), n2, "234567890", "bbb@bbb");
        sistemaCentral.createCliente(Rut.of("11.111.111-3"), n3, "345678901", "ccc@ccc");

        sistemaCentral.createBus("AABB12", "Volvo", "2021", 40);
        sistemaCentral.createBus("BBCC23", "Mercedes", "2020", 30);
        sistemaCentral.createBus("CCDD34", "Scania", "2019", 50);

        // FIN DE CREACION DE CLIENTES Y BUSES!

        main.menu();
    }

    private void menu() {
        boolean verificador = true;

        do {
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

            System.out.print("\n..:: Ingrese número de opción: ");
            int valor = sc.nextInt();

            switch (valor) {
                case 1 -> createCliente();
                case 2 -> createBus();
                case 3 -> createViaje();
                case 4 -> vendePasajes();
                case 5 -> listPasajerosViaje();
                case 6 -> listVentas();
                case 7 -> listViajes();
                case 8 -> verificador = false;
                default -> System.out.println("Error! Ingrese los datos de forma correcta!");
            }
        } while (verificador);
    }

    private void createCliente() {
        Nombre usuario = new Nombre();
        Tratamiento tratamiento;
        IdPersona id;

        System.out.println("\n...:::: Crear un nuevo Cliente ::::...\n");

        do {
            id = SelectorRut_Pasaporte();
        } while (id == null);

        do {
            tratamiento = SelectorTratamiento();
        } while (tratamiento == null);

        sc.nextLine();
        System.out.print("Nombres: ");
        usuario.setNombre(sc.nextLine());

        System.out.print("Apellido Paterno : ");
        usuario.setApellidoPaterno(sc.next());

        System.out.print("Apellido Materno : ");
        usuario.setApellidoMaterno(sc.next());

        System.out.print("Teléfono movil : ");
        String telefono = sc.next();

        System.out.print("Email : ");
        String email = sc.next();

        if (sistemaCentral.createCliente(id, usuario, telefono, email)) {
            System.out.println("\n...:::: Cliente guardado exitosamente ::::...");
        } else {
            System.out.println("\n...:::: Error al guardar cliente ::::...");
        }
    }

    private void createBus() {
        System.out.println("\n...:::: Creación de un nuevo BUS ::::...\n");

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
        System.out.println("\n...:::: Creación de un nuevo Viaje ::::...\n");

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
        IdPersona id;
        TipoDocumento tipo = null;

        System.out.println("\n....:::: Venta de pasajes ::::....\n");
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
        LocalDate fec = LocalDate.parse(fechaVenta, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        Nombre nombreCliente = new Nombre();

        System.out.println("\n:::: Datos del cliente\n");

        do {
            id = SelectorRut_Pasaporte();
        } while (id == null);

        System.out.print("Nombre Cliente [Sr/Sra. * * *] : ");
        sc.nextLine();
        String cliente = sc.nextLine();

        String[] nombres = cliente.split(" ");

        if (nombres[0].equals("Sr.")) {
            nombreCliente.setTratamiento(Tratamiento.SR);
        } else {
            nombreCliente.setTratamiento(Tratamiento.SRA);
        }
        nombreCliente.setNombre(nombres[1]);
        nombreCliente.setApellidoPaterno(nombres[2]);
        nombreCliente.setApellidoMaterno(nombres[3]);

        if (!sistemaCentral.iniciaVenta(idDocumento, tipo, fec, id)) {
            System.out.println(":::: Error! Cliente no existe!");
            return;
        }

        System.out.println("\n::::Pasajes a vender");
        System.out.print("Cantidad de pasajes : ");
        int cantidadPasajes = sc.nextInt();

        LocalDate fechaViaje;
        String[][] horariosDisponibles;

        do {
            System.out.print("Fecha de viaje[dd/mm/yyyy] : ");
            String fechaViajeSN = sc.next();
            fechaViaje = LocalDate.parse(fechaViajeSN, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            System.out.println("::::Listado de horarios disponibles");
            horariosDisponibles = sistemaCentral.getHorariosDisponibles(fechaViaje);

            if (horariosDisponibles.length == 0) {
                System.out.println(":::: Error! No hay horarios disponibles para la fecha seleccionada!");
            }
        } while (horariosDisponibles.length == 0);

        Viaje viajeAbordar = impresoraListadoViajes(new String[]{"|BUS", "|SALIDA", "|VALOR", "|ASIENTOS"}, horariosDisponibles, fechaViaje);

        if (viajeAbordar == null) {
            System.out.println(":::: Error! Viaje no existe!");
            return;
        }

        impresoraListadoAsientos(viajeAbordar.getAsientos());

        System.out.print("Seleccione sus asientos [separe por ,] : ");
        String asientos = sc.next();
        String[] split = asientos.split(",");

        for (int i = 0; i < cantidadPasajes; i++) {
            IdPersona id_pasajero;

            String asiento = split[i];
            System.out.println("\n:::: Datos del pasajero " + (i + 1));

            do {
                id_pasajero = SelectorRut_Pasaporte();
            } while (id_pasajero == null);

            if ((sistemaCentral.getNombrePasajero(id_pasajero) == null)) {
                System.out.print("Nombre [Sr/Sra. * * *] : ");

                sc.nextLine();
                String nombre = sc.nextLine();
                Nombre nombrePasajero = new Nombre();

                String[] nombres_1 = nombre.split(" ");

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

                System.out.print("Nombre de contacto [Sr/Sra. * * *] : ");
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

                sistemaCentral.createPasajero(id_pasajero, nombrePasajero, telefono, nombreContactoPasajero, telefonoContacto);
            }
            if (sistemaCentral.vendePasaje(idDocumento, tipo, viajeAbordar.getHora(), viajeAbordar.getFecha(), viajeAbordar.getBus().getPatente(), Integer.parseInt(asiento), id_pasajero)) {
                System.out.println(":::: Pasaje agregado exitosamente!");
            } else {
                System.out.println(":::: Error al agregar pasaje!");
                return;
            }
        }

        System.out.println(":::: Monto total de la venta : " + sistemaCentral.getMontoVenta(idDocumento, tipo));
        System.out.println("\n:::: Venta realizada exitosamente!");
        System.out.println(":::: Imprimiendo los pasajes");

        String[] x = sistemaCentral.pasajesAlImprimir(idDocumento, tipo);

        for (String s : x) {
            System.out.println(s);
        }
    }

    private void listPasajerosViaje() {
        System.out.println("\n...:::: Listado de pasajeros de un viaje ::::...\n");

        System.out.print("Fecha de viaje[dd/mm/yyyy] : ");
        String fechaViaje = sc.next();
        LocalDate fecha = LocalDate.parse(fechaViaje, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        System.out.print("Hora de viaje[hh:mm] : ");
        String horaViaje = sc.next();
        LocalTime hora = LocalTime.parse(horaViaje, DateTimeFormatter.ofPattern("HH:mm"));

        System.out.print("Patente del bus : ");
        String patente = sc.next();
        patente = patente.substring(0, 2) + patente.substring(3, 5) + patente.substring(6, 8);

        String[][] pasajeros_arreglo = sistemaCentral.listPasajeros(fecha, hora, patente);

        if (pasajeros_arreglo.length == 0) {
            System.out.println(":::: Error! No hay pasajeros para el viaje seleccionado!");
            return;
        }

        System.out.println("ASIENTO\tRUT - PASAPORTE\tNOMBRE\tNOMBRE CONTACTO\tTELEFONO CONTACTO");
        for (String[] pasajero : pasajeros_arreglo) {
            System.out.print(pasajero[0] + "\t");
            System.out.print(pasajero[1] + "\t");
            System.out.print(pasajero[2] + "\t");
            System.out.print(pasajero[3] + "\t");
            System.out.print(pasajero[4] + "\n");
        }
    }

    private void listVentas() {
        System.out.println("\n...:::: Listado de ventas ::::...\n");
        String[][] pasajeros_arreglo = sistemaCentral.listVentas();

        if (pasajeros_arreglo.length == 0) {
            System.out.println(":::: Error! No hay ventas");
            return;
        }

        System.out.println("ID DOCUMENT\tTIPO DOCU\tFECHA\tRUT - PASAPORTE\tCLIENTE\tCANT BOLETOS\tTOTAL VENTA");
        for (String[] pasajero : pasajeros_arreglo) {
            for (int x = 0; x < 7; x++) {
                System.out.print(pasajero[x] + "\t");
            }
            System.out.println();
        }
    }

    private void listViajes() {
        System.out.println("\n...:::: Listado de viajes ::::...\n");
        String[][] pasajeros_arreglo = sistemaCentral.listViajes();

        if (pasajeros_arreglo.length == 0) {
            System.out.println(":::: Error! No hay viajes");
            return;
        }

        System.out.println("FECHA\tHORA\tPRECIO\tDISPONIBLES\tPATENTE");
        for (String[] pasajero : pasajeros_arreglo) {
            for (int x = 0; x < 5; x++) {
                System.out.print(pasajero[x] + "\t");
            }
            System.out.println();
        }
    }

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

    private Tratamiento SelectorTratamiento() {
        System.out.print("Sr.[1] o Sra. [2] : ");

        int sr_o_sra = sc.nextInt();
        if (sr_o_sra == 1 || sr_o_sra == 2) {
            if (sr_o_sra == 1) {
                return Tratamiento.SR;
            } else {
                return Tratamiento.SRA;
            }
        } else {
            System.out.println("Error! Valor invalido!");
            return null;
        }
    }

    private Viaje impresoraListadoViajes(String[] cabeceras, String[][] arregloImpresora, LocalDate fecha) { // TODO ELIMINAR
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

        //SE CUENTA EL TOTAL DE CARACTERES DE LA CABECERA!

        for (String cabecera : cabeceras) {
            int espacios = contador_letras - cabecera.length();
            contador_todo += (espacios + cabecera.length());
        }

        // IMPRESION DE LA TABLA!
        for (z = 0; z < arregloImpresora.length; z++) {
            if (z == 0) {

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

                System.out.print("   ");
                for (String cabecera : cabeceras) {
                    int espacios = contador_letras - cabecera.length();
                    System.out.print(cabecera + " ".repeat(espacios));
                }

                System.out.println("|");
            }


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


        boolean viaje = sistemaCentral.createViaje(fecha, LocalTime.of(Integer.parseInt(arregloImpresora[seleccion - 1][1].substring(0, 2)), Integer.parseInt(arregloImpresora[seleccion - 1][1].substring(3, 5))), Integer.parseInt(arregloImpresora[seleccion - 1][2]), arregloImpresora[seleccion - 1][0]);

        if (viaje) {
            return null;
        } else {
            Bus bus = new Bus(arregloImpresora[seleccion - 1][0], Integer.parseInt(arregloImpresora[seleccion - 1][3]));
            return new Viaje(fecha, LocalTime.of(Integer.parseInt(arregloImpresora[seleccion - 1][1].substring(0, 2)), Integer.parseInt(arregloImpresora[seleccion - 1][1].substring(3, 5))), Integer.parseInt(arregloImpresora[seleccion - 1][2]), bus);
        }
    }

    private void impresoraListadoAsientos(String[][] listadoBuses) { // TODO Arreglar error en multiplos de 4 // usar String.delimiter
        int y = 3;
        int z;
        int q = 0;
        int contador_for = 4;
        int contador_f;
        int h = 1;

        // System.out.println(listadoBuses.length);
        System.out.println();

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

            } else {
                if (y != 5) {
                    System.out.print("| * ");
                } else {
                    System.out.print("|   ");
                    y = 0;
                    z -= 1;
                }

            }
            y++;
            q++;
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
package vista;
import controlador.SistemaVentaPasajes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@SuppressWarnings({"CanBeFinal", "FieldMayBeFinal"})
public class UISVP {

    // ATRIBUTOS (SINGLETON Y SCANNER)

    private static UISVP instancia = new UISVP();
    public static UISVP getInstancia(){
        return instancia;
    }
    private Scanner sc = new Scanner(System.in).useDelimiter("\t|\r\n|[\n\r\u2028\u2029\u0085]");

    // METODOS

    public void menu() {
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

        System.out.print("Nombres: ");
        usuario.setNombre(sc.next());

        System.out.print("Apellido Paterno : ");
        usuario.setApellidoPaterno(sc.next());

        System.out.print("Apellido Materno : ");
        usuario.setApellidoMaterno(sc.next());

        System.out.print("Teléfono movil : ");
        String telefono = sc.next();

        System.out.print("Email : ");
        String email = sc.next();

        if (SistemaVentaPasajes.getInstancia().createCliente(id, usuario, telefono, email)) {
            System.out.println("\n...:::: Cliente guardado exitosamente ::::...");
        } else {
            System.out.println("\n...:::: Error al guardar cliente ::::...");
        }
    }

    private void createBus() {
        System.out.println("\n...:::: Creación de un nuevo BUS ::::...\n");

        System.out.print("Patente : ");
        String patente = sc.next();

        System.out.print("Marca : ");
        String marca = sc.next();

        System.out.print("Modelo : ");
        String modelo = sc.next();

        System.out.print("Numero de asientos : ");
        int nroAsientos = sc.nextInt();

        if (SistemaVentaPasajes.getInstancia().createBus(patente, marca, modelo, nroAsientos)) {
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

        if (SistemaVentaPasajes.getInstancia().createViaje(fecha, hora, precio, patente)) {
            System.out.println("\n...:::: Viaje guardado exitosamente ::::...");
        } else {
            System.out.println("\n...:::: Error al guardar viaje ::::...");
        }
    }

    private void vendePasajes() {
        IdPersona id;
        TipoDocumento tipo = null;
        int selector = 1;

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

        System.out.println("\n:::: Datos del cliente\n");

        do {
            id = SelectorRut_Pasaporte();
        } while (id == null);

        System.out.print("Nombre Cliente [Sr/Sra. * * *] : "); //TODO ¿Para qué se solicita este dato?
        String cliente = sc.next();

        String[] nombres = cliente.split(" ");

        Nombre nombreCliente = new Nombre();

        if (nombres[0].equals("Sr.")) {
            nombreCliente.setTratamiento(Tratamiento.SR);
        } else {
            nombreCliente.setTratamiento(Tratamiento.SRA);
        }
        nombreCliente.setNombre(nombres[1]);
        nombreCliente.setApellidoPaterno(nombres[2]);
        nombreCliente.setApellidoMaterno(nombres[3]);

        if (!SistemaVentaPasajes.getInstancia().iniciaVenta(idDocumento, tipo, fec, id)) {
            System.out.println(":::: Error al iniciar venta!");
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
            horariosDisponibles = SistemaVentaPasajes.getInstancia().getHorariosDisponibles(fechaViaje);

            if (horariosDisponibles.length == 0) {
                System.out.println(":::: Error! No hay horarios disponibles para la fecha seleccionada!");
                System.out.print(":::: Desea seleccionar otra fecha? [1] Si [2] No : ");
                selector = sc.nextInt();
            }
            if (selector != 1) {
                return;
            }

        } while (horariosDisponibles.length == 0);

        int contador = 0;
        System.out.print("    *------------*------------*------------*------------*\n");
        System.out.printf("    | %10s | %10s | %10s | %10s |\n", "BUS", "SALIDA", "VALOR", "ASIENTOS");
        for (String[] horario : horariosDisponibles) {
            System.out.println("    |------------+------------+------------+------------|");
            System.out.printf(" %2s | %10s | %10s | %10s | %10s |\n", contador + 1, horario[0], horario[1], horario[2], horario[3]);
            contador++;
        }
        System.out.println("    *------------*------------*------------*------------*\n");

        System.out.print("Seleccione viaje en [1.." + contador + "] : ");
        int seleccion = sc.nextInt();

        String[] datosCompra = new String[4];

        for (int y = 0; y < seleccion; y++) {
            if (y == seleccion - 1) {
                datosCompra[0] = horariosDisponibles[y][0];
                datosCompra[1] = horariosDisponibles[y][1];
                datosCompra[2] = horariosDisponibles[y][2];
                datosCompra[3] = horariosDisponibles[y][3];
            }
        }

        int pasajesRedondeado = 1;

        while (Integer.parseInt(datosCompra[3]) >= pasajesRedondeado * 4) {
            pasajesRedondeado++;
        }
        String[] datos_viajeSelect = SistemaVentaPasajes.getInstancia().listAsientosDeViaje(fechaViaje, LocalTime.parse(datosCompra[1], DateTimeFormatter.ofPattern("HH:mm")), datosCompra[0]);

        System.out.println("*---*---*---*---*---*");
        int IndiceAsiento = 0;
        for (int y = 0; y < pasajesRedondeado-1; y++) {
            for (int x = 0; x <= 4; x++) {
                if (x == 2) {
                    System.out.print("|   ");
                } else {
                    if (IndiceAsiento < datos_viajeSelect.length) {
                        if (datos_viajeSelect[IndiceAsiento].equals("Ocupado")) {
                            System.out.print("|  *");
                        } else {
                            System.out.printf("|%3d", IndiceAsiento + 1);
                        }
                    } else {
                        System.out.print("|  *");
                    }
                    IndiceAsiento++;
                }
            }
            System.out.println("|");
        }

        System.out.println("*---*---*---*---*---*");

        System.out.print("Seleccione sus asientos [separe por ,] : ");

        String asientos = sc.next();
        String[] split = asientos.split(",");

        if (split.length != cantidadPasajes) {
            System.out.println(":::: Error! Cantidad de asientos no coincide con la cantidad de pasajes!");
            return;
        }

        for (int i = 0; i < cantidadPasajes; i++) {
            IdPersona id_pasajero;

            String asiento = split[i];
            System.out.println("\n:::: Datos del pasajero " + (i + 1));

            do {
                id_pasajero = SelectorRut_Pasaporte();
            } while (id_pasajero == null);

            if ((SistemaVentaPasajes.getInstancia().getNombrePasajero(id_pasajero) == null)) {
                System.out.print("Nombre [Sr/Sra. * * *] : ");

                String nombre = sc.next();
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
                String nombreContacto = sc.next();

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

                SistemaVentaPasajes.getInstancia().createPasajero(id_pasajero, nombrePasajero, telefono, nombreContactoPasajero, telefonoContacto);
            }
            if (SistemaVentaPasajes.getInstancia().vendePasaje(idDocumento, tipo,LocalTime.parse(datosCompra[1], DateTimeFormatter.ofPattern("HH:mm")), fechaViaje, datosCompra[0], Integer.parseInt(asiento), id_pasajero)) {
                System.out.println(":::: Pasaje agregado exitosamente!");
            } else {
                System.out.println(":::: Error al agregar pasaje!");
                return;
            }
        }

        System.out.println(":::: Monto total de la venta : " + SistemaVentaPasajes.getInstancia().getMontoVenta(idDocumento, tipo));
        System.out.println("\n:::: Venta realizada exitosamente!");
        System.out.println(":::: Imprimiendo los pasajes\n\n");

        String[] x = SistemaVentaPasajes.getInstancia().pasajesAlImprimir(idDocumento, tipo);

        for (String s : x) {
            System.out.println("-------------------- PASAJE ---------------------");
            System.out.println(s);
            System.out.println("------------------------------------------------\n\n");
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

        String[][] pasajeros_arreglo = SistemaVentaPasajes.getInstancia().listPasajeros(fecha, hora, patente);

        if (pasajeros_arreglo.length == 0) {
            System.out.println(":::: Error! No hay pasajeros para el viaje seleccionado!");
            return;
        }
        System.out.println("*----------*------------------*--------------------------*--------------------------*----------------------*");
        System.out.printf("| %8s | %16s | %24s | %24s | %20s |\n", "ASIENTO", "RUT/PASS", "PASAJERO", "CONTACTO","TELEFONO CONTACTO");
        for (String[] pasajero : pasajeros_arreglo) {
            System.out.println("*----------+------------------+----------------------------+------------------------+----------------------*");
            System.out.printf("| %8s |", pasajero[0]);
            System.out.printf(" %16s |", pasajero[1]);
            System.out.printf(" %-24s |",pasajero[2]);
            System.out.printf(" %-24s |", pasajero[3]);
            System.out.printf(" %-20s |\n", pasajero[4]);
        }
        System.out.println("*----------*------------------*--------------------------*--------------------------*----------------------*\n\n");
    }

    private void listVentas() {
        System.out.println("\n...:::: Listado de ventas ::::...\n");
        String[][] pasajeros_arreglo = SistemaVentaPasajes.getInstancia().listVentas();

        if (pasajeros_arreglo.length == 0) {
            System.out.println(":::: Error! No hay ventas");
            return;
        }

        System.out.println("*----------------*------------*------------------*------------------*--------------------------*------------------*----------------*");
        System.out.printf("| %14s | %10s | %16s | %16s | %24s | %16s | %14s |\n","ID DOCUMENT","TIPO DOCU","FECHA","RUT / PASAPORTE","CLIENTE","CANT BOLETOS","TOTAL VENTA");
        for (String[] pasajero : pasajeros_arreglo) {
            System.out.println("|----------------+------------+------------------+------------------+--------------------------+------------------+----------------|");
            System.out.printf("| %14s |", pasajero[0]);
            System.out.printf(" %10s |", pasajero[1]);
            System.out.printf(" %-16s |", pasajero[2]);
            System.out.printf(" %-16s |", pasajero[3]);
            System.out.printf(" %-24s |", pasajero[4]);
            System.out.printf(" %16s |", pasajero[5]);
            System.out.printf(" %14s |\n", pasajero[6]);
        }
        System.out.println("*----------------*------------*------------------*------------------*--------------------------*------------------*----------------*\n\n");
    }

    private void listViajes() {
        System.out.println("\n...:::: Listado de viajes ::::...\n");
        String[][] pasajeros_arreglo = SistemaVentaPasajes.getInstancia().listViajes();

        if (pasajeros_arreglo.length == 0) {
            System.out.println(":::: Error! No hay viajes");
            return;
        }

        System.out.println("*------------------*------------------*------------------*------------------*------------------*");
        System.out.printf("| %16s | %16s | %16s | %16s | %16s |\n", "FECHA", "HORA", "PRECIO", "DISPONIBLES", "PATENTE");
        for (String[] pasajero : pasajeros_arreglo) {
            System.out.println("*------------------+------------------+------------------+------------------+------------------*");
            for (int x = 0; x < 5; x++) {
                System.out.printf("| %16s ", pasajero[x]);
            }
            System.out.println("|");
        }
        System.out.println("*------------------*------------------*------------------*------------------*------------------*\n\n");
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

}

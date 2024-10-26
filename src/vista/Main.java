package vista;

@SuppressWarnings({"CanBeFinal", "FieldMayBeFinal", "", "SpellCheckingInspection"})
public class Main {
    public static void main(String[] args) {
        // SINGLETON
        UISVP.getInstancia().menu();
    }
    /*
    private void datosPrueba() {
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

        sistemaCentral.createViaje(LocalDate.parse("01/01/2021", DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalTime.parse("10:00", DateTimeFormatter.ofPattern("HH:mm")), 10000, "AABB12");
        sistemaCentral.createViaje(LocalDate.parse("01/01/2021", DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalTime.parse("12:00", DateTimeFormatter.ofPattern("HH:mm")), 10000, "BBCC23");
        sistemaCentral.createViaje(LocalDate.parse("01/01/2021", DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalTime.parse("14:00", DateTimeFormatter.ofPattern("HH:mm")), 10000, "CCDD34");
    }*/
}
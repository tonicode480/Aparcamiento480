package ejerciciojava;
import java.util.ArrayList;
import java.util.List;

public class Aparcamiento {
    private List<Vehiculo> vehiculos = new ArrayList<>();

    public void registrarVehiculo(Vehiculo vehiculo) {
        vehiculo.registrarEntrada();
        vehiculos.add(vehiculo);
    }

    public void registrarSalida(String matricula) {
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.matricula.equals(matricula)) {
                vehiculo.registrarSalida();
               
            }
        }
    }

}

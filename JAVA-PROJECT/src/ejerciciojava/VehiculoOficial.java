package ejerciciojava;

public class VehiculoOficial extends Vehiculo {
    public VehiculoOficial(String matricula) {
        super(matricula);
    }

    @Override
    public double calcularImporte() {
        // Los vehículos oficiales no pagan
        return 0;
    }
}


package ejerciciojava;

public class VehiculoNoResidente extends Vehiculo {
    public VehiculoNoResidente(String matricula) {
        super(matricula);
    }

    @Override
    public double calcularImporte() {
        long minutos = java.time.Duration.between(horaEntrada, horaSalida).toMinutes();
        return minutos * 0.02;
    }
}


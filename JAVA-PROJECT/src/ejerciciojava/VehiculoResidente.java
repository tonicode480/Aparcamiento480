package ejerciciojava;

public class VehiculoResidente extends Vehiculo {
    public VehiculoResidente(String matricula) {
        super(matricula);
    }

    @Override
    public double calcularImporte() {
        long minutos = java.time.Duration.between(horaEntrada, horaSalida).toMinutes();
        return minutos * 0.002;
    }
}

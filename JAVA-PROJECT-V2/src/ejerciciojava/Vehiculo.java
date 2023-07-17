package ejerciciojava;
import java.time.LocalTime;

public abstract class Vehiculo {
    protected String matricula;
    protected LocalTime horaEntrada;
    protected LocalTime horaSalida;

    public Vehiculo(String matricula) {
        this.matricula = matricula;
    }

    public void registrarEntrada() {
        this.horaEntrada = LocalTime.now();
    }

    public void registrarSalida() {
        this.horaSalida = LocalTime.now();
    }

    public abstract double calcularImporte();
}

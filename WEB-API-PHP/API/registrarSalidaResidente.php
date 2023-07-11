<?php
include 'database.php';

$matricula = $_POST['matricula'];

// Obtener la hora de entrada del vehículo y el tiempo acumulado estacionado
$query = "SELECT estancias.horaEntrada, vehiculos.tiempoEstacionado FROM estancias INNER JOIN vehiculos ON estancias.matricula = vehiculos.matricula WHERE estancias.matricula='$matricula'";
$result = $conn->query($query);
$row = $result->fetch_assoc();
$horaEntrada = new DateTime($row['horaEntrada']);
$tiempoAcumulado = $row['tiempoEstacionado'];

// Calcular el tiempo estacionado en minutos
$horaSalida = new DateTime();
$intervalo = $horaEntrada->diff($horaSalida);
$tiempoEstacionado = ($intervalo->days * 24 * 60) + ($intervalo->h * 60) + $intervalo->i;

// Sumar el tiempo de estancia al tiempo total acumulado
$tiempoTotal = $tiempoAcumulado + $tiempoEstacionado;

// Registrar la salida del vehículo residente y actualizar el tiempo total estacionado
$now = $horaSalida->format('Y-m-d H:i:s');
$query = "UPDATE estancias SET horaSalida='$now' WHERE matricula='$matricula'";
if ($conn->query($query) === TRUE) {
    $query = "UPDATE vehiculos SET tiempoEstacionado='$tiempoTotal' WHERE matricula='$matricula'";
    if ($conn->query($query) === TRUE) {
        echo "Salida registrada con éxito para el vehículo residente. Tiempo estacionado: $tiempoEstacionado minutos. Tiempo total acumulado: $tiempoTotal minutos";
    } else {
        echo "Error al actualizar el tiempo estacionado: " . $conn->error;
    }
} else {
    echo "Error al registrar la salida: " . $conn->error;
}

$conn->close();

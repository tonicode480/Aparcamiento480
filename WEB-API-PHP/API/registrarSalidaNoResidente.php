<?php
include 'database.php';

$matricula = $_POST['matricula'];

// Obtener la hora de entrada del vehículo
$query = "SELECT horaEntrada FROM estancias WHERE matricula='$matricula'";
$result = $conn->query($query);
$row = $result->fetch_assoc();
$horaEntrada = new DateTime($row['horaEntrada']);

// Calcular el tiempo estacionado en minutos
$horaSalida = new DateTime();
$intervalo = $horaEntrada->diff($horaSalida);
$tiempoEstacionado = ($intervalo->days * 24 * 60) + ($intervalo->h * 60) + $intervalo->i;

// Obtener el importe a pagar (0.02 EUR por minuto)
$importe = $tiempoEstacionado * 0.02;

// Registrar la salida del vehículo no residente
$now = $horaSalida->format('Y-m-d H:i:s');
$query = "UPDATE estancias SET horaSalida='$now' WHERE matricula='$matricula'";
if ($conn->query($query) === TRUE) {
    echo "Salida registrada con éxito para el vehículo no residente. Tiempo estacionado: $tiempoEstacionado minutos. Importe a pagar: $importe €";
} else {
    echo "Error al registrar la salida: " . $conn->error;
}

$conn->close();
?>

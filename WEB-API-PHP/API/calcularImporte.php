<?php
require 'database.php';

$matricula = $_GET['matricula'];

// Seleccionamos los datos del vehículo
$query = "SELECT v.tipo, v.tiempoEstacionado, p.precioPorMinuto
          FROM vehiculos v
          JOIN precios p ON v.tipo = p.tipo
          WHERE v.matricula = ?";

$stmt = $con->prepare($query);
$stmt->bind_param('s', $matricula);
$stmt->execute();

$result = $stmt->get_result();
$vehiculo = $result->fetch_assoc();

$importe = $vehiculo['tiempoEstacionado'] * $vehiculo['precioPorMinuto'];

echo json_encode(array("importe" => $importe));

$stmt->close();
$con->close();
?>

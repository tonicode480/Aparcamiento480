<?php
include 'database.php';

$matricula = $_POST["matricula"];

// Preparar la consulta SQL
$stmt = $conn->prepare("UPDATE estancias SET horaSalida=NOW() WHERE matricula=? AND horaSalida IS NULL");

// Enlazar los parámetros a la consulta
$stmt->bind_param("s", $matricula);

// Ejecutar la consulta
if ($stmt->execute()) {
    http_response_code(200); // OK
    echo "Salida registrada con éxito!";
} else {
    http_response_code(400); // Bad request
    echo "Error al registrar la salida: " . $stmt->error;
}

// Cerrar la consulta y la conexión
$stmt->close();
$conn->close();
?>

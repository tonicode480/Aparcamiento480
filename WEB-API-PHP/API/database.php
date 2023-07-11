<?php
$host = "localhost";
$db   = "parking480";
$user = "parking";
$pass = "parking";

// Crear la conexión
$conn = new mysqli($host, $user, $pass, $db);

// Verificar la conexión
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
?>

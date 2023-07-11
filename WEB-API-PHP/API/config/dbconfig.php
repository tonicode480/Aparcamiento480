<?php
$servername = "localhost";
$dbname = "parking480";
$username = "parking";
$password = "parking";

// Crear conexión
$conn = new mysqli($servername, $username, $password, $dbname);

// Comprobar conexión
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
?>

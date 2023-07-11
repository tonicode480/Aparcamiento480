<?php
include 'database.php';

$matricula = $_POST['matricula'];

// Consultar el tipo de vehiculo
$query = "SELECT tipo FROM vehiculos WHERE matricula='$matricula'";
$result = $conn->query($query);
$row = $result->fetch_assoc();
echo $row['tipo'];

$conn->close();
?>

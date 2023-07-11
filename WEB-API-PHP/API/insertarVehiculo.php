<?php
include 'database.php';

$matricula = $_POST['matricula'];
$tipo = $_POST['tipo']; // Oficial, Residente o No residente

$stmt = $conn->prepare("INSERT INTO vehiculos (matricula, tipo) VALUES (?, ?)");
$stmt->bind_param("ss", $matricula, $tipo);
$stmt->execute();
?>

<?php
include 'database.php';

$matricula = $_POST['matricula'];

$stmt = $conn->prepare("INSERT INTO estancias (matricula, horaEntrada) VALUES (?, NOW())");
$stmt->bind_param("s", $matricula);
$stmt->execute();
?>

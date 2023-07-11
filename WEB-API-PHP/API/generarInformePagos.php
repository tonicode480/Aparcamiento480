<?php
include 'database.php';

$query = "SELECT matricula, tiempoEstacionado, tiempoEstacionado * 0.002 AS totalPagar FROM vehiculos WHERE tipo = 'residente'";

$result = $conn->query($query);

if($result) {
    $file = fopen("pagosResidentes.csv", "w");

    // Agregar encabezados al archivo
    $headers = array("Matrícula", "Tiempo estacionado (min.)", "Cantidad a pagar");
    fputcsv($file, $headers);

    // Agregar datos al archivo
    while ($row = $result->fetch_assoc()) {
        $rowArray = array($row['matricula'], $row['tiempoEstacionado'], $row['totalPagar']);
        fputcsv($file, $rowArray);
    }

    fclose($file);

    echo "Archivo CSV generado con éxito!";
} else {
    echo "Error: " . $conn->error;
}

$conn->close();
?>

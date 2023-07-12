<?php
include 'database.php';

if(!isset($_GET['matricula'])) {
    die("No se proporcionó la matrícula");
}

$matricula = $_GET['matricula'];


// Iniciar transacción
$conn->begin_transaction();


try {
    // Verificar si el vehículo ya está registrado
    $stmt = $conn->prepare("SELECT COUNT(*) as count FROM vehiculos WHERE matricula = ?");
    $stmt->bind_param("s", $matricula);
    $stmt->execute();
    $result = $stmt->get_result();
    $row = $result->fetch_assoc();
    $count = $row['count'];

    if ($count > 0) {
        // El vehículo está registrado, proceder con el registro de entrada
        $stmt = $conn->prepare("INSERT INTO estancias (matricula, horaEntrada) VALUES (?, NOW())");
        $stmt->bind_param("s", $matricula);
        $stmt->execute();
    } else {
        // El vehículo no está registrado, registrar como vehículo no residemte
        $stmt = $conn->prepare("INSERT INTO vehiculos (matricula, tipo) VALUES (?, 'no residente')");
        $stmt->bind_param("s", $matricula);
        $stmt->execute();

        // Registrar entrada en la tabla estancias
        $stmt = $conn->prepare("INSERT INTO estancias (matricula, horaEntrada) VALUES (?, NOW())");
        $stmt->bind_param("s", $matricula);
        $stmt->execute();
    }


    $conn->commit();

} catch (mysqli_sql_exception $exception) {

    $conn->rollback();
    throw $exception;
} finally {
    
    $stmt->close();
    $conn->close();
}
?>

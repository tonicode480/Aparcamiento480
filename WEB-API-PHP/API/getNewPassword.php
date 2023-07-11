<?php
include 'database.php';

// Obtén el número de teléfono de la solicitud
$numero = $_GET['numero'];

// Obtén la nueva contraseña del usuario
$stmt = $conn->prepare("SELECT pass FROM usuarios WHERE telefono = ?");
$stmt->bind_param("s", $numero);
$stmt->execute();

$result = $stmt->get_result();
if ($result->num_rows == 1) {
    echo $result->fetch_assoc()['pass'];
} else {
    echo "Error al obtener la contraseña.";
}

$stmt->close();
$conn->close();
?>

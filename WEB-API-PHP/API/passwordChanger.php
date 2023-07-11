<?php
include 'database.php';

// Obtén el número de teléfono de la solicitud
$numero = $_GET['numero'];

// Genera una nueva contraseña
$nuevaContrasena = str_pad(rand(100000, 999999), 7, '0', STR_PAD_LEFT);


// Cambia la contraseña del usuario
$stmt = $conn->prepare("UPDATE usuarios SET pass = ? WHERE telefono = ?");
$stmt->bind_param("ss", $nuevaContrasena, $numero);
$stmt->execute();

if ($stmt->affected_rows == 1) {
    echo "La contraseña se ha cambiado correctamente.";
} else {
    echo "Error al cambiar la contraseña.";
}

$stmt->close();
$conn->close();
?>

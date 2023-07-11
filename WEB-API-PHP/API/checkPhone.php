<?php
include 'database.php';

$username = $_POST['user']; // Obtenemos el usuario del parámetro GET

$stmt = $conn->prepare('SELECT telefono FROM usuarios WHERE user = ?');
$stmt->bind_param('s', $username);

$stmt->execute();

$result = $stmt->get_result();
if($result->num_rows === 0) {
    http_response_code(404);
    echo 'Usuario no encontrado';
} else {
    $row = $result->fetch_assoc();
    echo $row['telefono'];
}

$stmt->close();

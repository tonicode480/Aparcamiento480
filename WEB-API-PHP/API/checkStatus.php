<?php
require_once './config/dbconfig.php';

header('Content-type: application/json');

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST['user'])) {
        $user = $_POST['user'];

        try {
            $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);

            // Configuramos PDO para lanzar excepciones cuando ocurra un error
            $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

            $stmt = $conn->prepare('SELECT isBlocked FROM usuarios WHERE user = :user');
            $stmt->bindParam(':user', $user);
            $stmt->execute();

            // Obtenemos el resultado
            $result = $stmt->fetch(PDO::FETCH_ASSOC);

            if ($result) {
                if ($result['isBlocked'] == 1) {
                    echo json_encode(['status' => 'blocked']);
                } else {
                    echo json_encode(['status' => 'unblocked']);
                }
            } else {
                echo json_encode(['error' => 'Usuario no encontrado']);
            }
        } catch(PDOException $e) {
            echo json_encode(['error' => 'Error de base de datos: ' . $e->getMessage()]);
        }
    } else {
        echo json_encode(['error' => 'Falta el usuario']);
    }
} else {
    echo json_encode(['error' => 'Método no permitido']);
}
?>

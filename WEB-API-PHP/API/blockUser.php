<?php
include_once './config/dbconfig.php';

if (isset($_GET['user'])) {
    $user = $_GET['user'];

    $sql = "UPDATE usuarios SET isBlocked = 1 WHERE user = ?";
    $stmt = $conn->prepare($sql);

    if ($stmt) {
        $stmt->bind_param("s", $user);
        $stmt->execute();

        if ($stmt->affected_rows > 0) {
            http_response_code(200);
            echo json_encode(array('status' => 'success', 'message' => 'User blocked successfully'));
        } else {
            http_response_code(404);
            echo json_encode(array('status' => 'fail', 'message' => 'User not found'));
        }
    } else {
        http_response_code(500);
        echo json_encode(array('status' => 'fail', 'message' => 'Could not prepare statement'));
    }
} else {
    http_response_code(400);
    echo json_encode(array('status' => 'fail', 'message' => 'Missing username'));
}

$conn->close();
?>

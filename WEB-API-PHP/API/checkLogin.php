<?php
include_once './config/dbconfig.php';

if(isset($_POST['user']) && isset($_POST['pass'])){
    $user = $_POST['user'];
    $pass = $_POST['pass'];

    $sql = "SELECT * FROM Usuarios WHERE user = ?";
    $stmt = $conn->prepare($sql);
    
    if($stmt){
        $stmt->bind_param("s", $user);
        $stmt->execute();

        $result = $stmt->get_result();
        $user = $result->fetch_assoc();

        if($user){
            if($user['pass'] == $pass) {
                http_response_code(200);
                echo json_encode(array('status'=>'success','message'=>'Logged in successfully'));
            } else {
                http_response_code(401);
                echo json_encode(array('status'=>'fail','message'=>'Incorrect password'));
            }
        } else {
            http_response_code(401);
            echo json_encode(array('status'=>'fail','message'=>'The entered user does not exist'));
        }
    } else {
        http_response_code(500);
        echo json_encode(array('status'=>'fail','message'=>'Could not prepare statement'));
    }
} else {
    http_response_code(400);
    echo json_encode(array('status'=>'fail','message'=>'Missing username or password'));
}

$conn->close();
?>

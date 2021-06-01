<?php
require_once "db_controller.php";
header("Content-Type: application/json");

if (!empty($_POST)) {
    $user_id = $_POST["user_id"];
    $balance = $_POST["balance"];
    $phone_number = $_POST["phone_number"];
    $address = $_POST["address"];

    $query = $conn->prepare("SELECT * FROM users WHERE user_id = ?");
    $query->bind_param("i", $user_id);
    $query->execute();
    $result = $query->get_result();

    if ($result->num_rows > 0) {
        $query = $conn->prepare("UPDATE users SET balance = ?, phone_number = ?, address = ? WHERE user_id = ?");
        $query->bind_param("dssi", $balance, $phone_number, $address, $user_id);
        $result = $query->execute();

        if ($result) {
            $response["message"] = "Data Updated";
        } else {
            $response["message"] = "Failed to save";
        }
    } else {
        $response["message"] = "Data not found";
    }
} else {
    $response["message"] = "No Post Data";
}

$query->close();
$conn->close();

echo json_encode($response);
<?php
require_once "db_controller.php";
header("Content-Type: application/json");

if (!empty($_POST)) {
    $user_id = $_POST["user_id"];
} else {
    $user_id = -1;
}

$query = $conn->prepare("SELECT * FROM users WHERE user_id = ?");
$query->bind_param("i", $user_id);
$query->execute();

$result = $query->get_result();
$data = $result->fetch_assoc();

if (!empty($data)) {
    $object = array(
        "user_id" => $data["user_id"],
        "name" => $data["name"],
        "email" => $data["email"],
        "balance" => $data["balance"],
        "phone_number" => $data["phone_number"],
        "address" => $data["address"]
    );

    $response["user"] = $object;
} else {
    $response["message"] = "Data not found";
}

$query->close();
$conn->close();

echo json_encode($response);
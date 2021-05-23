<?php
require_once "db_controller.php";
header("Content-Type: application/json");

if (!empty($_POST)) {
    $email = $_POST["email"];
    $password = $_POST["password"];
} else {
    $email = "";
    $password = "";
}

$query = $conn->prepare("SELECT * FROM users WHERE email = ? AND password = ?");
$query->bind_param("ss", $email, $password);
$query->execute();

$result = $query->get_result();
$data = $result->fetch_assoc();

if (!empty($data)) {
    $object = array(
        "user_id" => $data["user_id"],
        "name" => $data["name"],
        "email" => $data["email"],
    );

    $response["user"] = $object;
} else {
    $response["message"] = "Data not found";
}

$query->close();
$conn->close();

echo json_encode($response);
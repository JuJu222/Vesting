<?php
require_once "db_controller.php";
header("Content-Type: application/json");

if (!empty($_POST)) {
    $name = $_POST["name"];
    $email = $_POST["email"];
    $password = $_POST["password"];
    $phone_number = $_POST["phone_number"];
    $address = $_POST["address"];

    $query = $conn->prepare("INSERT INTO users (name, email, password, phone_number, address) VALUES (?, ?, ?, ?, ?)");
    $query->bind_param("sssss", $name, $email, $password, $phone_number, $address);
    $result = $query->execute();

    if ($result) {
        $response["message"] = "Data Created";
    } else {
        $response["message"] = "Failed to save";
    }
} else {
    $response["message"] = "No Post Data";
}

$query->close();
$conn->close();

echo json_encode($response);
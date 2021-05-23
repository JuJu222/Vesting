<?php
require_once "db_controller.php";
header("Content-Type: application/json");

if (!empty($_POST)) {
    $user_id = $_POST["user_id"];
} else {
    $user_id = -1;
}

$query1 = $conn->query("SELECT * FROM portfolio WHERE user_id = $user_id");
$query2 = $conn->query("SELECT balance FROM users WHERE user_id = $user_id");

$response["count"] = $query1->num_rows;
$result = $query2->fetch_assoc();
$balance = $result["balance"];
$response["balance"] = $balance;
$response["portfolio"] = array();

while ($data = mysqli_fetch_assoc($query1)) {
    $object = array(
      "portfolio_id" => $data["portfolio_id"],
      "symbol" => $data["symbol"],
      "lots" => $data["lots"],
      "price" => $data["price"],
      "user_id" => $data["user_id"]
    );

    array_push($response["portfolio"], $object);
}

$query1->close();
$conn->close();

echo json_encode($response);
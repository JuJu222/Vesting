<?php
require_once "db_controller.php";
header("Content-Type: application/json");

if (!empty($_POST)) {
    $symbol = $_POST["symbol"];
    $price = $_POST["price"];
    $user_id = $_POST["user_id"];
    $balance = $_POST["balance"];

    $query = $conn->prepare("SELECT * FROM portfolio WHERE symbol = ? && user_id = ?");
    $query->bind_param("si", $symbol, $user_id);
    $query->execute();
    $result = $query->get_result();

    if ($result->num_rows > 0) {
        $result = $result->fetch_assoc();
        $lots = $result["lots"] + 1;
        $price = ($result["price"] + $price) / 2;
        $query1 = $conn->prepare("UPDATE `portfolio` SET lots = ?, price = ? WHERE symbol = ?");
        $query1->bind_param("ids", $lots, $price, $symbol);
        $result1 = $query1->execute();

        $query2 = $conn->prepare("UPDATE `users` SET balance = ? WHERE user_id = ?");
        $query2->bind_param("di", $balance, $user_id);
        $result2 = $query2->execute();
        if ($result1 && $result2) {
            $response["message"] = "Data Updated";
        } else {
            $response["message"] = "Failed to save";
        }
    } else {
        $query1 = $conn->prepare("INSERT INTO portfolio (symbol, lots, price, user_id) VALUES (?, 1, ?, ?)");
        $query1->bind_param("sdi", $symbol, $price, $user_id);
        $result1 = $query1->execute();

        $query2 = $conn->prepare("UPDATE `users` SET balance = ? WHERE user_id = ?");
        $query2->bind_param("di", $balance, $user_id);
        $result2 = $query2->execute();

        if ($result1 && $result2) {
            $response["message"] = "Data Created";
        } else {
            $response["message"] = "Failed to save";
        }
    }
} else {
    $response["message"] = "No Post Data";
}

$conn->close();

echo json_encode($response);
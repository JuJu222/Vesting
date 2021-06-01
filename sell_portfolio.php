<?php
require_once "db_controller.php";
header("Content-Type: application/json");

if (!empty($_POST)) {
    $symbol = $_POST["symbol"];
    $lots = $_POST["lots"];
    $price = $_POST["price"];
    $user_id = $_POST["user_id"];
    $balance = $_POST["balance"];

    $query = $conn->prepare("SELECT * FROM portfolio WHERE symbol = ? && user_id = ?");
    $query->bind_param("si", $symbol, $user_id);
    $query->execute();
    $result = $query->get_result();

    if ($result->num_rows > 0) {
        $result = $result->fetch_assoc();

        $lotsA = $result["lots"] - $lots;
        if ($lotsA == 0) {
            $query1 = $conn->prepare("DELETE FROM portfolio WHERE portfolio_id = ?");
            $query1->bind_param("i", $result["portfolio_id"]);
            $result1 = $query1->execute();

            $query2 = $conn->prepare("UPDATE `users` SET balance = ? WHERE user_id = ?");
            $query2->bind_param("di", $balance, $user_id);
            $result2 = $query2->execute();

            if ($result1 && $result2) {
                $response["message"] = "Data Created";
            } else {
                $response["message"] = "Failed to save";
            }
        } else {
            $query1 = $conn->prepare("UPDATE `portfolio` SET lots = ?, price = ? WHERE symbol = ?");
            $query1->bind_param("ids", $lotsA, $price, $symbol);
            $result1 = $query1->execute();

            $query2 = $conn->prepare("UPDATE `users` SET balance = ? WHERE user_id = ?");
            $query2->bind_param("di", $balance, $user_id);
            $result2 = $query2->execute();

            if ($result1 && $result2) {
                $response["message"] = "Data Updated";
            } else {
                $response["message"] = "Failed to save";
            }
        }
    } else {
        $response["message"] = "Data not found";
    }
} else {
    $response["message"] = "No Post Data";
}

$conn->close();

echo json_encode($response);
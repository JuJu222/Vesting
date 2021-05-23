<?php
$host = "localhost";
$user = "root";
$pass = '';
$dbname = "vesting";

$conn = new mysqli($host, $user, $pass, $dbname);

if (!$conn) {
    die("Connection failed: " . $conn->connect_error);
}
<?php
define('db_user', 'hazim');
define('db_password', 'password');
define('db_host', 'localhost');
define('db_name', 'itt632');

$conn = mysqli_connect(db_host, db_user, db_password, db_name);
if (mysqli_connect_errno()) {
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$courseName = $_POST['courseName'];
$query = "UPDATE FROM course SET courseName='$courseName'";
$courseName = $_POST['courseName'];
echo "Car courseName: " . $courseName;

if (mysqli_query($conn, $query)=== TRUE) {
    echo "Record update successfully". "<br>". $courseName;
} else {
    echo "Error updating record:" . mysqli_error($conn);
}

mysqli_close($conn);
?>
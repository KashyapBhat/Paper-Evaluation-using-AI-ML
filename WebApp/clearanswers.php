<?php

include('conect.php');
error_reporting(1);
extract($_POST);
$cusnp = $_POST['cusnp'];
    if (file_exists($cusnp) && is_dir($cusnp)) {

    	array_map('unlink', glob($cusnp."/*.*"));
        rmdir($cusnp); 
		$query1 = "DELETE FROM `answer` WHERE `answer`.`usn` = '$cusnp'";
		mysqli_query($con,$query1);   

    } 
    else{
        echo "Data not present";
    }

    header('Location: ' . $_SERVER['HTTP_REFERER']); 
?>
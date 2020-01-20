<?php

include('conect.php');
extract($_POST);

  $query1 = "DELETE FROM `question` WHERE `question`.`qno` = '1';";
  $query2 = "DELETE FROM `question` WHERE `question`.`qno` = '2';";
  $query3 = "DELETE FROM `question` WHERE `question`.`qno` = '3';";
  $query4 = "DELETE FROM `question` WHERE `question`.`qno` = '4';"; 
  $query5 = "DELETE FROM `question` WHERE `question`.`qno` = '5';";
  $query6 = "DELETE FROM `question` WHERE `question`.`qno` = '6';";
  mysqli_query($con,$query1);
  mysqli_query($con,$query2);
  mysqli_query($con,$query3);
  mysqli_query($con,$query4);
  mysqli_query($con,$query5);
  mysqli_query($con,$query6);


    if (file_exists("submit1") && is_dir("submit1")) {
    	array_map('unlink', glob("submit1/*.*"));
        rmdir("submit1");

    } 
    if (file_exists("submit2") && is_dir("submit2")) {
    	array_map('unlink', glob("submit2/*.*"));
        rmdir("submit2");         
    } 
    if (file_exists("submit3") && is_dir("submit3")) {
    	array_map('unlink', glob("submit3/*.*"));
        rmdir("submit3");         
    } 
    if (file_exists("submit4") && is_dir("submit4")) {
    	array_map('unlink', glob("submit4/*.*"));
        rmdir("submit4");         
    } 
    if (file_exists("submit5") && is_dir("submit5")) {
    	array_map('unlink', glob("submit5/*.*"));
        rmdir("submit5");         
    } 
    if (file_exists("submit6") && is_dir("submit6")) {
    	array_map('unlink', glob("submit6/*.*"));
        rmdir("submit6");         
    }
    header('Location: ' . $_SERVER['HTTP_REFERER']); 
?>
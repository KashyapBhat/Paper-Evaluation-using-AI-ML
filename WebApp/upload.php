<?php

include('conect.php');
error_reporting(1);
extract($_POST);
    $error=array();

    //clear previos adds of images

    foreach($_POST as $nameaa => $content) {
    }

    if (!file_exists($nameaa) && !is_dir($nameaa)) {
        mkdir($nameaa);         
    } 

    $target_dir = $nameaa."/";
    $quesno = $_POST['quesnop'];
    $question = $_POST['quesp'];
    $keywords = $_POST['keywp'];
    $totalmarks = $_POST['tmarksp'];
    

    $extension=array("jpeg","jpg","png","gif");
    $i=0;
    $temp;

    foreach($_FILES["textp"]["tmp_name"] as $key=>$tmp_name)
            {   
                $i++;
                $file_name=$_FILES["textp"]["name"][$key];           //name: gets the name of the image
                $file_tmp=$_FILES["textp"]["tmp_name"][$key];        //tmp_name gets the path of the image

                $ext=pathinfo($file_name,PATHINFO_EXTENSION);
                if(in_array($ext,$extension))
                {
                    if(!file_exists($target_dir.$file_name))
                    {
                        $newFileName=$i.".".$ext;
                        move_uploaded_file($file_tmp=$_FILES["textp"]["tmp_name"][$key],$target_dir."$newFileName");
                        $tempp=$target_dir.$newFileName;
                        $temp=$temp.$tempp.",";
                        echo "$temp";
                    }
                    else
                    {
                        echo "File Exists";
                    }
                }
                else
                {
                    array_push($error,"$file_name, ");
                }

            }

            

  $query1 = "INSERT INTO `question`(`qno`, `question`, `keywords`, `totalmarks`, `textpic`) VALUES ('$quesno','$question','$keywords','$totalmarks','$temp')";
  mysqli_query($con,$query1);
           

header('Location: ' . $_SERVER['HTTP_REFERER']);
?>
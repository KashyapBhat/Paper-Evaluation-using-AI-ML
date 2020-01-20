<?php

include('conect.php');
error_reporting(1);
extract($_POST);
    $error=array();

    //clear previos adds of images

    foreach($_POST as $nameaa => $content) {
    }

   
 
    $usn = $_POST['usnp'];
    if (!file_exists($usn) && !is_dir($usn)) {
        mkdir($usn, 0777, true);         
    }
    
    $target_dir = $usn."/";
    $extension=array("jpeg","jpg","png","gif");
    $i1=0;
    $i2=0;
    $i3=0;
    $i4=0;
    $i5=0;
    $i6=0;

    foreach($_FILES["ans1p"]["tmp_name"] as $key=>$tmp_name)
            {   
                $i1++;
                $file_name=$_FILES["ans1p"]["name"][$key];           //name: gets the name of the image
                $file_tmp=$_FILES["ans1p"]["tmp_name"][$key];        //tmp_name gets the path of the image

                $ext=pathinfo($file_name,PATHINFO_EXTENSION);
                if(in_array($ext,$extension))
                {
                    if(!file_exists($target_dir.$file_name))
                    {
                        $newFileName="a".$i1.".".$ext;
                        move_uploaded_file($file_tmp=$_FILES["ans1p"]["tmp_name"][$key],$target_dir."$newFileName");
                        $tempp1=$target_dir.$newFileName;
                        $temp1=$temp1.$tempp1.",";
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
                $tempppp=$tempppp.",".$file_tmp;
            }

    foreach($_FILES["ans2p"]["tmp_name"] as $key=>$tmp_name)
            {   
                $i2++;
                $file_name=$_FILES["ans2p"]["name"][$key];           //name: gets the name of the image
                $file_tmp=$_FILES["ans2p"]["tmp_name"][$key];        //tmp_name gets the path of the image

                $ext=pathinfo($file_name,PATHINFO_EXTENSION);
                if(in_array($ext,$extension))
                {
                    if(!file_exists($target_dir.$file_name))
                    {
                        $newFileName="b".$i2.".".$ext;
                        move_uploaded_file($file_tmp=$_FILES["ans2p"]["tmp_name"][$key],$target_dir."$newFileName");
                        $tempp2=$target_dir.$newFileName;
                        $temp2=$temp2.$tempp2.",";
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

    foreach($_FILES["ans3p"]["tmp_name"] as $key=>$tmp_name)
            {   
                $i3++;
                $file_name=$_FILES["ans3p"]["name"][$key];           //name: gets the name of the image
                $file_tmp=$_FILES["ans3p"]["tmp_name"][$key];        //tmp_name gets the path of the image

                $ext=pathinfo($file_name,PATHINFO_EXTENSION);
                if(in_array($ext,$extension))
                {
                    if(!file_exists($target_dir.$file_name))
                    {
                        $newFileName="c".$i3.".".$ext;
                        move_uploaded_file($file_tmp=$_FILES["ans3p"]["tmp_name"][$key],$target_dir."$newFileName");
                        $tempp3=$target_dir.$newFileName;
                        $temp3=$temp3.$tempp3.",";
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

    foreach($_FILES["ans4p"]["tmp_name"] as $key=>$tmp_name)
            {   
                $i4++;
                $file_name=$_FILES["ans4p"]["name"][$key];           //name: gets the name of the image
                $file_tmp=$_FILES["ans4p"]["tmp_name"][$key];        //tmp_name gets the path of the image

                $ext=pathinfo($file_name,PATHINFO_EXTENSION);
                if(in_array($ext,$extension))
                {
                    if(!file_exists($target_dir.$file_name))
                    {
                        $newFileName="d".$i4.".".$ext;
                        move_uploaded_file($file_tmp=$_FILES["ans4p"]["tmp_name"][$key],$target_dir."$newFileName");
                        $tempp4=$target_dir.$newFileName;
                        $temp4=$temp4.$tempp4.",";
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

    foreach($_FILES["ans5p"]["tmp_name"] as $key=>$tmp_name)
            {   
                $i5++;
                $file_name=$_FILES["ans5p"]["name"][$key];           //name: gets the name of the image
                $file_tmp=$_FILES["ans5p"]["tmp_name"][$key];        //tmp_name gets the path of the image

                $ext=pathinfo($file_name,PATHINFO_EXTENSION);
                if(in_array($ext,$extension))
                {
                    if(!file_exists($target_dir.$file_name))
                    {
                        $newFileName="e".$i5.".".$ext;
                        move_uploaded_file($file_tmp=$_FILES["ans5p"]["tmp_name"][$key],$target_dir."$newFileName");
                        $tempp5=$target_dir.$newFileName;
                        $temp5=$temp5.$tempp5.",";
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

    foreach($_FILES["ans6p"]["tmp_name"] as $key=>$tmp_name)
            {   
                $i6++;
                $file_name=$_FILES["ans6p"]["name"][$key];           //name: gets the name of the image
                $file_tmp=$_FILES["ans6p"]["tmp_name"][$key];        //tmp_name gets the path of the image

                $ext=pathinfo($file_name,PATHINFO_EXTENSION);
                if(in_array($ext,$extension))
                {
                    if(!file_exists($target_dir.$file_name))
                    {
                        $newFileName="f".$i6.".".$ext;
                        move_uploaded_file($file_tmp=$_FILES["ans6p"]["tmp_name"][$key],$target_dir."$newFileName");
                        $tempp6=$target_dir.$newFileName;
                        $temp6=$temp6.$tempp6.",";
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


           
            $query1 = "INSERT INTO `answer`(`usn`, `q1`, `q2`, `q3`, `q4`, `q5`, `q6`) VALUES ('$usn','$temp1','$temp2','$temp3','$temp4','$temp5','$temp6')";
            mysqli_query($con,$query1);
            header('Location: ' . $_SERVER['HTTP_REFERER']);

?>
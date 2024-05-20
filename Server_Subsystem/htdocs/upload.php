<?php
	$vid_file_path = "files/" . basename($_FILES["file"]["name"]);	

    if(move_uploaded_file($_FILES['file']['tmp_name'], $vid_file_path)) {
        $success = true;
        $message = "File uploaded.";
        $filename = $_FILES["file"]["name"];
        $filetmp = $_FILES["file"]["tmp_name"];
    } else {
        $success = false;
        $message = "No file received.";
        $filename = "n/a";
        $filetmp = "n/a";
    }
    $response = array();

    $response["success"] = $success;
    $response["message"] = $message;
    $response["filename"] = $filename;
	$response["filetmp"] = $filetmp;
		
	echo json_encode($response);
	
	$command = escapeshellcmd('pythonw model_4.py ' . $filename . ' output/ 1>tmp1.txt 2>tmp2.txt');
	$output = shell_exec($command);
?>
<?php
	$lessons_path = "userlessons/";
	$lessons_file = $lessons_path . $_GET["file"];
		
	$response = array();
	
	$file_handle = fopen($lessons_file, "w");
	fwrite( $file_handle, $_GET["count"]);
	fclose( $file_handle );
	
	$file_handle = fopen($lessons_file, 'r');
	
	$response["lesson_count"] = fgets($file_handle);
	
	fclose($file_handle);
	
	echo json_encode( $response );
?>
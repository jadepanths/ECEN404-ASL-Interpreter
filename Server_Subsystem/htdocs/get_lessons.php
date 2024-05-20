<?php
	$lessons_path = "userlessons/";
	$lessons_file = $lessons_path . $_GET["file"];
		
	$response = array();
	
	if (is_file( $lessons_file )) {
		$lessons_file = $lessons_path . $_GET["file"];
	}
	else {
		$file_handle = fopen($lessons_file, "w");
		fwrite( $file_handle, "1");
		fclose( $file_handle );
	}
	
	$file_handle = fopen($lessons_file, 'r');
	
	$response["lesson_count"] = fgets($file_handle);
	
	fclose($file_handle);
	
	echo json_encode( $response );
?>
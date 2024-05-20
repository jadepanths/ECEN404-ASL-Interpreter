<?php
	$words_path = "userwords/";
	$words_file = $words_path . $_GET["file"];
		
	$response = array();
	
	if (is_file( $words_file )) {
		$words_file = $words_path . $_GET["file"];
	}
	else {
		$file_handle = fopen($words_file, "w");
		fwrite( $file_handle, "0");
		fclose( $file_handle );
	}
	
	$file_handle = fopen($words_file, 'r');
	
	$response["word_count"] = fgets($file_handle);
	
	fclose($file_handle);
	
	echo json_encode( $response );
?>
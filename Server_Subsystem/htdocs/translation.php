<?php
	$translation_path = "output/";
	$translation_file = "output/" . $_GET["file"];
		
	$response = array();
	
	$file_handle = fopen($translation_file, 'r');
	
	$response["translation"] = fgets($file_handle);
	$response["path"] = $translation_path;
	$response["file"] = $translation_file;
	
	fclose($file_handle);
	
	echo json_encode( $response );
?>
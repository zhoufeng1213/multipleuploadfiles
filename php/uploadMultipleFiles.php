<?php



//保存路径
$uploaddir = $_REQUEST['path'];

 //操作类型0：上传   1：删除
$type = $_REQUEST['type'];

$uploaddir = $uploaddir."/";
if(!file_exists($uploaddir))
{
	//mkdir($uploaddir);
	mkdir($uploaddir,0777,true);
}
if($type==0){
	if(isset($_REQUEST['fileCount'])){
		$isSave = true;
		$filecount = $_REQUEST['fileCount'];
		for($i = 0;$i < $filecount;$i++){
			//上传图片名称
			//$filename = $_REQUEST['filename'.$i];
			$filename = $_FILES['Filedata'.$i]['name'];
			$uploadfile = $uploaddir.basename($filename);
			$temploadfile = $_FILES['Filedata'.$i]['tmp_name'];
			if (!move_uploaded_file($temploadfile , $uploadfile)) {  
				$isSave = false;
				break;
			} 
		}
		if($isSave ){
			echo json_encode("true");
		}else{
			echo json_encode("false");
		}
	}else{
		//上传图片名称
		$filename = $_REQUEST['filename'];
		if($filename == '')
		{
			$filename = $_FILES['Filedata']['name'];
		}
		$uploadfile = $uploaddir.basename($filename);
		$temploadfile = $_FILES['Filedata']['tmp_name'];
		//move_uploaded_file($temploadfile , $uploadfile);//上传文件
		if (move_uploaded_file($temploadfile , $uploadfile)) {  
			echo json_encode("true");
		} else {
			echo json_encode("false");
		} 
	}
}else{
	$flag = unlink($uploaddir.$filename); //删除文件
	$flag1 = unlink($uploaddir.'small_'.$filename); //删除文件
}

?>
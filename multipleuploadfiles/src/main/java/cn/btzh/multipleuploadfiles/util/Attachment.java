package cn.btzh.multipleuploadfiles.util;

public class Attachment{

	private String filePath;//文件地址
	private String fileYasuoPath;//文件压缩后的路径
	private String fileName; //文件名称
	private String fileExtension; //文件扩展名称
	private boolean fileUploadState;// 文件上传状态

	public Attachment(String filePath,String fileYasuoPath) {
		this.filePath = filePath;
		this.fileYasuoPath = fileYasuoPath;
		fileHandle(filePath);
	}

	public Attachment(String filePath) {
		this.filePath = filePath;
		fileHandle(filePath);
	}

	public String getFileYasuoPath() {
		return fileYasuoPath;
	}

	public void setFileYasuoPath(String fileYasuoPath) {
		this.fileYasuoPath = fileYasuoPath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public boolean isFileUploadState() {
		return fileUploadState;
	}

	public void setFileUploadState(boolean fileUploadState) {
		this.fileUploadState = fileUploadState;
	}

	public void fileHandle(String filePath){
        fileExtension = filePath.substring(filePath.lastIndexOf(".") +1);
		fileName = filePath.substring(filePath.lastIndexOf("/")+1 , filePath.lastIndexOf("."));
	}


}
package io.netty.example.securechat;

public class FileRequestProtocol {
	
	public double fileSize;
	public String filePath;
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public double getFileSize() {
		return fileSize;
	}

	public void setFileSize(double fileSize) {
		this.fileSize = fileSize;
	}
}

package com.ohgiraffers.file;

/* 업로드 된 파일과 관련한 정보를 모아서 관리하는 DTO */
public class FileDTO {

    private String originalFileName;
    private String savedName;
    private String filePath;
    private String fileDescription;

    public FileDTO() {
    }

    public FileDTO(String originalFileName, String savedName, String filePath,
        String fileDescription) {
        this.originalFileName = originalFileName;
        this.savedName = savedName;
        this.filePath = filePath;
        this.fileDescription = fileDescription;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getSavedName() {
        return savedName;
    }

    public void setSavedName(String savedName) {
        this.savedName = savedName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileDescription() {
        return fileDescription;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }

    @Override
    public String toString() {
        return "FileDTO{" +
            "originalFileName='" + originalFileName + '\'' +
            ", savedName='" + savedName + '\'' +
            ", filePath='" + filePath + '\'' +
            ", fileDescription='" + fileDescription + '\'' +
            '}';
    }
}

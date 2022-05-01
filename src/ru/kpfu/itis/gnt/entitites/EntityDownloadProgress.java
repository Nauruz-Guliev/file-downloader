package ru.kpfu.itis.gnt.entitites;

import ru.kpfu.itis.gnt.entitites.EntityColors;

public class EntityDownloadProgress {
    private String fileName;
    private int progress;
    private long fileSize;
    private long downloadedSize;
    private String downloadStatus;

    public void setProgress(int progress) {this.progress = progress;}

    public void setFileSize(long fileSize) {this.fileSize = fileSize;}

    public void setDownloadedSize(long downloadedSize) {this.downloadedSize = downloadedSize;}

    public void setDownloadStatus (Boolean isCompleted, Boolean downloading) {
        if (!isCompleted && downloading) {
            downloadStatus = EntityColors.makeYellow("downloading...");
        } else if (!downloading && !isCompleted) {
            downloadStatus = EntityColors.makePurple("paused");
        } else {
            downloadStatus = EntityColors.makeGreen("completed");
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getProgress() {
        return progress;
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getDownloadedSize() {
        return downloadedSize;
    }

    public String getDownloadStatus() {
        return downloadStatus;
    }
}

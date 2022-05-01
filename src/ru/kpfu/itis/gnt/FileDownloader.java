package ru.kpfu.itis.gnt;

import ru.kpfu.itis.gnt.entitites.EntityDownloadProgress;

import javax.management.OperationsException;
import java.io.*;
import java.net.*;

public class FileDownloader implements Runnable {
    private final File fileToDownload;
    private final static int BUFFER_SIZE_ONE_KB = 1024;
    private boolean downloading;
    private long sizeOfFile;
    private final String fileName;
    private URLConnection urlconnection;
    private final URL url;
    private final EntityDownloadProgress entityDownloadProgress;

    public FileDownloader(String pathToDownloadInto, String urlToDownload, String fileName) throws IOException {
        this.fileToDownload = new File(pathToDownloadInto + fileName);
        this.url = new URL(urlToDownload);
        this.downloading = true;
        this.fileName = fileName;
        this.entityDownloadProgress = new EntityDownloadProgress();
        sizeOfFile();
    }

    @Override
    public void run(){
        try {
            startFileDownloading();
        } catch (IOException e) {
            System.out.println("exception from run() :" + e.getMessage());
        }
    }

    private void startFileDownloading() throws IOException {
        while (downloading && !isDownloadingCompleted()) {
            long range = 0;
            if (fileToDownload.exists()) {
                openUrlConnection();
                range = fileToDownload.length();
                urlconnection.setRequestProperty("Range", "bytes=" + range + "-");
                try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileToDownload, true));
                     BufferedInputStream in = new BufferedInputStream(urlconnection.getInputStream())
                ) {
                    download(in, out);
                }
            } else {
                try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileToDownload));
                     BufferedInputStream in = new BufferedInputStream(urlconnection.getInputStream())
                ) {
                    download(in, out);
                }
            }
        }
    }

    private void download(BufferedInputStream in, BufferedOutputStream out) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE_ONE_KB];
        int bytesRead = -1;
        while (downloading && (bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
    }

    private void sizeOfFile() throws IOException, UnsupportedOperationException {
        openUrlConnection();
        HttpURLConnection httpConnection = (HttpURLConnection) urlconnection;
        httpConnection.setRequestMethod("HEAD");
        sizeOfFile = httpConnection.getContentLengthLong();
    }

    private void openUrlConnection() throws IOException {
        urlconnection = url.openConnection();
    }

    private boolean isDownloadingCompleted() {
        if (sizeOfFile <= fileToDownload.length()) {
            downloading = false;
            return true;
        } else {
            return false;
        }
    }

    public void stopDownloading() throws OperationsException {
        downloading = false;
        if (isDownloadingCompleted()) {
            throw new OperationsException("File has been downloaded already.");
        }
    }

    public void startDownloading() throws OperationsException {
        if (!isDownloadingCompleted()) {
            downloading = true;
        } else {
            throw new OperationsException("File has been downloaded already.");
        }
    }

    public EntityDownloadProgress getEntityDownloadProgress() {
        // need to update the information before getting it
        entityDownloadProgress.setProgress((int) (fileToDownload.length() * 100 / sizeOfFile));
        entityDownloadProgress.setDownloadedSize(fileToDownload.length() / BUFFER_SIZE_ONE_KB);
        entityDownloadProgress.setFileSize(sizeOfFile / BUFFER_SIZE_ONE_KB);
        entityDownloadProgress.setDownloadStatus(isDownloadingCompleted(), downloading);
        entityDownloadProgress.setFileName(fileName);
        return entityDownloadProgress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileDownloader)) return false;
        FileDownloader that = (FileDownloader) o;
        return fileName.equals(that.fileName) && url.equals(that.url);
    }
}

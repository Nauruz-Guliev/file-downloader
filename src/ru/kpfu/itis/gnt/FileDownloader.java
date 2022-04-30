package ru.kpfu.itis.gnt;

import java.io.*;
import java.net.*;
import java.util.Objects;

public class FileDownloader implements Runnable {
    private static final String ANSI_RESET = "\u001b[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    private final File DOWNLOAD_FILE;
    private final static int BUFFER_SIZE = 1024;
    private boolean downloading;
    private long sizeOfFile;
    private String fileName;
    private URLConnection urlconnection;
    private URL url;
    private long percentsCompleted;

    public FileDownloader(String DOWNLOAD_PATH, String DOWNLOAD_URL, String fileName) throws IOException {
        this.DOWNLOAD_FILE = new File(DOWNLOAD_PATH + fileName);
        this.url = new URL(DOWNLOAD_URL);
        this.downloading = true;
        this.fileName = fileName;
        sizeOfFile();
    }

    @Override
    public void run() {
        try {
            startFileDownloading();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startFileDownloading() throws IOException {
        while (downloading && !isDownloadingCompleted()) {
            if (DOWNLOAD_FILE.exists()) {
                openUrlConnection();
                urlconnection.setRequestProperty("Range", "bytes=" + getDownloadedSoFarInBytes() + "-");
                try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(DOWNLOAD_FILE, true));
                     BufferedInputStream in = new BufferedInputStream(urlconnection.getInputStream())
                ) {
                    download(in, out);
                }
            } else {
                try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(DOWNLOAD_FILE));
                     BufferedInputStream in = new BufferedInputStream(urlconnection.getInputStream())
                ) {
                    download(in, out);
                }
            }
        }
    }

    public long getDownloadedSoFarInBytes() {
        return DOWNLOAD_FILE.length() / 1024;
    }

    private void download(BufferedInputStream in, BufferedOutputStream out) throws IOException {
        byte[] dataBuffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, BUFFER_SIZE)) != -1) {
            out.write(dataBuffer, 0, bytesRead);
        }
    }

    private void sizeOfFile() throws IOException {
        openUrlConnection();
        HttpURLConnection httpConnection = (HttpURLConnection) urlconnection;
        httpConnection.setRequestMethod("HEAD");
        sizeOfFile = httpConnection.getContentLengthLong();
    }

    public long getSizeOfFile() {
        return sizeOfFile / 1024;
    }

    public String getFileName() {
        return fileName;
    }

    private void openUrlConnection() throws IOException {
        urlconnection = url.openConnection();
    }

    public long getPercentsDownloaded() {
        percentsCompleted = 100 * getDownloadedSoFarInBytes() / getSizeOfFile();
        return percentsCompleted;
    }

    private boolean isDownloadingCompleted() {
        if (getSizeOfFile() <= getDownloadedSoFarInBytes()) {
            downloading = false;
            return true;
        } else {
            return false;
        }
    }

    public void stopDownloading() {
        downloading = false;
    }

    public void startDownloading() {
        if (!isDownloadingCompleted()) {
            downloading = true;
        }
    }

    public String isDownloading() {
        if (!isDownloadingCompleted() && downloading) {
            return ANSI_YELLOW + "downloading..." + ANSI_RESET;
        } else if (!downloading && !isDownloadingCompleted()) {
            return ANSI_PURPLE + "paused" + ANSI_RESET;
        } else {
            return ANSI_GREEN + "completed" + ANSI_RESET;
        }
    }

    @Override
    public String toString() {
        return "FileName=" + fileName.substring(1) +
                ", SizeOfFileDownloaded " + getDownloadedSoFarInBytes() +
                ". PercentsDownloaded=" + getPercentsDownloaded() +
                ", Status=" + isDownloading() +
                ", SizeOfFileInBytes=" + sizeOfFile + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileDownloader)) return false;
        FileDownloader that = (FileDownloader) o;
        return downloading == that.downloading && getSizeOfFile() == that.getSizeOfFile() && percentsCompleted == that.percentsCompleted && Objects.equals(DOWNLOAD_FILE, that.DOWNLOAD_FILE) && Objects.equals(fileName, that.fileName) && Objects.equals(urlconnection, that.urlconnection) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(DOWNLOAD_FILE, downloading, getSizeOfFile(), fileName, urlconnection, url, percentsCompleted);
    }
}

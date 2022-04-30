package ru.kpfu.itis.gnt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileDownloaderApp extends Application {
    private String download_url;
    private String download_file_path;
    private Scanner sc;
    private String fileName;
    private String userInput;
    public static final String ANSI_LIGHT_BLUE = "\u001B[36m";
    private static final String ANSI_RESET = "\u001b[0m";
    private final static String infoCommand = "INFO";
    private final static String pauseCommand = "PAUSE";
    private final static String resumeCommand = "RESUME";
    private ArrayList<FileDownloader> downloaders;
    private ArrayList<Thread> threads;

    @Override
    public void init() {
        download_file_path = "src/ru/kpfu/itis/gnt/downloads";
        downloaders = new ArrayList<>();
        threads = new ArrayList<>();
    }

    @Override
    public void start() {
        while (true) {
            try {
                System.out.print("Type in the URL to download, INFO to get the downloads info or either PAUSE or RESUME for certain downloading: ");
                sc = new Scanner(System.in).useDelimiter("\n");
                userInput = sc.next();
                if (checkIfInfo(userInput)){
                    printDownloadsInfo();
                } else if (checkIfPause(userInput)){
                    pauseDownloading();
                } else if (checkIfResume(userInput)){
                    resumeDownloading();
                } else {
                    initDownloadUrl(userInput);
                    initFileName();
                    downloaders.add(new FileDownloader(download_file_path, download_url, fileName));
                    threads.add(new Thread(downloaders.get(downloaders.size()-1)));
                    threads.get(threads.size()-1).start();
                }
                System.out.println();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    private boolean checkIfInfo(String str){
        return str.equals(infoCommand);
    }
    private boolean checkIfResume(String str){
        return str.equals(resumeCommand);
    }
    private void printDownloadsInfo(){
        for (int i = 0; i < downloaders.size(); i++) {
            System.out.printf("%-8s%-40s%32s%32s%25s%20s", "ID: " +colorP(String.valueOf(i)) + " ", "File Name: " + colorP(downloaders.get(i).getFileName().substring(1)) + " ", "Downloaded so far: " + colorP(String.valueOf(downloaders.get(i).getDownloadedSoFarInBytes()) +" KBs") + " ",
            "Total file size: " + colorP(String.valueOf(downloaders.get(i).getSizeOfFile()) +" KBs") + " ", "Status: " + downloaders.get(i).isDownloading() + " ", "Downloaded percentage: " + colorP(String.valueOf(downloaders.get(i).getPercentsDownloaded())+"%") +"\n");
        }
    }
    private void resumeDownloading() {
        printDownloadsInfo();
        System.out.print("Choose the downloading that you want to RESUME and type in its ID: ");
        sc = new Scanner(System.in).useDelimiter("\n");
        userInput = sc.next();
        downloaders.get(Integer.parseInt(userInput)).startDownloading();
    }
    private void pauseDownloading() {
        printDownloadsInfo();
        System.out.print("Choose the downloading that you want to PAUSE and type in its ID: ");
        sc = new Scanner(System.in).useDelimiter("\n");
        userInput = sc.next();
        downloaders.get(Integer.parseInt(userInput)).stopDownloading();
    }
    private boolean checkIfPause(String str) {
        return str.equals(pauseCommand);
    }

    private void initFileName() {
        fileName = download_url.substring(download_url.lastIndexOf("/"));
    }

    private void initDownloadUrl(String downloadUrl) {
        this.download_url = downloadUrl;
    }
    private String colorP(String str) {
        return ANSI_LIGHT_BLUE + str + ANSI_RESET;
    }
}

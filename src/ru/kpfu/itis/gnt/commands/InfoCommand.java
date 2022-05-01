package ru.kpfu.itis.gnt.commands;

import ru.kpfu.itis.gnt.entitites.EntityColors;
import ru.kpfu.itis.gnt.FileDownloader;

import java.util.ArrayList;
import java.util.Scanner;

public class InfoCommand implements Command{

    @Override
    public void execute(ArrayList<FileDownloader> downloaders, ArrayList<Thread> threads, Scanner sc) {
        if (downloaders.isEmpty()) {
            System.out.println(EntityColors.makeRed ("There is no ongoing downloading."));
        } else {
            for (int i = 0; i < downloaders.size(); i++) {
                System.out.printf("%-8s%-54s%-40s%-40s%-35s%-20s"
                        ,"ID: " + EntityColors.makeBlue(String.valueOf(i)) + " "
                        ,"File Name: " + EntityColors.makeBlue(downloaders.get(i).getEntityDownloadProgress().getFileName().substring(1)) + " "
                        ,"Downloaded so far: " + EntityColors.makeBlue(downloaders.get(i).getEntityDownloadProgress().getDownloadedSize() + " KBs") + " "
                        ,"Total file size: " + EntityColors.makeBlue(downloaders.get(i).getEntityDownloadProgress().getFileSize() + " KBs") + " "
                        ,"Status: " + downloaders.get(i).getEntityDownloadProgress().getDownloadStatus() + " "
                        ,"Downloaded percentage: " + EntityColors.makeBlue(downloaders.get(i).getEntityDownloadProgress().getProgress() + "%") + "\n");
            }
        }
    }
}

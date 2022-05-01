package ru.kpfu.itis.gnt.commands;

import ru.kpfu.itis.gnt.entitites.EntityColors;
import ru.kpfu.itis.gnt.FileDownloader;

import javax.management.OperationsException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ResumeCommand implements Command{
    private int threadIndexToRecreate;
    @Override
    public void execute(ArrayList<FileDownloader> downloaders, ArrayList<Thread> threads, Scanner sc) throws IOException, OperationsException {
        new InfoCommand().execute(downloaders, threads, sc);
        if (!downloaders.isEmpty()) {
            System.out.print("Choose the downloading that you want to RESUME and type in its ID: ");
            String userInput = sc.nextLine();
            threadIndexToRecreate = Integer.parseInt(userInput);
            downloaders.get(threadIndexToRecreate).startDownloading();
            threads.get(threadIndexToRecreate).interrupt();
            threads.remove(threadIndexToRecreate);
            threads.add(threadIndexToRecreate, new Thread(downloaders.get(threadIndexToRecreate)));
            threads.get(threadIndexToRecreate).start();
            System.out.println(EntityColors.makeGreen("File " + downloaders.get(Integer.parseInt(userInput)).getEntityDownloadProgress().getFileName().substring(1) + " will resume downloading."));
        }
    }
}

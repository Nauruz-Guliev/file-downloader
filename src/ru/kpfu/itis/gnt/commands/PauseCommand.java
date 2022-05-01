package ru.kpfu.itis.gnt.commands;

import ru.kpfu.itis.gnt.entitites.EntityColors;
import ru.kpfu.itis.gnt.FileDownloader;

import javax.management.OperationsException;
import java.util.ArrayList;
import java.util.Scanner;

public class PauseCommand implements Command{
    @Override
    public void execute(ArrayList<FileDownloader> downloaders, ArrayList<Thread> threads, Scanner sc) throws OperationsException {
        new InfoCommand().execute(downloaders,threads, sc);
        if (!downloaders.isEmpty()) {
            System.out.print("Choose the downloading that you want to PAUSE and type in its ID: ");
            String userInput = sc.nextLine();
            downloaders.get(Integer.parseInt(userInput)).stopDownloading();
            System.out.println(EntityColors.makeGreen("File " + downloaders.get(Integer.parseInt(userInput)).getEntityDownloadProgress().getFileName().substring(1) + " has been paused successfully."));
        }
    }
}

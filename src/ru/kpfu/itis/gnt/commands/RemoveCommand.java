package ru.kpfu.itis.gnt.commands;

import ru.kpfu.itis.gnt.entitites.EntityColors;
import ru.kpfu.itis.gnt.FileDownloader;

import javax.management.OperationsException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class RemoveCommand implements Command{

    @Override
    public void execute(ArrayList<FileDownloader> downloaders, ArrayList<Thread> threads, Scanner sc) throws IOException, OperationsException {
        new InfoCommand().execute(downloaders,threads, sc);
        if (!downloaders.isEmpty()) {
            System.out.print("Choose the downloading that you want to REMOVE and type in its ID: ");
            int userInput = Integer.parseInt(sc.nextLine());
            if(downloaders.get(userInput).getEntityDownloadProgress().getProgress() == 100) {
                System.out.println(EntityColors.makeGreen("File has been downloaded successfully and will be removed from the downloading list."));
            } else {
                System.out.println(EntityColors.makeRed("Downloading is not completed. File will be removed from the list but incomplete downloading will remain on your computer. Same link can be used later to continue downloading."));
            }
            downloaders.remove(userInput);
            threads.get(userInput).interrupt();
            threads.remove(userInput);
        }
    }
}

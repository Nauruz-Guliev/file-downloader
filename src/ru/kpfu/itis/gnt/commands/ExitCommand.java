package ru.kpfu.itis.gnt.commands;

import ru.kpfu.itis.gnt.FileDownloader;

import java.util.ArrayList;
import java.util.Scanner;

public class ExitCommand implements Command{
    @Override
    public void execute(ArrayList<FileDownloader> downloaders, ArrayList<Thread> threads, Scanner sc) {
        System.exit(0);
    }
}

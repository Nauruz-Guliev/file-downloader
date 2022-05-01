package ru.kpfu.itis.gnt.commands;

import ru.kpfu.itis.gnt.FileDownloader;

import javax.management.OperationsException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public interface Command {
    void execute(ArrayList<FileDownloader> downloaders, ArrayList<Thread> threads, Scanner sc) throws IOException, OperationsException;
}

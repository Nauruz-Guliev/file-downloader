package ru.kpfu.itis.gnt;

import ru.kpfu.itis.gnt.commands.*;
import ru.kpfu.itis.gnt.entitites.EntityColors;

import javax.management.OperationsException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileDownloaderApp extends Application {
    private String download_url;
    private String download_file_path;
    private Scanner sc;
    private String fileName;
    private ArrayList<FileDownloader> downloaders;
    private ArrayList<Thread> threads;
    private String[] commandNames;
    private Command[] commands;

    @Override
    public void init() {
        download_file_path = "src/ru/kpfu/itis/gnt/downloads";
        commandNames = new String[]{"INFO", "PAUSE", "RESUME", "EXIT", "REMOVE"};
        sc = new Scanner(System.in);
        commands = new Command[]{
                new InfoCommand(),
                new PauseCommand(),
                new ResumeCommand(),
                new ExitCommand(),
                new RemoveCommand()
        };
        downloaders = new ArrayList<>();
        threads = new ArrayList<>();
    }

    @Override
    public void start() {
        System.out.printf("%80s", "*** DOWNLOADER APP ***" + "\n\n");
        while (true) {
            try {
                System.out.print("Type in the URL or one of these commands: INFO, PAUSE, RESUME, REMOVE, EXIT: ");
                String userCommand = sc.nextLine();
                int commandIndex = 0;
                boolean found = false;
                for (String commandName : commandNames) {
                    if (userCommand.equals(commandName)) {
                        commands[commandIndex].execute(downloaders,threads, sc);
                        found = true;
                        break;
                    }
                    commandIndex++;
                }
                if (!found) {
                    initDownloadUrl(userCommand);
                    initFileName();
                    if (new FileDownloader(download_file_path, download_url, fileName).getEntityDownloadProgress().getFileSize() == 0) {
                        throw new UnsupportedOperationException("This url can not be downloaded.");
                    } else if (!downloaders.contains(new FileDownloader(download_file_path, download_url, fileName))) {
                        downloaders.add(new FileDownloader(download_file_path, download_url, fileName));
                        threads.add(new Thread(downloaders.get(downloaders.size() - 1)));
                        threads.get(threads.size() - 1).start();
                        System.out.println(EntityColors.makeGreen("File " + downloaders.get((downloaders.size())-1).getEntityDownloadProgress().getFileName().substring(1) + " is set to download."));
                    }  else{
                        throw new IllegalArgumentException("This url is already downloading.");
                    }
                }
            } catch (StringIndexOutOfBoundsException ex) {
                System.out.println(EntityColors.makeRed("Wrong input. Its neither a command nor a valid url."));
            } catch (IndexOutOfBoundsException ex) {
                if (downloaders.isEmpty()) {
                    System.out.println(EntityColors.makeRed("There is no ongoing downloading."));
                } else {
                    System.out.println(EntityColors.makeRed("There is no downloading with such an index."));
                }
            } catch (NumberFormatException ex) {
                System.out.println(EntityColors.makeRed("Input ID should be a number in range: " + "0-" + (downloaders.size()-1)));
            } catch (IOException | IllegalArgumentException | OperationsException | UnsupportedOperationException ex) {
                System.out.println(EntityColors.makeRed(ex.getMessage()));
            } catch (ArithmeticException ex) {
                downloaders.remove(downloaders.size()-1);
                threads.remove(threads.size()-1);
                System.out.println(EntityColors.makeRed("Unfortunately, your url can not be downloaded." ));
            }
        }
    }

    private void initFileName() {
        this.fileName = download_url.substring(download_url.lastIndexOf("/"));
    }

    private void initDownloadUrl(String downloadUrl) {
        this.download_url = downloadUrl;
    }
}

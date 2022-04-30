package ru.kpfu.itis.gnt;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        FileDownloaderApp app = new FileDownloaderApp();
        app.init();
        app.start();
    }
}

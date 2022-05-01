package ru.kpfu.itis.gnt;

public abstract class Application {

    public Application(){
        this.init();
        this.start();
    }
    public abstract void init();
    public abstract void start();
}

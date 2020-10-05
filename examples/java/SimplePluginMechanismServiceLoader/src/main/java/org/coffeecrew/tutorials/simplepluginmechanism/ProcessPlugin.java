package org.coffeecrew.tutorials.simplepluginmechanism;

public class ProcessPlugin implements Processable {

    @Override
    public String process(String processingToken) {
        return processingToken + "[PROCESS] " + this.getClass().getName() + "\n";
    }
}

package org.coffeecrew.tutorials.simplepluginmechanism;

public class PreProcessPlugin implements PreProcessable {

    @Override
    public String process(String processingToken) {
        return processingToken + "[PRE_PROCESS] " + this.getClass().getName() + "\n";
    }
}

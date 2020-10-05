package org.coffeecrew.tutorials.simplepluginmechanism;

public class PostProcessPlugin implements PostProcessable {

    @Override
    public String process(String processingToken) {
        return processingToken + "[POST_PROCESS] " + this.getClass().getName() + "\n";
    }
}

package org.coffeecrew.tutorials.simplepluginmechanism;

import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = Processable.class, position = 10)
public class ProcessPlugin implements Processable {

    @Override
    public String process(String processingToken) {
        return processingToken + "[PROCESS] " + this.getClass().getName() + "\n";
    }
}

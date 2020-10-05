package org.coffeecrew.tutorials.simplepluginmechanism;

import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = Processable.class)
public class MoreProcessPlugin implements Processable {

    @Override
    public String process(String processingToken) {
        return processingToken + "[PROCESS] " + this.getClass().getName() + "\n";
    }
}

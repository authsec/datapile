package org.coffeecrew.tutorials.simplepluginmechanism;

import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = PreProcessable.class)
public class PreProcessPlugin implements PreProcessable {

    @Override
    public String process(String processingToken) {
        return processingToken + "[PRE_PROCESS] " + this.getClass().getName() + "\n";
    }
}

package org.coffeecrew.tutorials.simplepluginmechanism;

import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = PostProcessable.class)
public class PostProcessPlugin implements PostProcessable {

    @Override
    public String process(String processingToken) {
        return processingToken + "[POST_PROCESS] " + this.getClass().getName() + "\n";
    }
}

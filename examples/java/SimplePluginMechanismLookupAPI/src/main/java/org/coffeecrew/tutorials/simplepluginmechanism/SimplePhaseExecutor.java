package org.coffeecrew.tutorials.simplepluginmechanism;

import java.util.Collection;
import org.openide.util.Lookup;

public class SimplePhaseExecutor {

    public static void main(String[] args) {
        String processingToken = "";

        for (final Phase p : Phase.values()) {
            final Collection<? extends Plugin> processables = Lookup.getDefault().lookupAll(p.getPhaseInterface());
            for (final Plugin plugin : processables) {
                processingToken = plugin.process(processingToken);
            }
        }
        System.out.println(processingToken);

    }
}

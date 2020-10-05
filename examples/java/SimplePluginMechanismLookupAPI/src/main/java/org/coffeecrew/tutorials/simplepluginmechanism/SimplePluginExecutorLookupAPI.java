package org.coffeecrew.tutorials.simplepluginmechanism;

import java.util.Collection;
import org.openide.util.Lookup;

public class SimplePluginExecutorLookupAPI {

    public static void main(String[] args) {
        String processingToken = "";

        final Collection<? extends PreProcessable> preProcessables = Lookup.getDefault().lookupAll(PreProcessable.class);
        for (final PreProcessable preProcessable : preProcessables) {
            processingToken = preProcessable.process(processingToken);
        }

        final Collection<? extends Processable> processables = Lookup.getDefault().lookupAll(Processable.class);
        for (final Processable processable : processables) {
            processingToken = processable.process(processingToken);
        }

        final Collection<? extends PostProcessable> postProcessables = Lookup.getDefault().lookupAll(PostProcessable.class);
        for (final PostProcessable postProcessable : postProcessables) {
            processingToken = postProcessable.process(processingToken);
        }

        System.out.println(processingToken);

    }
}

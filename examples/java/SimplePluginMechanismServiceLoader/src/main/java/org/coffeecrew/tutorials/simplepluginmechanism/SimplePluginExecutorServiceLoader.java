package org.coffeecrew.tutorials.simplepluginmechanism;

import java.util.ServiceLoader;

public class SimplePluginExecutorServiceLoader {

    public static void main(String[] args) {
        String processingToken = "";

        final ServiceLoader<PreProcessable> preProcessables = ServiceLoader.load(PreProcessable.class);
        for (final PreProcessable preProcessable : preProcessables) {
            processingToken = preProcessable.process(processingToken);
        }

        final ServiceLoader<Processable> processables = ServiceLoader.load(Processable.class);
        for (final Processable processable : processables) {
            processingToken = processable.process(processingToken);
        }

        final ServiceLoader<PostProcessable> postProcessables = ServiceLoader.load(PostProcessable.class);
        for (final PostProcessable postProcessable : postProcessables) {
            processingToken = postProcessable.process(processingToken);
        }

        System.out.println(processingToken);

    }
}

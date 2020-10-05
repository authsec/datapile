package org.coffeecrew.tutorials.simplepluginmechanism;

public enum Phase {

//    PRE_PROCESS(PreProcessable.class),
    PROCESS(Processable.class),
    POST_PROCESS(PostProcessable.class);
    private final Class<? extends Plugin> plugin;

    private Phase(Class<? extends Plugin> plugin) {
        this.plugin = plugin;
    }

    public Class<? extends Plugin> getPhaseInterface() {
        return plugin;
    }
}

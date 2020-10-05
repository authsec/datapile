package org.coffeecrew.tutorials.simplepluginmechanism;

/**
 Simple plugin interface.
 <p/>
 @author Jens Frey
 */
public interface Plugin {

    /**
     Simple worker method that will append data on the processingToken.
     <p/>
     And another.
     <p/>
     @param processingToken Token where data should be appended during
                            processing.
     <p/>
     @return processingToken that has been worked on.
     */
    public String process(String processingToken);
}

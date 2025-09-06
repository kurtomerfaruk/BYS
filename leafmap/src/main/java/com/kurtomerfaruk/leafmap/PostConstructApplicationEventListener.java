package com.kurtomerfaruk.leafmap;

import com.kurtomerfaruk.leafmap.utils.Constants;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.SystemEvent;
import jakarta.faces.event.SystemEventListener;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 5.09.2025 21:57
 */
public class PostConstructApplicationEventListener implements SystemEventListener {

    private static final Logger logger = Logger.getLogger(PostConstructApplicationEventListener.class.getName());

    @Override
    public void processEvent(SystemEvent se) throws AbortProcessingException {

        logger.log(Level.INFO, "Running on LeafMap {0}", Constants.VERSION);
    }

    @Override
    public boolean isListenerForSource(Object o) {
        return true;
    }
}

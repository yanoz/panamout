package fr.panamout.core.actors;

import akka.actor.UntypedActor;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yann on 5/6/15.
 */
public class Extractor extends UntypedActor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Extractor.class);

    @Override
    public void onReceive(Object message) throws Exception {
        Element link = (Element) message;
        LOGGER.info(link.data());
        //Document doc = Jsoup.connect((Element) message).get();

    }
}

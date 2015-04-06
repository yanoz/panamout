package fr.panamout.core.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.google.common.io.Files;
import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by yann on 4/6/15.
 */
public class Reader extends UntypedActor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Reader.class);

    private ActorRef getParser() {
        return getContext().actorFor("akka://import-system/user/parser");
    }


    @Override
    public void onReceive(Object message) throws Exception {
        Document document = (Document) message;
        Element root = document.getRootElement();
        LOGGER.info(root.getName());
    }
}

package fr.panamout.core.actors;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Created by yann on 4/6/15.
 */
public class Reader extends UntypedActor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Reader.class);

    private ActorSelection getExtractor() {
        return getContext().actorSelection("/user/extractor");
    }


    @Override
    public void onReceive(Object message) throws Exception {
        Document document = Jsoup.parse((File) message, Charset.defaultCharset().name());
        Elements links = document.getElementsByTag("a");
        for (Element e : links) {
            getExtractor().tell(e, null);
        }
    }
}

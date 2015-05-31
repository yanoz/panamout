package fr.panamout.core.actors;

import akka.actor.UntypedActor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
        String url = link.attr("href");

        Document doc = Jsoup.connect(url).get();
        Elements spans = doc.getElementsByTag("span");
        for (Element element : spans) {
            if (element.attr("itemprop")!= null && element.attr("itemprop").equals("streetAddress")) {
                LOGGER.info(element.text());
            }
        }


    }
}

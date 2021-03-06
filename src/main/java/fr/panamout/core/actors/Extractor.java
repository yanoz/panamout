package fr.panamout.core.actors;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import fr.panamout.domain.SpotBuilder;
import fr.panamout.domain.SpotCategory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yann on 5/6/15.
 */
public class Extractor extends UntypedActor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Extractor.class);

    private ActorSelection getSpoter() {
        return getContext().actorSelection("/user/spoter");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        Element link = (Element) message;
        URL url = new URL(link.attr("href"));
        switch (url.getHost()) {
            case "sortir.telerama.fr":
                extractTelerama(url);
                break;
            case "www.parisbouge.com":
                extractParisBouge(url);
                break;
            default:
                LOGGER.info(url.toString());
        }
    }

    private void extractParisBouge(URL url) throws IOException {
        Document doc = Jsoup.connect(url.toString()).userAgent("Mozilla").get();
        Elements title = doc.getElementsByTag("title");
        Elements address = doc.getElementsByTag("li");
        Pattern pAddr = Pattern.compile("(.*), ([0-9]{5}), Paris, France");


        SpotBuilder builder = new SpotBuilder();

        for (Element element : address) {
            if (element.attr("class").equals("place-address onelined")) {
                Matcher mAddress = pAddr.matcher(element.text());
                if (mAddress.find()) {
                    builder.located(mAddress.group(1));
                    builder.district(Integer.valueOf(mAddress.group(2)));
                    continue;
                }
            }
            if (element.attr("class").equals("title text-title onelined")) {
                builder.named(element.text());
            }
        }
        builder.url(url.toString());
        getSpoter().tell(builder.build(), getSelf());
    }

    private void extractTelerama(URL url) throws IOException {
        Document doc = Jsoup.connect(url.toString()).get();
        Elements metas = doc.getElementsByTag("meta");
        SpotBuilder builder = new SpotBuilder();
        Pattern pStreet = Pattern.compile("(.*)([0-9]{5}) Paris");

        for (Element element : metas) {
            if (element.attr("property")!= null && element.attr("property").equals("telerama:adresse")) {
                Matcher mStreet = pStreet.matcher(element.attr("content"));
                if (mStreet.find()) {
                    builder.located(mStreet.group(1));
                    builder.district(Integer.valueOf(mStreet.group(2)));
                    continue;
                }
            }
            if (element.attr("property")!= null && element.attr("property").equals("og:title")) {
                builder.named(element.attr("content"));
                continue;
            }
            if (element.attr("property")!= null && element.attr("property").equals("place:location:latitude")) {
                builder.lat(Double.valueOf(element.attr("content")));
                continue;
            }
            if (element.attr("property")!= null && element.attr("property").equals("place:location:longitude")) {
                builder.lng(Double.valueOf(element.attr("content")));
                continue;
            }
            if (element.attr("property")!= null && element.attr("property").equals("telerama:genre")) {
                if ("Bars".equals(element.attr("content"))) {
                    builder.category(SpotCategory.Bar);
                    continue;
                }
            }
           builder.url(url.toString());
        }
        getSpoter().tell(builder.build(), getSelf());
    }
}

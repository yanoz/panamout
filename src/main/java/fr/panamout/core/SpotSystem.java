package fr.panamout.core;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.SmallestMailboxPool;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import fr.panamout.core.actors.Persister;
import fr.panamout.core.actors.Spoter;
import fr.panamout.domain.Spot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yann on 5/7/15.
 */
public class SpotSystem {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportSystem.class);
    private static volatile SpotSystem singleton;

    private final ActorSystem system;

    public SpotSystem() {

        Config customConf = ConfigFactory.parseString("akka {" + //
                "     event-handlers = [\"akka.event.slf4j.Slf4jEventHandler\"]\n" + //
                "     loglevel = \"INFO\"\n" + //
                "     event-handler-level = \"INFO\"\n" + //
                "     jvm-exit-on-fatal-error = off" + //
                "}");
        system = ActorSystem.create("spot-system", ConfigFactory.load(customConf));

        system.actorOf(new SmallestMailboxPool(10).props(Props.create(Spoter.class)), "spoter");
        system.actorOf(new SmallestMailboxPool(10).props(Props.create(Persister.class)), "persister");
    }

    public static SpotSystem getInstance() {
        if (singleton == null) {
            singleton = new SpotSystem();
        }
        return singleton;
    }

    public void create(Spot spot) {
        system.actorSelection("/user/spoter").tell(spot, null);
    }

}

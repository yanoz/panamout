package fr.panamout.core;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.SmallestMailboxPool;
import akka.routing.SmallestMailboxRouter;
import com.google.common.io.ByteStreams;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import fr.panamout.core.actors.Parser;
import fr.panamout.core.actors.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by yann on 4/6/15.
 */
public class ImportSystem {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportSystem.class);
    private static volatile ImportSystem singleton;

    private final ActorSystem system;
    private final ActorRef reader;

    public ImportSystem() {
        Config customConf = ConfigFactory.parseString("akka {" + //
                "     event-handlers = [\"akka.event.slf4j.Slf4jEventHandler\"]\n" + //
                "     loglevel = \"INFO\"\n" + //
                "     event-handler-level = \"INFO\"\n" + //
                "     jvm-exit-on-fatal-error = off" + //
                "}");
        system = ActorSystem.create("import-system", ConfigFactory.load(customConf));

        reader = system.actorOf(Props.create(Reader.class), "reader");
        system.actorOf(new SmallestMailboxPool(10).props(Props.create(Parser.class)), "parser");
    }
    public static ImportSystem getInstance() {
        if (singleton == null) {
            singleton = new ImportSystem();
        }
        return singleton;
    }

    public void importFile(File file) throws IOException {
        reader.tell(file, null);
    }

    public void shutdown() {
        system.shutdown();
    }
}

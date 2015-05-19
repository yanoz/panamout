package fr.panamout.core.actors;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import fr.panamout.domain.Spot;

/**
 * Created by yann on 5/8/15.
 */
public class Persister extends AbstractActor {

    private void persist(Spot s) {
        
    }



    public Persister() {
        receive(ReceiveBuilder.match(Spot.class, s -> {persist(s);}).build());
    }


}

package nl.avisi.hawtio.issue;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;


public class ExampleRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        onException(Exception.class)
            .maximumRedeliveries(3)
            .redeliveryDelay(1000)
//            .backOffMultiplier(2)
//            .useExponentialBackOff()
            .log(LoggingLevel.WARN, "Processing failed, trying redelivery");

        from("jetty:http://0.0.0.0:9010/")
                .log(LoggingLevel.INFO, "Received message")
                .to("activemq:myqueue?disableReplyTo=true")
                .setBody(constant("enqueued"));

        from("activemq:myqueue?transacted=true&concurrentConsumers=3")
                .routeId(ExampleRoute.class.getSimpleName())
                .log(LoggingLevel.INFO, "dequeue");
    }
}



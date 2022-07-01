package com.lswebworld.rssbillreader.routes;

import com.lswebworld.rssbillreader.constants.ProcessorConstants;
import com.lswebworld.rssbillreader.dataobjects.EtlException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Rss Retrieval Route.
 */
@Component
public class RssRoute extends RouteBuilder {
  @Override
  public void configure() {

    onException(EtlException.class)
            .log("ERROR")
            .end();

    from("rss:https://www.legis.state.pa.us/WU01/LI/RSS/HouseBills.xml?splitEntries=false&delay=60000")
            .routeId("house-bills")
            .onCompletion()
                .log("Finished Pulling House Bills")
                .end()
            .log("Processing House Bills")
            .marshal().rss()
            .split().tokenizeXML("item").streaming()
            .parallelProcessing(true).threads(50, 250)
            .bean(ProcessorConstants.RSS_PROCESSOR)
            .bean(ProcessorConstants.BILL_HYDRATION_PROCESSOR)
            .bean(ProcessorConstants.DATABASE_PROCESSOR)
            .end();

    from("rss:https://www.legis.state.pa.us/WU01/LI/RSS/SenateBills.xml?splitEntries=false&delay=60000")
            .routeId("senate-bills")
            .onCompletion()
                .log("Finished Pulling Senate Bills")
                .end()
            .log("Processing Senate Bills")
            .marshal().rss()
            .split().tokenizeXML("item").streaming()
            .parallelProcessing(true).threads(50, 250)
            .bean(ProcessorConstants.RSS_PROCESSOR)
            .bean(ProcessorConstants.BILL_HYDRATION_PROCESSOR)
            .bean(ProcessorConstants.DATABASE_PROCESSOR)
            .end();
  }
}

package com.lswebworld.rssbillreader.routes;

import com.lswebworld.rssbillreader.constants.ProcessorConstants;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Rss Retrieval Route.
 */
@Component
public class RssRoute extends RouteBuilder {
  @Override
  public void configure() {

    from("rss:https://www.legis.state.pa.us/WU01/LI/RSS/HouseBills.xml?splitEntries=true")
            .routeId("RSS Feed Route")
            .log("Getting Entries")
            .bean(ProcessorConstants.RSS_PROCESSOR)
            .log("Done")
            .end();


  }
}

package com.lswebworld.rssbillreader.routes;

import com.lswebworld.rssbillreader.constants.ProcessorConstants;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Test Route for Rss Processor.
 */
@Component
public class RssProcessorTestRoute extends RouteBuilder {
  @Override
  public void configure() {

    from("direct:rss-input")
            .routeId("rss-processor-test-route")
            .bean(ProcessorConstants.RSS_PROCESSOR)
            .to("mock:rss-output")
            .end();

  }
}

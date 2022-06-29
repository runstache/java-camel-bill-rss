package com.lswebworld.rssbillreader.routes;

import com.lswebworld.rssbillreader.constants.ProcessorConstants;
import com.lswebworld.rssbillreader.dataobjects.EtlException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Schedule Processor Test Route.
 */
@Component
public class ScheduleProcessorTestRoute extends RouteBuilder {
  @Override
  public void configure() {

    onException(EtlException.class)
            .log("error")
            .to("mock:schedule-out")
            .end();

    from("direct:schedule-in")
            .routeId("schedule-processor-test-route")
            .bean(ProcessorConstants.SCHEDULE_PROCESSOR)
            .to("mock:schedule-out")
            .end();
  }
}

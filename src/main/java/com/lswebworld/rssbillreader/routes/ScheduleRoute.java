package com.lswebworld.rssbillreader.routes;

import com.lswebworld.rssbillreader.configuration.AppSettings;
import com.lswebworld.rssbillreader.constants.HeaderConstants;
import com.lswebworld.rssbillreader.constants.ProcessorConstants;
import com.lswebworld.rssbillreader.constants.ScheduleTypes;
import com.lswebworld.rssbillreader.dataobjects.EtlException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Main Route for pulling Schedule information.
 */
@Component
public class ScheduleRoute extends RouteBuilder {

  @Autowired
  AppSettings settings;

  @Override
  public void configure() {

    onException(EtlException.class)
            .log("ERROR")
            .bean(ProcessorConstants.ERROR_PROCESSOR)
            .handled(true)
            .end();

    from("rss:https://www.legis.state.pa.us/WU01/LI/RSS/CAL/HouseCalendarSS0reg.xml?splitEntries=false&delay=" + settings.getPollInterval())
            .routeId("house-schedule-route")
            .onCompletion()
                .log("Finished House Schedule Entries")
                .end()
            .log("Processing House Schedule")
            .setHeader(HeaderConstants.RECORD_SROUCE, constant("house-schedule"))
            .setProperty(HeaderConstants.SCHEDULE_TYPE, constant(ScheduleTypes.HOUSE))
            .marshal().rss()
            .split().tokenizeXML("item").streaming().parallelProcessing(true)
            .threads(5, 10)
            .bean(ProcessorConstants.SCHEDULE_PROCESSOR)
            .bean(ProcessorConstants.SCHEDULE_HYDRATION_PROCESSOR)
            .bean(ProcessorConstants.SCHEDULE_DB_PROCESSOR)
            .end();

    from("rss:https://www.legis.state.pa.us/WU01/LI/RSS/CAL/SenateCalendarSS0reg.xml?splitEntries=false&delay=" + settings.getPollInterval())
            .routeId("senate-schedule-route")
            .onCompletion()
            .log("Finished Senate Schedule Entries")
            .end()
            .log("Processing Senate Schedule")
            .setHeader(HeaderConstants.RECORD_SROUCE, constant("sentate-schedule"))
            .setProperty(HeaderConstants.SCHEDULE_TYPE, constant(ScheduleTypes.SENATE))
            .marshal().rss()
            .split().tokenizeXML("item").streaming().parallelProcessing(true)
            .threads(5, 10)
            .bean(ProcessorConstants.SCHEDULE_PROCESSOR)
            .bean(ProcessorConstants.SCHEDULE_HYDRATION_PROCESSOR)
            .bean(ProcessorConstants.SCHEDULE_DB_PROCESSOR)
            .end();
  }
}

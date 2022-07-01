package com.lswebworld.rssbillreader.processors;

import com.lswebworld.rssbillreader.dataobjects.ScheduleEntry;
import com.lswebworld.rssbillreader.repositories.ScheduleEntryRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Processor to keep the Schedule Entries up to date for the location
 * of a bill.
 */
public class ScheduleHydrationProcessor implements Processor {
  @Autowired
  ScheduleEntryRepository repo;

  @Transactional
  @Override
  public void process(Exchange exchange) {
    var entry = exchange.getMessage().getBody(ScheduleEntry.class);
    repo.deleteAllByIdentifier(entry.getIdentifier());
  }
}

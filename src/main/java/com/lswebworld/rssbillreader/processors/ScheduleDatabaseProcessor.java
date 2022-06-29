package com.lswebworld.rssbillreader.processors;

import com.lswebworld.rssbillreader.dataobjects.ScheduleEntry;
import com.lswebworld.rssbillreader.repositories.ScheduleEntryRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Processor for Saving Schedule Entries to the Database.
 */

public class ScheduleDatabaseProcessor implements Processor {

  @Autowired
  ScheduleEntryRepository repo;

  @Override
  public void process(Exchange exchange) {
    var item = exchange.getMessage().getBody(ScheduleEntry.class);
    repo.save(item);
  }
}

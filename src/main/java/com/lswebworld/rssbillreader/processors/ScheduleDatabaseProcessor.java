package com.lswebworld.rssbillreader.processors;

import com.lswebworld.bills.data.dataobjects.ScheduleInfo;
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
    var item = exchange.getMessage().getBody(ScheduleInfo.class);
    repo.save(item);
  }
}

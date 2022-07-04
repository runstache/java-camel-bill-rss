package com.lswebworld.rssbillreader.processors;

import com.lswebworld.bills.data.dataobjects.ScheduleInfo;
import com.lswebworld.rssbillreader.repositories.ScheduleEntryRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.ObjectUtils;
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
    var entry = exchange.getMessage().getBody(ScheduleInfo.class);

    var results =
            repo.findAllByIdentifierAndScheduleType(entry.getIdentifier(), entry.getScheduleType());
    if (ObjectUtils.isNotEmpty(results)) {
      repo.deleteAll(results);
    }
  }
}

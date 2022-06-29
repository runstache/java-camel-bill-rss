package com.lswebworld.rssbillreader.processors;

import com.lswebworld.rssbillreader.dataobjects.BillInfo;
import com.lswebworld.rssbillreader.repositories.BillDataRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Processor to save items to the Postgres Database.
 */
public class DatabaseProcessor implements Processor {
  @Autowired
  BillDataRepository repo;

  @Override
  public void process(Exchange exchange) {

    var bill = exchange.getMessage().getBody(BillInfo.class);
    repo.save(bill);
  }
}

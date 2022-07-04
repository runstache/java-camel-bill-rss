package com.lswebworld.rssbillreader.processors;

import com.lswebworld.bills.data.dataobjects.BillInfo;
import com.lswebworld.rssbillreader.repositories.BillDataRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Processor to Hydrate a new Bill entry with the original creation date if present.
 */
public class BillHydrationProcessor implements Processor {

  @Autowired
  BillDataRepository repo;

  @Transactional
  @Override
  public void process(Exchange exchange) {
    var bill = exchange.getMessage().getBody(BillInfo.class);
    if (repo.existsById(bill.getIdentifier())) {
      var original = repo.getById(bill.getIdentifier());
      var zdt = original.getCreatedOn();
      bill.setCreatedOn(zdt);
    }
  }
}

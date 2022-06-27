package com.lswebworld.rssbillreader.processors;

import com.lswebworld.rssbillreader.dataobjects.BillInfo;
import com.lswebworld.rssbillreader.tranformers.Transformer;
import com.rometools.rome.feed.synd.SyndFeed;
import java.net.URISyntaxException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Processor for handling Rss Entries.
 */
@Slf4j
public class RssProcessor implements Processor {

  @Autowired
  Transformer transformer;

  @Override
  public void process(Exchange exchange) throws URISyntaxException {
    var body = exchange.getMessage().getBody(SyndFeed.class);
    List<BillInfo> bills = transformer.transform(body);
    exchange.getMessage().setBody(bills);
  }
}

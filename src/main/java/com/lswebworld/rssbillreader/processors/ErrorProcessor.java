package com.lswebworld.rssbillreader.processors;

import com.lswebworld.rssbillreader.constants.HeaderConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Processor for printing errors.
 * Processor for printing errors.
 */
@Slf4j
public class ErrorProcessor implements Processor {

  @Override
  public void process(Exchange exchange) {

    var source = exchange.getMessage().getHeader(HeaderConstants.RECORD_SROUCE).toString();
    var exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

    log.error("ERROR PROCESSING RECORD FROM " + source);
    log.error(exchange.getMessage().getBody().toString());
    log.error(exception.getMessage());


  }
}

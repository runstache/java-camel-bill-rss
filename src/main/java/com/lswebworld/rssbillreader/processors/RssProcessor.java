package com.lswebworld.rssbillreader.processors;

import com.lswebworld.rssbillreader.constants.ErrorConstants;
import com.lswebworld.rssbillreader.dataobjects.BillInfo;
import com.lswebworld.rssbillreader.dataobjects.EtlException;
import com.lswebworld.rssbillreader.tranformers.Transformer;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Processor for handling Rss Entries.
 */
@Slf4j
public class RssProcessor implements Processor {

  @Autowired
  @Qualifier("RssTransformer")
  Transformer<BillInfo> transformer;

  @Override
  public void process(Exchange exchange) throws EtlException {

    try {
      var body = exchange.getMessage().getBody().toString();
      var bill = transformer.transform(body);
      if (bill.isPresent()) {
        exchange.getMessage().setBody(bill.get());
      } else {
        throw new EtlException(ErrorConstants.BILL_TRANSFORM_FAILURE);
      }
    } catch (URISyntaxException ex) {
      throw new EtlException(ErrorConstants.BILL_TRANSFORM_FAILURE, ex);
    }

  }
}

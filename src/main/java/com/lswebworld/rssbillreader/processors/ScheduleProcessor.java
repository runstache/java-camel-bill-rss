package com.lswebworld.rssbillreader.processors;

import com.lswebworld.bills.data.dataobjects.ScheduleInfo;
import com.lswebworld.rssbillreader.constants.ErrorConstants;
import com.lswebworld.rssbillreader.constants.HeaderConstants;
import com.lswebworld.rssbillreader.dataobjects.EtlException;
import com.lswebworld.rssbillreader.tranformers.Transformer;
import java.net.URISyntaxException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Processor for converting Schedule Entries to the Database.
 */
public class ScheduleProcessor implements Processor {

  @Autowired
  @Qualifier("ScheduleTransformer")
  Transformer<ScheduleInfo> transformer;

  @Override
  public void process(Exchange exchange) throws EtlException {

    try {
      var body = exchange.getMessage().getBody().toString();
      var item = transformer.transform(body);
      if (item.isPresent()) {
        if (ObjectUtils.isNotEmpty(exchange.getProperty(HeaderConstants.SCHEDULE_TYPE))) {
          item.get().setScheduleType(
                  exchange.getProperty(HeaderConstants.SCHEDULE_TYPE).toString());
        } else {
          throw new EtlException(ErrorConstants.MISSING_SCHEDULE_TYPE);
        }
        exchange.getMessage().setBody(item.get());
      } else {
        throw new EtlException(ErrorConstants.SCHEDULE_TRANSFORM_FAILURE);
      }
    } catch (URISyntaxException ex) {
      throw new EtlException(ErrorConstants.SCHEDULE_TRANSFORM_FAILURE, ex);
    }
  }
}

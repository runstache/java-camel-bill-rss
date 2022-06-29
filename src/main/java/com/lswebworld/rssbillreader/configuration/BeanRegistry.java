package com.lswebworld.rssbillreader.configuration;

import com.lswebworld.rssbillreader.constants.ProcessorConstants;
import com.lswebworld.rssbillreader.dataobjects.BillInfo;
import com.lswebworld.rssbillreader.dataobjects.ScheduleEntry;
import com.lswebworld.rssbillreader.processors.DatabaseProcessor;
import com.lswebworld.rssbillreader.processors.RssProcessor;
import com.lswebworld.rssbillreader.tranformers.RssTransformer;
import com.lswebworld.rssbillreader.tranformers.ScheduleTransformer;
import com.lswebworld.rssbillreader.tranformers.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application Bean Registry.
 */
@Configuration
public class BeanRegistry {

  @Bean(name = ProcessorConstants.RSS_PROCESSOR)
  public RssProcessor rssProcessor() {
    return new RssProcessor();
  }

  @Bean(name = "RssTransformer")
  public Transformer<BillInfo> transformer() {
    return new RssTransformer();
  }

  @Bean(name = "ScheduleTransformer")
  @Autowired
  public Transformer<ScheduleEntry> scheduleTransformer(AppSettings settings) {
    return new ScheduleTransformer(settings);
  }

  @Bean(name = ProcessorConstants.DATABASE_PROCESSOR)
  public DatabaseProcessor databaseProcessor() {
    return new DatabaseProcessor();
  }

}

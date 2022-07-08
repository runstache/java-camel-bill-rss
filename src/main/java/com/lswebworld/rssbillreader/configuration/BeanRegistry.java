package com.lswebworld.rssbillreader.configuration;

import com.codahale.metrics.MetricRegistry;
import com.lswebworld.bills.data.dataobjects.BillInfo;
import com.lswebworld.bills.data.dataobjects.ScheduleInfo;
import com.lswebworld.rssbillreader.constants.ProcessorConstants;
import com.lswebworld.rssbillreader.processors.BillHydrationProcessor;
import com.lswebworld.rssbillreader.processors.DatabaseProcessor;
import com.lswebworld.rssbillreader.processors.ErrorProcessor;
import com.lswebworld.rssbillreader.processors.RssProcessor;
import com.lswebworld.rssbillreader.processors.ScheduleDatabaseProcessor;
import com.lswebworld.rssbillreader.processors.ScheduleHydrationProcessor;
import com.lswebworld.rssbillreader.processors.ScheduleProcessor;
import com.lswebworld.rssbillreader.tranformers.RssTransformer;
import com.lswebworld.rssbillreader.tranformers.ScheduleTransformer;
import com.lswebworld.rssbillreader.tranformers.Transformer;
import org.apache.camel.CamelContext;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicyFactory;
import org.apache.camel.spring.boot.CamelContextConfiguration;
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
  public Transformer<ScheduleInfo> scheduleTransformer(AppSettings settings) {
    return new ScheduleTransformer(settings);
  }

  @Bean(name = ProcessorConstants.DATABASE_PROCESSOR)
  public DatabaseProcessor databaseProcessor() {
    return new DatabaseProcessor();
  }

  @Bean(name = ProcessorConstants.SCHEDULE_PROCESSOR)
  public ScheduleProcessor scheduleProcessor() {
    return new ScheduleProcessor();
  }

  @Bean(name = ProcessorConstants.SCHEDULE_DB_PROCESSOR)
  public ScheduleDatabaseProcessor scheduleDatabaseProcessor() {
    return new ScheduleDatabaseProcessor();
  }

  @Bean(name = ProcessorConstants.BILL_HYDRATION_PROCESSOR)
  public BillHydrationProcessor billHydrationProcessor() {
    return new BillHydrationProcessor();
  }

  @Bean(name = ProcessorConstants.SCHEDULE_HYDRATION_PROCESSOR)
  public ScheduleHydrationProcessor scheduleHydrationProcessor() {
    return new ScheduleHydrationProcessor();
  }

  @Bean(name = "MetricRegistry")
  public MetricRegistry metricRegistry() {
    return new MetricRegistry();
  }

  @Bean(name = ProcessorConstants.ERROR_PROCESSOR)
  public ErrorProcessor errorProcessor() {
    return new ErrorProcessor();
  }

  /**
   * Registers a Metric Registry to the Camel Context.
   *
   * @param registry Metric Registry.
   * @return Camel Context Configuration
   */
  @Bean(name = "CamelContextConfiguration")
  public CamelContextConfiguration camelContextConfiguration(MetricRegistry registry) {
    return new CamelContextConfiguration() {
      @Override
      public void beforeApplicationStart(CamelContext camelContext) {
        var factory = new MetricsRoutePolicyFactory();
        factory.setUseJmx(true);
        factory.setMetricsRegistry(registry);
        camelContext.addRoutePolicyFactory(factory);
      }

      @Override
      public void afterApplicationStart(CamelContext camelContext) {
        // No Post Start Items
      }
    };
  }
}

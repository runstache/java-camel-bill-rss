package com.lswebworld.rssbillreader.configuration;

import com.lswebworld.rssbillreader.constants.ProcessorConstants;
import com.lswebworld.rssbillreader.processors.RssProcessor;
import com.lswebworld.rssbillreader.tranformers.RssTransformer;
import com.lswebworld.rssbillreader.tranformers.Transformer;
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

  @Bean(name = "Transformer")
  public Transformer transformer() {
    return new RssTransformer();
  }

}

package com.lswebworld.rssbillreader.processors;

import static org.assertj.core.api.Assertions.assertThat;

import com.lswebworld.rssbillreader.dataobjects.BillInfo;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@Slf4j
class RssProcessorTest {

  @EndpointInject("direct:rss-input")
  ProducerTemplate template;

  @EndpointInject("mock:rss-output")
  MockEndpoint output;

  @Autowired
  ResourceLoader loader;

  private static final String XML_FILE = "classpath:item.xml";
  private String xmlData;
  @BeforeEach
  void setup() {
    try (var stream = loader.getResource(XML_FILE).getInputStream()) {
      xmlData = IOUtils.toString(stream, StandardCharsets.UTF_8);
      IOUtils.consume(stream);
    } catch (IOException ex) {
      log.error("COULD NOT LOAD FILE", ex);
    }
  }

  @Test
  @DirtiesContext
  void testProcessFeed() {
    var exchange = ExchangeBuilder.anExchange(template.getCamelContext())
            .withBody(xmlData)
            .build();
    template.send(exchange);

    assertThat(output.getExchanges()).as("Output should not be empty").isNotEmpty();

    var msg = output.getExchanges().get(0);

    assertThat(msg.getMessage().getBody())
            .as("Body should be a Bill Info")
            .isInstanceOf(BillInfo.class);
  }

}

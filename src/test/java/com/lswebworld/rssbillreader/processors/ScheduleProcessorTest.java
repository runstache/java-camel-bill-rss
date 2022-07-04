package com.lswebworld.rssbillreader.processors;

import static org.assertj.core.api.Assertions.assertThat;

import com.lswebworld.bills.data.dataobjects.ScheduleInfo;
import com.lswebworld.rssbillreader.constants.ErrorConstants;
import com.lswebworld.rssbillreader.constants.HeaderConstants;
import com.lswebworld.rssbillreader.constants.ScheduleTypes;
import com.lswebworld.rssbillreader.dataobjects.EtlException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
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

/**
 * Schedule Processor Tests.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class ScheduleProcessorTest {

  @EndpointInject("direct:schedule-in")
  ProducerTemplate template;

  @EndpointInject("mock:schedule-out")
  MockEndpoint output;

  @Autowired
  ResourceLoader loader;

  private static final String XML_FILE = "classpath:schedule-item.xml";
  private String xmlData;

  @BeforeEach
  void setup() {
    try (var stream = loader.getResource(XML_FILE).getInputStream()) {
      xmlData = IOUtils.toString(stream, StandardCharsets.UTF_8);
      IOUtils.consume(stream);
    } catch (IOException ex) {
      xmlData = "";
    }
  }

  @Test
  @DirtiesContext
  void testSendScheduleItem() {
    var exchange = ExchangeBuilder.anExchange(template.getCamelContext())
            .withProperty(HeaderConstants.SCHEDULE_TYPE, ScheduleTypes.SENATE)
            .withBody(xmlData)
            .build();

    template.send(exchange);

    assertThat(output.getExchanges()).as("Output should not be empty").isNotEmpty();

    var msg = output.getExchanges().get(0);
    assertThat(msg.getMessage().getBody())
            .as("Body should be a ScheduleEntry")
            .isNotNull()
            .isInstanceOf(ScheduleInfo.class);
  }

  @Test
  @DirtiesContext
  void testScheduleItemSenate() {
    var exchange = ExchangeBuilder.anExchange(template.getCamelContext())
            .withProperty(HeaderConstants.SCHEDULE_TYPE, ScheduleTypes.SENATE)
            .withBody(xmlData)
            .build();

    template.send(exchange);

    assertThat(output.getExchanges()).as("Output should not be empty").isNotEmpty();

    var msg = output.getExchanges().get(0);
    assertThat(msg.getMessage().getBody())
            .as("Body should be a ScheduleEntry")
            .isNotNull()
            .isInstanceOf(ScheduleInfo.class);

    var result = msg.getMessage().getBody(ScheduleInfo.class);
    assertThat(result.getScheduleType())
            .as("Schedule Type should be Senate")
            .isEqualToIgnoringCase(ScheduleTypes.SENATE);
  }

  @Test
  @DirtiesContext
  void testScheduleItemHouse() {
    var exchange = ExchangeBuilder.anExchange(template.getCamelContext())
            .withProperty(HeaderConstants.SCHEDULE_TYPE, ScheduleTypes.HOUSE)
            .withBody(xmlData)
            .build();

    template.send(exchange);

    assertThat(output.getExchanges()).as("Output should not be empty").isNotEmpty();

    var msg = output.getExchanges().get(0);
    assertThat(msg.getMessage().getBody())
            .as("Body should be a ScheduleEntry")
            .isNotNull()
            .isInstanceOf(ScheduleInfo.class);
    var result = msg.getMessage().getBody(ScheduleInfo.class);
    assertThat(result.getScheduleType())
            .as("Schedule Type should be House")
            .isEqualToIgnoringCase(ScheduleTypes.HOUSE);
  }


  @Test
  @DirtiesContext
  void testSendNoScheduleType() {
    var exchange = ExchangeBuilder.anExchange(template.getCamelContext())
            .withBody(xmlData)
            .build();

    template.send(exchange);

    assertThat(output.getExchanges()).as("Output should not be empty").isNotEmpty();

    var msg = output.getExchanges().get(0);

    assertThat(msg.getProperty(Exchange.EXCEPTION_CAUGHT))
            .as("Exception should be caught")
            .isInstanceOf(EtlException.class)
            .extracting(EtlException.class::cast)
            .extracting(EtlException::getMessage)
            .isEqualTo(ErrorConstants.MISSING_SCHEDULE_TYPE);

  }
}

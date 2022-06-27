package com.lswebworld.rssbillreader.processors;

import static org.assertj.core.api.Assertions.assertThat;

import com.lswebworld.rssbillreader.dataobjects.BillInfo;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import java.util.List;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class RssProcessorTest {

  @EndpointInject("direct:rss-input")
  ProducerTemplate template;

  @EndpointInject("mock:rss-output")
  MockEndpoint output;


  private SyndFeed feed;

  @BeforeEach
  void setup() {
    var entry = new SyndEntryImpl();
    entry.setUri("20210HB2010P3056");
    entry.setLink("https://www.legis.state.pa.us/cfdocs/legis/PN/Public/btCheck.cfm?txtType=HTM"
            + "&sessYr=2021&sessInd=0&billBody=H&billTyp=B&billNbr=2010&pn=3056");
    entry.setTitle("House Bill 2711 Printer's Number 3311");

    var desc = new SyndContentImpl();
    desc.setValue("An Act amending Title 20 (Decedents, Estates and Fiduciaries) of the Pennsylvania Consolidated"
            + " Statutes, providing for training of public pension fund and State fund fiduciaries....");
    entry.setDescription(desc);
    feed = new SyndFeedImpl();
    feed.getEntries().add(entry);
  }

  @Test
  @DirtiesContext
  @SuppressWarnings("unchecked")
  void testProcessFeed() {
    var exchange = ExchangeBuilder.anExchange(template.getCamelContext())
            .withBody(feed)
            .build();
    template.send(exchange);

    assertThat(output.getExchanges()).as("Output should not be empty").isNotEmpty();

    var msg = output.getExchanges().get(0);

    List<BillInfo> bills = msg.getMessage().getBody(List.class);

    assertThat(bills).as("Bills should not be empty").isNotEmpty();
  }

}

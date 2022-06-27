package com.lswebworld.rssbillreader.transformers;

import static org.assertj.core.api.Assertions.assertThat;

import com.lswebworld.rssbillreader.tranformers.RssTransformer;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import java.net.URISyntaxException;
import java.util.Date;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Rss Transformer.
 */
class RssTransformerTest {

  @Test
  void testTransform() throws URISyntaxException {
    var entry = new SyndEntryImpl();
    entry.setUri("20210HB2010P3056");
    entry.setLink("https://www.legis.state.pa.us/cfdocs/legis/PN/Public/btCheck.cfm?txtType=HTM"
            + "&sessYr=2021&sessInd=0&billBody=H&billTyp=B&billNbr=2010&pn=3056");
    entry.setTitle("House Bill 2711 Printer's Number 3311");

    var desc = new SyndContentImpl();
    desc.setValue("An Act amending Title 20 (Decedents, Estates and Fiduciaries) of the Pennsylvania Consolidated"
            + " Statutes, providing for training of public pension fund and State fund fiduciaries....");
    entry.setDescription(desc);
    var feed = new SyndFeedImpl();
    feed.getEntries().add(entry);

    entry.setPublishedDate(new Date());

    var transformer = new RssTransformer();
    var result = transformer.transform(feed);

    assertThat(result).
            as("Result should not be null")
            .isNotEmpty()
            .allSatisfy(r -> {


              assertThat(r.getIdentifier())
                      .as("Identifier should be 20210HB2010P3056")
                      .isEqualToIgnoringCase("20210HB2010P3056");

              assertThat(r.getDescription())
                      .as("Description should be populated")
                      .isEqualToIgnoringCase("An Act amending Title 20 (Decedents, Estates and Fiduciaries) of the Pennsylvania"
                              + " Consolidated Statutes, providing for training of public pension fund and State fund fiduciaries....");
              assertThat(r.getTitle())
                      .as("Title should be House Bill 2711 Printer's Number 3311")
                      .isNotNull()
                      .isEqualToIgnoringCase("House Bill 2711 Printer's Number 3311");
              assertThat(r.getPubDate())
                      .as("Pub Date should not be null")
                      .isNotNull();
            });
  }


  @Test
  void testTransformUpdateLink() throws URISyntaxException {
    var entry = new SyndEntryImpl();
    entry.setUri("20210HB2010P3056");
    entry.setLink("https://www.legis.state.pa.us/cfdocs/legis/PN/Public/btCheck.cfm?txtType=HTM"
                    + "&sessYr=2021&sessInd=0&billBody=H&billTyp=B&billNbr=2711&pn=3311");
    entry.setTitle("House Bill 2711 Printer's Number 3311");

    var desc = new SyndContentImpl();
    desc.setValue("An Act amending Title 20 (Decedents, Estates and Fiduciaries) of the Pennsylvania Consolidated"
            + " Statutes, providing for training of public pension fund and State fund fiduciaries....");
    entry.setDescription(desc);
    entry.setPublishedDate(new Date());
    var feed = new SyndFeedImpl();
    feed.getEntries().add(entry);

    var transformer = new RssTransformer();
    var result = transformer.transform(feed);

    assertThat(result)
            .as("Url should contain the PDF format in it.")
            .isNotEmpty()
            .allSatisfy(r -> assertThat(r.getUrl())
                    .isNotNull()
                    .isEqualToIgnoringCase("https://www.legis.state.pa.us/cfdocs/legis/PN/Public/btCheck.cfm?txtType=PDF"
                            + "&sessYr=2021&sessInd=0&billBody=H&billTyp=B&billNbr=2711&pn=3311"));

  }

  @Test
  void testNullPublishedDate() throws URISyntaxException {
    var entry = new SyndEntryImpl();
    entry.setUri("20210HB2010P3056");
    entry.setLink("https://www.legis.state.pa.us/cfdocs/legis/PN/Public/btCheck.cfm?txtType=HTM"
            + "&sessYr=2021&sessInd=0&billBody=H&billTyp=B&billNbr=2711&pn=3311");
    entry.setTitle("House Bill 2711 Printer's Number 3311");

    var desc = new SyndContentImpl();
    desc.setValue("An Act amending Title 20 (Decedents, Estates and Fiduciaries) of the Pennsylvania Consolidated"
            + " Statutes, providing for training of public pension fund and State fund fiduciaries....");
    entry.setDescription(desc);
    var feed = new SyndFeedImpl();
    feed.getEntries().add(entry);

    var transformer = new RssTransformer();
    var result = transformer.transform(feed);

    assertThat(result)
            .as("Pub Date should be null")
            .isNotEmpty()
            .allSatisfy(r -> assertThat(r.getPubDate()).isNull());
  }
}

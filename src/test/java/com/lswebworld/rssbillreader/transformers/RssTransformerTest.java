package com.lswebworld.rssbillreader.transformers;

import static org.assertj.core.api.Assertions.assertThat;

import com.lswebworld.rssbillreader.tranformers.RssTransformer;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Rss Transformer.
 */
class RssTransformerTest {

  private static final String XML_FILE = "item.xml";

  private String xml_string;

  @BeforeEach
  void setup() throws IOException {
    var loader = getClass().getClassLoader();
    xml_string = Files.readString(
            Path.of(Objects.requireNonNull(loader.getResource(XML_FILE)).getPath()));
  }

  @Test
  void testTransform() throws URISyntaxException {
    var transformer = new RssTransformer();

    var result = transformer.transform(xml_string);

    assertThat(result).as("Result should not be Empty").isNotEmpty();
    var pubDate = ZonedDateTime.of(2022,6,24,16,11,18,0, ZoneId.of("GMT"));
    assertThat(result.get().getPubDate()).as("Pub Date should be for 6/24/2022").isEqualTo(pubDate);

    assertThat(result.get().getIdentifier())
            .as("Identifier should be 20210HB2010P3056")
            .isEqualToIgnoringCase("20210HB2010P3056");
    assertThat(result.get().getUrl())
            .as("Url should be correct for the PDF version")
            .isEqualToIgnoringCase("https://www.legis.state.pa.us/cfdocs/legis/PN/Public/btCheck.cfm?txtType=PDF"
                    + "&sessYr=2021&sessInd=0&billBody=H&billTyp=B&billNbr=2010&pn=3056");
    assertThat(result.get().getDescription())
            .as("Description should be populated")
            .isEqualToIgnoringCase("An Act amending Title 20 (Decedents, Estates and Fiduciaries) of the Pennsylvania "
                    + "Consolidated Statutes, providing for training of public pension fund and State fund fiduciaries....");
    assertThat(result.get().getCreatedOn()).as("Created On should be populated").isNotNull();
    assertThat(result.get().getUpdatedOn()).as("Updated on should be populated").isNotNull();
    assertThat(result.get().getCoSponsors()).as("Co Sponsors should be GAYDOS, GLEIM, "
            + "GROVE, JAMES, KAUFFMAN, STAMBAUGH, SAYLOR, LAWRENCE and GILLEN")
            .isEqualToIgnoringCase("GAYDOS, GLEIM, GROVE, JAMES, KAUFFMAN, "
                    + "STAMBAUGH, SAYLOR, LAWRENCE and GILLEN");
    assertThat(result.get().getLastAction())
            .as("Last Action should be Referred to FINANCE, June 24, 2022")
            .isEqualToIgnoringCase("Referred to FINANCE, June 24, 2022");

    assertThat(result.get().getPrimeSponsors())
            .as("Prime Sponsor should be Representative RYAN")
            .isEqualToIgnoringCase("Representative RYAN");
    assertThat(result.get().isEnacted()).isFalse();
    assertThat(result.get().isPassedHouse()).isTrue();
    assertThat(result.get().isPassedSenate()).isFalse();
  }

}

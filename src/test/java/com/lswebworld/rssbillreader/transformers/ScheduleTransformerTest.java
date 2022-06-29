package com.lswebworld.rssbillreader.transformers;

import static org.assertj.core.api.Assertions.assertThat;

import com.lswebworld.rssbillreader.configuration.AppSettings;
import com.lswebworld.rssbillreader.tranformers.ScheduleTransformer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Schedule Transformer.
 */
class ScheduleTransformerTest {

  private static final String XML_FILE = "schedule-item.xml";
  private String xmlString;
  private AppSettings settings;
  @BeforeEach
  void setup() throws IOException {
    var loader = getClass().getClassLoader();

    xmlString = Files.readString(Path.of(Objects.requireNonNull(loader.getResource(XML_FILE)).getPath()));
    settings = new AppSettings();
    settings.setBillPrefix("20210");

  }

  @Test
  void testTransformItem() {
    var transformer = new ScheduleTransformer(settings);

    var result = transformer.transform(xmlString);

    assertThat(result).as("Result should not be empty").isNotEmpty();
    assertThat(result.get().getCreatedOn()).as("Created should not be null").isNotNull();
    assertThat(result.get().getUpdatedOn()).as("Updated should not be null").isNotNull();

    //Tue, 28 Jun 2022 19:01:29 GMT
    var zdt = ZonedDateTime.of(2022, 6,28, 19, 1, 29, 0, ZoneId.of("GMT"));
    assertThat(result.get().getScheduleDate())
            .as("Pub Date should be correct")
            .isEqualTo(zdt);
    assertThat(result.get().getIdentifier())
            .as("Identifier should be 20210HB1332P2272")
            .isEqualToIgnoringCase("20210HB1332P2272");

  }

}

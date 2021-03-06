package com.lswebworld.rssbillreader.tranformers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.lswebworld.bills.data.dataobjects.ScheduleInfo;
import com.lswebworld.rssbillreader.configuration.AppSettings;
import com.lswebworld.rssbillreader.constants.IntegrationConstants;
import com.lswebworld.rssbillreader.dataobjects.ScheduleRssItem;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Transformer for handling Transformation Schedule Entries.
 */
@Slf4j
public class ScheduleTransformer implements Transformer<ScheduleInfo> {

  private final AppSettings settings;

  @Autowired
  public ScheduleTransformer(AppSettings settings) {
    this.settings = settings;
  }


  @Override
  public Optional<ScheduleInfo> transform(String value) {

    try {
      var mapper = new XmlMapper();
      var rss = mapper.readValue(cleanUpXml(value), ScheduleRssItem.class);
      var schedule = rss.getItem();
      schedule.setCreatedOn(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")));
      schedule.setUpdatedOn(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")));
      schedule.setIdentifier(parseIdentifer(schedule.getIdentifier()));
      schedule.setScheduleDate(schedule.getScheduleDate().plusDays(1));
      return Optional.of(schedule);

    } catch (JsonProcessingException ex) {
      log.error("Failed to Parse Schedule item", ex);
    }
    return Optional.empty();
  }

  private String parseIdentifer(String value) {
    var parts = value.split(" ");
    if (parts.length > 0) {
      return settings.getBillPrefix() + parts[0];
    }
    return value;
  }

  private String cleanUpXml(String value) {
    String[] lines = value.split("\n");
    for (int i = 0; i < lines.length; i++) {
      if (lines[i].contains(IntegrationConstants.LINK_XML_START)) {
        lines[i] = encodeLink(lines[i]);
      }
    }

    var itemBody = String.join(" ", lines);
    return "<rss xmlns:dc=\"http://purl.org/dc/elements/1.1/\" "
            + "xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\" "
            + "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" "
            + "xmlns:taxo=\"http://purl.org/rss/1.0/modules/taxonomy/\" "
            + "xmlns:parss=\"https://www.legis.state.pa.us/RSS\" version=\"2.0\">"
            + itemBody
            + "</rss>";
  }

  private String encodeLink(String value) {
    var temp = value
            .replace(IntegrationConstants.LINK_XML_START, "")
            .replace(IntegrationConstants.LINK_XML_END, "");
    return encapsulateLink(Base64.getEncoder()
            .encodeToString(temp.getBytes(StandardCharsets.UTF_8)));
  }

  private String encapsulateLink(String base64Value) {
    return IntegrationConstants.LINK_XML_START + base64Value + IntegrationConstants.LINK_XML_END;
  }
}

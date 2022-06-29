package com.lswebworld.rssbillreader.tranformers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.lswebworld.rssbillreader.dataobjects.BillInfo;
import com.lswebworld.rssbillreader.dataobjects.RssItem;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;

/**
 * Implementation of Transformer for RSS Elements.
 */
@Slf4j
public class RssTransformer implements Transformer<BillInfo> {
  @Override
  public Optional<BillInfo> transform(String value) throws URISyntaxException {
    try {

      var mapper = new XmlMapper();
      var rss = mapper.readValue(cleanUpXml(value), RssItem.class);
      var bill = rss.getItem();
      bill.setUpdatedOn(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")));
      bill.setCreatedOn(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")));
      bill.setUrl(updateUri(bill.getUrl()));
      return Optional.of(bill);
    } catch (JsonProcessingException ex) {
      log.error("FAILED TO PARSE RSS ITEM", ex);

    }
    return Optional.empty();
  }

  private String updateUri(String url) throws URISyntaxException {
    if (StringUtils.isNotEmpty(url)) {
      var urlValue = Base64.getDecoder().decode(url.getBytes(StandardCharsets.UTF_8));

      var uri = new URI(new String(urlValue, StandardCharsets.UTF_8).trim());
      var builder = new URIBuilder()
              .setScheme(uri.getScheme())
              .setHost(uri.getHost())
              .setPath(uri.getPath())
              .setPort(uri.getPort());

      List<NameValuePair> params = URLEncodedUtils.parse(uri, StandardCharsets.UTF_8);
      for (var param : params) {
        if (param.getName().equalsIgnoreCase("txtType")) {
          builder.addParameter(param.getName(), "PDF");
        } else {
          builder.addParameter(param.getName(), param.getValue());
        }
      }
      return builder.build().toString();
    }
    return "";
  }

  private String cleanUpXml(String value) {
    String[] lines = value.split("\n");
    for (int i = 0; i < lines.length; i++) {
      if (lines[i].contains("<link>")) {
        var temp = lines[i].replace("<link>", "").replace("</link>", "");
        var url = Base64.getEncoder().encodeToString(temp.getBytes(StandardCharsets.UTF_8));
        lines[i] = "<link>" + url + "</link>";
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
}



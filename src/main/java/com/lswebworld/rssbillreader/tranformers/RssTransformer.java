package com.lswebworld.rssbillreader.tranformers;

import com.lswebworld.rssbillreader.constants.ForeignFieldConstants;
import com.lswebworld.rssbillreader.dataobjects.BillInfo;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.jdom2.Element;

/**
 * Implementation of Transformer for RSS Elements.
 */
public class RssTransformer implements Transformer {
  @Override
  public List<BillInfo> transform(SyndFeed feed) throws URISyntaxException {
    List<BillInfo> bills = new ArrayList<>();

    if (ObjectUtils.isNotEmpty(feed.getEntries())) {

      for (var entry : feed.getEntries()) {
        var info = new BillInfo();
        info.setDescription(entry.getDescription().getValue());
        info.setTitle(entry.getTitle());
        info.setIdentifier(entry.getUri());
        info.setUrl(updateUri(entry.getLink()));
        info.setPubDate(convertDate(entry.getPublishedDate()));
        info.setEnacted(convertBoolean(getMetaValue(entry, ForeignFieldConstants.ENACTED)));
        info.setCoSponsors(getMetaValue(entry, ForeignFieldConstants.CO_SPONSORS));
        info.setLastAction(getMetaValue(entry, ForeignFieldConstants.LAST_ACTION));
        info.setPassedHouse(convertBoolean(
                getMetaValue(entry, ForeignFieldConstants.PASSED_HOUSE)));
        info.setPassedSenate(convertBoolean(
                getMetaValue(entry, ForeignFieldConstants.PASSED_SENATE)));
        info.setPrimeSponsor(getMetaValue(entry, ForeignFieldConstants.PRIME_SPONSOR));
        bills.add(info);
      }
    }
    return bills;
  }

  private String getMetaValue(SyndEntry entry, String name) {
    Optional<Element> value =
            entry.getForeignMarkup()
                    .stream()
                    .filter(c -> c.getName().equalsIgnoreCase(name))
                    .findFirst();
    if (value.isPresent()) {
      return value.get().getValue();
    }
    return "";
  }

  private boolean convertBoolean(String value) {
    return value.equalsIgnoreCase("yes");
  }

  private ZonedDateTime convertDate(Date value) {
    if (ObjectUtils.isNotEmpty(value)) {
      return ZonedDateTime.ofInstant(Instant.ofEpochMilli(value.getTime()), ZoneId.of("GMT"));
    }
    return null;
  }

  private String updateUri(String url) throws URISyntaxException {
    var uri = new URI(url);
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
}



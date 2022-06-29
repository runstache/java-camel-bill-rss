package com.lswebworld.rssbillreader.dataobjects;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

/**
 * Deserializer for Boolean items.
 */
public class BooleanDeserializer extends StdDeserializer<Boolean> {

  public BooleanDeserializer() {
    this(null);
  }

  public BooleanDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public Boolean deserialize(JsonParser jsonParser,
                             DeserializationContext deserializationContext)
          throws IOException {

    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    String value = node.textValue();
    return value.equalsIgnoreCase("YES");
  }
}

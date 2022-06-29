package com.lswebworld.rssbillreader.dataobjects;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

/**
 * Boolean Serializer.
 */
public class BooleanSerializer extends StdSerializer<Boolean> {

  public BooleanSerializer() {
    this(null);
  }

  public BooleanSerializer(Class<Boolean> clazz) {
    super(clazz);
  }

  @Override
  public void serialize(Boolean value,
                        JsonGenerator jsonGenerator,
                        SerializerProvider serializerProvider) throws IOException {
    if (value) {
      jsonGenerator.writeString("YES");
    } else {
      jsonGenerator.writeString("NO");
    }

  }
}

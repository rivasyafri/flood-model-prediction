package com.mofp.model.support.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mofp.model.State;

import java.io.IOException;

/**
 * @author rivasyafri
 */
public class StateSerializer extends JsonSerializer<State> {

    @Override
    public void serialize(State value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeFieldName("id");gen.writeNumber(value.getId());
        gen.writeFieldName("name");gen.writeString(value.getName());
        gen.writeFieldName("active");gen.writeBoolean(value.isActive());
        gen.writeEndObject();
    }
}

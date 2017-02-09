package com.mofp.model.support.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mofp.model.Cell;

import java.io.IOException;

/**
 * @author rivasyafri
 */
public class CellSerializer extends JsonSerializer<Cell> {

    @Override
    public void serialize(Cell value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeFieldName("id");gen.writeNumber(value.getId());
        gen.writeFieldName("xArray");gen.writeNumber(value.getXArray());
        gen.writeFieldName("yArray");gen.writeNumber(value.getYArray());
        gen.writeEndObject();
    }
}

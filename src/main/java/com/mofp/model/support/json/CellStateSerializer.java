package com.mofp.model.support.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mofp.model.moving.CellState;

import java.io.IOException;

/**
 * @author rivasyafri
 */
public class CellStateSerializer extends JsonSerializer<CellState>{
    @Override
    public void serialize(CellState value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeFieldName("id");gen.writeNumber(value.getId());
        gen.writeFieldName("xArray");gen.writeNumber(value.getCell().getXArray());
        gen.writeFieldName("yArray");gen.writeNumber(value.getCell().getYArray());
        gen.writeFieldName("state");gen.writeString(value.getValue().getName());
        gen.writeFieldName("startTime");gen.writeNumber(value.getStartTime());
        gen.writeFieldName("endTime");gen.writeNumber(value.getEndTime());
        gen.writeEndObject();
    }
}

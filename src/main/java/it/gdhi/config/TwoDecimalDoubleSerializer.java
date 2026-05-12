package it.gdhi.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import it.gdhi.utils.ScoreRoundingUtil;

import java.io.IOException;

public class TwoDecimalDoubleSerializer extends JsonSerializer<Double> {

    @Override
    public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(ScoreRoundingUtil.roundToTwoDecimals(value));
    }
}

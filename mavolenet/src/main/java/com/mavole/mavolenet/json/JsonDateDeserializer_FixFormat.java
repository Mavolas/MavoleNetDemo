package com.mavole.mavolenet.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mavole.mavolenet.request.RestRequest;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonDateDeserializer_FixFormat implements JsonDeserializer<Date> {
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            String s = json.getAsJsonPrimitive().getAsString();
            SimpleDateFormat sdf = new SimpleDateFormat(RestRequest.GetJsonDateParseStr());
            Date date=sdf.parse(s);
            return date;
        } catch (Exception e) {
            return null;
        }
    }
}

package com.github.crehn.pantarhei.data;

import java.time.Instant;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class InstantConverter implements AttributeConverter<Instant, Date> {

    @Override
    public Date convertToDatabaseColumn(Instant instant) {
        return Date.from(instant);
    }

    @Override
    public Instant convertToEntityAttribute(Date value) {
        return value.toInstant();
    }
}
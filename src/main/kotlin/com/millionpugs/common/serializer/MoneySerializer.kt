package com.millionpugs.common.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.math.BigDecimal
import java.math.RoundingMode


class MoneySerializer : JsonSerializer<BigDecimal>() {
    override fun serialize(value: BigDecimal, jgen: JsonGenerator, provider: SerializerProvider?) {
        jgen.writeString(value.setScale(2, RoundingMode.HALF_UP).toString())
    }
}
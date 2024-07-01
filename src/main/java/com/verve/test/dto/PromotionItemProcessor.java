package com.verve.test.dto;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.verve.test.entity.Promotion;

@Component
public class PromotionItemProcessor implements ItemProcessor<PromotionReader, Promotion> {
	
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z z");
	
	@Override
	public Promotion process(PromotionReader item) throws Exception {
		Promotion write = new Promotion();
		write.setId(item.getId().toUpperCase());
		write.setPrice(item.getPrice());
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(item.getExpirationDate(), FORMATTER);
        ZonedDateTime gmtDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
		write.setExpirationDate(gmtDateTime.toLocalDateTime());
        return write;
	}
}
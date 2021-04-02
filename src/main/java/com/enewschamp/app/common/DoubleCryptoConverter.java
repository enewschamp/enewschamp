package com.enewschamp.app.common;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import javax.persistence.Converter;

@Converter
public class DoubleCryptoConverter extends AbstractCryptoConverter<Double> {

	public DoubleCryptoConverter() {
		this(new CipherInitializer());
	}

	public DoubleCryptoConverter(CipherInitializer cipherInitializer) {
		super(cipherInitializer);
	}

	@Override
	boolean isNotNullOrEmpty(Double attribute) {
		return attribute != null;
	}

	@Override
	Double stringToEntityAttribute(String dbData) {
		return isEmpty(dbData) ? null : Double.parseDouble(dbData);
	}

	@Override
	String entityAttributeToString(Double attribute) {
		return attribute == null ? null : attribute.toString();
	}

}

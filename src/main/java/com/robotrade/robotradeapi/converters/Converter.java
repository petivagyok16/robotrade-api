package com.robotrade.robotradeapi.converters;

import org.springframework.lang.Nullable;

@FunctionalInterface
public interface Converter<S, T> {

	@Nullable
	T convert(S var2);
}

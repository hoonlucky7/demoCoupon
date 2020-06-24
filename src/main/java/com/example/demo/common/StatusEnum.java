package com.example.demo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {
	Y("Y", "used"),
	N("N", "unused"),
	E("E", "expired"),
	A("A", "allocated");

	private String code;
	private String value;
}

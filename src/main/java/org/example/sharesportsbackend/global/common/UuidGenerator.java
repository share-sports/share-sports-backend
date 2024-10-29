package org.example.sharesportsbackend.global.common;

import java.util.UUID;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class UuidGenerator {
	public static String generateProductUuid() {
		return "PR-" + UUID.randomUUID().toString();
	}

	public static String generateCategoryUuid() {
		return "CT-" + UUID.randomUUID().toString().substring(0, 8);
	}

}
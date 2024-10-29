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

	public static String generateProductCode() {
		return "PT-" + UUID.randomUUID().toString();
	}

	public static String generateCategoryCode() {
		return "CT-" + UUID.randomUUID().toString().substring(0, 8);
	}

	public static String generateMemberUuid() {
		return UUID.randomUUID().toString().substring(0, 8);
	}

	public static String generatePurchaseCode() {
		return "PC-" + UUID.randomUUID().toString().substring(0, 8);
	}

	public static String generateTemporaryPassword() {
		return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
	}

	public static String generateAuthCode(){
		return UUID.randomUUID().toString().substring(0, 6);
	}

}
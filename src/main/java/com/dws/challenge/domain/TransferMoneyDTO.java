package com.dws.challenge.domain;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferMoneyDTO {

	@NotNull
	@NotBlank
	private String fromAccountId;

	@NotNull
	@NotBlank
	private String toAccountId;

	@Min(value = 0, message = "Transfer values must be greater than 0.")
	private BigDecimal transferAmount;


}

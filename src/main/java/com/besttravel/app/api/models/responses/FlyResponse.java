package com.besttravel.app.api.models.responses;

import com.besttravel.app.util.enums.AeroLine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FlyResponse {
	private Long id;
	private Double originLat;
	private Double originLng;
	private Double destinyLat;
	private Double destinyLng;
	private String originName;
	private String destinyName;
	private BigDecimal price;
	private AeroLine aeroLine;
}

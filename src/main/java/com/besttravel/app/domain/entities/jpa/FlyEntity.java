package com.besttravel.app.domain.entities.jpa;

import com.besttravel.app.util.enums.AeroLine;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity(name = "fly")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FlyEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Double originLat;
	private Double originLng;
	private Double destinyLng;
	private Double destinyLat;
	private BigDecimal price;
	@Column(length = 20)
	private String originName;
	@Column(length = 20)
	private String destinyName;
	@Enumerated(EnumType.STRING)
	private AeroLine aeroLine;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "fly", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private Set<TicketEntity> tickets;
}

package com.rental.solides.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "clients", indexes = @Index(columnList = "document", unique = true))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false, length = 200)
	private String fullName;

	@Column(nullable = false, length = 20)
	private String document;

	@Column(nullable = false, length = 200)
	private String email;

	@Column(length = 40)
	private String phone;
}

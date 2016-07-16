package com.github.crehn.pantarhei.data;

import java.net.URI;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SipEntity {

	@Id
	@Column
	private int id;

	@Column
	private UUID guid;
	@Column
	private String title;
	@Column
	private String summary;
	@Column
	private String text;
	@Column
	private URI sourceUri;
}

package com.github.crehn.pantarhei.api;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Singular;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(value = Include.NON_EMPTY)
public class Sip {

	@NonNull
	private UUID guid;
	@NonNull
	private String title;
	private String summary;
	private String text;
	private URI sourceUri;
	@Singular
	private List<String> tags;
}

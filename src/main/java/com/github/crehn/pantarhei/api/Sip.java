package com.github.crehn.pantarhei.api;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;
import lombok.experimental.Wither;

@Data
@Builder
@Wither
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(value = Include.NON_EMPTY)
public class Sip {

    private UUID guid;
    @NonNull
    @SuppressFBWarnings("ES_COMPARING_PARAMETER_STRING_WITH_EQ")
    private String title;
    @SuppressFBWarnings("ES_COMPARING_PARAMETER_STRING_WITH_EQ")
    private String notes;
    @SuppressFBWarnings("ES_COMPARING_PARAMETER_STRING_WITH_EQ")
    private String text;
    private URI sourceUri;
    @Singular
    private List<String> tags;
}

package com.github.crehn.pantarhei.api;

import java.net.URI;
import java.time.Instant;
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

    private static final String FALSE_POSITIVE = "ES_COMPARING_PARAMETER_STRING_WITH_EQ";
    private UUID guid;
    @NonNull
    @SuppressFBWarnings(FALSE_POSITIVE)
    private String title;
    @SuppressFBWarnings(FALSE_POSITIVE)
    private String notes;
    @SuppressFBWarnings(FALSE_POSITIVE)
    private String text;
    private URI sourceUri;
    @Singular
    private List<String> tags;

    @SuppressFBWarnings(FALSE_POSITIVE)
    private String status;

    private Instant originTimestamp;
    private Instant created;
    private Instant modified;
    private Instant due;
}

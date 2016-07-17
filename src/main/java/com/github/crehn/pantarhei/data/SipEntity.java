package com.github.crehn.pantarhei.data;

import static lombok.AccessLevel.PRIVATE;

import java.net.URI;
import java.util.UUID;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "Sips")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
@NamedQuery(name = SipEntity.GET_SIP_BY_GUID, query = "SELECT s FROM SipEntity s WHERE s.guid = :guid")
public class SipEntity {
    public static final String GET_SIP_BY_GUID = "SipEntity.GET_BY_GUID";

    public static class SipEntityBuilder {
        public SipEntityBuilder sourceUri(URI sourceUri) {
            this.sourceUri = sourceUri.toString();
            return this;
        }
    }

    @Id
    private int id;
    private UUID guid;
    private String title;
    private String summary;
    private String text;
    private String sourceUri;

    public URI getSourceUri() {
        return URI.create(sourceUri);
    }

    public void setSourceUri(URI sourceUri) {
        this.sourceUri = sourceUri.toString();
    }
}

package com.github.crehn.pantarhei.data;

import static com.github.crehn.pantarhei.data.SipEntity.GET_SIP_BY_GUID;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static lombok.AccessLevel.PRIVATE;

import java.net.URI;
import java.util.*;

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
@NamedQuery(name = GET_SIP_BY_GUID, query = "SELECT s FROM SipEntity s WHERE s.guid = :guid")
public class SipEntity {
    public static final String GET_SIP_BY_GUID = "SipEntity.GET_BY_GUID";

    public static class SipEntityBuilder {
        public SipEntityBuilder sourceUri(URI sourceUri) {
            this.sourceUri = sourceUri.toString();
            return this;
        }
    }

    @Id
    @GeneratedValue
    private Integer id;
    @Version
    private int version;

    private UUID guid;
    private String title;
    private String summary;
    private String text;
    private String sourceUri;

    @Singular
    @ManyToMany(fetch = EAGER, cascade = ALL)
    @JoinTable(joinColumns = { @JoinColumn(name = "sip_id") }, //
            inverseJoinColumns = { @JoinColumn(name = "tag_id") })
    private List<TagEntity> tags = new ArrayList<>();

    public URI getSourceUri() {
        return URI.create(sourceUri);
    }

    public void setSourceUri(URI sourceUri) {
        this.sourceUri = sourceUri.toString();
    }
}

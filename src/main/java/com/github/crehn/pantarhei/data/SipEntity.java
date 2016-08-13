package com.github.crehn.pantarhei.data;

import static com.github.crehn.pantarhei.data.SipEntity.DELETE_SIP_BY_GUID;
import static com.github.crehn.pantarhei.data.SipEntity.GET_SIP_BY_GUID;
import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static lombok.AccessLevel.PRIVATE;

import java.net.URI;
import java.util.*;

import javax.persistence.*;

import com.github.crehn.pantarhei.api.Sip;

import lombok.*;

@Entity
@Table(name = "Sips")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
@NamedQueries({ //
        @NamedQuery(name = GET_SIP_BY_GUID, query = "SELECT s FROM SipEntity s WHERE s.guid = :guid"),
        @NamedQuery(name = DELETE_SIP_BY_GUID, query = "DELETE FROM SipEntity s WHERE s.guid = :guid") })
public class SipEntity {
    public static final String GET_SIP_BY_GUID = "SipEntity.GET_BY_GUID";
    public static final String DELETE_SIP_BY_GUID = "SipEntity.DELETE_BY_GUID";

    public static class SipEntityBuilder {
        public SipEntityBuilder sourceUri(URI sourceUri) {
            this.sourceUri = sourceUri == null ? null : sourceUri.toString();
            return this;
        }
    }

    @Id
    @GeneratedValue
    private Integer id;
    @Version
    private Integer version;

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
        return sourceUri == null ? null : URI.create(sourceUri);
    }

    public void setSourceUri(URI sourceUri) {
        this.sourceUri = sourceUri == null ? null : sourceUri.toString();
    }

    public Sip toApi() {
        return Sip.builder() //
                .guid(guid) //
                .title(title) //
                .summary(summary) //
                .text(text) //
                .sourceUri(getSourceUri()) //
                .tags(toApi(tags)) //
                .build();
    }

    private List<String> toApi(List<TagEntity> tags) {
        return tags.stream() //
                .map(TagEntity::getName) //
                .collect(toList());
    }

}

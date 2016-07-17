package com.github.crehn.pantarhei.data;

import static com.github.crehn.pantarhei.data.TagEntity.FIND_TAGS_BY_NAMES;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "Tags")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@NamedQuery(name = FIND_TAGS_BY_NAMES, query = "SELECT t FROM TagEntity t WHERE name in (:names)")
public class TagEntity {
    public static final String FIND_TAGS_BY_NAMES = "TagEntity.FIND_ALL_BY_NAMES";

    @Id
    @GeneratedValue
    private Integer id;

    @Version
    private int version;

    @NonNull
    private String name;
}

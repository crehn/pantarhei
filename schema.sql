CREATE SEQUENCE hibernate_sequence;

CREATE TABLE sips (
    id integer PRIMARY KEY,
    guid uuid UNIQUE NOT NULL,
    title character varying,
    notes character varying,
    text text,
    sourceuri character varying,
    version integer,
    status character varying NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    due timestamp with time zone,
    origintimestamp timestamp with time zone
);

CREATE TABLE sips_tags (
    sip_id integer NOT NULL REFERENCES sips(id),
    tag_id integer NOT NULL REFERENCES tags(id),
    PRIMARY KEY (sip_id, tag_id)
);

CREATE TABLE tags (
    id integer PRIMARY KEY,
    name character varying UNIQUE NOT NULL,
    version integer
);


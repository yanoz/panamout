CREATE TYPE category AS ENUM ('Restaurant', 'Bar', 'Both');

CREATE TABLE spot (
	id 		    SERIAL PRIMARY KEY,
	name 	    VARCHAR,
	category    category,
	street   	VARCHAR,
	details 	VARCHAR,
	district 	integer,
	lat         double precision,
	lng         double precision,
	url         VARCHAR
);

CREATE INDEX geolocation on spot USING gist(ll_to_earth(lat, lng));
CREATE INDEX district on spot (district);
CREATE UNIQUE INDEX name on spot (name);

CREATE TABLE metas (
    id          SERIAL PRIMARY KEY,
    spot_id     INTEGER REFERENCES spot(id),
    metas        JSONB
);

create index type on metas ((metas->'type'));
create index price on metas ((metas->'price'));
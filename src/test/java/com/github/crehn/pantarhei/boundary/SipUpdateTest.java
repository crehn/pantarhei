package com.github.crehn.pantarhei.boundary;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.util.UUID;

import javax.json.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.crehn.pantarhei.api.Sip;
import com.github.crehn.pantarhei.control.UnknownPropertyException;
import com.github.crehn.pantarhei.data.*;

@RunWith(MockitoJUnitRunner.class)
public class SipUpdateTest extends AbstractSipBoundaryTest {

    @Test
    public void shouldReplaceSipInDb() {
        SipEntity sipEntity = createSipEntity();
        givenSipEntity(sipEntity);

        Sip sip = Sip.builder() //
                .guid(GUID) //
                .title(TITLE + "2") //
                .notes(NOTES + "2") //
                .text(TEXT + "2") //
                .sourceUri(URI.create(SOURCE_URI.toString() + "2")) //
                .status(STATUS + "2") //
                .originTimestamp(ORIGIN_TIMESTAMP.plusMillis(10)) //
                .created(CREATED.plusMillis(10)) //
                .modified(MODIFIED.plusMillis(10)) //
                .due(DUE.plusMillis(10)) //
                .build();
        boundary.putSip(GUID, sip);

        assertSipEntity(sip, sipEntity);
    }

    @Test
    public void shouldNotDeleteGuidWhenReplacingSipInDb() {
        SipEntity sipEntity = createSipEntity();
        givenSipEntity(sipEntity);

        Sip sip = Sip.builder() //
                .guid(null) //
                .title(TITLE + "2") //
                .build();
        boundary.putSip(GUID, sip);

        assertEquals(GUID, sipEntity.getGuid());
        assertEquals(TITLE + "2", sipEntity.getTitle());
        assertEquals(null, sipEntity.getSourceUri());
        assertEquals(null, sipEntity.getNotes());
        assertEquals(emptyList(), sipEntity.getTags());
        assertEquals(null, sipEntity.getText());
    }

    @Test
    public void shouldReplaceSipTagInDb() {
        SipEntity sipEntity = createSipEntity();
        givenSipEntity(sipEntity);

        Sip sip = SIP.withTags(asList(TAG3, TAG2));
        boundary.putSip(GUID, sip);

        assertThat(sipEntity.getTags()) //
                .extracting(TagEntity::getName) //
                .containsExactlyInAnyOrder(TAG2, TAG3);
    }

    @Test
    public void shouldPatchSipInDbChangingNothing() {
        SipEntity sipEntity = createSipEntity();
        givenSipEntity(sipEntity);

        JsonObject patch = Json.createObjectBuilder().build();
        boundary.patchSip(GUID, patch.toString());

        assertSipEntity(SIP, sipEntity);
    }

    @Test(expected = SipNotFoundException.class)
    public void shouldFailToPatchUnknownSip() {
        JsonObject patch = Json.createObjectBuilder().build();
        boundary.patchSip(OTHER_GUID, patch.toString());
    }

    @Test(expected = UnknownPropertyException.class)
    public void shouldFailToPatchSipWithUnknownProperty() {
        SipEntity sipEntity = createSipEntity();
        givenSipEntity(sipEntity);

        JsonObject patch = Json.createObjectBuilder() //
                .add("unknown", "value") //
                .build();
        boundary.patchSip(GUID, patch.toString());
    }

    @Test(expected = UnknownPropertyException.class)
    public void shouldFailToPatchSipWithGuid() {
        SipEntity sipEntity = createSipEntity();
        givenSipEntity(sipEntity);

        JsonObject patch = Json.createObjectBuilder() //
                .add("guid", UUID.randomUUID().toString()) //
                .build();
        boundary.patchSip(GUID, patch.toString());
    }

    @Test
    public void shouldPatchSipInDbDeletingValue() {
        SipEntity sipEntity = createSipEntity();
        givenSipEntity(sipEntity);

        JsonObject patch = Json.createObjectBuilder() //
                .add("text", JsonValue.NULL) //
                .build();
        boundary.patchSip(GUID, patch.toString());

        assertSipEntity(SIP.withText(null), sipEntity);
    }

    @Test
    public void shouldPatchSipInDbChangingStringValue() {
        SipEntity sipEntity = createSipEntity();
        givenSipEntity(sipEntity);

        JsonObject patch = Json.createObjectBuilder() //
                .add("title", TITLE + "2") //
                .build();
        boundary.patchSip(GUID, patch.toString());

        assertSipEntity(SIP.withTitle(TITLE + "2"), sipEntity);
    }

    @Test
    public void shouldPatchSipInDbChangingUriValue() {
        SipEntity sipEntity = createSipEntity();
        givenSipEntity(sipEntity);

        JsonObject patch = Json.createObjectBuilder() //
                .add("sourceUri", SOURCE_URI + "2") //
                .build();
        boundary.patchSip(GUID, patch.toString());

        assertSipEntity(SIP.withSourceUri(URI.create(SOURCE_URI.toString() + "2")), sipEntity);
    }

    @Test
    public void shouldPatchSipInDbChangingTags() {
        SipEntity sipEntity = createSipEntity();
        givenSipEntity(sipEntity);

        JsonObject patch = Json.createObjectBuilder() //
                .add("tags",
                        Json.createArrayBuilder() //
                                .add(TAG3) //
                                .add(TAG2) //
                                .build()) //
                .build();
        boundary.patchSip(GUID, patch.toString());

        assertThat(sipEntity.getTags()) //
                .extracting(TagEntity::getName) //
                .containsExactlyInAnyOrder(TAG2, TAG3);
    }
}

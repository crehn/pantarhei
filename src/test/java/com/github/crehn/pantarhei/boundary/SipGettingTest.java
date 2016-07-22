package com.github.crehn.pantarhei.boundary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.crehn.pantarhei.api.Sip;
import com.github.crehn.pantarhei.data.SipEntity;
import com.github.crehn.pantarhei.data.SipNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class SipGettingTest extends AbstractSipBoundaryTest {

    @Test(expected = SipNotFoundException.class)
    public void shouldFailGettingUnknownSip() {
        givenUnknownSip();

        boundary.getSip(OTHER_GUID);
    }

    private void givenUnknownSip() {
        when(sipStore.findSipByGuid(OTHER_GUID)).thenReturn(null);
    }

    @Test
    public void shouldGetSip() {
        SipEntity sipEntity = createSipEntity();
        givenSipEntity(sipEntity);

        Sip result = boundary.getSip(GUID);

        assertSip(sipEntity, result);
    }

    private void assertSip(SipEntity sipEntity, Sip result) {
        assertEquals(sipEntity.getGuid(), result.getGuid());
        assertEquals(sipEntity.getTitle(), result.getTitle());
        assertEquals(sipEntity.getSummary(), result.getSummary());
        assertEquals(sipEntity.getText(), result.getText());
        assertEquals(sipEntity.getSourceUri(), result.getSourceUri());
        assertThat(result.getTags()).containsExactly(TAG1, TAG2);
    }
}

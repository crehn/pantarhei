package com.github.crehn.pantarhei.boundary;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.crehn.pantarhei.api.Sip;
import com.github.crehn.pantarhei.data.SipNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class SipGettingTest extends AbstractSipBoundaryTest {

    @Test(expected = SipNotFoundException.class)
    public void shouldFailGettingUnknownSip() throws Exception {
        givenUnknownSip();

        boundary.getSip(OTHER_GUID);
    }

    private void givenUnknownSip() {
        when(store.getSipByGuid(OTHER_GUID)).thenThrow(new SipNotFoundException(OTHER_GUID));
    }

    @Test
    public void shouldGetSip() throws Exception {
        givenSipEntity();

        Sip result = boundary.getSip(GUID);

        assertSip(result);
    }

    private void givenSipEntity() {
        when(store.getSipByGuid(GUID)).thenReturn(SIP_ENTITY);
    }

    private void assertSip(Sip result) {
        assertEquals(SIP_ENTITY.getGuid(), result.getGuid());
        assertEquals(SIP_ENTITY.getTitle(), result.getTitle());
        assertEquals(SIP_ENTITY.getSummary(), result.getSummary());
        assertEquals(SIP_ENTITY.getText(), result.getText());
        assertEquals(SIP_ENTITY.getSourceUri(), result.getSourceUri());
    }
}

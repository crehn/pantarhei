package com.github.crehn.pantarhei.boundary;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SipDeletionTest extends AbstractSipBoundaryTest {

    @Test
    public void shouldDeleteSip() {
        givenSipEntity(createSipEntity());

        boundary.deleteSip(GUID);

        verify(sipStore).deleteSip(GUID);
    }
}

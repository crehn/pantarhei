package com.github.crehn.pantarhei.api;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SipTest {

    @Test(expected = NullPointerException.class)
    public void shouldFailToConstructSipWithoutTitle() {
        Sip.builder() //
                .guid(UUID.randomUUID()) //
                .title(null) //
                .build();
    }
}

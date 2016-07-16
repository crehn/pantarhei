package com.github.crehn.pantarhei.boundary;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.crehn.pantarhei.api.Sip;
import com.github.crehn.pantarhei.control.SipFacade;
import com.github.crehn.pantarhei.data.SipEntity;
import com.github.crehn.pantarhei.data.SipStore;

@RunWith(MockitoJUnitRunner.class)
public class SipCreationTest {
	private static final UUID GUID = UUID.randomUUID();
	private static final UUID OTHER_GUID = UUID.randomUUID();
	private static final Sip SIP = Sip.builder() //
			.guid(GUID) //
			.title("title") //
			.build();

	@InjectMocks
	SipBoundary boundary;

	@Spy
	@InjectMocks
	SipFacade facade = new SipFacade();

	@Mock
	SipStore store;

	@Captor
	ArgumentCaptor<SipEntity> sipEntityCaptor;

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailCreatingSipForWrongLocation() throws Exception {
		boundary.putSip(OTHER_GUID, SIP);
	}

	@Test
	public void shouldStoreNewSip() throws Exception {
		boundary.putSip(GUID, SIP);

		verify(store).store(sipEntityCaptor.capture());
		SipEntity sipEntity = sipEntityCaptor.getValue();
		assertEquals(SIP.getGuid(), sipEntity.getGuid());
		assertEquals(SIP.getTitle(), sipEntity.getTitle());
	}
}

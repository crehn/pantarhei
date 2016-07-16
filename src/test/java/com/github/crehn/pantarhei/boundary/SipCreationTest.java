package com.github.crehn.pantarhei.boundary;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.crehn.pantarhei.data.SipEntity;

@RunWith(MockitoJUnitRunner.class)
public class SipCreationTest extends AbstractSipBoundaryTest {
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

		assertSipEntity(sipEntity);
	}

	private void assertSipEntity(SipEntity sipEntity) {
		assertEquals(SIP.getGuid(), sipEntity.getGuid());
		assertEquals(SIP.getTitle(), sipEntity.getTitle());
		assertEquals(SIP.getSummary(), sipEntity.getSummary());
		assertEquals(SIP.getText(), sipEntity.getText());
		assertEquals(SIP.getSourceUri(), sipEntity.getSourceUri());
	}
}

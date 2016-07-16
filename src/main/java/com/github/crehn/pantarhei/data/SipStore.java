package com.github.crehn.pantarhei.data;

import java.util.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SipStore {

	private static Map<UUID, SipEntity> map = new HashMap<>();

	public SipEntity getSipByGuid(UUID guid) {
		log.info("get sip for guid [{}]", guid);
		SipEntity result = map.get(guid);
		if (result == null)
			throw new SipNotFoundException(guid);
		return result;
	}

	public void store(SipEntity sip) {
		log.info("store sip {}", sip);
		map.put(sip.getGuid(), sip);
	}
}

package com.ht.dl645.codec;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.dl645.service.IDL645Decoder;
import com.ht.dl645.service.IoSession;

public class DL645RequestDecoder implements IDL645Decoder {
	@SuppressWarnings("unused")
	private final static Logger LOGGER = LoggerFactory.getLogger(DL645RequestDecoder.class);
	private static final String DECODER_STATE_KEY = "DECODER.STATE";

	private static class DecoderState {
		Integer slave = null;
		Integer funCode = null;
		Integer start = null;
		Integer len = null;
		Integer byteLen = null;
		Integer crc = null;
		Integer value = null;
		int[] values = null;
		boolean[] status = null;
	}

	@Override
	public Object decode(IoSession session,
			ByteBuffer byteBuffer) {
		DecoderState decoderState = (DecoderState) session
				.getAttribute(DECODER_STATE_KEY);
		if (decoderState == null) {
			decoderState = new DecoderState();
			session.setAttribute(DECODER_STATE_KEY, decoderState);
		}

		if (decoderState.slave == null) {
			if (byteBuffer.remaining() > 0) {
				decoderState.slave = (int) byteBuffer.get() & 0xFF;
			} else {
				return null;
			}
		}
		
		
		return null;
	}

}

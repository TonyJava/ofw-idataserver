package com.ht.dl645.codec;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.dl645.FunctionCode;
import com.ht.dl645.exception.EndCodeErrorException;
import com.ht.dl645.exception.ParityErrorException;
import com.ht.dl645.exception.TransportException;
import com.ht.dl645.msg.response.ReadResponse;
import com.ht.dl645.service.IDL645Decoder;
import com.ht.dl645.util.MessageUtils;

public class DL645ResponseDecoder implements IDL645Decoder {
	private static final String DECODER_STATE_KEY = "DECODER.STATE";
	private final static Logger LOGGER = LoggerFactory
			.getLogger(DL645ResponseDecoder.class);

	private static class DecoderState {
		Integer start = null;
		String slaveBCD = null;
		Integer funCode = null;
		String dataid = null;
		Integer byteLen = null;
		String datas = null;
		Integer cs = null;
	}

	@Override
	public Object decode(com.ht.dl645.service.IoSession session,
			ByteBuffer byteBuffer) {
		DecoderState decoderState = (DecoderState) session
				.getAttribute(DECODER_STATE_KEY);
		if (decoderState == null) {
			decoderState = new DecoderState();
			session.setAttribute(DECODER_STATE_KEY, decoderState);
		}

		if (decoderState.start == null) {

			if (byteBuffer.remaining() > 0) {
				int temp = (byteBuffer.get() & 0xFF);
				if (temp != 0xFE) {// 前导帧
					//return new TransportException("前导帧错误！");
					return null;
				}
				while (byteBuffer.remaining() > 0) {
					temp = (byteBuffer.get() & 0xFF);
					if (temp == 0xFE) {// 前导帧
						continue;
					}
					if (temp == 0x68) {// 帧起始符
						break;
					} else {
						return new TransportException("帧起始符错误！");
					}
				}

				if (temp != 0x68) {// 未读到起始符
					return null;
				}
				// 1
				decoderState.start = temp;
			} else {
				return null;
			}
		}
		if (decoderState.slaveBCD == null) {
			if (byteBuffer.remaining() >= 9) {
				// 2-7
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < 6; i++) {
					sb.append(MessageUtils.decodeBCDByte(
							byteBuffer.get() & 0xFF, true));
				}
				decoderState.slaveBCD = sb.reverse().toString();

				// 8
				int temp = (byteBuffer.get() & 0xFF);// 0x68

				// 9
				decoderState.funCode = (int) byteBuffer.get() & 0xFF;
				// 10
				decoderState.byteLen = (int) byteBuffer.get() & 0xFF;
			} else {
				return null;
			}
		}

		switch (decoderState.funCode) {
		case FunctionCode.SLAVER_OK_RESPONSE1:// 0x81
			if (decoderState.dataid == null) {

				if (byteBuffer.remaining() >= (decoderState.byteLen + 2)) {

					// dataid
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < 2; i++) {
						int temp = (byteBuffer.get() & 0xFF) - 0x33;
						sb.append(MessageUtils.decodeBCDByte(temp, true));
					}
					decoderState.dataid = sb.reverse().toString();
					// LOGGER.debug(decoderState.dataid);

					// datas
					byte[] datas = new byte[decoderState.byteLen - 2];
					byteBuffer.get(datas);
					sb = new StringBuilder();
					for (int i = 0; i < datas.length; i++) {
						int temp = (datas[i] & 0xFF) - 0x33;
						sb.append(MessageUtils.decodeBCDByte(temp, true));
					}
					decoderState.datas = sb.reverse().toString();
					// LOGGER.debug(decoderState.datas);

					ReadResponse resp = new ReadResponse(decoderState.slaveBCD,
							decoderState.funCode, decoderState.byteLen,
							decoderState.dataid, decoderState.datas);

					// cs
					decoderState.cs = byteBuffer.get() & 0xFF;
					// LOGGER.debug(Integer.toHexString(decoderState.cs));

					// end 0x16
					int temp = byteBuffer.get() & 0xFF;
					// LOGGER.debug(Integer.toHexString(temp));
					if (temp != 0x16) {
						return new TransportException("帧结束符错误！");
					}

					// 校验检查
					if (resp.getCS() != decoderState.cs) {
						LOGGER.warn("校验码错误!");
						return new ParityErrorException("校验码错误!");
					}

					assert temp == 0x16;
					if (temp != 0x16) {
						LOGGER.warn("结束码错误!");
						return new EndCodeErrorException("结束码错误!");
					}

					return resp;
				} else {
					return null;
				}
			}

			break;

		default:
			// unsuport
			break;
		}

		return null;
	}
}

package com.ht.dl645.service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.dl645.util.HexMessageDumper;

public abstract class AbstractIoConnector implements IoConnector {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractIoConnector.class);
	private static final int BUFFER_SIZE = 512;
	private IoSession session = new IoSession();// 用于记录IO状态

	private ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
	private int mark = -1;
	private int limit = 0;

	@Override
	public void write(Object message) {
		if (!isOpen() || getOutputStream() == null) {
			LOGGER.warn("IO连接未建立。");
			return;
		}
		byte[] data = getEncoder().encode(session, message);

		LOGGER.debug(">>" + HexMessageDumper.getHexdump(data));
		try {

			while (getInputStream().available() > 0) {
				getInputStream().skip(getInputStream().available());
			}
			getOutputStream().write(data);
			getOutputStream().flush();

		} catch (IOException e) {
			LOGGER.equals(e.getMessage());
		}
	}

	@Override
	public Object read() {
		if (!isOpen() || getInputStream() == null) {
			return null;
		}

		long endTime = System.currentTimeMillis() + getTimeout();
		byteBuffer.clear();
		mark = -1;
		limit = 0;

		while (this.isOpen()) {
			int dataSize;
			try {
				dataSize = getInputStream().available();
				byte[] data = new byte[dataSize];
				int readBytes = getInputStream().read(data);

				if (readBytes > 0) {
					if (limit <= 0) {
						limit = readBytes;
					} else {
						limit += readBytes;
					}
					byteBuffer.limit(limit);

					if (mark >= 0) {// reset mark for put
						byteBuffer.position(mark);
					}
					byteBuffer.put(data, 0, readBytes);

					if (mark < 0) {// start
						mark = byteBuffer.position();// mark for put
						byteBuffer.flip();// go to the head
					} else {
						mark = byteBuffer.position();// mark for put
						byteBuffer.reset();// reset mark for get
					}

					Object obj = getDecoder().decode(session, byteBuffer);
					if (obj != null) {
						if (obj instanceof Exception) {
							LOGGER.error("	<<"
									+ HexMessageDumper.getHexdump(Arrays
											.copyOf(byteBuffer.array(), limit)));
						} else {
//							LOGGER.debug("	<<"
//									+ HexMessageDumper.getHexdump(Arrays
//											.copyOf(byteBuffer.array(), limit)));

						}
						// 丢掉剩余的数据
						while (getInputStream().available() > 0) {
							getInputStream().skip(getInputStream().available());
						}
						return obj;
					}
					byteBuffer.mark();// mark for get
				} else {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
					}// 优化性能
				}
			} catch (IOException e) {
				LOGGER.error("	<<"
						+ HexMessageDumper.getHexdump(Arrays.copyOf(
								byteBuffer.array(), limit)));
				return e;
			}
			if (endTime < System.currentTimeMillis()) {// timeout
				LOGGER.warn("	<<"
						+ HexMessageDumper.getHexdump(Arrays.copyOf(
								byteBuffer.array(), limit)));
				return null;
			}
		}

		LOGGER.debug("	<<"
				+ HexMessageDumper.getHexdump(Arrays.copyOf(byteBuffer.array(),
						limit)));
		return null;
	}

	@Override
	public IoSession getIoSession() {
		return this.session;
	}
}

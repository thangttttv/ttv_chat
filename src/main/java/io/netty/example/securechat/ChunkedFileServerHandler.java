package io.netty.example.securechat;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ChunkedFileServerHandler extends ChunkedWriteHandler {

	private Logger logger = Logger.getLogger(this.getClass());

	private File file;

	public ChunkedFileServerHandler(ChannelHandlerContext ctx, File file) {
		this.file = file;

		logger.info("New ChunkedFileServerHandler");
		ChunkedFile chunkedFile;
		try {
			chunkedFile = new ChunkedFile(this.file);
			ctx.writeAndFlush(chunkedFile);
			ctx.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		logger.info("FILE WRITE GETS ACTIVE");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}

package io.netty.example.securechat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.stream.ChunkedFile;

import org.apache.log4j.Logger;

public class FileChunkReqWriteHandler extends SimpleChannelInboundHandler<ChunkedFile> {

	Envelope fileRequestProtocol;
	private Logger logger = Logger.getLogger(this.getClass());

	public FileChunkReqWriteHandler(Envelope msg) {
		this.fileRequestProtocol = msg;
		logger.info("New ChunkedFile Handler " + msg);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		logger.info("in channel active method");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();

		if (ctx.channel().isActive()) {
			ctx.writeAndFlush(
					"ERR: " + cause.getClass().getSimpleName() + ": "
							+ cause.getMessage() + '\n').addListener(
					ChannelFutureListener.CLOSE);
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ChunkedFile msg)
			throws Exception {
		logger.info("in channelRead0");

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		logger.info("channelRead");
		ByteBuf buf = (ByteBuf) msg;
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		if (buf.readableBytes() >= this.fileRequestProtocol.getLength() ) {
			logger.info("received all data");
		}
	}
}

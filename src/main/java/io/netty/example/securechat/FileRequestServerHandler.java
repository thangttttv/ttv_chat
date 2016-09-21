package io.netty.example.securechat;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.stream.ChunkedFile;

public class FileRequestServerHandler extends
		SimpleChannelInboundHandler<FileRequestProtocol> {

	private File f;
	private Logger logger = Logger.getLogger(this.getClass());

	//@Override
	public void channelRead011(ChannelHandlerContext ctx,
			FileRequestProtocol fileRequest) {
		logger.info("Server new FileRequest " + fileRequest);
		f = new File(fileRequest.getFilePath());
		fileRequest.setFileSize(f.length());
		ctx.writeAndFlush(fileRequest);

		new ChunkedFileServerHandler(ctx, f);
	}
	
	
	 @Override
	    public void channelRead0(ChannelHandlerContext ctx, FileRequestProtocol fileRequest) {
	        logger.info("Server new FileRequest " + fileRequest);
	        f = new File(fileRequest.getFilePath());
	        fileRequest.setFileSize(f.length());
	        ctx.writeAndFlush(fileRequest);

	        // directly make your chunkedFile there instead of creating a sub handler
	        ChunkedFile chunkedFile;
			try {
				chunkedFile = new ChunkedFile(this.f);
				  ctx.writeAndFlush(chunkedFile);// need a specific handler
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      
	        // Don't create such an handler: new ChunkedFileServerHandler(ctx,f);
	}
	 
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		logger.info("Server read complete");

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}

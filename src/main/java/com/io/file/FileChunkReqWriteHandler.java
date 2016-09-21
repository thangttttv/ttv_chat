package com.io.file;

import java.io.File;
import java.io.FileOutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.stream.ChunkedFile;

public class FileChunkReqWriteHandler extends SimpleChannelInboundHandler<ChunkedFile> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("in channel active method");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();

        if (ctx.channel().isActive()) {
            ctx.writeAndFlush("ERR: " +
                    cause.getClass().getSimpleName() + ": " +
                    cause.getMessage() + '\n').addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChunkedFile msg)
            throws Exception {
        System.out.println("in channelRead0");

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	if(msg instanceof ChunkedFile ) System.out.println("msg");
    	
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        System.out.println(new String("so data"+bytes.length));
        
        FileOutputStream fileOut = new FileOutputStream(new File(
				"C:/Projects/Android/sql.txt"));
		fileOut.write(bytes);
		fileOut.close();
    }

}

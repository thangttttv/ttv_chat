package com.ttv.chat;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Calendar;

import org.apache.log4j.Logger;

import com.ttv.bean.SwapEnvelope;
import com.ttv.process.SwapEnvelopeFactory;

public class MyHandler extends ChannelDuplexHandler {
	final static Logger logger = Logger.getLogger(MyHandler.class);
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    	
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
            	logger.info(" Qua 60 s"+ " left join");
                ctx.close();
            } else if (e.state() == IdleState.WRITER_IDLE) {
            	logger.info(" Server send ping "+ctx.name());
            	SwapEnvelope ping = SwapEnvelopeFactory.builderEnvelopePing(0,0, 0, 0, Calendar.getInstance().getTimeInMillis());
                ctx.writeAndFlush(ping);
            }
        }
    }
}
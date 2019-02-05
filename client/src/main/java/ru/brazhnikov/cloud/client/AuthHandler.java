package ru.brazhnikov.cloud.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.brazhnikov.cloud.common.CommandMessage;

public class AuthHandler extends ChannelInboundHandlerAdapter {

    public AuthHandler() {}

    @Override
    public void channelRead( ChannelHandlerContext ctx, Object msg ) throws Exception {
        if ( msg == null ) {
            return;
        }

        if ( msg instanceof CommandMessage ) {
            CommandMessage cm = (CommandMessage ) msg;

            if ( cm.getType() == CommandMessage.CMD_MSG_AUTH_OK ) {
                ctx.pipeline().remove( this );
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

package ru.brazhnikov.cloud.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.brazhnikov.cloud.common.AuthMessage;

public class AuthGatewayHandler extends ChannelInboundHandlerAdapter {

    private boolean autohorized = false;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println( "New unauthorizet client connected..." );
    }

    @Override
    public void channelRead( ChannelHandlerContext ctx, Object msg ) throws Exception {
        System.out.println( "### AuthGatewayHandler => channelRead" );
        if ( msg == null ) {
            return;
        }

        if ( !this.autohorized ) {
            if ( msg instanceof AuthMessage) {
                AuthMessage am = (AuthMessage) msg;
                //ctx.pipeline().addLast( new MainHandler() );
            }
            else {
                ReferenceCountUtil.release( msg );
            }
        }
        else {
            ctx.fireChannelRead( msg );
        }
    }
}

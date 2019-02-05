package ru.brazhnikov.cloud.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.brazhnikov.cloud.common.AuthMessage;
import ru.brazhnikov.cloud.common.CommandMessage;

public class AuthGatewayHandler extends ChannelInboundHandlerAdapter {

    private static boolean authorized = false;

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

        if ( !this.authorized ) {
            if ( msg instanceof AuthMessage ) {
                AuthMessage am = (AuthMessage) msg;

                DbHandler dbHandler = DbHandler.getInstance();

                if ( dbHandler.getUserByName( am.getLogin() ) != null ) {
                    System.out.println( " dbHandler.getUserByName( am.getLogin() )" );
                    this.authorized = true;
                    CommandMessage commandMessage = new CommandMessage();
                    ctx.writeAndFlush( commandMessage ).await();
                    ctx.pipeline().addLast( new MainHandler() );

                    //System.out.println( "am.getLogin() : " + am.getLogin());
                    //System.out.println( "am.getPassword() : " + am.getPassword());
                }
            }
            else {
                ReferenceCountUtil.release( msg );
            }
        }
        else {
            ctx.fireChannelRead( msg );
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

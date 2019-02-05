package ru.brazhnikov.cloud.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.brazhnikov.cloud.common.AuthMessage;
import ru.brazhnikov.cloud.common.CommandMessage;

/**
 * AuthGatewayHandler - класс
 *
 * @version 1.0.1
 * @package ru.brazhnikov.cloud.server
 * @author  Vasya Brazhnikov
 * @copyright Copyright (c) 2019, Vasya Brazhnikov
 */
public class AuthGatewayHandler extends ChannelInboundHandlerAdapter {

    /**
     *  @access private
     *  @var boolean authorized -
     */
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

                AuthMessage am      = (AuthMessage) msg;
                DbHandler dbHandler = DbHandler.getInstance();
                User user           = dbHandler.getUserByName( am.getLogin() );

                if ( user.pass.equals( am.getPassword() ) ) {

                    CommandMessage commandMessage = new CommandMessage();
                    ctx.writeAndFlush( commandMessage ).await();
                    ctx.pipeline().addLast( new MainHandler() );
                    this.authorized = true;
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
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.authorized = false;
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

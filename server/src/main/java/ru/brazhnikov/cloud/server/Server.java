package ru.brazhnikov.cloud.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * Server -
 *
 * @version 1.0.1
 * @package ru.brazhnikov.cloud.server
 * @author  Vasya Brazhnikov
 * @copyright Copyright (c) 2019, Vasya Brazhnikov
 */
public class Server {

    /**
     *  @access private
     *  @var final int - максимальный размер передаваемого файла !Fixme
     */
    private static final int MAX_OBJ_SIZE = 100 * 1024 * 1024;

    /**
     * run - запустить сервер
     * @throws Exception
     */
    public void run() throws Exception {
        EventLoopGroup mainGroup   = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group( mainGroup, workerGroup )
                .channel( NioServerSocketChannel.class )
                .childHandler( new ChannelInitializer<SocketChannel>() {
                    protected void initChannel( SocketChannel socketChannel ) throws Exception {

                    socketChannel.pipeline().addLast(
                        new ObjectDecoder( MAX_OBJ_SIZE, ClassResolvers.cacheDisabled(null ) ),
                        new ObjectEncoder(),
                        new MainHandler()
                    );
                    }
                })
                .option( ChannelOption.SO_BACKLOG, 128 )
                .option( ChannelOption.TCP_NODELAY, true )
                .childOption( ChannelOption.SO_KEEPALIVE, true );

            ChannelFuture future = b.bind(8189 ).sync();
            future.channel().closeFuture().sync();
        }
        finally {
            mainGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main( String[] args ) throws Exception {
        new Server().run();
    }
}

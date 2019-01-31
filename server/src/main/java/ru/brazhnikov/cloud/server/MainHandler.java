package ru.brazhnikov.cloud.server;

import ru.brazhnikov.cloud.common.FileMessage;
import ru.brazhnikov.cloud.common.FileRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * MainHandler -
 *
 * @version 1.0.1
 * @package ru.brazhnikov.cloud.server
 * @author  Vasya Brazhnikov
 * @copyright Copyright (c) 2019, Vasya Brazhnikov
 */
public class MainHandler extends ChannelInboundHandlerAdapter {

    /**
     *  @access private
     *  @var String serverStorageDir - путь к папке с файлами на сервере
     */
    private String serverStorageDir = "server_storage/";

    @Override
    public void channelRead( ChannelHandlerContext ctx, Object msg ) throws Exception {
        try {

            if ( msg == null ) {
                return;
            }

            if ( msg instanceof FileRequest ) {
                System.out.println( "### SERVER MainHandler => msg instanceof FileRequest" );
                FileRequest fr = ( FileRequest ) msg;
                if ( Files.exists( Paths.get(this.serverStorageDir + fr.getFilename() ) ) ) {

                    FileMessage fm = new FileMessage( Paths.get("server_storage/" + fr.getFilename() ) );
                    ctx.writeAndFlush( fm );
                }
            }
            else if ( msg instanceof FileMessage ) {
                System.out.println( "### SERVER MainHandler => msg instanceof FileMessage" );
                FileMessage fileMessage = ( FileMessage ) msg;
                Files.write(
                    Paths.get(this.serverStorageDir + fileMessage.getFilename()),
                    fileMessage.getData(),
                    StandardOpenOption.CREATE
                );
            }
        }
        finally {
            ReferenceCountUtil.release( msg );
        }
    }

    @Override
    public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

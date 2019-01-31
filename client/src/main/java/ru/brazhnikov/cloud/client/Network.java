package ru.brazhnikov.cloud.client;

import java.net.Socket;
import java.io.IOException;
import ru.brazhnikov.cloud.common.AbstractMessage;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

/**
 * Network -
 *
 * @version 1.0.1
 * @package ru.brazhnikov.cloud.client
 * @author  Vasya Brazhnikov
 * @copyright Copyright (c) 2019, Vasya Brazhnikov
 */
public class Network {

    /**
     *  @access private
     *  @var Socket socket -
     */
    private static Socket socket;

    /**
     *  @access private
     *  @var ObjectEncoderOutputStream out -
     */
    private static ObjectEncoderOutputStream out;

    /**
     *  @access private
     *  @var ObjectDecoderInputStream in -
     */
    private static ObjectDecoderInputStream in;

    /**
     *  @access private
     *  @var int MAX_OBJ_SIZE -
     */
    private static final int MAX_OBJ_SIZE = 100 * 1024 * 1024;

    /**
     * start - подключается к серверу по сокету,
     * открытает потоки на запись и на считывание
     */
    public static void start() {
        try {
            socket = new Socket("localhost", 8189 );
            out    = new ObjectEncoderOutputStream( socket.getOutputStream() );
            in     = new ObjectDecoderInputStream( socket.getInputStream(), MAX_OBJ_SIZE );
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    /**
     * stop - останавливает сетевое взаимодействие,
     * закрывает потоки на ссчитывание и на запись
     */
    public static void stop() {

        try {
            out.close();
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }

        try {
            in.close();
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }

        try {
            socket.close();
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    /**
     * sendMsg - отправить сообщение
     * @param msg - объект сообщение
     * @return
     */
    public static boolean sendMsg( AbstractMessage msg ) {
        try {
            out.writeObject( msg );
            return true;
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * readObject -
     * @return AbstractMessage
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static AbstractMessage readObject() throws ClassNotFoundException, IOException {
        Object obj = in.readObject();
        return ( AbstractMessage ) obj;
    }


}



import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Echo {

    private static final Logger logger = LoggerFactory.getLogger(Echo.class);

    private String serverAddress;

    private Map<String, Object> handlerMap = new HashMap<>();
    private static ThreadPoolExecutor threadPoolExecutor;

    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;

	public Echo(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public static void main(String[] args) throws Exception{
        System.out.println("main.");
        
        new Echo("127.0.0.1:8181").start();   
    }

    public void start() throws Exception {
            ServerBootstrap bootstrap = new ServerBootstrap();

            NioEventLoopGroup workerGroup = new NioEventLoopGroup();
            bootstrap.group(workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {

                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);

            Channel channel = bootstrap.bind(port).sync().channel();
            System.out.println("channel.");
            logger.info("Server started on port {}", port);

        //Scheduled
        ScheduledFuture<?> future = channel.eventLoop().scheduleAtFixedRate(
        new Runnable() {
            @Override
            public void run() {
                System.out.println("Run every 33 seconds");
            }
        }, 3, 3, TimeUnit.SECONDS);
        
        for (int i=5; i< 1000; i++) {
            System.out.println("Run every:" + i);
        channel.eventLoop().scheduleAtFixedRate(
        new Runnable() {
            @Override
            public void run() {
                System.out.println("Run every seconds" );
            }
        }, i, 5, TimeUnit.SECONDS); 
        }   
        

    }

}


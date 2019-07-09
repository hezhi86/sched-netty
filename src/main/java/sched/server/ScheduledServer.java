import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class ScheduledServer implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(ScheduledServer.class);

	private String serverAddress;

	public ScheduledServer(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    public void start() throws Exception {
    	ServerBootstrap bootstrap = new ServerBootstrap();

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        bootstrap.group(workerGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new ScheduledServerHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        String[] array = serverAddress.split(":");
        String host = array[0];
        int port = Integer.parseInt(array[1]);

        Channel channel = bootstrap.bind(port).sync().channel();
        logger.info("Server started on port {}", port);

        //Scheduled
        ScheduledFuture<?> future = channel.eventLoop().scheduleAtFixedRate(
        new Runnable() {
            @Override
            public void run() {
                logger.info("Run every 33 seconds");
            }
        }, 5, 5, TimeUnit.SECONDS);
                
    }

}


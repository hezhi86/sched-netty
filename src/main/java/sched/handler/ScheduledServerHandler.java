
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientResponse;
import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;
import com.orbitz.consul.StatusClient;
import com.orbitz.consul.KeyValueClient;

/**
 * 处理服务端 channel.
 */
public class ScheduledServerHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ScheduledServerHandler.class);

    private volatile Channel channel;

    private volatile Consul consul;

    /*
     * https://api.seniverse.com/v3/weather/now.json?key=w99tf57ghc86thhv&location=beijing&language=zh-Hans&unit=c
	 */
    @Override
	public void channelActive(final ChannelHandlerContext ctx) {
		logger.info("channelActive.");
        String res = HttpClient.create()
        .get()
        .uri("https://api.seniverse.com/v3/weather/now.json?key=w99tf57ghc86thhv&location=beijing&language=zh-Hans&unit=c")
        .responseContent()
        .aggregate()
        .asString()
        .block();
        logger.info("## responseContent-->{}", res);
        

        consul = Consul.builder().withHostAndPort(HostAndPort.fromString("10.100.163.16:8500")).build();
        KeyValueClient kvClient = consul.keyValueClient();
        kvClient.putValue("foo", "bay");
	}

    /*
     * nc 127.0.0.1 port
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {      
        ByteBuf bufIn = (ByteBuf) msg;      
        String str = bufIn.toString(CharsetUtil.UTF_8).replace("\n", "");
        logger.info("channelRead<<{}", str);

        StatusClient statusClient = consul.statusClient();
        logger.info("==============getLeader:{}", statusClient.getLeader());

        KeyValueClient kvClient = consul.keyValueClient();
        String value = kvClient.getValueAsString("foo").get();
        logger.info("==============kvClient:{}", value);


        this.channel = ctx.channel();
        //Scheduled        
        //this.scheduleOnce(this.channel, 3);
        //this.scheduleRate(this.channel, 5, 5);
    }

    /*
     * run->Thread.sleep();会阻塞线程
     */
    private void scheduleOnce(Channel channel, int delay) {
        ScheduledFuture<?> future = channel.eventLoop().schedule(
            new Runnable() {
            @Override
            public void run() {                
                logger.info("Run later at {}", delay);
            }
        }, delay, TimeUnit.SECONDS);
    }

    private void scheduleRate(Channel channel, int delay, int rate) {
        ScheduledFuture<?> future = channel.eventLoop().scheduleAtFixedRate(
            new Runnable() {
            @Override
            public void run() {
                logger.info("Run every {} seconds", rate);
                String res = HttpClient.create()
                .get()
                .uri("https://api.seniverse.com/v3/weather/now.json?key=w99tf57ghc86thhv&location=beijing&language=zh-Hans&unit=c")
                .responseContent()
                .aggregate()
                .asString()
                .block();
                logger.info("==============responseContent->{}", res);
            }
        }, delay, rate, TimeUnit.SECONDS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
    
}
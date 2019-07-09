
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理服务端 channel.
 */
public class ScheduledServerHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ScheduledServerHandler.class);

    private volatile Channel channel;

	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		logger.info("channelActive.");
	}

    /*
     * nc 127.0.0.1 port
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {      
        ByteBuf bufIn = (ByteBuf) msg;      
        String str = bufIn.toString(CharsetUtil.UTF_8).replace("\n", "");
        logger.info("channelRead<<{}", str);

        this.channel = ctx.channel();
        //Scheduled        
        this.scheduleOnce(this.channel, 3);
        this.scheduleInteral(this.channel, 5, 5);
    }

    private void scheduleOnce(Channel channel, int delay) {
        ScheduledFuture<?> future = channel.eventLoop().schedule(
            new Runnable() {
            @Override
            public void run() {
                logger.info("Run later at {}", delay);
            }
        }, delay, TimeUnit.SECONDS);
    }

    private void scheduleInteral(Channel channel, int delay, int interal) {
        ScheduledFuture<?> future = channel.eventLoop().scheduleAtFixedRate(
            new Runnable() {
            @Override
            public void run() {
                logger.info("Run every {} seconds", interal);
            }
        }, delay, interal, TimeUnit.SECONDS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
    
}
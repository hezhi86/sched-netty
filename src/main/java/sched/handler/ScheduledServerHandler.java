
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

    /**
     * 处理服务端 channel.
     */
    public class ScheduledServerHandler extends ChannelInboundHandlerAdapter {

    	private static final Logger logger = LoggerFactory.getLogger(ScheduledServerHandler.class);

    	@Override
    	public void channelActive(final ChannelHandlerContext ctx) {
			System.out.println("=====================channelActive...");
    	}

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("channelRead......");                
        //Scheduled
        ScheduledFuture<?> future = ctx.channel().eventLoop().scheduleAtFixedRate(
        new Runnable() {
            @Override
            public void run() {
                logger.info("Run every 55 seconds");
            }
        }, 5, 5, TimeUnit.SECONDS);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            // 当出现异常就关闭连接
            cause.printStackTrace();
            ctx.close();
        }
        
}
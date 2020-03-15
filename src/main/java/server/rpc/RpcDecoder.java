package server.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import server.common.SerializationUtil;

import java.util.List;

/**
 * @author xkl
 * @date 2020/3/15
 * @description
 **/
public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;

    public RpcDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        //标记可读索引位置
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0) {
            ctx.close();
        }

        if (in.readableBytes() < dataLength) {
            //恢复索引位置
            in.resetReaderIndex();
            return;
        }

        byte[] data = new byte[dataLength];
        in.readBytes(data);
        Object obj = SerializationUtil.deserialize(data, genericClass);
        out.add(obj);

    }
}

package server.common;

import com.dyuproject.protostuff.Schema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xkl
 * @date 2020/3/15
 * @description
 **/
public class SerializationUtil {
    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    private static Objenesis objenesis = new ObjenesisStd(true);

    public SerializationUtil() {
    }

}

package cn.btzh.multipleuploadfiles.biz;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * 说明:自定义转转换器，返回原始字符串
 *      对使用retrofit框架 返回的数据不进行gson转换，
 *      目的将返回字符串数据转换公用实体 CallBackVo，然后回调到具体的Presneter中做处理将CallBackVo的resObj转为需要的bean
 */
public class DeserializerData implements JsonDeserializer {
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return json.toString();
    }
}

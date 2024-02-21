package com.banx.config;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建 FastJsonHttpMessageConverter 对象
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        // 创建 FastJson 的配置对象
        FastJsonConfig config = this.getFastJsonConfig();

        // 将配置应用到 FastJsonHttpMessageConverter
        converter.setFastJsonConfig(config);
        // 设置默认字符集为 UTF-8
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        // 设置支持的媒体类型为 APPLICATION_JSON
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        // 将 FastJsonHttpMessageConverter 添加到 converters 列表的首位
        converters.add(0, converter);
    }

    private   FastJsonConfig getFastJsonConfig() {
        FastJsonConfig config = new FastJsonConfig();
        /*
        * 设置日期格式为 "yyyy-MM-dd HH:mm:ss"。日期类型的字段会按照指定的格式转换为对应的字符串格式
        * 这里设置了输出的日期格式，日期类型的字段会按照指定的格式转换为对应的字符串格式
        * 同样的，在反序列时前端传过来的日期数据也要符合"yyyy-MM-dd HH:mm:ss" ，不然无法解析成Date对象或直接抛出异常*/
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        // 设置 JSONReader 特性，这里使用 FieldBased 和 SupportArrayToBean
        config.setReaderFeatures(JSONReader.Feature.FieldBased, JSONReader.Feature.SupportArrayToBean);
        // 设置 JSONWriter 特性，这里使用 PrettyFormat :输出的 JSON 字符串将进行格式化，添加缩进和换行符，使其更易读。
        config.setWriterFeatures(JSONWriter.Feature.PrettyFormat,
                // 将Long类型作为String序列化
                JSONWriter.Feature.WriteLongAsString);
        return config;
    }

}

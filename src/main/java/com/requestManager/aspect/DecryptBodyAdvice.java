//package com.requestManager.aspect;
//
//import com.alibaba.fastjson.JSONObject;
//import com.requestManager.util.AesCBC;
//import lombok.SneakyThrows;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.HttpInputMessage;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
//
//import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.lang.reflect.Type;
//import java.nio.charset.StandardCharsets;
//
//@ControllerAdvice
//public class DecryptBodyAdvice implements RequestBodyAdvice {
//
//    @Override
//    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
//        // 在这里可以根据需求判断是否要对当前方法的请求body进行处理
//        return true; // 支持对所有方法的请求body进行处理
//    }
//
//    @Override
//    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
//        // 在请求body为空时进行处理，如果不需要特殊处理，直接返回body即可
//        return body;
//    }
//
//    @SneakyThrows
//    @Override
//    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
//        // 在请求body被读取之前进行处理
//        // 可以在这里获取到原始的InputStream
//        InputStream requestBodyStream = inputMessage.getBody();
//        StringBuilder stringBuilder = new StringBuilder();
//        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(requestBodyStream, StandardCharsets.UTF_8))) {
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuilder.append(line);
//            }
//        }
//        String requestBody = stringBuilder.toString();
//        String decrypt = AesCBC.decrypt(requestBody);
//
////        decrypt = analyzeParam(requestBody);
//        decrypt = analyzeParam(decrypt);
//
//
//        byte[] bytes = decrypt.getBytes(StandardCharsets.UTF_8);
//        // 使用ByteArrayInputStream将字节数组包装成InputStream
//        InputStream inputStream = new ByteArrayInputStream(bytes);
//
//        // 创建一个新的HttpInputMessage，将解密后的body内容重新封装
//        HttpInputMessage newInputMessage = new DecryptHttpInputMessage(inputStream, inputMessage.getHeaders());
//
//        // 返回新的HttpInputMessage对象
//        return newInputMessage;
//    }
//
//    private String analyzeParam(String decrypt) throws UnsupportedEncodingException {
//        JSONObject jsonObject = JSONObject.parseObject(decrypt);
//        Object data = jsonObject.get("data");
//        return data.toString().replaceAll("^\"|\"$", "");
//    }
//
//    @Override
//    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
//        // 在请求body被转换为对象后进行处理
//        // 这里必须返回Object对象，通常直接返回body即可
//        return body;
//    }
//
//}

aspect:
  AuthAspect: token校验的切面类
  DecryptRequestBodyAdvice: 入参解密类
  EncodeAspect: 请求加解密的切面类
  EncryptResponseBodyAdvice：出参加密类
  UserThreadLocal: 保存用户信息

concurrent: 
  ConcurrentExecutor: 多线程工具类

config:
  ConstantConfig: 一些常量
  RedisTemplateConfiguration：redis配置
  RestTemplateConfigration: 创建httpClient的配置类

constant: 
  BusinessException: 定义运行时异常类
  CatchExceptionHandler: 捕获全局异常类
  Constant: 一些通用常量
  ErrorCodeEnum: 错误类型的枚举
  RestResponse: controller层的通用返回实体

controller:
  TestController：测试入口类

data:
  request：发送请求
  token：生成token
  url：url配置
  user：用户

mapper: mapper文件

redis: 缓存类

util: 一些工具
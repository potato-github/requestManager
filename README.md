aspect:
  AuthAspect: token校验的切面类
  EncodeAspect: 请求加解密的切面类
  UserThreadLocal: 保存用户信息

concurrent: 
  Concurrent: 多线程类

config:
  ConstantConfig: 一些应该写在配置文件的属性
  RestTemplateConfigration: 创建httpClient的配置类

constant: 
  BusinessException: 定义运行时异常类
  CatchExceptionHandler: 捕获全局异常类
  Constant: 一些通用常量
  ErrorCodeEnum: 错误类型的枚举
  RestResponse: controller层的通用返回实体

controller:

data:

mapper:

redis:

util:
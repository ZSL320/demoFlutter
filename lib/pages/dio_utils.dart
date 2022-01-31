import 'dart:convert';
import 'dart:io';
import 'package:dio/dio.dart';
import 'package:flutter/widgets.dart';

class DioUtils {
  static final String BASE_URL = "https://www.wanandroid.com"; //base url
  static DioUtils? _instance;
  late Dio _dio;
  late BaseOptions _baseOptions;

  static DioUtils getInstance() {
    if (_instance == null) {
      _instance = new DioUtils();
    }
    return _instance!;
  }

  /**
   * dio初始化配置
   */
  DioUtils() {
    //请求参数配置
    _baseOptions = new BaseOptions(
      baseUrl: BASE_URL,
      connectTimeout: 5000,
      receiveTimeout: 5000,
      headers: {
        //预设好的header信息
        "testHeader": "bb"
      },
      contentType: ContentType.json.toString(),
      responseType: ResponseType.json,
    );

    //创建dio实例
    _dio = new Dio(_baseOptions);

    //可根据项目需要选择性的添加请求拦截器
    _dio.interceptors.add(
      InterceptorsWrapper(onRequest:
          (RequestOptions? options, RequestInterceptorHandler handler) {
        print("请求数据开始 ---------->\n");
        print("│ 请求方式: ${options!.method.toString()}");
        print("│ 请求url:  ${options.uri.toString()}");
        print("│ 请求头:  ${options.headers}");
        print("│ 请求参数: ${options.queryParameters}");
        print("│ 请求参数body: ${options.data}\n\n");
        handler.next(options);
      }, onResponse: (Response response, ResponseInterceptorHandler handler) {
        print("开始响应 ---------->\n");
        print("${response.realUri}\n");
        print("code = ${response.statusCode}");
        print("data = ${json.encode(response.data)}");
        // LogUtil.v("data = ${json.encode(response.data)}"); //打印长Log
        handler.next(response);
        print("响应结束 ---------->\n\n\n");
      }, onError: (DioError e, ErrorInterceptorHandler handler) {
        print("错误响应数据 ---------->\n");
        print("type = ${e.type}");
        if (e.type == DioErrorType.other) {}
        print("message = ${e.message}");
        print("stackTrace = ${e.message}");
        print("\n");
        handler.next(e);
      }),
    );
  }

  /**
   * get请求
   */

  get(url, {data, options}) async {
    print('get request path ------${url}-------请求参数${data}');
    print('------------');
    Response? response;
    try {
      response = await _dio.get(url, queryParameters: data, options: options);
      debugPrint('get result ---${response.data}');
    } on DioError catch (e) {
      print('请求失败---错误类型${e.type}--错误信息${e.message}');
    }

    return response!.data.toString();
  }

  /**
   * Post请求
   */
  post(url, {data, options}) async {
    print('post request path ------${url}-------请求参数${data}');
    Response? response;
    try {
      response = await _dio.post(url, queryParameters: data, options: options);
      print('post result ---${response.data}');
    } on DioError catch (e) {
      print('请求失败---错误类型${e.type}--错误信息${e.message}');
    }

    return response!.data.toString();
  }
}

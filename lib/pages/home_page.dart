import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
// import 'package:fluttertoast/fluttertoast.dart';
import 'my_chat_page.dart';
import 'my_drawer.dart';
import 'my_home_page.dart';
import 'my_single_page.dart';

class HomePage extends StatefulWidget {
  const HomePage({Key? key}) : super(key: key);
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage>
    with SingleTickerProviderStateMixin, WidgetsBindingObserver {
  int last = 0;
  static final MethodChannel _MethodChannel =
      MethodChannel("com.zhuandian.flutter/android"); //平台交互通道
  static final EventChannel _EventChannel = EventChannel(
      "com.zhuandian.flutter/android/event"); //原生平台主动调用flutter端事件通道
  Future<bool> doubleClickBack() {
    int now = DateTime.now().millisecondsSinceEpoch;
    if (now - last > 1000) {
      last = DateTime.now().millisecondsSinceEpoch;
      // Fluttertoast.showToast(msg: "双击退出应用");
      return Future.value(false);
    } else {
      return Future.value(true);
    }
  }

  late EventChannel photoChannel=new EventChannel("getPhoto");
  late MethodChannel photoMethod=new MethodChannel("sendPhotoToFlutter");

  late TabController _controller;
  late PageController _pageController;

  int _tabIndex = 0;
  String _fromNativeInfo = "";
  StreamController<String> streamController = new StreamController<String>();
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance!.addObserver(this);
    _EventChannel.receiveBroadcastStream().listen(_onEvent, onError: _onErroe);
    _MethodChannel.invokeMethod("nativeSendMessage2Flutter");
    _controller = TabController(length: 3, vsync: this);
    _pageController = PageController(
      initialPage: 0,
      keepPage: true,
      viewportFraction: 1,
    );
    photoChannel.receiveBroadcastStream().listen((event) {
      setState(() {
        print(666666);
        print(event);
        streamController.sink.add(event);
      });
    });
    photoMethod.invokeMethod("sendPhoto");
  }

  Future<dynamic> nativeCallHandler(MethodCall methodCall) async {
    switch (methodCall.method) {
      case "getFlutterResult":
        String paramsFromNative = await methodCall.arguments["params"];
        print("原生android传递过来的参数为------ $paramsFromNative");
        return "result from flutter";
    }
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) async {
    super.didChangeAppLifecycleState(state);
    // 监听生命周期的变化
    setState(() {
      // 更新生命状态，触发build方法的调用
    });
  }

  /**
   * 监听原生传递回来的值（通过eventChannel）
   */
  void _onEvent(Object? object) {
    _fromNativeInfo = object.toString();
    print(6666666);
    print(object);
    if (_fromNativeInfo != "") {
      // streamController.sink.add(_fromNativeInfo);
      setState(() {});
    }
  }

  void _onErroe(Object object) {}

  @override
  void dispose() {
    _controller.dispose();
    WidgetsBinding.instance!.removeObserver(this);
    streamController.close();
    super.dispose();
  }

  List bottomList = [
    "首页",
    "聊天",
    "我的",
  ];

  List bottomIconList = [
    Icons.home,
    Icons.chat,
    Icons.my_location_sharp,
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      drawer: _tabIndex == 0
          ? Container(
              width: 250,
              child: MyDrawer(
                title: "我是左侧边栏",
              ),
            )
          : null,
      endDrawer: _tabIndex == 0
          ? Container(
              width: 250,
              child: MyDrawer(
                title: "我是右侧边栏",
              ),
            )
          : null,
      appBar: AppBar(
        title: Text("${bottomList[_tabIndex]}"),
        centerTitle: true,
        leading: Container(),
        actions: [Container()],
      ),
      body: WillPopScope(
          onWillPop: doubleClickBack,
          child: PageView(
            physics: NeverScrollableScrollPhysics(),
            onPageChanged: (index) {
              index = _tabIndex;
            },
            children: [
              MyHomePage(
                title: bottomList[_tabIndex],
              ),
              MyChatPage(
                title: bottomList[_tabIndex],
                streamController: streamController,
              ),
              MySinglePage(
                title: bottomList[_tabIndex],
              )
            ],
            controller: _pageController,
          )),
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: _tabIndex,
        items: bottomList
            .map((e) => BottomNavigationBarItem(
                icon: Icon(
                  bottomIconList[bottomList.indexOf(e)],
                  size: _tabIndex == bottomList.indexOf(e) ? 20 : 16,
                  color:
                      _tabIndex == bottomList.indexOf(e) ? Colors.blue : null,
                ),
                title: Text("$e")))
            .toList(),
        onTap: (index) {
          _tabIndex = index;
          _pageController.jumpToPage(_tabIndex);
          setState(() {});
        },
      ),
    );
  }
}

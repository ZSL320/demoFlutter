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

  late TabController _controller;
  late PageController _pageController;
  int _tabIndex = 0;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance!.addObserver(this);
    _controller = TabController(length: 3, vsync: this);
    _pageController = PageController(
      initialPage: 0,
      keepPage: true,
      viewportFraction: 1,
    );
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) async {
    super.didChangeAppLifecycleState(state);
    // 监听生命周期的变化
    setState(() {
      // print(66666);
      // 更新生命状态，触发build方法的调用
    });
  }

  @override
  void dispose() {
    _controller.dispose();
    WidgetsBinding.instance!.removeObserver(this);
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

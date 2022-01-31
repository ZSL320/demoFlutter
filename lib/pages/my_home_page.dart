import 'package:flutter/material.dart';
import 'my_drawer.dart';
import 'my_tab_page.dart';

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, this.title}) : super(key: key);
  final String? title;
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage>
    with SingleTickerProviderStateMixin, AutomaticKeepAliveClientMixin {
  late TabController _controller;
  int _tabIndex = 0;
  @override
  void initState() {
    super.initState();
    _controller = TabController(length: tabList.length, vsync: this);
  }

  @override
  bool get wantKeepAlive => true;

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  List tabList = [
    "视频",

  ];
  bool onChange = true;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      drawer: Container(
        width: 250,
        child: MyDrawer(
          title: "我是左边侧边栏",
        ),
      ),
      body: Stack(
        children: [
          Container(
            margin: EdgeInsets.only(top: 30),
            child: TabBar(
              onTap: (index) {
                _tabIndex = index;
              },
              controller: _controller,
              tabs: tabList
                  .map((e) => Container(
                        child: Text(
                          "$e",
                          style: TextStyle(color: Colors.black, fontSize: 16),
                        ),
                      ))
                  .toList(),
            ),
          ),
          Container(
            margin: EdgeInsets.only(top: 66),
            child: TabBarView(
              controller: _controller,
              physics: BouncingScrollPhysics(),
              children: tabList
                  .map((e) => MyTabPage(
                        tabIndex: tabList.indexOf(e),
                      ))
                  .toList(),
            ),
          ),
        ],
      ),
      // floatingActionButton: FloatingActionButton(
      //   onPressed: () {
      //     onChange
      //         ? Scaffold.of(context).openDrawer()
      //         : Scaffold.of(context).openEndDrawer();
      //     onChange = !onChange;
      //     setState(() {});
      //   },
      //   child: Text(onChange ? "左边" : "右边"),
      // ),
    );
  }
}

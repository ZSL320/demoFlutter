import 'package:flutter/material.dart';
import 'package:zslDev/pages/photo_upload.dart';
import 'package:zslDev/pages/platform.dart';
import 'package:zslDev/pages/platform_page.dart';
import 'package:zslDev/pages/state_manage.dart';

import 'ButtonPage.dart';
import 'GestureDetectorPage.dart';
import 'ImagePage.dart';
import 'IndexAndChosePage.dart';
import 'LayoutPage.dart';
import 'NavigatorPage.dart';
import 'PlatformPage.dart';
import 'ScaffoldPage.dart';
import 'StoragePage.dart';
import 'TextFieldPage.dart';
import 'TextPage.dart';
import 'ToastAndDialogPage.dart';
import 'custom_view_page.dart';
import 'custom_widget_page.dart';
import 'father.dart';
import 'key_demo.dart';
import 'myDate_page.dart';
import 'net_work_page..dart';

class MyDrawer extends StatefulWidget {
  const MyDrawer({Key? key, this.title}) : super(key: key);
  final String? title;
  @override
  State<StatefulWidget> createState() {
    return MyDrawerState();
  }
}

class MyDrawerState extends State<MyDrawer> {
  List toolList = [
    {
      "name": "图片上传",
      "route": PictureUpLoad(
        title: "图片上传",
      )
    },
    {
      "name": "日期",
      "route": MyDatePage(
        title: "日期",
      )
    },
    {
      "name": "父传子，调用方法",
      "route": FatherPageList(
        title: "父传子，调用方法",
      )
    },
    {
      "name": "Text组件",
      "route": TextPage(
        title: "Text组件",
      )
    },
    {
      "name": "TextField组件",
      "route": TextFieldPage(
        title: "TextField组件",
      )
    },
    {
      "name": "Image组件+涂鸦",
      "route": ImagePage(
        title: "Image组件+涂鸦",
      )
    },
    {
      "name": "layout布局",
      "route": LayoutPage(
        title: "layout布局",
      )
    },
    {
      "name": "GestureDetector",
      "route": GestureDetectorPage(
        title: "GestureDetector",
      )
    },
    {
      "name": "Navigator页面跳转",
      "route": NavigatorPage(
        title: "Navigator页面跳转",
      )
    },
    {
      "name": "ToastAndDialogPage",
      "route": ToastAndDialogPage(
        title: "ToastAndDialogPage",
      )
    },
    {
      "name": "Button 控件",
      "route": ButtonPage(
        title: "Button 控件",
      )
    },
    {
      "name": "IndexAndChose 控件",
      "route": IndexAndChosePage(
        title: "IndexAndChose 控件",
      )
    },
    {
      "name": "Scaffold 脚手架+头像裁剪",
      "route": ScaffoldPage(
        title: "Scaffold 脚手架",
      )
    },
    {
      "name": "交互原生",
      "route": PlatformPage(
        title: "交互原生",
      )
    },
    {
      "name": "数据存储",
      "route": StoragePage(
        title: "数据存储",
      )
    },
    {
      "name": "平台调用",
      "route": AllPlatformPage(
        title: "平台调用",
      )
    },
    {
      "name": "状态管理",
      "route": StateManage(
        title: "状态管理",
      )
    },
    {
      "name": "组件封装",
      "route": CustomWidgetPage(
        title: "组件封装",
      )
    },
    {
      "name": "Key Demo",
      "route": KeyDemo(
        title: "Key Demo",
      )
    },
    {
      "name": "网络请求",
      "route": NetWorkPage(
        title: "网络请求",
      )
    },
    {
      "name": "自定义View",
      "route": CustomViewPage(
        title: "自定义View",
      )
    },
    {
      "name": "点击调原生",
      "route": PlatFormDemo(
        title: "点击调原生",
      )
    }
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("${widget.title}"),
        centerTitle: true,
        leading: Container(),
      ),
      body: ListView.builder(
          itemCount: toolList.length,
          itemBuilder: (context, index) {
            return Container(
              margin: EdgeInsets.symmetric(horizontal: 12,vertical: 6),
              height: 50,
              child: RaisedButton(
                child: Text("${toolList[index]["name"]}"),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8),
                ),
                color: Colors.lightBlue,
                onPressed: () {
                  Navigator.pop(context);
                  Navigator.push(context, MaterialPageRoute(builder: (context) {
                    return toolList[index]["route"];
                  }));
                },
              ),
            );
          }),
    );
  }
}

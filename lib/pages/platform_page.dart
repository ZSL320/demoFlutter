import 'package:flutter/material.dart';

import 'MyRaisedButton.dart';
import 'android_platform_page.dart';
import 'calc_plugin_page.dart';

class AllPlatformPage extends StatelessWidget {
  const AllPlatformPage({Key? key, this.title}) : super(key: key);
  final String? title;
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text(
          "$title",
          style: TextStyle(color: Colors.white, fontSize: 16),
        ),
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: <Widget>[
          MyRaisedButton(AndroidPlatformPage(), "直接插件调用平台代码"),
          MyRaisedButton(CalcPluginPage(), "利用插件调用平台代码"),
        ],
      ),
    );
  }
}

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class PlatformPage extends StatelessWidget {
  static const platform = const MethodChannel("com.zhuandian.flutter/android");
  const PlatformPage({Key? key, this.title}) : super(key: key);
  final String? title;
  void _goAndroidPage() async {
    await platform.invokeMethod("new_page");
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        centerTitle: true,
        title: Text(
          "$title",
          style: TextStyle(color: Colors.white, fontSize: 16),
        ),
      ),
      body: new Center(
          child: new MaterialButton(
        onPressed: () {
          _goAndroidPage();
        },
        child: new Text("调到原生页面"),
      )),
    );
  }
}

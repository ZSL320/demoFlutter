import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class PlatFormDemo extends StatelessWidget {
  const PlatFormDemo({Key? key, this.title}) : super(key: key);
  final String? title;
  static const platform = const MethodChannel("com.zhuandian.flutter/android");
  showToast(String msg) async {
    try {
      await platform.invokeMethod("showToast", {"msg": msg});
    } on PlatformException catch (e) {
      print(e.toString());
    }
  }

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
        children: [
          Container(
            margin: EdgeInsets.symmetric(vertical: 60),
            child: RaisedButton(
              child: Text("点我提示"),
              onPressed: () {
                showToast("我是android系统的toast");
              },
            ),
          ),
          Container(
            height: 200,
            child: AndroidView(
              viewType: 'widget.name',
              creationParams: {'text': 'Flutter传给Android的参数'},
              creationParamsCodec: StandardMessageCodec(),
            ),
          )
        ],
      ),
    );
  }
}

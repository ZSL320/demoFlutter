import 'package:flutter/material.dart';
import 'package:zslDev/pages/redux_first_page.dart';
import 'MyRaisedButton.dart';
import 'bloc_state_page.dart';
import 'call_back_page.dart';


class StateManage extends StatelessWidget {
  const StateManage({Key? key, this.title}) : super(key: key);
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
          MyRaisedButton( CallBackPage(), "方法回调"),
          MyRaisedButton( ReduxFirstPage(), "FlutterRedux"),
          MyRaisedButton( BLoCStatePage(), "BLoC状态管理"),
        ],
      ),
    );
  }
}

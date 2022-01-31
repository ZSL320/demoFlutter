import 'package:flutter/material.dart';

import 'FileStorage.dart';
import 'MyRaisedButton.dart';
import 'SharedPerferenceStorage.dart';
import 'SqfliteStorage.dart';

class StoragePage extends StatelessWidget {
  const StoragePage({Key? key, this.title}) : super(key: key);
  final String? title;
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
        child: new Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            MyRaisedButton(new FileStorage(), "文件存储"),
            MyRaisedButton(
                new SharedPerferenceStorage(), "shared_preferences存储"),
            MyRaisedButton(new SqfliteStorage(), "Sqflite数据库存储"),
          ],
        ),
      ),
    );
  }
}

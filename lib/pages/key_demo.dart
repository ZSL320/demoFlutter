import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'box.dart';

/**
 * desc:
 * author: xiedong
 * date: 2021/11/30
 **/
class KeyDemo extends StatelessWidget {
  const KeyDemo({Key? key, this.title}) : super(key: key);
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
      body: Column(children: [
        Box(Colors.red, key: ValueKey(1)),
        Box(Colors.black, key: ValueKey(2)),
      ]),
    );
  }
}

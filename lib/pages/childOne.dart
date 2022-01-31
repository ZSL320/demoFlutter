import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'inheritedWidget.dart';

class ChildOne extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(child: Text("${MyInheritedWidget.of(context)!.data}"));
  }
}

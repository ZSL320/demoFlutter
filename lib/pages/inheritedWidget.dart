import 'package:flutter/cupertino.dart';

class MyInheritedWidget extends InheritedWidget {
  final int? data;
  final Widget child;
  MyInheritedWidget({Key? key, this.data, required this.child})
      : super(child: child);

  //类方法
  static MyInheritedWidget? of(BuildContext context) {
    return context.dependOnInheritedWidgetOfExactType<MyInheritedWidget>();
  }

  //示例方法
  @override
  bool updateShouldNotify(MyInheritedWidget oldWidget) {
    return data != oldWidget.data;
  }
}

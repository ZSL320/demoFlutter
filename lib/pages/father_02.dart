import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'childThree.dart';

class FatherTwo extends StatefulWidget {
  const FatherTwo({Key? key, this.title}) : super(key: key);
  final String? title;
  @override
  State<StatefulWidget> createState() {
    return FatherTwoState();
  }
}

class FatherTwoState extends State<FatherTwo> {
  int count = 0;
  ValueNotifier<int> _notifier = ValueNotifier<int>(0);
  @override
  void initState() {
    super.initState();
  }

  @override
  void dispose() {
    _notifier.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          "${widget.title}",
          style: TextStyle(color: Colors.white, fontSize: 16),
        ),
        centerTitle: true,
      ),
      body: Column(
        children: [
          Container(
            margin: EdgeInsets.symmetric(horizontal: 122, vertical: 22),
            child: RaisedButton(
              color: Colors.blue,
              child: Text("我是父组件"),
              onPressed: () {
                count++;
                _notifier.value = count;
              },
              shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(6)),
            ),
          ),
          SizedBox(
            height: 22,
          ),
          Container(
            height: 100,
            child: ChildThree(
              notifier: _notifier,
            ),
          ),
          Container(
            child: ValueListenableBuilder<int>(
              valueListenable: _notifier,
              builder: (context, int value, Widget? child) {
                return Container(
                  child: Text("$value"),
                );
              },
            ),
          )
        ],
      ),
    );
  }
}

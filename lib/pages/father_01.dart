import 'dart:async';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'childOne.dart';
import 'childTwo.dart';
import 'inheritedWidget.dart';

class FatherOne extends StatefulWidget {
  const FatherOne({Key? key, this.title}) : super(key: key);
  final String? title;
  @override
  State<StatefulWidget> createState() {
    return FatherOneState();
  }
}

class FatherOneState extends State<FatherOne> {
  int count = 0;

  StreamController<int> _controller = new StreamController<int>();
  @override
  void initState() {
    super.initState();
  }

  @override
  void dispose() {
    _controller.close();
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
                _controller.sink.add(count);
              },
              shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(6)),
            ),
          ),
          Container(
            child: StreamBuilder<int>(
                initialData: 0,
                stream: _controller.stream,
                builder: (context, AsyncSnapshot<int> snapshot) {
                  return MyInheritedWidget(
                    data: snapshot.data,
                    child: Container(
                      child: Column(
                        children: [
                          Container(
                            margin: EdgeInsets.symmetric(vertical: 22),
                            child: Text("childOne:"),
                          ),
                          ChildOne(),
                          Container(
                            margin: EdgeInsets.symmetric(vertical: 22),
                            child: Text("childTwo:"),
                          ),
                          ChildTwo()
                        ],
                      ),
                    ),
                  );
                }),
          ),
        ],
      ),
    );
  }
}

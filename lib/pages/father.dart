import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'father_01.dart';
import 'father_02.dart';

class FatherPageList extends StatefulWidget {
  final String? title;
  const FatherPageList({
    Key? key,
    this.title,
  }) : super(key: key);
  @override
  State<StatefulWidget> createState() {
    return FatherPageListState();
  }
}

class FatherPageListState extends State<FatherPageList> {
  int count = 0;
  List childrenList = [
    {
      "name": "数据共享InheritedWidget",
      "router": FatherOne(
        title: "数据共享InheritedWidget",
      ),
    },
    {
      "name": "ValueNotifier",
      "router": FatherTwo(
        title: "ValueNotifier",
      ),
    },
  ];
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          centerTitle: true,
          title: Text(
            "${widget.title}",
            style: TextStyle(color: Colors.white, fontSize: 16),
          ),
        ),
        body: Container(
          margin: EdgeInsets.symmetric(horizontal: 50, vertical: 29),
          child: childrenWidget(),
        ));
  }

  childrenWidget<Widget>() {
    return ListView.builder(
        itemCount: childrenList.length,
        physics: BouncingScrollPhysics(),
        itemBuilder: (context, index) {
          return Container(
            height: 39,
            width: 95,
            margin: EdgeInsets.symmetric(vertical: 6),
            child: RaisedButton(
              color: Colors.blue,
              child: Text("${childrenList[index]["name"]}"),
              onPressed: () {
                Navigator.push(context, MaterialPageRoute(builder: (context) {
                  return childrenList[index]["router"];
                }));
              },
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(6),
              ),
            ),
          );
        });
  }
}

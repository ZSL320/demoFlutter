import 'package:flutter/material.dart';

import 'AlignLayoutPage.dart';
import 'AspecRadioLayoutPage.dart';
import 'CenterPage.dart';
import 'ColunmLayoutPage.dart';
import 'ListViewPage.dart';
import 'OpacityPage.dart';
import 'PaddingLayoutPage.dart';
import 'SizedBoxPage.dart';
import 'StackLayoutPage.dart';

class LayoutPage extends StatelessWidget {
  const LayoutPage({Key? key, this.title}) : super(key: key);
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
        children: <Widget>[
          Text(
            "各种Layout布局",
            style:
                new TextStyle(fontSize: 30.0, color: Colors.deepOrangeAccent),
          ),
          new SizedBox(
            height: 1.0,
            child: new Container(
              color: Colors.blue,
            ),
          ),
          new FlatButton(
              onPressed: () {
                Navigator.push(
                    context,
                    new MaterialPageRoute(
                        builder: (context) => new AlignLayoutPage()));
              },
              child: new Text("Align布局")),
          new FlatButton(
              onPressed: () {
                Navigator.push(
                    context,
                    new MaterialPageRoute(
                        builder: (context) => new CenterLayoutPage()));
              },
              child: new Text("Center布局")),
          new FlatButton(
              onPressed: () {
                Navigator.push(
                    context,
                    new MaterialPageRoute(
                        builder: (context) => new ColunmLayoutPage()));
              },
              child: new Text("ColunmLayout布局")),
          new FlatButton(
              onPressed: () {
                Navigator.push(
                    context,
                    new MaterialPageRoute(
                        builder: (context) => new ListViewLayoutPage()));
              },
              child: new Text("ListView布局")),
          new FlatButton(
              onPressed: () {
                Navigator.push(
                    context,
                    new MaterialPageRoute(
                        builder: (context) => new OpacityLayoutDemo()));
              },
              child: new Text("Opacity布局")),
          new FlatButton(
              onPressed: () {
                Navigator.push(
                    context,
                    new MaterialPageRoute(
                        builder: (context) => new SizedBoxPage()));
              },
              child: new Text("SizedBox布局")),
          new FlatButton(
              onPressed: () {
                Navigator.push(
                    context,
                    new MaterialPageRoute(
                        builder: (context) => new StackLayoutPage()));
              },
              child: new Text("StackLayout布局")),
          new FlatButton(
              onPressed: () {
                Navigator.push(
                    context,
                    new MaterialPageRoute(
                        builder: (context) => new PaddingLayoutPage()));
              },
              child: new Text("PaddingLayout布局")),
          new FlatButton(
              onPressed: () {
                Navigator.push(
                    context,
                    new MaterialPageRoute(
                        builder: (context) => new AspectRadioLayoutPage()));
              },
              child: new Text("AspecRadioLayoutPage布局"))
        ],
      )),
    );
  }
}

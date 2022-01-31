import 'package:flutter/material.dart';

import 'LabelAlignment.dart';
import 'common_label_view.dart';
import 'common_label_view_with_angle.dart';
import 'label_view_bottom_left.dart';
import 'label_view_bottom_right.dart';
import 'label_view_top_left.dart';
import 'label_view_top_right.dart';

class CustomViewPage extends StatelessWidget {
  const CustomViewPage({Key? key, this.title}) : super(key: key);
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
          children: <Widget>[
            CustomPaint(
              painter: LabelViewTopLeft(),
            ),
            CustomPaint(
              painter: LabelViewTopRight(),
            ),
            CustomPaint(
              painter: LabelViewBottomLeft(),
            ),
            CustomPaint(
              painter: LabelViewBottomRight(),
            ),
//            CommonLabelView(size: Size(100, 400),labelColor: Colors.red,labelAlignment: LabelAlignment.bottomLeft,)
//            CommonLabelView(size: Size(100, 400),labelColor: Colors.red,labelAlignment: LabelAlignment.bottomLeft,)
            CommonLabelView(
              size: Size(100, 200),
              labelColor: Colors.red,
              labelAlignment: LabelAlignment.bottomRight,
            ),
            CommonLabelView(
              size: Size(300, 50),
              labelColor: Colors.pinkAccent,
              labelAlignment: LabelAlignment.bottomRight,
            ),

            //label 顶角挖空效果
            CommonLabelViewWithAngle(
              size: Size(300, 50),
              labelColor: Colors.red,
              labelAlignment: LabelAlignment.bottomRight,
              withAngle: true,
            ),
            CommonLabelViewWithAngle(
              size: Size(300, 50),
              labelColor: Colors.yellowAccent,
              labelAlignment: LabelAlignment.topLeft,
              withAngle: true,
            ),
            CommonLabelViewWithAngle(
              size: Size(300, 50),
              labelColor: Colors.red,
              labelAlignment: LabelAlignment.topRight,
              withAngle: true,
            ),
            SizedBox(height: 20),
            CommonLabelViewWithAngle(
              size: Size(300, 170),
              labelColor: Colors.blueAccent,
              labelAlignment: LabelAlignment.bottomLeft,
              withAngle: true,
            ),
          ],
        ));
  }
}

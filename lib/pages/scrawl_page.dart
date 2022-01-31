import 'dart:typed_data';
import 'dart:ui' as ui;
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:zslDev/pages/scrawl_painter.dart';


class ScrawlPage extends StatefulWidget {
  final String? imageData;
  final ValueChanged<List<int>>? imageDataCall;
  const ScrawlPage({this.imageData, this.imageDataCall});
  @override
  State<StatefulWidget> createState() => _ScrawlState();
}

class _ScrawlState extends State<ScrawlPage> {
  static final List<Color> colors = [
    Colors.redAccent,
    Colors.lightBlueAccent,
    Colors.greenAccent,
  ];
  double selectedLine = 2.0;
  List colorList = [
    [Colors.redAccent, true],
    [Colors.yellow, false],
    [Colors.orange, false],
    [Colors.blue, false],
    [Colors.green, false],
    [Colors.lightBlueAccent, false],
    [Colors.greenAccent, false],
    [Colors.grey, false],
    [Colors.black, false],
  ];
  Color? selectedColor;
  List<Point> points = [Point(colors[0], 2, [])];
  int curFrame = 0;
  bool isClear = false;
  List<Point> deletePoint = [];
  final GlobalKey _repaintKey = new GlobalKey();

  @override
  void initState() {
    super.initState();
    selectedColor = Colors.redAccent;
  }

  bool isDraw = true;
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: Stack(
        alignment: Alignment.center,
        children: <Widget>[
          Container(
            color: Colors.white,
            alignment: Alignment.center,
            child: RepaintBoundary(
              child: Stack(
                alignment: Alignment.center,
                children: <Widget>[
                  Container(
                    key: _repaintKey,
                    child: Image.network(widget.imageData!),
                  ),
                  Positioned(
                    child: _buildCanvas(),
                    top: 0.0,
                    bottom: 0.0,
                    left: 0.0,
                    right: 0.0,
                  ),
                ],
              ),
            ),
          ),
          Visibility(
            visible: isDraw && isOnTap,
            child: Positioned(
              bottom: 52,
              left: 12,
              right: 12,
              child: _buildBottomTop(),
            ),
          ),
          Visibility(
            visible: isDraw,
            child: Positioned(
              bottom: 12,
              left: 12,
              right: 12,
              child: _buildBottom(),
            ),
          )
        ],
      ),
    );
  }

  bool isOnTap = false;
  Widget _buildCanvas() {
    return StatefulBuilder(builder: (context, state) {
      return CustomPaint(
        painter: ScrawlPainter(
          points: points,
          strokeColor: selectedColor,
          strokeWidth: selectedLine,
          isClear: isClear,
        ),
        child: GestureDetector(
          onPanStart: (details) {
            if (isOnTap) {
              isClear = false;
              points[curFrame].color = selectedColor!;
              points[curFrame].strokeWidth = selectedLine;
              setState(() {
                isDraw = false;
              });
            }
          },
          onPanUpdate: (details) {
            if (isOnTap) {
              RenderBox? referenceBox =
                  context.findRenderObject()! as RenderBox?;
              Offset localPosition =
                  referenceBox!.globalToLocal(details.globalPosition);
              state(() {
                points[curFrame].points.add(localPosition);
              });
              setState(() {
                isDraw = false;
              });
            }
          },
          onPanEnd: (details) {
            if (isOnTap) {
              points.add(Point(selectedColor!, selectedLine, []));
              curFrame++;
              setState(() {
                isDraw = true;
              });
            }
          },
        ),
      );
    });
  }

  double valued = 2;
  Widget _buildBottomTop() {
    return StatefulBuilder(builder: (context, state) {
      return Container(
        color: Color(00),
        margin: EdgeInsets.symmetric(horizontal: 20),
        child: Container(
            child: Column(
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: colorList.map((e) {
                return GestureDetector(
                  child: Stack(
                    alignment: Alignment.center,
                    children: [
                      Container(
                        width: 22,
                        height: 22,
                        decoration: BoxDecoration(
                          borderRadius: BorderRadius.circular(11),
                          color: e[1] ? Colors.grey.withOpacity(0.5) : null,
                        ),
                      ),
                      Container(
                        width: 15,
                        height: 15,
                        decoration: BoxDecoration(
                          borderRadius: BorderRadius.circular(7.5),
                          color: e[0],
                        ),
                      ),
                    ],
                  ),
                  onTap: () {
                    state(() {
                      selectedColor = e[0];
                      for (var item in colorList) {
                        item[1] = false;
                      }
                      e[1] = !e[1];
                    });
                  },
                );
              }).toList(),
            ),
            Container(
              child: Slider(
                max: 10,
                min: 1,
                onChanged: (double value) {
                  state(() {
                    valued = value;
                    selectedLine = valued;
                  });
                },
                value: this.valued,
              ),
            ),
          ],
        )),
      );
    });
  }

  Widget _buildBottom() {
    return Container(
      color: Color(00),
      margin: EdgeInsets.symmetric(horizontal: 15),
      child: StatefulBuilder(builder: (context, state) {
        return Column(
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: <Widget>[
                GestureDetector(
                  child: Container(
                    color: isOnTap
                        ? Colors.grey.withOpacity(0.2)
                        : Colors.transparent,
                    child: Icon(
                      Icons.create,
                      color: colors[0],
                    ),
                  ),
                  onTap: () {
                    state(() {});
                    setState(() {
                      isOnTap = !isOnTap;
                    });
                  },
                ),
                GestureDetector(
                  child: Icon(
                    Icons.rotate_left,
                    size: 28,
                    color: Colors.blue,
                  ),
                  onTap: () {
                    setState(() {
                      if (points.length >= 2) {
                        --curFrame;
                        deletePoint.add(points[points.length - 2]);
                        points.removeAt(points.length - 2);
                      }
                    });
                  },
                ),
                GestureDetector(
                  child: Icon(
                    Icons.rotate_right,
                    size: 28,
                    color: Colors.blue,
                  ),
                  onTap: () {
                    setState(() {
                      if (deletePoint.length >= 1) {
                        ++curFrame;
                        deletePoint.add(deletePoint[0]);
                        points.insert(0, deletePoint[0]);
                        deletePoint.removeAt(0);
                      }
                    });
                  },
                ),
                GestureDetector(
                  child: Container(
                    width: 50,
                    height: 29,
                    alignment: Alignment.center,
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(6),
                      color: Colors.blue,
                    ),
                    child: Text(
                      '清除',
                      style: TextStyle(fontSize: 15, color: Colors.white),
                    ),
                  ),
                  onTap: () {
                    setState(() {
                      reset();
                    });
                  },
                ),
                GestureDetector(
                  child: Container(
                    width: 80,
                    height: 29,
                    alignment: Alignment.center,
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(6),
                      color: Colors.blue,
                    ),
                    child: Text(
                      '确认发送',
                      style: TextStyle(fontSize: 15, color: Colors.white),
                    ),
                  ),
                  onTap: () async {
                    RenderRepaintBoundary? boundary = _repaintKey
                        .currentContext!
                        .findAncestorRenderObjectOfType();
                    ui.Image _image = await boundary!.toImage();
                    ByteData? byteData =
                        await _image.toByteData(format: ui.ImageByteFormat.png);
                    Uint8List pngBytes = byteData!.buffer.asUint8List();
                    widget.imageDataCall!(pngBytes);
                  },
                ),
              ],
            )
          ],
        );
      }),
    );
  }

  void reset() {
    isClear = true;
    curFrame = 0;
    points.clear();
    points.add(Point(selectedColor!, selectedLine, []));
  }
}

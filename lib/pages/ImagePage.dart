import 'package:flutter/material.dart';

import 'full_screen_image.dart';

class ImagePage extends StatelessWidget {
  const ImagePage({
    Key? key,
    this.title,
  }) : super(key: key);
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
        body: ListView(
          children: <Widget>[
            new Text("从网络加载图片"),
            GestureDetector(
              child: Image.network(
                "https://p1.ssl.qhmsg.com/dr/220__/t01d5ccfbf9d4500c75.jpg",
                width: 500,
                height: 500,
              ),
              onTap: () {
                Navigator.push(context, MaterialPageRoute(builder: (context) {
                  return FullScreenImage(
                    imgList: [
                      "https://p1.ssl.qhmsg.com/dr/220__/t01d5ccfbf9d4500c75.jpg"
                    ],
                    imgIndex: 0,
                  );
                }));
              },
            ),
            new Text('从本地加载图片'),
            new Image.asset(
              'images/aaa.png',
              width: 500,
              height: 500,
            )
          ],
        ));
  }
}

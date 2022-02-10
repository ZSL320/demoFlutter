import 'dart:async';
import 'dart:ffi';
import 'dart:typed_data';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'dart:io';

import 'package:image_picker/image_picker.dart';

class MyChatPage extends StatefulWidget {
  const MyChatPage({Key? key, this.title, this.streamController})
      : super(key: key);
  final String? title;
  final StreamController<String>? streamController;
  @override
  _MyChatPageState createState() => _MyChatPageState();
}

class _MyChatPageState extends State<MyChatPage>
    with AutomaticKeepAliveClientMixin {
  String ?imgPath;
  @override
  void initState() {
    super.initState();

    widget.streamController!.stream.listen((event) {
      imgPath=event;
      print("Stream");
      print(event);
      print("Stream");
    });
  }


  @override
  bool get wantKeepAlive => true;

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: [
          imgPath!=null? Container(
            margin: EdgeInsets.only(top: 30),
            alignment: Alignment.center,
            child: Image.file(File(imgPath!)),
          ):Container(),
        ],
      ),
    );
  }
}

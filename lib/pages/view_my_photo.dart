import 'dart:io';
import 'package:flutter/material.dart';
// import 'package:fluttertoast/fluttertoast.dart';
import 'package:image_crop/image_crop.dart';
import 'package:image_picker/image_picker.dart';

class CropImageRoute extends StatefulWidget {
  final ValueChanged<File>? myPhoto;
  final XFile ?image; //原始图片路径
  const CropImageRoute({this.image, this.myPhoto});
  @override
  _CropImageRouteState createState() => new _CropImageRouteState();
}

class _CropImageRouteState extends State<CropImageRoute> {
  double ?baseLeft; //图片左上角的x坐标
  double ?baseTop; //图片左上角的y坐标
  double ?imageWidth; //图片宽度，缩放后会变化
  double? imageScale = 1; //图片缩放比例
  Image ?imageView;
  final cropKey = GlobalKey<CropState>();

  @override
  Widget build(BuildContext context) {
    var screen = MediaQuery.of(context).size;
    return Scaffold(
      backgroundColor: Colors.black,
      appBar: AppBar(
        elevation: 0,
        leading: IconButton(
          icon: Icon(
            Icons.arrow_back_ios,
            size: 20,
            color: Colors.black,
          ),
          onPressed: () {
            Navigator.pop(context);
          },
        ),
        automaticallyImplyLeading: true,
        toolbarHeight: 52,
        centerTitle: true,
        backgroundColor: Colors.white,
        title: Text(
          "移动和缩放",
          style: TextStyle(color: Colors.black, fontSize: 18),
        ),
      ),
      body: Container(
        color: Colors.black,
        child: Stack(
          children: <Widget>[
            Container(
              height: screen.height * 0.8,
              child: Crop(
                key: cropKey,
                aspectRatio: 4.0 / 3.0,
                alwaysShowGrid: true,
                image: FileImage(File(widget.image!.path)),
              ),
            ),
          ],
        ),
      ),
      floatingActionButton: Container(
        margin: EdgeInsets.only(bottom: 15,left: 12,right: 12),
        child: Container(
          width: double.infinity,
          height: 45,
          child: RaisedButton(
            color: Colors.blue,
            onPressed: () {
              _crop(
                File(widget.image!.path),
              );
            },
            child: Text(
              '确定',
              style: TextStyle(
                  fontSize: 18,
                  fontWeight: FontWeight.w600,
                  color: Colors.white),
            ),
          ),
        ),
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,
    );
  }

  _crop(File originalFile) {
    final crop = cropKey.currentState;
    final area = crop!.area;
    if (area == null) {
      print('裁剪失败');
    }
    ImageCrop.requestPermissions().then((value) {
      if (value) {
        ImageCrop.cropImage(
          file: originalFile,
          area: crop.area!,
        ).then((value) {
          Navigator.pop(context);
          widget.myPhoto!(value);
        }).catchError((e) {
           // Fluttertoast.showToast(msg: "裁剪失败");
        });
      } else {}
    });
  }
}

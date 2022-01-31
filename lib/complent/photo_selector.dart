import 'dart:typed_data';
import 'package:animations/animations.dart';
import 'package:flutter/material.dart';
import 'package:flutter_image_compress/flutter_image_compress.dart';
import 'package:image_picker/image_picker.dart';
import 'package:multi_image_picker/multi_image_picker.dart';

import 'color_config.dart';
import 'comment_photo_view.dart';

typedef void PhotoCallBack();

class PhotoSelector {
  PhotoSelector(
    this.context, {
    this.count = 6,
    this.isEdit = false,
    this.onAssetInt,
    this.size = 80,
    this.photoCallBack,
    this.urlImgListLength,
  });

  BuildContext context;
  int count;
  bool isEdit;
  double size;
  PhotoCallBack ?photoCallBack;
  int ?urlImgListLength;
  ValueChanged<List<List<int>>>? onAssetInt;
  // 照片墙 - 整体
  Widget buildPhotoView() {
    return Container(
      margin: const EdgeInsets.symmetric(vertical: 10),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisSize: MainAxisSize.min,
        children: [
          buildSelectorPhoto(),
        ],
      ),
    );
  }

  // 照片墙 - 选择照片部分
  Widget buildSelectorPhoto() {
    final width = MediaQuery.of(context).size.width;
    final height = MediaQuery.of(context).size.height;
    List<Widget> photoWidgetList = [];
    if (imageData.isNotEmpty) {
      imageData.forEach((imageAsset) {
        photoWidgetList.add(
          Stack(
            children: [
              OpenContainer(
                closedBuilder: (ctx, _) {
                  return Container(
                    width: width / 2.6,
                    height: height / 6,
                    decoration: BoxDecoration(
                      border: Border.all(width: 0.5, color: ColorTheme_Gray),
                    ),
                    child: Image.memory(
                      Uint8List.fromList(imageAsset),
                      fit: BoxFit.cover,
                    ),
                  );
                },
                openBuilder: (ctx, _) {
                  return FullPhotoViewScreenImage(
                    allImgList: imageData,
                    imgIndex: imageData.indexOf(imageAsset),
                    deletePhoto: () {
                      photoCallBack!();
                    },
                  );
                },
              ),
              Positioned(
                right: 0,
                child: GestureDetector(
                  onTap: () {
                    _removerImage(imageAsset);
                  },
                  child: Container(
                    decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(10),
                        color: Colors.black26,
                        border: Border.all(width: 0)),
                    padding: const EdgeInsets.all(2),
                    child: Icon(Icons.clear, color: Colors.white, size: 20),
                  ),
                ),
              ),
            ],
          ),
        );
      });
    }

    if (imageData.length < count) {
      photoWidgetList.add(
        Container(
          width: width / 2.6,
          height: height / 6,
          alignment: Alignment.center,
          decoration: BoxDecoration(
            color: Color(0xFFE9E9E9),
            borderRadius: BorderRadius.circular(6),
          ),
          child: Container(
            child: Stack(
              children: [
                Center(
                  child: IconButton(
                      icon: Icon(Icons.add, color: Color(0xFFC3C3C3)),
                      onPressed: () => _onSelectPhoto(context)),
                ),
                Center(
                  child: Container(
                    margin: EdgeInsets.only(top: 39),
                    child: Text(
                      "点击上传",
                      style: TextStyle(fontSize: 12, color: Color(0xFFAFAFAF)),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      );
    }
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 15, vertical: 10),
      width: double.infinity,
      color: Colors.white,
      child: Wrap(spacing: 5, runSpacing: 5, children: photoWidgetList),
    );
  }

  // 选择照片
  _onSelectPhoto(BuildContext context) {
    showModalBottomSheet(
      context: context,
      builder: (BuildContext context) {
        return Container(
          padding: const EdgeInsets.all(10.0),
          decoration: BoxDecoration(
            color: Colors.white,
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: <Widget>[
              Container(
                width: double.infinity,
                height: 40.0,
                child: TextButton(
                  onPressed: () => _openCamera(context),
                  style: ButtonStyle(
                    backgroundColor: MaterialStateProperty.all(ColorTheme),
                  ),
                  child: Text("拍照", style: TextStyle(color: Colors.white)),
                ),
              ),
              Container(
                width: double.infinity,
                margin: const EdgeInsets.only(top: 5.0),
                height: 40.0,
                child: TextButton(
                  onPressed: () => _openAllGallery(context),
                  style: ButtonStyle(
                    backgroundColor: MaterialStateProperty.all(ColorTheme),
                  ),
                  child: Text("从相册选择", style: TextStyle(color: Colors.white)),
                ),
              ),
              Container(
                width: double.infinity,
                margin: const EdgeInsets.only(top: 15.0),
                height: 40.0,
                child: TextButton(
                  onPressed: () => Navigator.pop(context),
                  style: ButtonStyle(
                    backgroundColor: MaterialStateProperty.all(ColorText_B4),
                  ),
                  child: Text("取消", style: TextStyle(color: Colors.white)),
                ),
              ),
            ],
          ),
        );
      },
    );
  }

  List<List<int>> imageData = [];
  // 拍照
  _openCamera(BuildContext context) async {
    Navigator.pop(context);
    var image = await ImagePicker().pickImage(source: ImageSource.camera);
    if (image != null) {
      List<List<int>> cameraImageData = [];
      var result = await FlutterImageCompress.compressWithFile(
        image.path,
        quality: 20,
      );
      cameraImageData.add(result!);
      imageData.addAll(cameraImageData);
      onAssetInt!(imageData);
    }
  }

  ///多图相册
  _openAllGallery(context) async {
    Navigator.pop(context);
    var resultList = await MultiImagePicker.pickImages(
      // 选择图片的最大数量
      maxImages: 20 - imageData.length - urlImgListLength!,
      // 是否支持拍照
      enableCamera: true,
      materialOptions: MaterialOptions(
          // 显示所有照片，值为 false 时显示相册
          startInAllView: true,
          allViewTitle: '所有照片',
          actionBarColor: '#2196F3',
          textOnNothingSelected: '没有选择照片'),
    );
    List<Asset> photoList = [];
    if (resultList.isNotEmpty) {
      photoList.addAll(resultList);
      List<List<int>> photoImageData = [];
      for (var item in photoList) {
        ByteData byteData = await item.getByteData();
        photoImageData.add(byteData.buffer.asUint8List());
      }
      imageData.addAll(photoImageData);
      onAssetInt!(imageData);
    }
  }

  // 删除照片
  _removerImage(image) {
    imageData.remove(image);
    onAssetInt!(imageData);
  }
}

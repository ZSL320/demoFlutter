import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:zslDev/complent/color_config.dart';
import 'package:zslDev/pages/view_my_photo.dart';

typedef void ImgCallBack(String path);
typedef void BackEdit(bool value);

class ImageSelectorDialog extends StatelessWidget {
  final ImgCallBack ?imgCallBack;
  final BackEdit ? isEdit;
  ImageSelectorDialog({Key? key, this.imgCallBack, this.isEdit})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return _buildWidget(context);
  }

  _buildWidget(BuildContext context) {
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
              SizedBox(
                width: double.infinity,
                height: 40.0,
                child: TextButton(
                  onPressed: () => _openCamera(context),
                  style: ButtonStyle(
                    backgroundColor: MaterialStateProperty.all(ColorTheme),
                  ),
                  child: const Text("拍照", style: TextStyle(color: Colors.white)),
                ),
              ),
              Container(
                width: double.infinity,
                margin: const EdgeInsets.only(top: 5.0),
                height: 40.0,
                child: TextButton(
                  onPressed: () => _openGallery(context),
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

  // 拍照
  _openCamera(BuildContext context) async {
    var image = await ImagePicker().pickImage(source: ImageSource.camera);
    if (image != null) {
      Navigator.push(context, MaterialPageRoute(builder: (context) {
        return CropImageRoute(
          image: image,
          myPhoto: (file) {
            _myPhoto = file.path;
          },
        );
      })).then((value) {
        if (_myPhoto != null) {
          Navigator.pop(context);
          _updateImageFileList(XFile(_myPhoto!));
        } else {
          Navigator.pop(context);
        }
      });
    } else {
      Navigator.pop(context);
    }
  }

  String ?_myPhoto;
  // 相册
  _openGallery(BuildContext context) async {
    XFile? image = await ImagePicker().pickImage(source: ImageSource.gallery);
    if (image != null) {
      Navigator.push(context, MaterialPageRoute(builder: (context) {
        return CropImageRoute(
          image: image,
          myPhoto: (file) {
            _myPhoto = file.path;
          },
        );
      })).then((value) {
        if (_myPhoto != null) {
          Navigator.pop(context);
          _updateImageFileList(XFile(_myPhoto!));
        } else {
          Navigator.pop(context);
        }
      });
    } else {
      Navigator.pop(context);
    }
  }

  // 更新照片
  _updateImageFileList(XFile ?image) async {
    if (image != null) {
      if (imgCallBack != null) {
        imgCallBack!(image.path);
      }
      isEdit!(true);
    }
  }
}

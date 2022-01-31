import 'dart:typed_data';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:loading_indicator/loading_indicator.dart';
import 'package:multi_image_picker/multi_image_picker.dart';
import 'package:photo_view/photo_view.dart';

typedef void DeletePhoto();

class FullPhotoViewScreenImage extends StatefulWidget {
  final int? imgIndex;
  final List<List<int>>? allImgList;
  final DeletePhoto? deletePhoto;
  const FullPhotoViewScreenImage(
      {this.deletePhoto, this.allImgList, this.imgIndex});

  @override
  _FullPhotoViewScreenImageState createState() =>
      _FullPhotoViewScreenImageState();
}

class _FullPhotoViewScreenImageState extends State<FullPhotoViewScreenImage> {
  List<List<int>> imageDataList = [];
  PageController _photoController = new PageController();
  int currentSelectIndex = 0;
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    currentSelectIndex = widget.imgIndex!;
    _photoController = new PageController(
      initialPage: currentSelectIndex,
      keepPage: true,
    );
    setState(() {
      imageDataList.addAll(widget.allImgList!);
    });
  }

  @override
  void dispose() {
    _photoController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black,
      appBar: AppBar(
        backgroundColor: Color(0xF1F2F6FF),
        elevation: 0,
        actions: [
          Container(
            margin: EdgeInsets.only(top: 8),
            child: IconButton(
              icon: Icon(
                Icons.delete_outline,
                size: 25,
                color: Colors.black,
              ),
              onPressed: () {
                widget.deletePhoto!();
                setState(() {
                  widget.allImgList!.removeAt(currentSelectIndex);
                  imageDataList.removeAt(currentSelectIndex);
                });
                if (currentSelectIndex > 0) {
                  currentSelectIndex--;
                } else {
                  currentSelectIndex = 0;
                }
                if (imageDataList.length == 0) {
                  Navigator.pop(context);
                }
              },
            ),
          ),
        ],
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
        title: Text(
          "${currentSelectIndex + 1}/${widget.allImgList!.length}",
          style: TextStyle(color: Colors.black, fontSize: 18),
        ),
      ),
      body: buildPageView(),
    );
  }

  PageView buildPageView() {
    return PageView.builder(
      physics:BouncingScrollPhysics(),
      controller: _photoController,
      onPageChanged: (index) {
        setState(() {
          currentSelectIndex = index;
        });
      },
      itemBuilder: (BuildContext context, int index) {
        return buildItemWidget(currentSelectIndex);
      },
      itemCount: widget.allImgList!.length,
    );
  }

  buildItemWidget(int index) {
    return Stack(
      children: [
        GestureDetector(
          child: PhotoView(
            imageProvider: MemoryImage(
              Uint8List.fromList(imageDataList[index]),
              scale: 1.0,
            ),
            basePosition: Alignment.center,
            minScale: PhotoViewComputedScale.contained,
            maxScale: 3.0,
            loadingBuilder: (context, _) {
              return Container(
                  alignment: Alignment.center,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Container(
                        height: 30,
                        width: 30,
                        child: LoadingIndicator(
                          color: Colors.grey,
                          indicatorType: Indicator.lineSpinFadeLoader,
                        ),
                      ),
                      Container(
                        padding: EdgeInsets.only(top: 6),
                        child: Text(
                          "加载中",
                          style: TextStyle(color: Colors.white, fontSize: 12),
                        ),
                      ),
                    ],
                  ));
            },
          ),
          onTap: () => Navigator.pop(context),
        )
      ],
    );
  }
}

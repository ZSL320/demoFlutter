import 'package:flutter/material.dart';
import 'package:loading_indicator/loading_indicator.dart';
import 'package:photo_view/photo_view.dart';
import 'dart:math' as math;

import 'package:zslDev/pages/scrawl_page.dart';

class FullScreenImage extends StatefulWidget {
  final List? imgList;
  final int? imgIndex;
  final ValueChanged<List<int>>? imageDataCall;
  const FullScreenImage({
    this.imgList,
    this.imgIndex,
    this.imageDataCall,
  });

  @override
  _FullScreenImageState createState() => _FullScreenImageState();
}

class _FullScreenImageState extends State<FullScreenImage> {
  PageController _photoController = new PageController();
  int currentSelectIndex = 0;
  @override
  void initState() {
    // TODO: implement initState
    setState(() {});
    super.initState();
    currentSelectIndex = widget.imgIndex!;
    _photoController = new PageController(
      initialPage: currentSelectIndex,
      keepPage: true,
    );
  }

  @override
  void dispose() {
    _photoController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: IconButton(
          icon: Icon(
            Icons.arrow_back_ios,
            size: 21,
            color: Colors.black,
          ),
          onPressed: () {
            Navigator.pop(context);
          },
        ),
        actions: [
          GestureDetector(
            child: Container(
              alignment: Alignment.center,
              margin: EdgeInsets.only(right: 12),
              child: Text(
                "编辑",
                style: TextStyle(fontSize: 17, color: Colors.black),
              ),
            ),
            onTap: () {
              _saveScreenShot(context);
            },
          ),
        ],
        backgroundColor: Colors.white,
        elevation: 0,
        toolbarHeight: 52,
        centerTitle: true,
        title: Text(
          "${currentSelectIndex + 1}/${widget.imgList!.length}",
          style: TextStyle(fontSize: 18, color: Colors.black),
        ),
      ),
      backgroundColor: Colors.black,
      body: buildPageView(),
    );
  }

  PageView buildPageView() {
    return PageView.builder(
      physics: BouncingScrollPhysics(),
      controller: _photoController,
      onPageChanged: (index) {
        setState(() {
          currentSelectIndex = index;
        });
      },
      itemBuilder: (BuildContext context, int index) {
        return buildItemWidget(currentSelectIndex);
      },
      itemCount: widget.imgList!.length,
    );
  }

  buildItemWidget(int index) {
    return Stack(
      children: [
        GestureDetector(
          child: PhotoView(
            imageProvider: NetworkImage(widget.imgList![index], scale: 1.0),
            minScale: PhotoViewComputedScale.contained,
            maxScale: 3.0,
            enableRotation: false,
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
          onTap: () {
            Navigator.pop(context);
          },
        ),
      ],
    );
  }

  void _saveScreenShot(BuildContext context) {
    Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) {
      return ScrawlPage(
        imageData: widget.imgList![currentSelectIndex],
        imageDataCall: (data) {
          widget.imageDataCall!(data);
        },
      );
    }));
  }
}

import 'package:flutter/material.dart';
import 'package:zslDev/complent/photo_selector.dart';

typedef void BackEditRefresh(bool value);

class PictureUpLoad extends StatefulWidget {
  const PictureUpLoad({Key? key, this.title}) : super(key: key);
  final String? title;
  @override
  _PictureUpLoadState createState() => _PictureUpLoadState();
}

class _PictureUpLoadState extends State<PictureUpLoad> {
  String checkType = "";
  late PhotoSelector _photoSelector;
  int limitNum = 20;
  bool isEdit = false;
  List<String> _hasLoadImg = [];
  List netWorkImage = [];
  List myComment = [];
  List<Widget> comment = [];
  List<Widget> picture = [];
  List<Widget> commentTime = [];
  List<List<int>> pictureList = [];
  List<List<int>> imageData = [];

  @override
  void initState() {
    super.initState();
    _photoSelector = PhotoSelector(
      context,
      urlImgListLength: _hasLoadImg.length,
      photoCallBack: () {
        setState(() {});
      },
      onAssetInt: (assetList) {
        setState(() {
          imageData = assetList;
        });
      },
    );
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text("${widget.title!}"),
      ),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Container(
              child: _photoSelector.buildPhotoView(),
            ),
          ],
        ),
      ),
      bottomNavigationBar: Container(
        height: 50,
        margin: EdgeInsets.only(left: 20, right: 20, bottom: 20),
        child: RaisedButton(
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
          onPressed: () {},
          color: Colors.blue,
          child: Text(
            "确定",
            style: TextStyle(color: Colors.white, fontSize: 20),
          ),
        ),
      ),
    );
  }
}

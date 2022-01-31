import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class MyChatPage extends StatefulWidget {
  const MyChatPage({Key? key, this.title}) : super(key: key);
  final String? title;
  @override
  _MyChatPageState createState() => _MyChatPageState();
}

class _MyChatPageState extends State<MyChatPage> with AutomaticKeepAliveClientMixin{
  @override
  void initState() {
    super.initState();
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
          Container(
            margin: EdgeInsets.only(top: 30),
            alignment: Alignment.center,
            child: Text(
              "${widget.title}",
              style: TextStyle(fontSize: 26, color: Colors.black),
            ),
          ),
        ],
      ),
    );
  }
}

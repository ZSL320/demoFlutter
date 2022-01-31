import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class MySinglePage extends StatefulWidget {
  const MySinglePage({Key? key, this.title}) : super(key: key);
  final String? title;
  @override
  _MySinglePageState createState() => _MySinglePageState();
}

class _MySinglePageState extends State<MySinglePage>with AutomaticKeepAliveClientMixin {
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

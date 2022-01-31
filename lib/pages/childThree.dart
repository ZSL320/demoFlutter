import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class ChildThree extends StatefulWidget {
  const ChildThree({Key? key, this.notifier}) : super(key: key);
  final ValueNotifier? notifier;
  @override
  State<StatefulWidget> createState() {
    return ChildThreeState();
  }
}

class ChildThreeState extends State<ChildThree> {
  int count = 0;
  @override
  void initState() {
    super.initState();
    widget.notifier!.addListener(listener);
  }

  listener() {
    setState(() {
      count = widget.notifier!.value;
    });
  }

  @override
  void dispose() {
    widget.notifier!.removeListener(listener);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        alignment: Alignment.center,
        child: Text("$count"),
      ),
    );
  }
}

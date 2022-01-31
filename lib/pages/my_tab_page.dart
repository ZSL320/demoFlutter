import 'package:flick_video_player/flick_video_player.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:video_player/video_player.dart';
import 'package:zslDev/complent/controlOver_play.dart';

class MyTabPage extends StatefulWidget {
  const MyTabPage({Key? key, this.tabIndex}) : super(key: key);
  final int? tabIndex;
  @override
  State<StatefulWidget> createState() {
    return MyTabPageState();
  }
}

class MyTabPageState extends State<MyTabPage> {
  int _tabIndex = 0;
  late VideoPlayerController _controller;
  late VideoPlayerController _videoPlayerController;
  @override
  void initState() {
    super.initState();
    _tabIndex = widget.tabIndex!;
    if (_tabIndex == 0) {
      _controller = VideoPlayerController.asset('video/ChengXiang.mp4');
      _controller.addListener(() {
        print(_controller.value.isPlaying);
        setState(() {});
      });
      _controller.setLooping(true);
      _controller.initialize().then((_) => setState(() {}));
      _videoPlayerController =
          VideoPlayerController.asset('video/ChengXiang.mp4');
    }
  }

  @override
  void dispose() {
    if (_tabIndex == 0) {
      _videoPlayerController.dispose();
      _controller.dispose();
    }
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Column(
        children: <Widget>[
          _tabIndex == 0
              ? Column(
                  children: [
                    Column(
                      children: [
                        Container(
                          padding: const EdgeInsets.symmetric(vertical: 22),
                          child: Text("video_player"),
                        ),
                        Container(
                          child: AspectRatio(
                            aspectRatio: _controller.value.aspectRatio,
                            child: Stack(
                              alignment: Alignment.bottomCenter,
                              children: <Widget>[
                                VideoPlayer(_controller),
                                ControlsOverlay(controller: _controller),
                              ],
                            ),
                          ),
                        )
                      ],
                    ),
                    Column(
                      children: [
                        Container(
                          padding: const EdgeInsets.symmetric(vertical: 22),
                          child: Text("flick_video_player"),
                        ),
                        FlickVideoPlayer(
                          flickManager: FlickManager(
                              autoPlay: false,
                              videoPlayerController: _videoPlayerController),
                          flickVideoWithControlsFullscreen:
                              FlickVideoWithControls(
                            videoFit: BoxFit.fitWidth,
                          ),
                          flickVideoWithControls: FlickVideoWithControls(
                            textStyle: TextStyle(fontSize: 12),
                            controls: FlickPortraitControls(),
                          ),
                        ),
                      ],
                    )
                  ],
                )
              : Container(),
        ],
      ),
    );
  }
}

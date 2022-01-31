import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:video_player/video_player.dart';

class ControlsOverlay extends StatefulWidget {
  const ControlsOverlay({Key? key, required this.controller}) : super(key: key);

  final VideoPlayerController controller;

  @override
  State<StatefulWidget> createState() {
    return ControlsOverlayState();
  }
}

class ControlsOverlayState extends State<ControlsOverlay> {
  static const _examplePlaybackRates = [
    0.25,
    0.5,
    1.0,
    1.5,
    2.0,
    3.0,
    5.0,
    10.0,
  ];
  late bool isOpen = false;
  late bool isShow = true;
  int process = 0;
  @override
  void initState() {
    super.initState();
    process = widget.controller.value.position.inMilliseconds;
  }

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: <Widget>[
        GestureDetector(
          onTap: () {
            print(widget.controller.value.position.inMilliseconds);
            setState(() {
              isShow = !isShow;
            });
          },
          onPanDown: (e) {
            // print(e.globalPosition.dx);
          },
          onPanUpdate: (e) {
            print(e.localPosition);
            process += e.localPosition.dx.toInt();
            setState(() {});
          },
          onPanStart: (e) {
            // print(e.globalPosition.dx);
          },
          onPanEnd: (e) {
            //print(e.velocity);
          },
        ),
        AnimatedSwitcher(
            duration: Duration(milliseconds: 500),
            reverseDuration: Duration(milliseconds: 500),
            child: isShow
                ? GestureDetector(
                    onTap: () {
                      setState(() {
                        isOpen = !isOpen;
                      });
                      if (isOpen) {
                        Timer.periodic(Duration(seconds: 3), (timer) {
                          isShow = false;
                          timer.cancel();
                        });
                        widget.controller.play();
                      } else {
                        isShow = true;
                        widget.controller.pause();
                      }
                    },
                    child: Stack(
                      children: [
                        Center(
                          child: isOpen
                              ? Icon(
                                  Icons.pause_circle_rounded,
                                  color: Colors.white,
                                  size: 50.0,
                                  semanticLabel: 'Play',
                                )
                              : Icon(
                                  Icons.play_circle_fill_rounded,
                                  color: Colors.white,
                                  size: 50.0,
                                  semanticLabel: 'Play',
                                ),
                        ),
                        Container(
                          alignment: Alignment.bottomCenter,
                          child: VideoProgressIndicator(widget.controller,
                              allowScrubbing: true),
                        ),
                      ],
                    ))
                : SizedBox.shrink()),
        Align(
          alignment: Alignment.topRight,
          child: PopupMenuButton<double>(
            initialValue: widget.controller.value.playbackSpeed,
            tooltip: 'Playback speed',
            onSelected: (speed) {
              widget.controller.setPlaybackSpeed(speed);
            },
            itemBuilder: (context) {
              return [
                for (final speed in _examplePlaybackRates)
                  PopupMenuItem(
                    value: speed,
                    child: Text('${speed}x'),
                  )
              ];
            },
            child: Padding(
              padding: const EdgeInsets.symmetric(
                vertical: 12,
                horizontal: 16,
              ),
              child: Text('${widget.controller.value.playbackSpeed}x'),
            ),
          ),
        ),
      ],
    );
  }
}

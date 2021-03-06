import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';

const double _kTabHeight = 40.0;
const double _kTextAndIconTabHeight = 72.0;
class RoundUnderDataIndicator extends Decoration {
  const RoundUnderDataIndicator({
    this.borderSide = const BorderSide(width: 2.0, color: Colors.white),
    this.insets = EdgeInsets.zero,
  })  : assert(borderSide != null),
        assert(insets != null);

  final BorderSide? borderSide;
  final EdgeInsetsGeometry? insets;
  @override
  Decoration? lerpTo(Decoration? b, double t) {
    if (b is UnderlineTabIndicator) {
      return UnderlineTabIndicator(
        borderSide: BorderSide.lerp(borderSide!, b.borderSide, t),
        insets: EdgeInsetsGeometry.lerp(insets, b.insets, t)!,
      );
    }
    return super.lerpTo(b, t);
  }

  @override
  _UnderlinePainter createBoxPainter([VoidCallback? onChanged]) {
    return _UnderlinePainter(this, onChanged!);
  }
}

class _UnderlinePainter extends BoxPainter {
  _UnderlinePainter(this.decoration, VoidCallback onChanged)
      : assert(decoration != null),
        super(onChanged);

  final RoundUnderDataIndicator decoration;

  BorderSide? get borderSide => decoration.borderSide;
  EdgeInsetsGeometry? get insets => decoration.insets;

  @override
  void paint(
      Canvas? canvas, Offset? offset, ImageConfiguration? configuration) {
    assert(configuration != null);
    assert(configuration!.size != null);
    final Rect rect = offset! & configuration!.size!;
    // final TextDirection textDirection = configuration.textDirection;
    // final Rect indicator =
    // _indicatorRectFor(rect, textDirection).deflate(borderSide.width / 2.0);
    // final Paint paint = borderSide.toPaint()..strokeCap = StrokeCap.square;
    // 改为圆角
    final Paint paint = borderSide!.toPaint()..strokeCap = StrokeCap.square;
    // canvas.drawLine(indicator.bottomLeft, indicator.bottomRight, paint);
    // canvas.drawCircle(indicator.center,12.0,paint);
    Rect myRect =
        Rect.fromLTWH(rect.left + 10, rect.top / 2, rect.width / 2, 23);
    RRect rRect = RRect.fromRectAndRadius(myRect, Radius.circular(0.1));
    canvas!.drawRRect(rRect, paint);
  }
}

class VgTab extends StatelessWidget {
  const VgTab({
    Key? key,
    this.text,
    this.icon,
    this.child,
  })  : assert(text != null || child != null || icon != null),
        assert(!(text != null &&
            null !=
                child)), // TODO(goderbauer): https://github.com/dart-lang/sdk/issues/34180
        super(key: key);
  final String? text;
  final Widget? child;

  final Widget? icon;

  Widget _buildLabelText() {
    return child ?? Text(text!, softWrap: false, overflow: TextOverflow.fade);
  }

  @override
  Widget build(BuildContext context) {
    assert(debugCheckHasMaterial(context));

    double height;
    Widget label;
    if (icon == null) {
      height = _kTabHeight;
      label = _buildLabelText();
    } else if (text == null && child == null) {
      height = _kTabHeight;
      label = icon!;
    } else {
      height = _kTextAndIconTabHeight;
      label = Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: <Widget>[
          Container(
            child: icon,
            margin: const EdgeInsets.only(bottom: 10.0),
          ),
          _buildLabelText(),
        ],
      );
    }

    return SizedBox(
      height: height,
      child: Center(
        child: label,
        widthFactor: 1.0,
      ),
    );
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties.add(StringProperty('text', text, defaultValue: null));
    properties
        .add(DiagnosticsProperty<Widget>('icon', icon, defaultValue: null));
  }
}

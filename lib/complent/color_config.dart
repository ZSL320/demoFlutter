import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

const ColorTheme = Color(0xFF3E8BFD);
const ColorTheme_BG = Color(0xFFF5F6FA);
const ColorTheme_Disabled = Color(0xFFD2D2D2);
const ColorTheme_Shadow = Color(0x255F5F5F);
const ColorTheme_Black = Color(0xFF404040);
const ColorTheme_Gray = Color(0xFFE5E5E5);
const ColorTheme_Red = Color(0xFFFF684E);
const ColorTheme_Orange = Color(0xFFFFAF47);
const ColorTheme_Blue = Color(0xFFBBD6FF);
const ColorTheme_Violet = Color(0xFF939DF6);

const ColorTheme_Btn_G1 = Color(0xFF4BD5FF);
const ColorTheme_Btn_G2 = Color(0xFF4975EF);

const ColorBannerIndicator = Color(0xFFECEFF4);
const ColorBannerIndicatorSelect = Color(0xFFBBC7D3);

const ColorText_3 = Color(0xFF333333);
const ColorText_6 = Color(0xFF666666);
const ColorText_5E = Color(0xFF5E5E5E);
const ColorText_9A = Color(0xFF9A9A9A);
const ColorText_B4 = Color(0xFFB4B4B4);
const ColorText_C9 = Color(0xFFC9C9C9);
const ColorText_D9 = Color(0xFFD9D9D9);
const ColorText_E1 = Color(0xFFE1E1E1);
const ColorText_E7 = Color(0xFFE7E7E7);

const ColorCharts_Red = Color(0xFFDF5867);
const ColorCharts_Orange = Color(0xFFF1A531);
const ColorCharts_Yellow = Color(0xFFFDC148);
const ColorCharts_Green = Color(0xFF1ABDB8);
const ColorCharts_Cyan = Color(0xFF4ED2DF);
const ColorCharts_Blue = Color(0xFF248CFD);
const ColorCharts_Violet = Color(0xFF7A49D6);

const ColorWeather_Orange_G1 = Color(0xFFFDC148);
const ColorWeather_Orange_G2 = Color(0xFFF1A531);
const ColorWeather_Blue_G1 = Color(0xFF66BAFC);
const ColorWeather_Blue_G2 = Color(0xFF3A6ADF);

MaterialColor createMaterialColor(Color color) {
  List strengths = <double>[.05];
  Map<int, Color> swatch = {};
  final int r = color.red, g = color.green, b = color.blue;

  for (int i = 1; i < 10; i++) {
    strengths.add(0.1 * i);
  }
  strengths.forEach((strength) {
    final double ds = 0.5 - strength;
    swatch[(strength * 1000).round()] = Color.fromRGBO(
      r + ((ds < 0 ? r : (255 - r)) * ds).round(),
      g + ((ds < 0 ? g : (255 - g)) * ds).round(),
      b + ((ds < 0 ? b : (255 - b)) * ds).round(),
      1,
    );
  });
  return MaterialColor(color.value, swatch);
}

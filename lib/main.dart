import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:zslDev/pages/home_page.dart';

void main() {
  runApp(MyApp());
}

const List<Locale> supportedLocales = <Locale>[
  Locale('zh'),
];

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Flutter Demo',
      supportedLocales: supportedLocales,
      localizationsDelegates: const [
        GlobalMaterialLocalizations.delegate,
      ],
      builder: EasyLoading.init(),
      theme: ThemeData(
        platform: TargetPlatform.android,
      ),
      home: HomePage(),
    );
  }
}

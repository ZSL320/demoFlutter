import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:zslDev/pages/phone_country_code_page.dart';
import 'package:zslDev/pages/syllabus_page.dart';
import 'package:zslDev/pages/verification_code_page.dart';


import 'MyRaisedButton.dart';

/**
 * @desc
 * @author xiedong
 * @date   2020-02-28.
 */

class CustomWidgetPage extends StatelessWidget {
  const CustomWidgetPage({Key? key, this.title}) : super(key: key);
  final String? title;
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text(
          "$title",
          style: const TextStyle(color: Colors.white, fontSize: 16),
        ),
      ),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          MyRaisedButton( VerficationCodePage(), "验证码倒计时Widget"),
          MyRaisedButton( PhoneCountryCodePage(), "城市地区选择页"),
          MyRaisedButton( SyllabusPage(), "课程表View")
        ],
      ),
    );
  }
}

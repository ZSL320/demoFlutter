import 'date.config.dart';

class DateFormats {
  static const String full = "yyyy-MM-dd HH:mm:ss";
  static const String y_mo_d_h_m = "yyyy-MM-dd HH:mm";
  static const String y_mo_d = "yyyy-MM-dd";
  static const String y_mo = "yyyy-MM";
  static const String mo_d = "MM.dd";
  static const String mo_d_h_m = "MM-dd HH:mm";
  static const String h_m_s = "HH:mm:ss";
  static const String h_m = "HH:mm";
  static const String full_name = "yyMMddHHmmss";

  static const String zh_full = "yyyy年MM月dd日 HH时mm分ss秒";
  static const String zh_y_mo_d_h_m = "yyyy年MM月dd日 HH时mm分";
  static const String zh_y_mo_d = "yyyy年MM月dd日";
  static const String zh_y_mo = "yyyy年MM月";
  static const String zh_mo_d = "MM月dd日";
  static const String zh_mo_d_h_m = "MM月dd日 HH时mm分";
  static const String zh_h_m_s = "HH时mm分ss秒";
  static const String zh_h_m = "HH时mm分";
}

class DateUtil {
  static String formatDate(DateTime? dateTime, {bool? isUtc, String? format}) {
    if (dateTime == null) return "";
    format = format ?? DateFormats.full;
    if (format.contains("yy")) {
      String year = dateTime.year.toString();
      if (format.contains("yyyy")) {
        format = format.replaceAll("yyyy", year);
      } else {
        format = format.replaceAll(
            "yy", year.substring(year.length - 2, year.length));
      }
    }

    format = _comFormat(dateTime.month, format, 'M', 'MM');
    format = _comFormat(dateTime.day, format, 'd', 'dd');
    format = _comFormat(dateTime.hour, format, 'H', 'HH');
    format = _comFormat(dateTime.minute, format, 'm', 'mm');
    format = _comFormat(dateTime.second, format, 's', 'ss');
    format = _comFormat(dateTime.millisecond, format, 'S', 'SSS');

    return format;
  }

  static String _comFormat(
      int value, String format, String single, String full) {
    if (format.contains(single)) {
      if (format.contains(full)) {
        format =
            format.replaceAll(full, value < 10 ? '0$value' : value.toString());
      } else {
        format = format.replaceAll(single, value.toString());
      }
    }
    return format;
  }

  // get WeekDay.
  static String? getWeekDay(DateTime? dateTime) {
    if (dateTime == null) return null;
    String? weekday;

    switch (dateTime.weekday) {
      case 1:
        weekday = "Monday";
        break;
      case 2:
        weekday = "Tuesday";
        break;
      case 3:
        weekday = "Wednesday";
        break;
      case 4:
        weekday = "Thursday";
        break;
      case 5:
        weekday = "Friday";
        break;
      case 6:
        weekday = "Saturday";
        break;
      case 7:
        weekday = "Sunday";
        break;
      default:
        break;
    }
    return weekday;
  }

  // get ZH WeekDay.
  static String? getZHWeekDay(DateTime? dateTime) {
    if (dateTime == null) return null;
    String? weekday;
    switch (dateTime.weekday) {
      case 1:
        weekday = "星期一";
        break;
      case 2:
        weekday = "星期二";
        break;
      case 3:
        weekday = "星期三";
        break;
      case 4:
        weekday = "星期四";
        break;
      case 5:
        weekday = "星期五";
        break;
      case 6:
        weekday = "星期六";
        break;
      case 7:
        weekday = "星期日";
        break;
      default:
        break;
    }
    return weekday;
  }

  // 分钟数转换为 x天x时x分
  static String minutesTransform(int minutes) {
    Duration duration = Duration(minutes: minutes);
    List<String> parts = duration.toString().split(':');
    int hour = int.parse(parts[0]);
    if (hour > 24) {
      int day = hour ~/ 24;
      hour = hour % (day * 24);
      return '$day天$hour小时${parts[1]}分';
    }
    return '$hour小时${parts[1]}分';
  }

  // 计算时差（天）
  static int? differenceDay(DateTime? startDate, DateTime? endDate) {
    if (startDate != null && endDate != null) {
      DateTime sDate = DateTime.parse(startDate.formatYMD);
      DateTime eDate = DateTime.parse(endDate.formatYMD);
      return eDate.difference(sDate).inDays;
    }
    return null;
  }

  // 获取本周开始时间
  static DateTime getLocalWeeksDate() {
    DateTime nowTime = DateTime.now();
    int weekday = nowTime.weekday;
    DateTime yesterday = nowTime.add(new Duration(days: -(weekday - 1)));
    DateTime day =
        DateTime(yesterday.year, yesterday.month, yesterday.day, 0, 0, 0);
    return day;
  }

  // 获取本月开始时间
  static DateTime getLocalMonthDate() {
    DateTime nowTime = DateTime.now();
    DateTime day = DateTime(nowTime.year, nowTime.month, 1, 0, 0, 0);
    return day;
  }

  // 获取本年开始时间
  static DateTime getLocalYearDate() {
    DateTime nowTime = DateTime.now();
    DateTime day = new DateTime(nowTime.year, 1, 1, 0, 0, 0);
    return day;
  }
}

import 'date_util.dart';

extension DateTimeExtension on DateTime {
  String get format => DateUtil.formatDate(this, format: DateFormats.full);

  String get formatYMD => DateUtil.formatDate(this, format: DateFormats.y_mo_d);
  String get formatYMDT =>
      DateUtil.formatDate(this, format: DateFormats.zh_y_mo_d);
  String get formatYM => DateUtil.formatDate(this, format: DateFormats.y_mo);

  String get formatYMDHM =>
      DateUtil.formatDate(this, format: DateFormats.y_mo_d_h_m);

  String get formatHMS => DateUtil.formatDate(this, format: DateFormats.h_m_s);
  String get formatHM => DateUtil.formatDate(this, format: DateFormats.h_m);

  String get formatMd => DateUtil.formatDate(this, format: DateFormats.mo_d);
  String get formatMD => DateUtil.formatDate(this, format: DateFormats.zh_mo_d);
}

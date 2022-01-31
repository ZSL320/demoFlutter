import 'package:flutter/material.dart';
import 'package:table_calendar/table_calendar.dart';
import 'package:zslDev/complent/canleder_indicator.dart';

class DateSelect extends StatefulWidget {
  const DateSelect({Key? key, this.title}) : super(key: key);
  final String? title;
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return DateSelectState();
  }
}

class DateSelectState extends State<DateSelect> {
  CalendarFormat _calendarFormat = CalendarFormat.month;
  DateTime _focusedDay = DateTime.now();
  DateTime? _selectedDay;
  int? _today;

  _weekGet(int year, int month, int day) {
    if (month == 1 || month == 2) {
      month += 12;
      year -= 1;
    }
    double week = ((day +
            2 * month +
            3 * (month + 1) / 5 +
            year +
            year / 4 -
            year / 100 +
            year / 400) %
        7);
    _today = (week + 1).floor();
  }

  @override
  void initState() {
    super.initState();
    _weekGet(DateTime.now().year, DateTime.now().month, DateTime.now().day);
  }


  _getDay(value) {
    switch (value) {
      case 1:
        return StartingDayOfWeek.monday;

      case 2:
        return StartingDayOfWeek.tuesday;

      case 3:
        return StartingDayOfWeek.wednesday;

      case 4:
        return StartingDayOfWeek.thursday;

      case 5:
        return StartingDayOfWeek.friday;

      case 6:
        return StartingDayOfWeek.saturday;
      case 7:
        return StartingDayOfWeek.sunday;
    }
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text("${widget.title}"),
      ),
      body: TableCalendar(
        calendarBuilders: CalendarBuilders(
          dowBuilder: (BuildContext context, DateTime day) {
            return day.day == DateTime.now().day
                ? Container(
                    alignment: Alignment.center,
                    child: const Text(
                      "今日",
                      style:
                          TextStyle(fontSize: 15, color: Color(0xFFFFFFFF)),
                    ),
                  )
                : null;
          },
        ),
        startingDayOfWeek: _getDay(_today),
        firstDay: DateTime(
                DateTime.now().year, DateTime.now().month, DateTime.now().day)
            .subtract(const Duration(days: 30)),
        lastDay: DateTime(
                DateTime.now().year, DateTime.now().month, DateTime.now().day)
            .add(const Duration(days: 30)),
        daysOfWeekStyle:const  DaysOfWeekStyle(
          weekdayStyle: TextStyle(
            color: Colors.white,
          ),
          weekendStyle:  TextStyle(
            color: Colors.white,
          ),
          decoration: BoxDecoration(
            border: Border(
                top: BorderSide(
              width: 0,
            )),
          ),
        ),
        focusedDay: _focusedDay,
        daysOfWeekHeight: 25,
        headerVisible: false,
        rowHeight: 28,
        headerStyle: const HeaderStyle(
          titleTextStyle: TextStyle(
            inherit: false,
          ),
          formatButtonVisible: false,
          leftChevronVisible: false,
          rightChevronVisible: false,
        ),
        calendarStyle: CalendarStyle(
          weekendTextStyle: const TextStyle(color: Color(0xFFFFFFFF)),
          holidayTextStyle:const TextStyle(color: Color(0xFFFFFFFF)),
          defaultTextStyle: const TextStyle(color: Color(0xFFFFFFFF)),
          outsideTextStyle: const TextStyle(color: Color(0xFFFFFFFF)),
          disabledTextStyle:const TextStyle(color: Color(0xFFFFFFFF)),
          markerSizeScale: 0,
          markersAnchor: 0,
          tableBorder: TableBorder.all(
            width: 0,
            style: BorderStyle.none,
          ),
          selectedDecoration: const RoundUnderDataIndicator(
              borderSide: BorderSide(
            width: 29.0,
            color: Color(0x7FE1E1E1),
          )),
          rowDecoration: const BoxDecoration(
            border: Border(
              top: BorderSide(
                width: 0,
              ),
              bottom: BorderSide(
                width: 0,
              ),
            ),
          ),
          todayDecoration: _selectedDay == null
              ? RoundUnderDataIndicator(
                  borderSide: BorderSide(
                  width: 29.0,
                  color: Color(0x7FE1E1E1),
                ))
              : RoundUnderDataIndicator(
                  borderSide: BorderSide(
                  width: 29.0,
                  color: Color(0x00),
                )),
        ),
        calendarFormat: CalendarFormat.week,
        selectedDayPredicate: (day) {
          return isSameDay(_selectedDay, day);
        },
        onDaySelected: (selectedDay, focusedDay) {
          if (!isSameDay(_selectedDay, selectedDay)) {
            setState(() {
              _selectedDay = selectedDay;
              _focusedDay = focusedDay;
            });
          }
        },
        onFormatChanged: (format) {
          if (_calendarFormat != format) {
            setState(() {
              _calendarFormat = format;
            });
          }
        },
        onPageChanged: (focusedDay) {
          // No need to call `setState()` here
          _focusedDay = focusedDay;
          setState(() {});
        },
      ),
    );
  }
}

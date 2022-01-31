import 'dart:async';

import 'package:flutter/material.dart';
import 'package:table_calendar/table_calendar.dart';
import 'package:zslDev/complent/canleder_indicator.dart';
import 'package:zslDev/complent/date.config.dart';
import 'day_model.dart';

class MyDatePage extends StatefulWidget {
  const MyDatePage({Key? key, this.title}) : super(key: key);
  final String? title;
  @override
  _MyDatePageState createState() => _MyDatePageState();
}

class _MyDatePageState extends State<MyDatePage> {
  CalendarFormat _calendarFormat = CalendarFormat.month;
  DateTime _focusedDay = DateTime.now();
  DateTime? _selectedDay;
  int? _today;
  bool isChange = false;
  SelectDay selectDay = SelectDay(DateTime.now(), DateTime.now());
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

  StreamController<SelectDay> _controller = new StreamController();
  @override
  void initState() {
    super.initState();
    _weekGet(DateTime.now().year, DateTime.now().month, DateTime.now().day);
  }

  @override
  void dispose() {
    _controller.close();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text("${widget.title}"),
          centerTitle: true,
        ),
        body: Container(
            margin: EdgeInsets.only(top: 60),
            child: StreamBuilder<SelectDay>(
              initialData: SelectDay(DateTime.now(), DateTime.now()),
              stream: _controller.stream,
              builder: (context, AsyncSnapshot<SelectDay> snapshot) {
                return TableCalendar(
                  locale: "zh",
                  calendarBuilders: CalendarBuilders(
                    dowBuilder: (BuildContext context, DateTime day) {
                      return day.day == DateTime.now().day
                          ? Container(
                              alignment: Alignment.center,
                              child: Text(
                                "今日",
                                style: TextStyle(
                                    fontSize: 15, color: Color(0xFFFFFFFF)),
                              ),
                            )
                          : null;
                    },
                    headerTitleBuilder: (BuildContext context, DateTime day) {
                      return Row(
                        mainAxisAlignment: MainAxisAlignment.spaceAround,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Container(
                            child: Text(
                              "${snapshot.data!.selectDay.formatYMDT}",
                              style: TextStyle(
                                fontSize: 16,
                              ),
                            ),
                          ),
                          Container(
                            width: 90,
                            height: 29,
                            child: RaisedButton(
                              shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(6)),
                              color: Color(0xFF3396AE),
                              child: Text(
                                "回今日",
                                style: TextStyle(
                                    fontSize: 16, color: Colors.white),
                              ),
                              onPressed: () {
                                isChange = true;
                                _controller.sink.add(selectDay);
                              },
                            ),
                          ),
                        ],
                      );
                    },
                  ),
                  startingDayOfWeek: _getDay(_today),
                  firstDay: DateTime(DateTime.now().year, DateTime.now().month,
                          DateTime.now().day)
                      .subtract(Duration(days: 30)),
                  lastDay: DateTime(DateTime.now().year, DateTime.now().month,
                          DateTime.now().day)
                      .add(Duration(days: 30)),
                  daysOfWeekStyle: DaysOfWeekStyle(
                    weekdayStyle: TextStyle(
                      color: Colors.white,
                    ),
                    weekendStyle: TextStyle(
                      color: Colors.white,
                    ),
                    decoration: BoxDecoration(
                      color: Color(0xFF3396AE),
                      border: const Border(
                          top: BorderSide(
                        width: 0,
                        color: Color.fromRGBO(51, 150, 174, 1),
                      )),
                    ),
                  ),
                  headerStyle: HeaderStyle(
                    titleTextStyle: TextStyle(
                      inherit: false,
                    ),
                    formatButtonVisible: false,
                    leftChevronVisible: false,
                    rightChevronVisible: false,
                  ),
                  focusedDay:
                      isChange ? snapshot.data!.focusedDay : _focusedDay,
                  daysOfWeekHeight: 25,
                  headerVisible: true,
                  rowHeight: 28,
                  calendarStyle: CalendarStyle(
                    weekendTextStyle: TextStyle(color: Color(0xFFFFFFFF)),
                    holidayTextStyle: TextStyle(color: Color(0xFFFFFFFF)),
                    defaultTextStyle: TextStyle(color: Color(0xFFFFFFFF)),
                    outsideTextStyle: TextStyle(color: Color(0xFFFFFFFF)),
                    disabledTextStyle: TextStyle(color: Color(0xFFFFFFFF)),
                    markerSizeScale: 0,
                    markersAnchor: 0,
                    tableBorder: TableBorder.all(
                      width: 0,
                      style: BorderStyle.none,
                    ),
                    selectedDecoration: RoundUnderDataIndicator(
                        borderSide: BorderSide(
                      width: 29.0,
                      color: Color(0x7FE1E1E1),
                    )),
                    rowDecoration: BoxDecoration(
                      color: Color(0xFF3396AE),
                      border: Border(
                        top: BorderSide(
                          color: Color(0xFF3396AE),
                          width: 0,
                        ),
                        bottom: BorderSide(
                          width: 0,
                          color: Color(0xFF3396AE),
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
                    return isSameDay(
                        isChange ? snapshot.data!.selectDay : _selectedDay,
                        day);
                  },
                  onDaySelected: (selectedDay, focusedDay) {
                    if (!isSameDay(
                        isChange ? snapshot.data!.selectDay : _selectedDay,
                        selectedDay)) {
                      isChange = false;
                      _controller.sink.add(SelectDay(selectedDay, focusedDay));
                      _selectedDay = selectedDay;
                      _focusedDay = focusedDay;
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
                  },
                );
              },
            )));
  }
}

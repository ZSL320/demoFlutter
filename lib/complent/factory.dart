class Person {
  bool? isClick;
  static Person? instance;
  factory Person([bool isClick = true]) {
    // 工厂构造函数总不能使用this关键字
    if (Person.instance == null) {
      Person.instance = Person.newSelf(isClick);
      print(Person.instance!.isClick);
    }
    return Person.instance!;
  }
// 命名构造函数
  Person.newSelf(this.isClick);
}

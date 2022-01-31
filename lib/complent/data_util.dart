// 数据处理工具类
class DataUtil {
  // 补0
  static String fill0(int number, int length) {
    int numberLength = "$number".length;
    int lengthDiff = length - numberLength; // 位数差
    return "${"0" * lengthDiff}$number";
  }

  // 数据判空
  static bool isEmpty(value) => !isNotEmpty(value);

  static bool isNotEmpty(value) =>
      value != null && value != "null" && value.length > 0;
}

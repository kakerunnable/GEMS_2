import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RowSelectionAction {

  public static RowSelection run(File file, Integer rowRangeFirst, Integer rowRangeEnd) throws IOException {

    // すべての行をリストで取得
    List<String> lines = Files.readLines(file, Charset.defaultCharset());

    // すべての行を結合
    String allLines = lines.stream().collect(Collectors.joining(System.lineSeparator()));

    // 抽出対象行のみ取得
    List<String> extractLines = IntStream.range(rowRangeFirst - 1, rowRangeEnd)
        .mapToObj(lines::get).collect(Collectors.toList());

    // 開始位置を取得
    Integer start = allLines.indexOf(extractLines.get(0));

    // 長さ
    Integer length = calcLength(extractLines);

    // オブジェクト生成
    RowSelection selection = new RowSelection(start, length);

    return selection;
  }

  static Integer calcLength(List<String> strs) {
    return strs.stream().collect(Collectors.joining(System.lineSeparator())).length();
  }
}

package selection;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RowSelectionAction {

  public static RowSelection run(File file, Integer rowRangeFirst, Integer rowRangeEnd) throws IOException {

    // 引数チェック
    RowSelectionValidator.validate(file, rowRangeFirst, rowRangeEnd);

    // すべての行をリストで取得
    List<String> lines = Files.readLines(file, Charset.defaultCharset());
    // すべての行を結合
    String allLines = lines.stream().collect(Collectors.joining(System.lineSeparator()));

    // 抽出対象行のみ取得
    String extractLines = IntStream.range(rowRangeFirst - 1, rowRangeEnd)
        .mapToObj(lines::get)
        .collect(Collectors.joining(System.lineSeparator()));

    // 開始位置・長さを取得
    Integer start = allLines.indexOf(extractLines);
    Integer length = extractLines.length();

    return RowSelection.of(start, length);
  }
}

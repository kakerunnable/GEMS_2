package selection;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.io.Files;

public class RowSelectionAction {

  public static RowSelection run(File file, Integer rowRangeFirst, Integer rowRangeEnd) {

    // 引数チェック
    try {
		RowSelectionValidator.validate(file, rowRangeFirst, rowRangeEnd);
	} catch (IOException e) {
		throw new RuntimeException(e);
	}

    // すべての行をリストで取得
    List<String> lines;
	try {
		lines = Files.readLines(file, Charset.defaultCharset());
	} catch (IOException e) {
		throw new RuntimeException(e);
	}
    // すべての行を結合
    String allLines = lines.stream().collect(Collectors.joining(System.lineSeparator()));

    // 最初の行から抽出対象行まで取得
    String firstLines = IntStream.range(0, rowRangeFirst)
            .mapToObj(lines::get)
            .collect(Collectors.joining(System.lineSeparator()));


    // 抽出対象行のみ取得
    String extractLines = IntStream.range(rowRangeFirst - 1, rowRangeEnd)
        .mapToObj(lines::get)
        .collect(Collectors.joining(System.lineSeparator()));

    // 開始位置・長さを取得
    Integer start = firstLines.length() - lines.get(rowRangeFirst - 1).length();
    Integer length = extractLines.length();

    return RowSelection.of(start, length);
  }
}

package selection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class RowSelectionValidator {

  public static void validate(File file, Integer rowRangeFirst, Integer rowRangeEnd) throws IOException {

    if (!file.exists()) {
      throw new RowSelectionException(String.format("ファイル'%s'が見つかりません。", file.toString()));
    }

    if (rowRangeEnd == 0) {
      throw new RowSelectionException("抽出文字数が 0 です。");
    }

    if (rowRangeFirst > Files.lines(file.toPath()).count()) {
      throw new RowSelectionException("指定開始位置がファイルの文字数を超過しています。");
    }
  }
}

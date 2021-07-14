import java.io.File;
import java.io.IOException;

public class TestMain {

  public static void main(String[] args) throws IOException {

    File targetFile = new File("ifile-testing/src/test/SampleClass.java");

    // targetFile の 6行目から8行目を抽出する場合
    RowSelection selection = RowSelectionAction.run(targetFile, 6, 8);

    System.out.printf("start:%s, length:%s", selection.getSelectionStart(), selection.getSelectionLength());
  }
}

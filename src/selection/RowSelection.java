package selection;

public class RowSelection {

  private Integer selectionStart;
  private Integer selectionLength;

  private RowSelection(Integer selectionStart, Integer selectionLength) {
    this.selectionStart = selectionStart;
    this.selectionLength = selectionLength;
  }

  public Integer getSelectionStart() {
    return this.selectionStart;
  }

  public Integer getSelectionLength() {
    return this.selectionLength;
  }

  public static RowSelection of(Integer selectionStart, Integer selectionLength) {
    return new RowSelection(selectionStart, selectionLength);
  }
}

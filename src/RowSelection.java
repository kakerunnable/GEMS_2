public class RowSelection {

  private Integer selectionStart;
  private Integer selectionLength;

  public RowSelection(Integer selectionStart, Integer selectionLength) {
    this.selectionStart = selectionStart;
    this.selectionLength = selectionLength;
  }

  public Integer getSelectionStart() {
    return this.selectionStart;
  }

  public Integer getSelectionLength() {
    return this.selectionLength;
  }
}

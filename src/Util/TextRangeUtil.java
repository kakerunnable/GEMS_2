/*    */ package Util;
/*    */ 
/*    */ import org.eclipse.jdt.core.ICompilationUnit;
/*    */ import org.eclipse.jdt.core.ISourceRange;
/*    */ import org.eclipse.jdt.core.SourceRange;
/*    */ import org.eclipse.jface.text.BadLocationException;
/*    */ import org.eclipse.jface.text.Document;
/*    */ import org.eclipse.jface.text.IDocument;
/*    */ import org.eclipse.jface.text.IRegion;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TextRangeUtil
/*    */ {
/*    */   public static ISourceRange getSelection(ICompilationUnit cu, int startLine, int startColumn, int endLine, int endColumn) throws Exception {
/* 16 */     Document document = new Document(cu.getSource());
/* 17 */     int offset = getOffset((IDocument)document, startLine, startColumn);
/* 18 */     int end = getOffset((IDocument)document, endLine, endColumn);
/* 19 */     return (ISourceRange)new SourceRange(offset, end - offset);
/*    */   }
/*    */   
/*    */   public static int getOffset(ICompilationUnit cu, int line, int column) throws Exception {
/* 23 */     Document document = new Document(cu.getSource());
/* 24 */     return getOffset((IDocument)document, line, column);
/*    */   }
/*    */   
/*    */   public static int getOffset(String source, int line, int column) throws BadLocationException {
/* 28 */     Document document = new Document(source);
/* 29 */     return getOffset((IDocument)document, line, column);
/*    */   }
/*    */   
/*    */   private static int getOffset(IDocument document, int line, int column) throws BadLocationException {
/* 33 */     int r = document.getLineInformation(line - 1).getOffset();
/* 34 */     IRegion region = document.getLineInformation(line - 1);
/* 35 */     int lineTabCount = calculateTabCountInLine(document.get(region.getOffset(), region.getLength()), column);
/* 36 */     r += column - 1 - lineTabCount * getTabWidth() + lineTabCount;
/* 37 */     return r;
/*    */   }
/*    */   
/*    */   private static final int getTabWidth() {
/* 41 */     return 4;
/*    */   }
/*    */   
/*    */   public static int calculateTabCountInLine(String lineSource, int lastCharOffset) {
/* 45 */     int acc = 0;
/* 46 */     int charCount = 0;
/* 47 */     for (int i = 0; charCount < lastCharOffset - 1; i++) {
/* 48 */       if ('\t' == lineSource.charAt(i)) {
/* 49 */         acc++;
/* 50 */         charCount += getTabWidth();
/*    */       } else {
/* 52 */         charCount++;
/*    */       } 
/* 54 */     }  return acc;
/*    */   }
/*    */ }


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/Util/TextRangeUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
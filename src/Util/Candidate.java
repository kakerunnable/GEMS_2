/*    */ package Util;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Candidate
/*    */ {
/*    */   String filePath;
/*    */   String methodName;
/*    */   List<Integer> startIndex;
/*    */   List<Integer> endIndex;
/*    */   List<Boolean> isExtractable;
/*    */   
/*    */   public Candidate() {}
/*    */   
/*    */   public Candidate(String filePath, List<Integer> startIndex, List<Integer> endIndex) {
/* 21 */     this.filePath = filePath;
/* 22 */     this.startIndex = startIndex;
/* 23 */     this.endIndex = endIndex;
/*    */   }
/*    */   
/*    */   public Candidate(String filepath, String methodName, int startIndex, int endIndex) {
/* 27 */     this.filePath = filepath;
/* 28 */     this.startIndex = new ArrayList<>();
/* 29 */     this.endIndex = new ArrayList<>();
/*    */     
/* 31 */     this.startIndex.add(Integer.valueOf(startIndex));
/* 32 */     this.endIndex.add(Integer.valueOf(endIndex));
/*    */     
/* 34 */     this.methodName = methodName;
/* 35 */     this.isExtractable = new ArrayList<>();
/*    */   }
/*    */   
/*    */   public String getMethodName() {
/* 39 */     return this.methodName;
/*    */   }
/*    */   
/*    */   public void setMethodName(String methodName) {
/* 43 */     this.methodName = methodName;
/*    */   }
/*    */   
/*    */   public String getFilePath() {
/* 47 */     return this.filePath;
/*    */   }
/*    */   public void setFilePath(String filePath) {
/* 50 */     this.filePath = filePath;
/*    */   }
/*    */   
/*    */   public List<Integer> getStartIndex() {
/* 54 */     return this.startIndex;
/*    */   }
/*    */   
/*    */   public void setStartIndex(List<Integer> startIndex) {
/* 58 */     this.startIndex = startIndex;
/*    */   }
/*    */   
/*    */   public void appendStartIndex(int index) {
/* 62 */     this.startIndex.add(Integer.valueOf(index));
/*    */   }
/*    */   
/*    */   public void appendEndIndex(int index) {
/* 66 */     this.endIndex.add(Integer.valueOf(index));
/*    */   }
/*    */   
/*    */   public List<Integer> getEndIndex() {
/* 70 */     return this.endIndex;
/*    */   }
/*    */   
/*    */   public void setEndIndex(List<Integer> endIndex) {
/* 74 */     this.endIndex = endIndex;
/*    */   }
/*    */   
/*    */   public void appendIsExtractable(Boolean isExtractable) {
/* 78 */     this.isExtractable.add(isExtractable);
/*    */   }
/*    */   
/*    */   public List<Boolean> getIsExtractable() {
/* 82 */     return this.isExtractable;
/*    */   }
/*    */   
/*    */   public void setIsExtractable(List<Boolean> isExtractable) {
/* 86 */     this.isExtractable = isExtractable;
/*    */   }
/*    */ }


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/Util/Candidate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
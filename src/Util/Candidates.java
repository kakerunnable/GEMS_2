/*    */ package Util;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Candidates
/*    */ {
/*    */   String projectPath;
/*    */   String filePath;
/*    */   List<Integer> startIndex;
/*    */   List<Integer> endIndex;
/*    */   List<Boolean> isExtractable;
/*    */   
/*    */   public Candidates() {}
/*    */   
/*    */   public Candidates(String projectPath, String filePath, List<Integer> startIndex, List<Integer> endIndex) {
/* 20 */     this.projectPath = projectPath;
/* 21 */     this.filePath = filePath;
/* 22 */     this.startIndex = startIndex;
/* 23 */     this.endIndex = endIndex;
/*    */   }
/*    */   
/*    */   public Candidates(String projectPath, String[] data) {
/* 27 */     this.projectPath = projectPath;
/* 28 */     this.filePath = data[0];
/* 29 */     this.startIndex = new ArrayList<>();
/* 30 */     this.endIndex = new ArrayList<>();
/* 31 */     this.startIndex.add(Integer.valueOf(Integer.parseInt(data[2])));
/* 32 */     this.endIndex.add(Integer.valueOf(Integer.parseInt(data[4])));
/* 33 */     this.isExtractable = new ArrayList<>();
/*    */   }
/*    */   
/*    */   public String getProjectPath() {
/* 37 */     return this.projectPath;
/*    */   }
/*    */   public void setProjectPath(String projectPath) {
/* 40 */     this.projectPath = projectPath;
/*    */   }
/*    */   public String getFilePath() {
/* 43 */     return this.filePath;
/*    */   }
/*    */   public void setFilePath(String filePath) {
/* 46 */     this.filePath = filePath;
/*    */   }
/*    */   
/*    */   public List<Integer> getStartIndex() {
/* 50 */     return this.startIndex;
/*    */   }
/*    */   
/*    */   public void setStartIndex(List<Integer> startIndex) {
/* 54 */     this.startIndex = startIndex;
/*    */   }
/*    */   
/*    */   public void appendStartIndex(int index) {
/* 58 */     this.startIndex.add(Integer.valueOf(index));
/*    */   }
/*    */   
/*    */   public void appendEndIndex(int index) {
/* 62 */     this.endIndex.add(Integer.valueOf(index));
/*    */   }
/*    */   
/*    */   public List<Integer> getEndIndex() {
/* 66 */     return this.endIndex;
/*    */   }
/*    */   
/*    */   public void setEndIndex(List<Integer> endIndex) {
/* 70 */     this.endIndex = endIndex;
/*    */   }
/*    */   
/*    */   public void appendIsExtractable(Boolean isExtractable) {
/* 74 */     this.isExtractable.add(isExtractable);
/*    */   }
/*    */   
/*    */   public List<Boolean> getIsExtractable() {
/* 78 */     return this.isExtractable;
/*    */   }
/*    */   
/*    */   public void setIsExtractable(List<Boolean> isExtractable) {
/* 82 */     this.isExtractable = isExtractable;
/*    */   }
/*    */ }


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/Util/Candidates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
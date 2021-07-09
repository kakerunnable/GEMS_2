/*    */ package Util;
/*    */ 
/*    */ 
/*    */ public class ExtractMethodResults
/*    */ {
/*    */   String top;
/*    */   String methodName;
/*    */   String startinLineNo;
/*    */   String endingLineNo;
/*    */   String prob;
/*    */   
/*    */   public ExtractMethodResults(String top, String methodName, String startinLineNo, String endingLineNo, String prob) {
/* 13 */     this.top = top;
/* 14 */     this.methodName = methodName;
/* 15 */     this.startinLineNo = startinLineNo;
/* 16 */     this.endingLineNo = endingLineNo;
/* 17 */     this.prob = prob;
/*    */   }
/*    */   public String getTop() {
/* 20 */     return this.top;
/*    */   }
/*    */   public void setTop(String top) {
/* 23 */     this.top = top;
/*    */   }
/*    */   public String getMethodName() {
/* 26 */     return this.methodName;
/*    */   }
/*    */   public void setMethodName(String methodName) {
/* 29 */     this.methodName = methodName;
/*    */   }
/*    */   public String getStartinLineNo() {
/* 32 */     return this.startinLineNo;
/*    */   }
/*    */   public void setStartinLineNo(String startinLineNo) {
/* 35 */     this.startinLineNo = startinLineNo;
/*    */   }
/*    */   public String getEndingLineNo() {
/* 38 */     return this.endingLineNo;
/*    */   }
/*    */   public void setEndingLineNo(String endingLineNo) {
/* 41 */     this.endingLineNo = endingLineNo;
/*    */   }
/*    */   public String getProb() {
/* 44 */     return this.prob;
/*    */   }
/*    */   public void setProb(String prob) {
/* 47 */     this.prob = prob;
/*    */   }
/*    */ }


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/Util/ExtractMethodResults.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
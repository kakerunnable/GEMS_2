/*    */ package Util;
/*    */ 
/*    */ public class InvocationInformation
/*    */ {
/*    */   String caller;
/*    */   int noOfCalls;
/*    */   String returnType;
/*    */   int startLineNumber;
/*    */   int endLineNumber;
/*    */   int length;
/*    */   
/*    */   public InvocationInformation(String caller, int noOfCalls, String returnType) {
/* 13 */     this.caller = caller;
/* 14 */     this.noOfCalls = noOfCalls;
/* 15 */     this.returnType = returnType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InvocationInformation(String caller, int noOfCalls, int startLineNumber, int endLineNumber, int length) {
/* 22 */     this.caller = caller;
/* 23 */     this.noOfCalls = noOfCalls;
/* 24 */     this.returnType = this.returnType;
/* 25 */     this.startLineNumber = startLineNumber;
/* 26 */     this.endLineNumber = endLineNumber;
/* 27 */     this.length = length;
/*    */   }
/*    */ 
/*    */   
/*    */   public InvocationInformation(String caller, int noOfCalls) {
/* 32 */     this.caller = caller;
/* 33 */     this.noOfCalls = noOfCalls;
/*    */   }
/*    */   
/*    */   public String getCaller() {
/* 37 */     return this.caller;
/*    */   }
/*    */   public void setCaller(String caller) {
/* 40 */     this.caller = caller;
/*    */   }
/*    */   public int getNoOfCalls() {
/* 43 */     return this.noOfCalls;
/*    */   }
/*    */   public void setNoOfCalls(int noOfCalls) {
/* 46 */     this.noOfCalls = noOfCalls;
/*    */   }
/*    */   
/*    */   public String getReturnType() {
/* 50 */     return this.returnType;
/*    */   }
/*    */   
/*    */   public void setReturnType(String returnType) {
/* 54 */     this.returnType = returnType;
/*    */   }
/*    */   
/*    */   public int getStartLineNumber() {
/* 58 */     return this.startLineNumber;
/*    */   }
/*    */   
/*    */   public void setStartLineNumber(int startLineNumber) {
/* 62 */     this.startLineNumber = startLineNumber;
/*    */   }
/*    */   
/*    */   public int getEndLineNumber() {
/* 66 */     return this.endLineNumber;
/*    */   }
/*    */   
/*    */   public void setEndLineNumber(int endLineNumber) {
/* 70 */     this.endLineNumber = endLineNumber;
/*    */   }
/*    */   
/*    */   public int getLength() {
/* 74 */     return this.length;
/*    */   }
/*    */   
/*    */   public void setLength(int length) {
/* 78 */     this.length = length;
/*    */   }
/*    */ }


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/Util/InvocationInformation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
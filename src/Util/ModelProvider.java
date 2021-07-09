/*    */ package Util;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public enum ModelProvider {
/*  7 */   INSTANCE;
/*    */   
/*    */   ModelProvider() {
/* 10 */     this.results = new ArrayList<>();
/*    */   }
/*    */   private List<ExtractMethodResults> results;
/*    */   public List<ExtractMethodResults> getResults() {
/* 14 */     return this.results;
/*    */   }
/*    */   
/*    */   public void setResults(List<ExtractMethodResults> results) {
/* 18 */     this.results = results;
/*    */   }
/*    */ }


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/Util/ModelProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package gumtree.spoon.diff.operations;
/*    */ 
/*    */ import com.github.gumtreediff.actions.model.Action;
/*    */ import spoon.reflect.declaration.CtElement;
/*    */ 
/*    */ public abstract class Operation<T extends Action>
/*    */ {
/*    */   private final CtElement node;
/*    */   private final T action;
/*    */   
/*    */   public Operation(T action) {
/* 12 */     this.action = action;
/* 13 */     this.node = (CtElement)action.getNode().getMetadata("spoon_object");
/*    */   }
/*    */   
/*    */   public CtElement getNode() {
/* 17 */     return this.node;
/*    */   }
/*    */   
/*    */   public T getAction() {
/* 21 */     return this.action;
/*    */   }
/*    */ }


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/lib/gumtree/spoon/diff/operations/Operation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
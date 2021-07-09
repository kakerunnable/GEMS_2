/*    */ package gumtree.spoon.diff.operations;
/*    */ 
/*    */ import com.github.gumtreediff.actions.model.Action;
/*    */ import com.github.gumtreediff.actions.model.Addition;
/*    */ import gumtree.spoon.diff.operations.Operation;
/*    */ import spoon.reflect.declaration.CtElement;
/*    */ 
/*    */ abstract class AdditionOperation<T extends Addition> extends Operation<T> {
/*    */   private final CtElement parent;
/*    */   
/*    */   AdditionOperation(T action) {
/* 12 */     super(action);
/* 13 */     this.position = action.getPosition();
/* 14 */     this.parent = (CtElement)action.getParent().getMetadata("spoon_object");
/*    */   }
/*    */   private final int position;
/*    */   public int getPosition() {
/* 18 */     return this.position;
/*    */   }
/*    */   
/*    */   public CtElement getParent() {
/* 22 */     return this.parent;
/*    */   }
/*    */ }


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/lib/gumtree/spoon/diff/operations/AdditionOperation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
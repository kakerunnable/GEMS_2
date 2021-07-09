/*    */ package gumtree.spoon.diff.operations;
/*    */ 
/*    */ import com.github.gumtreediff.actions.model.Action;
/*    */ import com.github.gumtreediff.actions.model.Update;
/*    */ import gumtree.spoon.diff.operations.Operation;
/*    */ import spoon.reflect.declaration.CtElement;
/*    */ 
/*    */ public class UpdateOperation
/*    */   extends Operation<Update> {
/*    */   public UpdateOperation(Update action) {
/* 11 */     super(action);
/* 12 */     this.destElement = (CtElement)action.getNode().getMetadata("spoon_object_dest");
/*    */   }
/*    */   private final CtElement destElement;
/*    */   public CtElement getDestElement() {
/* 16 */     return this.destElement;
/*    */   }
/*    */ }


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/lib/gumtree/spoon/diff/operations/UpdateOperation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
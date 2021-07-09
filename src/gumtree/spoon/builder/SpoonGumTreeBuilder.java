/*    */ package gumtree.spoon.builder;
/*    */ 
/*    */ import com.github.gumtreediff.tree.ITree;
/*    */ import com.github.gumtreediff.tree.TreeContext;
/*    */ import com.github.gumtreediff.tree.TreeUtils;
/*    */ import gumtree.spoon.builder.TreeScanner;
/*    */ import spoon.reflect.declaration.CtElement;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SpoonGumTreeBuilder
/*    */ {
/*    */   public static final String SPOON_OBJECT = "spoon_object";
/*    */   public static final String SPOON_OBJECT_DEST = "spoon_object_dest";
/* 34 */   private final TreeContext treeContext = new TreeContext();
/*    */   
/*    */   public ITree getTree(CtElement element) {
/* 37 */     ITree root = this.treeContext.createTree(-1, "", "root");
/* 38 */     (new TreeScanner(this.treeContext, root)).scan(element);
/*    */     
/* 40 */     root.refresh();
/* 41 */     TreeUtils.postOrderNumbering(root);
/* 42 */     TreeUtils.computeHeight(root);
/*    */     
/* 44 */     return root;
/*    */   }
/*    */   
/*    */   public TreeContext getTreeContext() {
/* 48 */     return this.treeContext;
/*    */   }
/*    */ }


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/lib/gumtree/spoon/builder/SpoonGumTreeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
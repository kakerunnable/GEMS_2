/*    */ package gumtree.spoon.builder;
/*    */ 
/*    */ import com.github.gumtreediff.tree.ITree;
/*    */ import com.github.gumtreediff.tree.TreeContext;
/*    */ import gumtree.spoon.builder.NodeCreator;
/*    */ import java.util.Stack;
/*    */ import spoon.reflect.declaration.CtElement;
/*    */ import spoon.reflect.declaration.CtModifiable;
/*    */ import spoon.reflect.declaration.CtTypedElement;
/*    */ import spoon.reflect.declaration.ModifierKind;
/*    */ import spoon.reflect.visitor.CtScanner;
/*    */ 
/*    */ 
/*    */ 
/*    */ class TreeScanner
/*    */   extends CtScanner
/*    */ {
/*    */   private final TreeContext treeContext;
/* 19 */   private final Stack<ITree> nodes = new Stack<>();
/*    */   
/*    */   TreeScanner(TreeContext treeContext, ITree root) {
/* 22 */     this.treeContext = treeContext;
/* 23 */     this.nodes.push(root);
/*    */   }
/*    */ 
/*    */   
/*    */   public void enter(CtElement element) {
/* 28 */     if (element instanceof spoon.reflect.reference.CtReference) {
/* 29 */       this.nodes.push((ITree)null);
/*    */       
/*    */       return;
/*    */     } 
/* 33 */     int size = this.nodes.size();
/* 34 */     (new NodeCreator(this)).scan(element);
/* 35 */     if (size == this.nodes.size()) {
/* 36 */       addNodeToTree(createNode(element, ""));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void exit(CtElement element) {
/* 42 */     this.nodes.pop();
/* 43 */     super.exit(element);
/*    */   }
/*    */   
/*    */   void addNodeToTree(ITree node) {
/* 47 */     ITree parent = this.nodes.peek();
/* 48 */     if (parent != null) {
/* 49 */       parent.addChild(node);
/*    */     }
/* 51 */     this.nodes.push(node);
/*    */   }
/*    */   
/*    */   ITree createNode(CtElement element, String label) {
/* 55 */     ITree newNode = createNode(getTypeName(element.getClass().getSimpleName()), label);
/*    */     
/* 57 */     if (element instanceof CtModifiable) {
/* 58 */       addModifiers((CtModifiable)element, newNode);
/*    */     }
/*    */ 
/*    */     
/* 62 */     if (element instanceof spoon.reflect.declaration.CtParameter || element instanceof spoon.reflect.declaration.CtField || element instanceof spoon.reflect.code.CtLocalVariable) {
/* 63 */       addStaticTypeNode((CtTypedElement)element, newNode);
/*    */     }
/*    */     
/* 66 */     newNode.setMetadata("spoon_object", element);
/* 67 */     return newNode;
/*    */   }
/*    */ 
/*    */   
/*    */   private String getTypeName(String simpleName) {
/* 72 */     return simpleName.substring(2, simpleName.length() - 4);
/*    */   }
/*    */   
/*    */   private void addStaticTypeNode(CtTypedElement obj, ITree node) {
/* 76 */     ITree modifier = createNode("StaticType", "");
/* 77 */     modifier.setMetadata("spoon_object", obj);
/* 78 */     modifier.setLabel(obj.getType().getQualifiedName());
/* 79 */     node.addChild(modifier);
/*    */   }
/*    */   
/*    */   private void addModifiers(CtModifiable obj, ITree node) {
/* 83 */     ITree modifiers = createNode("Modifiers", "");
/* 84 */     for (ModifierKind kind : obj.getModifiers()) {
/* 85 */       ITree modifier = createNode("Modifier", kind.toString());
/* 86 */       modifier.setMetadata("spoon_object", obj);
/* 87 */       modifiers.addChild(modifier);
/*    */     } 
/* 89 */     node.addChild(modifiers);
/*    */   }
/*    */   
/*    */   private ITree createNode(String typeClass, String label) {
/* 93 */     return this.treeContext.createTree(typeClass.hashCode(), label, typeClass);
/*    */   }
/*    */ }


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/lib/gumtree/spoon/builder/TreeScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
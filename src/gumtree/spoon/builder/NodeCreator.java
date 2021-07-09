/*    */ package gumtree.spoon.builder;
/*    */ 
/*    */ import gumtree.spoon.builder.TreeScanner;
/*    */ import spoon.reflect.code.CtArrayAccess;
/*    */ import spoon.reflect.code.CtBinaryOperator;
/*    */ import spoon.reflect.code.CtConstructorCall;
/*    */ import spoon.reflect.code.CtInvocation;
/*    */ import spoon.reflect.code.CtLiteral;
/*    */ import spoon.reflect.code.CtThisAccess;
/*    */ import spoon.reflect.code.CtTypeAccess;
/*    */ import spoon.reflect.code.CtUnaryOperator;
/*    */ import spoon.reflect.code.CtVariableAccess;
/*    */ import spoon.reflect.declaration.CtElement;
/*    */ import spoon.reflect.declaration.CtNamedElement;
/*    */ import spoon.reflect.visitor.CtInheritanceScanner;
/*    */ 
/*    */ class NodeCreator
/*    */   extends CtInheritanceScanner {
/*    */   NodeCreator(TreeScanner builder) {
/* 20 */     this.builder = builder;
/*    */   }
/*    */   private final TreeScanner builder;
/*    */   
/*    */   public void scanCtNamedElement(CtNamedElement e) {
/* 25 */     this.builder.addNodeToTree(this.builder.createNode((CtElement)e, e.getSimpleName()));
/*    */   }
/*    */ 
/*    */   
/*    */   public <T, E extends spoon.reflect.code.CtExpression<?>> void scanCtArrayAccess(CtArrayAccess<T, E> arrayAccess) {
/* 30 */     this.builder.addNodeToTree(this.builder.createNode((CtElement)arrayAccess, arrayAccess.toString()));
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> void scanCtVariableAccess(CtVariableAccess<T> variableAccess) {
/* 35 */     if (variableAccess.getVariable() != null) {
/* 36 */       this.builder.addNodeToTree(this.builder.createNode((CtElement)variableAccess, variableAccess.getVariable().getSimpleName()));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> void visitCtInvocation(CtInvocation<T> invocation) {
/* 42 */     if (invocation.getExecutable() != null) {
/* 43 */       this.builder.addNodeToTree(this.builder.createNode((CtElement)invocation, invocation.getExecutable().getSignature()));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> void visitCtConstructorCall(CtConstructorCall<T> ctConstructorCall) {
/* 49 */     if (ctConstructorCall.getType() != null) {
/* 50 */       this.builder.addNodeToTree(this.builder.createNode((CtElement)ctConstructorCall, ctConstructorCall.getExecutable().getSignature()));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> void visitCtLiteral(CtLiteral<T> literal) {
/* 56 */     this.builder.addNodeToTree(this.builder.createNode((CtElement)literal, literal.toString()));
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> void visitCtBinaryOperator(CtBinaryOperator<T> operator) {
/* 61 */     this.builder.addNodeToTree(this.builder.createNode((CtElement)operator, operator.getKind().toString()));
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> void visitCtUnaryOperator(CtUnaryOperator<T> operator) {
/* 66 */     this.builder.addNodeToTree(this.builder.createNode((CtElement)operator, operator.getKind().toString()));
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> void visitCtThisAccess(CtThisAccess<T> thisAccess) {
/* 71 */     this.builder.addNodeToTree(this.builder.createNode((CtElement)thisAccess, thisAccess.toString()));
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> void visitCtTypeAccess(CtTypeAccess<T> typeAccess) {
/* 76 */     this.builder.addNodeToTree(this.builder.createNode((CtElement)typeAccess, typeAccess.getSignature()));
/*    */   }
/*    */ }


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/lib/gumtree/spoon/builder/NodeCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
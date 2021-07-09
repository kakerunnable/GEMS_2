/*     */ package gumtree.spoon;
/*     */ 
/*     */ import gumtree.spoon.builder.SpoonGumTreeBuilder;
/*     */ import gumtree.spoon.diff.Diff;
/*     */ import gumtree.spoon.diff.DiffImpl;
/*     */ import java.io.File;
/*     */ import java.util.List;
/*     */ import spoon.compiler.Environment;
/*     */ import spoon.compiler.SpoonResource;
/*     */ import spoon.compiler.SpoonResourceHelper;
/*     */ import spoon.reflect.declaration.CtElement;
/*     */ import spoon.reflect.declaration.CtType;
/*     */ import spoon.reflect.factory.CoreFactory;
/*     */ import spoon.reflect.factory.Factory;
/*     */ import spoon.reflect.factory.FactoryImpl;
/*     */ import spoon.support.DefaultCoreFactory;
/*     */ import spoon.support.StandardEnvironment;
/*     */ import spoon.support.compiler.VirtualFile;
/*     */ import spoon.support.compiler.jdt.JDTBasedSpoonCompiler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AstComparator
/*     */ {
/*     */   private final Factory factory;
/*     */   
/*     */   static {
/*  38 */     System.setProperty("gumtree.match.bu.sim", "0.3");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  43 */     System.setProperty("gumtree.match.gt.minh", "1");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  51 */     System.setProperty("gumtree.match.bu.size", "1000");
/*     */   }
/*     */   
/*     */   public AstComparator() {
/*  55 */     this((Factory)new FactoryImpl((CoreFactory)new DefaultCoreFactory(), (Environment)new StandardEnvironment()));
/*     */   }
/*     */   
/*     */   public AstComparator(Factory factory) {
/*  59 */     this.factory = factory;
/*  60 */     factory.getEnvironment().setNoClasspath(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public Diff compare(File f1, File f2, String Extracted_Mtd, String Src_Mtd, int position, String path) throws Exception {
/*  65 */     return compare((CtElement)getCtType(f1, position), (CtElement)getCtType(f2, position), Extracted_Mtd, Src_Mtd, position, path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Diff compare(String left, String right, String Extracted_Mtd, String Src_Mtd, int position, String path) {
/*  72 */     return compare((CtElement)getCtType(left, position), (CtElement)getCtType(right, position), Extracted_Mtd, Src_Mtd, position, path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Diff compare(CtElement left, CtElement right, String Extracted_Mtd, String Src_Mtd, int position, String path) {
/*  80 */     SpoonGumTreeBuilder scanner = new SpoonGumTreeBuilder();
/*  81 */     return (Diff)new DiffImpl(scanner.getTreeContext(), scanner.getTree(left), Src_Mtd, position, path);
/*     */   }
/*     */   
/*     */   private CtType getCtType(File file, int position) throws Exception {
/*  85 */     JDTBasedSpoonCompiler jDTBasedSpoonCompiler = new JDTBasedSpoonCompiler(this.factory);
/*  86 */     jDTBasedSpoonCompiler.getFactory().getEnvironment().setLevel("OFF");
/*  87 */     jDTBasedSpoonCompiler.addInputSource(SpoonResourceHelper.createResource(file));
/*  88 */     jDTBasedSpoonCompiler.build();
/*     */     
/*  90 */     if (this.factory.Type().getAll().size() == 0) {
/*  91 */       return null;
/*     */     }
/*  93 */     List<CtType<?>> types = this.factory.Type().getAll();
/*  94 */     int size = types.size();
/*  95 */     int i = 0;
/*  96 */     for (; i < size; i++) {
/*  97 */       CtType<?> temp = types.get(i);
/*  98 */       if (temp.getPosition().getLine() < position && temp.getPosition().getEndLine() > position) {
/*  99 */         return temp;
/*     */       }
/*     */     } 
/* 102 */     return this.factory.Type().getAll().get(0);
/*     */   }
/*     */   
/*     */   private CtType<?> getCtType(String content, int position) {
/* 106 */     JDTBasedSpoonCompiler jDTBasedSpoonCompiler = new JDTBasedSpoonCompiler(this.factory);
/* 107 */     jDTBasedSpoonCompiler.addInputSource((SpoonResource)new VirtualFile(content, "/test"));
/* 108 */     jDTBasedSpoonCompiler.build();
/* 109 */     List<CtType<?>> types = this.factory.Type().getAll();
/* 110 */     int size = types.size();
/* 111 */     int i = 0;
/* 112 */     for (; i < size; i++) {
/* 113 */       CtType<?> temp = types.get(i);
/* 114 */       if (temp.getPosition().getLine() < position && temp.getPosition().getEndLine() > position) {
/*     */         break;
/*     */       }
/*     */     } 
/* 118 */     return this.factory.Type().getAll().get(i);
/*     */   }
/*     */   
/*     */   public Diff compare(String file_path, String method_name, int position, String path) throws Exception {
/* 122 */     Diff result = compare(new File(file_path), new File(file_path), method_name, method_name, position, path);
/* 123 */     System.out.println(result.toString());
/* 124 */     return result;
/*     */   }
/*     */ }


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/lib/gumtree/spoon/AstComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
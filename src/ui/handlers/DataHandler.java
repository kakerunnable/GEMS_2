/*     */ package ui.handlers;
/*     */ 
/*     */ import Util.InvocationInformation;
/*     */ import Util.TextRangeUtil;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.eclipse.core.commands.AbstractHandler;
/*     */ import org.eclipse.core.commands.ExecutionEvent;
/*     */ import org.eclipse.core.commands.ExecutionException;
/*     */ import org.eclipse.core.resources.IFile;
/*     */ import org.eclipse.core.resources.IProject;
/*     */ import org.eclipse.core.resources.IResource;
/*     */ import org.eclipse.core.resources.IResourceVisitor;
/*     */ import org.eclipse.core.resources.IWorkspace;
/*     */ import org.eclipse.core.resources.IWorkspaceRoot;
/*     */ import org.eclipse.core.resources.ResourcesPlugin;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.core.runtime.IPath;
/*     */ import org.eclipse.core.runtime.IProgressMonitor;
/*     */ import org.eclipse.core.runtime.NullProgressMonitor;
/*     */ import org.eclipse.core.runtime.Path;
/*     */ import org.eclipse.jdt.core.ICompilationUnit;
/*     */ import org.eclipse.jdt.core.ISourceRange;
/*     */ import org.eclipse.jdt.core.ITypeRoot;
/*     */ import org.eclipse.jdt.core.JavaCore;
/*     */ import org.eclipse.jdt.core.dom.ASTParser;
/*     */ import org.eclipse.jdt.core.dom.ASTVisitor;
/*     */ import org.eclipse.jdt.core.dom.Block;
/*     */ import org.eclipse.jdt.core.dom.CompilationUnit;
/*     */ import org.eclipse.jdt.core.dom.MethodDeclaration;
/*     */ import org.eclipse.jdt.core.dom.MethodInvocation;
/*     */ import org.eclipse.jdt.internal.corext.refactoring.code.InlineMethodRefactoring;
/*     */ import org.eclipse.jdt.internal.corext.refactoring.util.RefactoringASTParser;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.handlers.HandlerUtil;
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
/*     */ 
/*     */ 
/*     */ public class DataHandler
/*     */   extends AbstractHandler
/*     */ {
/*     */   public static Hashtable<String, Integer> methodTable;
/*     */   public static Hashtable<String, List<InvocationInformation>> invocationTable;
/*     */   public String path;
/*     */   
/*     */   public Object execute(ExecutionEvent event) throws ExecutionException {
/*  68 */     IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
/*     */     
/*  70 */     (new String[12])[0] = "android_after"; (new String[12])[1] = "antlr4_after"; (new String[12])[2] = "deeplearning4j_after"; (new String[12])[3] = "elasticsearch_after"; (new String[12])[4] = "guava_after"; (new String[12])[5] = "intellij-community_after"; (new String[12])[6] = "MapDB_after"; (new String[12])[7] = 
/*  71 */       "mockito_after"; (new String[12])[8] = "buck_after"; (new String[12])[9] = "presto_after"; (new String[12])[10] = "facebook-android-sdk_after"; (new String[12])[11] = "RxJava_after"; //new String[12];
/*  72 */     this.path = "/Users/Aish/Documents/RefactoringDataset/";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     inlineMethod(String.valueOf(this.path) + "src/deeplearning4j_after/deeplearning4j-core/src/main/java/org/deeplearning4j/api/storage/impl/RemoteUIStatsStorageRouter.java", 46, 1);
/*  80 */     MessageDialog.openInformation(
/*  81 */         window.getShell(), 
/*  82 */         "UI", 
/*  83 */         "Generating Data complete");
/*  84 */     return null;
/*     */   }
/*     */   
/*     */   public void getAllProjects() {
/*  88 */     IWorkspace workspace = ResourcesPlugin.getWorkspace();
/*  89 */     IWorkspaceRoot root = workspace.getRoot();
/*  90 */     IProject[] projects = root.getProjects();
/*     */     try {
/*  92 */       projects[0].accept(new IResourceVisitor()
/*     */           {
/*     */ 
/*     */             
/*     */             public boolean visit(IResource resource) throws CoreException
/*     */             {
/*  98 */               if (resource.getName().endsWith(".java"))
/*     */               {
/* 100 */                 DataHandler.this.getMethodsInFile(resource.getLocation().toOSString(), resource.getFullPath().toOSString());
/*     */               }
/*     */               
/* 103 */               return true;
/*     */             }
/*     */           });
/*     */     
/*     */     }
/* 108 */     catch (CoreException e) {
/*     */       
/* 110 */       e.printStackTrace();
/*     */     }  } public void getAllFilesInFolder(File folder) {
/*     */     byte b;
/*     */     int i;
/*     */     File[] arrayOfFile;
/* 115 */     for (i = (arrayOfFile = folder.listFiles()).length, b = 0; b < i; ) { File fileEntry = arrayOfFile[b];
/* 116 */       if (fileEntry.isDirectory()) {
/* 117 */         getAllFilesInFolder(fileEntry);
/*     */       }
/* 119 */       else if (fileEntry.getName().endsWith(".java")) {
/* 120 */         getMethodsInFile(fileEntry.getAbsolutePath(), fileEntry.getName());
/*     */       } 
/*     */       b++; }
/*     */   
/*     */   }
/*     */   
/*     */   public void getMethodsInFile(String filePath, String relativePath) {
/* 127 */     System.out.println("In file " + relativePath);
/* 128 */     ASTParser parser = ASTParser.newParser(8);
/*     */     
/* 130 */     methodTable = new Hashtable<>();
/* 131 */     invocationTable = new Hashtable<>();
/*     */     try {
/* 133 */       String source = new String(Files.readAllBytes(Paths.get(filePath, new String[0])));
/* 134 */       parser.setSource(source.toCharArray());
/* 135 */       final CompilationUnit cu = (CompilationUnit)parser.createAST(null);
/* 136 */       cu.getAST();
/*     */       
/* 138 */       cu.accept(new ASTVisitor()
/*     */           {
/*     */             public boolean visit(MethodDeclaration node) {
/*     */               try {
/* 142 */                 Block body = node.getBody();
/* 143 */                 if (body != null && body.statements() != null) {
/* 144 */                   DataHandler.methodTable.put(node.getName().toString(), Integer.valueOf(body.statements().size()));
/*     */                 } else {
/* 146 */                   DataHandler.methodTable.put(node.getName().toString(), Integer.valueOf(0));
/*     */                 } 
/* 148 */               } catch (Exception e) {
/* 149 */                 System.out.println("The error is " + e.getMessage());
/*     */               } 
/*     */               
/* 152 */               return super.visit(node);
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void endVisit(MethodDeclaration node) {}
/*     */           });
/* 161 */       cu.accept(new ASTVisitor() {
/* 162 */             String activeMethod = "";
/*     */             
/*     */             public boolean visit(MethodDeclaration node) {
/* 165 */               this.activeMethod = node.getName().toString();
/* 166 */               return super.visit(node);
/*     */             }
/*     */             
/*     */             public void endVisit(MethodDeclaration node) {
/* 170 */               this.activeMethod = "";
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean visit(MethodInvocation node) {
/* 175 */               String callee = node.getName().toString();
/*     */               
/* 177 */               int startLineNumber = node.getStartPosition();
/* 178 */               int nodeLength = node.getLength();
/* 179 */               int endLineNumber = cu.getLineNumber(node.getStartPosition() + nodeLength) - 1;
/* 180 */               if (DataHandler.methodTable.containsKey(callee) && DataHandler.methodTable.containsKey(this.activeMethod)) {
/* 181 */                 System.out.println("The call is frm the method " + this.activeMethod + " invocation is " + node);
/* 182 */                 double ratioOfStatements = ((Integer)DataHandler.methodTable.get(this.activeMethod)).intValue() * 1.0D / ((Integer)DataHandler.methodTable.get(callee)).intValue();
/* 183 */                 if (((Integer)DataHandler.methodTable.get(callee)).intValue() >= 3 && ((Integer)DataHandler.methodTable.get(this.activeMethod)).intValue() >= 4 && ratioOfStatements > 0.5D) {
/* 184 */                   if (!DataHandler.invocationTable.containsKey(callee)) {
/* 185 */                     InvocationInformation invo = new InvocationInformation(this.activeMethod, 1, startLineNumber, endLineNumber, nodeLength);
/* 186 */                     List<InvocationInformation> list = new ArrayList<>();
/* 187 */                     list.add(invo);
/* 188 */                     DataHandler.invocationTable.put(callee, list);
/*     */                   } else {
/* 190 */                     List<InvocationInformation> list = DataHandler.invocationTable.get(callee);
/* 191 */                     boolean found = false;
/* 192 */                     for (int i = 0; i < list.size(); i++) {
/* 193 */                       if (((InvocationInformation)list.get(i)).getCaller().equals(this.activeMethod)) {
/* 194 */                         int currentNo = ((InvocationInformation)((List<InvocationInformation>)DataHandler.invocationTable.get(callee)).get(i)).getNoOfCalls() + 1;
/* 195 */                         ((InvocationInformation)((List<InvocationInformation>)DataHandler.invocationTable.get(callee)).get(i)).setNoOfCalls(currentNo);
/* 196 */                         found = true;
/*     */                         break;
/*     */                       } 
/*     */                     } 
/* 200 */                     if (!found) {
/* 201 */                       InvocationInformation invo = new InvocationInformation(this.activeMethod, 1, startLineNumber, endLineNumber, nodeLength);
/* 202 */                       List<InvocationInformation> invoList = DataHandler.invocationTable.get(callee);
/* 203 */                       invoList.add(invo);
/* 204 */                       DataHandler.invocationTable.put(callee, invoList);
/*     */                     } 
/*     */                   } 
/*     */                 }
/*     */               } 
/* 209 */               return super.visit(node);
/*     */             }
/*     */           });
/*     */ 
/*     */       
/* 214 */       printToFile(filePath);
/*     */ 
/*     */     
/*     */     }
/* 218 */     catch (IOException e) {
/*     */       
/* 220 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void printToFile(String filePath) {
/* 228 */     FileWriter fw = null;
/*     */     try {
/* 230 */       fw = new FileWriter(String.valueOf(this.path) + "oracle.txt", true);
/* 231 */       Set<String> keys = invocationTable.keySet();
/* 232 */       Iterator<String> itr = keys.iterator();
/* 233 */       while (itr.hasNext()) {
/* 234 */         String str = itr.next();
/* 235 */         List<InvocationInformation> invoList = invocationTable.get(str);
/* 236 */         for (int i = 0; i < invoList.size(); i++) {
/* 237 */           if (invoList.size() < 3 && ((InvocationInformation)invoList.get(i)).getNoOfCalls() < 3 && 
/* 238 */             !((InvocationInformation)invoList.get(i)).getCaller().equals(str)) {
/* 239 */             String print = String.valueOf(filePath.split(this.path)[1]) + "," + ((InvocationInformation)invoList.get(i)).getCaller() + "," + str + "," + (
/* 240 */               (InvocationInformation)invoList.get(i)).getStartLineNumber() + "," + ((InvocationInformation)invoList.get(i)).getEndLineNumber() + "," + (
/* 241 */               (InvocationInformation)invoList.get(i)).getLength() + "\n";
/* 242 */             if (!filePath.toUpperCase().contains("TEST")) {
/* 243 */               fw.write(print);
/*     */             }
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */     
/* 251 */     } catch (IOException e) {
/*     */       
/* 253 */       e.printStackTrace();
/*     */     } finally {
/*     */       try {
/* 256 */         fw.close();
/* 257 */       } catch (IOException e) {
/*     */         
/* 259 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void inlineMethod(String filePath, int startLineNumber, int length) {
/* 268 */     IWorkspace workspace = ResourcesPlugin.getWorkspace();
/* 269 */     IWorkspaceRoot root = workspace.getRoot();
/* 270 */     IProject[] projects = root.getProjects();
/* 271 */     JavaCore.create(projects[0]);
/* 272 */     String pathString = "/Users/Aish/Documents/workspace/hello/src/hello/Hello.java";
/* 273 */     IPath iPath = Path.fromOSString(pathString);
/* 274 */     IFile file = workspace.getRoot().getFileForLocation(iPath);
/* 275 */     ICompilationUnit compilationUnit = (ICompilationUnit)JavaCore.create(file);
/*     */     try {
/* 277 */       String source = new String(Files.readAllBytes(Paths.get(pathString, new String[0])));
/* 278 */       ASTParser parser = ASTParser.newParser(8);
/* 279 */       parser.setResolveBindings(true);
/* 280 */       parser.setSource(source.toCharArray());
/* 281 */       //(CompilationUnit)parser.createAST(null);
/* 282 */       compilationUnit.open(null);
/* 283 */       ISourceRange expected = TextRangeUtil.getSelection(compilationUnit, 18, 0, 18, 0);
/* 284 */       InlineMethodRefactoring im = InlineMethodRefactoring.create((ITypeRoot)compilationUnit, (new RefactoringASTParser(8)).parse((ITypeRoot)compilationUnit, true), expected.getOffset(), expected.getLength());
/* 285 */       NullProgressMonitor nullProgressMonitor = new NullProgressMonitor();
/* 286 */       im.checkInitialConditions((IProgressMonitor)nullProgressMonitor);
/* 287 */       im.checkFinalConditions((IProgressMonitor)nullProgressMonitor);
/* 288 */     } catch (Exception e) {
/*     */       
/* 290 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void printNegativeSamplesToFile(String filePath) {
/* 297 */     FileWriter fw = null;
/*     */     try {
/* 299 */       fw = new FileWriter(String.valueOf(this.path) + "NegativeOracle.txt", true);
/* 300 */       Set<String> keys = methodTable.keySet();
/* 301 */       Iterator<String> itr = keys.iterator();
/* 302 */       while (itr.hasNext()) {
/* 303 */         String str = itr.next();
/* 304 */         if (((Integer)methodTable.get(str)).intValue() >= 4) {
/* 305 */           String print = String.valueOf(filePath.split(this.path)[1]) + "," + str + "\n";
/* 306 */           fw.write(print);
/*     */         } 
/*     */       } 
/* 309 */     } catch (IOException e) {
/*     */       
/* 311 */       e.printStackTrace();
/*     */     } finally {
/*     */       try {
/* 314 */         fw.close();
/* 315 */       } catch (IOException e) {
/*     */         
/* 317 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/ui/handlers/DataHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
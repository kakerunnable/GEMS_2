/*     */ package ui.handlers;
/*     */ 
/*     */ import Util.Candidates;
/*     */ import Util.TextRangeUtil;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.eclipse.core.commands.AbstractHandler;
/*     */ import org.eclipse.core.commands.ExecutionEvent;
/*     */ import org.eclipse.core.commands.ExecutionException;
/*     */ import org.eclipse.core.resources.IFile;
/*     */ import org.eclipse.core.resources.IProject;
/*     */ import org.eclipse.core.resources.IWorkspace;
/*     */ import org.eclipse.core.resources.IWorkspaceRoot;
/*     */ import org.eclipse.core.resources.ResourcesPlugin;
/*     */ import org.eclipse.core.runtime.IPath;
/*     */ import org.eclipse.core.runtime.IProgressMonitor;
/*     */ import org.eclipse.core.runtime.NullProgressMonitor;
/*     */ import org.eclipse.core.runtime.Path;
/*     */ import org.eclipse.jdt.core.ICompilationUnit;
/*     */ import org.eclipse.jdt.core.IJavaProject;
/*     */ import org.eclipse.jdt.core.ISourceRange;
/*     */ import org.eclipse.jdt.core.JavaCore;
/*     */ import org.eclipse.jdt.core.JavaModelException;
/*     */ import org.eclipse.jdt.core.dom.ASTParser;
/*     */ import org.eclipse.jdt.core.dom.CompilationUnit;
/*     */ import org.eclipse.jdt.internal.corext.refactoring.code.ExtractMethodRefactoring;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.ltk.core.refactoring.RefactoringStatus;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RefactoringHandler
/*     */   extends AbstractHandler
/*     */ {
/*     */   private String PATH;
/*     */   String fileName;
/*     */   String PROJECTNAME;
/*     */   String filePath;
/*     */   
/*     */   public Object execute(ExecutionEvent event) throws ExecutionException {
/*  68 */     this.PATH = "/Users/Aish/Downloads/";
/*  69 */     this.fileName = "junit";
/*  70 */     this.filePath = "CH/ifa/draw/util/";
/*     */ 
/*     */ 
/*     */     
/*  74 */     this.PROJECTNAME = "wikidev-filters/src/";
/*     */     
/*  76 */     readCandidates(String.valueOf(this.PATH) + "wikidev" + ".csv");
/*     */     
/*  78 */     IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
/*  79 */     MessageDialog.openInformation(
/*  80 */         window.getShell(), 
/*  81 */         "UI", 
/*  82 */         "Refactoring check complete");
/*  83 */     return null;
/*     */   }
/*     */   
/*     */   public void readCandidates(String fileName) {
/*  87 */     BufferedReader br = null;
/*  88 */     String line = "";
/*  89 */     int count = 0;
/*  90 */     ArrayList<Candidates> candidates = new ArrayList<>();
/*     */     try {
/*  92 */       Candidates cand = new Candidates();
/*  93 */       br = new BufferedReader(new FileReader(fileName));
/*  94 */       while ((line = br.readLine()) != null) {
/*  95 */         if (count > 0) {
/*  96 */           String[] rowData = line.split(",");
/*  97 */           String projectPath = String.valueOf(this.PATH) + this.PROJECTNAME;
/*  98 */           String filePath = rowData[0];
/*  99 */           if (candidates.size() > 0 && ((Candidates)candidates.get(candidates.size() - 1)).getProjectPath().equals(projectPath) && (
/* 100 */             (Candidates)candidates.get(candidates.size() - 1)).getFilePath().equals(filePath)) {
/*     */             
/* 102 */             int startIndex = Integer.parseInt(rowData[2]);
/* 103 */             int endIndex = Integer.parseInt(rowData[4]);
/* 104 */             ((Candidates)candidates.get(candidates.size() - 1)).appendStartIndex(startIndex);
/* 105 */             ((Candidates)candidates.get(candidates.size() - 1)).appendEndIndex(endIndex);
/*     */           } else {
/*     */             
/* 108 */             cand = new Candidates(projectPath, rowData);
/* 109 */             candidates.add(cand);
/* 110 */             System.out.print("new project beg");
/*     */           } 
/* 112 */           System.out.println("Start Number " + rowData[2] + " End Number " + rowData[4]);
/*     */         } 
/* 114 */         count++;
/* 115 */         System.out.println(candidates.size());
/*     */       } 
/* 117 */       refactor(candidates);
/* 118 */     } catch (FileNotFoundException e) {
/*     */       
/* 120 */       e.printStackTrace();
/* 121 */     } catch (IOException e) {
/*     */       
/* 123 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void refactor(List<Candidates> candidates) {
/* 128 */     IWorkspace workspace = ResourcesPlugin.getWorkspace();
/* 129 */     IWorkspaceRoot root = workspace.getRoot();
/* 130 */     IProject[] projects = root.getProjects();
/* 131 */     IJavaProject javaP = JavaCore.create(projects[0]);
/*     */     
/*     */     try {
/* 134 */       javaP.getPackageFragments();
/* 135 */     } catch (JavaModelException e) {
/*     */       
/* 137 */       e.printStackTrace();
/*     */     } 
/*     */     
/* 140 */     projects[0].getFolder("src");
/*     */     
/*     */     try {
/* 143 */       for (int j = 0; j < candidates.size(); j++) {
/* 144 */         Candidates cand = candidates.get(j);
/* 145 */         IPath path = Path.fromOSString(String.valueOf(this.PATH) + this.PROJECTNAME + cand.getFilePath() + ".java");
/* 146 */         IFile file = workspace.getRoot().getFileForLocation(path);
/* 147 */         ICompilationUnit compilationUnit = (ICompilationUnit)JavaCore.create(file);
/* 148 */         System.out.println(path.toString());
/* 149 */         compilationUnit.open(null);
/* 150 */         for (int i = 0; i < cand.getStartIndex().size(); i++) {
/* 151 */           Boolean isExtractable = em(compilationUnit, ((Integer)cand.getStartIndex().get(i)).intValue(), ((Integer)cand.getEndIndex().get(i)).intValue());
/* 152 */           ((Candidates)candidates.get(j)).appendIsExtractable(isExtractable);
/*     */         } 
/*     */       } 
/* 155 */       writeToFile(candidates);
/* 156 */     } catch (JavaModelException e) {
/*     */       
/* 158 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeToFile(List<Candidates> candidates) {
/* 165 */     BufferedWriter bw = null;
/* 166 */     FileWriter fw = null;
/*     */     
/* 168 */     try { fw = new FileWriter(String.valueOf(this.PATH) + "extractable" + this.fileName + ".csv");
/* 169 */       bw = new BufferedWriter(fw);
/* 170 */       bw.write("FilePath,StartIndex,EndIndex,Extractable\n");
/* 171 */       for (int j = 0; j < candidates.size(); j++) {
/* 172 */         Candidates cand = candidates.get(j);
/* 173 */         for (int i = 0; i < cand.getStartIndex().size(); i++) {
/* 174 */           StringBuilder rowData = new StringBuilder(String.valueOf(cand.getFilePath()) + "," + Integer.toString(((Integer)cand.getStartIndex().get(i)).intValue()) + 
/* 175 */               "," + Integer.toString(((Integer)cand.getEndIndex().get(i)).intValue()) + "," + ((Boolean)cand.getIsExtractable().get(i)).toString() + "\n");
/* 176 */           System.out.println(rowData);
/*     */           
/* 178 */           bw.write(rowData.toString());
/*     */         } 
/*     */       }  }
/* 181 */     catch (IOException iOException)
/*     */     
/*     */     { 
/*     */       
/* 185 */       try { bw.close();
/* 186 */         fw.close(); }
/* 187 */       catch (IOException e)
/*     */       
/* 189 */       { e.printStackTrace(); }  } finally { try { bw.close(); fw.close(); } catch (IOException e) { e.printStackTrace(); }
/*     */        }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean em(ICompilationUnit unit, int startIndex, int endIndex) {
/*     */     try {
/* 201 */       ISourceRange expected = TextRangeUtil.getSelection(unit, startIndex, 0, endIndex, 0);
/* 202 */       ExtractMethodRefactoring refactoring = new ExtractMethodRefactoring(unit, expected.getOffset(), expected.getLength());
/* 203 */       refactoring.setMethodName("extracted");
/* 204 */       refactoring.setVisibility(0);
/*     */       
/* 206 */       RefactoringStatus status = refactoring.checkAllConditions((IProgressMonitor)new NullProgressMonitor());
/* 207 */       if (status.isOK()) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 212 */         System.out.println("Extractable for start_ " + startIndex + " end_ " + endIndex);
/* 213 */         return Boolean.valueOf(true);
/*     */       } 
/* 215 */       System.out.println("The end of em");
/* 216 */     } catch (Exception e1) {
/*     */       
/* 218 */       e1.printStackTrace();
/*     */     } 
/* 220 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFileForLineNumber(String filePath) {
/* 225 */     BufferedReader br = null;
/* 226 */     String line = "";
/* 227 */     int count = 0;
/*     */     try {
/* 229 */       br = new BufferedReader(new FileReader(filePath));
/* 230 */       while ((line = br.readLine()) != null) {
/* 231 */         if (count > 0) {
/* 232 */           System.out.println(line);
/* 233 */           System.out.println(count);
/* 234 */           String[] rowData = line.split(",");
/* 235 */           String javaFilePath = String.valueOf(this.PATH) + this.PROJECTNAME + rowData[0].replace('.', '/') + ".java";
/* 236 */           String startChar = rowData[1].split(":")[0].substring(1);
/* 237 */           String length = rowData[1].split(":")[1].split(";")[0];
/* 238 */           getLineNumber(rowData, javaFilePath, Integer.parseInt(startChar), Integer.parseInt(length));
/*     */         } 
/* 240 */         count++;
/*     */       } 
/* 242 */     } catch (FileNotFoundException e) {
/*     */       
/* 244 */       e.printStackTrace();
/* 245 */     } catch (IOException e) {
/*     */       
/* 247 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void getLineNumber(String[] rowData, String filePath, int position, int length) {
/* 252 */     ASTParser parser = ASTParser.newParser(8);
/*     */     
/*     */     try {
/* 255 */       String source = new String(Files.readAllBytes(Paths.get(filePath, new String[0])));
/* 256 */       parser.setSource(source.toCharArray());
/* 257 */       CompilationUnit cu = (CompilationUnit)parser.createAST(null);
/* 258 */       cu.getColumnNumber(position);
/* 259 */       int lineNo = cu.getLineNumber(position);
/* 260 */       int endLineNo = cu.getLineNumber(position + length);
/*     */       
/* 262 */       System.out.println("Start Line no: " + lineNo + " End Line no: " + endLineNo);
/* 263 */       writeLNtoFile(rowData, lineNo, endLineNo + 1);
/* 264 */     } catch (IOException e) {
/*     */       
/* 266 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeLNtoFile(String[] rowData, int startLineNo, int endLineNo) {
/* 272 */     BufferedWriter bw = null;
/* 273 */     FileWriter fw = null;
/*     */     try {
/* 275 */       fw = new FileWriter(String.valueOf(this.PATH) + "LN.csv", true);
/* 276 */       bw = new BufferedWriter(fw);
/* 277 */       bw.write(String.valueOf(rowData[0]) + "," + rowData[1] + "," + startLineNo + "," + endLineNo + "\n");
/* 278 */     } catch (IOException e) {
/*     */       
/* 280 */       e.printStackTrace();
/*     */     } finally {
/*     */       try {
/* 283 */         if (bw != null)
/* 284 */           bw.close(); 
/* 285 */         if (fw != null) {
/* 286 */           fw.close();
/*     */         }
/* 288 */       } catch (IOException ex) {
/*     */         
/* 290 */         ex.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/ui/handlers/RefactoringHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package Util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.eclipse.core.resources.IFile;
/*     */ import org.eclipse.core.resources.IWorkspace;
/*     */ import org.eclipse.core.resources.ResourcesPlugin;
/*     */ import org.eclipse.core.runtime.IPath;
/*     */ import org.eclipse.core.runtime.IProgressMonitor;
/*     */ import org.eclipse.core.runtime.NullProgressMonitor;
/*     */ import org.eclipse.core.runtime.Path;
/*     */ import org.eclipse.jdt.core.ICompilationUnit;
/*     */ import org.eclipse.jdt.core.ISourceRange;
/*     */ import org.eclipse.jdt.core.JavaCore;
/*     */ import org.eclipse.jdt.core.JavaModelException;
/*     */ import org.eclipse.jdt.internal.corext.refactoring.code.ExtractMethodRefactoring;
/*     */ import org.eclipse.ltk.core.refactoring.RefactoringStatus;
/*     */ 
/*     */ 
/*     */ public class CandidateValidation
/*     */ {
/*     */   String PATH_TO_CANDIDATES;
/*     */   String PATH_TO_REF_FILE;
/*     */   String CANDIDATE_FILE_NAME;
/*     */   String PATH_TO_FOLDER;
/*     */   List<Candidate> candidates;
/*     */   
/*     */   public CandidateValidation(String pATH_TO_CANDIDATES, String pATH_TO_REF_FILE, String cANDIDATE_FILE_NAME, String pATH_TO_FOLDER) {
/*  35 */     this.PATH_TO_CANDIDATES = pATH_TO_CANDIDATES;
/*  36 */     this.PATH_TO_REF_FILE = pATH_TO_REF_FILE;
/*  37 */     this.CANDIDATE_FILE_NAME = cANDIDATE_FILE_NAME;
/*  38 */     this.PATH_TO_FOLDER = pATH_TO_FOLDER;
/*  39 */     this.candidates = new ArrayList<>();
/*     */   }
/*     */   
/*     */   public void processCandidates() {
/*  43 */     readCandidates();
/*  44 */     checkForValidRefactoring();
/*     */   }
/*     */   
/*     */   private void checkForValidRefactoring() {
/*  48 */     IWorkspace workspace = ResourcesPlugin.getWorkspace();
/*     */     try {
/*  50 */       for (int j = 0; j < this.candidates.size(); j++) {
/*  51 */         Candidate cand = this.candidates.get(j);
/*  52 */         IPath path = Path.fromOSString(cand.getFilePath());
/*  53 */         IFile file = workspace.getRoot().getFileForLocation(path);
/*  54 */         ICompilationUnit compilationUnit = (ICompilationUnit)JavaCore.create(file);
/*  55 */         System.out.println(path.toString());
/*  56 */         compilationUnit.open(null);
/*  57 */         for (int i = 0; i < cand.getStartIndex().size(); i++) {
/*  58 */           Boolean isExtractable = em(compilationUnit, ((Integer)cand.getStartIndex().get(i)).intValue(), ((Integer)cand.getEndIndex().get(i)).intValue());
/*  59 */           ((Candidate)this.candidates.get(j)).appendIsExtractable(isExtractable);
/*     */         } 
/*     */       } 
/*  62 */       writeToFile();
/*  63 */     } catch (JavaModelException e) {
/*     */       
/*  65 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeToFile() {
/*  71 */     BufferedWriter bw = null;
/*  72 */     FileWriter fw = null;
/*     */     
/*  74 */     try { fw = new FileWriter(String.valueOf(this.PATH_TO_FOLDER) + "test_candidates.csv");
/*  75 */       bw = new BufferedWriter(fw);
/*  76 */       bw.write("FilePath,StartIndex,EndIndex,Extractable\n");
/*  77 */       for (int j = 0; j < this.candidates.size(); j++) {
/*  78 */         Candidate cand = this.candidates.get(j);
/*  79 */         for (int i = 0; i < cand.getStartIndex().size(); i++) {
/*  80 */           StringBuilder rowData = new StringBuilder(String.valueOf(cand.getFilePath()) + "," + Integer.toString(((Integer)cand.getStartIndex().get(i)).intValue()) + 
/*  81 */               "," + Integer.toString(((Integer)cand.getEndIndex().get(i)).intValue()) + "," + ((Boolean)cand.getIsExtractable().get(i)).toString() + "\n");
/*  82 */           bw.write(rowData.toString());
/*     */         } 
/*     */       }  }
/*  85 */     catch (IOException iOException)
/*     */     
/*     */     { 
/*     */       
/*  89 */       try { bw.close();
/*  90 */         fw.close(); }
/*  91 */       catch (IOException e)
/*     */       
/*  93 */       { e.printStackTrace(); }  } finally { try { bw.close(); fw.close(); } catch (IOException e) { e.printStackTrace(); }
/*     */        }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean em(ICompilationUnit unit, int startIndex, int endIndex) {
/*     */     try {
/* 104 */       ISourceRange expected = TextRangeUtil.getSelection(unit, startIndex, 0, endIndex, 0);
/* 105 */       ExtractMethodRefactoring refactoring = new ExtractMethodRefactoring(unit, expected.getOffset(), expected.getLength());
/* 106 */       refactoring.setMethodName("extracted");
/* 107 */       refactoring.setVisibility(0);
/*     */       
/* 109 */       RefactoringStatus status = refactoring.checkAllConditions((IProgressMonitor)new NullProgressMonitor());
/* 110 */       if (status.isOK()) {
/* 111 */         System.out.println("Extractable for start_ " + startIndex + " end_ " + endIndex);
/* 112 */         return Boolean.valueOf(true);
/*     */       } 
/* 114 */     } catch (Exception e1) {
/*     */       
/* 116 */       e1.printStackTrace();
/*     */     } 
/* 118 */     return Boolean.valueOf(false);
/*     */   }
/*     */   
/*     */   public void readCandidates() {
/* 122 */     BufferedReader br = null;
/* 123 */     String line = "";
/* 124 */     int count = 0;
/*     */     
/*     */     try {
/* 127 */       Candidate cand = new Candidate();
/* 128 */       br = new BufferedReader(new FileReader(String.valueOf(this.PATH_TO_CANDIDATES) + this.CANDIDATE_FILE_NAME));
/* 129 */       while ((line = br.readLine()) != null) {
/* 130 */         if (count > 0) {
/* 131 */           String[] rowData = line.split(",");
/* 132 */           String filePath = this.PATH_TO_REF_FILE;
/* 133 */           String methodName = rowData[0];
/* 134 */           int startIndex = Integer.parseInt(rowData[1]);
/* 135 */           int endIndex = Integer.parseInt(rowData[3]);
/* 136 */           if (this.candidates.size() > 0 && ((Candidate)this.candidates.get(this.candidates.size() - 1)).getFilePath().equals(filePath) && (
/* 137 */             (Candidate)this.candidates.get(this.candidates.size() - 1)).getMethodName().equals(methodName)) {
/*     */             
/* 139 */             ((Candidate)this.candidates.get(this.candidates.size() - 1)).appendStartIndex(startIndex);
/* 140 */             ((Candidate)this.candidates.get(this.candidates.size() - 1)).appendEndIndex(endIndex);
/*     */           } else {
/*     */             
/* 143 */             cand = new Candidate(this.PATH_TO_REF_FILE, methodName, startIndex, endIndex);
/* 144 */             this.candidates.add(cand);
/*     */           } 
/* 146 */           System.out.println("Start Number " + rowData[1] + " End Number " + rowData[3]);
/*     */         } 
/* 148 */         count++;
/* 149 */         System.out.println(this.candidates.size());
/*     */       }
/*     */     
/* 152 */     } catch (FileNotFoundException e) {
/*     */       
/* 154 */       e.printStackTrace();
/* 155 */     } catch (IOException e) {
/*     */       
/* 157 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/Util/CandidateValidation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
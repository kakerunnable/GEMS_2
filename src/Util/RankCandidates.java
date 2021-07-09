/*    */ package Util;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ 
/*    */ public class RankCandidates
/*    */ {
/*    */   public void callPythonProcess(String path, String pythonPath) throws IOException {
/* 11 */     String s = null;
/* 12 */     String pathToScript = String.valueOf(path) + "gems.py";
/* 13 */     //"python " + pathToScript;
/*    */     
/* 15 */     ProcessBuilder pb = new ProcessBuilder(new String[] { pythonPath, "gems.py" });
/* 16 */     pb.directory(new File(path));
/* 17 */     Process p = pb.start();
/* 18 */     BufferedReader stdInput = new BufferedReader(
/* 19 */         new InputStreamReader(p.getInputStream()));
/*    */     
/* 21 */     BufferedReader stdError = new BufferedReader(
/* 22 */         new InputStreamReader(p.getErrorStream()));
/* 23 */     System.out.println("Here is the standard output of the command:\n");
/* 24 */     while ((s = stdInput.readLine()) != null) {
/* 25 */       System.out.println(s);
/*    */     }
/*    */ 
/*    */     
/* 29 */     System.out.println("Here is the standard error of the command (if any):\n");
/* 30 */     while ((s = stdError.readLine()) != null)
/* 31 */       System.out.println(s); 
/*    */   }
/*    */ }


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/Util/RankCandidates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
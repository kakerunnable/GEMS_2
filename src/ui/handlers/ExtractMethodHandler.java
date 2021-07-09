package ui.handlers;

import Util.CandidateValidation;
import Util.ExtractMethodResults;
import Util.ModelProvider;
import Util.RankCandidates;
import gumtree.spoon.AstComparator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.code.ExtractMethodRefactoring;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;
import ui.Activator;
import ui.RefactoringResults;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.ui.ide.IDE;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jdt.ui.JavaUI;

public class ExtractMethodHandler
//extends AbstractHandler
extends AbstractUIPlugin implements IStartup
{
	
	private String PROJECT_NAME = "Android-IMSI-Catcher-Detector";
	private String FILE_PATH = "AIMSICD/src/main/java/com/secupwn/aimsicd/AndroidIMSICatcherDetector.java";

	@Override
	public void earlyStartup() {
		
		System.out.println("earlyStartup OK!");
		
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
		IPath path = new Path(FILE_PATH);
		IFile ifile = project.getFile(path);
		
		// ICompilationUnit を取得するために，ここで ifile を開いておく
		Display.getDefault().syncExec(new Runnable() {
		    @Override
		    public void run() {
		    	IWorkbench workbench = PlatformUI.getWorkbench();
		    	IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		    	IWorkbenchPage page = window.getActivePage();
				try {
					EditorUtility.openInEditor(ifile, true);
				} catch (Throwable e) {
					e.printStackTrace();
				}
		    }
		});
		
		/*
		 例として，Android-IMSI-Catcher-Detector の masterブランチにある，/AIMSICD/src/main/java/com/secupwn/aimsicd/AndroidIMSICatcherDetector.java
		 の メソッド onCreate の 62~67 行目のコード片をメソッド抽出（Extract Method）する場合を想定する．
		 使用できる情報は，
			 methodName
			 startLine
			 startColumn
			 endLine
			 endColumn
		 の5つ．最終的には，これらは外部から渡すような実装にする予定だが，今回はベタ書きで動けばそれでOK
		*/
		String methodName = "onCreate"; // コード片が抽出されるメソッドの名前
		int startLine = 62; // 抽出したいコード片の開始行
		int startColumn = 9; // startLineの1文字目の位置
		int endLine = 67; // 抽出したいコード片の終了行
		int endColumn = 55; // endLineの末尾文字の位置
		
		/*
		 実装①：ここで methodName・startLine・startColumn・endLine・endColumn を活用するなどして，selectionStart と selectionLength を取得したい．方法はなんでも良い．
		*/
		
		/*
		 onCreate の 62〜67行目 を抽出する場合の selectionStart と selectionLength は以下の値になると思われる．
		 今回は，
		 selectionStart は wcコマンドで 62行目 までのバイト数を測った．
		 selectionLength は wcコマンドで位置(行, 列) = (67, 55) までのバイト数（67行目の;までのバイト数）を測った結果（= 2330）から，selectionStart を引いて求めた，
		 このソースコードを編集せずに実行すると，AndroidIMSICatcherDetector.java の 62〜67行目 が抽出されて，"extracted"という名前のメソッドが下記のように完成する．
		 ---
			private Realm extracted() {
				RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
			            .deleteRealmIfMigrationNeeded()
			            .build();
			
			    Realm.setDefaultConfiguration(realmConfiguration);
			    final Realm realm = Realm.getDefaultInstance();
				return realm;
			}
		 ---
		*/
		int selectionStart = 2056;
		int selectionLength = 274;
		
		extractMethodRefactoring(ifile, selectionStart, selectionLength);
		
		/*
		 実装②：ここで，抽出されたメソッドの名前を，"extracted"から任意の値に変更する
		*/
		
		/*
		 実装③：ここで，抽出されたメソッド（名前変更後）を任意のパスにファイル形式で保存（形式.javaで）
		*/
		
		return;
	}	

	// extractMethodRefactoringを実行する関数
	@SuppressWarnings("restriction")
	public static void extractMethodRefactoring(IFile ifile, int selectionStart, int selectionLength) {
		ICompilationUnit _compilationUnit = JavaCore.createCompilationUnitFrom(ifile);

	    //The following line is part of the internal API
	    ExtractMethodRefactoring tempR = new ExtractMethodRefactoring(_compilationUnit, selectionStart, selectionLength);

	    try {
	        NullProgressMonitor pm = new NullProgressMonitor();
	        tempR.checkAllConditions(pm);
	        Change change = tempR.createChange(pm);
	        change.perform(pm);
	    } catch (Exception e) {e.printStackTrace();}

	}
	
}

package ui.handlers;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.corext.refactoring.code.ExtractMethodRefactoring;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import file.SaveFile;

public class ExtractMethodHandler
//extends AbstractHandler
extends AbstractUIPlugin implements IStartup
{

//	private String PROJECT_NAME = "ifile-testing";
//	private String FILE_PATH = "AIMSICD/src/main/java/com/secupwn/aimsicd/AndroidIMSICatcherDetector.java"; // 任意のファイルを指定する

	private String PROJECT_NAME = "ifile-testing";
	private String FILE_PATH = "src/test/SampleClass.java"; // 任意のファイルを指定する

	@Override
	public void earlyStartup() {

		System.out.println("earlyStartup OK!");

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
		IPath path = new Path(FILE_PATH);
		IFile ifile = project.getFile(path);

		// 普通のファイル取得
		SaveFile file = new SaveFile(ifile.getLocation().toFile().toPath().toAbsolutePath().normalize().toFile());

		// 元のクラスを保持しておく
		file.save();

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
//		int startLine = 62; // 抽出したいコード片の開始行
//		int startColumn = 9; // startLineの1文字目の位置
//		int endLine = 67; // 抽出したいコード片の終了行
//		int endColumn = 55; // endLineの末尾文字の位置

		/*
		 実装①：ここで methodName・startLine・startColumn・endLine・endColumn を活用するなどして，selectionStart と selectionLength を取得したい．方法はなんでも良い．
		*/

		/*
		 onCreate の 62〜67行目 を抽出する場合の selectionStart と selectionLength は以下の値になると思われる．
		 今回は，
		 selectionStart は wcコマンドで62行目に到達するまで（61行目まで）のバイト数を測った．
		 selectionLength は wcコマンドで67行目まで（68行目に到達するまで）のバイト数を測った結果（= 2330）から，selectionStart を引いて求めた，
		 このソースコードを編集せずに実行すると，AndroidIMSICatcherDetector.java の 62〜67行目 が抽出されて，
		 extractMethodRefactoring()の中で指定した名前（今回は"testMethod"）のメソッドが下記のように完成する．
		 ---
			private Realm testMethod() {
				RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
			            .deleteRealmIfMigrationNeeded()
			            .build();

			    Realm.setDefaultConfiguration(realmConfiguration);
			    final Realm realm = Realm.getDefaultInstance();
				return realm;
			}
		 ---
		*/

		// 抽出箇所の取得
		selection.RowSelection rowSelection = selection.RowSelectionAction.run(file, 7, 8);

		// リファクタリング情報取得
		ExtractMethodRefactoring emr = extractMethodRefactoring(ifile, rowSelection);

		// 抽出メソッド名
		String newMethodName = "extractMethod";

		// リファクタリング実行
		executeRefactoring(emr, newMethodName);

		/*
		 実装②：ここで，抽出されたメソッドを任意のパスにファイル形式で保存（形式.javaで）
		*/

		// TODO 詳細確認中

		/*
		 実装③：抽出前の状態に戻す
		*/
		file.load();

		return;
	}

	@SuppressWarnings("restriction")
	public static ExtractMethodRefactoring extractMethodRefactoring(IFile ifile, selection.RowSelection rowSelection) {

		ICompilationUnit _compilationUnit = JavaCore.createCompilationUnitFrom(ifile);

	    //The following line is part of the internal API
	    return new ExtractMethodRefactoring(_compilationUnit, rowSelection.getSelectionStart(), rowSelection.getSelectionLength());
	}

	@SuppressWarnings("restriction")
	public static Change executeRefactoring(ExtractMethodRefactoring emr, String newMethodName) {

	    try {
	        NullProgressMonitor pm = new NullProgressMonitor();
	        emr.checkAllConditions(pm);
	        emr.setMethodName(newMethodName);  // setMethodName で名前を変更できる
	        Change change = emr.createChange(pm);
	        return change.perform(pm);
	    } catch (Exception e) {
	    	throw new RuntimeException(e);
	    }
	}

}

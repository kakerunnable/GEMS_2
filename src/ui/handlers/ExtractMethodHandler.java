package ui.handlers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Deque;
import java.util.LinkedList;
import java.util.StringJoiner;

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

import Util.ReflectUtils;
import file.SaveFile;
import ui.ChangeResult;

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

		// ファイル取得
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
		selection.RowSelection rowSelection = selection.RowSelectionAction.run(file, 14, 16);

		// リファクタリング情報取得
		ExtractMethodRefactoring emr = extractMethodRefactoring(ifile, rowSelection);

		// 抽出メソッド名
		String newMethodName = "extractMethod";

		// リファクタリング実行
		ChangeResult result = executeRefactoring(emr, newMethodName);

		/*
		 実装②：ここで，抽出されたメソッドを任意のパスにファイル形式で保存（形式.javaで）
		*/

		// 1. 対象メソッド元の構文を取得
		Object fAnalyzer = ReflectUtils.getFieldValue(ExtractMethodRefactoring.class, "fAnalyzer", emr);
		Object fEnclosingBodyDeclaration = ReflectUtils.getFieldValue(fAnalyzer.getClass(), "fEnclosingBodyDeclaration", fAnalyzer);
		String targetBeforeMethodName = ReflectUtils.getFieldValue(fEnclosingBodyDeclaration.getClass(), "methodName", fEnclosingBodyDeclaration).toString();
		String targetBeforeMethodCode = fEnclosingBodyDeclaration.toString();

		// メソッドの出力フォルダ作成
		java.nio.file.Path saveDirRoot = file.toPath().getParent().resolve(file.getName().split("\\.")[0]);
		saveDirRoot.toFile().mkdirs();

		// メソッドの出力
		try {
			File targetFile = saveDirRoot.resolve(targetBeforeMethodName + ".java").toFile();
			Files.write(targetFile.toPath(), targetBeforeMethodCode.getBytes(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 2. リファクタリング後のメソッドを取得
		String regex = result.diffMethodRegex();
		SaveFile currentFile = new SaveFile(file);
		currentFile.save();
		String[] lines = currentFile.getSaved().split(System.lineSeparator());
		StringJoiner joiner = new StringJoiner(System.lineSeparator());
		Deque<Character> queue = new LinkedList<>();
		boolean matched = false;
		for (String line : lines) {

			if (line.matches(regex)) {
				matched = true;
			}

			if (!matched) {
				continue;
			}

			line.chars().forEachOrdered(c -> {
				boolean anymatch = false;
				if (c == '{') {
					queue.add((char)c);
				} else if (c == '}') {
					queue.poll();
				}
			});

			joiner.add(line);

			if (queue.size() == 0) {
				break;
			}
		}
		String extractMethodCode = joiner.toString();

		// メソッドの出力
		try {
			File targetFile = saveDirRoot.resolve(newMethodName + ".java").toFile();
			Files.write(targetFile.toPath(), extractMethodCode.getBytes(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 実装③：抽出前の状態に戻す
		*/

		file.load();
	}

	@SuppressWarnings("restriction")
	public static ExtractMethodRefactoring extractMethodRefactoring(IFile ifile, selection.RowSelection rowSelection) {

		ICompilationUnit _compilationUnit = JavaCore.createCompilationUnitFrom(ifile);

	    //The following line is part of the internal API
	    return new ExtractMethodRefactoring(_compilationUnit, rowSelection.getSelectionStart(), rowSelection.getSelectionLength());
	}

	@SuppressWarnings("restriction")
	public static ChangeResult executeRefactoring(ExtractMethodRefactoring emr, String newMethodName) {

	    try {
	        NullProgressMonitor pm = new NullProgressMonitor();
	        emr.checkAllConditions(pm);
	        emr.setMethodName(newMethodName);  // setMethodName で名前を変更できる
	        Change before = emr.createChange(pm);
	        String unitBefore = ReflectUtils.getFieldValue(before.getClass(), "fCUnit", before).toString();

	        Change after = before.perform(pm);
	        String unitAfter = ReflectUtils.getFieldValue(after.getClass(), "fCUnit", after).toString();

	        return new ChangeResult(unitBefore, unitAfter);
	    } catch (Exception e) {
	    	throw new RuntimeException(e);
	    }
	}

}

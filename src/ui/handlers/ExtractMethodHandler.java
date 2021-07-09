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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.json.JSONObject;

import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.ui.ide.IDE;

import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jdt.ui.JavaUI;

public class ExtractMethodHandler
//extends AbstractHandler
extends AbstractUIPlugin implements IStartup
{
//	public static IFile REF_FILE;
	public IFile REF_FILE;
	List<String> methods;
	String PATH_TO_CANDIDATES;
	String PYTHON_PATH;
	private String currentMethod;
	List<ExtractMethodResults> results;
	public static ICompilationUnit compilationUnit;
	public static ILog log;
	
	private String filePathString;
	private int startLineInt;
	private String methodNameString;
	
//	private String PROJECT_SAVE_PATH = "/Users/yamanakajinto/Desktop/RefactoringMiner/build/distributions/RefactoringMiner-2.1.0/bin/";
	private String PROJECT_SAVE_PATH = "/Users/yamanakajinto/Documents/runtime-EclipseApplication/";
	private String RESULT_SAVE_PATH = "/Users/yamanakajinto/Desktop/GEMS_result/";
	private String GEMS_FILE_PATH = "/Users/yamanakajinto/Documents/runtime-EclipseApplication/gems/";
	private String PROJECT_NAME = "Android-IMSI-Catcher-Detector";
//	private IWorkbenchWindow WINDOW;
//	private IWorkbenchPage PAGE;

	@Override
	public void earlyStartup() {
		
		System.out.println("earlyStartup OK!");
		
		
		Repository localRepo;
		Git git = null;
		try {
			localRepo = new FileRepository( PROJECT_SAVE_PATH + PROJECT_NAME + "/.git" ); // プロジェクト名（指定する）
			git = new Git(localRepo);
			System.out.println("OK_1");
		} catch (IOException e) {
			e.printStackTrace();
		} 
		  
		try{
		  git.fetch().setRemote(Constants.DEFAULT_REMOTE_NAME).call();
		  System.out.println("OK_2");
		}catch( Exception e ){
		  e.printStackTrace();
		}
		
//		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME); // 指定する
//		IPath iPath = new Path("./master.json");
//		IFile ifile = project.getFile(iPath);
//		IFile ifile = project.getFile("./" + "app/src/main/java/com/SecUpwN/AIMSICD/adapters/AIMSICDDbAdapter.java");

		String path = PROJECT_SAVE_PATH + PROJECT_NAME + "/master.json";
//		String path = PROJECT_SAVE_PATH + PROJECT_NAME + "/test_2.json";
		System.out.println("path: " + path);
		
//		extractMethodRefactoring(ifile);
		
		ObjectMapper mapper = new ObjectMapper();

		try {
			JsonNode node = mapper.readTree(new File(path));
			System.out.println("JsonNode!");
			System.out.println("----------");
			System.out.println(node.get("commits"));
			System.out.println("----------");
			JsonNode commits = node.get("commits");

		    for(int i = 0;i<commits.size();i++) {
		        System.out.println(commits.get(i).get("sha1"));
		        JsonNode sha1 = commits.get(i).get("sha1");
		        String sha1String = sha1.toString().replace("\"", ""); // 使う情報：メソッド開始行

		        //////////////////////////// ここでコミットを切り替える ////////////////////////////
				ObjectId previousCommitId;
				String previousCommitIdString = null;
				try {
					// 子コミット + ~（チルダ）で親コミットを特定
					previousCommitId = git.getRepository().resolve( sha1String + "~" );
					previousCommitIdString = ObjectId.toString(previousCommitId);
					System.out.println(previousCommitIdString);
				} catch (RevisionSyntaxException e) {
					e.printStackTrace();
				} catch (AmbiguousObjectException e) {
					e.printStackTrace();
				} catch (IncorrectObjectTypeException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
		        try {
		            // 初めてリモートリポジトリを checkout する場合
		            git.checkout().setCreateBranch(true).setName(previousCommitIdString)
		                .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.SET_UPSTREAM)
//		                .setStartPoint("origin/" + "e235f884f2e0bc258da77b9c80492ad33386fa86~1").call();
		            	.setStartPoint(previousCommitIdString).call();
		        } catch (RefAlreadyExistsException e) {
		            // 2回目以降(checkout 済みだと上記例外が投げられる)
		            try {
		                git.checkout().setName(previousCommitIdString).call();
		                git.pull().call();
		            } catch (GitAPIException e1) {
//		                throw new RuntimeException(e);
		            }
		        } catch (GitAPIException e) {
		            throw new RuntimeException(e);
		        }
		        //////////////////////////////////////////////////////////////////////////////////////
		        
		        // リフレッシュ
		        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME); // ここは毎回指定する
		        project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		        
		        JsonNode refactorings = commits.get(i).get("refactorings");
			    for(int j = 0;j<refactorings.size();j++) { // 一つのコミットに対して複数のリファクタリングを全て確認

			    	JsonNode type = refactorings.get(j).get("type");

			        if("\"Extract Method\"".equals(type.toString())) {
			        	System.out.println("Extract Method !!!!");
			        	JsonNode leftSideLocations = refactorings.get(j).get("leftSideLocations");
			        	for(int k = 0;k<leftSideLocations.size();k++) {
			        		JsonNode codeElementType = leftSideLocations.get(k).get("codeElementType");
			        		if("\"METHOD_DECLARATION\"".equals(codeElementType.toString())) {
			        			JsonNode filePath = leftSideLocations.get(k).get("filePath");
			        			filePathString = filePath.toString().replace("\"", ""); // 使う情報：ファイルパス
			        			
			        			JsonNode startLine = leftSideLocations.get(k).get("startLine");
			        			String startLineString = startLine.toString().replace("\"", ""); // 使う情報：メソッド開始行
			        			startLineInt = Integer.parseInt(startLineString);
			        			
			        			JsonNode codeElement = leftSideLocations.get(k).get("codeElement");
			        			String codeElementString = codeElement.toString().replace("\"", "");

			        			Pattern p = Pattern.compile(".* (.*)\\(");
			        			Matcher m = p.matcher(codeElementString);
			        			if (m.find()){
			        				System.out.println(m.group(1));
			        				methodNameString = m.group(1); // 使う情報：メソッド名
			        			}
			        		}
			        	}
			        	System.out.println("gems Start");
//			        	// フォルダ名は検討の余地あり
			        	String dirName = sha1String + "_" + String.valueOf(j);;
			        	gems(filePathString, startLineInt, methodNameString, sha1String, dirName);
			        	System.out.println("gems End");
			        	
			        	Display.getDefault().syncExec(new Runnable() {
//			    		Display.getDefault().asyncExec(new Runnable() {
						    @Override
						    public void run() {
						        IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
						        IWorkbenchPage page = workbenchWindow.getActivePage();

						        IEditorReference[] editorRefs = page.getEditorReferences();
						        for(IEditorReference editorRef: editorRefs) {
						        	IEditorPart editor = editorRef.getEditor(false);
						        	IEditorInput input = editor.getEditorInput();
						        	IFile file = (IFile)input.getAdapter(IFile.class);
						        	page.closeEditor(editor, false);
						        }
						    }
						});
			    		
			        }
			    }
		    }
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
		
	}	
		
	public void gems(String paramFilePath, int paramStartLine, String paramMethodName, String commitId, String dirName) {
		// TODO Auto-generated method stub
		log = Activator.getDefault().getLog();
		parseArguments();
		setPythonPath();

//		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
//		IWorkbench workbench = PlatformUI.getWorkbench();
//		IWorkbenchWindow activeWindow = workbench.getActiveWorkbenchWindow();
//		ISelection selection = HandlerUtil.getCurrentSelection(event);

//		setFileStoragePath();
		setFileStoragePath(commitId, dirName); // 変更

		deleteFile("test_candidates.csv");
		deleteFile("test_feasibility.csv");
		deleteFile("test_features.csv");
		deleteFile("test_prob.csv");

		try {
			System.out.println("-----info-----");
			System.out.println(paramFilePath);
			System.out.println(paramStartLine);
			System.out.println(paramMethodName);
			boolean isJavaFile = checkIfCommandOnJavaFile(paramFilePath); // 変更

			if (!isJavaFile) {
				log.log((IStatus)new Status(4, "UI", "Please Choose a Java File and Method to refactor"));
			} else {

				getAllMethodNames();

//				String text = "onPostCreate"; // ここ メソッド名→取れる
				String text = paramMethodName;
				this.currentMethod = text;
//				int startLine = 227; // ここ　メソッドの開始行→取れる
				int startLine = paramStartLine;
				
				System.out.println("---REF_FILE---");
				System.out.println(REF_FILE);
				
				if (this.methods.contains(text)) {
					generateFeatures(REF_FILE.getRawLocation().toOSString(), text, startLine);
					CandidateValidation validation = new CandidateValidation(this.PATH_TO_CANDIDATES, REF_FILE.getRawLocation().toOSString(), "test_feasibility.csv", 
							this.PATH_TO_CANDIDATES);
					validation.processCandidates();

					RankCandidates rank = new RankCandidates();
					try {
						log.log((IStatus)new Status(4, "UI", "Path and python " + this.PATH_TO_CANDIDATES + " , " + this.PYTHON_PATH));
						rank.callPythonProcess(this.PATH_TO_CANDIDATES, this.PYTHON_PATH);
						/* 不要
						readProbabilityResults();
						ModelProvider.INSTANCE.setResults(this.results);
						RefactoringResults.updateViewer();
						*/
					} catch (Exception exception) {
						log.log((IStatus)new Status(4, "UI", "Exception while ranking candidates. Make sure python script is in the same path as the created csv files."));
					} 
				} else {
					log.log((IStatus)new Status(4, "UI", "Please Choose a Method Name from method definition for refactoring"));
				} 
			} 
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			log.log((IStatus)new Status(4, "UI", e.toString()));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			log.log((IStatus)new Status(4, "UI", e.toString()));
			
		} 
	}

	private void setFileStoragePath(String commitId, String dirName) {
		try {
			Files.createDirectories(Paths.get(RESULT_SAVE_PATH + commitId + "/" + dirName)); // commitId（子コミット）を変数で定義
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.PATH_TO_CANDIDATES = RESULT_SAVE_PATH + commitId + "/" + dirName + "/"; // 変更：dirNameを変数で定義
		
		// GEMSの実行に必要なファイルをPATH_TO_CANDIDATESにコピー
		try {
			Files.copy(Paths.get(GEMS_FILE_PATH + "gems.py"), Paths.get(this.PATH_TO_CANDIDATES + "gems.py"));
			Files.copy(Paths.get(GEMS_FILE_PATH + "gems.pickle.dat"), Paths.get(this.PATH_TO_CANDIDATES + "gems.pickle.dat"));
			Files.copy(Paths.get(GEMS_FILE_PATH + "normailize.out"), Paths.get(this.PATH_TO_CANDIDATES + "normailize.out"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void generateFeatures(String filePath, String methodName, int lineNo) throws Exception {
		AstComparator compartor = new AstComparator();
		System.out.println("Output is saved in dir : " + this.PATH_TO_CANDIDATES);
		log.log((IStatus)new Status(1, "UI", "Output is saved in dir : " + this.PATH_TO_CANDIDATES));
		compartor.compare(filePath, methodName, lineNo, this.PATH_TO_CANDIDATES);
	}

	private void parseArguments() {
		String[] args = Platform.getCommandLineArgs();
		for (int i = 0; i < args.length; i++) {
			if (args[i].contains("pythonPath")) {
				this.PYTHON_PATH = args[i + 1];
				i++;
			} 
		} 
	}

	private void deleteFile(String fileName) {
		try {
			File file = new File(String.valueOf(this.PATH_TO_CANDIDATES) + fileName);
			if (file.delete()) {
				System.out.println("Deleted " + fileName);
			}
		} catch (Exception exception) {
			System.out.println("Please delete the csv file: " + fileName + " at location : " + this.PATH_TO_CANDIDATES);
			log.log((IStatus)new Status(4, "UI", "Please delete the csv file: " + fileName + " at location : " + this.PATH_TO_CANDIDATES));
		} 
	}

	private void getAllMethodNames() {
		
		Display.getDefault().syncExec(new Runnable() {
//		Display.getDefault().asyncExec(new Runnable() {
		    @Override
		    public void run() {
		    	IWorkbench workbench = PlatformUI.getWorkbench();
		    	IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		    	IWorkbenchPage page = window.getActivePage();;
		    	
		        System.out.println("---syncExec---");
				System.out.println(workbench);
				System.out.println(window);
				System.out.println(page);

				try {
					EditorUtility.openInEditor(REF_FILE, true);
//					System.out.println(EditorUtility.openInEditor(REF_FILE, true));
				} catch (Throwable e) {
					e.printStackTrace();
				}
		    }
		});
		
		compilationUnit = (ICompilationUnit)JavaCore.create(REF_FILE);
		System.out.println("---compilationUnit---");
		System.out.println(compilationUnit);

		this.methods = new ArrayList<>();
		try {
			IType[] allTypes = compilationUnit.getAllTypes(); byte b; int i; IType[] arrayOfIType1;
			for (i = (arrayOfIType1 = allTypes).length, b = 0; b < i; ) { IType type = arrayOfIType1[b]; byte b1; int j; IMethod[] arrayOfIMethod;
			for (j = (arrayOfIMethod = type.getMethods()).length, b1 = 0; b1 < j; ) { IMethod method = arrayOfIMethod[b1];
			this.methods.add(method.getElementName()); b1++; }
			b++; }

		} catch (JavaModelException e) {

			e.printStackTrace();
			log.log((IStatus)new Status(4, "UI", e.toString()));
		} 
		// 名前の重複があるとバグるので注意
		System.out.println("-----allMethodNames-----");
		System.out.println(this.methods);
	}


	public boolean checkIfCommandOnJavaFile(String filePath) throws FileNotFoundException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME); // ここは毎回指定する
//		IPath path = new Path("./" + filePath); //　ここ→取れる　ファイルパス
		IPath path = new Path(filePath); //　ここ→取れる　ファイルパス
		IFile file = project.getFile(path);

//		IWorkspace workspace = ResourcesPlugin.getWorkspace();    
//		IPath location = Path.fromOSString(PROJECT_SAVE_PATH + PROJECT_NAME + "/" + filePath);
//		IFile ifile = workspace.getRoot().getFile(location);

		if (file == null) throw new FileNotFoundException(); 
		if (file.getFileExtension().contains("java")) {
			REF_FILE = file;
			return true;
		} 
		return false;
	}
	
//	public boolean checkIfCommandOnJavaFile() throws FileNotFoundException {
//		IWorkbenchPart workbenchPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
//		IFile file = (IFile)workbenchPart.getSite().getPage().getActiveEditor().getEditorInput().getAdapter(IFile.class);
//		if (file == null) throw new FileNotFoundException(); 
//		if (file.getFileExtension().contains("java")) {
//			REF_FILE = file;
//			return true;
//		} 
//		return false;
//	}

	public void readProbabilityResults() throws IOException {
		this.results = new ArrayList<>();
		String csvFile = String.valueOf(this.PATH_TO_CANDIDATES) + "test_prob.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		br = new BufferedReader(new FileReader(csvFile));
		int count = 1;
		while ((line = br.readLine()) != null) {

			String[] data = line.split(cvsSplitBy);
			if (count < 31) {
				this.results.add(new ExtractMethodResults(String.valueOf(count), this.currentMethod, data[0], String.valueOf(Integer.parseInt(data[1]) - 1), data[2]));
			}
			count++;
		} 
	}

	public void setPythonPath() {
		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode("UI");

		log.log((IStatus)new Status(4, "UI", prefs.absolutePath()));
		try {
			if ((prefs.keys()).length == 0) {
				prefs.put("PYTHON_PATH", "<replace_this_with_python_path>");
				log.log((IStatus)new Status(4, "UI", "Lenght is 0"));
			} else {
				byte b; int i; String[] arrayOfString; for (i = (arrayOfString = prefs.keys()).length, b = 0; b < i; ) { String key = arrayOfString[b];
				if (key.compareTo("PYTHON_PATH") == 0) {

					this.PYTHON_PATH = prefs.get("PYTHON_PATH", null);
					log.log((IStatus)new Status(4, "UI", "Key found and the value is " + this.PYTHON_PATH));
				} else {
					prefs.put("PYTHON_PATH", "<replace_this_with_python_path>");
					log.log((IStatus)new Status(4, "UI", "key notfound"));
				}  b++; }

			} 
		} catch (BackingStoreException e) {

			e.printStackTrace();
		} 
	}

	// extractMethodRefactoringを実行する関数
	@SuppressWarnings("restriction")    //Works but is INTERNAL USE ONLY
	public static void extractMethodRefactoring(IFile ifile) {
//	    ITextSelection selection = compilationUnitSelection.getITextSelection();
		ICompilationUnit _compilationUnit = JavaCore.createCompilationUnitFrom(ifile);
	    int start = 61956;
	    int length = 475;

	    //The following line is part of the internal API
	    ExtractMethodRefactoring tempR = new ExtractMethodRefactoring(_compilationUnit, start, length);

	    try {
	        NullProgressMonitor pm = new NullProgressMonitor();
	        tempR.checkAllConditions(pm);
	        Change change = tempR.createChange(pm);
	        change.perform(pm);
	    } catch (Exception e) {e.printStackTrace();}

	}
	
}


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/ui/handlers/ExtractMethodHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
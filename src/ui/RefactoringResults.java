/*     */ package ui;
/*     */ 
/*     */ import Util.ExtractMethodResults;
/*     */ import Util.ModelProvider;
/*     */ import Util.TextRangeUtil;
/*     */ import org.eclipse.jdt.core.ISourceRange;
/*     */ import org.eclipse.jface.text.TextSelection;
/*     */ import org.eclipse.jface.viewers.ArrayContentProvider;
/*     */ import org.eclipse.jface.viewers.CellLabelProvider;
/*     */ import org.eclipse.jface.viewers.ColumnLabelProvider;
/*     */ import org.eclipse.jface.viewers.IContentProvider;
/*     */ import org.eclipse.jface.viewers.ISelection;
/*     */ import org.eclipse.jface.viewers.ISelectionProvider;
/*     */ import org.eclipse.jface.viewers.TableViewer;
/*     */ import org.eclipse.jface.viewers.TableViewerColumn;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Event;
/*     */ import org.eclipse.swt.widgets.Listener;
/*     */ import org.eclipse.swt.widgets.Table;
/*     */ import org.eclipse.swt.widgets.TableColumn;
/*     */ import org.eclipse.swt.widgets.TableItem;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ import org.eclipse.ui.part.ViewPart;
/*     */ import ui.handlers.ExtractMethodHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RefactoringResults
/*     */   extends ViewPart
/*     */ {
/*     */   private static TableViewer viewer;
/*     */   
/*     */   public void createPartControl(Composite parent) {
/*  37 */     createViewer(parent);
/*     */   }
/*     */   
/*     */   public static void updateViewer() {
/*  41 */     viewer.setInput(ModelProvider.INSTANCE.getResults());
/*  42 */     viewer.refresh();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void selectCode(int startLineNo, int endLineNo) {
/*     */     try {
/*  50 */       ISourceRange range = TextRangeUtil.getSelection(ExtractMethodHandler.compilationUnit, startLineNo, 0, endLineNo + 1, 0);
/*  51 */       TextSelection textSelection = new TextSelection(range.getOffset(), range.getLength());
/*  52 */       PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getSite().getSelectionProvider().setSelection((ISelection)textSelection);
/*  53 */     } catch (Exception e) {
/*     */       
/*  55 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void createViewer(Composite parent) {
/*  61 */     viewer = new TableViewer(parent, 68354);
/*     */     
/*  63 */     createColumns(parent, viewer);
/*  64 */     final Table table = viewer.getTable();
/*  65 */     table.setHeaderVisible(true);
/*  66 */     table.setLinesVisible(true);
/*     */     
/*  68 */     table.addListener(13, new Listener()
/*     */         {
/*     */           public void handleEvent(Event event) {
/*  71 */             ExtractMethodResults currentSelection = null;
/*  72 */             TableItem[] selection = table.getSelection();
/*  73 */             for (int i = 0; i < selection.length; i++) {
/*  74 */               currentSelection = (ExtractMethodResults)selection[i].getData();
/*     */             }
/*     */             
/*  77 */             RefactoringResults.this.selectCode(Integer.parseInt(currentSelection.getStartinLineNo()), Integer.parseInt(currentSelection.getEndingLineNo()));
/*     */           }
/*     */         });
/*     */     
/*  81 */     viewer.setContentProvider((IContentProvider)new ArrayContentProvider());
/*     */ 
/*     */     
/*  84 */     viewer.setInput(ModelProvider.INSTANCE.getResults());
/*     */     
/*  86 */     getSite().setSelectionProvider((ISelectionProvider)viewer);
/*     */ 
/*     */ 
/*     */     
/*  90 */     GridData gridData = new GridData();
/*  91 */     gridData.verticalAlignment = 4;
/*  92 */     gridData.horizontalSpan = 2;
/*  93 */     gridData.grabExcessHorizontalSpace = true;
/*  94 */     gridData.grabExcessVerticalSpace = true;
/*  95 */     gridData.horizontalAlignment = 4;
/*  96 */     viewer.getControl().setLayoutData(gridData);
/*     */   }
/*     */   
/*     */   private void createColumns(Composite parent, TableViewer viewer) {
/* 100 */     String[] titles = { "Top", "Method Name", "Starting Line Number", "Ending Line Number", "Probability" };
/* 101 */     int[] bounds = { 200, 200, 200, 200, 200 };
/* 102 */     TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
/* 103 */     col.setLabelProvider((CellLabelProvider)new ColumnLabelProvider()
/*     */         {
/*     */           public String getText(Object element) {
/* 106 */             ExtractMethodResults p = (ExtractMethodResults)element;
/* 107 */             return p.getTop();
/*     */           }
/*     */         });
/* 110 */     col = createTableViewerColumn(titles[1], bounds[1], 1);
/* 111 */     col.setLabelProvider((CellLabelProvider)new ColumnLabelProvider()
/*     */         {
/*     */           public String getText(Object element) {
/* 114 */             ExtractMethodResults p = (ExtractMethodResults)element;
/* 115 */             return p.getMethodName();
/*     */           }
/*     */         });
/* 118 */     col = createTableViewerColumn(titles[2], bounds[2], 2);
/* 119 */     col.setLabelProvider((CellLabelProvider)new ColumnLabelProvider()
/*     */         {
/*     */           public String getText(Object element) {
/* 122 */             ExtractMethodResults p = (ExtractMethodResults)element;
/* 123 */             return p.getStartinLineNo();
/*     */           }
/*     */         });
/* 126 */     col = createTableViewerColumn(titles[3], bounds[3], 3);
/* 127 */     col.setLabelProvider((CellLabelProvider)new ColumnLabelProvider()
/*     */         {
/*     */           public String getText(Object element) {
/* 130 */             ExtractMethodResults p = (ExtractMethodResults)element;
/* 131 */             return p.getEndingLineNo();
/*     */           }
/*     */         });
/* 134 */     col = createTableViewerColumn(titles[4], bounds[4], 4);
/* 135 */     col.setLabelProvider((CellLabelProvider)new ColumnLabelProvider()
/*     */         {
/*     */           public String getText(Object element) {
/* 138 */             ExtractMethodResults p = (ExtractMethodResults)element;
/* 139 */             return p.getProb();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private TableViewerColumn createTableViewerColumn(String title, int bound, int colNumber) {
/* 146 */     TableViewerColumn viewerColumn = new TableViewerColumn(viewer, 0);
/* 147 */     TableColumn column = viewerColumn.getColumn();
/* 148 */     column.setText(title);
/* 149 */     column.setWidth(bound);
/* 150 */     column.setResizable(true);
/* 151 */     column.setMoveable(true);
/* 152 */     return viewerColumn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFocus() {
/* 157 */     viewer.getControl().setFocus();
/*     */   }
/*     */ }


/* Location:              /Users/shanzhongrendou/Desktop/gems/UI_1.0.0.201706122202.jar!/ui/RefactoringResults.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
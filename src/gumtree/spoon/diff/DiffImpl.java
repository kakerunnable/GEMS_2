// 
// Decompiled by Procyon v0.5.36
// 

package gumtree.spoon.diff;

import gumtree.spoon.diff.operations.MoveOperation;
import com.github.gumtreediff.actions.model.Move;
import gumtree.spoon.diff.operations.DeleteOperation;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import java.io.Writer;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtSwitch;
import spoon.reflect.code.CtConditional;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtAssert;
import spoon.reflect.code.CtLocalVariable;
import org.apache.commons.csv.CSVPrinter;
import java.io.FileWriter;
import org.apache.commons.csv.CSVFormat;
import java.io.File;
import spoon.reflect.code.CtLiteral;
import java.util.Collection;
import com.github.gumtreediff.actions.model.Update;
import gumtree.spoon.diff.operations.UpdateOperation;
import gumtree.spoon.diff.operations.OperationKind;
import java.util.Optional;
import java.util.NoSuchElementException;
import java.util.Iterator;
import gumtree.spoon.diff.operations.InsertOperation;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Collections;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.matchers.CompositeMatchers;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.visitor.Filter;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.reflect.declaration.CtMethod;
import java.util.ArrayList;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.matchers.MappingStore;
import gumtree.spoon.diff.operations.Operation;
import spoon.reflect.declaration.CtModifiable;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.code.CtBlock;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtTypedElement;
import spoon.reflect.code.CtTypeAccess;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.code.CtVariableAccess;
import spoon.support.reflect.reference.CtPackageReferenceImpl;
import java.util.List;

public class DiffImpl implements Diff
{
    String Name_Src_Mtd;
    String Extracted_Code;
    List<CtPackageReferenceImpl> package_most_used;
    List<CtVariableAccess> variable_access_most_used;
    List<CtFieldAccess> field_access_most_used;
    List<CtTypeAccess> type_access_most_used;
    List<CtTypedElement> typed_ele_most_used;
    List<CtInvocation> invocation_most_used;
    List<CtElement> deleteStuff;
    CtBlock extracted_Code;
    CtElement src_Method;
    CtElement deleted;
    List<CtVariable> deletedVariable;
    List<CtVariable> srcVariable;
    List<CtVariable> commonVariable;
    List<CtVariableAccess> delVarAcc;
    List<CtVariableAccess> srcVarAcc;
    List<CtVariableAccess> commonVariableAccess;
    List<CtFieldAccess> delFieldAcc;
    List<CtFieldAccess> srcFieldAcc;
    List<CtFieldAccess> commonFieldAccess;
    List<CtInvocation> delInvo;
    List<CtInvocation> srcInvo;
    List<CtInvocation> commonInvo;
    List<CtTypeAccess> delTypeAcc;
    List<CtTypeAccess> srcTypeAcc;
    List<CtTypeAccess> commonTypeAccess;
    List<CtTypedElement> delTypedEle;
    List<CtTypedElement> srcTypedEle;
    List<CtTypedElement> commonTypedElement;
    List<CtModifiable> delMod;
    List<CtModifiable> srcMod;
    List<CtModifiable> commonModifiable;
    String Access_Modifier;
    String Returned_Type;
    int Num_Parameters;
    boolean flagtemp;
    boolean flag_find;
    String path;
    int LOC_Extracted_Method;
    int Num_Variable;
    int Num_local;
    int Num_Literal;
    int Num_Com;
    int Num_Annotation;
    int Num_AnnotationType;
    int Num_Invocation;
    int Num_Executable;
    int Num_ExeRefExp;
    int Num_Loop;
    int Num_While;
    int Num_For;
    int Num_If;
    int Num_Conditional;
    int Num_Switch;
    int Num_Var_Ac;
    int Num_Type_Ac;
    int Num_Field_Ac;
    int Num_Arr_Ac;
    int Num_Com_Var;
    int Num_Com_Var_Acc;
    int Num_Com_Field_Acc;
    int Num_Com_Invocation;
    int Num_Com_Type_Acc;
    int Num_Com_Typed_Ele;
    int Num_Com_Mod;
    private final List<Operation> allOperations;
    private final List<Operation> rootOperations;
    private final MappingStore _mappingsComp;
    private final TreeContext context;
    private int Num_Assert;
    private int Num_Assign;
    
    public DiffImpl(final TreeContext context, final ITree rootSpoonLeft, final String Src_Mtd, final int position, final String path) {
        this.package_most_used = new ArrayList<CtPackageReferenceImpl>();
        this.variable_access_most_used = new ArrayList<CtVariableAccess>();
        this.field_access_most_used = new ArrayList<CtFieldAccess>();
        this.type_access_most_used = new ArrayList<CtTypeAccess>();
        this.typed_ele_most_used = new ArrayList<CtTypedElement>();
        this.invocation_most_used = new ArrayList<CtInvocation>();
        this.deleteStuff = new ArrayList<CtElement>();
        this.src_Method = null;
        this.flagtemp = false;
        this.flag_find = false;
        this.Name_Src_Mtd = Src_Mtd;
        this.path = path;
        boolean flag = false;
        final CtElement all_before = (CtElement)rootSpoonLeft.getChild(0).getMetadata("spoon_object");
        final List<CtMethod> methods_before = (List<CtMethod>)all_before.getElements((Filter)new TypeFilter((Class)CtMethod.class));
        for (int i = 0; i < methods_before.size(); ++i) {
            final CtMethod aMethod = methods_before.get(i);
            final String aName = aMethod.getSimpleName().toString();
            if (aName.equals(Src_Mtd)) {
                this.src_Method = (CtElement)aMethod.clone();
                flag = true;
                break;
            }
        }
        if (!flag) {
            final List<CtConstructor> con_before = (List<CtConstructor>)all_before.getElements((Filter)new TypeFilter((Class)CtConstructor.class));
            for (int j = 0; j < con_before.size(); ++j) {
                final CtConstructor aCon = con_before.get(j);
                final String aName2 = aCon.getSimpleName().toString();
                if (aName2.equals(Src_Mtd)) {
                    this.src_Method = (CtElement)aCon.clone();
                    flag = true;
                }
            }
        }
        if (!flag) {
            final List<CtConstructor> con_before = (List<CtConstructor>)all_before.getElements((Filter)new TypeFilter((Class)CtConstructor.class));
            if (con_before.get(0).getParent() instanceof CtClass) {
                final CtClass c = (CtClass)con_before.get(0).getParent();
                if (c.getSimpleName().toString().equals(Src_Mtd)) {
                    for (int k = 0; k < con_before.size(); ++k) {
                        final CtConstructor aCon2 = con_before.get(k);
                        final String aName3 = aCon2.getSimpleName().toString();
                        if (aName3.equals("<init>")) {
                            this.src_Method = (CtElement)aCon2;
                            flag = true;
                            break;
                        }
                    }
                }
            }
        }
        if (!flag) {
            System.out.println(": cannot find src_Method method: " + Src_Mtd);
        }
        if (flag) {
            this.flag_find = true;
        }
        final MappingStore mappingsComp = new MappingStore();
        final Matcher matcher = new CompositeMatchers.ClassicGumtree(rootSpoonLeft, rootSpoonLeft, mappingsComp);
        matcher.match();
        final ActionGenerator actionGenerator = new ActionGenerator(rootSpoonLeft, rootSpoonLeft, matcher.getMappings());
        actionGenerator.generate();
        final ActionClassifier actionClassifier = new ActionClassifier();
        this.allOperations = this.convertToSpoon(actionGenerator.getActions());
        this.rootOperations = this.convertToSpoon(actionClassifier.getRootActions(matcher.getMappingSet(), actionGenerator.getActions()));
        this._mappingsComp = mappingsComp;
        this.context = context;
        this.Name_Src_Mtd = Src_Mtd;
    }
    
    private List<Operation> convertToSpoon(List<Action> actions) {
		  return (List<Operation>)actions.stream().map(action -> {
		        if (action instanceof Insert) {
		          return (Operation)new InsertOperation((Insert)action);
		        }
		        if (action instanceof Delete) {
		          return (Operation)new DeleteOperation((Delete)action);
		        }
		        if (action instanceof Update)
		          return (Operation)new UpdateOperation((Update)action); 
		        if (action instanceof Move)
		          return (Operation)new MoveOperation((Move)action); 
		        throw new IllegalArgumentException("Please support the new type " + action.getClass());
		      }).collect(Collectors.toList());
    }
    
    @Override
    public List<Operation> getAllOperations() {
        return Collections.unmodifiableList((List<? extends Operation>)this.allOperations);
    }
    
    @Override
    public List<Operation> getRootOperations() {
        return Collections.unmodifiableList((List<? extends Operation>)this.rootOperations);
    }
    
    @Override
//    public List<Operation> getOperationChildren(final Operation operationParent, final List<Operation> rootOperations) {
//        return rootOperations.stream().filter(operation -> operation.getNode().getParent().equals(operationParent)).collect((Collector<? super Object, ?, List<Operation>>)Collectors.toList());
//    }
    public List<Operation> getOperationChildren(Operation operationParent, List<Operation> rootOperations) {
        return (List<Operation>)rootOperations.stream()
          .filter(operation -> operation.getNode().getParent().equals(operationParent))
          .collect(Collectors.toList());
    }
    
    @Override
    public CtElement changedNode() {
        if (this.rootOperations.size() != 1) {
            throw new IllegalArgumentException("Should have only one root action.");
        }
        return this.commonAncestor();
    }
    
    @Override
    public CtElement commonAncestor() {
        final List<CtElement> copy = new ArrayList<CtElement>();
        for (final Operation operation : this.rootOperations) {
            CtElement el = operation.getNode();
            if (operation instanceof InsertOperation) {
                el = (CtElement)this._mappingsComp.getSrc(operation.getAction().getNode().getParent()).getMetadata("spoon_object");
            }
            copy.add(el);
        }
        while (copy.size() >= 2) {
            final CtElement first = copy.remove(0);
            final CtElement second = copy.remove(0);
            copy.add(this.commonAncestor(first, second));
        }
        return copy.get(0);
    }
    
    private CtElement commonAncestor(CtElement first, final CtElement second) {
        while (first != null) {
            for (CtElement el = second; el != null; el = el.getParent()) {
                if (first == el) {
                    return first;
                }
            }
            first = first.getParent();
        }
        return null;
    }
    
    @Override
    public CtElement changedNode(final Class<? extends Operation> operationWanted) {
        final Optional<Operation> firstNode = this.rootOperations.stream().filter(operation -> operationWanted.isAssignableFrom(operation.getClass())).findFirst();
        if (firstNode.isPresent()) {
            return firstNode.get().getNode();
        }
        throw new NoSuchElementException();
    }
    
    @Override
    public boolean containsOperation(final OperationKind kind, final String nodeKind) {
        return this.rootOperations.stream().anyMatch(operation -> operation.getAction().getClass().getSimpleName().equals(kind.name()) && this.context.getTypeLabel(operation.getAction().getNode()).equals(nodeKind));
    }
    
    @Override
    public boolean containsOperation(final OperationKind kind, final String nodeKind, final String nodeLabel) {
        return this.containsOperations(this.getRootOperations(), kind, nodeKind, nodeLabel);
    }
    
    @Override
    public boolean containsOperations(final List<Operation> operations, final OperationKind kind, final String nodeKind, final String nodeLabel) {
        return operations.stream().anyMatch(operation -> operation.getAction().getClass().getSimpleName().equals(kind.name()) && this.context.getTypeLabel(operation.getAction().getNode()).equals(nodeKind) && operation.getAction().getNode().getLabel().equals(nodeLabel));
    }
    
    @Override
    public void debugInformation() {
        System.err.println(this.toDebugString());
    }
    
    private String toDebugString() {
        String result = "";
        for (final Operation operation : this.rootOperations) {
            final ITree node = operation.getAction().getNode();
            final CtElement nodeElement = operation.getNode();
            String label = "\"" + node.getLabel() + "\"";
            if (operation instanceof UpdateOperation) {
                label = label + " to \"" + ((Update)operation.getAction()).getValue() + "\"";
            }
            String nodeType = "CtfakenodeImpl";
            if (nodeElement != null) {
                nodeType = nodeElement.getClass().getSimpleName();
                nodeType = nodeType.substring(2, nodeType.length() - 4);
            }
            result = result + "\"" + operation.getAction().getClass().getSimpleName() + "\", \"" + nodeType + "\", " + label + " (size: " + node.getDescendants().size() + ")" + node.toTreeString();
        }
        System.out.println(result);
        return result;
    }
    
    public <T> List<T> getNewList(List<T> li) {
        li = (List<T>)filterNull((List<Object>)li);
        final List<T> list = new ArrayList<T>();
        for (int i = 0; i < li.size(); ++i) {
            boolean flag = false;
            final T str = li.get(i);
            for (int j = 0; j < list.size(); ++j) {
                if (list.get(j).toString().equals(str.toString())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                list.add(str);
            }
        }
        return list;
    }
    
    public static <T> List<T> minusList(final List<T> src, final List<T> deleted) {
        List<T> list = new ArrayList<T>((Collection<? extends T>)src);
        final List<T> list2 = new ArrayList<T>((Collection<? extends T>)deleted);
        for (int i = 0; i < list2.size(); ++i) {
            final T str = list2.get(i);
            final int index = contains2(list, str);
            if (index != -1) {
                list.remove(index);
            }
        }
        list = filterNull(list);
        return list;
    }
    
    public static <T> int contains2(final List<T> src, final T str) {
        for (final T var : src) {
            if (var.toString().equals(str.toString())) {
                return src.indexOf(var);
            }
        }
        return -1;
    }
    
    @Override
    public String toString() {
        if (this.flag_find) {
            this.print_file();
        }
        return "Finish Candidate Generation";
    }
    
    private static int min(final int one, final int two, final int three) {
        int min = one;
        if (two < min) {
            min = two;
        }
        if (three < min) {
            min = three;
        }
        return min;
    }
    
    public static int ld(final String str1, final String str2) {
        final int n = str1.length();
        final int m = str2.length();
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        final int[][] d = new int[n + 1][m + 1];
        for (int i = 0; i <= n; ++i) {
            d[i][0] = i;
        }
        for (int j = 0; j <= m; ++j) {
            d[0][j] = j;
        }
        for (int i = 1; i <= n; ++i) {
            final char ch1 = str1.charAt(i - 1);
            for (int j = 1; j <= m; ++j) {
                final char ch2 = str2.charAt(j - 1);
                int temp;
                if (ch1 == ch2) {
                    temp = 0;
                }
                else {
                    temp = 1;
                }
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }
    
    public static double sim(final String str1, final String str2) {
        final int ld = ld(str1, str2);
        return 1.0 - ld / (double)Math.max(str1.length(), str2.length());
    }
    
    public static <T> List<T> filterNull(final List<T> list) {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).toString().equals("")) {
                list.remove(i);
                --i;
            }
        }
        return list;
    }
    
    public static List<CtLiteral> filterNum(final List<CtLiteral> con_literal) {
        for (int i = 0; i < con_literal.size(); ++i) {
            if (!con_literal.get(i).toString().contains("\"")) {
                con_literal.remove(i);
                --i;
            }
        }
        return con_literal;
    }
    
    private List<CtBlock> getAllCandi(final CtBlock blk, final List<BlockNo> blockNos) {
        final List<CtBlock> candidates = new ArrayList<CtBlock>();
        int i = 0;
        final List<CtBlock> all_blocks = (List<CtBlock>)blk.getElements((Filter)new TypeFilter((Class)CtBlock.class));
        for (int j = 0; j < all_blocks.size(); ++j) {
            final CtBlock temp = all_blocks.get(j);
            for (int blk_size = temp.getStatements().size(), z = 0; z < blk_size; ++z) {
                for (int y = z; y < blk_size; ++y) {
                    final BlockNo bn = new BlockNo();
                    bn.blk_no = j;
                    bn.start_no = z;
                    bn.end_no = y;
                    blockNos.add(bn);
                    final CtBlock cblk = this.newBlock(blk);
                    for (int q = z; q <= y; ++q) {
                        cblk.addStatement(temp.getStatement(q));
                    }
                    candidates.add(cblk);
                    ++i;
                }
            }
        }
        return candidates;
    }
    
    public void print_file() {
        final String str1 = this.path + "test_features.csv";
        final String str2 = this.path + "test_feasibility.csv";
        System.out.println("hey!");
        final File file = new File(str1);
        final File file2 = new File(str2);
        CSVFormat format = null;
        CSVFormat format2 = null;
        if (file.exists()) {
            format = CSVFormat.DEFAULT.withHeader(new String[] { "No.Sample", "StartLine", "EndLine", "Source_Method", "Con_LOC", "CON_LOCAL", "CON_LITERAL", "CON_INVOCATION", "CON_IF", "CON_CONDITIONAL", "CON_SWITCH", "CON_VAR_ACC", "CON_TYPE_ACC", "CON_FIELD_ACC", "CON_ASSERT", "CON_ASSIGN", "CON_TYPED_ELE", "CON_PACKAGE", "LOC_Extracted_Method", "Num_local", "Num_Literal", "Num_Invocation", "Num_If", "Num_Conditional", "Num_Switch", "Num_Var_Ac", "Num_Type_Ac", "Num_Field_Ac", "Num_Assign", "Num_Typed_Ele", "Num_Package", "ratio_LOC", "Ratio_Variable_Access", "Ratio_Variable_Access2", "VarAc_Cohesion", "VarAc_Cohesion2", "Ratio_Field_Access", "Ratio_Field_Access2", "Field_Cohesion", "Field_Cohesion2", "Ratio_Invocation", "Invocation_Cohesion", "Ratio_Type_Access", "Ratio_Type_Access2", "TypeAc_Cohesion", "TypeAc_Cohesion2", "Ratio_Typed_Ele", "TypedEle_Cohesion", "Ratio_Package", "Ratio_Package2", "Package_Cohesion", "Package_Cohesion2", "BlockNumber", "StartNumber", "EndNumber" }).withSkipHeaderRecord();
        }
        else {
            format = CSVFormat.DEFAULT.withHeader(new String[] { "No.Sample", "StartLine", "EndLine", "Source_Method", "Con_LOC", "CON_LOCAL", "CON_LITERAL", "CON_INVOCATION", "CON_IF", "CON_CONDITIONAL", "CON_SWITCH", "CON_VAR_ACC", "CON_TYPE_ACC", "CON_FIELD_ACC", "CON_ASSERT", "CON_ASSIGN", "CON_TYPED_ELE", "CON_PACKAGE", "LOC_Extracted_Method", "Num_local", "Num_Literal", "Num_Invocation", "Num_If", "Num_Conditional", "Num_Switch", "Num_Var_Ac", "Num_Type_Ac", "Num_Field_Ac", "Num_Assign", "Num_Typed_Ele", "Num_Package", "ratio_LOC", "Ratio_Variable_Access", "Ratio_Variable_Access2", "VarAc_Cohesion", "VarAc_Cohesion2", "Ratio_Field_Access", "Ratio_Field_Access2", "Field_Cohesion", "Field_Cohesion2", "Ratio_Invocation", "Invocation_Cohesion", "Ratio_Type_Access", "Ratio_Type_Access2", "TypeAc_Cohesion", "TypeAc_Cohesion2", "Ratio_Typed_Ele", "TypedEle_Cohesion", "Ratio_Package", "Ratio_Package2", "Package_Cohesion", "Package_Cohesion2", "BlockNumber", "StartNumber", "EndNumber" });
        }
        if (file2.exists()) {
            format2 = CSVFormat.DEFAULT.withHeader(new String[] { "MethodName", "StartLine", "StartColumn", "EndLine", "EndColumn" }).withSkipHeaderRecord();
        }
        else {
            format2 = CSVFormat.DEFAULT.withHeader(new String[] { "MethodName", "StartLine", "StartColumn", "EndLine", "EndColumn" });
        }
        try (final Writer out = new FileWriter(str1, true);
             final CSVPrinter printer = new CSVPrinter((Appendable)out, format)) {
            CtBlock blk = null;
            if (this.src_Method instanceof CtMethod) {
                final CtMethod m = (CtMethod)this.src_Method;
                blk = m.getBody();
            }
            else if (this.src_Method instanceof CtConstructor) {
                final CtConstructor constr = (CtConstructor)this.src_Method;
                blk = constr.getBody();
            }
            try (final Writer out2 = new FileWriter(str2, true);
                 final CSVPrinter printer2 = new CSVPrinter((Appendable)out2, format2)) {
                final List<BlockNo> blk_Nos = new ArrayList<BlockNo>();
                final List<CtBlock> exhau_candi = this.getAllCandi(blk, blk_Nos);
                for (int kl = 0; kl < exhau_candi.size(); ++kl) {
                    final int BlockNumber = blk_Nos.get(kl).blk_no;
                    final int StartNumber = blk_Nos.get(kl).start_no;
                    final int EndNumber = blk_Nos.get(kl).end_no;
                    this.deleted = (CtElement)exhau_candi.get(kl);
                    this.extracted_Code = exhau_candi.get(kl);
                    final CtBlock cblk = exhau_candi.get(kl);
                    final int start = cblk.getStatement(0).getPosition().getLine();
                    final int getEndLine = cblk.getLastStatement().getPosition().getEndLine();
                    this.LOC_Extracted_Method = this.getLOC(cblk.toString());
                    final int LOC_Src = this.getLOC(blk.toString());
                    int Con_LOC = LOC_Src - this.LOC_Extracted_Method;
                    if (Con_LOC < 0) {
                        Con_LOC = 0;
                    }
                    if (this.LOC_Extracted_Method >= 3) {
                        printer2.printRecord(new Object[] { this.Name_Src_Mtd, start, 0, getEndLine + 1, 0 });
                        printer2.flush();
                    }
                    final int CON_LOCAL = minusList(blk.getElements((Filter)new TypeFilter((Class)CtLocalVariable.class)), (List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtLocalVariable.class))).size();
                    List<CtLiteral> con_literal = minusList(blk.getElements((Filter)new TypeFilter((Class)CtLiteral.class)), (List<CtLiteral>)this.deleted.getElements((Filter)new TypeFilter((Class)CtLiteral.class)));
                    con_literal = filterNull(con_literal);
                    con_literal = filterNum(con_literal);
                    final int CON_LITERAL = (con_literal.size() > 0) ? 1 : 0;
                    final int CON_ASSERT = minusList(blk.getElements((Filter)new TypeFilter((Class)CtAssert.class)), (List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtAssert.class))).size();
                    final int CON_INVOCATION = minusList(blk.getElements((Filter)new TypeFilter((Class)CtInvocation.class)), (List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtInvocation.class))).size();
                    final int CON_IF = minusList(blk.getElements((Filter)new TypeFilter((Class)CtIf.class)), (List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtIf.class))).size();
                    final int CON_CONDITIONAL = minusList(blk.getElements((Filter)new TypeFilter((Class)CtConditional.class)), (List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtConditional.class))).size();
                    final int CON_SWITCH = minusList(blk.getElements((Filter)new TypeFilter((Class)CtSwitch.class)), (List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtSwitch.class))).size();
                    final int CON_VAR_ACC = minusList(blk.getElements((Filter)new TypeFilter((Class)CtVariableAccess.class)), (List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtVariableAccess.class))).size();
                    final int CON_TYPE_ACC = minusList(blk.getElements((Filter)new TypeFilter((Class)CtTypeAccess.class)), (List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtTypeAccess.class))).size();
                    final int CON_FIELD_ACC = minusList(blk.getElements((Filter)new TypeFilter((Class)CtFieldAccess.class)), (List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtFieldAccess.class))).size();
                    final int CON_ASSIGN = minusList(blk.getElements((Filter)new TypeFilter((Class)CtAssignment.class)), (List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtAssignment.class))).size();
                    final int CON_TYPED_ELE = minusList(blk.getElements((Filter)new TypeFilter((Class)CtTypedElement.class)), (List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtTypedElement.class))).size();
                    final int CON_PACKAGE = minusList(blk.getElements((Filter)new TypeFilter((Class)CtPackageReferenceImpl.class)), (List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtPackageReferenceImpl.class))).size();
                    double ratio_LOC = 0.0;
                    final double[] temp_result = new double[2];
                    temp_result[0] = (temp_result[1] = 0.0);
                    this.Num_Variable = filterNull((List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtVariable.class))).size();
                    this.Num_local = filterNull((List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtLocalVariable.class))).size();
                    this.Num_Literal = ((filterNull((List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtLiteral.class))).size() > 0) ? 1 : 0);
                    this.Num_Invocation = filterNull((List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtInvocation.class))).size();
                    this.Num_If = filterNull((List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtIf.class))).size();
                    this.Num_Conditional = filterNull((List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtConditional.class))).size();
                    this.Num_Switch = filterNull((List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtSwitch.class))).size();
                    this.Num_Var_Ac = filterNull((List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtVariableAccess.class))).size();
                    this.Num_Type_Ac = filterNull((List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtTypeAccess.class))).size();
                    this.Num_Field_Ac = filterNull((List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtFieldAccess.class))).size();
                    this.Num_Assign = filterNull((List<Object>)this.deleted.getElements((Filter)new TypeFilter((Class)CtAssignment.class))).size();
                    if (LOC_Src > 0) {
                        ratio_LOC = this.LOC_Extracted_Method / (double)LOC_Src;
                    }
                    final int s = this.extracted_Code.getStatements().size();
                    this.delVarAcc = new ArrayList<CtVariableAccess>(this.deleted.getElements((Filter)new TypeFilter((Class)CtVariableAccess.class)));
                    this.srcVarAcc = new ArrayList<CtVariableAccess>(blk.getElements((Filter)new TypeFilter((Class)CtVariableAccess.class)));
                    final double Ratio_Variable_Access = this.ratio_u(temp_result, this.delVarAcc, this.srcVarAcc, this.variable_access_most_used);
                    final double Ratio_Variable_Access2 = temp_result[1];
                    double VarAc_Cohesion3;
                    double VarAc_Cohesion2 = VarAc_Cohesion3 = 0.0;
                    int n3;
                    int n2 = n3 = 0;
                    if (this.variable_access_most_used.size() == 1) {
                        for (int i = 0; i < s; ++i) {
                            final List<CtVariableAccess> t = (List<CtVariableAccess>)this.extracted_Code.getStatement(i).getElements((Filter)new TypeFilter((Class)CtVariableAccess.class));
                            if (contains2(t, this.variable_access_most_used.get(0)) != -1) {
                                ++n3;
                            }
                        }
                    }
                    else if (this.variable_access_most_used.size() == 2) {
                        for (int i = 0; i < s; ++i) {
                            final List<CtVariableAccess> t = (List<CtVariableAccess>)this.extracted_Code.getStatement(i).getElements((Filter)new TypeFilter((Class)CtVariableAccess.class));
                            if (contains2(t, this.variable_access_most_used.get(0)) != -1) {
                                ++n3;
                            }
                            if (contains2(t, this.variable_access_most_used.get(1)) != -1) {
                                ++n2;
                            }
                        }
                    }
                    VarAc_Cohesion3 = n3 / (double)s;
                    VarAc_Cohesion2 = n2 / (double)s;
                    this.delFieldAcc = new ArrayList<CtFieldAccess>(this.deleted.getElements((Filter)new TypeFilter((Class)CtFieldAccess.class)));
                    this.srcFieldAcc = new ArrayList<CtFieldAccess>(blk.getElements((Filter)new TypeFilter((Class)CtFieldAccess.class)));
                    final double Ratio_Field_Access = this.ratio_u(temp_result, this.delFieldAcc, this.srcFieldAcc, this.field_access_most_used);
                    final double Ratio_Field_Access2 = temp_result[1];
                    double Field_Cohesion3;
                    double Field_Cohesion2 = Field_Cohesion3 = 0.0;
                    n2 = (n3 = 0);
                    if (this.field_access_most_used.size() == 1) {
                        for (int j = 0; j < s; ++j) {
                            final List<CtFieldAccess> t2 = (List<CtFieldAccess>)this.extracted_Code.getStatement(j).getElements((Filter)new TypeFilter((Class)CtFieldAccess.class));
                            if (contains2(t2, this.field_access_most_used.get(0)) != -1) {
                                ++n3;
                            }
                        }
                    }
                    else if (this.field_access_most_used.size() == 2) {
                        for (int j = 0; j < s; ++j) {
                            final List<CtFieldAccess> t2 = (List<CtFieldAccess>)this.extracted_Code.getStatement(j).getElements((Filter)new TypeFilter((Class)CtFieldAccess.class));
                            if (contains2(t2, this.field_access_most_used.get(0)) != -1) {
                                ++n3;
                            }
                            if (contains2(t2, this.field_access_most_used.get(1)) != -1) {
                                ++n2;
                            }
                        }
                    }
                    Field_Cohesion3 = n3 / (double)s;
                    Field_Cohesion2 = n2 / (double)s;
                    this.delInvo = new ArrayList<CtInvocation>(this.deleted.getElements((Filter)new TypeFilter((Class)CtInvocation.class)));
                    this.srcInvo = new ArrayList<CtInvocation>(blk.getElements((Filter)new TypeFilter((Class)CtInvocation.class)));
                    final double Ratio_Invocation = this.ratio_u(temp_result, this.delInvo, this.srcInvo, this.invocation_most_used);
                    double Invocation_Cohesion = 0.0;
                    n2 = (n3 = 0);
                    if (this.invocation_most_used.size() > 0) {
                        for (int k = 0; k < s; ++k) {
                            final List<CtInvocation> t3 = (List<CtInvocation>)this.extracted_Code.getStatement(k).getElements((Filter)new TypeFilter((Class)CtInvocation.class));
                            if (contains2(t3, this.invocation_most_used.get(0)) != -1) {
                                ++n3;
                            }
                        }
                        Invocation_Cohesion = n3 / (double)s;
                    }
                    this.delTypeAcc = new ArrayList<CtTypeAccess>(this.deleted.getElements((Filter)new TypeFilter((Class)CtTypeAccess.class)));
                    this.srcTypeAcc = new ArrayList<CtTypeAccess>(blk.getElements((Filter)new TypeFilter((Class)CtTypeAccess.class)));
                    final double Ratio_Type_Access = this.ratio_u(temp_result, this.delTypeAcc, this.srcTypeAcc, this.type_access_most_used);
                    final double Ratio_Type_Access2 = temp_result[1];
                    double TypeAc_Cohesion3;
                    double TypeAc_Cohesion2 = TypeAc_Cohesion3 = 0.0;
                    n2 = (n3 = 0);
                    if (this.type_access_most_used.size() == 1) {
                        for (int l = 0; l < s; ++l) {
                            final List<CtTypeAccess> t4 = (List<CtTypeAccess>)this.extracted_Code.getStatement(l).getElements((Filter)new TypeFilter((Class)CtTypeAccess.class));
                            if (contains2(t4, this.type_access_most_used.get(0)) != -1) {
                                ++n3;
                            }
                        }
                    }
                    else if (this.type_access_most_used.size() == 2) {
                        for (int l = 0; l < s; ++l) {
                            final List<CtTypeAccess> t4 = (List<CtTypeAccess>)this.extracted_Code.getStatement(l).getElements((Filter)new TypeFilter((Class)CtTypeAccess.class));
                            if (contains2(t4, this.type_access_most_used.get(0)) != -1) {
                                ++n3;
                            }
                            if (contains2(t4, this.type_access_most_used.get(1)) != -1) {
                                ++n2;
                            }
                        }
                    }
                    TypeAc_Cohesion3 = n3 / (double)s;
                    TypeAc_Cohesion2 = n2 / (double)s;
                    this.delTypedEle = new ArrayList<CtTypedElement>(this.deleted.getElements((Filter)new TypeFilter((Class)CtTypedElement.class)));
                    this.srcTypedEle = new ArrayList<CtTypedElement>(blk.getElements((Filter)new TypeFilter((Class)CtTypedElement.class)));
                    final int Num_Typed_Ele = this.delTypedEle.size();
                    final double Ratio_Typed_Ele = this.ratio_u(temp_result, this.delTypedEle, this.srcTypedEle, this.typed_ele_most_used);
                    double TypedEle_Cohesion = 0.0;
                    n2 = (n3 = 0);
                    if (this.typed_ele_most_used.size() > 0) {
                        for (int i2 = 0; i2 < s; ++i2) {
                            final List<CtTypedElement> t5 = (List<CtTypedElement>)this.extracted_Code.getStatement(i2).getElements((Filter)new TypeFilter((Class)CtTypedElement.class));
                            if (contains2(t5, this.typed_ele_most_used.get(0)) != -1) {
                                ++n3;
                            }
                        }
                        TypedEle_Cohesion = n3 / (double)s;
                    }
                    final List<CtPackageReferenceImpl> delPackage = new ArrayList<CtPackageReferenceImpl>(this.deleted.getElements((Filter)new TypeFilter((Class)CtPackageReferenceImpl.class)));
                    final List<CtPackageReferenceImpl> srcPackage = new ArrayList<CtPackageReferenceImpl>(blk.getElements((Filter)new TypeFilter((Class)CtPackageReferenceImpl.class)));
                    final int Num_Package = delPackage.size();
                    final double Ratio_Package = this.ratio(temp_result, delPackage, srcPackage, this.package_most_used);
                    final double Ratio_Package2 = temp_result[1];
                    double Package_Cohesion3;
                    double Package_Cohesion2 = Package_Cohesion3 = 0.0;
                    n2 = (n3 = 0);
                    if (this.package_most_used.size() == 1) {
                        for (int i3 = 0; i3 < s; ++i3) {
                            final List<CtPackageReferenceImpl> t6 = (List<CtPackageReferenceImpl>)this.extracted_Code.getStatement(i3).getElements((Filter)new TypeFilter((Class)CtPackageReferenceImpl.class));
                            if (contains2(t6, this.package_most_used.get(0)) != -1) {
                                ++n3;
                            }
                        }
                    }
                    else if (this.package_most_used.size() == 2) {
                        for (int i3 = 0; i3 < s; ++i3) {
                            final List<CtPackageReferenceImpl> t6 = (List<CtPackageReferenceImpl>)this.extracted_Code.getStatement(i3).getElements((Filter)new TypeFilter((Class)CtPackageReferenceImpl.class));
                            if (contains2(t6, this.package_most_used.get(0)) != -1) {
                                ++n3;
                            }
                            if (contains2(t6, this.package_most_used.get(1)) != -1) {
                                ++n2;
                            }
                        }
                    }
                    Package_Cohesion3 = n3 / (double)s;
                    Package_Cohesion2 = n2 / (double)s;
                    if (this.LOC_Extracted_Method >= 3) {
                        printer.printRecord(new Object[] { 1, start, getEndLine + 1, this.Name_Src_Mtd, Con_LOC, CON_LOCAL, CON_LITERAL, CON_INVOCATION, CON_IF, CON_CONDITIONAL, CON_SWITCH, CON_VAR_ACC, CON_TYPE_ACC, CON_FIELD_ACC, CON_ASSERT, CON_ASSIGN, CON_TYPED_ELE, CON_PACKAGE, this.LOC_Extracted_Method, this.Num_local, this.Num_Literal, this.Num_Invocation, this.Num_If, this.Num_Conditional, this.Num_Switch, this.Num_Var_Ac, this.Num_Type_Ac, this.Num_Field_Ac, this.Num_Assign, Num_Typed_Ele, Num_Package, ratio_LOC, Ratio_Variable_Access, Ratio_Variable_Access2, VarAc_Cohesion3, VarAc_Cohesion2, Ratio_Field_Access, Ratio_Field_Access2, Field_Cohesion3, Field_Cohesion2, Ratio_Invocation, Invocation_Cohesion, Ratio_Type_Access, Ratio_Type_Access2, TypeAc_Cohesion3, TypeAc_Cohesion2, Ratio_Typed_Ele, TypedEle_Cohesion, Ratio_Package, Ratio_Package2, Package_Cohesion3, Package_Cohesion2, BlockNumber, StartNumber, EndNumber });
                        printer.flush();
                        System.out.println("printer.flush");
                    }
                    this.package_most_used.clear();
                    this.typed_ele_most_used.clear();
                    this.type_access_most_used.clear();
                    this.invocation_most_used.clear();
                    this.field_access_most_used.clear();
                    this.variable_access_most_used.clear();
                }
                printer2.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            printer.close();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }
    
    private int getLOC(final CtBlock cblk) {
        final int start = cblk.getStatement(0).getPosition().getLine();
        final int getEndLine = cblk.getLastStatement().getPosition().getEndLine();
        return getEndLine - start + 1;
    }
    
    private int getLOC(final String cblk) {
        int lines = 0;
        final char[] ch = cblk.toCharArray();
        for (int i = 0; i < ch.length; ++i) {
            if (ch[i] == '\n') {
                ++lines;
            }
        }
        return lines;
    }
    
    private CtBlock newBlock(final CtBlock blk) {
        final CtBlock cblk = blk.clone();
        for (int y = 0; y < cblk.getStatements().size(); --y, ++y) {
            cblk.removeStatement(cblk.getStatement(y));
        }
        return cblk;
    }
    
    private double ratio(final double[] ratio, final List<CtPackageReferenceImpl> delPackage, final List<CtPackageReferenceImpl> srcPackage, final List<CtPackageReferenceImpl> pkg) {
        final List<CtPackageReferenceImpl> delPackage2 = this.getNewList(delPackage);
        final int size = delPackage2.size();
        final double[] all_ratios = new double[size];
        for (int i = 0; i < size; ++i) {
            final CtPackageReferenceImpl temp = delPackage2.get(i);
            final int num1 = this.getFrequency(delPackage, temp);
            final int num2 = this.getFrequency(srcPackage, temp);
            if (num2 != 0) {
                all_ratios[i] = num1 / (double)num2;
            }
            else {
                all_ratios[i] = 0.0;
            }
        }
        double result = 0.0;
        double result2 = 0.0;
        int index = -1;
        int index2 = -1;
        for (int j = 0; j < size; ++j) {
            if (all_ratios[j] > result) {
                index2 = index;
                index = j;
                result2 = result;
                result = all_ratios[j];
            }
        }
        if (index != -1) {
            for (int j = 0; j < size; ++j) {
                if (all_ratios[j] == result && j != index && this.getFrequency(delPackage, delPackage2.get(j)) > this.getFrequency(delPackage, delPackage2.get(index))) {
                    index2 = index;
                    index = j;
                    result2 = result;
                    result = all_ratios[j];
                }
            }
        }
        if (index2 != -1) {
            for (int j = 0; j < size; ++j) {
                if (all_ratios[j] == result2 && j != index2 && j != index && this.getFrequency(delPackage, delPackage2.get(j)) > this.getFrequency(delPackage, delPackage2.get(index2))) {
                    index2 = j;
                    result2 = all_ratios[j];
                }
            }
        }
        if (index != -1) {
            pkg.add(delPackage2.get(index));
        }
        if (index2 != -1) {
            pkg.add(delPackage2.get(index2));
        }
        ratio[0] = ((result > 1.0) ? 1.0 : result);
        ratio[1] = ((result2 > 1.0) ? 1.0 : result2);
        return result;
    }
    
    private <T> int getFrequency(final List<T> list, final T t) {
        int num = 0;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).toString().equals(t.toString())) {
                ++num;
            }
        }
        return num;
    }
    
    private <T> double ratio_u(final double[] ratio, final List<T> delPackage, final List<T> srcPackage, final List<T> pkg) {
        final List<T> delPackage2 = this.getNewList(delPackage);
        final int size = delPackage2.size();
        final double[] all_ratios = new double[size];
        for (int i = 0; i < size; ++i) {
            final T temp = delPackage2.get(i);
            final int num1 = this.getFrequency(delPackage, temp);
            final int num2 = this.getFrequency(srcPackage, temp);
            if (num2 != 0) {
                all_ratios[i] = num1 / (double)num2;
            }
            else {
                all_ratios[i] = 0.0;
            }
        }
        double result = 0.0;
        double result2 = 0.0;
        int index = -1;
        int index2 = -1;
        for (int j = 0; j < size; ++j) {
            if (all_ratios[j] > result) {
                index2 = index;
                index = j;
                result2 = result;
                result = all_ratios[j];
            }
        }
        if (index != -1) {
            for (int j = 0; j < size; ++j) {
                if (all_ratios[j] == result && j != index && this.getFrequency(delPackage, delPackage2.get(j)) > this.getFrequency(delPackage, delPackage2.get(index))) {
                    index2 = index;
                    index = j;
                    result2 = result;
                    result = all_ratios[j];
                }
            }
        }
        if (index2 != -1) {
            for (int j = 0; j < size; ++j) {
                if (all_ratios[j] == result2 && j != index2 && j != index && this.getFrequency(delPackage, delPackage2.get(j)) > this.getFrequency(delPackage, delPackage2.get(index2))) {
                    index2 = j;
                    result2 = all_ratios[j];
                }
            }
        }
        if (index != -1) {
            pkg.add(delPackage2.get(index));
        }
        if (index2 != -1) {
            pkg.add(delPackage2.get(index2));
        }
        ratio[0] = ((result > 1.0) ? 1.0 : result);
        ratio[1] = ((result2 > 1.0) ? 1.0 : result2);
        return result;
    }
}
